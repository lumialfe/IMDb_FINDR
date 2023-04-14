import type {Commit, Dispatch, Module, Store} from "vuex";
import type {Media} from "@/store/interfaces";

export const FINDRModule: Module<State, ComponentCustomProperties> = {
    namespaced: true,
    state: {
        FINDR: false,
        likedMedia: [],
        dislikedMedia: [],
        FINDRMedia: [],
    },
    getters: {
        getFINDR: (state: State) => state.FINDR,
        getLikedMedia: (state: State) => state.likedMedia,
        getDislikedMedia: (state: State) => state.dislikedMedia,
        getFINDRMedia: (state: State) => state.FINDRMedia,
    },
    mutations: {
        setFINDR: (state: State, FINDR: boolean) => state.FINDR = FINDR,
        invertFINDR: (state: State) => state.FINDR = !state.FINDR,

        setLikedMedia: (state: State, likedMedia: Media[]) => state.likedMedia = likedMedia,
        addLikedMedia: (state: State, likedMedia: Media) => state.likedMedia.push(likedMedia),
        removeLikedMedia: (state: State, likedMedia: Media) => state.likedMedia = state.likedMedia.filter((media: Media) => media.id !== likedMedia.id),
        clearLikedMedia: (state: State) => state.likedMedia = [],

        setDislikedMedia: (state: State, dislikedMedia: Media[]) => state.dislikedMedia = dislikedMedia,
        addDislikedMedia: (state: State, dislikedMedia: Media) => state.dislikedMedia.push(dislikedMedia),
        removeDislikedMedia: (state: State, dislikedMedia: Media) => state.dislikedMedia = state.dislikedMedia.filter((media: Media) => media.id !== dislikedMedia.id),
        clearDislikedMedia: (state: State) => state.dislikedMedia = [],

        setFINDRMedia: (state: State, FINDRMedia: Media[]) => state.FINDRMedia = FINDRMedia,
    },
    actions: {
        async updateFINDRResults({commit, state}): Promise<Media[]> {
            let results: Media[] = [];
            // Calculate weights for each media type
            console.log("STARTING FINDR");
            let weights = weightFINDRChoices(state.likedMedia, state.dislikedMedia);
            console.log(weights);
            console.log("FINDR DONE");
            // @ts-ignore
            commit("setResults", results);
            return results;
        },
    }
}

// INTERFACES

interface State {
    FINDR: boolean,
    likedMedia: Media[],
    dislikedMedia: Media[],
    FINDRMedia: Media[],
}

interface ComponentCustomProperties {
    $store: Store<State> | Store<Commit> | Store<Dispatch>
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
                weights.set(property, .5);
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

function weightFINDRChoices(liked: Media[], disliked: Media[]): Map<string, number>[] {
    let genreWeights = weightProperty(liked, disliked, "genres", "genre", 1.1, .9);

    //TODO: In the future, we can add more properties to weight.
    //let typeWeights = weightProperty(liked, disliked, "types", "type", 1.1, .9);
    //let directorWeights = weightProperty(liked, disliked, "directors", "director", 1.1, .9);
    //let starringWeights = weightProperty(liked, disliked, "starring", "starring", 1.1, .9)
    //return [genreWeights, typeWeights, directorWeights, starringWeights];

    return [genreWeights];
}
