<template>
  <div class="stack">
    <FINDR_Card v-for="(mediaItem, index) in this.media" v-bind:media="mediaItem" v-bind:parent="this" v-bind:index="index" ></FINDR_Card>
  </div>
</template>

<script>
import FINDR_Card from "@/components/FINDR/FINDR_Card.vue";
import {store} from "@/store/store";

export default {
  data() {
    return {
      current: 0
    }
  },
  props: ["media"],
  components: {FINDR_Card},
  name: "FINDR_Card_Stack",
  methods: {
    next() {
      this.current++;
      store.dispatch("FINDR/updateFINDRResults");
    },
  },
};
</script>

<style scoped lang="scss">
.stack {
  display: grid;
  margin: 0;
}
</style>