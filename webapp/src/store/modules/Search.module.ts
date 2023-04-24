import type {ComponentCustomProperties} from "../util";
import type {Media} from "../util";
import {endpoints} from "@/store/endpoints";
import type {Module} from "vuex";
import {myFetch} from "@/store/util";
import {store} from "@/store/store";
import {movieTypes} from "../util";


async function fetchTrending(): Promise<Media[]> {
    let params = new Map<string, string>([
        ["year", (new Date().getFullYear() - 1).toString()],
        ["size", "20"],
    ]);
    return await myFetch(endpoints.API_RECOMMENDED, params);
}

async function fetchTopAllTime(): Promise<Media[]> {
    return await myFetch(endpoints.API_TOP);
}

async function fetchNotToWatch(): Promise<Media[]> {
    return await myFetch(endpoints.API_NOT_TO_WATCH);
}

async function fetchNew(): Promise<Media[]> {
    let params = new Map<string, string>([
        ["year", (new Date().getFullYear()).toString()],
        ["size", "20"],
    ]);
    return await myFetch(endpoints.API_RECOMMENDED, params);
}

async function fetchByTitle(query: string, type: string, images: boolean = true): Promise<Media[]> {
    let params = new Map<string, string>([
        ["title", query],
        ["type", type],
    ]);
    return await myFetch(endpoints.API_TITLE, params, images);
}

// Executed when the page is loaded.
let trendingMedia: Media[] = (await fetchTrending()).sort((n1, n2) => n2.averageRating - n1.averageRating);
let newMedia: Media[] = await fetchNew();
if (newMedia.length < 20) {
    newMedia.sort((n1, n2) => n2.averageRating - n1.averageRating).push(...trendingMedia.sort((n1, n2) => n2.averageRating - n1.averageRating).slice(0, 20 - newMedia.length));
}
let topAllTimeMedia: Media[] = (await fetchTopAllTime()).sort((a, b) => b.averageRating - a.averageRating).slice(0, 20);
let notToWatchMedia: Media[] = (await fetchNotToWatch()).slice(0, 20).sort((n1, n2) => n2.startYear - n1.startYear);

export const SearchModule: Module<State, ComponentCustomProperties> = {
    state: {
        results: [],
        trending: [],
        new: [],
        topAllTime: [],
        notToWatch: [],
        preResults: new Map<string, boolean>(),
        searching: false,
    },
    getters: {
        getResults: (state: State) => state.results,
        getTrending: (state: State) => state.trending,
        getNew: (state: State) => state.new,
        getTopAllTime: (state: State) => state.topAllTime,
        getNotToWatch: (state: State) => state.notToWatch,
        getSearching: (state: State) => state.searching,
        getPreResults: (state: State) => state.preResults,
    },
    mutations: {
        setResults: (state: State, results: Media[]) => state.results = results,
        setTrending: (state: State, trending: Media[]) => state.trending = trending,
        setNew: (state: State, newMedia: Media[]) => state.new = newMedia,
        setTopAllTime: (state: State, topAllTime: Media[]) => state.topAllTime = topAllTime,
        setNotToWatch: (state: State, notToWatch: Media[]) => state.notToWatch = notToWatch,
        setSearching: (state: State, searching: boolean) => state.searching = searching,
        setPreResults: (state: State, preResults: Map<string, boolean>) => state.preResults = preResults,
    },
    actions: {
        load({commit}) {
            commit("setTrending", trendingMedia);
            commit("setNew", newMedia);
            commit("setTopAllTime", topAllTimeMedia);
            commit("setNotToWatch", notToWatchMedia);
            commit("FINDR/addFINDRCardMedia", trendingMedia[0], {root: true});
        },
        async search({commit}): Promise<void> {
            store.commit("setPreResults", []);
            commit("setSearching", true);

            let dropdown: HTMLSelectElement = (document.getElementById("media-type") as HTMLSelectElement);
            let searchBar: HTMLInputElement = (document.getElementById("media-query") as HTMLInputElement);
            if (dropdown && searchBar) {
                let type: string = dropdown.value;
                let query: string = searchBar.value;
                if (query.length > 2) {
                    console.log("searching " + type + ": " + query);
                    let results: Media[] = await fetchByTitle(query, type);

                    // Re-sort results with undefined or null poster paths
                    let resultsWOImages: Media[] = [];
                    results.forEach((media: Media) => {
                        let flag: boolean | undefined = (media.posterPath?.includes("undefined") || media.posterPath?.includes("null"));
                        if (flag) {
                            resultsWOImages.push(results.splice(results.indexOf(media), 1)[0]);
                        }
                    });
                    results.push(...resultsWOImages);

                    if (results.length === 0) {
                        (document.getElementById("media-query") as HTMLInputElement).style.border = "2px solid red";
                    } else {
                        (document.getElementById("media-query") as HTMLInputElement).style.border = "none";

                        // Set the first result as the FINDR card media
                        commit("FINDR/setFINDRCardMedia", [], {root: true});
                        commit("FINDR/addFINDRCardMedia", results[0], {root: true});
                    }

                    commit("setResults", results);
                } else {
                    commit("setResults", []);
                }
            }
            store.commit("setSearching", false);
        },
        async preSearch({commit}): Promise<void> {
            let dropdown: HTMLSelectElement = (document.getElementById("media-type") as HTMLSelectElement);
            let searchBar: HTMLInputElement = (document.getElementById("media-query") as HTMLInputElement);
            if (dropdown && searchBar) {
                let type: string = dropdown.value;
                let query: string = searchBar.value;
                console.log("pre-searching " + type + ": " + query);
                let results: Media[] = await fetchByTitle(query, type, false);
                let resultNames: Map<string, boolean> = new Map<string, boolean>();
                for (let result of results.slice(0, 5)) {
                    resultNames.set(result.title, movieTypes.includes(result.type));
                }

                if (results.length === 0) {
                    (document.getElementById("media-query") as HTMLInputElement).style.border = "2px solid red";
                    commit("setPreResults", new Map<string, boolean>());
                } else {
                    (document.getElementById("media-query") as HTMLInputElement).style.border = "none";
                    commit("setPreResults", resultNames);
                }

            }
        }
    }
}

// INTERFACES

interface State {
    results: Media[],
    trending: Media[],
    new: Media[],
    topAllTime: Media[],
    notToWatch: Media[],
    preResults: Map<string, boolean>,
    searching: boolean,
}