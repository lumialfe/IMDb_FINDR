<template>
  <div class="results-page">
    <TransitionGroup name="list">
      <MovieCard v-for="(media, index) in media" :media="media" :key="index"/>
    </TransitionGroup>
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
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  grid-gap: 5vh 1vw;
  width: 60vw;
  margin: auto;
  transition: all 0.5s ease;

}

.list-enter-active,
.list-leave-active {
  transition: all 0.5s ease;
}

.list-enter-from,
.list-leave-to {
  opacity: 0;
  transform: translateY(100%);
}

@media only screen and (max-width: 640px) {
  .results-page {
    width: 100%;
    grid-template-columns: 100%;
  }
}
</style>