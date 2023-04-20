<template>
  <header>
    <div class="header-content">
      <a href="/"><img class="header--logo" src="../assets/media/logo.svg" alt="Page logo."/></a>
      <SearchBar></SearchBar>
      <div class="header--links">
        <button v-on:click="changeAsideVisibility()">FINDR™</button>
      </div>
    </div>
  </header>
</template>

<script lang="ts">
import {store} from "@/store/store";
import {defineComponent} from "vue";
import SearchBar from "./SearchBar.vue";

export default defineComponent({
  name: "Header",
  components: {SearchBar},
  methods: {
    changeAsideVisibility() {
      store.dispatch("FINDR/invertFINDR");
      if (store.getters["FINDR/getFINDR"]) {
        (document.getElementById("main-container") as HTMLElement).style.gridTemplateColumns = "65% 35%";
        (document.getElementById("main-container") as HTMLElement).style.columnGap = "4rem";
      } else {
        (document.getElementById("main-container") as HTMLElement).style.gridTemplateColumns = "100%";
      }
    },
  },
});
</script>

<style scoped lang="scss">
@import "../assets/styles/styles.scss";

header {
  background-color: var(--main-color);

  .header-content {
    width: 60%;
    margin: auto;
    grid-area: header;
    display: flex;
    flex-flow: row;
    flex-wrap: nowrap;
    padding-top: .69rem;
    padding-bottom: .69rem;
    justify-content: space-between;
    align-items: center;

    a {
      display: flex;
      justify-content: center;

      .header--logo {
        width: 4rem;
      }
    }


    .header--links {
      display: flex;
      justify-content: space-between;
      color: var(--link-color);

      button {
        min-width: fit-content;
        font-size: 110%;

        &:hover {
          transition: color .3s ease-in-out;
          color: var(--accent-color);
        }
      }
    }
  }
}

@media only screen and (max-width: 640px) {
  header {
    .header-content {
      width: 100%;
      margin: 0;
      border-radius: 0;
      justify-content: space-around;

      .header--links {
        display: none;
      }
    }
  }
}

</style>