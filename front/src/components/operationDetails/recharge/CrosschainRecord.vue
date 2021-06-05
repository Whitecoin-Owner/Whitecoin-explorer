<template>
  <div class="operation-details">
    <header>
      {{$t('transferDetails.details.crosschainRecord')}}
    </header>
    <div class="con">
      <ul>
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
            <router-link :to="'/address?address='+detail[0].minerAddr">
            {{detail[0].minerAddr}}
            </router-link>
          </span>
        </li>
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
    name: "crosschain-record",
    props: ['detail']
  }
</script>

<style lang="less" scoped>
.operation-details {
  .text_area {
    border-top: 1px solid #e5e5e5;
    ul li {
      border: none;
      span {
        text-align: left;
        display: inline-block;
        &:first-of-type {
          width:160px;
        }
      }
    }
  }
}
</style>
