<template>
  <div class="FINDR-choice">
    <div class="FINDR-choice--content">
      <div class="FINDR-choice--content--image">
        <img v-if="media.backdropPath.includes('undefined')" src="../../assets/media/missing_img.jpeg"
             alt="media image">
        <img v-else :src="media.backdropPath" alt="media image">
      </div>
      <div class="FINDR-choice--content--text">
        <h3>{{ media.title }}</h3>
        <button class="card--button" id="dislike" v-on:click="remove">
          <i class="fa fa-times" aria-hidden="true"></i>
        </button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "FINDR_Choice_Card",
  props: ["media"],
  methods: {
    remove() {
      this.$store.commit("FINDR/removeLikedMedia", this.media);
    }
  }
}
</script>

<style scoped lang="scss">
@import "../../assets/styles/styles.scss";

.FINDR-choice {
  max-width: 20vw;
  margin: 0;
  display: grid;
  grid-template-rows: 90% 10%;
  background-color: $main-color;

  img {
    width: 100%;
  }

  .FINDR-choice--content {
    .FINDR-choice--content--text {
      width: 80%;
      padding-left: 10%;
      padding-right: 10%;
      display: grid;
      grid-template-columns: 90% 10%;
      align-content: center;

      .card--button {
        margin: auto;
        padding: 0;
        width: 3rem;
        height: 3rem;
        border-radius: 100%;
        font-size: 150%;
        text-align: center;
        color: $title-color;
        background: none;
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

@media only screen and (max-width: 640px) {
  .FINDR-choice {
    max-width: 50%;
    font-size: 50%;

    img {
      width: 100%;
    }
  }
}

</style>