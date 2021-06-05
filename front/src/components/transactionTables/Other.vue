<template>
  <div class="table-wrap">
    <el-table
      :data="tableData"

      style="width: 100%"
    >
      <el-table-column
        align="center"
        :label="$t('home.transaction.txHash')"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <router-link :to="'/transfer_details/'+scope.row.trxId+'/'+scope.row.opType">{{scope.row.trxId}}</router-link>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        :label="$t('transaction.block')"
        width="160">
        <template slot-scope="scope">
          <router-link :to="'/blockDetails/'+scope.row.blockNum">{{scope.row.blockNum}}</router-link>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        :formatter="getTypeName"
        :label="$t('transaction.types')"
        width="240"
        show-overflow-tooltip
      >
      </el-table-column>
      <el-table-column
        align="center"
        :label="$t('transaction.age')"
        width="240"
      >
        <template slot-scope="scope">
          <div>
            <timeago :since="scope.row.trxTime" :locale="getBusLocal" :auto-update="0.2"></timeago>
            &nbsp;
            <span>{{new Date(scope.row.trxTime)}}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        :label="$t('transaction.fee')"
        width="220"
      >
        <template slot-scope="scope">
          {{scope.row.feeStr}}
          <img v-if="scope.row.guaranteeUse" class="feeShow" src="../../assets/img/shouxufei.png"/>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
  import typeObj from "../../utils/type";
  import bus from "../../utils/bus";

  export default {
    name: "other",
    props: ['tableData'],
    methods: {
      getTypeName(row) {
        return typeObj[row.opType];
      }
    },
    computed: {
      getBusLocal() {
        return bus.local;
      }
    }
  }
</script>

<style scoped>

</style>
