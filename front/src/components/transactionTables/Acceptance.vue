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
          <router-link    :to="'/transfer_details/'+scope.row.trxId+'/'+scope.row.opType">{{scope.row.trxId}}</router-link>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        :label="$t('transaction.block')"
        width="120">
        <template slot-scope="scope">
          <router-link    :to="'/blockDetails/'+scope.row.blockNum">{{scope.row.blockNum}}</router-link>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        :formatter="getTypeName"
        :label="$t('transaction.types')"
        width="200"
      >
      </el-table-column>
      <el-table-column
        align="center"
        :label="$t('transaction.createTime')"
        width="200"
      >
        <template slot-scope="scope">
          <timeago :since="scope.row.createdTime" :locale="getBusLocal" :auto-update="0.2"></timeago>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        prop="exchange"
        :label="$t('transaction.exchange')"
        width="180"
      >
      </el-table-column>
      <el-table-column
        align="center"
        prop="feeStr"
        :label="$t('transaction.fee')"
        width="160"
      >
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
  import typeObj from "../../utils/type";
  import bus from "../../utils/bus";

  export default {
    name: "acceptance",
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

<style lang="less" scoped>

</style>
