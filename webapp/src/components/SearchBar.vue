<template>
  <div class="searchbar">
    <select class="searchbar--filters" id="media-type" v-on:input="search">
      <option class="searchbar--filter" value="ALL">All</option>
      <option class="searchbar--filter" value="MOVIE">Movies</option>
      <option class="searchbar--filter" value="EPISODE">Series</option>
    </select>
    <input class="searchbar--input" type="text" placeholder="Search IMDb..." autofocus id="media-query"
           @focusout="reset()"
           v-on:input="store.dispatch('preSearch')"
           v-on:keyup.enter="store.dispatch('search')"/>
  </div>
  <PreResults></PreResults>
</template>

<script lang="ts">
import {store} from "@/store/store";
import PreResults from "@/components/PreResults.vue";

export default {
  name: "SearchBar",
  components: {PreResults},
  computed: {
    store() {
      return store
    }
  },
  methods: {
    reset() {
      (document.getElementById("media-query") as HTMLInputElement).style.border = "none";
    },
    search() {
      if ((document.getElementById("media-query") as HTMLInputElement).value === "") {
        store.dispatch("preSearch");
      } else {
        store.dispatch("search");
      }
    }
  }
}
</script>

<style scoped lang="scss">
@import "../assets/styles/styles.scss";

.searchbar {
  display: grid;
  grid-template-columns: auto 1fr;
  width: 60%;

  .searchbar--filters {
    border-radius: .5rem 0 0 .5rem;
    border: none;
    font-family: "Montserrat", sans-serif;
    height: 2.3rem;
    text-align: left;
    font-weight: 500;

    .searchbar--filter {
      font-family: "Montserrat", sans-serif;
      text-align: center;
    }
  }

  .searchbar--input {
    font-family: "Montserrat", sans-serif;
    border-radius: 0 .5rem .5rem 0;
    border: none;
    margin-left: .1rem;
    padding-left: 1rem;
  }

  @media only screen and (max-width: 640px) {
    .searchbar--input {
      margin-right: 18%;
    }
  }
}
</style>