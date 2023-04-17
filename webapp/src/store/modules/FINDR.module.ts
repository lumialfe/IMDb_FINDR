import type {Module} from "vuex";
import type {Media} from "@/store/util";
import type {ComponentCustomProperties} from "@/store/util";
import {endpoints} from "@/store/endpoints";
import {myFetch} from "@/store/util";
import {store} from "@/store/store";

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
        removeLikedMedia: (state: State, likedMedia: Media) => state.likedMedia = state.likedMedia.filter((media: Media) => media.id !== likedMedia.id),
        clearLikedMedia: (state: State) => state.likedMedia = [],

        setDislikedMedia: (state: State, dislikedMedia: Media[]) => state.dislikedMedia = dislikedMedia,
        addDislikedMedia: (state: State, dislikedMedia: Media) => state.dislikedMedia.push(dislikedMedia),
        removeDislikedMedia: (state: State, dislikedMedia: Media) => state.dislikedMedia = state.dislikedMedia.filter((media: Media) => media.id !== dislikedMedia.id),
        clearDislikedMedia: (state: State) => state.dislikedMedia = [],

        setFINDRMedia: (state: State, FINDRMedia: Media[]) => state.FINDRMedia = FINDRMedia,
        setFINDRCardMedia: (state: State, FINDRCardMedia: Media[]) => state.FINDRCardMedia = FINDRCardMedia,
        addFINDRCardMedia: (state: State, FINDRCardMedia: Media) => state.FINDRCardMedia.push(FINDRCardMedia),
    },
    actions: {
        async updateFINDRResults({commit, state}): Promise<Media[]> {
            store.commit("setResults", []);

            let results: Media[] = [];
            // Calculate weights for each media type
            console.log("STARTING FINDR");
            let weights = weightFINDRChoices(state.likedMedia, state.dislikedMedia);
            let i: number = 0;
            for (let [key, value] of weights) {
                if (i < 3 && value > 0.5) {
                    let aux = await myFetch(
                        endpoints.API_FILTERS,
                        new Map<string, string>([
                            ["minYear", "0"],
                            ["maxYear", "0"],
                            ["maxRuntimeMin", "0"],
                            ["minRuntimeMin", "0"],
                            ["minAvgRating", "0"],
                            ["maxAvgRating", "0"],
                            ["type", "movie"],
                            ["genres", [key].toString()],

                        ]));
                    aux.sort((a: Media, b: Media) => b.averageRating - a.averageRating);
                    results = results.concat(aux.slice(0, (3 - i) * 5));
                    i++;
                } else {
                    break;
                }
            }
            console.log(weights);
            console.log(results);

            for (let result of results) {
                if (state.dislikedMedia.includes(result)) {
                    results.splice(results.indexOf(result), 1);
                }
            }

            console.log("FINDR DONE");
            //TODO: Fix repeated FINDR media in stack

            //commit("setFINDRMedia", results);
            store.commit("setResults", results);
            for (let i = 0; i < results.length; i++) {
                if (!state.likedMedia.includes(results[i])) {
                    commit("addFINDRCardMedia", results[i]);
                    break;
                }
            }
            commit("addFINDRCardMedia", results[0]);
            return results;
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
                weights.set(property, .75);
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

    const sortedWeights = new Map([...weights.entries()].sort((a, b) => b[1] - a[1]));

    let max = Math.max(...sortedWeights.values());
    let min = Math.min(...sortedWeights.values());

    Object.keys(sortedWeights).forEach(function (key) {
        // @ts-ignore
        sortedWeights[key] = map(sortedWeights[key], min, max, 0, 1);
    });

    return sortedWeights;
}

function weightFINDRChoices(liked: Media[], disliked: Media[]): Map<string, number> {
    let genreWeights = weightProperty(liked, disliked, "genres", "genre", 1, .9);

    //TODO: In the future, we can add more properties to weight.
    //let typeWeights = weightProperty(liked, disliked, "types", "type", 1.1, .9);
    //let directorWeights = weightProperty(liked, disliked, "directors", "director", 1.1, .9);
    //let starringWeights = weightProperty(liked, disliked, "starring", "starring", 1.1, .9)
    //return [genreWeights, typeWeights, directorWeights, starringWeights];

    return genreWeights;
}
