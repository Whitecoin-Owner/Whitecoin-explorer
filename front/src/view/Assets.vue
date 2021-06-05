<template>
  <div class="wrap">
    <div class="tr_main">
      <div class="con_top">
        <p>{{$t('nav.assets')}}</p>
        <Search class="search_con"/>
      </div>
      <div class="con_all">
        <div class="table-wrap">
          <el-table :data="richlist" style="width: 100%">
            <el-table-column align="center" type="index" width="120">
              <template slot-scope="scope">
              <span>{{(page - 1) * size + scope.$index + 1}}</span>
            </template>
            </el-table-column>
            <el-table-column align="center" :label="$t('richlist.address')">
              <template slot-scope="scope">
                <router-link :to="'/address?address='+scope.row.addr">{{scope.row.addr !==null ? scope.row.addr : '--'}}</router-link>
                <span style="color: red;" v-if="scope.row.addr==='XWCNWKLUcsybWt4bW5EXV1CfdaSNHiSKj4Hzw' || scope.row.addr==='XWCNi146ffqUffGJk3tTjnY1MdVGJn3m8jH29'">({{$t('address.overview.abnormal_address')}})</span>
              </template>
            </el-table-column>
            <el-table-column align="center" :label="$t('richlist.accountName')">
              <template slot-scope="scope">
                <span>{{scope.row.name !==null ? scope.row.name : '--'}}</span>
              </template>
            </el-table-column>
            <el-table-column align="center" :label="$t('richlist.amount')">
              <template slot-scope="scope">
                <span>{{scope.row.amount !==null ? scope.row.amount : '--'}} XWC</span>
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
import mixin from "../utils/mixin";
import Search from "../components/search/Search";
export default {
  mixins: [mixin],
  name: "contracts",
  components:{Search},
  data() {
    return {
      page: 1,
      size: 100,
      total: 0,
      richlist: []
    };
  },
  created() {
    this.getRichlistData();

    bus.navChoice = 4
  },
  methods: {
    getRichlistData() {
      let that = this;
      this.$axios.post("https://third.whitecoin.info/whitecoin-explorer/address/listPage", {pageNum: that.page, pageSize: that.size}).then(function(res) {
        if(res.status===200 && res.data.records !==null){
          let data = res.data.records;
          that.richlist = data;
          that.total = res.data.total;
          if (window.localStorage) {
            localStorage.setItem("xwc.richlist", JSON.stringify(data));
          }
        }
      });
    },
    pageChange(page) {
      this.page = page;
      this.getRichlistData();
    },
    dataFormate(row) {
      let time = new Date(row.createTime);
      return common.format(time, "yyyy-MM-dd hh:mm:ss");
    }
  },
  computed: {
    getBusLocal() {
      return bus.local;
    },
    getIndex (index) {
      return index+this.page*this.size
    }
  },
  mounted(){
    bus.local = 4
  }
};
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
