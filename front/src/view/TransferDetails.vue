<template>
  <div class="wrap">
    <div class="tr_main">
      <div class="con_top">
        <p>{{$t('transferDetails.tips.overview')}}</p>
        <Search class="search_con"/>
      </div>
      <div class="con_all">
        <template v-if="tips[0].show">
          <!-- Transacton Information -->
          <div class="information">
            <h3>{{$t('transferDetails.information.title')}}</h3>
            <div class="transfer_con">
              <ul>
                <li>
                  <span class="name">{{$t('transferDetails.information.txHash')}}</span>
                  <span>{{detailsData.txHash}}</span>
                </li>
                <li>
                  <span class="name">{{$t('transferDetails.information.status')}}</span>
                  <span v-if="statusFlag" class="success">{{statusName}}</span>
                  <span v-else class="fail">{{statusName}}</span>
                </li>
                <li>
                  <span class="name">{{$t('transferDetails.information.type')}}</span>
                  <span>{{getTypeName(detailsData.txType)}}</span>
                </li>
                <li>
                  <span class="name">{{$t('transferDetails.information.height')}}</span>
                  <span class="height">
                    <router-link
                      :to="'/blockDetails/'+detailsData.blockHeight"
                    >{{detailsData.blockHeight}}</router-link>
                  </span>
                </li>
                <li>
                  <span class="name">{{$t('transferDetails.information.timeStamp')}}</span>
                  <span>
                    <timeago :since="detailsData.timeStamp" :locale="getBusLocal" :auto-update="0.2"></timeago>
                    &nbsp;
                    <span>{{detailsData.timeStamp}}</span>
                  </span>
                </li>
              </ul>
              <div
              class="tokenTransfers"
              v-if="detailsData && detailsData.tokenTransactions && detailsData.tokenTransactions.length>0"
              >
                <div
                  v-for="(tokenTransfer, idx) in detailsData.tokenTransactions"
                  :key="idx"
                  class="token-transfer-detail"
                >
                <token-transfer-detail :detail="tokenTransfer"></token-transfer-detail>
                </div>
              </div>
            </div>
          </div>
          <!-- Token transfers -->
          
          <!-- Operation Details -->
          <div class="details">
            <h3>{{$t('transferDetails.details.title')}}</h3>
            <div class="transfer_con">
              <contract-invoke v-if="type===79" :detail="detailsData.operationData"></contract-invoke>
              <contract-register v-else-if="type===76" :detail="detailsData.operationData"></contract-register>
              <contract-transfer v-else-if="type===81" :detail="detailsData.operationData"></contract-transfer>
              <contract-upgrade v-else-if="type===77" :detail="detailsData.operationData"></contract-upgrade>
              <operation-transfer
                v-else-if="type===0 || type ===55 || type === 56"
                :detail="detailsData.operationData"
                :type="detailsData.txType"
              ></operation-transfer>
              <crosschain-record v-else-if="type===60" :detail="detailsData.operationData"></crosschain-record>
              <crosschain-withdraw
                v-else-if="type===61 || type===65"
                :detail="detailsData.operationData"
                :type="detailsData.txType"
              ></crosschain-withdraw>
              <pay-back v-else-if="type===73" :detail="detailsData.operationData"></pay-back>
              <acceptance
                v-else-if="type===82 || type===83"
                :detail="detailsData.operationData"
                :type="detailsData.txType"
              ></acceptance>
              <operation-other v-else :detail="detailsData.operationData" :type="detailsData.txType"></operation-other>
            </div>
          </div>
          
          <div class="swapEventsDetails"
            v-if="detailsData && detailsData.swapTransactions && detailsData.swapTransactions.length>0">
            <div class="transfer_con">
              <div v-for="(detail, idx) in detailsData.swapTransactions" :key="idx" class="swap-event-detail">
                <swap-event-detail :detail="detail"></swap-event-detail>
              </div>
            </div>
          </div>

          
          <div class="contractBalanceChangeDetails"
            v-if="detailsData && detailsData.txContractBalanceChanges && detailsData.txContractBalanceChanges.length>0">
            <div class="transfer_con">
              <div v-for="(detail, idx) in detailsData.txContractBalanceChanges" :key="idx" class="token-transfer-detail">
                <contract-balance-change-detail :detail="detail"></contract-balance-change-detail>
              </div>
            </div>
          </div>

        </template>
        <template v-if="tips[1] && tips[1].show">
          <div class="events">
            <span v-for="(item,index) in events" :key="index">{{item}}</span>
          </div>
        </template>
      </div>
    </div>
  </div>
</template>

<script>
import Tips from "../components/tips/Tips";
import bus from "../utils/bus";
import ContractInvoke from "../components/operationDetails/contract/ContractInvoke";
import ContractRegister from "../components/operationDetails/contract/ContractRegister";
import ContractTransfer from "../components/operationDetails/contract/ContractTransfer";
import ContractUpgrade from "../components/operationDetails/contract/ContractUpgrade";
import OperationTransfer from "../components/operationDetails/transfer/Transfer";
import CrosschainRecord from "../components/operationDetails/recharge/CrosschainRecord";
import CrosschainWithdraw from "../components/operationDetails/withdraw/CrosschainWithdraw";
import OperationOther from "../components/operationDetails/other/operationOther";
import PayBack from "../components/operationDetails/wage/PayBack";
import Acceptance from "../components/operationDetails/acceptance/Acceptance";
import TokenTransferDetail from '../components/token/TokenTransferDetail';
import ContractBalanceChangeDetail from '../components/contractBalanceChange/ContractBalanceChangeDetail';
import SwapEventDetail from '../components/swap/SwapEventDetail';
import typeObj from "../utils/type";
import Search from "../components/search/Search";
import dayjs from 'dayjs';

export default {
  components: {
    Acceptance,
    PayBack,
    OperationOther,
    CrosschainWithdraw,
    CrosschainRecord,
    OperationTransfer,
    ContractUpgrade,
    ContractTransfer,
    ContractRegister,
    ContractInvoke,
    TokenTransferDetail,
    ContractBalanceChangeDetail,
    SwapEventDetail,
    Tips,
    Search
  },
  name: "transfer-details",
  data() {
    return {
      xhHash: "",
      type: null,
      tips: [
        {
          name: this.$t("transferDetails.tips.overview"),
          show: true
        }
      ],
      detailsData: {
        blockHeight: 0,
        timeStamp: null,
        txHash: "",
        txStatus: null,
        txType: null,
        operationData: [{ contractAddress: "" }]
      },
      events: [],
      statusName: "",
      statusFlag: false
    };
  },
  created() {
    this.xhHash = this.$route.params.xhHash;
    this.type = parseInt(this.$route.params.type);
    if (this.type === 77 || this.type === 79 || this.type === 81) {
      this.tips.push({
        name: this.$t("transferDetails.tips.event"),
        show: false
      });
    }
    this.getTransactionData();
    let that = this;
    bus.$on("tipChange", function(index) {
      that.tipChange(index);
    });
    bus.navChoice = 2;
  },
  methods: {
    getTypeName(typeNum) {
      return typeObj[typeNum] || "Other";
    },
    tipChange(index) {
      for (let i = 0; i < this.tips.length; i++) {
        this.tips[i].show = false;
        this.tips[index].show = true;
      }
    },
    getTransactionData() {
      let that = this;
      this.$axios
        .post("/queryTransactionDetail", {
          trxId: this.xhHash,
          opType: this.type
        })
        .then(function(res) {
          if(res.data.retCode===200 && res.data.data !==null){
            res.data.data.timeStamp = dayjs(res.data.data.timeStamp).format('YYYY-MM-DD HH:mm:ss');
            that.detailsData = res.data.data;
            console.log(that.detailsData)
            if (that.type === 77 || that.type === 79 || that.type === 81) {
              that.$axios
                .post("/getEvents", {
                  contractId: that.detailsData.operationData[0].contractAddress
                })
                .then(function(res) {
                  that.events = that.detailsData.events;
                });
            }
            let statu = res.data.data.txStatus;
            if (that.type === 61) {
              switch (statu) {
                case 0:
                  that.statusName = "Transaction Request";
                  that.statusFlag = false;
                  break;
                case 1:
                  that.statusName = "Transaction Create";
                  that.statusFlag = false;
                  break;
                case 2:
                  that.statusName = "Transaction Signature";
                  that.statusFlag = false;
                  break;
                case 3:
                  that.statusName = "Trading broadcast";
                  that.statusFlag = false;
                  break;
                case 4:
                  that.statusName = "Success";
                  that.statusFlag = true;
                  break;
              }
            } else if (that.type === 82) {
              that.statusFlag = statu === 1 ? true : false;
              that.statusName = statu === 1 ? "Valid" : "Invalid";
            } else {
              that.statusFlag = statu === 1 ? true : false;
              that.statusName = statu === 1 ? "Success" : "Pending";
            }
          }
        });
    }
  },
  computed: {
    getBusLocal() {
      return bus.local;
    }
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
      .information,.details,.tokenTransfers,.contractBalanceChangeDetails,.swapEventsDetails{
        h3 {
          font-size: 16px;
          color: #333333;
          border-bottom: 1px solid #EEEEEE;
          font-weight: 600;
          padding: 20px 0 20px 30px;
        }
        .transfer_con{
          width: 94.5%;
          margin: 0 auto;
          border: 1px solid #B8C8DA;
          border-radius: 8px;
          margin-top: 20px;
          ul {
            box-sizing: border-box;
            padding: 20px;
            color:#333333;
            li {
              padding: 10px;
              font-size: 14px;
              border-bottom: 1px solid #fcfcfc;
              &:last-of-type {
                border-bottom: none;
              }
              .name {
                display: inline-block;
                width: 160px;
              }
              .success {
                color: green;
              }
              .fail {
                color: red;
              }
            }
          }
        }
        
      }
    }
  }
}

</style>
