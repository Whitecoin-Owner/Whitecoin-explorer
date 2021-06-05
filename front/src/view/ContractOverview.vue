<template>
  <div class="wrap">
    <div class="tr_main">
      <div class="con_top">
        <p>{{$t('contractOverview.title')}}</p>
        <Search class="search_con"/>
      </div>
      <div class="con_all">
        <div class="all_section">
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
              <li v-if="contractInfo && contractInfo.tokenContract">
                  <span>
                    Token Symbol
                  </span>
                <span>
                    {{contractInfo.tokenContract.tokenSymbol}}
                  </span>
              </li>
              <li v-if="contractInfo && contractInfo.tokenContract">
                  <span>
                    Token Precision
                  </span>
                <span>
                    {{contractInfo.tokenContract.precision}}
                  </span>
              </li>
              <li v-if="contractInfo && contractInfo.tokenContract">
                  <span>
                    Token Supply
                  </span>
                <span>
                    {{contractInfo.tokenContract.tokenSupply}}
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
        </div>
        <div class="all_aside">
          <div class="all_header">
            <div>
              <span @click="choiceFlagChange(0)" :class="{'choice':choiceFlag===0}">{{$t('contractOverview.tableTitle')}}</span>
              <span @click="choiceFlagChange(2)" :class="{'choice':choiceFlag===2}">Token Transfers</span>
              <span @click="choiceFlagChange(1)" :class="{'choice':choiceFlag===1}">{{$t('contractOverview.api')}}</span>
              <span @click="choiceFlagChange(3)" :class="{'choice':choiceFlag===3}">RICHLIST</span>
            </div>
            <div class="total" v-if="choiceFlag!==1">
              A Total Of {{total}} transactions found
            </div>
          </div>
          <template v-if="choiceFlag===0">
            <div class="table-wrap">
              <el-table
                :data="tableData"
                style="width: 100%"
              >
                <el-table-column
                  align="center"
                  :label="$t('contractOverview.txHash')"
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
          <template v-if="choiceFlag===2">
            <div class="table-wrap">
              <el-table
                :data="tokenTransactions"
                style="width: 100%"
              >
                <el-table-column
                  align="center"
                  :label="$t('contractOverview.txHash')"
                >
                  <template slot-scope="scope">
                    <router-link :to="'/transfer_details/'+scope.row.trxId+'/'+(scope.row.opType||79)">{{scope.row.trxId}}
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
                  label="From"
                >
                  <template slot-scope="scope">
                    <span class="link"
                          @click="_mixin_address_jump(scope.row.fromAccount)">{{scope.row.fromAccount}}</span>
                  </template>
                </el-table-column>
                <el-table-column
                  align="center"
                  label="To"
                >
                  <template slot-scope="scope">
                    <span class="link"
                          @click="_mixin_address_jump(scope.row.toAccount)">{{scope.row.toAccount}}</span>
                  </template>
                </el-table-column>
                <el-table-column
                  align="center"
                  label="Amount"
                >
                  <template slot-scope="scope">
                    <span>{{scope.row.amountStr}} &nbsp; {{scope.row.symbol}}</span>
                  </template>
                </el-table-column>
                <el-table-column
                  align="center"
                  label="Memo"
                >
                  <template slot-scope="scope">
                    <span>{{scope.row.memo}}</span>
                  </template>
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
          <template v-if="choiceFlag===1">
            <div class="api-wrap">
              <template v-for="(item,index) in abi" >
                <span class="abi" :key="index">{{item}}</span>
              </template>
            </div>
          </template>
           <template v-if="choiceFlag===3">
            <div class="table-wrap">
              <el-table
                :data="balancesData"
                style="width: 100%"
              >
                <el-table-column align="center" type="index" width="120" label="#">
                  <template slot-scope="scope">
                    <span>{{(page - 1) * size + scope.$index + 1}}</span>
                  </template>
                </el-table-column>
                <el-table-column
                  align="center"
                  prop="addr"
                  :label="$t('richlist.address')">
                  <template slot-scope="scope">
                    <span class="link" @click="_mixin_address_jump(scope.row.addr)">{{scope.row.addr}}</span>
                  </template>
                </el-table-column>
                <el-table-column
                  align="center"
                  prop="tokenSymbol"
                  :label="$t('richlist.accountName')">
                    <template slot-scope="scope">{{scope.row.tokenSymbol}}</template>
                </el-table-column>
                <el-table-column
                  align="center"
                  prop="tokenAmount"
                  :label="$t('richlist.amount')">
                    <template slot-scope="scope">{{scope.row.tokenAmount}}</template>
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
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import bus from "../utils/bus";
  import mixin from '../utils/mixin';
  import Search from "../components/search/Search";

  export default {
    mixins: [mixin],
    name: "contract-overview",
    components:{Search},
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
        abi: [],
        tokenTransactions: [],
        balancesData: []
      }
    },
    created() {
      this.contractAddress = this.$route.params.contractAddress;
      this.getContractInfo();
      this.getTransactionData();
    },
    methods: {
      choiceFlagChange(flag) {
        this.choiceFlag = flag;
        if (this.choiceFlag === 0) {
          this.page = 1
          this.getTransactionData();
        } else if (this.choiceFlag === 1) {
          this.getAbiData();
        }else if (this.choiceFlag === 2) {
          this.page = 1
          this.getTokenTransactionData();
        } else if (this.choiceFlag === 3) {
          this.page = 1
          this.tokenContractbalances();
        }
      },
      getContractInfo() {
        let that = this;
        this.$axios.post('/getContractStatis', {contractId: this.contractAddress}).then(function (res) {
          if(res.data.retCode===200 && res.data.data !==null){
            that.contractInfo = res.data.data;
          }
        })
      },
      getTransactionData() {
        let that = this;
        this.$axios.post('/queryTrxByAddr', {
          address: this.contractAddress,
          page: this.page,
          rows: this.size
        }).then(function (res) {
          if(res.data.retCode===200 && res.data.data !==null){
            let data = res.data.data;
            that.tableData = data.rows;
            that.total = data.total;
          }
        })
      },
      getTokenTransactionData() {
        let that = this;
        this.$axios.post('/getTokenTransactionList', {
          contractId: this.contractAddress,
          page: this.page,
          rows: this.size
        }).then(function (res) {
          if(res.data.retCode===200 && res.data.data !==null){
            let data = res.data.data;
            that.tokenTransactions = data.rows;
            that.total = data.total;
          }
        })
      },
      pageChange(page) {
        this.page = page;
        if (this.choiceFlag === 0) {
          this.getTransactionData();
        } else if(this.choiceFlag === 2) {
          this.getTokenTransactionData();
        } else if(this.choiceFlag === 3) {
          this.tokenContractbalances();
        }
        if(document.body.scrollTop){
          document.body.scrollTop = 0
        }else {
          document.documentElement.scrollTop = 0
        }
      },
      getAbiData() {
        let that = this;
        this.$axios.post('/getAbi', {contractId: this.contractAddress}).then(function (res) {
          if(res.data.retCode===200 && res.data.data !==null){
            that.abi = res.data.data.abi;
          }
        });
      },
      tokenContractbalances() {
        let that = this;
        this.$axios.get('/token_contract_balances', {
          params: {
            contractAddress: this.contractAddress,
            pageNum: this.page,
            pageSize: this.size
          }
        }).then(function (res) {
          if(res.data.retCode===200 && res.data.data.rows !== null){
            that.balancesData = res.data.data.rows
            that.total = res.data.data.total || 0;
          }
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
        }
      }
      .con_all{
        background: #fff;
        box-shadow: 0px 2px 13px 0px rgba(0, 0, 0, 0.09);
        margin: 30px 0;
        padding: 30px 30px;
        border-radius: 5px;
      }
      .search {
        position: absolute;
        right: 0;
        top: 0;
      }
      h2 {
        font-size: 36px;
      }
      .all_section {
        overflow: hidden;
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
      .all_aside {
        margin-top: 30px;
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
