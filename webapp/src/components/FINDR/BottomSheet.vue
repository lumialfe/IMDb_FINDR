<template>
  <div class="bottom-sheet">
    <button id="showBottomButton" class="bottom-sheet--button" @click="this.toggle">
      <span v-if="!this.isShowing" class="fa fa-chevron-up"></span>
      <span v-else class="fa fa-chevron-down"></span>
    </button>
    <Transition name="slide-fade">
      <div class="bottom-sheet--content" v-if="this.isShowing">
        <FINDR_Div></FINDR_Div>
      </div>
    </Transition>
  </div>
</template>

<script lang="ts">
import {defineComponent} from "vue";
import {store} from "@/store/store";
import FINDR_Div from "./FINDR_Div.vue";

export default defineComponent({
  computed: {
    store() {
      return store
    }
  },
  components: {FINDR_Div},
  data() {
    return {
      isShowing: false
    }
  },
  methods: {
    toggle() {
      this.isShowing = !this.isShowing;
    }
  },
});

</script>

<style scoped lang="scss">
@import "../../assets/styles/styles";


.bottom-sheet {
  display: none;
}

@media only screen and (max-width: 640px) {

  .bottom-sheet {
    display: block;
    position: fixed;
    bottom: 0;
    text-align: center;
    width: 90vw;

    .bottom-sheet--content {
      height: 80vh;

    }

    .bottom-sheet--button {
      background-color: white;
      border-radius: 10px;
      width: 100%;
    }
  }
}
</style>