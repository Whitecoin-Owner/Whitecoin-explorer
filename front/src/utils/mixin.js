var mixin = {
  methods: {
    _mixin_address_jump: function (address) {
      let that = this;
      if (address.startsWith('CLN')) {
        this.$axios.post('/getContractStatis', {contractId: address}).then(function (res) {
          if (res.data.data) {
            that.$router.push({ path: `/contractOverview/${address}` });
          } else {
            that.$message({
              message: that.$t('alert.notSearch'),
              type: 'warning'
            });
          }
        });
      } else {
        this.$axios.post('/addrStatis',{address:address}).then(function (res) {
          if (res.data.data) {
            that.$router.push({ path: '/address', query: { address: address }});
          } else {
            that.$message({
              message: that.$t('alert.notSearch'),
              type: 'warning'
            });
          }
        });
      }
    }
  }
}
export default mixin;
