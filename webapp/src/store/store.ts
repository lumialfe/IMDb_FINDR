import {
    createStore,
    Store,
} from "vuex";

interface State {
    FINDR: boolean,
    likedMedia: Media[],
    dislikedMedia: Media[],
    FINDRMedia: Media[],
    results: Media[],
    trending: Media[],
    new: Media[],
}

const movieTypes = ["short", "movie", "tvMovie", "tvShort"];
const tvTypes = ["tvSeries", "tvMiniSeries", "tvSpecial"];

async function fetchTrending(): Promise<Media[]> {
    let baseURL = "http://localhost:8080/imdb/_search/";
    let url = baseURL + "recommended" + "?year=" + (new Date().getFullYear() - 1) + "&size=20";
    return await fetchMedia(url);
}

async function fetchNew(): Promise<Media[]> {
    let baseURL = "http://localhost:8080/imdb/_search/";
    let url = baseURL + "recommended" + "?year=" + (new Date().getFullYear()) + "&size=20";
    return await fetchMedia(url);
}

async function fetchByTitle(query: string, type: string): Promise<Media[]> {
    let baseURL = "http://localhost:8080/imdb/_search/";
    let url = baseURL + "title" + "?title=" + query + "&type=" + type;
    return await fetchMedia(url);
}

async function fetchMedia(url: string,): Promise<Media[]> {
    let ret: Media[] = [];
    await fetch(url).then((response) => response.json())
        .then(async (data) => {
            console.log(data);

            for (let media of data) {
                let rating = data.vote_average;

                let m: Media = {
                    id: media.tconst,
                    title: media.primaryTitle,
                    genres: media.genres,
                    averageRating: rating === undefined ? -1 : rating.toString().substring(0, 3),
                    type: media.titleType,
                    startYear: media.startYear,
                    isAdult: media.isAdult,
                    directors: media.directors,
                    starring: media.starrings,
                };
                await fetchMovieData(m);
                ret.push(m);
            }
        }).catch((ex) => {
            console.log(ex); // Log Exception on console.
        });

    return ret;
}

async function fetchMovieData(media: Media) {
    let baseURL = "https://api.themoviedb.org/3/find/";
    const apiKEY = "89d117037278a5d054a427790b60933e";
    let url = baseURL + media.id + "?api_key=" + apiKEY + "&language=en-US&external_source=imdb_id";

    await fetch(url).then(response => response.json()).then(async data => {
        console.log(data);

        if (data.movie_results.length) {
            data = data.movie_results[0];
        } else if (data.tv_results.length) {
            data = data.tv_results[0];
        }

        media.posterPath = "https://image.tmdb.org/t/p/w500" + data.poster_path;
        media.backdropPath = "https://image.tmdb.org/t/p/w500" + data.backdrop_path;
        media.overview = data.overview;
        if (media.averageRating === -1) {
            let rating = data.vote_average;
            media.averageRating = rating === undefined ? media.averageRating : rating.toString().substring(0, 3);
        }
        media.imdbLink = "https://www.imdb.com/title/" + media.id;

        let baseURL = "https://api.themoviedb.org/3/";
        movieTypes.includes(media.type as string) ? baseURL += "movie/" : baseURL += "tv/";

        await fetch(baseURL + data.id + "/videos?api_key=" + apiKEY + "&language=en-US" + data.id).then(response => response.json()).then(data => {
            for (let i = 0; i < data.results.length; i++) {
                if (data.results[i].site === "YouTube" && data.results[i].name.toLowerCase().includes("trailer")) {
                    media.trailer = "https://www.youtube.com/embed/" + data.results[i].key;
                    break;
                } else {
                    media.trailer = "https://www.youtube.com/embed/" + data.results[0].key;
                }
            }
        }).catch(ex => {
            console.log(ex); // Log Exception on console.
        });

    }).catch(ex => {
        console.log(ex); // Log Exception on console.
    });
}

function weightFINDRChoices(liked: Media[], disliked: Media[]): Map<string, number>[] {
    let genreWeights = new Map<string, number>();
    let typeWeights = new Map<string, number>();
    let directorWeights = new Map<string, number>();
    let starringWeights = new Map<string, number>();

    for (let media of liked) {
        for (let genre of media.genres) {
            // @ts-ignore
            if (genreWeights.has(genre)) {
                // @ts-ignore
                genreWeights.set(genre, genreWeights.get(genre) * 1); //Do not change it
            } else {
                // @ts-ignore
                genreWeights.set(genre, 1);
            }
        }

        // @ts-ignore
        for (let director of media.directors) {
            // @ts-ignore
            if (directorWeights.has(director.nconst)) {
                // @ts-ignore
                directorWeights.set(director.nconst, directorWeights.get(director.nconst) * 1); //Do not change it
            } else {
                // @ts-ignore
                directorWeights.set(director.nconst, 1);
            }
        }

        // @ts-ignore
        let actor = media.starring[0];            // @ts-ignore
        // @ts-ignore
        if (media.starring?.indexOf(actor) > 1) {
            break;
        }
        // @ts-ignore
        if (starringWeights.has(actor.name.nconst)) {
            // @ts-ignore
            starringWeights.set(actor.name.nconst, starringWeights.get(actor.name.nconst) * 1); //Do not change it
        } else {
            // @ts-ignore
            starringWeights.set(actor.name.nconst, 1);
        }


        let type = media.type;
        // @ts-ignore
        if (typeWeights.has(type)) {
            // @ts-ignore
            typeWeights.set(type, typeWeights.get(type) * 1); //Do not change it
        } else {
            // @ts-ignore
            typeWeights.set(type, 1);
        }

    }

    for (let media of disliked) {
        for (let genre of media.genres) {
            // @ts-ignore
            if (genreWeights.has(genre)) {
                // @ts-ignore
                genreWeights.set(genre, genreWeights.get(genre) * .5);
            } else {
                // @ts-ignore
                genreWeights.set(genre, 0);
            }
        }

        // @ts-ignore
        for (let director of media.directors) {
            // @ts-ignore
            if (directorWeights.has(director.nconst)) {
                // @ts-ignore
                directorWeights.set(genre, directorWeights.get(director.nconst) * .5);
            } else {
                // @ts-ignore
                directorWeights.set(director.nconst, 0);
            }
        }

        // @ts-ignore
        let actor = media.starring[0];            // @ts-ignore
        // @ts-ignore
        if (starringWeights.has(actor.name.nconst)) {
            // @ts-ignore
            starringWeights.set(actor.name.nconst, starringWeights.get(actor.name.nconst) * .5); //Do not change it
        } else {
            // @ts-ignore
            starringWeights.set(actor.name.nconst, 0);
        }


        let type = media.type;
        // @ts-ignore
        if (typeWeights.has(type)) {
            // @ts-ignore
            typeWeights.set(type, typeWeights.get(type) * .9); //Do not change it
        } else {
            // @ts-ignore
            typeWeights.set(type, .5);
        }
    }

    const sortedGenreWeights = new Map([...genreWeights.entries()].sort((a, b) => b[1] - a[1]));
    const sortedDirectorWeights = new Map([...directorWeights.entries()].sort((a, b) => b[1] - a[1]));
    const sortedStarringWeights = new Map([...starringWeights.entries()].sort((a, b) => b[1] - a[1]));
    const sortedTypeWeights = new Map([...typeWeights.entries()].sort((a, b) => b[1] - a[1]));

    let max = Math.max(...sortedGenreWeights.values());
    let min = Math.min(...sortedGenreWeights.values());

    Object.keys(sortedGenreWeights).forEach(function (key) {
        // @ts-ignore
        sortedGenreWeights[key] = map(sortedGenreWeights[key], min, max, 0, 1);
    });

    max = Math.max(...sortedDirectorWeights.values());
    min = Math.min(...sortedDirectorWeights.values());

    Object.keys(sortedDirectorWeights).forEach(function (key) {
        // @ts-ignore
        sortedDirectorWeights[key] = map(sortedDirectorWeights[key], min, max, 0, 1);
    });

    max = Math.max(...sortedStarringWeights.values());
    min = Math.min(...sortedStarringWeights.values());

    Object.keys(sortedStarringWeights).forEach(function (key) {
        // @ts-ignore
        sortedStarringWeights[key] = map(sortedStarringWeights[key], min, max, 0, 1);
    });

    max = Math.max(...sortedTypeWeights.values());
    min = Math.min(...sortedTypeWeights.values());

    Object.keys(sortedTypeWeights).forEach(function (key) {
        // @ts-ignore
        sortedTypeWeights[key] = map(sortedTypeWeights[key], min, max, 0, 1);
    });

    console.log([sortedGenreWeights, sortedDirectorWeights, sortedStarringWeights, sortedTypeWeights]);

    return [sortedGenreWeights, sortedDirectorWeights, sortedStarringWeights, sortedTypeWeights];
}

function clamp(input: number, min: number, max: number): number {
    return input < min ? min : input > max ? max : input;
}

function map(current: number, in_min: number, in_max: number, out_min: number, out_max: number): number {
    const mapped: number = ((current - in_min) * (out_max - out_min)) / (in_max - in_min) + out_min;
    return clamp(mapped, out_min, out_max);
}

let trendingMedia: Media[] = (await fetchTrending()).sort((n1, n2) => n2.averageRating - n1.averageRating);
let newMedia: Media[] = await fetchNew();
if (newMedia.length < 20) {
    newMedia.sort((n1, n2) => n2.averageRating - n1.averageRating).push(...trendingMedia.sort((n1, n2) => n2.averageRating - n1.averageRating).slice(0, 20 - newMedia.length));
}
export const store: Store<State> = createStore({
    state: {
        FINDR: false,
        likedMedia: [],
        dislikedMedia: [],
        FINDRMedia: [],
        results: [],
        trending: [],
        new: [],
    },

    getters: {
        getFINDR(state: State): boolean {
            return state.FINDR;
        },
        getTrending(state: State): Media[] {
            return state.trending;
        },
        getNew(state: State): Media[] {
            return state.new;
        },
        getLikedMedia(state: State): Media[] {
            return state.likedMedia;
        },
        getDislikedMedia(state: State): Media[] {
            return state.dislikedMedia;
        },
        getFINDRMedia(state: State): Media[] {
            return state.FINDRMedia;
        },
        getResults(state: State): Media[] {
            return state.results;
        },
        getFINDRMediaDemo(state: State): Media[] {
            return state.trending
        }
    },
    mutations: {
        setFINDR(state: State, FINDR: boolean) {
            state.FINDR = FINDR;
        },
        invertFINDR(state: State) {
            state.FINDR = !state.FINDR;
        },
        addLikedMedia(state: State, media: Media) {
            state.likedMedia.push(media);
            weightFINDRChoices(state.likedMedia, state.dislikedMedia);
        },
        removeLikedMedia(state: State, media: Media) {
            const index = state.likedMedia.indexOf(media);
            if (index > -1) { // only splice array when item is found
                state.likedMedia.splice(index, 1); // 2nd parameter means remove one item only
            }
            console.log(weightFINDRChoices(state.likedMedia, state.dislikedMedia));
        },
        setLikedMedia(state: State, media: Media[]) {
            state.likedMedia = media;
            weightFINDRChoices(state.likedMedia, state.dislikedMedia);
        },
        clearLikedMedia(state: State) {
            state.likedMedia = [];
            weightFINDRChoices(state.likedMedia, state.dislikedMedia);
        },
        addDislikedMedia(state: State, media: Media) {
            state.dislikedMedia.push(media);
            weightFINDRChoices(state.likedMedia, state.dislikedMedia);
        },
        setDislikedMedia(state: State, media: Media[]) {
            state.dislikedMedia = media;
            weightFINDRChoices(state.likedMedia, state.dislikedMedia);
        },
        clearDislikedMedia(state: State) {
            state.dislikedMedia = [];
            weightFINDRChoices(state.likedMedia, state.dislikedMedia);
        },
        setTrending(state: State, trending: Media[]) {
            state.trending = trending;
        },
        setNew(state: State, newMedia: Media[]) {
            state.new = newMedia;
        },
        setResults(state: State, results: Media[]) {
            state.results = results;
        },
    },
    actions: {
        load({commit}) {
            commit("setTrending", trendingMedia);
            commit("setNew", newMedia);
        },
        async search({commit}): Promise<void> {
            let dropdown: HTMLSelectElement = (document.getElementById("media-type") as HTMLSelectElement);
            let searchBar: HTMLInputElement = (document.getElementById("media-query") as HTMLInputElement);
            if (dropdown && searchBar) {
                let type: string = dropdown.value;
                let query: string = searchBar.value;
                if (query.length > 3) {
                    //TODO: if all, show carousel of results, else show list

                    console.log("searching " + type + ": " + query);
                    let results: Media[] = await fetchByTitle(query, type);
                    // @ts-ignore
                    //commit("setResults", results.sort((a, b) => similar(query, b.title) - similar(query, a.title)));
                    commit("setResults", results.sort((a, b) => b.startYear - a.startYear));

                } else {
                    commit("setResults", []);
                }
            }
        }
    },
});

function similar(a: string, b: string): number {
    a = a.toLowerCase();
    b = b.toLowerCase();
    let equivalency = 0;
    let minLength = (a.length > b.length) ? b.length : a.length;
    let maxLength = (a.length < b.length) ? b.length : a.length;
    for (let i = 0; i < minLength; i++) {
        if (a[i] == b[i]) {
            equivalency++;
        }
    }

    let weight = equivalency / maxLength;
    return (weight * 100);
}

interface Media {
    id: string,
    title: string,
    genres: string[],
    averageRating: number,
    overview?: string,
    posterPath?: string,
    backdropPath?: string,
    trailer?: string,
    imdbLink?: string,
    type: string,
    runtimeMinutes?: number,
    isAdult?: boolean,
    startYear?: number,
    directors?: string[],
    starring?: string[],
}