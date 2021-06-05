<template>
  <div class="operation-details">
    <header>
      {{$t('transferDetails.swapEvent.title')}}
    </header>
    <div class="con">
              <ul v-if="detail.eventName=='NativeBalanceChange'">
                <li>
                    <span class="name">Event Type</span>
                    <span class="value">{{$t('transferDetails.swapEvent.' + detail.eventName)}}</span>
                </li>
                <li>
                  <span class="name">Address</span>
                  <span class="value">{{detail.changeAddress}}</span>
                </li>
                <li>
                  <span class="name">Amount</span>
                  <span class="value">
                    {{detail.change}} &nbsp;
                    {{detail.symbol}}
                  </span>
                </li>
                <li>
                  <span class="name">Reason</span>
                  <span class="value">{{detail.reason}}</span>
                </li>
              </ul>

              <ul v-if="detail.eventName=='Exchanged'">
                <li>
                    <span class="name">Event Type</span>
                    <span class="value">{{$t('transferDetails.swapEvent.' + detail.eventName)}}</span>
                </li>
                <li>
                  <span class="name">Address</span>
                  <span class="value">{{detail.changeAddress}}</span>
                </li>
                <li>
                  <span class="name">Sell</span>
                  <span class="value">
                    {{detail.sellAmount}} &nbsp;
                    {{detail.sellAsset}}
                  </span>
                </li>
                <li>
                  <span class="name">Buy</span>
                  <span class="value">
                    {{detail.buyAmount}} &nbsp;
                    {{detail.buyAsset}}
                  </span>
                </li>
              </ul>

              <ul v-if="detail.eventName=='LiquidityTokenMinted'">
                <li>
                    <span class="name">Event Type</span>
                    <span class="value">{{$t('transferDetails.swapEvent.' + detail.eventName)}}</span>
                </li>
                <li>
                  <span class="name">Address</span>
                  <span class="value">{{detail.callerAddress}}</span>
                </li>
                <li>
                  <span class="name">Amount</span>
                  <span class="value">
                    {{detail.change}} &nbsp;
                    {{detail.symbol}}
                  </span>
                </li>
              </ul>

              <ul v-if="detail.eventName=='LiquidityTokenDestoryed'">
                <li>
                    <span class="name">Event Type</span>
                    <span class="value">{{$t('transferDetails.swapEvent.' + detail.eventName)}}</span>
                </li>
                <li>
                  <span class="name">Address</span>
                  <span class="value">{{detail.callerAddress}}</span>
                </li>
                <li>
                  <span class="name">Amount</span>
                  <span class="value">
                    {{detail.change}} &nbsp;
                    {{detail.symbol}}
                  </span>
                </li>
              </ul>

              <ul v-if="detail.eventName=='LiquidityAdded'">
                <li>
                    <span class="name">Event Type</span>
                    <span class="value">{{$t('transferDetails.swapEvent.' + detail.eventName)}}</span>
                </li>
                <li>
                  <span class="name">Address</span>
                  <span class="value">{{detail.changeAddress}}</span>
                </li>
                <li>
                  <span class="name">{{detail.liquidityToken1}}</span>
                  <span class="value">
                    {{detail.liquidityToken1ChangeAmount}}
                  </span>
                </li>
                <li>
                  <span class="name">{{detail.liquidityToken2}}</span>
                  <span class="value">
                    {{detail.liquidityToken2ChangeAmount}}
                  </span>
                </li>
              </ul>

              <ul v-if="detail.eventName=='LiquidityRemoved'">
                <li>
                    <span class="name">Event Type</span>
                    <span class="value">{{$t('transferDetails.swapEvent.' + detail.eventName)}}</span>
                </li>
                <li>
                  <span class="name">Address</span>
                  <span class="value">{{detail.changeAddress}}</span>
                </li>
                <li>
                  <span class="name">{{detail.liquidityToken1}}</span>
                  <span class="value">
                    {{detail.liquidityToken1ChangeAmount}}
                  </span>
                </li>
                <li>
                  <span class="name">{{detail.liquidityToken2}}</span>
                  <span class="value">
                    {{detail.liquidityToken2ChangeAmount}}
                  </span>
                </li>
              </ul>

        </div>
  </div>
</template>

<script>
  import mixin from '../../utils/mixin';
  export default {
    mixins: [mixin],
    name: "swap-event-detail",
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
