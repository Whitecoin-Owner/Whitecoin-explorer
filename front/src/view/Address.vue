<template>
  <div class="wrap">
    <div class="background"></div>
    <div class="top-line"></div>
    <main>
      <h2>
        {{$t('address.overview.title')}}
      </h2>
      <section class="clear">
        <div class="left">
          <ul>
            <li>
                <span>
                  {{$t('address.overview.name')}}
                </span>
              <span>
                  {{minerInfo.name}}
                </span>
            </li>
            <li>
                <span>
                  {{$t('address.overview.address')}}
                </span>
              <span>
                  {{minerInfo.address}} 
                  <span style="color: red;" v-if="minerInfo.address==='XWCNWKLUcsybWt4bW5EXV1CfdaSNHiSKj4Hzw' || minerInfo.address==='XWCNi146ffqUffGJk3tTjnY1MdVGJn3m8jH29'">({{$t('address.overview.abnormal_address')}})</span>
              </span>
            </li>
            <li>
                <span>
                  {{$t('address.overview.contracts')}}
                </span>
              <span>
                  {{minerInfo.contracts}}
                </span>
            </li>
            <li>
              <span>
                  {{$t('address.overview.balances')}}
                </span>
              <span>
                <template v-for="(item, index) in minerInfo.balances">
                    {{item}}<template v-if="index < minerInfo.balances.length - 1">,</template>
                  </template>
                </span>
            </li>
          </ul>
        </div>
        <div class="right">
          <ul>
            <li>
                <span>
                  {{$t('miner.overview.transaction')}}
                </span>
              <span>
                  {{minerInfo.transaction}}
                </span>
            </li>
            <li>
                <span>
                  {{$t('miner.overview.rewards')}}
                </span>
              <span>
                  {{minerInfo.rewards}}
                </span>
            </li>
            <li>
                <span>
                  {{$t('address.overview.lockBalance')}}
                </span>
                <span>
                  <template v-for="(item, index) in minerInfo.lockBalances">
                    {{item}}<template v-if="index < minerInfo.lockBalances.length - 1">,</template>
                  </template>
                </span>
            </li>
            <li>
                <span>
                  {{$t('address.overview.paybackBalances')}}
                </span>
                <span>
                  <template v-for="(item, index) in minerInfo.paybackBalances">
                    {{item}}<template v-if="index < minerInfo.paybackBalances.length - 1">,</template>
                  </template>
                </span>
            </li>
          </ul>
        </div>
      </section>
      <aside>
        <header>
          <div>
            <span @click="choiceFlagChange(0)"
                  :class="{'choice':choiceFlag===0}">{{$t('address.myTransactions.title')}}</span>
            <span @click="choiceFlagChange(1)"
                  :class="{'choice':choiceFlag===1}">{{$t('address.myContracts.title')}}</span>
          </div>
          <div class="total">
            A Total Of {{total}} transactions found
          </div>
        </header>
        <div v-if="choiceFlag===0" class="table-wrap">
          <el-table
            :data="transaction"

            style="width: 100%"
          >
            <el-table-column
              align="center"
              :label="$t('address.myTransactions.txHash')"
              show-overflow-tooltip
            >
              <template slot-scope="scope">
                <router-link :to="'/transfer_details/'+scope.row.trxId+'/'+scope.row.opType">{{scope.row.trxId}}
                </router-link>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              :label="$t('address.myTransactions.block')"
              width="80">
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
            >
            </el-table-column>
            <el-table-column
              align="center"
              :label="$t('address.myTransactions.age')"
              width="120"
              show-overflow-tooltip>
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
                <span class="link" @click="_mixin_address_jump(scope.row.fromAccount)">{{scope.row.fromAccount}}</span>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              show-overflow-tooltip
              :label="$t('address.myTransactions.to')"
            >
              <template slot-scope="scope">
                <span class="link" @click="_mixin_address_jump(scope.row.toAccount)">{{scope.row.toAccount}}</span>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              prop="amountStr"
              :label="$t('miner.myTransactions.value')"
              width="100"
              show-overflow-tooltip>
            </el-table-column>
            <el-table-column
              align="center"
              :label="$t('address.myTransactions.fee')"
              width="130"
              show-overflow-tooltip>
              <template slot-scope="scope">
                {{scope.row.feeStr}}
                <img v-if="scope.row.guaranteeUse" class="feeShow" src="../assets/img/shouxufei.png"/>
              </template>
            </el-table-column>
          </el-table>
        </div>
        <div v-else class="table-wrap">
          <el-table
            :data="contaracts"
            style="width: 100%"
            key="contaracts"
          >
            <el-table-column
              align="center"
              :label="$t('address.myContracts.address')"
              show-overflow-tooltip
            >
              <template slot-scope="scope">
                <i class="el-icon-circle-check-outline" v-if="scope.row.status === 2"></i>
                <span class="link"
                      @click="_mixin_address_jump(scope.row.contractAddress)">{{scope.row.contractAddress}}</span>
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
            >
            </el-table-column>
            <el-table-column
              align="center"
              :formatter="dateFormate"
              :label="$t('address.myContracts.create')"
              width="220"
            >
            </el-table-column>
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
        <el-pagination
          class="pagination"
          layout="prev, pager, next, jumper"
          :current-page="page"
          :page-size="size"
          :total="total"
          @current-change="pageChange">
        </el-pagination>
      </aside>
    </main>
  </div>
</template>

<script>
  import bus from "../utils/bus";
  import common from "../utils/common";
  import typeObj from "../utils/type"
  import mixin from '../utils/mixin';

  export default {
    mixins: [mixin],
    name: "v-address",
    data() {
      return {
        page: 1,
        size: 10,
        total: 0,
        minerName: '',
        address: '',
        choiceFlag: 0,
        minerInfo: {
          name: '',//矿工名
          transaction: 0,//交易总量
          address: "",//矿工地址
          rewards: null,//奖励
          contracts: 0//拥有合约量
        },
        transaction: [],
        contaracts: []
      }
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
      if (this.$route.query.address) {
        this.address = this.$route.query.address;
        this.getDataByAddress();
      } else {
        this.minerName = this.$route.query.minerName;
        this.getDataByMinerName();
      }
    },
    methods: {
      pageChange(page) {
        this.page = page;
        if (this.choiceFlag === 0) {
          this.getTransactionData();
        } else {
          this.getContractData();
        }
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
        } else {
          this.getContractData();
        }
      },
      getDataByMinerName() {
        let that = this;
        this.$axios.post('/minerInfo', {name: this.minerName}).then(function (res) {
          let data = res.data;
          that.minerInfo = data.data;
          that.getTransactionData();
        });
      },
      getDataByAddress() {
        let that = this;
        this.$axios.post('/addrStatis', {address: this.address}).then(function (res) {
          let data = res.data;
          that.minerInfo = data.data;
          that.getTransactionData();
        });
      },
      getTransactionData() {
        let that = this;
        this.$axios.post('/queryTrxByAddr', {
          address: this.minerInfo.address,
          page: this.page,
          rows: this.size
        }).then(function (res) {
          let data = res.data;
          that.transaction = data.data.rows;
          that.total = data.data.total;
        });
      },
      getContractData() {
        let that = this;
        this.$axios.post('/addrContracts', {
          ownerAddress: this.minerInfo.address,
          page: this.page,
          rows: this.size
        }).then(function (res) {
          let data = res.data;
          that.contaracts = data.data.rows;
          that.total = data.data.total;
        });
      },
      dateFormate(row) {
        return common.format(new Date(row.createTime), 'yyyy-MM-dd hh:mm:ss');
      }
    },
    computed: {
      getBusLocal() {
        return bus.local;
      }
    }
  }
</script>

<style lang="less" scoped>
  .wrap {
    .top-line {
      height: 1px;
    }
    .background {
      width: 100%;
      height: 368px;
      position: absolute;
      top: 0;
      left: 0;
      background: white;
      color: black;
    }
    main {
      width: 77%;
      min-width: 1160px;
      margin: 120px auto;
      position: relative;
      color: black;;
      .search {
        position: absolute;
        right: 0;
        top: 0px;
      }
      h2 {
        font-size: 36px;
      }
      section {
        width: 75%;
        min-width: 1160px;
        margin: 20px auto 0;
        div ul li {
          width: 514px;
          box-sizing: border-box;
          bborder-bottom: 1px solid rgba(255, 255, 255, .5);
          padding: 12px;
          font-size: 14px;
          span {
            display: inline-block;
            &:first-of-type {
              width: 140px;
            }
          }
        }
        .left {
          float: left;
        }
        .right {
          float: right;
        }
      }
      aside {
        margin-top: 50px;
        header {
          display: flex;
          font-size: 16px;
          padding: 0 15px;
          margin-bottom: 20px;
          div {
            flex: 1;
            &:first-of-type {
              span {
                display: inline-block;
                margin-right: 20px;
                color: #C6CAD4;
                position: relative;
                cursor: pointer;
                &:hover {
                  color: #332f2f;
                  &::after {
                    display: block;
                    position: absolute;
                    content: '';
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
                  content: '';
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
            color: #EB6100;
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
