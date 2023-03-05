// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import './rem'
import store from './store/index'

import axios from 'axios'

import ElementUI from 'element-ui'
import locale from 'element-ui/lib/locale/lang/en'
import 'element-ui/lib/theme-chalk/index.css'

Vue.prototype.$http = axios
Vue.use(ElementUI, { locale })
Vue.config.productionTip = false
axios.defaults.baseURL = "http://192.168.21.136:8080/click"
//请求拦截器 在请求头中加token
axios.interceptors.request.use(
  config => {
    if(localStorage.getItem('token')){
      config.headers.token = localStorage.getItem('token');
    }
    return config;
  },
  error => {
    return Promise.reject(error);
  }
)

router.beforeEach((to,from,next)=>{
  //verify if the router jumped should be verified
  if (to.path === '/login') {
    next()
    //pass if router jumped need not verify
  } else {
    //get token from cookies,return null if userName is not exist
    let userToken = window.localStorage.getItem('token')
    //if login info is already stored in cookies,just pass
    if(userToken) {
      next()
      //redirect to login page if userName is null
    } else {
      next({
        path: '/login',
      })
    }
  }
})

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App),
  components: { App },
  template: '<App/>'
})
