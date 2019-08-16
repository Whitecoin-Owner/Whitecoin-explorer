<template>
  <div class="wrap">
    <div class="background"></div>
    <div class="top-line"></div>
    <main>
      <tips :tips="tips"></tips>
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
    </main>
  </div>
</template>

<script>
  import Tips from "../components/tips/Tips";
  import bus from "../utils/bus";
  import typeObj from '../utils/type';
  export default {
    components: {
      Tips
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
          let data = res.data;
          that.blockData = data.data;
        });
      },
      getTransactionData() {
        let that = this;
        this.$axios.post('/queryBlockTxNum',{blockNum:this.height,page:this.page,rows:this.size}).then(function (res) {
          let data = res.data.data;
          that.transaction = data.rows;
          that.total = data.total;
        });
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
      height: 168px;
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
      .search {
        position: absolute;
        right: 0;
        top: -20px;
      }
      .overview-div {
        h2 {
          margin: 40px 0 20px;
          font-size: 20px;
          color: #5688ED;
        }
        .list {
          ul li {
            font-size: 14px;
            border-bottom: 1px solid #FCFCFC;
            padding: 15px;
            span {
              display: inline-block;
            }
            .name {
              width: 240px;
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
