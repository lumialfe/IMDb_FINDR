import type {Commit, Dispatch, Module, Store} from "vuex";
import type {Media} from "@/store/interfaces";

interface State {
    FINDR: boolean,
    likedMedia: Media[],
    dislikedMedia: Media[],
    FINDRMedia: Media[],
}

interface ComponentCustomProperties {
    $store: Store<State> | Store<Commit> | Store<Dispatch>
}

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
