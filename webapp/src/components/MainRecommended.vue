<template>
  <div class="main-recommended">
    <div class="recommended--current">
      <button v-on:click="this.previous()" class="current-previous">
        <span class="fa fa-chevron-left"></span>
      </button>
      <div class="recommended--current-video">
        <RecommendedCard :media="this.media[0]"/>
      </div>
      <button v-on:click="this.next()" class="current-next">
        <span class="fa fa-chevron-right"></span>
      </button>
    </div>
    <div class="recommended--next">
      <p></p>
      <h4>Up Next</h4>
      <div class="next--cards">
        <NextCard v-for="(media, index) in this.media.slice(1, 4)" :media="media" :key="media.id" @click="swipeCards(index + 1)"/>
      </div>
    </div>
  </div>
</template>

<script>
import NextCard from "@/components/NextCard.vue";
import RecommendedCard from "@/components/RecommendedCard.vue";

export default {
  name: "MainRecommended",
  components: {RecommendedCard, NextCard},
  props: ["media"],
  data() {
    return {
      current: 0,
    }
  }, methods: {
    next() {
      this.media.push(this.media.shift());
      this.$forceUpdate();
    },
    previous() {
      this.media.unshift(this.media.pop());
      this.$forceUpdate();
    }, swipeCards(index) {
      for (let i = 0; i < index; i++) {
        this.media.push(this.media.shift());
      }
      this.$forceUpdate();
    }
  },
}
</script>

<style scoped lang="scss">
@import "../assets/styles/styles.scss";

.main-recommended {
  display: grid;
  grid-template-columns: 65% auto;
  grid-gap: 1vh 1vw;
  width: 60vw;
  height: 60vh;
  margin: auto;

  .recommended--current {
    display: grid;
    grid-template-columns: 5% auto 5%;
    margin: 0;
    border-radius: 1rem;

    button {
      height: 100%;
      width: 100%;
      background: none;
      transition: font-size 0.3s;
      transition: all 0.3s ease-in-out;


      &:hover {
        font-size: 200%;
        transition: all 0.3s ease-in-out;
      }
    }

    .current-previous {
      border-radius: 10px 0 0 10px;
    }

    .current-next {
      border-radius: 0 10px 10px 0;
    }

    .recommended--current-video {
      display: grid;
      grid-template-rows: 5vh 50vh;
      grid-gap: 4vh;
    }
  }

  .recommended--next {
    display: grid;
    grid-template-rows: 5% 10% 85%;

    .next--cards {
      display: grid;
      grid-gap: 1vh;
    }
  }
}

.slide-leave-active,
.slide-enter-active {
  transition: 1s;
}

.slide-enter {
  transform: translate(100%, 0);
}

.slide-leave-to {
  transform: translate(-100%, 0);
}

@media only screen and (max-width: 640px) {
  .main-recommended {
    grid-template-rows: 100%;
    grid-template-columns: 100%;
    width: 100%;
    height: auto;

    .recommended--current {
      font-size: 70%;
      height: 36vh;

      .recommended--current-video {
        grid-template-rows: 6vh 30vh;
      }
    }

    .recommended--next {
      display: none;
    }
  }
}
</style>