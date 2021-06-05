<template>
  <div class="wrap">
    <div class="tr_main">
      <SearchMobile class="search_con"/>
      <div class="con_top">
        <p>{{$t('tokens.title')}} <span>{{$t('tokens.total_span_before')}} {{total}} {{$t('tokens.total_span_after')}}</span></p>
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
            <li v-for="(item,index) of tokens" :key="index">
              <p><span>{{$t('tokens.tokenSymbol')}}</span>{{item.tokenSymbol}}</p>
              <p><span>{{$t('tokens.contractAddress')}}</span><router-link   :to="'/contractOverview/'+item.contractAddress">{{item.contractAddress}}</router-link></p>
              <p><span>{{$t('tokens.precision')}}</span>{{item.precision}}</p>
              <p><span>{{$t('tokens.tokenSupply')}}</span>{{item.tokenSupply}}</p>
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

  import SearchMobile from "../../components/search/SearchMobile";
  import bus from "../../utils/bus";
  import common from "../../utils/common";
  export default {
    name: "tokens",
    components:{SearchMobile},
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
            console.log(data,'ooo')
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
          display: flex;
          align-items: center;
          span{
            font-size: 26rem;
            font-weight: normal;
            margin-left: 15rem;
          }
        }
      }
      .con_all{
        background: #fff;
        box-shadow: 0rem 2rem 13rem 0rem rgba(0, 0, 0, 0.09);
        margin-top: 30rem;
        padding-bottom: 30rem;
        border-radius: 5rem;
        margin-bottom: 30rem;
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
          }
          .trans_page{
            padding-left:30rem;
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
