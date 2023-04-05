<template>
  <h1>{{ media.title }}</h1>
  <div class="yt_video">
    <iframe class="recommended-video" v-bind:src="media.trailer"></iframe>
  </div>

</template>

<script>
export default {
  name: "RecommendedCard",
  props: ["media"],
}
</script>

<style scoped lang="scss">
@import "./src/assets/styles/styles.scss";

.recommended-video {
  width: 100%;
  height: 50vh;
  border: none
}

@media only screen and (max-width: 640px) {
  .yt_video {
    height: 30vh;
    .recommended-video {
      height: 30vh;
    }
  }
}
</style>