<template>
  <div class="wrap">
    <div class="tr_main">
      <SearchMobile class="search_con"/>
      <div class="con_top">
        <p>{{$t('nav.assets')}}</p>
      </div>
      <div>
        <el-pagination
          class="pagination"
          layout="prev, total, next, jumper"
          :current-page="page"
          :page-size="size"
          :total="total"
        @current-change="pageChange">
        </el-pagination>
      </div>
      <div class="con_all">
        <div class="con_table">
          <div class="trans_ul">
            <li v-for="(item,index) of richlist" :key="index">
              <p><span>ID:</span>{{(index+1)+(page-1)*size}}</p>
              <p><span>{{$t('richlist.address')}}</span>
                <router-link :to="'/address?address='+item.addr">{{item.addr !==null ? item.addr : '--'}}</router-link>
                <span style="color: red;" v-if="item.addr==='XWCNWKLUcsybWt4bW5EXV1CfdaSNHiSKj4Hzw' || item.addr==='XWCNi146ffqUffGJk3tTjnY1MdVGJn3m8jH29'">({{$t('address.overview.abnormal_address')}})</span>
              </p>
              <p><span>{{$t('richlist.accountName')}}</span>{{item.name !==null ? item.name : '--'}}</p>
              <p><span>{{$t('richlist.amount')}}</span>{{item.amount !==null ? item.amount : '--'}} XWC</p>
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
      </div>
    </div>
  </div>
</template>

<script>
import bus from "../../utils/bus";
import common from "../../utils/common";
import mixin from "../../utils/mixin";
import SearchMobile from "../../components/search/SearchMobile";
export default {
  mixins: [mixin],
  name: "contracts",
  components:{SearchMobile},
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
    }
  },
  mounted(){
    bus.local = 4
  }
};
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
      border-radius: 5rem;
      .con_table{
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
          li:last-child{
            border-bottom:none;
          }
        }
        .trans_page{
          padding-left:30rem;
          padding-bottom: 30rem;
        }
      }
    }
    .search {
      position: absolute;
      right: 0;
      top: 0;
    }
    h2 {
      font-size: 36rem;
    }
    .total {
      margin: 10rem 0 65rem;
    }
    .pagination {
      margin-top: 20rem;
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
