const baseURL = "http://localhost:8080/imdb/_search/";
export const endpoints = {
    API_RECOMMENDED: baseURL + "recommended",
    API_TITLE: baseURL + "title",
    API_NOT_TO_WATCH: baseURL + "not-to-watch",
    API_TOP: baseURL + "recommended-all-times",
    API_FILTERS: baseURL.substring(0, baseURL.length - 1),
};