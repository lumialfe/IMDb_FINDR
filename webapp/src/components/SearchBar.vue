<template>
  <div class="searchbar">
    <select class="searchbar--filters" id="media-type" v-on:input="store.dispatch('search')">
      <option class="searchbar--filter" value="ALL">All</option>
      <option class="searchbar--filter" value="MOVIE">Movies</option>
      <option class="searchbar--filter" value="EPISODE">Series</option>
    </select>
    <input class="searchbar--input" type="text" placeholder="Search IMDb..." autofocus id="media-query"
           v-on:input="store.dispatch('search')"/>
  </div>
</template>

<script lang="ts">
import {store} from "@/store/store";

export default {
  name: "SearchBar",
  computed: {
    store() {
      return store
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