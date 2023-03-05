import Vue from 'vue'
import Router from 'vue-router'

import Home from '../components/Home'
import Block from "../components/block/Block";
import Camera from "../components/camera/Camera";
import Reaction from "../components/reaction/Reaction";
import ReactionScr from "../components/reaction/ReactionScr";
import ReactionLib from "../components/reactionLib/ReactionLib";
import Result from "../components/result/Result";
import ReactionPar from "../components/reaction/ReactionPar";
import Login from "../components/login/login";

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      redirect: '/login',
    },
    {
      path: '/login',
      component: Login,
      meta: {
        keepAlive: true
      },
    },
    {
      path: '/home',
      component: Home,
      meta:{
        keepAlive: true,
        requireAuth: true
      },
      children:[
        {
          path: '/block',
          name:'Block',
          meta:{
            keepAlive: true,
            requireAuth: true
          },
          component: Block
        },
        {
          path:'/react',
          name:'ReactionPar',
          meta:{
            keepAlive: true,
            requireAuth: true
          },
          component:ReactionPar,//需指定父组件，且在其中通过<router-view></router-view>进行子路由
          children:[
            {
              path:'reaction',
              name:'Reaction',
              meta:{
                keepAlive: true,
                requireAuth: true
              },
              component:Reaction
            },
            {
              path: 'reactionScr',
              name:'ReactionScr',
              meta:{
                keepAlive: true,
                requireAuth: true
              },
              component: ReactionScr
            },
          ]
        },
        {
          path: '/camera',
          name:'Camera',
          meta:{
            keepAlive: true,
            requireAuth: true
          },
          component: Camera
        },
        {
          path: '/reactionlib',
          name:'ReactionLib',
          meta:{
            keepAlive: true,
            requireAuth: true
          },
          component: ReactionLib
        },
        {
          path: '/result',
          name:'Result',
          meta:{
            keepAlive: true,
            requireAuth: true
          },
          component: Result
        },
      ]
    },
  ]
})
