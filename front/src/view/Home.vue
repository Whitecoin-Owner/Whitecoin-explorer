<template>
  <div class="wrap" >
    <div class="search">
      <div class="search_con">
        <div class="ser_left">
          <h2>The Whitecoin Blockchain Explper</h2>
          <Search/>
          <p>Your friendly gate into the Whitecoin world</p>
        </div>
        <div class="ser_right"><img src="../assets/img/ser_right.png" alt=""></div>
      </div>
    </div>
    <div class="content_all">
      <div class="head-part">
        <div class="part_left">
          <div class="part_con">
            <div class="con_top">
              <p class="p1">XWC {{$t('home.valueInfo.price')}}</p>
              <p class="p2">${{mainCoinPrice.in_usdt.price}} <span>({{mainCoinPrice.in_usdt.change}}%)</span></p>
            </div>
            <div class="con_top">
              <p class="p1">{{$t('home.blockchinaInfo.totalSupply')}}</p>
              <p class="p2">{{blockInfo.totalAmount}}</p>
            </div>
          </div>
          <div class="part_con">
            <div class="con_top">
              <p class="p1">{{$t('home.blockchinaInfo.blockHeight')}}</p>
              <p class="p2">{{blockInfo.height}}</p>
            </div>
            <div class="con_top">
              <p class="p1">{{$t('home.blockchinaInfo.blockReward')}}</p>
              <p class="p2">{{blockInfo.reward}}</p>
            </div>
          </div>
          <div class="part_con">
            <div class="con_top">
              <p class="p1">{{$t('home.blockchinaInfo.transactions')}}</p>
              <p class="p2">{{blockInfo.totalTxNum}}</p>
            </div>
            <div class="con_top">
              <p class="p1">{{$t('home.blockchinaInfo.accountCount')}}</p>
              <p class="p2">1,000,000,000 XWC</p>
            </div>
          </div>
        </div>
        <div class="part_right">
          <!-- echarts -->
          <div class="line">
            <div class="line-wrap">
              <header>
                <div class="line_tit">{{$t('home.transactionLine.summary')}}</div>
                <div class="title">
                  <span
                    @click="timeChange(0)"
                    :class="{'timeChioce':timeChoice===0}"
                  >{{$t('home.transactionLine.today')}}</span>
                  <span
                    @click="timeChange(1)"
                    :class="{'timeChioce':timeChoice===1}"
                  >{{$t('home.transactionLine.week')}}</span>
                  <span
                    @click="timeChange(2)"
                    :class="{'timeChioce':timeChoice===2}"
                  >{{$t('home.transactionLine.month')}}</span>
                </div>
              </header>
              <div ref="echarts" class="echarts"></div>
            </div>
          </div>
        </div>
      </div>
      <main>
        <div class="main_block">
          <h2><router-link to="/blocks">{{$t('home.blocks.title')}}</router-link></h2>
          <div class="block_ul">
            <div class="block_li" v-for="(item,index) of blocks" :key="index">
              <div class="block_img"><img src="../assets/img/block_img1.png" alt=""></div>
              <div class="block_er">
                <p><router-link :to="'/blockDetails/'+item.blockNum">{{item.blockNum}}</router-link></p>
                <p><timeago :since="item.blockTime" :locale="getBusLocal" :auto-update="0.5"></timeago></p>
              </div>
              <div class="block_san">
                <p><router-link :to="'/address?minerName='+item.minerName"><span>Miner</span> {{item.minerName}}</router-link></p>
                <p>{{item.seconds}}s {{$t('home.blocks.over')}} {{item.trxCount}} {{$t('home.blocks.success')}}</p>
              </div>
              <div class="block_si">
                <p><span>{{item.rewards}}</span></p>
                <p>{{$t('home.blocks.minerFee')}}</p>
              </div>
            </div>
            <div class="block_more">
              <router-link to="/blocks">{{$t('home.blocks.more')}}</router-link>
            </div>
          </div>
        </div>
        <div class="main_block1">
          <h2><router-link to="/blocks">{{$t('home.transaction.title')}}</router-link></h2>
          <div class="block_ul">
            <div class="block_li" v-for="(item,index) of transaction" :key="index">
              <div class="block_img"><img src="../assets/img/block_img2.png" alt=""></div>
              <div class="block_er">
                <p><router-link
                    :to="'/transfer_details/'+item.trxId+'/'+item.opType"
                  >{{item.trxId.substring(0,15)}}...</router-link></p>
                <p><timeago :since="item.trxTime" :locale="getBusLocal" :auto-update="0.5"></timeago></p>
              </div> 
              <div class="block_san">
                <p><span>FROM:</span> <router-link :to="`/address?address=${item.fromAccount}`">{{item.fromAccount !== null ? ( item.fromAccount!=='Mining'? (item.fromAccount.substring(0,11) + ('...')) : item.fromAccount ) : ($t('home.transaction.fromDeafult')) }} </router-link></p>
                <p class="toAccount"><span>TO:</span> <router-link :to="`/address?address=${item.toAccount}`"> {{item.toAccount!==null? (item.toAccount.substring(0,16) + '...') : '--'}} </router-link></p>
              </div>
              <div class="block_si">
                <p><span>{{item.amountStr !== null ? item.amountStr : '--'}}</span></p>
                <p>{{$t('home.transaction.numberss')}}</p>
              </div>
            </div>
            <div class="block_more">
              <router-link to="/transaction">{{$t('home.blocks.more')}}</router-link>
            </div>
          </div>
        </div>
      </main>
    </div>
  </div>
</template>

<script>
import bus from "../utils/bus";
import common from "../utils/common";
import typeObj from "../utils/type";
import Search from "../components/search/Search";
import mixin from '../utils/mixin';

var echarts = require("echarts");
export default {
  mixins: [mixin],
  name: "home",
  // inject: ['isMobile'],
  components:{Search},
  data() {
    return {
      blockInfo: {
        totalAmount: null, //总金额
        height: null, //区块最新高度
        totalTxNum: null, //交易总量
        reward: null //奖金
      },
      mainCoinPrice: {
        in_usdt: 0, // 主链资产以USDT计价的价格
        in_btc: 0, // 主链资产以BTC计价的价格
      },
      blocks: [],
      transaction: [],
      timeChoice: 1,
      choiceTime: {
        startTime: "",
        endTime: ""
      },
      option: {
        background: "#D96EED",
        tooltip: {},
        grid: {
          width: "95%",
          left: "3%",
          x: 0,
          y: 10,
          x2: 10,
          y2: 10,
        },
        xAxis: {
          show: true,
          type: "time",
          axisLine: {
            show: true,
            lineStyle: {
              color: "rgba(255,255,255,.3)"
            }
          },
          axisTick: {
            show: false
          },
          splitLine: {
            show: false
          },
          axisLabel: {
            color: "rgba(255,255,255,.3)",
            fontSize: 14
          },
          axisPointer: {
            show: true,
            snap: true,
            lineStyle: {
              color: "#ddeeff",
              opacity: 0
            },
            label: {
              show: false,
              backgroundColor: "#7575b1"
            }
          }
        },
        yAxis: {
          show: false,
          type: "value",
          axisLine: {
            show: false
          },
          axisTick: {
            show: false
          },
          axisLabel: {
            color: "#e5e500",
            fontSize: 14
          },
          splitLine: {
            lineStyle: {
              color: "#e5e500"
            }
          }
        },
        series: [
          {
            data: [
              ["2018-05-10", 25],
              ["2018-05-11", 62],
              ["2018-05-12", 165],
              ["2018-05-13", 249],
              ["2018-05-14", 106],
              ["2018-05-15", 187],
              ["2018-05-16", 88]
            ],
            type: "line",
            name: "volumes of transactions",
            symbol: "circle",
            symbolSize: 10,
            lineStyle: {
              color: "#7575b1",
              width: 2
            },
            itemStyle: {
              color: "#7575b1"
            },
            areaStyle: {
              color: {
                type: "linear",
                x: 0,
                y: 0,
                x2: 0,
                y2: 1,
                colorStops: [
                  {
                    offset: 0,
                    color: "#6079B3" // 0% 处的颜色
                  },
                  {
                    offset: 1,
                    color: "white" // 100% 处的颜色
                  }
                ],
                globalCoord: false // 缺省为 false
              }
            }
          }
        ]
      }
    };
  },
  created() {
    bus.navChoice = 0;
    let that = this;
    this.$axios.post("/getStatis").then(function(res) {
      let data = res.data;
      if (data.retCode === 200) {
        that.blockInfo = data.data;
        for (let item in that.blockInfo) {
          that.blockInfo[item] = that.blockInfo[item] || "- -";
        }
      }
    });
    this.$axios.post("/blocksInfo").then(function(res) {
      let data = res.data;
      if (data.retCode === 200) {
        that.blocks = data.data;
        console.log(data.data,'000')
      }
    });
    this.$axios.post("/getTrxance").then(function(res) {
      let data = res.data;
      if (data.retCode === 200) {
        data.data.forEach(item=>{
          // item.amountStr 截取小数点后四位
          if(item.amountStr !==null && item.amountStr.indexOf('.') !== -1){
            item.amountStr = parseFloat(item.amountStr) //吧字符串的数字取出
            item.amountStr = (item.amountStr).toString().replace(/^(.*\..{4}).*$/,"$1") + ' XWC'  //进行截取
          }       
        })
        that.transaction = data.data;
        console.log(data.data,'iii')
      }
    });
    this.$axios.get('/mainCoinPrice').then((res) => {
      let data = res.data;
      if(data.retCode === 200) {
        that.mainCoinPrice = data.data;
      }
    });
  },
  mounted() {
    this.timeChange(1);
  },
  methods: {
    moreClick(num) {
      bus.navChoice = num;
    },
    getTypeName(row) {
      return typeObj[row.opType];
    },
    getLineData(url) {
      let that = this;
      this.$axios.post(url, this.choiceTime).then(function(res) {
        let data = res.data;
        if (data.retCode === 200) {
          that.option.series[0].data = data.data;
          that.drowLine();
        }
      });
    },
    drowLine() {
      let echart = echarts.init(this.$refs.echarts);
      echart.setOption(this.option);
    },
    timeChange(num) {
      this.timeChoice = num;
      switch (num) {
        case 0:
          this.choiceTime.startTime =
            common.format(new Date(), "yyyy-MM-dd") + " 00:00:00";
          this.choiceTime.endTime = common.format(
            new Date(),
            "yyyy-MM-dd hh:mm:ss"
          );
          this.choiceTime.queryTime = "";
          this.getLineData("/getHourTrxNum");
          break;
        case 1:
          this.choiceTime.startTime = common.initDefaultDate(-7, "d");
          this.choiceTime.endTime = common.format(new Date(), "yyyy-MM-dd");
          this.choiceTime.queryTime = "";
          this.getLineData("/getDayTrxNum");
          break;
        case 2:
          this.choiceTime.startTime = common.initDefaultDate(-1, "M");
          this.choiceTime.endTime = common.format(new Date(), "yyyy-MM-dd");
          this.choiceTime.queryTime = "";
          this.getLineData("/getDayTrxNum");
          break;
      }
    }
  },
  computed: {
    getBusLocal() {
      return bus.local;
    }
  }
};
</script>

<style lang="less" scoped>
.wrap {
  height: 100%;
  .search{
    background: url(../assets/img/search_bg.png) no-repeat top;
    background-size: 100% 100%;
    overflow: hidden;
    padding: 40px 0 90px 0;
    box-sizing: border-box;
    .search_con{
      width: 1140px;
      margin: 0 auto;
      display: flex;
      justify-content: space-between;
      align-items: center;

      .ser_left{
        h2{
          color: #fff;
          margin-bottom: 20px;
          font-size: 28px;
        }
        p{
          color: #fff;
          margin-top: 20px;
          font-weight: normal;
          font-size: 18px;
        }
      }
      .ser_right{
        img{
          width: 300px;
        }
      }
    }
  }
  .content_all{
    width: 1140px;
    margin: 0 auto;
  }
  .head-part {
    position: relative;
    background:#fff;
    box-shadow: 0px 2px 13px 0px rgba(0, 0, 0, 0.09);
    border-radius: 5px;
    color: #0b0e2e;
    background-size: 100% 100%;
    margin-top: -50px;
    padding: 20px 0 0 10px ;
    display: flex;
    justify-content: space-between;
    margin-bottom: 20px;
    .part_left{
      display: flex;
      width: 65%;
      .part_con{
        padding:0 20px;
        border-right:1px solid #EEEEEE;
        &:first-child{
          width: 26%;
        }
        &:nth-child(2){
          width: 37%;
        }
        &:last-child{
          width: 37%;
        }
        .con_top{
          margin-bottom: 20px;
          &:first-child{
            border-bottom:1px #EEEEEE solid
          }
          .p1{
            font-size: 14px;
            color: #677897;
            margin-bottom: 15px;
          }
          .p2{
            font-size: 18px;
            color: #333333; 
            margin-bottom: 15px;
            span{
              color: #34BD5A;
              font-size: 14px;
              margin-left: 5px;
            }
          }
        }
      }
    }
    .part_right{
      width: 35%;
    }
  }
}
main {
  width: 100%;
  display: flex;
  justify-content: space-between;
  margin-bottom: 50px;
  .main_block,.main_block1{
    background: #FFFFFF;
    box-shadow: 0px 2px 13px 0px rgba(0, 0, 0, 0.09);
    border-radius: 5px;
    padding: 20px 0;
    box-sizing: border-box;
    h2{
      color: #333333;
      font-size: 18px;
      font-weight: bold;
      padding:0 0 20px 20px;
      border-bottom: 1px solid #EEEEEE;
    }
    .block_ul{
      padding: 10px 20px 0px 20px;
      .block_li{
        display: flex;
        align-items: center;
        justify-content: space-between;
        border-bottom: 1px solid #EEEEEE;
        padding: 10px 0;
        .block_img{
          width: 15%;
          img{
            width: 46px ;
            height: 46px;
          }
        }
        .block_er,.block_san,.block_si{
          width: 27%;
          p:first-child{
            color: #0279FF;
            font-size: 16px;
            margin-bottom: 5px;
            span{
              color: #333333;
            }
          }
          p:last-child{
            color: #677897;
            font-size: 14px;
            &.toAccount{
              color: #0279FF;
              span{
                color: #333333;
              }
            }
          }
        }
        .block_san{
          width: 35%;
          P{
            cursor: pointer;
          }
        }
        .block_si{
          width: 23%;
        }
      }
      .block_more{
        margin-top: 20px;
        a{
          line-height: 46px;
          display: block;
          background: #735DFF;
          border-radius: 4px;
          color: #fff;
          text-align: center;
          font-size: 16px;
          &:hover{
            background: #403682;
          }
        }
      }
    }
  }
  .main_block{
    width: 42%;
  }
  .main_block1{
    width: 56%;
  }
}
.line {
  box-sizing: border-box;
  color: black;
  .line-wrap {
    padding: 0 20px;
    header {
      display: flex;
      box-sizing: border-box;
      justify-content: space-between;
      align-items: center;
      .line_tit{
        color: #677897;
        font-size: 14px;
      }
      .title {
        display: flex;
        span {
          display: inline-block;
          font-size: 12px;
          line-height: 25px;
          padding: 0 8px;
          cursor: pointer;
          background: #F5F5F5;
          color: #808080;
        }
        .timeChioce {
          font-weight: bold;
          position: relative;
          color: #4B4BF0;
          background: #D5D5FF;
          &::after {
            position: absolute;
            display: block;
            content: "";
            width: 100%;
            background: #D5D5FF;
            bottom: 0;
            left: 0;
            transform: translateY(50%);
          }
        }
      }
      .time {
        width: 240px;
        position: relative;
        color: black;
        font-size: 24px;
        span {
          position: absolute;
          display: block;
          bottom: 0;
          right: 0;
          transform: translateY(50%);
        }
      }
    }
    .echarts { 
      width: 100%;
      height: 130px;
    }
  }
}

</style>
