<template>
  <div class="wrap">
    <div class="wrap_two">
      <div class="con_aside">
        <div class="con_logo">
          <a href="https://www.whitecoin.info/"><img class="logo" src="../../assets/img/logo.svg" @click="logoClick"></a>
        </div>
        <div class="navbar">
          <ul>
            <router-link to="/">
              <li :class="{'choice':getChoiceIndex === 0}" @click="navChange(0)">{{$t('nav.home')}}</li>
            </router-link>
            <router-link to="/transaction">
              <li
                :class="{'choice':getChoiceIndex === 2}"
                @click="navChange(2)"
              >{{$t('nav.transactions')}}</li>
            </router-link>
            <router-link to="/blocks">
              <li :class="{'choice':getChoiceIndex === 1}" @click="navChange(1)">{{$t('nav.blocks')}}</li>
            </router-link>
            <!-- <router-link to="/contracts">
              <li :class="{'choice':getChoiceIndex === 3}" @click="navChange(3)">{{$t('nav.contracts')}}</li>
            </router-link>
            <router-link to="/richlist">
              <li :class="{'choice':getChoiceIndex === 4}" @click="navChange(4)">{{$t('nav.richlist')}}</li>
            </router-link> -->
            <router-link to="/tokens">
              <li :class="{'choice':getChoiceIndex === 5}" @click="navChange(5)">{{$t('nav.tokens')}}</li>
            </router-link>
            <el-select
              :value="$t('nav.ziyuan')"
              :class="['-lang-select',{'choice':getChoiceIndex === 4}]"
              @click="navChange(4)"
            >
              <router-link to="/contracts">
                <el-option :value="$t('nav.contracts')">{{$t('nav.contracts')}}</el-option>
              </router-link>
              <router-link to="/richlist">
                <el-option :value="$t('nav.richlist')">RICHLIST</el-option>
              </router-link>
              <router-link to="/assets">
                <el-option :value="$t('nav.assets')">{{$t('nav.assets')}}</el-option>
              </router-link>
            </el-select>
          </ul>
          <el-select
              v-model="selectedLang"
              placeholder="English"
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
        </div>
      </div>
    </div>
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
        { label: "English", value: "en" },
        { label: "한국어", value: "korea"},
        { label: "日本語", value: "japan"}
      ]
    };
  },
  watch:{
    selectedLang(val){
      this.selectedLang = val
    },
    
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
      localStorage.setItem('lang',bus.local);
      console.log(bus.local,'pppp')
    },
    navChange(index) {
      bus.navChoice = index;
      console.log(bus.navChoice)
    }
  },
  mounted(){
    if(location.search.indexOf("?lang=") > -1 ){
      const query=location.search.substr(1)
      const lang={};
      query.split("&").forEach(item=>{
        let obj = item.split("=");
        lang[obj[0]] = obj[1]
      })
      this.selectedLang = lang.lang;
    }else{
      this.selectedLang = localStorage.getItem('lang')
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

<style lang="less" >
.wrap {
  width: 100%;
  box-sizing: border-box;
  height: 78px;
  .wrap_two{
    box-shadow: 0px 4px 12px 0px rgba(0, 0, 0, 0.09);
    .con_aside {
      width: 1140px;
      margin: 0 auto;
      height: 78px;
      display: flex;
      align-items: center;
      .con_logo {
        height: 33px;
        .logo {
          cursor: pointer;
          width: 115px;
          height: 33px;
          margin-right: 30px;
        }
      }
      .navbar{
        width: 100%;
        display: flex;
        justify-content: space-between;
        align-items: center;
      }
      // .search-wrap {
      //   display: inline-block;
      //   vertical-align: middle;
      //   .search {
      //     margin-left: 30px;
      //   }
      // }
      ul {
        display:flex;
        align-items: center;
        li {
          margin-left: 30px;
          padding: 0 20px;
          color: #333333;
          font-size: 16px;
          cursor: pointer;
          line-height: 65px;
          border-bottom: 4px solid #fff;
          box-sizing: border-box;
          &:hover {
            border-bottom-color:#735DFF;
            color: #735DFF;
          }
        }
        .choice {
          border-bottom-color: #735DFF;
          color: #735DFF;
          .el-input__inner{
            border-bottom: 4px solid #735DFF;
            border-radius: 0;
          }
        }
      }
    }
  }
  section {
    float: right;
    margin-top: 39px;
    transform: translateY(-50%);
  }
.el-select-dropdown .el-popper {
    margin-top: 0 !important;
}
.-lang-select {
  margin-left: 15px;
  width:120px;
  box-sizing: border-box;
    input::-webkit-input-placeholder{
      color: #333333 !important;
      font-size: 14px !important;
    }
    .el-input__inner {
      color: #333333 !important;
      text-align: center;
      padding: 0;
      height: 68px;
      line-height: 68px !important;
      font-weight: normal !important;
      font-family: "Avenir", Helvetica, Arial, sans-serif;
      box-sizing: border-box;
      border-bottom: 4px solid #fff;
    }
  }
}
.el-input--medium{
  font-size: 16px !important;
}
 .el-input--medium .el-input__icon{
  line-height: 50px;
}
.el-select-dropdown__item{
  text-align: center;
}
 .el-select-dropdown__item.selected{
  color: #735DFF !important;
}
.popper__arrow::after{
  margin-left: 20px;
}

</style>
