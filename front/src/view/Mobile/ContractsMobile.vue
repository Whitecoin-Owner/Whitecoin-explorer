<template>
  <div class="wrap">
    <div class="tr_main">
      <SearchMobile class="search_con"/>
      <div class="con_top">
        <p>{{$t('contracts.title')}}</p>
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
            <li v-for="(item,index) of contracts" :key="index">
              <p><span>{{$t('contracts.contractAddress')}}</span><router-link  :to="'/contractOverview/'+item.contractAddress">{{item.contractAddress}}</router-link></p>
              <p><span>{{$t('contracts.authorAddress')}}</span><router-link  :to="'/address?address='+item.onwerAddress">{{item.onwerAddress}}</router-link></p>
              <p><span>{{$t('contracts.callTimes')}}</span>{{item.callTimes}}</p>
              <p><span>{{$t('contracts.createTime')}}</span>{{item.createTime}}</p>
              <p><span>{{$t('contracts.lastTime')}}</span><timeago v-if="item.lastTime !== null ? item.lastTime : '--'" :since="item.lastTime" :locale="getBusLocal" :auto-update="0.5"></timeago></p>
            </li>
          </div>
          <div class="trans_page">
            <el-pagination
              class="pagination"
              layout="prev, total, next, jumper"
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
  import mixin from '../../utils/mixin';
  import SearchMobile from "../../components/search/SearchMobile";
  export default {
    mixins: [mixin],
    name: "contracts",
    components:{SearchMobile},
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
        padding-bottom: 30rem;
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
                  width: 32%;
                  color: #677897;
                }
                a{
                  color: #0279FF;
                  text-align: left;
                  word-break:break-all;
                  width: 68%;
                }
              }
            }
          }
          .trans_page{
            padding-left:30rem;
          }
        }
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
