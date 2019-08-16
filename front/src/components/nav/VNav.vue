<template>
  <div class="wrap">
    <div class="background"></div>
    <aside>
      <img class="logo" src="../../assets/img/logo.png" @click="logoClick">

      <ul>
        <router-link to="/">
          <li :class="{'choice':getChoiceIndex === 0}" @click="navChange(0)">{{$t('nav.home')}}</li>
        </router-link>
        <router-link to="/blocks">
          <li :class="{'choice':getChoiceIndex === 1}" @click="navChange(1)">{{$t('nav.blocks')}}</li>
        </router-link>
        <router-link to="/transaction">
          <li
            :class="{'choice':getChoiceIndex === 2}"
            @click="navChange(2)"
          >{{$t('nav.transactions')}}</li>
        </router-link>
        <router-link to="/contracts">
          <li :class="{'choice':getChoiceIndex === 3}" @click="navChange(3)">{{$t('nav.contracts')}}</li>
        </router-link>
        <el-select
          v-model="selectedLang"
          placeholder="Select Language"
          class="-lang-select"
          @change="changeLang"
        >
          <el-option
            v-for="item in langOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>
      </ul>
    </aside>
    <section>
      <div class="search-wrap">
        <search class="search"></search>
      </div>
    </section>
  </div>
</template>

<script>
import bus from "../../utils/bus";
import Search from "../search/Search";
export default {
  components: { Search },
  name: "v-nav",
  data() {
    return {
      value: true,
      time: "2018-05-25",
      selectedLang: "en",
      langOptions: [
        { label: "中文", value: "cn" },
        { label: "Engish", value: "en" }
      ]
    };
  },
  methods: {
    logoClick() {
      this.$router.push({ path: "/" });
    },
    changeLang() {
      // if (bus.locale === "en") {
      //   this.$i18n.locale = "cn";
      //   bus.local = "cn";
      // } else {
      //   this.$i18n.locale = "en";
      //   bus.local = "en";
      // }
      const newLocale = this.selectedLang;
      this.$i18n.locale = newLocale;
      bus.local = newLocale;
      console.log("locale changed to ", bus.local);
    },
    navChange(index) {
      bus.navChoice = index;
    }
  },
  computed: {
    getBusLocal() {
      return bus.local;
    },
    getChoiceIndex() {
      return bus.navChoice;
    }
  }
};
</script>

<style lang="less" scoped>
.wrap {
  position: absolute;
  width: 100%;
  box-sizing: border-box;
  height: 78px;
  padding: 0 80px;
  top: 0;
  left: 0;
  z-index: 2;
  a {
    text-decoration: none;
  }
  a:hover,
  a:visited,
  a:link,
  a:active {
    color: inherit;
  }
  .background {
    background-color: black;
    position: absolute;
    width: 100%;
    height: 100%;
    left: 0;
    top: 0;
    z-index: -1;
    /*z-index: 1;*/
  }
  aside {
    float: left;
    height: 78px;
    line-height: 78px;
    font-size: 0;
    z-index: 100;
    .logo {
      vertical-align: middle;
      cursor: pointer;
    }
    .search-wrap {
      display: inline-block;
      vertical-align: middle;
      .search {
        margin-left: 30px;
      }
    }
    ul {
      display: inline-block;
      vertical-align: middle;
      li {
        padding: 8px 26px;
        float: left;
        margin-left: 30px;
        color: #dddddd;
        font-size: 16px;
        cursor: pointer;
        height: 30px;
        line-height: 30px;
        &:hover {
          color: white;
        }
        @media screen and (max-width: 1400px) {
          padding: 6px 20px;
          margin-left: 18px;
        }
      }
      .choice {
        // border: 1px solid white;
        // border-radius: 8px;
        color: white;
        font-weight: bold;
        // background:rgba(39,81,141,1);
      }
    }
  }
  section {
    float: right;
    margin-top: 39px;
    transform: translateY(-50%);
  }
  .-lang-select {
    width: 120px;
    height: 45px;
    line-height: 45px;
    float: left;
    .el-input__inner {
      color: white !important;
    }
  }
}
</style>
