<template>
  <div class="operation-details">
    <template v-if="type == 61">
      <header>
        {{$t('transferDetails.details.crosschainWithdraw')}}
      </header>
      <div class="con">
        <ul>
          <li>
          <span class="name">
            {{$t('transferDetails.details.value')}}
          </span>
            <span class="value">
            {{detail[0].value}}
          </span>
          </li>
          <li>
          <span class="name">
            {{$t('transferDetails.details.from')}}
          </span>
            <span class="value link" @click="_mixin_address_jump(detail[0].fromAddress)">
            {{detail[0].fromAddress}}
          </span>
          </li>
          <li>
          <span class="name">
            {{$t('transferDetails.details.to')}}
          </span>
            <span class="value link" @click="_mixin_address_jump(detail[0].toAddress)">
            {{detail[0].toAddress}}
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
    </template>
    <template v-if="detail[0].withdrawResult">
      <header>
        {{$t('transferDetails.details.crosschainWithdrawResult')}}
      </header>
      <div class="con">
        <ul>
          <li>
          <span class="name">
            {{$t('transferDetails.details.minerName')}}
          </span>
            <span class="value">
              <router-link :to="'/address?minerName='+detail[0].withdrawResult.minerName">
                {{detail[0].withdrawResult.minerName}}
              </router-link>
          </span>
          </li>
          <li>
          <span class="name">
            {{$t('transferDetails.details.minerAddress')}}
          </span>
            <span class="value">
              <router-link :to="'/address?address='+detail[0].withdrawResult.minerAddr">
                {{detail[0].withdrawResult.minerAddr}}
              </router-link>
          </span>
          </li>
          <li>
          <span class="name">
            {{$t('transferDetails.details.txHash')}}
          </span>
            <span class="value">
            {{detail[0].withdrawResult.txHash}}
          </span>
          </li>
        </ul>
      </div>
    </template>
    <template v-if="type == 65">
      <header>
        {{$t('transferDetails.details.crosschainWithdrawResult')}}
      </header>
      <div class="con">
        <ul>
          <li>
          <span class="name">
            {{$t('transferDetails.details.value')}}
          </span>
            <span class="value">
            {{detail[0].value}}
          </span>
          </li>
          <li>
          <span class="name">
            {{$t('transferDetails.details.minerName')}}
          </span>
            <span class="value">
              <router-link :to="'/address?minerName='+detail[0].minerName">
                {{detail[0].minerName}}
              </router-link>
          </span>
          </li>
          <li>
          <span class="name">
            {{$t('transferDetails.details.minerAddress')}}
          </span>
            <span class="value">
              <router-link :to="'/address?minerName='+detail[0].minerAddr">
                {{detail[0].minerAddr}}
              </router-link>
          </span>
          </li>
          <li>
          <span class="name">
            {{$t('transferDetails.details.fromAddress')}}
          </span>
            <span class="value link" @click="_mixin_address_jump(detail[0].fromAddress)">
            {{detail[0].fromAddress}}
          </span>
          </li>
          <li>
          <span class="name">
            {{$t('transferDetails.details.toAddress')}}
          </span>
            <span class="value link" @click="_mixin_address_jump(detail[0].toAddress)">
            {{detail[0].toAddress}}
          </span>
          </li>
        </ul>
      </div>
    </template>
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
    name: "crosschain-withdraw",
    props: ['detail', 'type']
  }
</script>

<style lang="less" scoped>
  .operation-details {
    .el-icon-back {
      margin: 0 12px;
      transform: rotate(180deg);
    }
  }
</style>
