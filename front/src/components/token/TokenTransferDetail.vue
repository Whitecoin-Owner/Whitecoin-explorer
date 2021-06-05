<template>
  <div class="operation-details">
    <header>
      {{$t('transferDetails.tokenTransfers.title')}}
    </header>
    <div class="con">
              <ul>
                <li>
                  <span class="name">From</span>
                  <span class="value">{{detail.fromAccount}}</span>
                </li>
                <li>
                  <span class="name">{{$t('transferDetails.tokenTransfers.transfer')}}</span>
                  <span class="value">
                    {{detail.amountStr}} &nbsp;
                    <router-link
                      :to="'/contractOverview/'+detail.contractId"
                    >{{detail.symbol}}</router-link>
                  </span>
                </li>
                <li>
                  <span class="name">To</span>
                  <span class="value">{{detail.toAccount}}</span>
                </li>
              </ul>
            </div>
  </div>
</template>

<script>
  import mixin from '../../utils/mixin';
  export default {
    mixins: [mixin],
    name: "token-transfer-detail",
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
