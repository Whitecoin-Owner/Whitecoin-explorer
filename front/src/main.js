// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue';
import App from './App';
import router from './router';
import Element from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import './assets/less/resetElement.less';
import './assets/font/iconfont.css';
import './assets/less/common.less';
import i18n from './i18n/i18n';
import VueTimeago from 'vue-timeago'
Vue.config.productionTip = false;

//引入axios
require('./axios/request');

Vue.use(Element, {
  size: 'medium',// set element-ui default size,
  // enLocale //默认英文
})

Vue.use(VueTimeago, {
  name: 'Timeago', // Component name, `Timeago` by default
  locale: undefined, // Default locale
  locales: {
    'cn': require('date-fns/locale/zh_cn'),
    'en': require('date-fns/locale/en'),
    'ja': require('date-fns/locale/ja')
  }
})


new Vue({
  el: '#app',
  router,
  i18n,
  components: { App },
  template: '<App/>'
});
