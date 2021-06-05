<template>
  <div class="wrap">
    <div class="tr_main">
      <SearchMobile class="search_con"/>
      <div class="con_top">
        <p>{{$t('contractOverview.title')}}</p>
      </div>
      <div class="con_all">
        <div class="all_section">
          <div class="all_left">
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
          <div class="all_right">
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
            <div class="all_tab">
              <span @click="choiceFlagChange(0)" :class="{'choice':choiceFlag===0}">{{$t('contractOverview.tableTitle')}}</span>
              <span @click="choiceFlagChange(2)" :class="{'choice':choiceFlag===2}">Token Transfers</span>
              <span @click="choiceFlagChange(1)" :class="{'choice':choiceFlag===1}">{{$t('contractOverview.api')}}</span>
              <span @click="choiceFlagChange(3)" :class="{'choice':choiceFlag===3}">RICHLIST</span>
            </div>
          </div>
          <template v-if="choiceFlag===0">
            <div class="con_table">
              <div class="trans_ul">
                <li v-for="(item,index) of tableData" :key="index">
                  <p><span>{{$t('contractOverview.txHash')}}</span>
                    <router-link :to="'/transfer_details/'+item.trxId+'/'+item.opType">{{item.trxId}}
                    </router-link>
                  </p>
                  <p><span>{{$t('contractOverview.block')}}</span><router-link :to="'/blockDetails/'+item.blockNum">{{item.blockNum}}</router-link></p>
                  <p><span>{{$t('contractOverview.time')}}</span><timeago :since="item.trxTime" :locale="getBusLocal"></timeago></p>
                  <p><span>{{$t('contractOverview.callerAddress')}}</span><router-link :to="'/address?address='+item.fromAccount">{{item.fromAccount}}</router-link></p>
                  <p><span>{{$t('contractOverview.value')}}</span>{{item.amountStr}}</p>
                  <p><span>{{$t('contractOverview.fee')}}</span>{{item.feeStr}}</p>
                </li>
              </div>
              <div class="trans_page">
                <el-pagination
                  class="pagination"
                  layout="prev, total, next, jumper"
                  :current-page="page"
                  :page-size="size"
                  :total="total"
                  @current-change="pageChange">
                </el-pagination>
              </div>
            </div>
          </template>
          <template v-if="choiceFlag===2">
            <div class="con_table">
              <div class="trans_ul">
                <li v-for="(item,index) of tokenTransactions" :key="index">
                  <p><span>{{$t('contractOverview.txHash')}}</span>
                    <router-link :to="'/transfer_details/'+item.trxId+'/'+(item.opType||79)">{{item.trxId}}
                    </router-link>
                  </p>
                  <p><span>{{$t('contractOverview.block')}}</span><router-link :to="'/blockDetails/'+item.blockNum">{{item.blockNum}}</router-link></p>
                  <p><span>{{$t('contractOverview.time')}}</span><timeago :since="item.trxTime" :locale="getBusLocal"></timeago></p>
                  <p><span>From:</span><router-link :to="'/address?address='+item.fromAccount">{{item.fromAccount}}</router-link></p>
                  <p><span>To:</span><router-link :to="'/address?address='+item.toAccount">{{item.toAccount}}</router-link></p>
                  <p><span>Amount</span>{{item.amountStr}} &nbsp; {{item.symbol}}</p>
                  <p><span>Memo</span>{{item.memo}}</p>
                  <p><span>{{$t('contractOverview.fee')}}</span>{{item.feeStr}}</p>
                </li>
              </div>
              <div class="trans_page">
                <el-pagination
                  class="pagination"
                  layout="prev, total, next, jumper"
                  :current-page="page"
                  :page-size="size"
                  :total="total"
                  @current-change="pageChange">
                </el-pagination>
              </div>
            </div>
          </template>
          <template v-if="choiceFlag===1">
            <div class="api-wrap">
              <template v-for="(item,index) in abi" >
                <span class="abi" :key="index">{{item}}</span>
              </template>
            </div>
          </template>
          <template v-if="choiceFlag===3">
            <div class="con_table">
              <div class="trans_ul">
                <li v-for="(item,index) of balancesData" :key="index">
                  <p><span>ID</span>{{(page - 1) * size + index + 1}}
                  </p>
                  <p><span>{{$t('richlist.address')}}</span><router-link :to="'/address?address='+item.addr">{{item.addr}}</router-link></p>
                  <p><span>{{$t('richlist.accountName')}}</span>{{item.tokenSymbol}}</p>
                  <p><span>{{$t('richlist.amount')}}</span>{{item.tokenAmount}}</p>
                </li>
              </div>
              <div class="trans_page">
                <el-pagination
                  class="pagination"
                  layout="prev, total, next, jumper"
                  :current-page="page"
                  :page-size="size"
                  :total="total"
                  @current-change="pageChange">
                </el-pagination>
              </div>
            </div>
          </template>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import bus from "../../utils/bus";
  import mixin from '../../utils/mixin';
  import SearchMobile from "../../components/search/SearchMobile";

  export default {
    mixins: [mixin],
    name: "contract-overview",
    components:{SearchMobile},
    // beforeRouteUpdate(to, from, next) {
    //   this.contractAddress = to.params.contractAddress;
    //   this.getContractInfo();
    //   this.getTransactionData();
    //   this.getTokenTransactionData();
    //   next();
    // },
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
          this.getTransactionData()
        } else if(this.choiceFlag === 2) {
          this.getTokenTransactionData()
        } else if(this.choiceFlag === 3) {
          this.tokenContractbalances()
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
    height: 100%;
    padding-top: 153rem;
    .tr_main {
      position: relative;
      padding: 0 40rem; 
      .con_top{
        display: flex;
        align-items: center;
        justify-content: space-between;
        margin-top: 30rem;
        p{
          font-size: 32rem;
          color: #333333;
          font-weight: 600;
        }
      }
      .con_all{
        background: #fff;
        box-shadow: 0rem 2rem 13rem 0rem rgba(0, 0, 0, 0.09);
        margin: 30rem 0;
        padding-bottom: 30rem;
        border-radius: 5rem;
      }
      .search {
        position: absolute;
        right: 0;
        top: 0;
      }
      h2 {
        font-size: 36rem;
      }
      .all_section {
        overflow: hidden;
        padding: 30rem 30rem 0 30rem;
        div ul li {
          box-sizing: border-box;
          border-bottom: 1rem solid rgba(255, 255, 255, .5);
          font-size: 28rem;
          display: flex;
          padding: 5rem 0;
          span {
            word-break:break-all;
            &:first-of-type {
              width: 35%;
            color: #677897;
            }
            &:last-of-type {
              width: 65%;
              color: #333;
            }
          }
        }
      }
      .all_aside {
        margin-top: 30rem;
        .all_header {
          font-size: 28rem;
          margin-bottom: 20rem;
          div {
            border-bottom: 1px solid #dedede;
            padding-left: 30rem;
            width: 100%;
            box-sizing: border-box;
            &:first-of-type {
              span {
                display: inline-block;
                margin-right: 40rem;
                color: #C6CAD4;
                position: relative;
                padding-bottom: 10rem;
                cursor: pointer;
                margin-top: 10rem;
              }
              .choice {
                color: #333;
                &::after {
                  width: 100%;
                  display: block;
                  position: absolute;
                  content: '';
                  height: 2rem;
                  background: #0279FF;
                  left: 50%;
                  z-index: 1;
                  transform: translateX(-50%);
                  font-weight: bold;
                  margin-top: 10rem;
                }
              }
            }
          }
        }
        .con_table{
          .trans_ul{
            li{
              border-bottom: 1px solid #dedede;
              padding: 20rem 30rem;
              font-size: 28rem;
              color: #333;
              p{
                display: flex;
                align-items: center;
                margin: 20rem 0;
                span{
                  width: 32%;
                  color: #677897;
                }
                a{
                  color: #0279FF;
                  text-align: left;
                  word-break:break-all;
                  width: 68%;
                }
              }
            }
          }
          .trans_page{
            padding-left:30rem;
          }
        }
        .pagination {
          margin-top: 20rem;
        }
      }
      .total {
        color: #333333;
      }
      .api-wrap {
        border: 1px solid #dedede;
        margin: 0 30rem;
        padding: 30rem 0;
        .abi {
          font-size: 24rem;
          color: #666666;
          display: block;
          padding: 0 0 10rem 30rem;
        }
      }

    }
  }
/deep/ .el-pagination .btn-prev{
  font-size: 26rem;
  min-width: 80rem;
  height: 50rem; 
  color: #737D92;
  background: #E3DEFF;
  border: 0;
}

/deep/ .el-pagination .btn-next{
  font-size: 26rem;
  min-width: 80rem;
  height: 50rem; 
  color: #737D92;
  background: #E3DEFF;
  border: 0;
}
/deep/.el-pagination .btn-prev .el-icon, .el-pagination .btn-prev .el-icon{
  font-size: 26rem;
}
/deep/.el-pagination .btn-next .el-icon, .el-pagination .btn-next .el-icon{
  font-size: 26rem;
}
/deep/ .el-pagination span:not([class*=suffix]){
  font-size: 22rem;
}
/deep/ .el-pagination__jump{
  font-size: 26rem !important;
}
/deep/ .el-pagination__editor.el-input .el-input__inner {
  font-size: 26rem;
  min-width: 80rem;
  height: 50rem; 
  line-height: 50rem;
  color: #737D92;
  background: #E3DEFF;
  border: 0;
}
/deep/ .el-pagination span:not([class*=suffix]){
  height: 50rem; 
  line-height: 50rem;
}
</style>
