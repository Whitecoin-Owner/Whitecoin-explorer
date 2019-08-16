<template>
  <div id="app">
    <v-nav></v-nav>
    <router-view/>
  </div>
</template>

<script>
import VNav from "./components/nav/VNav";
import bus from './utils/bus';

export default {
  components: {VNav},
  name: 'App',
  created() {
    let href = window.location.href
    if (href.indexOf('/blocks') > 0) {
      bus.navChoice = 1;
    } else if (href.indexOf('/transaction') > 0) {
      bus.navChoice = 2;
    } else if (href.indexOf('/contracts') > 0) {
      bus.navChoice = 3;
    }
  }
}
</script>

<style lang="less">
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  /*height: 100%;*/
  .app-top-lin {
    display: block;
    height: 0;
  }
}
</style>
