<template>
  <a v-bind:href="media.imdbLink">
    <div class="movie-card">
      <img loading="lazy" v-if="this.media.posterPath.includes('undefined')" src="../assets/media/missing_img.jpeg"
           alt="Card image"
           class="movie-card--image"/>
      <img loading="lazy" v-else v-bind:src="this.media.posterPath"
           alt="Card image"
           class="movie-card--image"/>
      <div class="movie-card--info">
        <div class="info--title">
          <a class="info--title--link" v-bind:href="media.imdbLink">{{ this.media.title }}</a>
        </div>
        <div class="info--rating">
        <span>
          <span class="fa fa-star checked"></span>
          {{ this.media.averageRating === -1 ? "N/A" : this.media.averageRating }}
        </span>
          <span>
         {{ this.media.type.toUpperCase() }}
        </span>
        </div>
        <div class="info--trailer">
          <a class="button-link" target="_blank" v-bind:href="media.trailer">
            <button class="info--trailer-button">
              Trailer <span class="fa fa-play"></span>
            </button>
          </a>
        </div>
      </div>
    </div>
  </a>
</template>

<script>
import {store} from "@/store/store";

export default {
  computed: {
    store() {
      return store
    }
  },
  props: ["media"],
  name: "MovieCard",
}
</script>

<style scoped lang="scss">
@import "./src/assets/styles/styles.scss";

.movie-card {
  display: grid;
  grid-template-rows: auto 1fr;

  background-color: $main-color;

  margin: auto;

  border-radius: 0 0 .3rem .3rem;

  width: 230px;
  height: 100%;

  transition: all 0.3s ease-in-out;

  &:hover {
    transition: all 0.3s ease-in-out;
    background-color: $main-color;
    width: 270px;

    .movie-card--info {
      .info--title {
        transition: all 0.3s ease-in-out;
        display: none;
      }

      .info--rating {
        transition: all 0.3s ease-in-out;
        display: none;
      }

      .info--trailer {
        a {
          .info--trailer-button {
            transition: all 0.3s ease-in-out;
            color: $accent-color;
          }
        }
      }
    }
  }

  .movie-card--image {
    max-width: 100%;
  }

  .movie-card--info {
    color: $link-color;
    padding: 1rem;
    display: grid;
    grid-template-rows: 50% auto auto;

    .info--title {
      transition: all 0.3s ease-in-out;

      font-size: 110%;
      font-weight: 500;
      text-align: center;

      .info--title--link {
        color: $title-color;

        &:hover {
          text-decoration: underline $link-color 2px;
        }

        &:active {
          opacity: 70%;
        }
      }
    }

    .info--rating {
      transition: all 0.3s ease-in-out;

      display: flex;
      flex-direction: row;
      justify-content: space-around;

      .checked {
        color: $accent-color;
      }
    }


    .info--trailer {
      text-align: center;

      a {
        .info--trailer-button {
          transition: all 0.3s ease-in-out;

          margin: auto;
          width: 50%;
        }
      }
    }
  }
}
</style>