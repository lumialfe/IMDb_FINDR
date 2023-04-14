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

export const movieTypes = ["short", "movie", "tvMovie", "tvShort"];
export const tvTypes = ["tvSeries", "tvMiniSeries", "tvSpecial"];
