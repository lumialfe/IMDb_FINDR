# IMDB FINDR™

## Project Description

This project is a demo of a movie search app. It uses an API to search for movies and display the results. The most
interesting functionality is FINDR, a movie recommendation system based on cards that the user can swipe left or right
on to indicate their preference on the movie shown. This will update the results shown to the user, as well as influence
search results.

## SWOT Analysis

“SWOT (strengths, weaknesses, opportunities, and threats) analysis is a framework used to evaluate a company's
competitive position and to develop strategic planning and strategic management. SWOT analysis assesses internal and
external factors, as well as current and future potential.”

### Internal factors

The resources and experience readily available to you. What the company already has.

* ***Strengths***. What factors are going well? What gives us an advantage in the marketplace over our competitors? What
  do our current resources excel at? Is the return on investment worth it?
* ***Weaknesses***. What factors did not go well? What gives us a disadvantage in the marketplace or among our
  competitors? What are the weaknesses of our current resources? Is the return on investment less than optimal?

### External factors

External forces influence and affect every company, organisation and individual. External factors are typically things
you or your company do not control.

* ***Opportunities***. What is going on outside the organisation to capitalise on? How does your business fit into this
  context?
* ***Threats***. What things are happening outside the organisation that can negatively impact our business?

### SWOT

|          | Positive                                                                                                                                                                                  | Negative                                                                                           |
|----------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------|
| Internal | Creativity                                                                                                                                                                                | Lack of experience in the sector                                                                   |
| External | Increasing number of streaming services<br/><br/>Exceed offer of films and tv series. Nowadays there are new things to watch constantly, which increases the uncertainty of what to watch | Competitors<br/><br/>People already know what to watch before even entering the streaming platform |

## Preview

<img width="1440" alt="Screenshot 2023-04-10 at 09 31 52" src="https://user-images.githubusercontent.com/60442261/230851182-3b7f7a57-e85f-45ce-9a98-9940546618db.png">

## Web App

### Technologies

#### Vue.js

Vue.js (commonly referred to as Vue; pronounced "view") is an open-source model–view–viewmodel front end JavaScript
framework for building user interfaces and single-page applications. It was created by Evan You, and is maintained by
him and the rest of the active core team members.

Vue.js features an incrementally adaptable architecture that focuses on declarative rendering and component composition.
The core library is focused on the view layer only. Advanced features required for complex applications such as routing,
state management and build tooling are offered via officially maintained supporting libraries and packages.

#### TypeScript

TypeScript is a free and open source high-level programming language developed and maintained by Microsoft. It is a
strict syntactical superset of JavaScript and adds optional static typing to the language. It is designed for the
development of large applications and transpiles to JavaScript. As it is a superset of JavaScript, existing JavaScript
programs are also valid TypeScript programs.

#### VueX

Vuex is a state management pattern + library for Vue.js applications. It serves as a centralized store for all the
components in an application, with rules ensuring that the state can only be mutated in a predictable fashion.

### Project Setup

```sh
npm install
```

#### Compile and Hot-Reload for Development

```sh
npm run dev
```

#### Type-Check, Compile and Minify for Production

```sh
npm run build
```

#### Lint with [ESLint](https://eslint.org/)

```sh
npm run lint
```

## RestApi

### Tech Stack

<div align="left">
<img src="https://img.shields.io/badge/Java%20-%234cd137" alt="Java">
<img src="https://img.shields.io/badge/Spring Boot%20-%23e74c3c" alt="Spring Boot">
<img src="https://img.shields.io/badge/Elasticsearch%20-%239b59b6" alt="Elasticsearch">
<img src="https://img.shields.io/badge/Docker%20-%231c93e4" alt="Docker">
<img src="https://img.shields.io/badge/Maven%20-%e5923b" alt="Maven">
</div>

### Installation

1. Clone the repository

  ```
  git clone https://github.com/noeliaigc/IMDb-project.git
  ```

2. Start the project

  ```
  docker compose up --build -d
  ```

3. Shut down the project
  ```
  docker compose down
  ```
  
### Docker images
  
  1. Elastic image:
  ```
  docker pull noeliai/imdb:version3
  ```

### Endpoints

#### Index:

1. `POST /index/uploadMovies`

    Parameters:
    - basics -> File which contains the basic information about the movies.
    - akas -> File which contains the akas that a film has.
    - ratings -> File which contains the ratings of the films.
    - crew -> File which contains information about the director of the film.
    - principals -> File which contains the actors that participate on the films.
    
    This endpoint is used for uploading and index the specified files.

2. `DELETE /index/delete`

    Parameters:
    - indexName -> String that contains the name of the index.
    
    This endpoint is used for deleting the specified index.

#### Queries:

1. `GET /imdb/_search/range`

    Parameters:
    - from -> Year of start of the movies wanted to be searched
    - size -> Size of the hits of the search
    
    This endpoint is used to search movies within a range of the start year of the movie.

2. `GET /imdb/_search/title`

    Parameters:
    - title -> Text that want to be searched in order to find a movie
    - type -> Type of the movie that want to be searched (MOVIE, EPISODE, ALL)
    
    This endpoint is used to search movies by its title and filtered by type.

3. `GET /imdb/_search/recommended`

    Parameters:
    - year -> Year of start of the movies wanted to be searched
    - size -> Size of the hits of the search
    
    This endpoint is used to search the recommended movies with a good rating in the year specified.

4. `GET /imdb/_search`

    Parameters:
    - minYear -> Minimum year of the movies to be searched
    - maxYear -> Maximum year of the movies to be searched
    - maxRuntimeMin -> Maximum runtime minutes of the movies to be searched
    - minRuntimeMin -> Minimum runtime minutes of the movies to be searched
    - minAvgRating -> Minimum average rating of the movies to be searched
    - maxAvgRating -> Maximum average rating of the movies to be searched
    - type -> Type of the movies to be searched (MOVIE, EPISODE, ALL)
    - genres -> Array of genres of the movies to be searched (Drama, Action, Thriller)
    
    This endpoint is used to perform a filtered search of movies indicating any value of every field.

5. `GET /imdb/_search/not-to-watch`

    This endpoint is used to search the must NOT to watch movies.

6. `GET /imdb/_search/recommended-all-times`

    This endpoint is used to search the all times recommended movies with a good rating.

7. `GET /imdb/_search/genres`

    Parameters:
    - mustGenres -> String array which has the genres that must contain the movies to be searched.
    - mustNotGenres -> String array which has the gernes that must not contain the movies to be searched.
    - excludedIds -> String array which has the movies ids that must not be searched.
    - types -> String with the type of the movies to be searched (ALL, EPISODE, MOVIE)
