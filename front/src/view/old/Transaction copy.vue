<template>
  <div class="wrap">
    <div class="top-line"></div>
    <div class="background"></div>
    <main>
      <h2>
        {{$t('transaction.title')}}
      </h2>
      <div class="total">
        {{$t('transaction.txs_found_before')}}{{allTransactionTotal}} {{$t('transaction.txs_found_after')}}
      </div>
      <nav>
        <div class="nav-item-wrap">
          <span v-for="item in tips" :key="item.name" class="normal" :class="{'choice':item.show}"
                @click="tipChange(item)">{{item.name}}</span>
        </div>
      </nav>
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
    </main>
  </div>
</template>

<script>
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
      All
    },
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
        allTransactionTotal: 0,
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
        txHash: null
      }
    },
    created() {
      if (this.$route.query.txHash) {
        this.txHash = this.$route.query.txHash;
      }
      this.getTableData();
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
      getTableData() {
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
          that.tableData = res.data.data.rows;
          that.total = res.data.data.total;
          if (!parentOpType) {
            that.allTransactionTotal = res.data.data.total;
          }
        });
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
      .total {
        margin: 10px 0 15px;
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
