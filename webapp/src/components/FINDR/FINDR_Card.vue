<template>
  <Transition v-bind:name="this.chosenCardAction">
    <div v-if="this.index === this.parent.current">
      <div class="FINDR-card" v-if="this.isShowing">
        <Transition name="fade">
          <div class="card--flip" v-on:mouseenter="changeInfo()" v-on:mouseleave="changeInfo()">
            <div class="flip--image">
              <img loading="lazy" v-if="this.media.backdropPath == null" src="../../assets/media/missing_img.jpeg"
                   alt="Card image"
                   class="card--image"/>
              <img loading="lazy" v-else v-bind:src="this.media.backdropPath"
                   alt="Card image"
                   class="card--image"/>
            </div>
            <div class="flip--text">
              <p class="overview">{{ this.media.overview }}</p>
            </div>
          </div>
        </Transition>
        <div class="card--info">
          <div class="card--title">{{ media.title }}</div>
          <div class="card--categories">
            <span v-for="genre in media.genres">{{ genre }}</span>
          </div>
          <div class="card--buttons">
            <button class="card--button" id="dislike" v-on:click="dislike"><i class="fa fa-times"
                                                                              aria-hidden="true"></i>
            </button>
            <button class="card--button" id="like" v-on:click="like"><i class="fa fa-heart-o" aria-hidden="true"></i>
            </button>
          </div>
        </div>
      </div>
    </div>
  </Transition>
</template>

<script lang="ts">
import {store} from "@/store/store";
import {defineComponent} from "vue";

export default defineComponent({
  name: "FINDR_Card",
  props: ["media", "parent", "index"],
  data() {
    return {
      isShowing: true,
      isShowingDescription: false,
      chosenCardAction: "slide-like",
    }
  },
  methods: {
    changeInfo() {
      this.isShowingDescription = !this.isShowingDescription;
      if (this.isShowingDescription) {
        document.getElementsByClassName("flip--image")[0].style.display = "none";
        document.getElementsByClassName("flip--text")[0].style.display = "block";
      } else {
        document.getElementsByClassName("flip--image")[0].style.display = "block";
        document.getElementsByClassName("flip--text")[0].style.display = "none";
      }
    },
    like() {
      console.log("like");
      this.chosenCardAction = "slide-like";
      this.isShowing = false;
      store.commit("addLikedMedia", this.media);
      this.parent.next();
    },
    dislike() {
      this.chosenCardAction = "slide-dislike";
      console.log("dislike");
      this.isShowing = false;
      store.commit("addDislikedMedia", this.media);
      this.parent.next();
    },
  },
});
</script>

<style scoped lang="scss">

@import "../../assets/styles/styles";

.FINDR-card {
  grid-row: 1;
  grid-column: 1;

  display: grid;
  grid-template-rows: auto 1fr;

  text-align: center;

  background-color: $main-color-light;
  border-radius: 1.3rem;

  width: 80%;
  margin: 0 auto 8rem;

  .card--flip {
    height: 300px;

    .flip--image {
      .card--image {
        width: 100%;
        object-fit: cover;
        height: 300px;
        border-radius: 1rem 1rem 0 0;
      }
    }

    .flip--text {
      display: none;
      padding: 1.5rem;
      color: white;
      text-align: justify;
      text-justify: auto;
    }
  }

  .card--info {
    display: grid;
    padding: 5%;

    .card--title {
      font-weight: bold;
      font-size: 200%;
      color: white;
    }

    .card--categories {
      display: flex;
      gap: 1rem;
      margin: .5rem auto 0;
      font-size: 110%;
      color: $link-color;
      text-align: center;

      span {
        text-align: center;
      }
    }

    .card--buttons {
      display: flex;
      flex-flow: nowrap;
      align-items: center;
      justify-content: space-around;
      margin: .1rem auto;
      width: 70%;

      .card--button {
        border-radius: 100%;
        height: 5rem;
        width: 5rem;
        font-size: 200%;
        text-align: center;
        color: $title-color;
        background: none;
      }

      #like {
        &:hover {
          transition: color .3s ease-in-out;
          color: #00ff51;
        }
      }

      #dislike {
        &:hover {
          transition: color .3s ease-in-out;
          color: #ff0062;
        }
      }
    }
  }
}

.slide-like-leave-active,
.slide-like-enter-active,
.slide-dislike-leave-active,
.slide-dislike-enter-active {
  transition: all 0.3s ease-out;
}

.slide-like-enter-from,
.slide-like-leave-to {
  transform: translateX(-30vw);
}

.slide-dislike-enter-from,
.slide-dislike-leave-to {
  transform: translateX(30vw);
}

@media only screen and (max-width: 640px) {
  .FINDR-card {
    width: 80vw;
    min-height: 50vh;

    font-size: 90%;

    .card--flip {
      height: auto;

      .flip--image {
        .card--image {
          width: 80vw;
          height: auto;
        }
      }
    }
  }
}

</style>