<template>
  <div class="wrap">
    <div class="tr_main">
      <div class="con_top">
        <p>
          {{$t('address.overview.title')}} 
          <span class="address_link">Address:{{minerInfo.address}} <span
                    style="color: red;"
                    v-if="minerInfo.address==='XWCNWKLUcsybWt4bW5EXV1CfdaSNHiSKj4Hzw' || minerInfo.address==='XWCNi146ffqUffGJk3tTjnY1MdVGJn3m8jH29'"
                  >({{$t('address.overview.abnormal_address')}})</span></span>
          <span class="copy"></span>
        </p>
        <Search class="search_con"/>
      </div>
      <div class="con_all">
        <div class="all_section">
          <div class="left"> 
            <ul>
              <li>
                <span>{{$t('address.overview.name')}}</span>
                <span>{{minerInfo.name}}</span>
              </li>
              <li>
                <span>{{$t('miner.overview.transaction')}}</span>
                <span>{{minerInfo.transaction}}</span>
              </li>  
              <li>
                <span>{{$t('address.overview.lockBalance')}}</span>
                <span>
                  <template v-for="(item, index) in minerInfo.lockBalances">
                    {{item}}
                    <template v-if="index < minerInfo.lockBalances.length - 1">,</template>
                  </template>
                </span>
              </li>
              <!-- <li>
                <span>{{$t('address.overview.contracts')}}</span>
                <span>{{minerInfo.contracts}}</span>
              </li> -->
            </ul>
          </div>
          <div class="right">
            <ul>
              <li>
                <span v-if="minerInfo.balances.length>1">{{$t('address.overview.balances')}}</span>
                <span v-else>XWC{{$t('address.overview.balances')}}</span>
                <span>
                  <template v-for="(item, index) in minerInfo.balances">
                    {{item}}
                    <template v-if="index < minerInfo.balances.length - 1">,</template>
                  </template>
                </span>
              </li>
              <li>
                <span>代币余额:</span>
                <span>28848.02000198 <em class="tp_click">TP <strong></strong></em> </span>
              </li>
            </ul>
          </div>
        </div>
        <div class="ser_input">
          <div class="ser_icon">
            <span></span>
            <input type="text" placeholder="请输入代币名称或合约地址">
          </div>
          <div class="daibi" v-for="(item,index) of tokenBalances.data" :key="index">
            <div class="daibi_li" >
              <img src="../assets/img/cusd.png" alt="">
              <div class="daibi_p">
                <p>{{item.tokenSymbol}}</p>
                <p>{{item.tokenContract}}...</p>
              </div>
            </div>
          </div>
        </div>
        <div class="all_aside">
          <div class="all_header">
            <div>
              <span
                @click="choiceFlagChange(0)"
                :class="{'choice':choiceFlag===0}"
              >{{$t('address.myTransactions.title')}}</span>
              <span
                @click="choiceFlagChange(2)"
                :class="{'choice':choiceFlag===2}"
              >{{$t('address.myTokenTransactions.title')}}</span>
              <!-- <span
                @click="choiceFlagChange(3)"
                :class="{'choice':choiceFlag===3}"
              >{{$t('address.mySwapTransactions.title')}}</span>
              <span
                @click="choiceFlagChange(4)"
                :class="{'choice':choiceFlag===4}"  
              >Tokens</span>
              <span
                @click="choiceFlagChange(1)"
                :class="{'choice':choiceFlag===1}"
              >{{$t('address.myContracts.title')}}</span> -->
            </div>
            <!-- <div class="total">A Total Of {{total}} transactions found</div> -->
          </div>
          <div v-if="choiceFlag===0" class="table-wrap">
            <el-table :data="transaction" style="width: 100%">
              <el-table-column
                align="center"
                :label="$t('address.myTransactions.txHash')"
                show-overflow-tooltip
              >
                <template slot-scope="scope">
                  <router-link
                    :to="'/transfer_details/'+scope.row.trxId+'/'+scope.row.opType"
                  >{{scope.row.trxId}}</router-link>
                </template>
              </el-table-column>
              <el-table-column align="center" :label="$t('address.myTransactions.block')" width="80">
                <template slot-scope="scope">
                  <router-link :to="'/blockDetails/'+scope.row.blockNum">{{scope.row.blockNum}}</router-link>
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                :label="$t('address.myTransactions.types')"
                :formatter="getTypeName"
                width="80"
                show-overflow-tooltip
              ></el-table-column>
              <el-table-column
                align="center"
                :label="$t('address.myTransactions.age')"
                width="120"
                show-overflow-tooltip
              >
                <template slot-scope="scope">
                  <timeago :since="scope.row.trxTime" :locale="getBusLocal"></timeago>
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                show-overflow-tooltip
                :label="$t('address.myTransactions.from')"
              >
                <template slot-scope="scope">
                  <span
                    class="link"
                    @click="_mixin_address_jump(scope.row.fromAccount)"
                  >{{scope.row.fromAccount}}</span>
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                show-overflow-tooltip
                :label="$t('address.myTransactions.to')"
              >
                <template slot-scope="scope">
                  <span
                    class="link"
                    @click="_mixin_address_jump(scope.row.toAccount)"
                  >{{scope.row.toAccount}}</span>
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                prop="amountStr"
                :label="$t('miner.myTransactions.value')"
                width="100"
                show-overflow-tooltip
              ></el-table-column>
              <el-table-column
                align="center"
                :label="$t('address.myTransactions.fee')"
                width="130"
                show-overflow-tooltip
              >
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
          <div v-if="choiceFlag===2" class="table-wrap">
            <el-table :data="tokenTransactions" style="width: 100%">
              <el-table-column
                align="center"
                :label="$t('address.myTransactions.txHash')"
                show-overflow-tooltip
              >
                <template slot-scope="scope">
                  <!-- invoke contract type is 79 -->
                  <router-link
                    :to="'/transfer_details/'+scope.row.trxId+'/'+79" 
                  >{{scope.row.trxId}}</router-link>
                </template>
              </el-table-column>
              <el-table-column align="center" :label="$t('address.myTransactions.block')" width="80">
                <template slot-scope="scope">
                  <router-link :to="'/blockDetails/'+scope.row.blockNum">{{scope.row.blockNum}}</router-link>
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                :label="$t('address.myTokenTransactions.symbol')"
                width="150"
                show-overflow-tooltip
              >
                <template slot-scope="scope">
                  <span>{{scope.row.symbol}}</span>
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                :label="$t('address.myTransactions.age')"
                width="120"
                show-overflow-tooltip
              >
                <template slot-scope="scope">
                  <timeago :since="scope.row.trxTime" :locale="getBusLocal"></timeago>
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                show-overflow-tooltip
                :label="$t('address.myTransactions.from')"
              >
                <template slot-scope="scope">
                  <span
                    class="link"
                    @click="_mixin_address_jump(scope.row.fromAccount)"
                  >{{scope.row.fromAccount}}</span>
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                show-overflow-tooltip
                :label="$t('address.myTransactions.to')"
              >
                <template slot-scope="scope">
                  <span
                    class="link"
                    @click="_mixin_address_jump(scope.row.toAccount)"
                  >{{scope.row.toAccount}}</span>
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                prop="amountStr"
                :label="$t('miner.myTransactions.value')"
                width="100"
                show-overflow-tooltip
              ></el-table-column>
              <el-table-column
                align="center"
                :label="$t('address.myTransactions.fee')"
                width="130"
                show-overflow-tooltip
              >
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

          <div v-if="choiceFlag===1" class="table-wrap">
            <el-table :data="contaracts" style="width: 100%" key="contaracts">
              <el-table-column
                align="center"
                :label="$t('address.myContracts.address')"
                show-overflow-tooltip
              >
                <template slot-scope="scope">
                  <i class="el-icon-circle-check-outline" v-if="scope.row.status === 2"></i>
                  <span
                    class="link"
                    @click="_mixin_address_jump(scope.row.contractAddress)"
                  >{{scope.row.contractAddress}}</span>
                </template>
              </el-table-column>
              <!--<el-table-column-->
              <!--align="center"-->
              <!--:formatter="getTypeName"-->
              <!--:label="$t('address.myContracts.types')"-->
              <!--show-overflow-tooltip>-->
              <!--</el-table-column>-->
              <el-table-column
                align="center"
                prop="callTimes"
                :label="$t('address.myContracts.call')"
                width="200"
              ></el-table-column>
              <el-table-column
                align="center"
                :formatter="dateFormate"
                :label="$t('address.myContracts.create')"
                width="220"
              ></el-table-column>
              <el-table-column
                align="center"
                show-overflow-tooltip
                :label="$t('address.myContracts.last')"
                width="220"
              >
                <template slot-scope="scope">
                  <timeago :since="scope.row.lastTime" :locale="getBusLocal" :auto-update="0.5"></timeago>
                </template>
              </el-table-column>
            </el-table>
          </div>

          <div v-if="choiceFlag===3" class="table-wrap">
            <el-table :data="swapTransactions" style="width: 100%">
              <el-table-column
                align="center"
                :label="$t('address.mySwapTransactions.txHash')"
                show-overflow-tooltip
              >
                <template slot-scope="scope">
                  <!-- invoke contract type is 79 -->
                  <router-link
                    :to="'/transfer_details/'+scope.row.trxId+'/'+79" 
                  >{{scope.row.trxId}}</router-link>
                </template>
              </el-table-column>
              <el-table-column align="center" :label="$t('address.myTransactions.block')" width="80">
                <template slot-scope="scope">
                  <router-link :to="'/blockDetails/'+scope.row.blockNum">{{scope.row.blockNum}}</router-link>
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="Event Name"
                width="250"
                show-overflow-tooltip
              >
                <template slot-scope="scope">
                  <span>{{scope.row.eventName}}</span>
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="Event Arg"
                width="500"
                show-overflow-tooltip
              >
                <template slot-scope="scope">
                  <span>{{scope.row.eventArg}}</span>
                </template>
              </el-table-column>
              
            </el-table>
          </div>

          <div v-if="choiceFlag===4" class="table-wrap">
            <el-table :data="tokenBalances" style="width: 100%">
              <el-table-column
                align="center"
                label="Symbol"
                show-overflow-tooltip
              >
                <template slot-scope="scope">
                  <span>{{scope.row.tokenSymbol}}</span>
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="Token Contract"
                show-overflow-tooltip
              >
                <template slot-scope="scope">
                  <span>{{scope.row.tokenContract}}</span>
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="Balance"
                show-overflow-tooltip
              >
                <template slot-scope="scope">
                  <span>{{scope.row.amount}}</span>
                </template>
              </el-table-column>
              
            </el-table>
          </div>

          <el-pagination
            class="pagination"
            layout="prev, pager, next, jumper"
            :current-page="page"
            :page-size="size"
            :total="total"
            @current-change="pageChange"
          ></el-pagination>
        </div>
      </div>

    </div>
  </div>
</template>

<script>
import bus from "../utils/bus";
import common from "../utils/common";
import typeObj from "../utils/type";
import mixin from "../utils/mixin";
import Search from "../components/search/Search";

export default {
  mixins: [mixin],
  name: "v-address",
  components:{Search},
  data() {
    return {
      page: 1,
      size: 10,
      total: 0,
      minerName: "",
      address: "",
      choiceFlag: 0,
      minerInfo: {
        name: "", //矿工名
        transaction: 0, //交易总量
        address: "", //矿工地址
        rewards: null, //奖励
        contracts: 0 //拥有合约量
      },
      transaction: [],
      tokenTransactions: [],
      swapTransactions: [],
      tokenBalances: [],
      contaracts: []
    };
  },
  beforeRouteUpdate(to, from, next) {
    if (to.query.address) {
      this.address = to.query.address;
      this.getDataByAddress();
    } else {
      this.minerName = to.query.minerName;
      this.getDataByMinerName();
    }
    next();
  },
  created() {
    console.log(this.$route.query.address,'000')
    if (this.$route.query.address) {
      this.address = this.$route.query.address;
      this.getDataByAddress();
    } else {
      this.minerName = this.$route.query.minerName;
      this.getDataByMinerName();
    };
  },
  methods: {
    pageChange(page) {
      this.page = page;
      if (this.choiceFlag === 0) {
        this.getTransactionData();
      } else if (flag === 2) {
        this.getTokenTransactionData();
      } 
      // else if(flag===3) {
      //   this.getSwapTransactionData();
      // } else if(flag===4) {
      //   this.getTokenBalancesData();
      // } else {
      //   this.getContractData();
      // }
    },
    getTypeName(row) {
      if (row.opType || row.opType === 0) {
        return typeObj[row.opType];
      } else {
        return typeObj[row.type];
      }
    },
    choiceFlagChange(flag) {
      this.choiceFlag = flag;
      this.page = 1;
      if (flag === 0) {
        this.getTransactionData();
      } else if (flag === 2) {
        this.getTokenTransactionData();
      } 
      // else if(flag===3) {
      //   this.getSwapTransactionData();
      // } else if(flag===4) {
      //   this.getTokenBalancesData();
      // } else {
      //   this.getContractData();
      // }
    },
    getDataByMinerName() {
      let that = this;
      this.$axios
        .post("/minerInfo", { name: this.minerName })
        .then(function(res) {
          let data = res.data;
          that.minerInfo = data.data;
          console.log(that.minerInfo,'pp')
          that.getTransactionData();
        });
    },
    getDataByAddress() {
      let that = this;
      this.$axios
        .post("/addrStatis", { address: this.address })
        .then(function(res) {
          let data = res.data;
          that.minerInfo = data.data;
          console.log(that.minerInfo,'ooo')
          that.getTransactionData();
        });
    },
    getTransactionData() {
      let that = this;
      this.$axios
        .post("/queryTrxByAddr", {
          address: this.minerInfo.address,
          page: this.page,
          rows: this.size
        })
        .then(function(res) {
          let data = res.data;
          that.transaction = data.data.rows;
          that.total = data.data.total;
        });
    },
    getTokenBalancesData() {
      let that = this;
      this.$axios
        .get(`/user_tokens/${this.address}`)
        .then(function(res) {
          let data = res.data;
          that.tokenBalances = data.data;
          console.log(that.tokenBalances,'3333')
          that.total = data.data.length;
        });
    },
    getTokenTransactionData() {
      let that = this;
      this.$axios
        .post("/queryTokenTrxByAddr", {
          address: this.minerInfo.address,
          page: this.page,
          rows: this.size
        })
        .then(function(res) {
          let data = res.data;
          that.tokenTransactions = data.data.rows;
          that.total = data.data.total;
        });
    },
    getSwapTransactionData() {
      let that = this;
      this.$axios
        .post("/querySwapTrxByAddr", {
          address: this.minerInfo.address,
          page: this.page,
          rows: this.size
        })
        .then(function(res) {
          let data = res.data;
          that.swapTransactions = data.data.rows;
          that.total = data.data.total;
        });
    },
    getContractData() {
      let that = this;
      this.$axios
        .post("/addrContracts", {
          ownerAddress: this.minerInfo.address,
          page: this.page,
          rows: this.size
        })
        .then(function(res) {
          let data = res.data;
          that.contaracts = data.data.rows;
          that.total = data.data.total;
        });
    },
    dateFormate(row) {
      return common.format(new Date(row.createTime), "yyyy-MM-dd hh:mm:ss");
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
  .tr_main {
    width: 1140px;
    margin: 0 auto;
    position: relative;
    color: black;
    .con_top{
      display: flex;
      align-items: center;
      justify-content: space-between;
      margin-top: 30px;
      p{
        font-size: 22px;
        color: #333333;
        font-weight: 600;
        display: flex;
        align-items: center;
        .normal&.choice{
          font-size: 22px;
        }
        .address_link{
          font-size: 16px;
          font-weight: normal;
          margin: 0 20px;
        }
        .copy{
          width: 29px;
          height: 29px;
          display: block;
          background: url(../assets/img/copy.png) no-repeat;
          background-size: 100%;
        }
      }
      .search_con{
        width: 500px;
      }
    }
    .con_all{
        background: #fff;
        box-shadow: 0px 2px 13px 0px rgba(0, 0, 0, 0.09);
        margin: 30px 0;
        padding: 30px;
        overflow: hidden;
        position: relative;
      }
    .search {
      position: absolute;
      right: 0;
      top: 0px;
    }
    h2 {
      font-size: 36px;
    }
    .all_section {
      overflow: hidden;
      display: flex;
      align-items: center;
      justify-content: space-between;
      ul li {
        width: 514px;
        box-sizing: border-box;
        border-bottom: 1px solid rgba(255, 255, 255, 0.5);
        padding: 12px;
        font-size: 14px;
        span {
          display: inline-block;
          
            position: relative;
          &:first-of-type {
            width: 140px;
          }
          .tp_click{
            margin-left: 10px;
            border-radius: 3px;
            border: 1px solid #B8C8DA;
            padding: 3px 23px 3px 5px;
            strong{
              position: absolute;
              background: url(../assets/img/xia.png) no-repeat;
              background-size: 100%;
              width: 13px;
              height: 13px;
              top: 3px;
              right: 5px;
            }
          }
        }
      }
    }
    .ser_input{
      position: absolute;
      width: 300px;
      top: 140px;
      right: 85px;
      z-index: 99999;
      background: #fff;
      box-shadow: 0px 0px 11px 0px rgba(0, 0, 0, 0.09);
      box-sizing: border-box;
      padding: 10px;
      border-radius: 5px;
      .ser_icon{
        display: flex;
        align-items: center;
        border: 1px solid #DBDBDB;
        border-radius: 5px;
        padding: 5px 10px;
        input{
          width: 100%;
          padding-left: 7px;
          outline: none;
          border: 0;
          line-height: 0;
          &::placeholder{
            padding-bottom: 3px;
            margin-bottom: 5px;
            color:#999999
          }
          &::-webkit-input-placeholder{
            padding-bottom: 5px;
          }
        }
        span{
          width: 15px;
          height: 15px;
          background: url(../assets/img/ser_icon.png) no-repeat;
          background-size: 100%;
        }
      }
      .daibi{
        margin-top: 5px;
        .daibi_li{
          display: flex;
          align-items: center;
          margin-bottom: 5px;
          img{
            width: 40px;
            height: 40px;
          }
          .daibi_p{
            p:first-child{
              font-size: 14px;
            }
            p:last-child{
              font-size: 12px;
              color:#999999;
            }
          }
        }
      }
    }
    .all_aside {
      margin-top: 50px;
      .all_header {
        display: flex;
        justify-content: space-between;
        font-size: 16px;
        padding: 0 15px;
        margin-bottom: 20px;
        div {
          &:first-of-type {
            span {
              display: inline-block;
              margin-right: 20px;
              color: #c6cad4;
              position: relative;
              cursor: pointer;
              &:hover {
                color: #332f2f;
                &::after {
                  display: block;
                  position: absolute;
                  content: "";
                  width: 22px;
                  height: 2px;
                  background: #332f2f;
                  left: 50%;
                  bottom: -5px;
                  z-index: 1;
                  transform: translateX(-50%);
                }
              }
            }
            .choice {
              color: #332f2f;
              &::after {
                display: block;
                position: absolute;
                content: "";
                width: 22px;
                height: 2px;
                background: #332f2f;
                left: 50%;
                bottom: -5px;
                z-index: 1;
                transform: translateX(-50%);
              }
            }
          }
          &:last-of-type {
            text-align: right;
          }
        }
      }
      .total {
        color: #333333;
      }
      .table-wrap {
        .el-icon-circle-check-outline {
          color: #eb6100;
          position: absolute;
          left: 10px;
          top: 50%;
          transform: translateY(-50%);
        }
      }
      .pagination {
        text-align: center;
        margin-top: 20px;
      }
    }
  }
}
</style>
