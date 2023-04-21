<!--suppress JSUnresolvedVariable -->
<template>
  <div v-if="store.getters['getSearching']" class="div-loading">
    <h1 class="loading">LOADING...</h1>
  </div>
  <div class="main--main" v-else>
    <CardCarousel v-if="store.getters['FINDR/getLikedMedia'].length > 0" title="Your FINDR™ Choices"
                  :media="store.getters['FINDR/getLikedMedia']" v-slot="slotProps">
      <TransitionGroup name="list">
        <div v-for="(mediaItem, index) in slotProps.media" class="carousel--card" key="{{index}}">
          <FINDR_Choice_Card :media="mediaItem"></FINDR_Choice_Card>
        </div>
      </TransitionGroup>
    </CardCarousel>

    <div v-if="store.getters['getResults'].length > 0" class="main--main--results">
      <ResultsPage v-bind:media="store.getters['getResults']"></ResultsPage>
    </div>
    <div v-else class="main--main--main>">
      <MainRecommended :media="store.getters['getTrending'].slice(0, 4)"></MainRecommended>

      <CardCarousel title="Trending Now" :media="store.getters['getTrending']" v-slot="slotProps">
        <div v-for="mediaItem in slotProps.media" class="carousel--card">
          <MovieCard v-if="!store.getters['isRendering']" :media="mediaItem"></MovieCard>
        </div>
      </CardCarousel>
      <CardCarousel title="Top New Releases" :media="store.getters['getNew']" v-slot="slotProps">
        <div v-for="mediaItem in slotProps.media" class="carousel--card">
          <MovieCard v-if="!store.getters['isRendering']" :media="mediaItem"></MovieCard>
        </div>
      </CardCarousel>
      <CardCarousel title="Top Ever" :media="store.getters['getTopAllTime']" v-slot="slotProps">
        <div v-for="mediaItem in slotProps.media" class="carousel--card">
          <MovieCard v-if="!store.getters['isRendering']" :media="mediaItem"></MovieCard>
        </div>
      </CardCarousel>
      <CardCarousel title="Not To Watch" :media="store.getters['getNotToWatch']" v-slot="slotProps">
        <div v-for="mediaItem in slotProps.media" class="carousel--card">
          <MovieCard v-if="!store.getters['isRendering']" :media="mediaItem"></MovieCard>
        </div>
      </CardCarousel>
    </div>
  </div>
</template>

<script>
import {store} from "@/store/store";
import CardCarousel from "./CardCarousel.vue";
import ResultsPage from "@/components/ResultsPage.vue";
import MainRecommended from "@/components/MainRecommended.vue";
import MovieCard from "@/components/MovieCard.vue";
import FINDR_Choice_Card from "@/components/FINDR/FINDR_Choice_Card.vue";

export default {
  name: "MainPage",
  components: {FINDR_Choice_Card, MovieCard, MainRecommended, ResultsPage, CardCarousel},
  computed: {
    store() {
      return store
    }
  }
}
</script>

<style scoped lang="scss">
.div-loading {
  font-size: 200%;
  text-align: center !important;
  .loading {
    color: var(--accent-color);
    animation: blinker 2s linear infinite;
    @keyframes blinker {
      50% {
        opacity: 0;
      }
    }
  }
}



.main--main {
  display: grid;
  width: 60vw;
  margin: auto;
  min-height: 100vh;
}

@media only screen and (max-width: 640px) {
  .main--main {
    width: 100%;
  }
}

.list-enter-active,
.list-leave-active {
  transition: all 0.5s ease;
  position: absolute;
}

.list-enter-from,
.list-leave-to {
  opacity: 0;
  transform: translateY(-100%);
}
</style>