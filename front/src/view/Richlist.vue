<template>
  <div class="wrap">
    <div class="background"></div>
    <div class="top-line"></div>
    <main>
      <h2>{{$t('richlist.title')}}</h2>
      <div class="table-wrap">
        <el-table :data="richlist" style="width: 100%">
          <el-table-column type="index" width="50"></el-table-column>
          <el-table-column align="center" :label="$t('richlist.address')" show-overflow-tooltip>
            <template slot-scope="scope">
              <router-link :to="'/address?address='+scope.row.addr">{{scope.row.addr}}</router-link>
              <span style="color: red;" v-if="scope.row.addr==='XWCNWKLUcsybWt4bW5EXV1CfdaSNHiSKj4Hzw' || scope.row.addr==='XWCNi146ffqUffGJk3tTjnY1MdVGJn3m8jH29'">({{$t('address.overview.abnormal_address')}})</span>
            </template>
          </el-table-column>
          <el-table-column align="center" show-overflow-tooltip :label="$t('richlist.accountName')">
            <template slot-scope="scope">
              <span>{{scope.row.name}}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" show-overflow-tooltip :label="$t('richlist.amount')">
            <template slot-scope="scope">
              <span>{{scope.row.amount}} XWC</span>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </main>
  </div>
</template>

<script>
import bus from "../utils/bus";
import common from "../utils/common";
import mixin from "../utils/mixin";
export default {
  mixins: [mixin],
  name: "contracts",
  data() {
    return {
      page: 1,
      size: 100,
      total: 0,
      richlist: []
    };
  },
  created() {
    this.getRichlistData();
  },
  methods: {
    getRichlistData() {
      let that = this;
      if (window.localStorage) {
        let richlistLocalStr = localStorage.getItem("xwc.richlist");
        if (richlistLocalStr && richlistLocalStr !== "") {
          try {
            let richlistLocal = JSON.parse(richlistLocalStr);
            that.richlist = richlistLocal;
            that.total = richlistLocal.length;
          } catch (e) {}
        }
      }
      this.$axios.get("/richlist", {}).then(function(res) {
        let data = res.data.data;
        const limit = 100;
        if (data.length > limit) {
          data.length = limit;
        }
        that.richlist = data;
        that.total = data.length;
        if (window.localStorage) {
          localStorage.setItem("xwc.richlist", JSON.stringify(data));
        }
      });
    },
    pageChange(page) {
      this.page = page;
      this.getRichlistData();
    },
    dataFormate(row) {
      let time = new Date(row.createTime);
      return common.format(time, "yyyy-MM-dd hh:mm:ss");
    }
  },
  computed: {
    getBusLocal() {
      return bus.local;
    }
  }
};
</script>

<style lang="less" scoped>
.wrap {
  .top-line {
    height: 1px;
  }
  .background {
    width: 100%;
    height: 338px;
    position: absolute;
    top: 0;
    left: 0;
    background: white;
  }
  main {
    width: 77%;
    min-width: 1160px;
    margin: 120px auto;
    position: relative;
    color: black;
    .search {
      position: absolute;
      right: 0;
      top: 0;
    }
    h2 {
      font-size: 36px;
    }
    .total {
      margin: 10px 0 65px;
    }
    .pagination {
      text-align: center;
      margin-top: 20px;
    }
  }
}
</style>
