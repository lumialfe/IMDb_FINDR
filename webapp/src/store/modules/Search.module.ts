import type {ComponentCustomProperties} from "../util";
import type {Media} from "../util";
import {endpoints} from "@/store/endpoints";
import type {Module} from "vuex";
import {myFetch} from "@/store/util";


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

async function fetchByTitle(query: string, type: string): Promise<Media[]> {
    let params = new Map<string, string>([
        ["title", query],
        ["type", type],
    ]);
    return await myFetch(endpoints.API_TITLE, params);
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
    },
    getters: {
        getResults: (state: State) => state.results,
        getTrending: (state: State) => state.trending,
        getNew: (state: State) => state.new,
        getTopAllTime: (state: State) => state.topAllTime,
        getNotToWatch: (state: State) => state.notToWatch,
    },
    mutations: {
        setResults: (state: State, results: Media[]) => state.results = results,
        setTrending: (state: State, trending: Media[]) => state.trending = trending,
        setNew: (state: State, newMedia: Media[]) => state.new = newMedia,
        setTopAllTime: (state: State, topAllTime: Media[]) => state.topAllTime = topAllTime,
        setNotToWatch: (state: State, notToWatch: Media[]) => state.notToWatch = notToWatch,
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
            let dropdown: HTMLSelectElement = (document.getElementById("media-type") as HTMLSelectElement);
            let searchBar: HTMLInputElement = (document.getElementById("media-query") as HTMLInputElement);
            if (dropdown && searchBar) {
                let type: string = dropdown.value;
                let query: string = searchBar.value;
                if (query.length > 2) {
                    console.log("searching " + type + ": " + query);
                    let results: Media[] = await fetchByTitle(query, type);

                    // Sort results by year and rating
                    results.sort((a, b) => {
                        if (b.startYear == a.startYear) {
                            return b.averageRating - a.averageRating;
                        }
                        return b.startYear - a.startYear;
                    });

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
        },
    }
}

// INTERFACES

interface State {
    results: Media[],
    trending: Media[],
    new: Media[],
    topAllTime: Media[],
    notToWatch: Media[],
}