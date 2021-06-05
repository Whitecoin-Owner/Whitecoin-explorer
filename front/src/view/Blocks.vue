<template>
  <div class="wrap">
    <div class="tr_main">
      <div class="con_top">
        <p>{{$t('blocks.title')}} <span>{{$t('blocks.total_span_before')}} {{total}} {{$t('blocks.total_span_after')}}</span></p>
        <Search class="search_con"/>
      </div>
      <div class="con_all">
        <div class="con_table">
          <el-table
          :data="blocks"
          style="width: 100%"
          >
            <el-table-column
              align="center"
              :label="$t('blocks.height')"
            >
              <template slot-scope="scope">
                <router-link :to="'/blockDetails/'+scope.row.blockNum">{{scope.row.blockNum}}</router-link>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              :label="$t('blocks.age')"
              width="180">
              <template slot-scope="scope">
                <timeago :since="scope.row.blockTime" :locale="getBusLocal" :auto-update="0.5"></timeago>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              prop="trxCount"
              :label="$t('blocks.txn')"
            >
            </el-table-column>
            <el-table-column
              align="center"
              :label="$t('blocks.miner')"
              show-overflow-tooltip
            >
              <template slot-scope="scope">
                <router-link :to="'/address?minerName='+scope.row.minerName">{{scope.row.minerName}}</router-link>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              prop="rewards"
              :label="$t('blocks.reward')"
              width="180"
            >
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
  import Search from "../components/search/Search";

  export default {
    name: "blocks",
    components:{Search},
    data() {
      return {
        blocks: [],
        page: 1,
        size: 25,
        total: 0
      };
    },
    created() {
      this.getBlocksList();
      bus.navChoice = 1;
    },
    methods: {
      getBlocksList() {
        let that = this;
        this.$axios.post('/queryBlockList',{page:this.page,rows:this.size}).then(function (res) {
          if(res.data.retCode===200 && res.data.data !==null){
            let data = res.data.data;
            that.blocks = data.rows;
            that.total = data.total;
          }
        })
      },
      pageChange(page) {
        this.page = page;
        this.getBlocksList();
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
        margin: 30px 0;
        padding: 30px 0;
        border-radius: 5px;
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
            padding: 15px;
            border-bottom:2px solid #fff;
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
          a{
            color: #0279FF;
            &:hover{
              color: #333;
            }
          }
        }
      }
      .pagination {
        margin-top: 20px;
        text-align: center;
      }
    }
  }

</style>
