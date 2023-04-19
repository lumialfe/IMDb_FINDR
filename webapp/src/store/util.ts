import type {Commit, Dispatch, Store} from "vuex";

export default interface State {
    FINDR: boolean,
    likedMedia: Media[],
    dislikedMedia: Media[],
    FINDRMedia: Media[],
    results: Media[],
    trending: Media[],
    new: Media[],
    topAllTime: Media[],
    notToWatch: Media[],
}

export interface Media {
    id: string,
    title: string,
    genres: string[],
    averageRating: number,
    overview?: string,
    posterPath?: string,
    backdropPath?: string,
    trailer?: string,
    imdbLink: string,
    type: string,
    runtimeMinutes: number,
    isAdult: boolean,
    startYear: number,
    directors: string[],
    starring: string[],
}

export interface ComponentCustomProperties {
    $store: Store<State> | Store<Commit> | Store<Dispatch>
}

export const movieTypes = ["short", "movie", "tvMovie", "tvShort"];
export const tvTypes = ["tvSeries", "tvMiniSeries", "tvSpecial"];


export async function myFetch(endpoint: string, params?: Map<string, string>): Promise<Media[]> {
    let url = endpoint;
    if (params) {
        url += "?";
        for (let [key, value] of params) {
            url += key + "=" + value + "&";
        }
        url = url.substring(0, url.length - 1);
    }
    return await fetchMedia(url);
}

async function fetchMedia(url: string): Promise<Media[]> {
    let ret: Media[] = [];
    await fetch(url).then((response) => response.json())
        .then(async (data) => {
            for (let media of data) {
                let rating = data.vote_average;

                let m: Media = {
                    id: media.tconst,
                    imdbLink: "https://www.imdb.com/title/" + media.tconst,
                    title: media.primaryTitle,
                    genres: media.genres,
                    averageRating: rating === undefined ? -1 : rating.toString().substring(0, 3),
                    type: media.titleType,
                    startYear: media.startYear,
                    isAdult: false, // No adult movies indexed
                    directors: media.directors,
                    starring: media.starrings,
                    runtimeMinutes: media.runtimeMinutes,
                    trailer: "https://www.youtube.com/embed/dQw4w9WgXcQ",
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
    const apiKEY = import.meta.env.VITE_API_KEY
    let url = baseURL + media.id + "?api_key=" + apiKEY + "&language=en-US&external_source=imdb_id";

    await fetch(url).then(response => response.json()).then(async data => {
        if (data.movie_results.length) {
            data = data.movie_results[0];
        } else if (data.tv_results.length) {
            data = data.tv_results[0];
        }

        media.posterPath = "https://image.tmdb.org/t/p/w500" + data.poster_path;
        media.backdropPath = "https://image.tmdb.org/t/p/w500" + data.backdrop_path;
        media.overview = data.overview === undefined ? "Nothing to show here..." : data.overview;
        if (media.averageRating === -1) {
            let rating = data.vote_average;
            media.averageRating = rating === undefined ? media.averageRating : rating.toString().substring(0, 3);
        }

        let baseURL = "https://api.themoviedb.org/3/";
        movieTypes.includes(media.type as string) ? baseURL += "movie/" : baseURL += "tv/";

        await fetch(baseURL + data.id + "/videos?api_key=" + apiKEY + "&language=en-US" + data.id).then(response => response.json()).then(data => {
            if (data.results.length !== 0) {
                for (let i = 0; i < data.results.length; i++) {
                    if (data.results[i].site.toLowerCase() === "youtube") {
                        if (data.results[i].name.toLowerCase().includes("trailer")) {
                            media.trailer = "https://www.youtube.com/embed/" + data.results[i].key;
                            break;
                        } else {
                            media.trailer = "https://www.youtube.com/embed/" + data.results[0].key;
                        }
                    }
                }
            }
        }).catch(ex => {
            console.log(ex); // Log Exception on console.
        });

    }).catch(ex => {
        console.log(ex); // Log Exception on console.
    });
}

function clamp(input: number, min: number, max: number): number {
    return input < min ? min : input > max ? max : input;
}

export function map(current: number, in_min: number, in_max: number, out_min: number, out_max: number): number {
    const mapped: number = ((current - in_min) * (out_max - out_min)) / (in_max - in_min) + out_min;
    return clamp(mapped, out_min, out_max);
}
