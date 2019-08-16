<template>
  <div class="operation-details">
    <header>
      {{$t('transferDetails.details.contractRegister')}}
    </header>
    <div class="con">
      <ul>
        <li>
          <span class="name">
            {{$t('transferDetails.details.contractAddress')}}
          </span>
          <span class="value link" @click="_mixin_address_jump(detail[0].contractAddress)">
            {{detail[0].contractAddress}}
          </span>
        </li>
        <li>
          <span class="name">
            {{$t('transferDetails.details.authorAddress')}}
          </span>
          <span class="value link" @click="_mixin_address_jump(detail[0].callerAddress)">
            {{detail[0].authorAddress}}
          </span>
        </li>
        <li>
          <span class="name">
            {{$t('transferDetails.details.inheritFrom')}}
          </span>
          <span class="value">
            {{detail[0].inheritFrom}}
          </span>
        </li>
        <!--<li>-->
          <!--<span class="name">-->
            <!--{{$t('transferDetails.details.gasLimit')}}-->
          <!--</span>-->
          <!--<span class="value">-->
            <!--{{detail[0].gasLimit}}-->
          <!--</span>-->
        <!--</li>-->
        <li>
          <span class="name">
            {{$t('transferDetails.details.gasLimit')}}
          </span>
          <span class="value">
           {{detail[0].gasUsed}}
          </span>
        </li>
        <li>
          <span class="name">
            {{$t('transferDetails.details.gasPrice')}}
          </span>
          <span class="value">
            {{detail[0].gasPrice}}
          </span>
        </li>
        <li>
          <span class="name">
            {{$t('transferDetails.details.actualTxCost_Fee')}}
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
            <span class="value" @click="_mixin_address_jump(detail[0].guarantee.ownerAddr)">
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
    name: "contract-register",
    props: ['detail']
  }
</script>

<style lang="less" scoped>
  .operation-details {
    .value {
      .el-icon-back {
        margin: 0 12px;
        transform: rotate(180deg);
      }
    }
  }
</style>
