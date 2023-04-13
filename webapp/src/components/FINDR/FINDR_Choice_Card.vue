<template>
  <div class="FINDR-choice">
    <div class="FINDR-choice--content">
      <div class="FINDR-choice--content--image">
        <img :src="media.backdropPath" alt="media image">
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
      this.$store.commit("removeLikedMedia", this.media);
    }
  }
}
</script>

<style scoped lang="scss">
@import "../../assets/styles/styles.scss";

.FINDR-choice {
  margin: 0;
  display: grid;
  grid-template-rows: 90% 10%;
  background-color: $main-color;

  img {
    width: 100%;
  }

  .FINDR-choice--content {
    .FINDR-choice--content--text {
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

</style>