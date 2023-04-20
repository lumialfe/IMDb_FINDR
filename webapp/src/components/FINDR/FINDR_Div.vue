<template>
  <div id="FINDR-container" class="main--FINDR">
    <h2>FINDR™</h2>
    <FINDR_Card_Stack v-bind:media="store.getters['FINDR/getFINDRCardMedia']"></FINDR_Card_Stack>
    <h4>About FINDR™</h4>
    <p>
      FINDR™ is a movie recommendation system that uses a combination of machine learning and
      natural language processing to recommend movies based on your preferences.
    </p>
    <h4>How to use FINDR™</h4>
    <p>
      To use FINDR™, you can click on the FINDR™ button to the right of the
      search bar to show the cards. You can then click on the "Like" button so the system can show you similar
      movies.
      You can also click on the "Dislike" button to tell the system that you don't like the movie or that you have
      already seen it.
    </p>
  </div>
</template>

<script>
import {store} from "@/store/store";
import FINDR_Card_Stack from "./FINDR_Card_Stack.vue";

export default {
  name: "FINDR_Div",
  components: {FINDR_Card_Stack},
  computed: {
    store() {
      return store
    }
  }
}
</script>

<style scoped lang="scss">
@import "../../assets/styles/styles.scss";

.main--FINDR {
  background-color: var(--main-color);
  border-radius: 1rem;
  height: fit-content;
  padding-bottom: 5%;

  h2 {
    margin-left: 3rem;
  }

  h4, p {
    margin-left: 3rem;
    margin-right: 3rem;
  }

  a {
    margin-left: 3rem;

    &:hover {
      text-decoration: underline;
    }
  }
}

@media only screen and (max-width: 640px) {
  .main--FINDR {
    margin-top: 1vh;
    padding-top: 3vh;

    h4, p {
      display: none;
    }

    h2 {
      margin: 0 0 2vh;
    }
  }


}
</style>