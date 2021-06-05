<template>
  <div id="app">
    <v-nav v-if="desktop"/>
    <v-nav-mobile v-else/>
    <router-view class="mian_alls" />
    <iFooter v-if="desktop" />
    <iFooterMobile v-else/>
  </div>
</template>

<script>
import VNav from "./components/nav/VNav";
import VNavMobile from "./components/nav/VNavMobile";
import iFooter from "./components/nav/iFooter";
import iFooterMobile from "./components/nav/iFooterMobile";
import bus from "./utils/bus";

export default {
  components: { VNav,iFooter,VNavMobile,iFooterMobile },
  name: "App",
  data(){
    return {
      desktop: device.desktop()
    }
  },
  created() {
    let href = window.location.href;
    if (href.indexOf("/blocks") > 0) {
      bus.navChoice = 1;
    } else if (href.indexOf("/transaction") > 0) {
      bus.navChoice = 2;
    } else if (href.indexOf("/contracts") > 0) {
      bus.navChoice = 3;
    } else if(href.indexOf('/richlist') > 0) {
      bus.navChoice = 4;
    }
  }
};
</script>

<style lang="less">
#app {
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  /*height: 100%;*/
  .app-top-lin {
    display: block;
    height: 0;
  }

  input::-webkit-input-placeholder {
    font-size: 12px;
  }
  .mian_alls{
    height: 100%;
  }
}

</style>
