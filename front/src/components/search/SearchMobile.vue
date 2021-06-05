<template>
  <div class="wrap_ser">
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
        this.$notify({
          message: this.$t("alert.noSpace"),
          type: "warning",
          duration:2000,
        });
      } else if (this.keyWord === "") {
        this.$notify({
          message: this.$t("alert.enterContent"),
          type: "warning",
          duration:2000,
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
              that.$notify({
                message: that.$t("alert.notSearch"),
                type: "warning",
                duration:2000,
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
              that.$notify({
                message: that.$t("alert.notSearch"),
                type: "warning",
                duration:2000,
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
              that.$notify({
                message: that.$t("alert.notSearch"),
                type: "warning",
                duration:2000,
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
              that.$notify({
                message: that.$t("alert.notSearch"),
                type: "warning",
              });
            }
          });
      }
    }
  }
};
</script>

<style lang="less" scoped>
.wrap_ser {
  width: 100%;
  height: 94rem;
  position: relative;
  display: flex;
  align-items: center;
  padding-top: 0;
  input {
    width:100%;
    box-sizing: border-box;
    background: #fff;
    // border-bottom: 2px solid white;
    color:#333333;
    outline: none;
    border-radius: 8rem;
    font-size: 26rem;
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
      font-size: 24rem;
    }
    width: 96rem;
    height: 96rem;
    background: url(../../assets/img/search_ico.png) no-repeat;
    background-size: 100%;
  }
}
/deep/.el-input.el-input--medium{
  height: 96rem;
  line-height: 96rem;
  border-radius:15rem;
  box-sizing: border-box;
  input{
    &::placeholder {
      color: #677897;
      font-size: 20rem !important;
    }
    &::-webkit-input-placeholder{
      font-size: 20rem !important;
    }
  }
}
/deep/.el-input--medium .el-input__inner{
  font-size: 26rem;
  line-height: 40rem;
  height: 40rem;
  padding-left: 8rem;
}
</style>