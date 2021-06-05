<template>
  <div class="wrap">
    <div class="tr_main">
      <div class="con_top">
        <p>{{$t('tokens.title')}} <span>{{$t('tokens.total_span_before')}} {{total}} {{$t('tokens.total_span_after')}}</span></p>
        <Search class="search_con"/>
      </div>
      <div class="con_all">
        <div class="con_table">
          <el-table
          :data="tokens"
          style="width: 100%"
          > 
          <el-table-column
              align="center"
              :label="$t('tokens.tokenSymbol')"
            >
              <template slot-scope="scope">
                <span>{{scope.row.tokenSymbol}}</span>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              :label="$t('tokens.contractAddress')"
            >
              <template slot-scope="scope">
                <router-link   :to="'/contractOverview/'+scope.row.contractAddress">
                  {{scope.row.contractAddress}}
                </router-link>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              :label="$t('tokens.precision')"
            >
              <template slot-scope="scope">
                <span>{{scope.row.precision}}</span>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              :label="$t('tokens.tokenSupply')"
            >
              <template slot-scope="scope">
                <span>{{scope.row.tokenSupply}}</span>
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

  import Search from "../components/search/Search";
  import bus from "../utils/bus";
  import common from "../utils/common";
  export default {
    name: "tokens",
    components:{Search},
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
          if(res.data.retCode===200 && res.data.data !==null){
            let data = res.data.data;
            // console.log(data,'ooo')
            that.tokens = data.rows;
            that.total = data.total;
          }
        })
      },
      pageChange(page) {
        this.page = page;
        this.getTokensData();
      },
      dataFormate(row) {
        let time = new Date(row.createTime);
        return common.format(time,'yyyy-MM-dd hh:mm:ss');
      },
      navChange(index) {
        bus.navChoice = index;
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
          display: flex;
          align-items: center;
          span{
            font-size: 16px;
            font-weight: normal;
            margin-left: 15px;
          }
        }
      }
      .con_all{
        background: #fff;
        box-shadow: 0px 2px 13px 0px rgba(0, 0, 0, 0.09);
        margin-top: 30px;
        padding: 30px 0;
        border-radius: 5px;
        margin-bottom: 30px;
        .con_table{
          margin: 0 30px;
          a{
            color: #0279FF;
            &:hover{
              color: #333;
            }
          }
        }
      }

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
