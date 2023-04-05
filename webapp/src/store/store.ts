import {createStore, Store} from "vuex";

interface State {
    FINDR: boolean,
    likedMedia: Media[],
    dislikedMedia: Media[],
    FINDRMedia: Media[],
    results: Media[],
}

function fetchMovieData(media: Media) {
    let baseURL = "https://api.themoviedb.org/3/movie/";
    let apiKEY = "89d117037278a5d054a427790b60933e";
    let url = baseURL + media.id + "?api_key=" + apiKEY;

    fetch(url).then(response => response.json()).then(data => {
        media.posterPath = "https://image.tmdb.org/t/p/w500" + data.poster_path;
        media.backdropPath = "https://image.tmdb.org/t/p/w500" + data.backdrop_path;
        media.genres = data.genres;
        media.overview = data.overview;
        media.averageRating = data.vote_average.toString().substring(0, 3);
        media.imdbLink = "https://www.imdb.com/title/" + data.imdb_id

        fetch("https://api.themoviedb.org/3/movie/" + data.id + "/videos?api_key=" + apiKEY + "&language=en-US" + data.id).then(response => response.json()).then(data => {
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

function weightFINDRChoices(liked: Media[], disliked: Media[]): Map<string, number> {
    let weights = new Map<string, number>();

    for (let media of liked) {
        for (let genre of media.genres) {
            // @ts-ignore
            if (weights.has(genre.name)) {
                // @ts-ignore
                weights.set(genre.name, weights.get(genre.name) * 1); //Do not change it
            } else {
                // @ts-ignore
                weights.set(genre.name, 1);
            }
        }
    }

    for (let media of disliked) {
        for (let genre of media.genres) {
            // @ts-ignore
            if (weights.has(genre.name)) {
                // @ts-ignore
                weights.set(genre.name, weights.get(genre.name) * .5);
            } else {
                // @ts-ignore
                weights.set(genre.name, 0);
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

function clamp(input: number, min: number, max: number): number {
    return input < min ? min : input > max ? max : input;
}

function map(current: number, in_min: number, in_max: number, out_min: number, out_max: number): number {
    const mapped: number = ((current - in_min) * (out_max - out_min)) / (in_max - in_min) + out_min;
    return clamp(mapped, out_min, out_max);
}

export const store: Store<State> = createStore({
    state: {
        FINDR: false,
        likedMedia: [],
        dislikedMedia: [],
        FINDRMedia: [],
        results: [],
    },

    getters: {
        getFINDR(state: State): boolean {
            return state.FINDR;
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
            state.FINDRMedia.push({
                id: "tt0111161",
                title: "The Shawshank Redemption",
                genres: ["Crime", "Drama"],
                averageRating: 4.5,
            });
            state.FINDRMedia.push({
                id: "tt0068646",
                title: "The Godfather",
                genres: ["Crime", "Drama"],
                averageRating: 4.5,
            });
            state.FINDRMedia.push({
                id: "tt0071562",
                title: "The Godfather: Part II",
                genres: ["Crime", "Drama"],
                averageRating: 4.5,
            });
            state.FINDRMedia.push({
                id: "tt0468569",
                title: "The Dark Knight",
                genres: ["Action", "Crime", "Drama", "Thriller"],
                averageRating: 4.5,
            });
            state.FINDRMedia.push({
                id: "tt0099674",
                title: "The Godfather: Part III",
                genres: ["Crime", "Drama"],
                averageRating: 4.5,
            });
            state.FINDRMedia.push({
                id: "tt1345836",
                title: "The Dark Knight Rises",
                genres: ["Action", "Crime", "Drama", "Thriller"],
                averageRating: 4.5,
            });

            for (let media of state.FINDRMedia) {
                fetchMovieData(media);
            }

            return state.FINDRMedia
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
    }
});


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
}