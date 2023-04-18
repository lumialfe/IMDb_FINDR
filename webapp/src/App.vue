<!--suppress JSUnusedGlobalSymbols -->
<script lang="ts">
import BottomSheet from "@/components/FINDR/BottomSheet.vue";
import {defineComponent} from "vue";
import {store} from "@/store/store";
import Header from "@/components/Header.vue";
import Footer from "@/components/Footer.vue";
import FINDR_Div from "@/components/FINDR/FINDR_Div.vue";
import MainPage from "@/components/MainPage.vue";

export default defineComponent({
  computed: {
    store() {
      return store
    },
    FINDR() {
      return store.getters['FINDR/getFINDR'];
    }
  },
  created() {
    this.store.dispatch("load");
  },
  components: {MainPage, FINDR_Div, Footer, Header, BottomSheet},
});
</script>

<template>
  <Header></Header>
  <main id="main-container">
    <MainPage></MainPage>
    <Transition name="slide-fade">
      <FINDR_Div v-if="FINDR"></FINDR_Div>
    </Transition>
    <BottomSheet></BottomSheet>
  </main>
  <Footer></Footer>
</template>

<style scoped lang="scss">
@import "./assets/styles/styles.scss";

main {
  grid-area: main;
  display: grid;
  width: 90%;
  margin: 2rem auto auto;
}
</style>
