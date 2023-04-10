<template>
  <div class="movie-card">
    <img loading="lazy" v-if="this.media.posterPath == null" src="../../assets/media/missing_img.jpeg"
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
        <span class="fa fa-star checked"></span>
        {{ this.media.averageRating }}
      </div>
      <div class="info--trailer">
        <a class="button-link" target="_blank">
          <button class="info--trailer-button">
            Trailer <span class="fa fa-play"></span>
          </button>
        </a>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: ["media"],
  name: "MovieCard",
  mounted() {
    console.log(this.media);
  }
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

  .movie-card--image {
    max-width: 100%;
  }

  .movie-card--info {
    color: $link-color;
    padding: 1rem;
    display: grid;
    grid-template-rows: 50% auto auto;

    .info--title {
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

      .checked {
        color: $accent-color;
      }
    }


    .info--trailer {
      text-align: center;

      a {
        .info--trailer-button {
          margin: auto;
          width: 50%;
        }
      }
    }
  }
}
</style>