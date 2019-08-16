<template>
  <div class="wrap">
    <div class="background"></div>
    <div class="top-line"></div>
    <main>
      <tips :tips="tips"></tips>
      <template v-if="tips[0].show">
        <!-- Transacton Information -->
        <div class="information">
          <h3>
            {{$t('transferDetails.information.title')}}
          </h3>
          <ul>
            <li>
            <span class="name">
              {{$t('transferDetails.information.txHash')}}
            </span>
              <span>
                  {{detailsData.txHash}}
            </span>
            </li>
            <li>
            <span class="name">
              {{$t('transferDetails.information.status')}}
            </span>
              <span v-if="statusFlag" class="success">
              {{statusName}}
            </span>
              <span v-else class="fail">
              {{statusName}}
            </span>
            </li>
            <li>
            <span class="name">
              {{$t('transferDetails.information.type')}}
            </span>
              <span>
              {{getTypeName(detailsData.txType)}}
            </span>
            </li>
            <li>
            <span class="name">
              {{$t('transferDetails.information.height')}}
            </span>
              <span class="height">
                <router-link :to="'/blockDetails/'+detailsData.blockHeight">
              {{detailsData.blockHeight}}
                </router-link>
            </span>
            </li>
            <li>
            <span class="name">
              {{$t('transferDetails.information.timeStamp')}}
            </span>
              <span>
              <timeago :since="detailsData.timeStamp" :locale="getBusLocal" :auto-update="0.2"></timeago>
            </span>
            </li>
          </ul>
        </div>
        <!-- Operation Details -->
        <div class="details">
          <h3>
            {{$t('transferDetails.details.title')}}
          </h3>
          <contract-invoke v-if="type===79" :detail="detailsData.operationData"></contract-invoke>
          <contract-register v-else-if="type===76" :detail="detailsData.operationData"></contract-register>
          <contract-transfer v-else-if="type===81" :detail="detailsData.operationData"></contract-transfer>
          <contract-upgrade v-else-if="type===77" :detail="detailsData.operationData"></contract-upgrade>
          <operation-transfer v-else-if="type===0 || type ===55 || type === 56" :detail="detailsData.operationData"
                              :type="detailsData.txType"></operation-transfer>
          <crosschain-record v-else-if="type===60" :detail="detailsData.operationData"></crosschain-record>
          <crosschain-withdraw v-else-if="type===61 || type===65" :detail="detailsData.operationData"
                               :type="detailsData.txType"></crosschain-withdraw>
          <pay-back v-else-if="type===73" :detail="detailsData.operationData"></pay-back>
          <acceptance v-else-if="type===82 || type===83" :detail="detailsData.operationData"
                      :type="detailsData.txType"></acceptance>
          <operation-other v-else :detail="detailsData.operationData" :type="detailsData.txType"></operation-other>
        </div>
      </template>
      <template v-if="tips[1] && tips[1].show">
        <div class="events">
          <span v-for="item in events">
            {{item}}
          </span>
        </div>
      </template>
    </main>
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
  import typeObj from "../utils/type";

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
      Tips
    },
    name: "transfer-details",
    data() {
      return {
        xhHash: '',
        type: null,
        tips: [
          {
            name: this.$t('transferDetails.tips.overview'),
            show: true
          }
        ],
        detailsData: {
          blockHeight: 0,
          timeStamp: null,
          txHash: '',
          txStatus: null,
          txType: null,
          operationData: [
            {contractAddress: ''}
          ]
        },
        events: [],
        statusName: '',
        statusFlag: false
      };
    },
    created() {
      this.xhHash = this.$route.params.xhHash;
      this.type = parseInt(this.$route.params.type);
      if (this.type === 77 || this.type === 79 || this.type === 81) {
        this.tips.push({
          name: this.$t('transferDetails.tips.event'),
          show: false
        });
      }
      this.getTransactionData();
      let that = this;
      bus.$on('tipChange', function (index) {
        that.tipChange(index);
      });
    },
    methods: {
      getTypeName(typeNum) {
        return typeObj[typeNum] || 'Other';
      },
      tipChange(index) {
        for (let i = 0; i < this.tips.length; i++) {
          this.tips[i].show = false;
          this.tips[index].show = true;
        }
      },
      getTransactionData() {
        let that = this;
        this.$axios.post('/queryTransactionDetail', {trxId: this.xhHash, opType: this.type}).then(function (res) {
          that.detailsData = res.data.data;
          if (that.type === 77 || that.type === 79 || that.type === 81) {
            that.$axios.post('/getEvents', {contractId: that.detailsData.operationData[0].contractAddress}).then(function (res) {
              that.events = res.data.data.events;
            });
          }
          let statu = res.data.data.txStatus
          if (that.type === 61) {
            switch (statu) {
              case 0:
                that.statusName = 'Transaction Request';
                that.statusFlag = false;
                break;
              case 1:
                that.statusName = 'Transaction Create';
                that.statusFlag = false;
                break;
              case 2:
                that.statusName = 'Transaction Signature';
                that.statusFlag = false;
                break;
              case 3:
                that.statusName = 'Trading broadcast';
                that.statusFlag = false;
                break;
              case 4:
                that.statusName = 'Success';
                that.statusFlag = true;
                break;
            }
          } else if (that.type === 82) {
            that.statusFlag = statu === 1 ? true : false;
            that.statusName = statu === 1 ? 'Valid' : 'Invalid';
          } else {
            that.statusFlag = statu === 1 ? true : false;
            that.statusName = statu === 1 ? 'Success' : 'Pending';
          }
        })
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
      .information {
        h3 {
          font-size: 20px;
          color: #5688ED;
          margin-top: 40px;
        }
        ul {
          margin-top: 25px;
          li {
            padding: 15px;
            font-size: 14px;
            border-bottom: 1px solid #fcfcfc;
            &:last-of-type {
              border-bottom: none;
            }
            .name {
              display: inline-block;
              width: 250px;
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
      .details {
        h3 {
          font-size: 20px;
          color: #5688ED;
          margin: 40px 0 25px 0;
        }
      }
    }
  }
</style>
