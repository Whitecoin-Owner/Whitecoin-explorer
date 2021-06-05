<template>
  <div class="operation-details">
    <template v-for="(item,index) in detail">
      <header  :key="index">
        <!--{{$t('transferDetails.details.transfer')}}-->
        {{getTypeName(type)}}
      </header>
      <div class="con"  :key="index">
        <ul>
          <li class="value">
                <span class="name">
                  {{$t('transferDetails.details.value')}}
                </span>
                <span class="from">
                  {{item.value}}
                </span>
          </li>
          <li class="record">
            <div class="re_section">
              <div class="name">
                {{$t('transferDetails.details.from')}}
              </div>
              <div class="from-to">
                <span class="link" @click="_mixin_address_jump(item.fromAddress)">{{item.fromAddress}}</span>
              </div>
              <div class="mid">
                {{$t('transferDetails.details.to')}}
              </div>
              <div class="from-to">
                <span class="link" @click="_mixin_address_jump(item.toAddress)">{{item.toAddress}}</span>
              </div>
            </div>
          </li>
          <li>
            <span class="name">
              {{$t('transferDetails.details.fee')}}
            </span>
            <span>
              {{item.fee}}
            </span>
          </li>
          <li v-if="type === 55 || type === 56">
            <span class="name">
              Citizen:
            </span>
            <span>
              {{item.citizen}}
            </span>
          </li>
          <li>
            <span class="name top">
              {{$t('transferDetails.details.memo')}}
            </span>
            <el-input
              class="border"
              type="textarea"
              :autosize="{ minRows: 2, maxRows: 4}"
              v-model="item.memo"
              readonly>
            </el-input>
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
  import typeObj from "../../../utils/type";

  export default {
    mixins: [mixin],
    name: "operation-transfer",
    props: ['detail','type'],
    methods: {
      getTypeName(typeNum) {
        return typeObj[typeNum] || 'Other';
      }
    }
  }
</script>

<style lang="less" scoped>
  .operation-details {
    .from, .to, .from-to {
      display: inline-block;
    }
    .mid {
      display: inline-block;
      margin: 0 50px;
    }
    .name {
      display: inline-block;
    }
    .from-to {
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
    .el-icon-back {
      transform: rotate(180deg);
      margin: 0 12px;
    }
  }
</style>
