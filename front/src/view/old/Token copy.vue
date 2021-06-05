<template>
  <div class="wrap">
    <div class="background"></div>
    <div class="top-line"></div>
    <main>
      <h2>{{$t('tokens.title')}}</h2>
      <div class="total">
        {{$t('tokens.total_span_before')}} {{total}} {{$t('tokens.total_span_after')}}
      </div>
      <div class="table-wrap">
        <el-table
          :data="tokens"
          style="width: 100%"
        >
        <el-table-column
            align="center"
            show-overflow-tooltip
            :label="$t('tokens.tokenSymbol')"
          >
            <template slot-scope="scope">
              <span class="link">{{scope.row.tokenSymbol}}</span>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            :label="$t('tokens.contractAddress')"
            show-overflow-tooltip
          >
            <template slot-scope="scope">
              <router-link   :to="'/contractOverview/'+scope.row.contractAddress">
                {{scope.row.contractAddress}}
              </router-link>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            show-overflow-tooltip
            :label="$t('tokens.precision')"
          >
            <template slot-scope="scope">
              <span class="link">{{scope.row.precision}}</span>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            show-overflow-tooltip
            :label="$t('tokens.tokenSupply')"
          >
            <template slot-scope="scope">
              <span class="link">{{scope.row.tokenSupply}}</span>
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
    </main>
  </div>
</template>

<script>
  import bus from "../utils/bus";
  import common from "../utils/common";
  import mixin from '../utils/mixin';
  export default {
    mixins: [mixin],
    name: "tokens",
    data() {
      return {
        page: 1,
        size: 25,
        total: 0,
        tokens: []
      };
    },
    created() {
      this.getTokensData();
    },
    methods: {
      getTokensData() {
        let that = this;
        this.$axios.post('/listTokens', {page: this.page, rows: this.size}).then(function (res) {
          let data = res.data.data;
          that.tokens = data.rows;
          that.total = data.total;
        })
      },
      pageChange(page) {
        this.page = page;
        this.getTokensData();
      },
      dataFormate(row) {
        let time = new Date(row.createTime);
        return common.format(time,'yyyy-MM-dd hh:mm:ss');
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
        margin: 10px 0 65px;
      }
      .pagination {
        text-align: center;
        margin-top: 20px;
      }
    }
  }
</style>
