<template>
  <div class="wrap">
    <div class="tr_main">
      <div class="con_top">
        <p>{{$t('transferDetails.tips.overview')}}</p>
        <Search class="search_con"/>
      </div>
      <div class="con_all">
        <div v-if="tips[0].show" class="overview-div">
          <h2>
            {{$t('blocks.overview.title')}}
          </h2>
          <div class="list">
            <ul>
              <li>
                <span class="name">{{$t('blocks.overview.hash')}}</span>
                <span>
                    {{blockData.blockId}}
                  </span>
              </li>
              <li>
                <span class="name">{{$t('blocks.overview.height')}}</span>
                <span>
                  <router-link :to="'/blockDetails/'+height">
                    {{height}}
                  </router-link>
                  </span>
              </li>
              <li>
                <span class="name">{{$t('blocks.overview.timeStamp')}}</span>
                <span>
                    <timeago :since="blockData.blockTime" :locale="getBusLocal" :auto-update="0.5"></timeago>
                  </span>
              </li>
              <li>
                <span class="name">{{$t('blocks.overview.transactions')}}</span>
                <span>
                  {{blockData.trxCount}}
                  </span>
              </li>
              <li>
                <span class="name">{{$t('blocks.overview.miner')}}</span>
                <span>
                  <router-link :to="'/address?minerName='+blockData.minerName">
                    {{blockData.minerName}}
                  </router-link>
                  </span>
              </li>
              <li>
                <span class="name">{{$t('blocks.overview.rewards')}}</span>
                <span>
                  {{blockData.rewards}}
                </span>
              </li>
              <!--<li>-->
                <!--<span class="name">{{$t('blocks.overview.version')}}</span>-->
                <!--<span>-->
                    <!--0-->
                  <!--</span>-->
              <!--</li>-->
              <li>
                <span class="name">{{$t('blocks.overview.merkleRoot')}}</span>
                <span>
                    {{blockData.merkleRoot}}
                  </span>
              </li>
            </ul>
          </div>
        </div>
        <div v-else class="transaction-div">
          <div class="total">
            A Total Of {{total}} transactions found
          </div>
          <div class="table-wrap">
            <el-table
              :data="transaction"

              style="width: 100%"
            >
              <el-table-column
                align="center"
                :label="$t('blocks.transaction.txHash')"
                show-overflow-tooltip
              >
                <template slot-scope="scope">
                  <router-link    :to="'/transfer_details/'+scope.row.trxId+'/'+scope.row.opType">{{scope.row.trxId}}</router-link>
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                :formatter="getTypeName"
                :label="$t('blocks.transaction.types')"
                width="220">
              </el-table-column>
              <el-table-column
                align="center"
                prop="amountStr"
                :label="$t('blocks.transaction.value')"
                width="220">
              </el-table-column>
              <el-table-column
                align="center"
                prop="feeStr"
                :label="$t('blocks.transaction.fee')"
                width="220"
              >
              </el-table-column>
            </el-table>
          </div>
          <el-pagination
            class="pagination"
            layout="prev, pager, next, jumper"
            :current-page="page"
            :page-size="size"
            :total="total">
          </el-pagination>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import Tips from "../components/tips/Tips";
  import bus from "../utils/bus";
  import typeObj from '../utils/type';
  import Search from "../components/search/Search";
  export default {
    components: {
      Tips,Search
    },
    name: "block-details",
    beforeRouteUpdate (to, from, next) {
      this.height = to.params.height;
      this.getBlockData();
      this.getTransactionData();
      next();
    },
    data() {
      return {
        height: 0,
        blockData:{
          blockId:null,
          blockNum:null,
          blockSize:null,
          minerName:null,
          minerAddress:null,
          blockTime:null,
          trxCount:null,
          amount:null,
          fee:null,
          reward:null,
          createdTime:null
        },
        tips: [
          {
            name: this.$t('blocks.overview.name'),
            show: true
          },
          {
            name: this.$t('blocks.transaction.name'),
            show: false
          }
        ],
        transaction: [],
        page:1,
        size:10,
        total:0
      };
    },
    created() {
      this.height = this.$route.params.height;
      this.getBlockData();
      this.getTransactionData();
      let that = this;
      bus.$on('tipChange', function (index) {
        that.tipChange(index);
      });
    },
    methods: {
      getTypeName(row) {
        return typeObj[row.opType];
      },
      tipChange(index) {
        for (let i = 0; i < this.tips.length; i++) {
          this.tips[i].show = false;
        }
        this.tips[index].show = true;
      },
      getBlockData() {
        let that = this;
        this.$axios.post('/queryBlockByNum',{blockNum:this.height}).then(function (res) {
          if(res.data.retCode===200 && res.data.data !==null){
            let data = res.data;
            that.blockData = data.data;
          }
        });
      },
      getTransactionData() {
        let that = this;
        this.$axios.post('/queryBlockTxNum',{blockNum:this.height,page:this.page,rows:this.size}).then(function (res) {
          if(res.data.retCode===200 && res.data.data !==null){
            let data = res.data.data;
            that.transaction = data.rows;
            that.total = data.total;
          }
        });
      }
    },
    computed: {
      getBusLocal() {
        return bus.local;
      }
    },
    mounted(){
      bus.navChoice = 1;
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
          .normal&.choice{
            font-size: 22px;
          }
        }
      }
      .con_all{
        background: #fff;
        box-shadow: 0px 2px 13px 0px rgba(0, 0, 0, 0.09);
        margin: 30px 0;
        padding-bottom: 30px;
      }
      .search {
        position: absolute;
        right: 0;
        top: -20px;
      }
      .overview-div {
        h2 {
          margin: 0 0 20px 0;
          font-size: 16px;
          color: #333;
          font-weight: 600;
          border-bottom: 1px #EEEEEE solid;
          padding: 20px 0 20px 30px;
        }
        .list {
          border-radius: 8px;
          border: 1px solid #B8C8DA;
          padding: 20px 10px;
          margin: 0 30px;
          ul li {
            font-size: 14px;
            border-bottom: 1px solid #FCFCFC;
            padding: 15px;
            span {
              display: inline-block;
            }
            .name {
              width: 160px;
            }
          }
          a{
            color: #0279FF;
            &:hover{
              color: #333;
            }
          }
        }
      }
      .transaction-div {
        .total {
          margin: 40px 0 25px;
          padding-left: 15px;
          font-size: 16px;
        }
      }
      .pagination {
        text-align: center;
        margin-top: 20px;
      }
    }
  }
</style>
