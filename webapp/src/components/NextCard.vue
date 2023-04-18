<template>
  <div class="movie-card">
    <img v-if="media.posterPath == null" class="movie-card--image" src="../assets/media/missing_img.jpeg"
         alt="image alt text">
    <img v-else class="movie-card--image" v-bind:src="'https://image.tmdb.org/t/p/w500' +this.media.backdropPath"
         alt="image alt text">
    <div class="movie-card--info">
      <div class="info--title">
        <a class="info--title--link">{{ this.media.title }}</a>
      </div>
      <div class="info--rating">
        <span class="fa fa-star checked"></span>
        {{ this.media.averageRating === -1 ? "N/A" : this.media.averageRating }}
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: ["media"],
  name: "MovieCard",
}
</script>

<style scoped lang="scss">
@import "./src/assets/styles/styles.scss";

.movie-card {
  display: grid;
  grid-template-columns: 60% auto;
  background-color: $main-color;
  margin: auto;
  height: fit-content;
  width: 100%;

  border-radius: 0 .3rem .3rem 0;

  transition: all 0.3s ease-in-out;


  &:hover {
    cursor: pointer;
    transition: all 0.3s ease-in-out;
    transform: scale(1.1);
  }

  .movie-card--image {
    width: 100%;
    max-height: 19vh;
  }

  .movie-card--info {
    color: $link-color;
    padding: .8rem;
    display: flex;
    flex-direction: column;
    gap: 15%;
    text-align: left;


    .info--title {
      font-weight: 500;
      display: flex;
      flex-direction: column;

      .info--title--link {
        color: $title-color;
      }
    }

    .info--rating {

      .checked {
        color: $accent-color;
      }
    }
  }
}
</style>