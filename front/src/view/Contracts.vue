<template>
  <div class="wrap">
    <div class="tr_main">
      <div class="con_top">
        <p>{{$t('contracts.title')}}</p>
        <Search class="search_con"/>
      </div>
      <div class="con_all">
        <div class="table-wrap">
          <el-table
            :data="contracts"
            style="width: 100%"
          >
            <el-table-column
              align="center"
              :label="$t('contracts.contractAddress')"
            >
              <template slot-scope="scope">
                <router-link  :to="'/contractOverview/'+scope.row.contractAddress">
                  {{scope.row.contractAddress}}
                </router-link>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              :label="$t('contracts.authorAddress')"
            >
              <template slot-scope="scope">
                 <router-link  :to="'/address?address='+scope.row.onwerAddress">
                  {{scope.row.onwerAddress}}
                </router-link>
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
              :label="$t('contracts.lastTime')"
              width="180"
            >
              <template slot-scope="scope">
                <timeago v-if="scope.row.lastTime !== null" :since="scope.row.lastTime" :locale="getBusLocal" :auto-update="0.5"></timeago>
                <span v-else> -- </span>
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
      </div>
    </div>
  </div>
</template>

<script>
  import bus from "../utils/bus";
  import common from "../utils/common";
  import mixin from '../utils/mixin';
  import Search from "../components/search/Search";
  export default {
    mixins: [mixin],
    name: "contracts",
    components:{Search},
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

      bus.navChoice = 4
    },
    methods: {
      getContractsData() {
        let that = this;
        this.$axios.post('/queryContractList', {page: this.page, rows: this.size}).then(function (res) {
          if(res.data.retCode===200 && res.data.data !==null){
            let data = res.data.data;
            that.contracts = data.rows;
            that.total = data.total;
          }
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
        padding: 30px;
        border-radius: 5px;
        a{
          color: #0279FF;
          &:hover{
            color: #333;
          }
        }
      }
      .pagination {
        text-align: center;
        margin-top: 20px;
      }
    }
  }
</style>
