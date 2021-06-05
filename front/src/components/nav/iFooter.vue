<template>
  <div>
    <div class="footer" :class="{transparent: useTransparent}">
      <footer>
        <div class="top">
          <div class="left">
            <img src="../../assets/img/footer_logo1.png" alt width="150"/>
            <p>Copyright @ 2018-2020 xwcWhitecoin | All rights reserved</p>
          </div>
          <div class="right">
             <ul>
              <li>
                <a :href="pdfUrl" target="_blank">{{ $t('home.footer.t1') }}</a>
              </li>
              <li>
                <a href="mailto:xwcwhitecoin@gmail.com">{{ $t('home.footer.t2') }}</a>
              </li>
              <li>
                <a
                  href="https://xwcc.whitecoin.info/news/"
                  rel="nofollow"
                  target="_blank"
                >{{ $t('home.footer.t3') }}</a>
              </li>
              <li>
                <a href="javaScript:void(0)" @click="modalShow = true">{{ $t('home.footer.t4') }}</a>
              </li>
            </ul>
            <p class="icon">
              <a href="mailto:info@whitecoin.info" rel="nofollow" target="_blank">
                <img src="../../assets/img/home_3_logo_1.png" alt />
              </a>
              <a href="https://github.com/Whitecoin-XWC/Whitecoin-core" rel="nofollow" target="_blank">
                <img src="../../assets/img/home_3_logo_2.png" alt />
              </a>
              <a href="https://twitter.com/WhiteCoiner" rel="nofollow" target="_blank">
                <img src="../../assets/img/home_3_logo_3.png" alt />
              </a>
              <a href="https://weibo.com/u/6395981275" rel="nofollow" target="_blank">
                <img src="../../assets/img/home_3_logo_4.png" alt />
              </a>
              <a
                :href="isEn == 'en' ? 'http://t.me/xwc_whitecoin' : 'https://t.me/whitecoincn'"
                rel="nofollow"
                target="_blank"
              >
                <img src="../../assets/img/home_3_logo_5.png" alt />
              </a>
              <a href="javaScript:void(0)">
                <img src="../../assets/img/home_3_logo_6.png" alt />
                <img src="../../assets/img/Wechat.jpeg" alt class="WeChat" />
              </a>
              <a href="javaScript:void(0)">
                <img src="../../assets/img/home_3_logo_7.png" alt />
                <img src="../../assets/img/QQ.jpeg" alt class="qq" />
              </a>
              
            </p>
          </div>
        </div>

        <div class="linkS">
          <div class="inner">
            <span>{{ $t('home.footer.t5') }}:</span>
            <ul>
              <li>
                <a href="https://www.zb.live" rel="nofollow" target="_blank">ZB</a>
              </li>
              <li>
                <a href="https://www.xt.com" rel="nofollow" target="_blank">XT</a>
              </li>
              <li>
                <a href="https://www.exx.com" rel="nofollow" target="_blank">EXX</a>
              </li>
              <li>
                <a href="https://www.zbg.com/" rel="nofollow" target="_blank">ZBG</a>
              </li>
              <!-- <li>
                <a href="https://funcoin.io/" target="_blank">Funcoin</a>
              </li> -->
              <li>
                <a href="https://www.lbkex.co/" rel="nofollow" target="_blank">Lbank</a>
              </li>
              <li>
                <a href="https://www.bw.com/" rel="nofollow" target="_blank">BW</a>
              </li>
              <li>
                <a href="https://www.coinomi.com/en/" rel="nofollow" target="_blank">Coinomi</a>
              </li>
              <li>
                <a href="https://www.coingecko.com/en" rel="nofollow" target="_blank">Coingecko</a>
              </li>
              <li>
                <a href="https://bitkan.com/en/" rel="nofollow" target="_blank">Bitkan</a>
              </li>
              <li>
                <a href="https://www.mytoken.io/" rel="nofollow" target="_blank">Mytoken</a>
              </li>
              <li>
                <a href="https://www.feixiaohao.com/" rel="nofollow" target="_blank">Feixiaohao</a>
              </li>
              <li>
                <a href="https://www.aicoin.com/" rel="nofollow" target="_blank">AICoin</a>
              </li>
              <li>
                <a href="http://coinfolix.com/" rel="nofollow" target="_blank">Coinfolix</a>
              </li>
              <li>
                <a href="https://coincap.io/" rel="nofollow" target="_blank">Coincap</a>
              </li>
              <li>
                <a href="https://coinmarketcap.com" rel="nofollow" target="_blank">CoinMarketCap</a>
              </li>
            </ul>
          </div>
        </div>
      </footer>
    </div>
  </div>
</template>

<script>
export default {
  name: "iFooter",
  props: {
    showFooter: {
      type: Boolean,
      default() {
        return true;
      }
    },
    useTransparent: {
      type: Boolean,
      default() {
        return false;
      }
    }
  },
  data() {
    return {
      isEn: this.$i18n.locale,
      modalShow: false,
      pdfUrl:
        this.$i18n.locale == "cn"
          ? "https://www.whitecoin.info/pdf/Whitecoin Technical White Paper_cn.pdf"
          : "https://www.whitecoin.info/pdf/Whitecoin Technical White Paper_en.pdf"
    };
  },
  methods: {
    modalCancel(v) {
      this.modalShow = v;
    },
    baidu(){
      var hmt = hmt || [];
      (function() {
        var hm = document.createElement("script");
        hm.src = "https://hm.baidu.com/hm.js?5166d089a046ddbabb6b2f2cecb20436";
        var s = document.getElementsByTagName("script")[0]; 
        s.parentNode.insertBefore(hm, s);
      })();
    }
  },
  created() {
    //console.log(this.useTransparent);
  },
  mounted() {
    //console.log(this.$t('lang.footer'));
    this.baidu()

    console.log(this.$i18n.locale,'uuuu')
  }
};
(function(){
    var bp = document.createElement('script');
    bp.src = 'https://zz.bdstatic.com/linksubmit/push.js';
    var s = document.getElementsByTagName("script")[0];
    s.parentNode.insertBefore(bp, s);
})();
</script>

<style lang='less' scoped>
.footer {
  width: 100%;

  .bar{
    width: 100%;
    background:rgba(248,249,255,1);
    .inner{
      width: 1140px;
      margin: 0 auto;
      display: flex;
      align-items: center;
      justify-content: space-around;
      h3{
        font-size:22px;
        font-family:PingFangSC-Medium,PingFang SC;
        font-weight:500;
        color:rgba(184,200,218,1);
        line-height:30px;
      }
      .partner {
            width: 100%;
            color: #B8C8DA;
            padding: 20px 0;
            .title {
              font-size: 22px;
              font-weight: 400;
              color: #B8C8DA;
              width: auto;
              text-align: center;
              margin-bottom: 20px;
            }
            .tip {
              display: flex;
              justify-content: space-between;
              flex-wrap: wrap;
              .text {
                p {
                  &:first-child{
                    font-size: 18px;
                  }
                  &:last-child {
                    font-size: 12px;
                  }
                }
              }
              a {
                width: 33%;
                background: none;
                border-radius: 0;
                // height: 42px;
                display: flex;
                justify-content: left;
                align-items: center;
                margin-bottom: 30px;
                padding-left: 60px;
                color: rgba(138, 148, 167, 1);
                .icon {
                  display: flex;
                  justify-content: center;
                  align-items: center;
                }
                span {
                  margin-right: 15px;
                  display: block;
                }
              }
            }
          }
  
    }
  }
  footer {
    width: 100%;
    // height: 115px;
    background:#403682 url(../../assets/img/foot_bg.png) no-repeat;
    background-position: center bottom;
    display: flex;
    flex-direction: column;
    align-items: center;
    font-weight: 400;
    color: #fff;
    .top {
      width: 1140px;
      display: flex;
      border-bottom: 1px solid rgba(255, 255, 255, 0.1);
      padding-top: 60px;
      padding-bottom: 30px;
      > div {
        flex: 1;
      }
      .left {
        //  background-color: red;
        p {
          font-size: 14px;
          margin-top: 30px;
          color: #fff;
          font-weight: normal;
        }
      }
      .right {
        ul{
          a{
            color: #fff !important;
            &:hover{
              color: #A394FF !important;
            }
          }
        }
        .icon {
          display: flex;
          justify-content: flex-end;
          align-items: center;
          height: 44px;
          a {
            margin-right: 24px;
            position: relative;
            display: flex;
            justify-content: center;
            align-items: center;
            img {
              width: 20px;
            }
            .WeChat {
                width: 100px;
                height: auto;
                position: absolute;
                top: 50%;
                transform: translateY(-50%);
                left: -110px;
                display: none;
                z-index: 99;
              }
              &:hover .WeChat {
                display: block;
              }
            &:last-child {
              margin-right: 0px;
              .qq {
                width: 100px;
                height: auto;
                position: absolute;
                top: 50%;
                transform: translateY(-60%);
                left: -110px;
                display: none;
                z-index: 99;
              }
              &:hover .qq {
                display: block;
              }
            }
          }
        }
        ul {
          margin-top: 14px;
          margin-bottom: 38px;
          display: flex;
          justify-content: flex-end;
          li {
            margin-left: 40px;
            a{
              font-size: 18px;
              font-family:PingFangSC-Medium,PingFang SC;
              font-weight:500;
              color:rgba(79,94,119,1);
              line-height:25px;
            }
          }
        }
      }
    }
    .linkS {
      width: 100%;
      border-top: 1px solid rgba(103, 120, 151, 0.8);
      .inner{
        width: 1140px;
        margin: 0 auto;
        display: flex;
        height: 70px;
        align-items: center;
      }
      span {
        font-size: 12px;
        display: inline-block;
        white-space: nowrap;
      }
      ul {
        display: flex;
        flex-wrap: wrap;
        li {
          margin-left: 32px;
          a {
            font-size: 12px;
            color: #fff;
          }
        }
      }
    }
  }
}

.mobile-footer {
  padding: 1rem 0.5rem 0.5rem;
  background: #0b0e19;

  &.transparent {
    background: transparent;
  }

  .logo {
    width: 2.16rem;
    height: 0.71rem;
    background: url(../../assets/img/footer_logo.png) no-repeat center;
    background-size: 100% 100%;
  }

  .icon {
    margin: 0.2rem 0 0;
    a {
      margin: 0 0.6rem 0 0;
      img {
        width: 0.4rem;
        height: 0.4rem; 
      }
    }
  }

  .copyright {
    position: relative;
    font-size: 0.32rem;
    color: rgba(255, 255, 255, 0.5);
    margin: 0.2rem 0 0;
    padding: 0.2rem 0 0 0;
    &::before {
      content: "";
      display: block;
      position: absolute;
      left: 0;
      right: 0;
      top: 0;
      width: 100%;
      height: 1px;
      background: rgba(255, 255, 255, 0.1);
      transform: scaleY(0.5);
    }
  }
}

/deep/.vertical-center-modal {
  display: flex;
  align-items: center;
  justify-content: center;

  .ivu-modal {
    top: 0;
  }
}

/deep/.ivu-modal-content {
  .ivu-modal-footer {
    border: none;
    padding: 0;
  }
  .ivu-modal-body {
    .links {
      display: flex;
      flex-wrap: wrap;
      height: 222px;
      li {
        width: 122px;
        height: 50%;
        display: flex;
        justify-content: center;
        align-items: center;
        a {
          display: flex;
          flex-direction: column;
          align-items: center;
          color: #4349be;
          font-size: 12px;
          position: relative;
          &.WeChata:hover .WeChat {
            display: block;
          }
          &.qqa:hover .qq {
            display: block;
          }
          img {
            width: 24px;
            height: 24px;
            &.qq,
            &.WeChat {
              position: absolute;
              right: -100px;
              width: 100px;
              height: auto;
              z-index: 9;
              top: -25px;
              display: none;
            }
            &.qq {
              top: -60px;
            }
          }
        }
      }
    }
  }
}
</style>
