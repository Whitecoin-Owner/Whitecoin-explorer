<template>
  <div class="wrap">
    <el-input placeholder="Search by Address/TxHash/Contract/Height/Account Name" v-model="keyWord" @keyup.enter="search" />
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
      console.log(this.keyWord)
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
                path:  `/transfer_details/${that.keyWord}/0`,
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
  width: 690px;
  height: 52px;
  position: relative;
  @media screen and (max-width: 900px) {
    width: 100%;
  }
  input {
    width:100%;
    line-height: 52px;
    box-sizing: border-box;
    background: #fff;
    // border-bottom: 2px solid white;
    color:#333333;
    outline: none;
    border-radius: 6px;
    padding-left: 15px;
    font-size: 20px;
    border: 1px solid #735DFF;
    &::placeholder {
      color: #677897;
      font-size: 20px;
      line-height:45px;
    }
    &::-webkit-input-placeholder{
      line-height:45px;
    }
  }

  .search-btn {
    display: block;
    position: absolute;
    right: 0;
    top: 0;
    /*width: 60px;*/
    text-align: center;
    color: black;
    cursor: pointer;
    .iconfont {
      font-size: 24px;
    }
    width: 54px;
    height: 54px;
    background: url(../../assets/img/search_ico.png) no-repeat;
    background-size: 100%;
  }
}
</style>

