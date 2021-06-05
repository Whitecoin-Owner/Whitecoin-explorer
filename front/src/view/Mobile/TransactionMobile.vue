<template>
  <div class="wrap">
    <div class="tr_main">
      <SearchMobile class="search_con"/>
      <div class="con_top">
        <p>{{$t('transaction.title')}}</p>
      </div>
      <div v-show="traActive === 1">
        <el-pagination
          class="pagination"
          layout="prev,total,next,jumper"
          :current-page="page"
          :page-size="size"
          :total="total"
          @current-change="pageChange">
        </el-pagination> 
      </div>
      <div v-show="traActive === 2">
        <el-pagination
          class="pagination"
          layout="prev,total,next,jumper"
          :current-page="page1"
          :page-size="size"
          :total="total1"
          @current-change="pageChange1">
        </el-pagination>
      </div>
      <div class="con_all">
        <div class="all_title">
          <li :class="{'active':traActive === 1}" @click="getTableData">{{$t('maxTitle.t1')}}</li>
          <li :class="{'active':traActive === 2}" @click="tokenTableData">{{$t('maxTitle.t2')}}</li>
        </div>
        <div class="con_table" v-show="traActive === 1">
          <div class="trans_ul">
            <li v-for="(item,index) of tableData" :key="index">
              <p><span>{{$t('home.transaction.txHash')}}</span><router-link class="yanse" :to="'/transfer_details/'+item.trxId+'/'+item.opType">{{item.trxId}}</router-link></p>
              <p><span>{{$t('transaction.block')}}</span><router-link class="yanse" :to="'/blockDetails/'+item.blockNum">{{item.blockNum}}</router-link></p>
              <p><span>{{$t('transaction.age')}}</span>{{item.trxTime}}</p>
              <p><span>{{$t('transaction.from')}}</span><router-link class="yanse" :to="'/address?address='+item.fromAccount">{{item.fromAccount !== null ? item.fromAccount  : ($t('home.transaction.fromDeafult')) }}</router-link></p>
              <p><span>{{$t('transaction.to')}}</span><router-link class="yanse" :to="'/address?address='+item.toAccount">{{item.toAccount !==null ? item.toAccount :'--'}}</router-link></p>
              <p><span>{{$t('transaction.value')}}</span>{{item.amountStr}}</p>
              <p><span>{{$t('transaction.fee')}}</span>{{item.feeStr}}</p>
            </li>
          </div>
          <div class="trans_page">
            <el-pagination
              class="pagination"
              layout="prev,total,next,jumper"
              :current-page="page"
              :page-size="size"
              :total="total"
              @current-change="pageChange">
            </el-pagination> 
          </div>
        </div>
        <div class="con_table" v-show="traActive === 2">
          <div class="trans_ul">
            <li v-for="(item,index) of TokenTrxData" :key="index">
              <p><span>{{$t('home.transaction.txHash')}}</span><router-link class="yanse" :to="'/transfer_details/'+item.trxId+'/'+item.opType">{{item.trxId}}</router-link></p>
              <p><span>{{$t('transaction.block')}}</span><router-link class="yanse" :to="'/blockDetails/'+item.blockNum">{{item.blockNum}}</router-link></p>
              <p><span>{{$t('transaction.age')}}</span>{{item.trxTime}}</p>
              <p><span>{{$t('transaction.from')}}</span><router-link class="yanse" :to="'/address?address='+item.fromAccount">{{item.fromAccount !==null ? item.fromAccount :'--'}}</router-link></p>
              <p><span>{{$t('transaction.to')}}</span><router-link class="yanse" :to="'/address?address='+item.toAccount">{{item.toAccount !==null ? item.toAccount :'--'}}</router-link></p>
              <p><span>{{$t('transaction.value')}}</span>{{item.amountStr}}</p>
              <p><span>{{$t('transaction.fee')}}</span>{{item.feeStr}}</p>
            </li> 
          </div>
          <div class="trans_page">
            <el-pagination
              class="pagination"
              layout="prev,total,next,jumper"
              :current-page="page1"
              :page-size="size"
              :total="total1"
              @current-change="pageChange1">
            </el-pagination> 
          </div>
        </div>
      </div>
      
      <!-- <nav>
        <div class="nav-item-wrap">
          <span v-for="item in tips" :key="item.name" class="normal" :class="{'choice':item.show}"
                @click="tipChange(item)">{{item.name}}</span>
        </div>
      </nav> -->
      
    </div>
  </div>
</template>

<script>
  import SearchMobile from "../../components/search/SearchMobile";
  import All from "../../components/transactionTables/All";
  import Contract from "../../components/transactionTables/Contract";
  import Wage from "../../components/transactionTables/Wage";
  import Withdraw from "../../components/transactionTables/Withdraw";
  import Transfer from "../../components/transactionTables/Transfer";
  import Recharge from "../../components/transactionTables/Recharge";
  import Acceptance from "../../components/transactionTables/Acceptance";
  import Other from "../../components/transactionTables/Other";
  import Mortgage from "../../components/transactionTables/Mortgage";
  import Foreclose from "../../components/transactionTables/Foreclose";
  import dayjs from 'dayjs';
  import bus from "../../utils/bus";
  import mixin from "../../utils/mixin";

  export default {
    components: {
      Foreclose,
      Mortgage,
      Other,
      Acceptance,
      Recharge,
      Transfer,
      Withdraw,
      Wage,
      Contract,
      All,
      SearchMobile
    },
    mixins: [mixin],
    name: "transaction",
    beforeRouteUpdate(to, from, next) {
      if (to.query) {
        this.txHash = to.query.txHash;
      }
      this.getTableData();
      next();
    },
    data() {
      return {
        page: 1,
        size: 10,
        total: 0,
        page1: 1,
        total1: 0,
        allTransactionTotal: 0,
        traActive:1,
        tips: [
          {
            name: this.$t('transaction.all'),
            show: true,
            parentOpType: null
          },
          {
            name: this.$t('transaction.transfer'),
            show: false,
            parentOpType: 1
          },
          {
            name: this.$t('transaction.withdraw'),
            show: false,
            parentOpType: 4
          },
          {
            name: this.$t('transaction.recharge'),
            show: false,
            parentOpType: 3
          },
          {
            name: this.$t('transaction.contract'),
            show: false,
            parentOpType: 2
          },
          {
            name: this.$t('transaction.wage'),
            show: false,
            parentOpType: 5
          },
          {
            name: this.$t('transaction.acceptance'),
            show: false,
            parentOpType: 6
          },
          {
            name: this.$t('transaction.mortgage'),
            show: false,
            parentOpType: 8
          },
          {
            name: this.$t('transaction.foreclose'),
            show: false,
            parentOpType: 9
          },
          {
            name: this.$t('transaction.other'),
            show: false,
            parentOpType: 7
          }
        ],
        tableData: [],
        TokenTrxData:[],
        txHash: null,
        isShow:false
      }
    },
    
    methods: {
      tipChange(item) {
        if (item.show) {
          return;
        } else {
          for (let i = 0; i < this.tips.length; i++) {
            this.tips[i].show = false;
          }
          item.show = true;
        }
        this.page = 1;
        this.getTableData();
      },
      pageChange(page) {
        this.page = page;
        this.getTableData();
      },
      pageChange1(page) {
        this.page1 = page;
        this.tokenTableData();
      },
      getTableData() {
        this.traActive = 1;

        let that = this;
        let parentOpType = null;
        for (let i = 0; i < this.tips.length; i++) {
          if (this.tips[i].show) {
            parentOpType = this.tips[i].parentOpType;
            break;
          }
        }
        let paramData = {};
        if (parentOpType !== null) {
          if (this.txHash) {
            paramData = {trxId: this.txHash, parentOpType: parentOpType, page: this.page, rows: this.size}
          } else {
            paramData = {parentOpType: parentOpType, page: this.page, rows: this.size}
          }
        } else {
          if (this.txHash) {
            paramData = {trxId: this.txHash, page: this.page, rows: this.size}
          } else {
            paramData = {page: this.page, rows: this.size}
          }
        }
        this.$axios.post('queryTransactionList', {
          trxId: this.txHash,
          parentOpType: parentOpType,
          page: this.page,
          rows: this.size
        }).then(function (res) {
          res.data.data.rows.forEach(item=>{
            item.trxTime = dayjs(item.trxTime).format('YYYY-MM-DD HH:mm:ss');
            item.amountStr = item.amountStr !==null ? item.amountStr : 0
          })
          that.tableData = res.data.data.rows;
          console.log(res.data.data.rows,'0000')
          that.total = res.data.data.total;
          if (!parentOpType) {
            that.allTransactionTotal = res.data.data.total;
          }
        });
      },
      tokenTableData(){
        this.traActive = 2;
        // 我的代币交易
        let that = this;
        this.$axios.post('queryTokenTrx', {
          page: this.page1,
          rows: this.size
        }).then(function (res) {
          if(res.data.retCode===200 && res.data.data !==null){
            res.data.data.rows.forEach(item=>{
              item.trxTime = dayjs(item.trxTime).format('YYYY-MM-DD HH:mm:ss');
              item.amountStr = item.amountStr !==null ? item.amountStr : 0
            })
            console.log(res.data.data.rows,'9999')
            that.TokenTrxData = res.data.data.rows;
            that.total1 = res.data.data.total;
          }
        });
      },
      traChange(index) {
        this.traActive = index;
      },
    },
    created() {
      bus.navChoice = 2;
      this.getTableData();
    },
  }
</script>

<style lang="less" scoped>
.wrap {
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
        .all_title{
          display: flex;
          align-items: center;
          border-bottom:1px solid #dedede;
          padding:10rem 0 0 30rem;
          color: #333333;
          font-size: 32rem;
          li{
            list-style: none;
            padding: 15rem;
            border-bottom:4rem solid #fff;
          }
          li:hover{
            border-bottom:4rem solid #735DFF;
            cursor: pointer;
          }
          .active{
            border-bottom:2rem solid #735DFF;
            color: #333333;
            font-weight: bold;
          }
        }
        .con_table{
          box-sizing: border-box;
          .trans_ul{
            li{
              border-bottom: 1px solid #E2E2E2;
              padding: 20rem 30rem;
              font-size: 28rem;
              color: #333;
              p{
                display: flex;
                align-items: center;
                margin: 20rem 0;
                span{
                  width: 25%;
                  color: #677897;
                }
                a{
                  color: #0279FF;
                  text-align: left;
                  word-break:break-all;
                  width: 75%;
                }
              }
            }
          }
          .trans_page{
            padding-left: 30rem;
          }
        }
      }
      .pagination {
        margin-top: 20rem;
      }
      nav {
        margin: 0 auto 26rem;
        text-align: center;
        .nav-item-wrap {
          display: inline-block;
          border-bottom: 1px solid rgba(255, 255, 255, .5);
          .normal {
            font-weight: bold;
            padding: 12rem 0;
            margin-right: 35rem;
            &:last-of-type {
              margin-right: 0;
            }
            display: inline-block;
            color: rgba(255, 255, 255, .5);
            font-size: 16rem;
            cursor: pointer;
            &:hover {
              color: rgba(255, 255, 255, 1);
              /*font-weight: bold;*/
              position: relative;
            }
          }
          .choice {
            color: rgba(255, 255, 255, 1);
            position: relative;
            &::after {
              content: '';
              position: absolute;
              display: block;
              width: 100%;
              height: 5rem;
              border-radius: 1px;
              background: white;
              left: 0;
              bottom: 0;
              transform: translateY(50%);
              z-index: 1;
            }
          }
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
