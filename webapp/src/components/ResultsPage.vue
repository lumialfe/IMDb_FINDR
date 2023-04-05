<template>
  <div class="results-page">
    <MovieCard v-for="media in media" :media="media"/>
  </div>
</template>

<script>
import MovieCard from "@/components/MovieCard.vue";

export default {
  name: "ResultsPage",
  components: {MovieCard},
  props: ["media"],
}
</script>

<style scoped lang="scss">
.results-page {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  grid-gap: 1vh 1vw;
  width: 60vw;
  margin: auto;
}
</style>