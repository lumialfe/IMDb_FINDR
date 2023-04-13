<template>
  <div class="results-page">
    <TransitionGroup name="list">
      <MovieCard v-for="media in media" :media="media"/>
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
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  grid-gap: 1vh 1vw;
  width: 60vw;
  margin: auto;
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
</style>