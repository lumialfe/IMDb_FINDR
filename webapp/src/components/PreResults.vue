<template>
  <div class="pre-results" v-if="store.getters['getPreResults'].length !== 0">
    <button v-for="res in store.getters['getPreResults']" :key="res" @click="setSearch(res)">
      {{ res }}
    </button>
  </div>
</template>

<script>
import {store} from "@/store/store";

export default {
  name: "PreResults",
  computed: {
    store() {
      return store
    }
  },
  methods: {
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
  top: 6%;
  left: 31.4%;
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