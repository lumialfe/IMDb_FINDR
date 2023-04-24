<template>
  <div class="pre-results" v-if="store.getters['getPreResults'].length !== 0">
    <button v-for="res in store.getters['getPreResults']" :key="res" @click="setSearch(res.title)">
      <span v-if='movieTypes().includes(res.type)'>
          <i class="fa fa-film" aria-hidden="true"></i>
        </span>
      <span v-else>
          <i class="fa fa-television" aria-hidden="true"></i>
        </span>
      <span>&emsp;</span>
      <span>{{ res.title }}</span>
    </button>
  </div>
</template>

<script>
import {store} from "@/store/store";
import {movieTypes} from "@/store/util";

export default {
  name: "PreResults",
  computed: {
    store() {
      return store
    }
  },
  methods: {
    movieTypes() {
      return movieTypes
    },
    setSearch(res) {
      document.getElementById("media-query").value = res;
      store.commit("setPreResults", []);
      store.dispatch('search');
    }
  }
}
</script>

<style scoped lang="scss">
.pre-results {
  position: absolute;
  background-color: white;
  border-radius: .5rem;
  padding: 1%;
  top: 7.2%;
  left: 31.2%;
  width: 34%;
  display: flex;
  flex-direction: column;

  button {
    width: 100%;
    text-align: left;
    background: none;
    color: black;

    &:hover {
      font-weight: bold;
      box-shadow: none;
    }
  }
}

@media only screen and (max-width: 640px) {
  .pre-results {
    top: 6%;
    left: 35%;
    width: 52%;
  }
}
</style>