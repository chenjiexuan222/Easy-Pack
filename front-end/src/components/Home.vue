<template>
  <div>
    <meta charset="UTF-8">
    <el-container>
      <!--side bar-->
      <el-aside width="200px" style="margin-top:-8px;margin-left:-7px;background-color: #f5f5f5;text-align:left;line-height: 100px">
        <div class="logo" >
          <img class="img" src="http://zjuaim.com/uploadfile/upfiles/2021071914543560f521ab60e22.png">
        </div>
        <el-menu class="menu" :default-active="'/' +this.$route.path.split('/')[1]" active-text-color="#1a5195" router>
          <el-menu-item index="/block" route = "/block">
            <i class="el-icon-coin"></i>
            <span slot="title">Block Library</span>
          </el-menu-item>

          <el-submenu index="/react">
            <template slot="title"><i class="el-icon-edit-outline"></i>New Task</template>
            <el-menu-item-group style="background-color:#f5f5f5;margin-left: -10px">
              <el-menu-item  index="/react/reactionScr"><i class="el-icon-edit"></i>New Screen Task</el-menu-item>
              <el-menu-item  index = "/react/reaction"><i class="el-icon-edit"></i>New reaction Task </el-menu-item>
            </el-menu-item-group>
          </el-submenu>

          <el-menu-item index="/result">
            <i class="el-icon-data-line"></i>
            <span slot="title">Result</span>
          </el-menu-item>

          <el-menu-item index="/camera">
            <i class="el-icon-video-camera"></i>
            <span slot="title">Real Time Camera</span>
          </el-menu-item>

          <el-menu-item index="/reactionlib">
            <i class="el-icon-document"></i>
            <span slot="title">Reaction Lib</span>
          </el-menu-item>

        </el-menu>
      </el-aside>
      <el-container>
        <!--head-->
        <el-header>
          <el-row>
            <el-col :xl="{span:4}" :lg="{span:5}" :md="{span:7}">
              <div style="text-align:left;margin-top: 14px">
                <span style="font-weight: bold;color:#1a5195;font-size: 18px;font-family:Arial">Easy-Pack Platform</span>
              </div>
            </el-col>
            <el-col :xl="{span:1,offset:17}" :lg="{span:1,offset:16}" :md="{span:1,offset:13}">
              <div style="text-align:right;margin-right: 120px;margin-top: 8px">
                <el-avatar icon="el-icon-user-solid" size="small"></el-avatar>
              </div>
            </el-col>
            <el-col :xl="{span:2}" :lg="{span:2}" :md="{span:3}">
              <div style="margin-top: 12px">
                <el-dropdown @command="handleCommand">
                <span class="el-dropdown-link">
                  {{this.$store.state.user.userName}}<i class="el-icon-arrow-down el-icon--right"></i>
                </span>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item command="switch" >Switch</el-dropdown-item>
                    <el-dropdown-item command="out" divided>Log out</el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </div>
            </el-col>
          </el-row>
          <div style="margin-top: -15px">
            <el-divider></el-divider>
          </div>
        </el-header>

        <el-main>
<!--          <router-view/>-->
          <transition name="fade" mode="out-in">
            <keep-alive>
              <router-view v-if="$route.meta.keepAlive"></router-view>
            </keep-alive>
          </transition>
          <router-view v-if="!$route.meta.keepAlive"></router-view>

        </el-main>

      </el-container>
    </el-container>
  </div>
</template>

<script>
export default {
  name: "Home",
  data() {
    return {
      path:"/",
    }
  },
    methods: {
      switchAccount(){
        //return to login page
        this.$router.replace('/login');
      },
      logout(){
        //remove user in cookies
        this.$store.commit("removeUser");
        //返回登录界面
        this.$router.replace('/login');
      },
      handleCommand(command){
        if ("switch" == command) this.switchAccount();
        else if ("out" == command) this.logout();
      }
    },
  created() {
    // this.$store.state.user.userName = window.sessionStorage.getItem('userName')
    this.$store.commit('restate')
  }
}
</script>

<style scoped>
  .logo {
    color: #ffffff;
    height: 60px;
    padding: 8px;
    margin-left: 8px;
    margin-top: 0px;
  }
  .img{
    width: 160px;
  }
  .menu{
    height: 100vh;
    font-weight: bold;
    background-color: #f5f5f5;
  }
  .el-dropdown-link{
    cursor: pointer;
    color: #245c95;
    font-weight: bold;
  }
</style>
