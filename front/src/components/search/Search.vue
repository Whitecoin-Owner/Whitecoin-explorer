<template>
  <div class="wrap">
    <input :placeholder="$t('search.placeholder')" v-model="keyWord" @keyup.enter="search" />
    <span class="search-btn" @click="search">
      <i v-if="false" class="iconfont icon-sousuo"></i>
    </span>
  </div>
</template>

<script>
export default {
  name: "search",
  data() {
    return {
      keyWord: ""
    };
  },
  methods: {
    search() {
      this.keyWord = this.keyWord.trim(); //去首尾空格
      let that = this;
      let regS = /(^\s+)|(\s+$)|\s+/g;
      let reg = /^[0-9]+[0-9]*$/;
      if (regS.test(this.keyWord)) {
        this.$message({
          message: this.$t("alert.noSpace"),
          type: "warning"
        });
      } else if (this.keyWord === "") {
        this.$message({
          message: this.$t("alert.enterContent"),
          type: "warning"
        });
      } else if (this.keyWord.length === 40) {
        this.$axios
          .post("queryTransactionList", {
            trxId: this.keyWord,
            page: 1,
            rows: 10
          })
          .then(function(res) {
            let data = res.data.data;
            if (data.total > 0) {
              that.$router.push({
                path: "/transaction",
                query: { txHash: that.keyWord }
              });
            } else {
              that.$message({
                message: that.$t("alert.notSearch"),
                type: "warning"
              });
            }
          });
      } else if (this.keyWord.startsWith("XWCC")) {
        this.$axios
          .post("/getContractStatis", { contractId: this.keyWord })
          .then(function(res) {
            if (res.data.data) {
              that.$router.push({ path: `/contractOverview/${that.keyWord}` });
            } else {
              that.$message({
                message: that.$t("alert.notSearch"),
                type: "warning"
              });
            }
          });
      } else if (reg.test(this.keyWord)) {
        this.$axios
          .post("/queryBlockByNum", { blockNum: this.keyWord })
          .then(function(res) {
            if (res.data.data) {
              that.$router.push({ path: `/blockDetails/${that.keyWord}` });
            } else {
              that.$message({
                message: that.$t("alert.notSearch"),
                type: "warning"
              });
            }
          });
      } else {
        this.$axios
          .post("/addrjudge", { address: this.keyWord })
          .then(function(res) {
            if (res.data.data) {
              that.$router.push({
                path: "/address",
                query: { address: that.keyWord }
              });
            } else {
              that.$message({
                message: that.$t("alert.notSearch"),
                type: "warning"
              });
            }
          });
      }
    }
  }
};
</script>

<style lang="less" scoped>
.wrap {
  width: 474px;
  height: 30px;
  position: relative;
  @media screen and (max-width: 1400px) {
    width: 400px;
  }
  input {
    width: calc(~"100% - 40px");
    height: 100%;
    position: absolute;
    line-height: 30px;
    box-sizing: border-box;
    background: rgba(11, 14, 46, 1);
    // border-bottom: 2px solid white;
    color: white;
    outline: none;
    left: 10px;
    top: 0;
    border-radius: 15px;
    text-indent: 15px;
    &::placeholder {
      color: white;
      font-size: 13px;
    }
  }

  .search-btn {
    display: block;
    position: absolute;
    right: 0;
    top: 0;
    /*width: 60px;*/
    height: 30px;
    line-height: 30px;
    text-align: center;
    color: black;
    cursor: pointer;
    .iconfont {
      font-size: 24px;
    }
    width: 43px;
    height: 32px;
    // background: rgba(76, 93, 143, 1);
    background: #4C5D8F;
    border-radius: 0px 16px 16px 0px;
    color: white;
    right: 33px;
    background-image: url(/static/img/browser_search_ico.png);
    opacity: 0.5;
    background-repeat: no-repeat;
        background-size: 20px;
        background-position-y: center;
    background-position-x: center;
  }
}
</style>
