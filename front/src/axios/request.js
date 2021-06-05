import axios from 'axios';
import Vue from 'vue';
// import qs from 'qs';
const store = window.sessionStorage;
if(process.env.NODE_ENV==='development') {
  //开发环境
  // axios.defaults.baseURL = 'https://third.whitecoin.info/whitecoin-explorer';
  // axios.defaults.baseURL = 'https://explorer.whitecoin.info/api'; //'http://explorer.hx.cash/hx-browser';
  axios.defaults.baseURL = 'https://explorer.whitecoin.info/api';
} else {
  //真实环境
  // axios.defaults.baseURL = 'http://192.168.110.11:9007/api';
  axios.defaults.baseURL = '/api';
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
