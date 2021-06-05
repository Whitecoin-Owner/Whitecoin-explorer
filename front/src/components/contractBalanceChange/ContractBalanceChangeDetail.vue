<template>
  <div class="operation-details">
    <header>
      {{$t('transferDetails.contractBalanceChangeDetail.' + detail.changeType)}}
    </header>
    <div class="con">
              <ul>
                <li>
                  <span class="name">
                      <div v-if="detail.changeType==='contract_withdraw' || detail.changeType==='deposit_contract' || detail.changeType==='contract_balances'">
                          Contract
                      </div>
                      <div v-if="detail.changeType==='deposit_to_address'">
                          Address
                      </div>
                      </span>
                  <span class="value">
                    <router-link
                        v-if="detail.changeType==='contract_withdraw' || detail.changeType==='deposit_contract' || detail.changeType==='contract_balances'"
                      :to="'/contractOverview/'+detail.address"
                    >{{detail.address}}</router-link>
                    <router-link
                        v-if="detail.changeType==='deposit_to_address'"
                      :to="'/address?address='+detail.address"
                    >{{detail.address}}</router-link>
                  </span>
                </li>
                <li>
                  <span class="name">Value</span>
                  <span class="value">{{detail.amountStr}} {{detail.assetSymbol}}</span>
                </li>
              </ul>
            </div>
  </div>
</template>

<script>
  import mixin from '../../utils/mixin';
  export default {
    mixins: [mixin],
    name: "contract-balance-change-detail",
    props:['detail']
  }
</script>

<style lang="less" scoped>
.operation-details {
  .text_area {
    margin-bottom: 15px;
    border: 1px solid #e5e5e5;
    ul li {
      border: none;
      span {
        text-align: left;
        display: inline-block;
        &:first-of-type {
          width: 250px;
        }
      }
    }
  }
}
</style>
