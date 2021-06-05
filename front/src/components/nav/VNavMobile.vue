<template>
  <div class="wrap1">
    <div class="wrap1_two1">
      <div class="con_aside1">
        <div class="con_logo">
          <a href="https://www.whitecoin.info/"><img class="logo" src="../../assets/img/logo.svg" @click="logoClick"></a>
        </div>
        <div class="navBar1" @touchmove.prevent @click="toggleMenu">
        </div>
        <div class="menu-opened1" :class="{ open: showMenu, hide: hideMenu }">
          <div class="mask" @touchmove.prevent @click="toggleMenu">
            <div class="side-menu1">
              <div class="menu-bottom1">
                <ul class="links1">
                  <router-link to="/">
                    <li :class="{'choice1':getChoiceIndex === 0}" @click="navChange(0)"><span class="home"></span>{{$t('nav.home')}}</li>
                  </router-link>
                  <router-link to="/transaction" >
                    <li :class="{'choice1':getChoiceIndex === 2}" @click="navChange(2)" ><span class="transaction"></span>{{$t('nav.transactions')}}</li>
                  </router-link>
                  <router-link to="/blocks">
                    <li :class="{'choice1':getChoiceIndex === 1}" @click="navChange(1)"><span class="blocks"></span>{{$t('nav.blocks')}}</li>
                  </router-link>
                  <router-link to="/tokens">
                    <li :class="{'choice1':getChoiceIndex === 5}" @click="navChange(5)"><span class="tokens"></span>{{$t('nav.tokens')}}</li>
                  </router-link>
                  <div :class="['downLa',{'choice1':getChoiceIndex === 4}]">
                    <div class="downIcon"></div>
                    <el-select
                    :value="$t('nav.ziyuan')"
                    @click="navChange(4)"
                    @change="changeLang"
                    :class="['language_default',{'active':getChoiceIndex === 4}]"
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
                  </div>
                  <div class="downLa">
                    <div class="language"></div>
                    <el-select
                      v-model="selectedLang"
                      placeholder="English"
                      @change="changeLang"
                      class="language_default"
                    >
                    <a>
                      <el-option
                        v-for="item in langOptions"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value"
                      ></el-option>
                      </a>
                  </el-select>
                  </div>
                </ul>
              </div>
            </div>
          </div>
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
      ],
      showMenu: false,
      hideMenu: true,
      isShow: false,
      switcher: true,
    };
  },
  watch:{
    selectedLang(val){
      this.selectedLang = val;
    },
    
  },
  methods: {
    logoClick() {
      this.$router.push({ path: "/" });
    },
    changeLang() {
      // console.log(`changeLang=${index}`)
      // if (bus.locale === "en") {
      //   this.$i18n.locale = "cn";
      //   bus.local = "cn";
      // } else {
      //   this.$i18n.locale = "en";
      //   bus.local = "en";
      // }
      const newLocale = this.selectedLang
      this.$i18n.locale = newLocale;
      bus.local = newLocale;
      localStorage.setItem('lang',bus.local);
      // console.log(bus.local,'pppp');
      this.toggleMenu()
    },
    navChange(index) {
      bus.navChoice = index;
      // console.log(bus.navChoice)
    },
    toggleMenu() {
      let that = this;
      if (!that.switcher) {
        return;
      }
      that.showMenu = !that.showMenu;
      that.switcher = false;
      if (!that.showMenu) {
        const timeout = setTimeout(() => {
          that.hideMenu = true;
          that.$nextTick(() => {
            that.switcher = true;
          });
          clearTimeout(timeout);
        }, 350);
      } else {
        that.switcher = true;
        that.hideMenu = false;
      }
    },
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

<style>
.el-scrollbar__bar {
  opacity: 1 !important;
}
</style>

<style lang="less" scoped>
.wrap1 {
  width: 100%;
  box-sizing: border-box;
  position: fixed;
  background: #fff;
  z-index: 999;
  top: 0;
  height: 113rem;
  .wrap1_two1{
    box-shadow: 0 4rem 12rem 0 rgba(0, 0, 0, 0.09);
    .con_aside1 {
      width: 100%;
      height: 113rem;
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 0 30rem;
      box-sizing: border-box;
      .con_logo {
        height: 56rem;
        .logo {
          cursor: pointer;
          width: 200rem;
          height: 56rem;
        }
      }
      .navBar1 {
        width: 42rem;
        height: 30rem;
        background: url(../../assets/img/mobile/nav.png) no-repeat;
        background-size: 100% 100%;
      }
      .menu-opened1 {
        position: fixed;
        left: 0;
        top: 0;
        right: 0;
        bottom: 0;

        &.hide {
          z-index: -1;
          visibility: hidden;
        }

        .mask {
          width: 100%;
          height: 100%;
          background: rgba(27, 27, 27, 0);
          transition: all linear 0.2s;
        }

        .side-menu1 {
          position: fixed;
          right: -400rem;
          top: 0;
          bottom: 0;
          width: 400rem;
          overflow-y: auto;
          -ms-overflow-style: none;
          overflow: -moz-scrollbars-none;
          -webkit-overflow-scrolling: touch;
          padding-top: 40rem;
          &::-webkit-scrollbar {
            width: 0 !important;
          }
          background: #1D1E2C;
          transition: all linear 0.2s;

          .menu-top {
            padding: 0.47rem 0.47rem 0.6rem;
            position: relative;

            &:after {
              content: "";
              display: block;
              position: absolute;
              left: 0;
              right: 0;
              bottom: 0;
              width: 100%;
              height: 1px;
              background: rgba(255, 255, 255, 0.1);
              transform: scaleY(0.5);
            }

            .row {
              text-align: center;
              height: 0.88rem;
              line-height: 0.88rem;

              &.btn {
                width: 5.56rem;
                border-radius: 0.13rem;
                background: #6565E3;
                text-align: center;
                margin: 0.3rem auto 0;
              }

              .link,
              a {
                font-weight: 400;
                color: #fff;
              }
            }
          }

          .menu-bottom1 {
            width: 100%;
            .links1 {
              li{
                color:#fff;
                font-size: 28rem;
                padding-left: 80rem;
                display: flex;
                align-items: center;
                line-height: 90rem;
                span{
                  position: relative;
                  transition: all 0.2s;
                  margin-right: 45rem;
                }
                .home {
                  width: 35rem;
                  height: 35rem;
                  background: url(../../assets/img/mobile/nav_home.svg) no-repeat;
                  background-size: 100%;
                }
                .transaction {
                  width: 35rem;
                  height: 35rem;
                  background: url(../../assets/img/mobile/nav_transactions.svg) no-repeat;
                  background-size: 100%;
                }
                .blocks {
                  width: 35rem;
                  height: 35rem;
                  background: url(../../assets/img/mobile/nav_blocks.svg) no-repeat;
                  background-size: 100%;
                }
                .tokens {
                  width: 35rem;
                  height: 35rem;
                  background: url(../../assets/img/mobile/nav_tokens.svg) no-repeat;
                  background-size: 100%;
                }
              }
              .choice1{
                color: #fff;
                background: #735DFF;
              }
              .downLa{
                display: flex;
                align-items: center;
                padding: 0 0 0 80rem;
                .downIcon{
                  width: 35rem;
                  height: 35rem;
                  background: url(../../assets/img/mobile/nav_downLa.svg) no-repeat;
                  background-size: 100%;
                  margin-top: 5rem;
                }
                .language{
                  width: 35rem;
                  height: 35rem;
                  background: url(../../assets/img/mobile/nav_language.svg) no-repeat;
                  background-size: 100%;
                }
              }
            }
          }
        }

        &.open {
          z-index: 1000;
          visibility: visible;
          .mask {
            background: rgba(27, 27, 27, 0.5);
          }

          .side-menu1 {
            right: 0;
          }
        }
      }
    }
  }
  section {
    float: right;
    margin-top: 39rem;
    transform: translateY(-50%);
  }
}
/deep/.el-input.el-input--medium{
  background: none;
}
/deep/.el-select-dropdown{
  background: none !important;
}
/deep/.el-select-dropdown .el-popper {
    margin-top: 0 !important;
}
/deep/.el-input__inner {
  line-height: 50rem !important;
  height:50rem !important ;
  padding: 0 0 0 48rem !important;
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  color: #fff;
}
/deep/.el-input--medium{
  width: 220rem;
  font-size: 28rem !important;
}
/deep/.language_default{
  input::-webkit-input-placeholder{
    font-size: 24rem !important;
    color: #fff;
  }
}
/deep/.language_default.active{
  input::-webkit-input-placeholder{
    font-size: 24rem !important;
    color: #fff;
  }
}
/deep/.el-input--medium .el-input__icon{
  line-height: 50rem;
  width: 25rem;
}
/deep/.el-select-dropdown__item{
  font-size: 26rem !important;
  line-height: 60rem;
  height:60rem ;
  text-align: center;
  span{
    font-size: 26rem;
  }
}
/deep/.el-select-dropdown__item.selected{
  color: #735DFF !important;
  font-size: 26rem;
}
/deep/.popper__arrow::after{
  margin-left: 20rem;
}
/deep/.el-select-dropdown__wrap1{
  max-height: 274rem !important;
}
/deep/ .el-select .el-input .el-select__caret{
  font-size: 20rem;
}
</style>
