<template>
  <div class="operation-details">
    <header>
      {{$t('transferDetails.details.payBack')}}
    </header>
    <div class="con">
      <ul>
        <li>
          <span class="name">
            {{$t('transferDetails.details.ownerAddress')}}
          </span>
          <span class="value link" @click="_mixin_address_jump(detail[0].ownerAddr)">
            {{detail[0].ownerAddr}}
          </span>
        </li>
        <li>
          <span class="name">
            {{$t('transferDetails.details.amount')}}
          </span>
          <span class="value">
            <span class="balance" v-for="item in detail[0].balance">
              {{item}}
            </span>
          </span>
        </li>
        <li>
          <span class="name">
            {{$t('transferDetails.details.fee')}}
          </span>
          <span class="value">
            {{detail[0].fee}}
          </span>
        </li>
      </ul>
    </div>
    <template v-if="detail[0].guarantee">
      <header>
        {{$t('transferDetails.details.acceptance')}}
      </header>
      <div class="con">
        <ul>
          <li>
          <span class="name">
            {{$t('transferDetails.details.id')}}
          </span>
            <span class="value">
            {{detail[0].guarantee.guaranteeId}}
          </span>
          </li>
          <li>
          <span class="name">
            {{$t('transferDetails.details.exchange')}}
          </span>
            <span class="value">
            {{detail[0].guarantee.assetOrign}}<i class="el-icon-back rotat"></i>{{detail[0].guarantee.assetTarget}}
          </span>
          </li>
          <li>
          <span class="name">
            {{$t('transferDetails.details.rate')}}
          </span>
            <span class="value">
            {{detail[0].guarantee.rate}}
          </span>
          </li>
          <li>
          <span class="name">
            {{$t('transferDetails.details.ownerAddress')}}
          </span>
            <span class="value link" @click="_mixin_address_jump(detail[0].guarantee.ownerAddr)">
            {{detail[0].guarantee.ownerAddr}}
          </span>
          </li>
        </ul>
      </div>
    </template>
  </div>
</template>

<script>
  import mixin from '../../../utils/mixin';

  export default {
    mixins: [mixin],
    name: "pay-back",
    props: ['detail']
  }
</script>

<style lang="less" scoped>
  .operation-details {
    .el-icon-back {
      margin: 0 12px;
      transform: rotate(180deg);
    }
    .balance {
      margin-right: 16px;
    }
  }
</style>
