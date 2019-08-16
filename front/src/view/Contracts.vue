<template>
  <div class="wrap">
    <div class="background"></div>
    <div class="top-line"></div>
    <main>
      <h2>{{$t('contracts.title')}}</h2>
      <div class="total">
        {{$t('contracts.total_span_before')}} {{total}} {{$t('contracts.total_span_after')}}
      </div>
      <div class="table-wrap">
        <el-table
          :data="contracts"
          style="width: 100%"
        >
          <el-table-column
            align="center"
            :label="$t('contracts.contractAddress')"
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
            :label="$t('contracts.authorAddress')"
          >
            <template slot-scope="scope">
              <span class="link" @click="_mixin_address_jump(scope.row.onwerAddress)">{{scope.row.onwerAddress}}</span>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            prop="callTimes"
            :label="$t('contracts.callTimes')"
            width="120"
          >
          </el-table-column>
          <el-table-column
            align="center"
            prop="createTime"
            :label="$t('contracts.createTime')"
            :formatter="dataFormate"
            width="180"
          >
          </el-table-column>
          <el-table-column
            align="center"
            show-overflow-tooltip
            :label="$t('contracts.lastTime')"
            width="180"
          >
            <template slot-scope="scope">
              <timeago v-if="scope.row.lastTime !== null" :since="scope.row.lastTime" :locale="getBusLocal" :auto-update="0.5"></timeago>
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
    name: "contracts",
    data() {
      return {
        page: 1,
        size: 25,
        total: 0,
        contracts: []
      };
    },
    created() {
      this.getContractsData();
    },
    methods: {
      getContractsData() {
        let that = this;
        this.$axios.post('/queryContractList', {page: this.page, rows: this.size}).then(function (res) {
          let data = res.data.data;
          that.contracts = data.rows;
          that.total = data.total;
        })
      },
      pageChange(page) {
        this.page = page;
        this.getContractsData();
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
