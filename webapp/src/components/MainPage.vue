<template>
  <PopUp></PopUp>
  <div class="main--main">
    <FINDR_Choice_Carousel v-if="store.getters.getLikedMedia.length > 0" title="Your FINDR™ Choices"
                  :media="store.getters.getLikedMedia"></FINDR_Choice_Carousel>
    <div v-if="store.getters.getResults.length > 0" class="main--main--results">
      <ResultsPage v-bind:media="store.getters.getResults"></ResultsPage>
    </div>
    <div v-else class="main--main--main>">
      <MainRecommended :media="store.getters.getFINDRMediaDemo.slice(0, 4)"></MainRecommended>

      <CardCarousel title="Trending Now" :media="store.getters.getFINDRMediaDemo"></CardCarousel>
      <CardCarousel title="New Releases" :media="store.getters.getFINDRMediaDemo"></CardCarousel>
      <CardCarousel title="Soon Available" :media="store.getters.getFINDRMediaDemo"></CardCarousel>
    </div>
  </div>
</template>

<script>
import {store} from "@/store/store";
import CardCarousel from "./CardCarousel.vue";
import ResultsPage from "@/components/ResultsPage.vue";
import MainRecommended from "@/components/MainRecommended.vue";
import FINDR_Choice_Carousel from "@/components/FINDR/FINDR_Choice_Carousel.vue";
import PopUp from "@/components/FINDR/PopUp.vue";

export default {
  name: "MainPage",
  components: {PopUp, FINDR_Choice_Carousel, MainRecommended, ResultsPage, CardCarousel},
  computed: {
    store() {
      return store
    }
  }
}
</script>

<style scoped lang="scss">
.main--main {
  display: grid;
  width: 60vw;
  margin: auto;
}

@media only screen and (max-width: 640px) {
  .main--main {
    width: 100%;
  }
}
</style>