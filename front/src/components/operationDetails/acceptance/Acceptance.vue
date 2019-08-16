<template>
  <div class="operation-details">
    <template v-if="type == 82">
      <header>
        {{$t('transferDetails.details.acceptanceCreate')}}
      </header>
      <div class="con">
        <ul>
          <li>
          <span class="name">
            {{$t('transferDetails.details.ownerAddress')}}
          </span>
            <span class="value" @click="_mixin_address_jump(detail[0].ownerAddr)">
            {{detail[0].ownerAddr}}
          </span>
          </li>
          <li>
          <span class="name">
            {{$t('transferDetails.details.exchange')}}
          </span>
            <span class="value">
            {{detail[0].originSymbol}}<i class="el-icon-back"></i>{{detail[0].targetSymbol}}
          </span>
          </li>
          <li>
          <span class="name">
            {{$t('transferDetails.details.rate')}}
          </span>
            <span class="value">
            {{detail[0].rate}}
          </span>
          </li>
          <li>
          <span class="name">
            {{$t('transferDetails.details.valueTarget')}}
          </span>
            <span class="value">
            {{detail[0].value}}
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
    <template v-if="type == 83">
      <header>
        {{$t('transferDetails.details.acceptanceCancel')}}
      </header>
      <div class="con">
        <ul>
          <li>
          <span class="name">
            {{$t('transferDetails.details.ownerAddress')}}
          </span>
            <span class="value" @click="_mixin_address_jump(detail[0].ownerAddr)">
            {{detail[0].ownerAddr}}
          </span>
          </li>
          <li>
          <span class="name">
            {{$t('transferDetails.details.acceptanceID')}}
          </span>
            <span class="value">
            {{detail[0].guaranteeId}}
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
  </div>
</template>

<script>
  import mixin from '../../../utils/mixin';
    export default {
      mixins: [mixin],
      name: "acceptance",
      props:['type','detail']
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
