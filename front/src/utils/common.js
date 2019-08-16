var utils = {
  dateFormatter: function (row, column, cellValue, index) {
    return this.formatDate(cellValue);
  },
  formatNumber: function(value) {
    return (value < 10 ? '0' : '') + value;
  },
  /**
   * 获取一周前 一天前 之类的时间
   * @param n
   * @param timeUnit d:天  M:月  y:年
   * @returns {string}
   */
  initDefaultDate: function(n, timeUnit) {
    var curr_date = new Date();
    if (timeUnit === 'd') {
      curr_date.setDate(curr_date.getDate() + n);
    } else if (timeUnit === 'M') {
      curr_date.setMonth(curr_date.getMonth() + n);
    } else if (timeUnit === 'y') {
      curr_date.setFullYear(curr_date.getFullYear() + n);
    }
    var strYear = curr_date.getFullYear();
    var strMonth = curr_date.getMonth() + 1;
    var strDay = curr_date.getDate();
    var strHours = curr_date.getHours();
    var strMinutes = curr_date.getMinutes();
    var strSecond = curr_date.getSeconds();

    var datastr = strYear + '-' + this.formatNumber(strMonth) + '-' + this.formatNumber(strDay);
      // + ' ' + this.formatNumber(strHours) + ':'
      // + this.formatNumber(strMinutes) + ':' + this.formatNumber(strSecond);
    return datastr;
  },
  formatDate: function (trx_time) {
    // let oldTime = (new Date(trx_time)).getTime();
    let newTime = this.format(new Date(trx_time * 1000), "yyyy-MM-dd hh:mm:ss");
    return newTime;
  },
  format: function (date, fmt) {
    var o = {
      "M+": date.getMonth() + 1,                 //月份
      "d+": date.getDate(),                    //日
      "h+": date.getHours(),                   //小时
      "m+": date.getMinutes(),                 //分
      "s+": date.getSeconds(),                 //秒
      "q+": Math.floor((date.getMonth() + 3) / 3), //季度
      "S": date.getMilliseconds()             //毫秒
    };
    if (/(y+)/.test(fmt)) {
      fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    for (var k in o) {
      if (new RegExp("(" + k + ")").test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
      }
    }
    return fmt;
  }
}


export default utils;
