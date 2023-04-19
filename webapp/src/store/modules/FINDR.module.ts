import type {Module} from "vuex";
import type {Media} from "@/store/util";
import type {ComponentCustomProperties} from "@/store/util";
import {endpoints} from "@/store/endpoints";
import {myFetch} from "@/store/util";
import {store} from "@/store/store";
import {map} from "@/store/util";

export const FINDRModule: Module<State, ComponentCustomProperties> = {
    namespaced: true,
    state: {
        FINDR: false,
        likedMedia: [],
        dislikedMedia: [],
        FINDRMedia: [],
        FINDRCardMedia: [],
    },
    getters: {
        getFINDR: (state: State) => state.FINDR,
        getLikedMedia: (state: State) => state.likedMedia,
        getDislikedMedia: (state: State) => state.dislikedMedia,
        getFINDRMedia: (state: State) => state.FINDRMedia,
        getFINDRCardMedia: (state: State) => state.FINDRCardMedia,
    },
    mutations: {
        setFINDR: (state: State, FINDR: boolean) => {
            state.FINDR = FINDR;
        },

        setLikedMedia: (state: State, likedMedia: Media[]) => state.likedMedia = likedMedia,
        addLikedMedia: (state: State, likedMedia: Media) => state.likedMedia.push(likedMedia),
        removeLikedMedia: (state: State, likedMedia: Media) => {
            state.likedMedia.splice(state.likedMedia.indexOf(likedMedia), 1);
            store.dispatch("FINDR/updateFINDRCardMedia").then(r => r);
        },
        clearLikedMedia: (state: State) => state.likedMedia = [],

        setDislikedMedia: (state: State, dislikedMedia: Media[]) => state.dislikedMedia = dislikedMedia,
        addDislikedMedia: (state: State, dislikedMedia: Media) => state.dislikedMedia.push(dislikedMedia),
        removeDislikedMedia: (state: State, dislikedMedia: Media) => state.dislikedMedia = state.dislikedMedia.filter((media: Media) => media.id !== dislikedMedia.id),
        clearDislikedMedia: (state: State) => state.dislikedMedia = [],

        setFINDRMedia: (state: State, FINDRMedia: Media[]) => state.FINDRMedia = FINDRMedia,
        setFINDRCardMedia: (state: State, FINDRCardMedia: Media[]) => state.FINDRCardMedia = FINDRCardMedia,
        addFINDRCardMedia: (state: State, FINDRCardMedia: Media) => state.FINDRCardMedia.push(FINDRCardMedia),
        removeFINDRCardMedia: (state: State, FINDRCardMedia: Media) => state.FINDRCardMedia = state.FINDRCardMedia.filter((media: Media) => media.id !== FINDRCardMedia.id),
    },
    actions: {
        async updateFINDRResults({commit, state}): Promise<void> {
            store.commit("setResults", []) //Reset results
            let results: Media[] = [];

            let weights = weightFINDRChoices(state.likedMedia, state.dislikedMedia);
            let mustGenres = [];
            let mustNotGenres = [];
            for (let [key, value] of weights) {
                if (value >= .5) {
                    mustGenres.push(key);
                } else {
                    mustNotGenres.push(key);
                }
            }

            // Do not show already liked or disliked media
            let excludedMovies: string[] = [];
            excludedMovies.push(...state.likedMedia.map((media: Media) => media.id));
            excludedMovies.push(...state.dislikedMedia.map((media: Media) => media.id));

            let params = new Map<string, string>([
                ["mustGenres", mustGenres.slice(0, 2).join(",")],
                ["mustNotGenres", mustNotGenres.slice(0, 2).join(",")],
                ["excludedIds", excludedMovies.join(",")],
            ]);

            console.log(params);

            results = await myFetch(endpoints.API_GENRES, params);

            // If no results are found, try again with a less strict search
            if (results.length === 0) {
                results = await myFetch(endpoints.API_GENRES, new Map<string, string>([
                    ["mustGenres", mustGenres.join(",")],
                    ["mustNotGenres", ""],
                    ["excludedIds", excludedMovies.join(",")],
                ]));
            }

            // If no results are found, try again with a less strict search
            if (results.length === 0) {
                results = await myFetch(endpoints.API_GENRES, new Map<string, string>([
                    ["mustGenres", ""],
                    ["mustNotGenres", ""],
                    ["excludedIds", excludedMovies.join(",")],
                ]));
            }

            // Sort results by year and rating
            results.sort((a, b) => {
                if (b.startYear == a.startYear) {
                    return b.averageRating - a.averageRating;
                }
                return b.startYear - a.startYear;
            });

            // Add one of the results to FINDR Stack
            for (let result of results) {
                if (!(state.FINDRCardMedia.includes(result))) {
                    state.FINDRCardMedia.push(result);
                    break;
                }
            }

            store.commit("setResults", results);
        },
        invertFINDR: ({commit, state}) => {
            commit("setFINDR", !state.FINDR);
        }
    }
}


// INTERFACES

interface State {
    FINDR: boolean,
    likedMedia: Media[],
    dislikedMedia: Media[],
    FINDRMedia: Media[],
    FINDRCardMedia: Media[],
}

// METHODS

function weightProperty(liked: Media[], disliked: Media[], properties: string, property: string, positiveWeight: number, negativeWeight: number): Map<string, number> {
    let weights = new Map<string, number>();

    for (let media of liked) {
        // @ts-ignore
        for (property of media[properties]) {
            if (weights.has(property)) {
                // @ts-ignore
                weights.set(property, weights.get(property) * positiveWeight); //Do not change it
            } else {
                weights.set(property, 1);
            }
        }
    }

    for (let media of disliked) {
        // @ts-ignore
        for (property of media[properties]) {
            if (weights.has(property)) {
                // @ts-ignore
                weights.set(property, weights.get(property) * negativeWeight);
            } else {
                weights.set(property, .25);
            }
        }
    }

    const sortedWeights: Map<string, number> = new Map([...weights.entries()].sort((a, b) => b[1] - a[1]));

    let max = Math.max(...sortedWeights.values());
    let min = Math.min(...sortedWeights.values());

    Object.keys(sortedWeights).forEach(function (key: string) {
        sortedWeights.set(key, map(sortedWeights.get(key) as number, min, max, 0, 1));
    });

    return sortedWeights;
}

function weightFINDRChoices(liked: Media[], disliked: Media[]): Map<string, number> {
    let genreWeights = weightProperty(liked, disliked, "genres", "genre", 1.5, .75);

    //TODO: In the future, we can add more properties to weight.
    //let typeWeights = weightProperty(liked, disliked, "types", "type", 1.1, .9);
    //let directorWeights = weightProperty(liked, disliked, "directors", "director", 1.1, .9);
    //let starringWeights = weightProperty(liked, disliked, "starring", "starring", 1.1, .9)
    //return [genreWeights, typeWeights, directorWeights, starringWeights];

    console.log(genreWeights);
    return genreWeights;
}
