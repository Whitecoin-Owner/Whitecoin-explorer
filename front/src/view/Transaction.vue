<template>
  <div class="wrap">
    <div class="tr_main">
      <div class="con_top">
        <p>{{$t('transaction.title')}}</p>
        <Search class="search_con"/>
      </div>
      <div class="con_all">
        <div class="all_title">
          <li :class="{'active':traActive === 1}" @click="getTableData">{{$t('maxTitle.t1')}}</li>
          <li :class="{'active':traActive === 2}" @click="tokenTableData">{{$t('maxTitle.t2')}}</li>
        </div>
        <div class="con_table" v-show="traActive === 1">
          <all v-if="tips[0].show" :tableData="tableData"></all>
          <transfer v-else-if="tips[1].show" :tableData="tableData"></transfer>
          <withdraw v-else-if="tips[2].show" :tableData="tableData"></withdraw>
          <recharge v-else-if="tips[3].show" :tableData="tableData"></recharge>
          <contract v-else-if="tips[4].show" :tableData="tableData"></contract>
          <wage v-else-if="tips[5].show" :tableData="tableData"></wage>
          <acceptance v-else-if="tips[6].show" :tableData="tableData"></acceptance>
          <mortgage v-else-if="tips[7].show" :tableData="tableData"></mortgage>
          <foreclose v-else-if="tips[8].show" :tableData="tableData"></foreclose>
          <other v-else-if="tips[9].show" :tableData="tableData"></other>
          <el-pagination
            class="pagination"
            layout="prev, pager, next, jumper"
            :current-page="page"
            :page-size="size"
            :total="total"
            @current-change="pageChange">
          </el-pagination> 
        </div>
        <div class="con_table" v-show="traActive === 2">
          <el-table
            :data="TokenTrxData"
            width="100%"
          >
            <el-table-column
              align="center"
              :label="$t('home.transaction.txHash')"
              width="180"
            >
              <template slot-scope="scope">
                <router-link  class="yanse"  :to="'/transfer_details/'+scope.row.trxId+'/'+79">{{scope.row.trxId}}</router-link>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              :label="$t('transaction.block')"
              width="80"
              >
              <template slot-scope="scope">
                <router-link  class="yanse"  :to="'/blockDetails/'+scope.row.blockNum">{{scope.row.blockNum}}</router-link>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              :label="$t('transaction.age')"
              
            >
              <template slot-scope="scope">
                <div>
                  <span>{{scope.row.trxTime}}</span>  
                </div>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              :label="$t('transaction.from')"
              width="190"
            >
              <template slot-scope="scope">
                <router-link :to="'/address?address='+scope.row.fromAccount"><span class="yanse" >{{scope.row.fromAccount !==null ? scope.row.fromAccount :'--'}}</span></router-link>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              :label="$t('transaction.to')"
              width="180"
            >
              <template slot-scope="scope">
                <router-link :to="'/address?address='+scope.row.toAccount"><span class="yanse" >{{scope.row.toAccount !==null ? scope.row.toAccount :'--'}}</span></router-link>
              </template>
            </el-table-column>
              <el-table-column
                align="center"
                prop="amountStr"
                :label="$t('transaction.value')"
                width="150"
              >
              </el-table-column>
                <el-table-column
                  align="center"
                  :label="$t('transaction.fee')"
                  
                >
                  <template slot-scope="scope">
                    {{scope.row.feeStr}}
                    <img v-if="scope.row.guaranteeUse" class="feeShow" src="../assets/img/shouxufei.png"/>
                  </template>
                </el-table-column>
          </el-table>
            <el-pagination
            class="pagination"
            layout="prev, pager, next, jumper"
            :current-page="page1"
            :page-size="size"
            :total="total1"
            @current-change="pageChange1">
          </el-pagination> 
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
  import Search from "../components/search/Search";
  import All from "../components/transactionTables/All";
  import Contract from "../components/transactionTables/Contract";
  import Wage from "../components/transactionTables/Wage";
  import Withdraw from "../components/transactionTables/Withdraw";
  import Transfer from "../components/transactionTables/Transfer";
  import Recharge from "../components/transactionTables/Recharge";
  import Acceptance from "../components/transactionTables/Acceptance";
  import Other from "../components/transactionTables/Other";
  import Mortgage from "../components/transactionTables/Mortgage";
  import Foreclose from "../components/transactionTables/Foreclose";
  import dayjs from 'dayjs';
  import bus from "../utils/bus";
  import mixin from "../utils/mixin";

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
      Search
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
          if(res.data.retCode===200 && res.data.data !==null){
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
        padding-bottom: 30px;
        .all_title{
          display: flex;
          align-items: center;
          border-bottom:1px solid #EEEEEE;
          line-height: 30px;
          padding:10px 0 0 30px;
          margin-bottom: 30px;
          color: #333333;
          li{
            list-style: none;
            padding: 12px 0;
            border-bottom:2px solid #fff;
            margin-right: 60px;
          }
          li:hover{
            border-bottom:2px solid #735DFF;
            cursor: pointer;
          }
          .active{
            border-bottom:2px solid #735DFF;
            color: #333333;
            font-weight: bold;
          }
        }
        .con_table{
          margin: 0 30px;
          box-sizing: border-box;
          padding-bottom: 30px;
        }
      }
      .pagination {
        text-align: center;
        margin-top: 20px;
      }
      nav {
        margin: 0 auto 26px;
        text-align: center;
        .nav-item-wrap {
          display: inline-block;
          border-bottom: 1px solid rgba(255, 255, 255, .5);
          .normal {
            font-weight: bold;
            padding: 12px 0;
            margin-right: 35px;
            &:last-of-type {
              margin-right: 0;
            }
            display: inline-block;
            color: rgba(255, 255, 255, .5);
            font-size: 16px;
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
              height: 5px;
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

</style>
