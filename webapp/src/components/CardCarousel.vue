<template>
  <div class="card--carousel">
    <h2>{{ title }}</h2>
    <div class="carousel">
      <button id="carousel--left-button" v-on:click="slide(true)" class="carousel--left-button">
        <span class="fa fa-chevron-left"></span>
      </button>
      <div class="carousel--scroll-panel" id="scroll" ref="scroll">
        <div class="carousel--cards">
          <slot :media="media"></slot>
        </div>
      </div>
      <button id="carousel--right-button" v-on:click="slide(false)" class="carousel--right-button">
        <span class="fa fa-chevron-right"></span>
      </button>
    </div>
  </div>
</template>

<script lang="ts">
import MovieCard from "@/components/MovieCard.vue";
import {defineComponent} from "vue";
import {store} from "@/store/store";

export default defineComponent({
  computed: {
    store() {
      return store
    }
  },
  components: {MovieCard},
  props: ["title", "media"],
  methods: {
    slide(left: boolean): void {
      let divContent: HTMLDivElement = this.$refs.scroll;

      if (left) {
        divContent.scrollLeft -= 242;
      } else {
        divContent.scrollLeft += 242;
      }
    },
  },
});
</script>

<style scoped lang="scss">
@import "../assets/styles/styles.scss";

.card--carousel {
  width: 100%;
  margin: auto auto 3rem;

  h2 {
    margin-left: 2rem;
  }
}

.carousel {
  display: grid;
  grid-template-columns: 1fr auto 1fr;

  button {
    background: none !important;
    width: 2rem;

    &:hover {
      background-color: #4f4f4f !important;
    }
  }

  .carousel--left-button {
    height: 100%;
    border-radius: .5rem 0 0 .5rem;
  }

  .carousel--right-button {
    height: 100%;
    border-radius: 0 .5rem .5rem 0;
  }

  .carousel--scroll-panel {
    display: flex;
    flex: 100%;
    flex-flow: row nowrap;
    overflow-x: auto;
    overflow-y: hidden;
    -ms-overflow-style: none;
    scroll-behavior: smooth;

    ::-webkit-scrollbar {
      display: none;
    }

    .carousel--cards {
      min-width: 60vw;
      display: flex;
      flex-flow: row nowrap;
      gap: 1rem;

      .carousel--card {
        min-width: 12rem;
      }
    }
  }
}

.carousel--scroll-panel > * {
  flex: 0 0 auto;
}

@media only screen and (max-width: 640px) {
  main {
    width: 100%;
    margin: 0;

    .card--carousel {
      width: 100%;
    }
  }
}
</style>