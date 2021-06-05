<template>
  <div>
    <div class="footer">
      <div class="footer-bom">
        <div class="top-foot">
          <div class="foot_left">
            <img src="../../assets/imagesMobile/logo_footer.png" alt />
            <p>Copyright © 2019 www.whitecoin.info All Rights Reserved.</p>
          </div>
          <div class="foot_right">
            <ul>
              <li>
                <a :href="pdfUrl" target="_blank">{{ $t('home.footer.t1') }}</a>
              </li>
              <li>
                <a href="mailto:info@whitecoin.info">{{ $t('home.footer.t2') }}</a>
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
            <div class="icon">
              <li>
                <a href="mailto:info@whitecoin.info" rel="nofollow" target="_blank">
                  <img src="../../assets/imagesMobile/footer_icon01.png" alt />
                </a>
              </li>
              <li>
                <a href="https://github.com/Whitecoin-XWC/Whitecoin-core" rel="nofollow" target="_blank">
                  <img src="../../assets/imagesMobile/footer_icon02.png" alt />
                </a>
              </li>
              <li>
                <a href="https://twitter.com/WhiteCoiner" rel="nofollow" target="_blank">
                  <img src="../../assets/imagesMobile/footer_icon03.png" alt />
                </a>
              </li>
              <li>
                <a href="https://weibo.com/u/6395981275" rel="nofollow" target="_blank">
                  <img src="../../assets/imagesMobile/footer_icon04.png" alt />
                </a>
              </li>
              <li>
                <a
                  :href="isEn == 'en' ? 'http://t.me/xwc_whitecoin' : 'https://t.me/whitecoincn'"
                  rel="nofollow"
                  target="_blank"
                >
                  <img src="../../assets/imagesMobile/footer_icon05.png" alt />
                </a>
              </li>
              <li>
                  <img src="../../assets/imagesMobile/footer_icon06.png" alt />
                  <div class="button-item">
                    <img  class="img" src="../../assets/img/Wechat.jpeg" alt/>
                  </div>
              </li>
              <li>
                  <img src="../../assets/imagesMobile/footer_icon07.png" alt />
                  <div class="button-item">
                    <img  class="img" src="../../assets/img/QQ.jpeg" alt />
                  </div>
              </li>
            </div>
          </div>
        </div>
        <div class="linkS">
          <div class="linkS-inner">
            <span>{{ $t('home.footer.t5') }}：</span>
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
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      isEn: this.$i18n.locale,
      modalShow: false,
      pdfUrl:
        this.$i18n.locale == "cn"
          ? "/pdf/Whitecoin Technical White Paper_cn.pdf"
          : "/pdf/Whitecoin Technical White Paper_en.pdf"
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
    this.baidu();
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
  .footer-bom{
    background: #403682;
    .top-foot {
      padding: 40rem;
      border-bottom: 1px solid rgba(255, 255, 255, 0.1);
      .foot_left {
        img{
          width: 180rem;
          margin-bottom: 25rem;
        }
        p {
          font-size: 22rem;
          color: #fff;
          margin-top: 10rem;
        }
      }
      .foot_right {
        margin-top: 33rem;
        .icon {
          display: flex;
          justify-content: space-around;
          align-items: center;
          li {
            margin-right: 24rem;
            display: flex;
            justify-content:space-between;
            align-items: center;
            img {
              width: 45rem;
            }
            .button-item{
              position: relative;
              .img{
                width: 130rem;
                position: absolute;
                top: 30rem;
                transition: all .5s;
                opacity: 0;
                left: -90rem;
                visibility: hidden;
                z-index: 999;
              }
            }
          }
          li:nth-child(6):hover,li:last-child:hover{
            .button-item{
              .img{
                opacity: 1;
                visibility: visible;
              }
            }
          }
        }
        ul {
          margin-top: 14rem;
          margin-bottom: 38rem;
          display: flex;
          li {
            a{
              font-size: 28rem;
              font-weight:500;
              color:#F3F3F3;
              margin-right: 40rem;
            }
          }
        }
      }
    }
    .linkS {
      padding:20rem 40rem;
      .linkS-inner{
        font-size: 22rem;
        color: #8F9DB7;
      }
      span {
        display: inline-block;
        white-space: nowrap;
        margin-bottom: 20rem;
      }
      ul {
        display: flex;
        flex-wrap: wrap;
        li {
          margin-right:15rem;
          a {
            font-size: 22rem;
            color:#8F9DB7;
          }
        }
      }
    }
  }
}

</style>
