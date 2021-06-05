<template>
  <div class="wrap">
    <div class="tr_main">
      <SearchMobile class="search_con"/>
      <div class="con_top">
        <p>{{$t('blocks.title')}} <span>{{$t('blocks.total_span_before')}} {{total}} {{$t('blocks.total_span_after')}}</span></p>
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
            <li v-for="(item,index) of blocks" :key="index">
              <p><span>{{$t('blocks.height')}}</span><router-link :to="'/blockDetails/'+item.blockNum">{{item.blockNum}}</router-link></p>
              <p><span>{{$t('blocks.age')}}</span><timeago :since="item.blockTime" :locale="getBusLocal" :auto-update="0.5"></timeago></p>
              <p><span>{{$t('blocks.txn')}}</span>{{item.trxCount}}</p>
              <p><span>{{$t('blocks.miner')}}</span><router-link :to="'/address?minerName='+item.minerName">{{item.minerName}}</router-link></p>
              <p><span>{{$t('blocks.reward')}}</span>{{item.rewards}}</p>
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
  import SearchMobile from "../../components/search/SearchMobile";

  export default {
    name: "blocks",
    components:{SearchMobile},
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
            font-size: 24rem;
            font-weight: normal;
            margin-left: 15rem;
          }
        }
      }
      .con_all{
        background: #fff;
        box-shadow: 0rem 2rem 13rem 0rem rgba(0, 0, 0, 0.09);
        margin: 30rem 0;
        padding-bottom: 30rem;
        border-radius: 5rem;
        .all_title{
          display: flex;
          align-items: center;
          border-bottom:1rem solid #EEEEEE;
          line-height: 30rem;
          padding:10rem 0 0 30rem;
          margin-bottom: 30rem;
          color: #333333;
          li{
            list-style: none;
            padding: 15rem;
            border-bottom:2rem solid #fff;
          }
          li:hover{
            border-bottom:2rem solid #735DFF;
            cursor: pointer;
          }
          .active{
            border-bottom:2rem solid #735DFF;
            color: #333333;
            font-weight: bold;
          }
        }
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
                  width: 140rem;
                  color: #677897;   
                }
                a{
                  color: #0279FF;
                  text-align: left;
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
