<template>
  <div class="wrap">
    <div class="background"></div>
    <div class="top-line"></div>
    <main>
      <h2>
        {{$t('contractOverview.title')}}
      </h2>
      <section class="clear">
        <div class="left">
          <ul>
            <li>
                <span>
                  {{$t('contractOverview.contractAddress')}}
                </span>
              <span>
                  {{contractInfo.contractAddress}}
                </span>
            </li>
            <li>
                <span>
                  {{$t('contractOverview.authorAddress')}}
                </span>
              <span>
                  {{contractInfo.onwerAddress}}
                </span>
            </li>
            <li>
                <span>
                  {{$t('contractOverview.balance')}}
                </span>
              <span>
                  <template v-if="contractInfo.balance === null">- -</template>
                  <template v-for="item in contractInfo.balance">
                    {{item}}&nbsp;
                  </template>
                </span>
            </li>
          </ul>
        </div>
        <div class="right">
          <ul>
            <li>
                <span>
                  {{$t('contractOverview.transaction')}}
                </span>
              <span>
                  {{contractInfo.transactions}}
                </span>
            </li>
            <li>
                <span>
                  {{$t('contractOverview.createTxn')}}
                </span>
              <span>
                  {{contractInfo.createTxId}}
                </span>
            </li>
          </ul>
        </div>
      </section>
      <aside>
        <header>
          <div>
            <span @click="choiceFlagChange(0)"
                  :class="{'choice':choiceFlag===0}">{{$t('contractOverview.tableTitle')}}</span>
            <span @click="choiceFlagChange(1)" :class="{'choice':choiceFlag===1}">{{$t('contractOverview.api')}}</span>
          </div>
          <div class="total">
            A Total Of {{total}} transactions found
          </div>
        </header>
        <template v-if="choiceFlag===0">
          <div class="table-wrap">
            <el-table
              :data="tableData"
              style="width: 100%"
            >
              <el-table-column
                align="center"
                :label="$t('contractOverview.txHash')"
                show-overflow-tooltip
              >
                <template slot-scope="scope">
                  <router-link :to="'/transfer_details/'+scope.row.trxId+'/'+scope.row.opType">{{scope.row.trxId}}
                  </router-link>
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                :label="$t('contractOverview.block')"
                width="150">
                <template slot-scope="scope">
                  <router-link :to="'/blockDetails/'+scope.row.blockNum">{{scope.row.blockNum}}</router-link>
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                :label="$t('contractOverview.time')"
                width="150">
                <template slot-scope="scope">
                  <timeago :since="scope.row.trxTime" :locale="getBusLocal"></timeago>
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                :label="$t('contractOverview.callerAddress')"
                show-overflow-tooltip
              >
                <template slot-scope="scope">
                  <span class="link"
                        @click="_mixin_address_jump(scope.row.fromAccount)">{{scope.row.fromAccount}}</span>
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                prop="amountStr"
                :label="$t('contractOverview.value')"
                width="150">
              </el-table-column>
              <el-table-column
                align="center"
                prop="feeStr"
                :label="$t('contractOverview.fee')"
                width="150">
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
        </template>
        <template v-else>
          <div class="api-wrap">
            <template v-for="item in abi">
              <span class="abi">{{item}}</span>
            </template>
          </div>
        </template>
      </aside>
    </main>
  </div>
</template>

<script>
  import bus from "../utils/bus";
  import mixin from '../utils/mixin';

  export default {
    mixins: [mixin],
    name: "contract-overview",
    beforeRouteUpdate(to, from, next) {
      this.contractAddress = to.params.contractAddress;
      this.getContractInfo();
      this.getTransactionData();
      next();
    },
    data() {
      return {
        page: 1,
        size: 25,
        total: 0,
        contractAddress: '',
        contractInfo: {
          contractAddress: '',
          onwerAddress: '',
          transactions: 0,
          createTxId: '',
          balance: []
        },
        choiceFlag: 0,
        tableData: [],
        abi: []
      }
    },
    created() {
      this.contractAddress = this.$route.params.contractAddress;
      this.getContractInfo();
      this.getTransactionData();
      this.getAbiData();
    },
    methods: {
      choiceFlagChange(flag) {
        this.choiceFlag = flag;
      },
      getContractInfo() {
        let that = this;
        this.$axios.post('/getContractStatis', {contractId: this.contractAddress}).then(function (res) {
          that.contractInfo = res.data.data;
        })
      },
      getTransactionData() {
        let that = this;
        this.$axios.post('/queryTrxByAddr', {
          address: this.contractAddress,
          page: this.page,
          rows: this.size
        }).then(function (res) {
          let data = res.data.data;
          that.tableData = data.rows;
          that.total = data.total;
        })
      },
      pageChange(page) {
        this.page = page;
        this.getTransactionData();
      },
      getAbiData() {
        let that = this;
        this.$axios.post('/getAbi', {contractId: this.contractAddress}).then(function (res) {
          that.abi = res.data.data.abi;
        });
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
      height: 338px;
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
        top: 0;
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
          border-bottom: 1px solid rgba(255, 255, 255, .5);
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
        margin-top: 60px;
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
        .pagination {
          margin-top: 16px;
          text-align: center;
        }
      }
      .total {
        color: #333333;
      }
      .api-wrap {
        border: 1px solid #C6CAD4;
        .abi {
          color: #666666;
          display: block;
          margin: 12px;
        }
      }

    }
  }
</style>
