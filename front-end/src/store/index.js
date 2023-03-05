import Vue from "vue"
import Vuex from "vuex"

Vue.use(Vuex);

export default new Vuex.Store({
  state:{
    pathName:'',
    token:'',
    user: {
      userName:'',
      password:''
    },
  },
  mutations:{
    //after login successfully
    loginSucess (state, user) {
      //put login info into store.state to share this info
      state.user = user
      //put login info into localStorage，vue will put in into cookie
      window.sessionStorage.setItem('userName',JSON.stringify(user))
    },
    setToken (state, token) {
      state.token = token
      window.localStorage.setItem('token',token)
    },
    // set user after login
    setUser (state, userN) {
      state.user = userN
    },
    removeUser (state) {
      state.user = {
        username: '',
        password: ''
      }
      state.token = ''
      window.sessionStorage.removeItem('userName')
      window.localStorage.removeItem('token')
    },
    //reset state in sessionStore when page flushes
    restate(state){
      if (window.sessionStorage.getItem('userName'))
      state.user = JSON.parse(window.sessionStorage.getItem('userName'))
    },
    // save current menu bar path
    savePath(state,pathName){
      state.pathName = pathName;
    },
  }
})
