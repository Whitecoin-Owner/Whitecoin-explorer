import axios from 'axios';
import Vue from 'vue';
// import qs from 'qs';
const store = window.sessionStorage;
if(process.env.NODE_ENV==='development') {
  //开发环境
  // axios.defaults.baseURL = 'http://192.168.1.196:9007';
  axios.defaults.baseURL = 'http://192.168.1.121:9007/';
} else {
  //真实环境
  // axios.defaults.baseURL = 'http://explorer.whitecoin.cash/whitecoin-browser';
  axios.defaults.baseURL = 'http://127.0.0.1:9007';
}

// http request 请求拦截器，有token值则配置上token值
// axios.interceptors.request.use(
//   config => {
//     config.data = qs.stringify(config.data);
//     // config.headers = {
//     //   "Content-Type":"application/x-www-form-urlencoded",
//     // }
//       let token = store.getItem('token');
//       if(token){
//         config.headers.sessionId = token;
//       }
//     return config;
//   },
//   err => {
//     return Promise.reject(err);
//   });
// http response 拦截器
// axios.interceptors.response.use(function (response) {
//   if (response.data.retCode === 400) {
//     location.href = '/';
//   } else {
//     return response;
//   }
// }, function (error) {
//   // Do something with response error
//   return Promise.reject(error);
// });
Vue.prototype.$axios = axios;
