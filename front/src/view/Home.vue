<template>
  <div class="wrap">
    <div class="head-part">
      <div class="info-wrap">
        <div class="info clear">
          <section class="left">
            <h3>{{$t('home.blockchinaInfo.title')}}</h3>
            <div class="box -left-box" style>
              <ul>
                <li class="clear" style="border-bottom: 0; border-top: 0; border-radius: 10px;">
                  <div class="name left">{{$t('home.blockchinaInfo.totalSupply')}}</div>
                  <div class="value right">{{blockInfo.totalAmount}}</div>
                </li>
                <li class="clear" style="border-bottom: 0; border-top: 0;">
                  <div class="name left">{{$t('home.blockchinaInfo.transactions')}}</div>
                  <div class="value right">{{blockInfo.totalTxNum}}</div>
                </li>
                <li class="clear" style="border-bottom: 0; border-top: 0;">
                  <div class="name left">{{$t('home.blockchinaInfo.blockHeight')}}</div>
                  <div class="value right">{{blockInfo.height}}</div>
                </li>
                <li class="clear" style="border-bottom: 0; border-top: 0;">
                  <div class="name left">{{$t('home.blockchinaInfo.blockReward')}}</div>
                  <div class="value right">{{blockInfo.reward}}</div>
                </li>
              </ul>
            </div>
          </section>
          <aside class="right">
            <h3>{{$t('home.valueInfo.title')}}</h3>
            <div class="box -right-box" style>
              <ul>
                <li class="clear" style="border-bottom: 0; border-top: 0;">
                  <div class="name left">{{$t('home.valueInfo.price')}}</div>
                  <div class="value right">- -</div>
                </li>
                <li class="clear" style="border-bottom: 0; border-top: 0;">
                  <div class="name left">{{$t('home.valueInfo.priceBTC')}}</div>
                  <div class="value right">- -</div>
                </li>
                <li class="clear" style="border-bottom: 0; border-top: 0;">
                  <div class="name left">{{$t('home.valueInfo.change')}}</div>
                  <div class="value right">- -</div>
                </li>
              </ul>
            </div>
          </aside>
        </div>
      </div>
    </div>
    <main style="margin-top: 0;">
      <!-- blocks -->
      <div class="home-part">
        <header>
          <div class="title">{{$t('home.blocks.title')}}</div>
          <div class="more" @click="moreClick(1)">
            <router-link class="more-a" to="/blocks">
              {{$t('home.blocks.more')}}
              <i v-if="false" class="icon-color el-icon-caret-right"></i>
            </router-link>
          </div>
        </header>
        <div class="table-wrap-home">
          <el-table :data="blocks" style="width: 100%;">
            <el-table-column align="center" :label="$t('home.blocks.height')">
              <template slot-scope="scope">
                <router-link :to="'/blockDetails/'+scope.row.blockNum">{{scope.row.blockNum}}</router-link>
              </template>
            </el-table-column>
            <el-table-column align="center" prop="blockTime" :label="$t('home.blocks.age')">
              <template slot-scope="scope">
                <timeago :since="scope.row.blockTime" :locale="getBusLocal"></timeago>
              </template>
            </el-table-column>
            <el-table-column align="center" :label="$t('home.blocks.miner')">
              <template slot-scope="scope">
                <router-link :to="'/address?minerName='+scope.row.minerName">{{scope.row.minerName}}</router-link>
              </template>
            </el-table-column>
            <el-table-column align="center" prop="rewards" :label="$t('home.blocks.reward')"></el-table-column>
          </el-table>
        </div>
      </div>

      <!-- transaction -->
      <div class="home-part">
        <header>
          <div class="title">{{$t('home.transaction.title')}}</div>
          <div class="more" @click="moreClick(2)">
            <router-link class="more-a" to="/transaction">
              {{$t('home.transaction.more')}}
              <i v-if="false" class="icon-color el-icon-caret-right"></i>
            </router-link>
          </div>
        </header>
        <div class="table-wrap-home">
          <el-table :data="transaction" style="width: 100%;">
            <el-table-column
              align="center"
              :label="$t('home.transaction.txHash')"
              show-overflow-tooltip
            >
              <template slot-scope="scope">
                <router-link
                  :to="'/transfer_details/'+scope.row.trxId+'/'+scope.row.opType"
                >{{scope.row.trxId}}</router-link>
              </template>
            </el-table-column>
            <el-table-column align="center" :label="$t('home.transaction.block')" width="180">
              <template slot-scope="scope">
                <router-link :to="'/blockDetails/'+scope.row.blockNum">{{scope.row.blockNum}}</router-link>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              :label="$t('home.transaction.types')"
              width="180"
              show-overflow-tooltip
              :formatter="getTypeName"
            ></el-table-column>
            <el-table-column align="center" :label="$t('home.transaction.age')" width="180">
              <template slot-scope="scope">
                <timeago :since="scope.row.trxTime" :locale="getBusLocal" :auto-update="0.5"></timeago>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              prop="amountStr"
              :label="$t('home.transaction.value')"
              width="180"
            ></el-table-column>
            <el-table-column align="center" :label="$t('home.transaction.fee')" width="180">
              <template slot-scope="scope">
                {{scope.row.feeStr}}
                <img
                  v-if="scope.row.guaranteeUse"
                  class="feeShow"
                  src="../assets/img/shouxufei.png"
                />
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </main>
    <!-- echarts -->
    <div class="line">
      <div class="line-wrap">
        <header>
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
          <div class="time">
            <span>{{$t('home.transactionLine.summary')}}</span>
          </div>
        </header>
        <div ref="echarts" class="echarts"></div>
      </div>
    </div>
    <footer>
      <div>Copyright @2018-2019 Whitecoin | All rights reserved</div>
    </footer>
  </div>
</template>

<script>
import bus from "../utils/bus";
import common from "../utils/common";
import typeObj from "../utils/type";

var echarts = require("echarts");
export default {
  name: "home",
  data() {
    return {
      blockInfo: {
        totalAmount: null, //总金额
        height: null, //区块最新高度
        totalTxNum: null, //交易总量
        reward: null //奖金
      },
      blocks: [],
      transaction: [],
      timeChoice: 1,
      choiceTime: {
        startTime: "",
        endTime: ""
      },
      option: {
        background: "#ffffff",
        tooltip: {},
        grid: {
          width: "95%",
          left: "3%"
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
            symbolSize: 16,
            lineStyle: {
              color: "#7575b1",
              width: 5
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
      }
    });
    this.$axios.post("/getTrxance").then(function(res) {
      let data = res.data;
      if (data.retCode === 200) {
        that.transaction = data.data;
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
  .head-part {
    position: relative;
    padding: 240px 0 100px;
    background-image: white;
    color: #0b0e2e;
    background-size: 100% 100%;
    .info-wrap {
      width: 77%;
      min-width: 1160px;
      margin: auto;
      position: relative;
      .search-wrap {
        position: absolute;
        top: -100px;
        right: 0;
      }
      .info {
        width: 70%;
        min-width: 1080px;
        margin: auto;
        color: black;
        h3 {
          width: 165px;
          font-size: 24px;
          display: inline-block;
          border-bottom: 2px solid white;
        }
        ul {
          position: relative;
        }
        li {
          width: 428px;
          padding: 24px 0 8px;
        }
        .cross_asset {
          position: absolute;
          width: auto;
          min-width: 1160px;
          top: 100%;
          left: 0;
          /*white-space: nowrap;*/
          .name,
          .value {
            display: inline-block;
            vertical-align: top;
          }
          .value {
            margin-left: 40px;
            max-width: 1000px;
          }
        }
        section li,
        aside li {
          border-bottom: 1px solid rgba(255, 255, 255, 0.18);
          &:first-of-type {
            border-top: 1px solid rgba(255, 255, 255, 0.18);
          }
        }
        .info section li {
          border-bottom: 0 !important;
          border-top: 0 !important;
        }
      }
    }
  }
}
main {
  width: 77%;
  min-width: 1160px;
  margin: 120px auto;
  position: relative;
  .home-part {
    // margin-top: 60px;
    header {
      margin-bottom: 24px;
      display: flex;
      box-sizing: border-box;
      div {
        flex: 1;
      }
      .title {
        color: #132241;
        font-size: 36px;
        font-weight: bold;
      }
      .more {
        text-align: right;
        cursor: pointer;
        position: relative;
        .more-a {
          position: absolute;
          display: block;
          bottom: 0;
          right: 0;
          font-size: 16px;
          color: #160f2e;
          padding: 6px 12px 6px 20px;
          // border: 1px solid #8f50b4;
          border-radius: 6px;
          .icon-color {
            color: #8f50b4;
          }
        }
      }
    }
  }
}
.line {
  box-sizing: border-box;
  background: white;
  color: black;
  margin-top: 60px;
  padding: 40px 0;
  .line-wrap {
    width: 77%;
    min-width: 1160px;
    margin: auto;
    header {
      margin-bottom: 24px;
      display: flex;
      padding: 0 30px;
      box-sizing: border-box;
      div {
        font-size: 16px;
      }
      .title {
        flex: 1;
        color: rgba(0, 0, 0, 0.5);
        // border-bottom: 1px solid rgba(0, 0, 0, 0.32);
        span {
          display: inline-block;
          padding: 12px 16px;
          font-size: 16px;
          cursor: pointer;
        }
        .timeChioce {
          font-weight: bold;
          position: relative;
          color: #6079b3;
          background: #e8f1fc;
          border-radius: 10px;
          &::after {
            position: absolute;
            display: block;
            content: "";
            width: 100%;
            height: 5px;
            background: white;
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
      height: 400px;
    }
  }
}
footer {
  width: 100%;
  height: 200px;
  line-height: 200px;
  text-align: center;
  background: rgba(11, 14, 46, 1);
  color: #492d95;
}

.-left-box {
  //  background:linear-gradient(108deg,rgba(65,64,125,1),rgba(94,106,179,1));
  color: white;
  padding: 20px;
  border-radius: 10px;
  height: 200px;
  background-image: url(/static/img/browser_b_bg.png);
  background-repeat: no-repeat;
}
.-right-box {
  // background: linear-gradient(108deg, #395593, #4977b8);
  color: white;
  padding: 25px;
  border-radius: 10px;
  height: 200px;
  background-image: url(/static/img/browser_v_bg.png);
  background-repeat: no-repeat;
}
</style>
