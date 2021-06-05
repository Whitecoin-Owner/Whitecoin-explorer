<template>
  <div class="wrap" @click="isShow = false">
    <div class="tr_main">
      <SearchMobile class="search_con"/>
      <div class="con_top">
        <p>{{$t('address.overview.title')}} </p>
        <p>
          <span class="address_link">{{minerInfo.address}} <span
                    style="color: red;"
                    v-if="minerInfo.address==='XWCNWKLUcsybWt4bW5EXV1CfdaSNHiSKj4Hzw' || minerInfo.address==='XWCNi146ffqUffGJk3tTjnY1MdVGJn3m8jH29'"
                  >({{$t('address.overview.abnormal_address')}})</span></span>
          <span class="copy" v-clipboard:copy="minerInfo.address" v-clipboard:success="onCopy" v-clipboard:error="onError"></span>
        </p>
      </div>
      <div class="con_all">
        <div class="all_section">
          <div class="all_left"> 
            <ul>
              <li>
                <span>{{$t('address.overview.name')}}</span>
                <span>{{minerInfo.name}}</span>
              </li>
              <li>
                <span>{{$t('miner.overview.transaction')}}</span>
                <span>{{minerInfo.transaction}}</span>
              </li>  
              <li>
                <span>{{$t('address.overview.lockBalance')}}</span>
                <span>
                  <template v-for="(item, index) in minerInfo.lockBalances">
                    {{item}}
                    <template v-if="index < minerInfo.lockBalances.length - 1">,</template>
                  </template>
                </span>
              </li>
              <!-- <li>
                <span>{{$t('address.overview.contracts')}}</span>
                <span>{{minerInfo.contracts}}</span>
              </li> -->
            </ul>
          </div>
          <div class="all_right">
            <ul>
              <li>
                <span>XWC{{$t('address.overview.balances')}}</span>
                <span v-if="minerInfo.balances">{{minerInfo.balances[0]}}</span>
              </li>
              <li @click.stop="nameSymbol" class="rightName">
                <span>{{$t('address.overview.tokenBalances')}}</span>
                <span v-if="tokenBalances.length>0">{{tokenAmount}} <em class="tp_click">{{tokenName}} <strong></strong></em> </span>
                <div class="ser_input" v-show="isShow">
                  <div class="ser_icon" @click.stop="isShow=true">
                    <b></b>
                    <input type="text" :placeholder="$t('search.placeholder2')" v-model='serValue' @input="tokenBalancesData($event)">
                  </div>
                  <div class="daibi"  v-if="tokenBalancesResult.length>0 && serValue !=''">
                    <div class="daibi_li" v-for="(item,index) of tokenBalancesResult" :key="index">
                      <img :src="item.imgUrl!==null ? item.imgUrl:require('../../assets/img/icon_logo/XWC.png')" alt="">
                      <div class="daibi_p" v-if="item.id !==null" @click.stop="changeToken(item.id)">
                        <p>{{item.tokenSymbol}}</p>
                        <p>{{item.tokenContract!==null ? (item.tokenContract.substring(0,29)+'...') : '--'}}</p>
                      </div>
                      <div class="daibi_p" v-else @click.stop="changeToken(item.tokenSymbol)">
                        <p>{{item.tokenSymbol}}</p>
                        <p>{{item.tokenContract!==null ? (item.tokenContract.substring(0,29)+'...') : '--'}}</p>
                      </div>
                    </div>
                  </div>
                  <div class="daibi" v-else-if="tokenBalancesResult.length==0 && serValue !=''">
                    <div class="daibi_li">
                      NO Data
                    </div>
                  </div>
                  <div class="daibi" v-if="tokenBalances.length>0 && serValue ==''">
                    <div class="daibi_li" v-for="(item,index) of tokenBalances" :key="index" @click.stop="changeToken(index)">
                      <img :src="item.imgUrl!==null ? item.imgUrl:require('../../assets/img/icon_logo/XWC.png')" alt="">
                      <div class="daibi_p" v-if="item.id !==null" @click.stop="changeToken(item.id)">
                        <p>{{item.tokenSymbol}}</p>
                        <p>{{item.tokenContract!==null ? (item.tokenContract.substring(0,29)+'...') : '--'}}</p>
                      </div>
                      <div class="daibi_p" v-else @click.stop="changeToken(item.tokenSymbol)">
                        <p>{{item.tokenSymbol}}</p>
                        <p>{{item.tokenContract!==null ? (item.tokenContract.substring(0,29)+'...') : '--'}}</p>
                      </div>
                    </div>
                  </div>
                </div>
              </li>
            </ul>
          </div>
        </div>
        <div class="all_aside">
          <div class="all_header">
            <div>   
              <span
                @click="choiceFlagChange(0)"
                :class="{'choice':choiceFlag===0}"
              >{{$t('transferDetails.tokenTransfers.transfer')}}</span>
              <span
                @click="choiceFlagChange(2)"
                :class="{'choice':choiceFlag===2}"
              >{{$t('transferDetails.tokenTransfers.title')}}</span>
            </div>
          </div>
          <div v-if="choiceFlag===0" class="table-wrap">
            <div class="trans_ul">
              <li v-for="(item,index) of transaction" :key="index">
                <p><span>{{$t('address.myTransactions.txHash')}}</span>
                  <router-link :to="'/transfer_details/'+item.trxId+'/'+item.opType">{{item.trxId}}</router-link>
                </p>
                <p><span>{{$t('address.myTransactions.block')}}</span><router-link :to="'/blockDetails/'+item.blockNum">{{item.blockNum}}</router-link></p>
                <p><span>{{$t('address.myTransactions.types')}}</span>{{getTypeName(item)}}</p>
                <p><span>{{$t('address.myTransactions.age')}}</span><timeago :since="item.trxTime" :locale="getBusLocal" :auto-update="0.5"></timeago></p>
                <p><span>{{$t('address.myTransactions.from')}}</span><router-link :to="'/address?address='+item.fromAccount">{{item.fromAccount}}</router-link></p>
                <p><span>{{$t('address.myTransactions.to')}}</span><router-link :to="'/address?address='+item.toAccount">{{item.toAccount}}</router-link></p>
                <p><span>{{$t('miner.myTransactions.value')}}</span>{{item.amountStr}}</p>
                <p><span>{{$t('address.myTransactions.fee')}}</span>{{item.feeStr}}<img
                    v-if="item.guaranteeUse"
                    class="feeShow"
                    src="../../assets/img/shouxufei.png"
                  /></p>
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
          <div v-if="choiceFlag===2" class="table-wrap">
            <div class="trans_ul">
              <li v-for="(item,index) of tokenTransactions" :key="index">
                <p><span>{{$t('address.myTransactions.txHash')}}</span>
                  <router-link
                    :to="'/transfer_details/'+item.trxId+'/'+79" 
                  >{{item.trxId}}</router-link>
                </p>
                <p><span>{{$t('address.myTransactions.block')}}</span><router-link :to="'/blockDetails/'+item.blockNum">{{item.blockNum}}</router-link></p>
                <p><span>{{$t('address.myTokenTransactions.symbol')}}</span>{{item.symbol}}</p>
                <p><span>{{$t('address.myTransactions.age')}}</span><timeago :since="item.trxTime" :locale="getBusLocal" :auto-update="0.5"></timeago></p>
                <p><span>{{$t('address.myTransactions.from')}}</span><router-link :to="'/address?address='+item.fromAccount">{{item.fromAccount}}</router-link></p>
                <p><span>{{$t('address.myTransactions.to')}}</span><router-link :to="'/address?address='+item.toAccount">{{item.toAccount}}</router-link></p>
                <p><span>{{$t('miner.myTransactions.value')}}</span>{{item.amountStr}}</p>
                <p><span>{{$t('address.myTransactions.fee')}}</span>{{item.feeStr}}<img
                    v-if="item.guaranteeUse"
                    class="feeShow"
                    src="../../assets/img/shouxufei.png"
                  /></p>
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
  </div>
</template>

<script>
import bus from "../../utils/bus";
import common from "../../utils/common";
import typeObj from "../../utils/type";
import mixin from "../../utils/mixin";
import SearchMobile from "../../components/search/SearchMobile";

export default {
  mixins: [mixin],
  name: "v-address",
  components:{SearchMobile},
  data() {
    return {
      page: 1,
      size: 10,
      total: 0,
      minerName: "",
      address: "",
      choiceFlag: 0,
      minerInfo: {
        name: "", //矿工名
        transaction: 0, //交易总量
        address: "", //矿工地址
        rewards: null, //奖励
        contracts: 0 //拥有合约量
      },
      transaction: [],
      tokenTransactions: [],
      swapTransactions: [],
      tokenBalances: [],
      tokenBalancesResult: [],
      contaracts: [],
      isShow:false,
      tokenAmount:0,
      tokenName:'',
      serValue:'',
    };
  },
  beforeRouteUpdate(to, from, next) {
    if (to.query.address) {
      this.address = to.query.address;
      this.getDataByAddress();
    } else {
      this.minerName = to.query.minerName;
      this.getDataByMinerName();
    }
    next();
  },
  created() {
    if (this.$route.query.address) {
      this.address = this.$route.query.address;
      this.getDataByAddress();
      this.getTokenBalancesData();
      // console.log(this.isShow,'ccc')

    } else {
      this.minerName = this.$route.query.minerName;
      this.getDataByMinerName();
    };
    
    bus.navChoice = 4
  },
  methods: {
    pageChange(page) {
      this.page = page;
      if (this.choiceFlag === 0) {
        this.getTransactionData();
      } else if (flag === 2) {
        this.getTokenTransactionData();
      } else if(flag===3) {
        this.getSwapTransactionData();
      } else if(flag===4) {
        this.getTokenBalancesData();
      } else {
        this.getContractData();
      }
    },
    getTypeName(row) {
      if (row.opType || row.opType === 0) {
        return typeObj[row.opType];
      } else {
        return typeObj[row.type];
      }
    },
    choiceFlagChange(flag) {
      this.choiceFlag = flag;
      this.page = 1;
      if (flag === 0) {
        this.getTransactionData();
      } else if (flag === 2) {
        this.getTokenTransactionData();
      } else if(flag===3) {
        this.getSwapTransactionData();
      } else if(flag===4) {
        this.getTokenBalancesData();
      } else {
        this.getContractData();
      }
    },
    getDataByMinerName() {
      let that = this;
      this.$axios
        .post("/minerInfo", { name: this.minerName })
        .then(function(res) {
          if(res.data.retCode===200 && res.data.data !==null){
            let data = res.data;
            that.minerInfo = data.data;
            // console.log(that.minerInfo,'pp')
            that.getTransactionData();
          }
        });
    },
    getDataByAddress() {
      let that = this;
      this.$axios
        .post("/addrStatis", { address: this.address })
        .then(function(res) {
          if(res.data.retCode===200 && res.data.data !==null){
            let data = res.data;
            that.minerInfo = data.data;
            // console.log(that.minerInfo,'ooo')
            that.getTransactionData();
          }
        });
    },
    getTransactionData() {
      let that = this;
      this.$axios
        .post("/queryTrxByAddr", {
          address: this.minerInfo.address,
          page: this.page,
          rows: this.size
        })
        .then(function(res) {
          if(res.data.retCode===200 && res.data.data !==null){
            let data = res.data;
            // console.log(res.data,'666')
            that.transaction = data.data.rows;
            that.total = data.data.total;
          }
        });
    },
    getTokenBalancesData() {
      let that = this;
      this.$axios
        .get(`/user_tokens/${this.address}`)
        .then(function(res) {
          if(res.data.retCode===200 && res.data.data !==null){
            let data = res.data;
            that.tokenBalances = data.data.tokenBalances;
            // console.log(that.tokenBalances,6666)
            // 代币余额默认值 
            if(that.tokenBalances.length>0){
              that.tokenAmount = that.tokenBalances[0].amount;
              that.tokenName = that.tokenBalances[0].tokenSymbol
            }
          }
        });
    },
    getTokenTransactionData() {
      let that = this;
      this.$axios
        .post("/queryTokenTrxByAddr", {
          address: this.minerInfo.address,
          page: this.page,
          rows: this.size
        })
        .then(function(res) {
          if(res.data.retCode===200 && res.data.data !==null){
            let data = res.data;
            that.tokenTransactions = data.data.rows;
            that.total = data.data.total;
          }
        });
    },
    getSwapTransactionData() {
      let that = this;
      this.$axios
        .post("/querySwapTrxByAddr", {
          address: this.minerInfo.address,
          page: this.page,
          rows: this.size
        })
        .then(function(res) {
          if(res.data.retCode===200 && res.data.data !==null){
            let data = res.data;
            that.swapTransactions = data.data.rows;
            that.total = data.data.total;
          }
        });
    },
    getContractData() {
      let that = this;
      this.$axios
        .post("/addrContracts", {
          ownerAddress: this.minerInfo.address,
          page: this.page,
          rows: this.size
        })
        .then(function(res) {
          if(res.data.retCode===200 && res.data.data !==null){
            let data = res.data;
            that.contaracts = data.data.rows;
            that.total = data.data.total;
          }
        });
    },
    dateFormate(row) {
      return common.format(new Date(row.createTime), "yyyy-MM-dd hh:mm:ss");
    },
    tokenBalancesData(e){
      this.serValue = e.target.value.trim();
      this.tokenBalancesResult = this.tokenBalances.filter(item=>{
        if( (item.tokenSymbol !== null && item.tokenSymbol.includes(this.serValue.toUpperCase()) ) || (item.tokenContract!==null && item.tokenContract.includes(this.serValue)) ){
          return item
        }
      })
    },
    changeToken(index){
      let that = this;
      that.serValue= '';
      that.tokenBalances.forEach(item=>{
        if(item.id === index){
          that.tokenAmount = item.amount;
          that.tokenName = item.tokenSymbol;
          that.isShow = false;
        }else if(item.tokenSymbol == index){
          that.tokenAmount = item.amount;
          that.tokenName = item.tokenSymbol
          that.isShow = false;
        }
      })
    },
    //点击复制成功
    onCopy() {
      this.$notify({
        message: this.$t("message.success"),
        type: "success",
        duration:2000,
      });
    },
    onError() {
      this.$notify.error({
        message: this.$t("message.failed"),
        duration:2000,
      });
    },
    nameSymbol(){
      if(this.tokenBalances.length>0){
        this.isShow = !this.isShow;
        this.serValue = ''
      }
    }
  },
  computed: {
    getBusLocal() {
      return bus.local;
    }
  },
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
      margin-top: 30rem;
      p{
        font-size: 32rem;
        color: #333333;
        font-weight: 600;
        display: flex;
        align-items: center;
        word-break:break-all;
        margin-top: 10rem;
        .normal&.choice{
          font-size: 22rem;
        }
        .address_link{
          font-size: 24rem;
          font-weight: normal;
          margin-right: 20rem;
        }
        span.copy{
          width: 35rem;
          height: 35rem;
          display: block;
          background: url(../../assets/img/copy.png) no-repeat;
          background-size: 100%;
          cursor: pointer;
        }
      }
      .search_con{
        width: 500rem;
      }
    }
    .con_all{
        background: #fff;
        box-shadow: 0rem 2rem 13rem 0rem rgba(0, 0, 0, 0.09);
        margin: 30rem 0;
        overflow: hidden;
        position: relative;
        padding-bottom: 30rem;
      }
    .search {
      position: absolute;
      right: 0;
      top: 0rem;
    }
    h2 {
      font-size: 36rem;
    }
    .all_section {
      overflow: hidden;
      padding: 30rem;
      ul li {
        box-sizing: border-box;
        border-bottom: 1px solid rgba(255, 255, 255, 0.5);
        padding: 12rem 0;
        font-size: 26rem;
        display: flex;
        align-items: center;
        color: #333;
        span {
          display: inline-block; 
          position: relative;
          &:first-of-type {
            width: 30%;
            color: #677897;
          }
          &:last-of-type {
            width: 70%;
            display: flex;
            flex-wrap: wrap;
            align-items: center;
          }
          .tp_click{
            margin-left: 10rem;
            border-radius: 3rem;
            border: 1px solid #B8C8DA;
            padding: 5rem 40rem 3rem 10rem;
            position: relative;
            strong{
              position: absolute;
              background: url(../../assets/img/xia.png) no-repeat;
              background-size: 100%;
              width: 23rem;
              height: 23rem;
              top: 10rem;
              right: 10rem;
            }
          }
        }
      }
    }
    .ser_input{
      position: absolute;
      width: 500rem;
      top: 350rem;
      right: 85rem;
      z-index: 99;
      background: #fff;
      box-shadow: 0rem 0rem 11rem 0rem rgba(0, 0, 0, 0.09);
      box-sizing: border-box;
      padding: 20rem;
      border-radius: 5rem;
      .ser_icon{
        display: flex;
        align-items: center;
        border: 1px solid #DBDBDB;
        border-radius: 5rem;
        padding: 5rem 10rem;
        /deep/ input{
          width: 100%;
          padding-left: 7rem;
          outline: none;
          border: 0;
          height:65rem;
          line-height: 65rem;
          text-decoration: none;
          font-size: 26rem;
          &::placeholder{
            font-size: 20rem !important;
          }
        }
        b{
          width: 30rem;
          height: 30rem;
          background: url(../../assets/img/ser_icon.png) no-repeat;
          background-size: 100%;
          margin-top: 5rem;
        }
      }
      .daibi{
        margin-top: 20rem;
        .daibi_li{
          display: flex;
          align-items: center;
          margin-bottom: 8rem;
          padding-top: 5rem;
          cursor: pointer;
          .default-image{
            background: url(../../assets/img/icon_logo/XWC.png) no-repeat;
          }
          img{
            width: 50rem;
            margin-right: 8rem;
          }
          .daibi_p{
            p:first-child{
              font-size: 26rem;
            }
            p:last-child{
              font-size: 20rem;
              color:#999999;
            }
          }
        }
      }
    }
    .all_aside {
      .all_header {
        display: flex;
        justify-content: space-between;
        font-size: 28rem;
        margin-bottom: 20rem;
        padding-left:30rem;
        div {
          width: 100%;
          border-bottom:1px solid #dedede;
          &:first-of-type {
            span {
              display: inline-block;
              margin-right: 40rem;
              color: #c6cad4;
              position: relative;
              cursor: pointer;
              padding-bottom: 10rem;
            }
            .choice {
              color: #333;
              &::after {
                display: block;
                position: absolute;
                content: "";
                width: 100%;
                height: 2rem;
                background: #0279FF;
                left: 50%;
                z-index: 1;
                transform: translateX(-50%);
                margin-top: 10rem;
              }
            }
          }
        }
      }
      .total {
        color: #333333;
      }
      .table-wrap {
        .trans_ul{
          li{
            border-bottom: 1px solid #dedede;
            padding: 20rem 30rem;
            font-size: 28rem;
            color: #333;
            span{
              color: #677897;
            }
            p{
              display: flex;
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
      .pagination {
        margin-top: 20rem;
      }
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
