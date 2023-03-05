<template>
  <div class="background">
    <div class="pic">
<!--      <el-row>
        <el-col :xl={span:7} :lg={span:7} :md={span:7} class="pic1">
          <el-image fit="fit" :src="require('@/assets/pic/platform.png')"></el-image>
        </el-col>
      </el-row>-->

      <el-row>
        <el-col :xl={span:12,offset:10} :lg={span:13,offset:10} :md={span:18,offset:10}>
          <img src="../../assets/pic/platform.png" :height="this.bannerHeight + 'px'" :width="this.bannerWidth + 'px'">
        </el-col>
      </el-row>
    </div>

      <el-form class="login-container" :model="form" ref="form" :rules="rules" label-position="right" label-width="65px">
      <div class="logo">
        <img class="img" src="http://zjuaim.com/uploadfile/upfiles/2021071914543560f521ab60e22.png">
      </div>
        <el-row>
          <el-col span = 21 >
            <el-form-item prop="userName" style="margin-top: 120px">
              <el-input class="input" type="text" v-model="form.userName" placeholder="Username" prefix-icon="el-icon-user" clearable></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col span=21>
            <el-form-item prop="password">
              <el-input type="password" v-model="form.password" placeholder="Password"  prefix-icon="el-icon-lock" show-password clearable></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col span=8 offset=2>
            <el-form-item style="width: 100%;margin-top: 5px">
              <el-button type="primary" style="background: #505458;border: none" v-on:click="login('form')">login</el-button>
            </el-form-item>
          </el-col>
          <el-col span=8>
            <el-form-item style="width: 100%;margin-left: -20px;margin-top: 5px">
              <el-button type="primary" style="background: #b0b4ab;border: none" v-on:click="reset">reset</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
  </div>
</template>

<script>
export default {
  name: "login",
  data(){
    return{
      form:{
        userName:'',
        password:'',
      },
      bannerHeight:'',
      screenHeight:'',
      screenWidth:'',
      bannerWidth:'',
      rules:{
        userName: [
          { required:true, message: 'Please enter the Username', trigger: 'blur' },
        ],
        password: [
          {required: true, message: 'Please enter the Password', trigger: 'blur'},
          {min: 3, max: 11, message: 'the length of password is limited to 3-11 characters', trigger: 'blur'}
        ],
      },
    }
  },
  methods:{
    //login verify
    login(formName){
      this.$refs[formName].validate((valid) => {
        if (valid) {

          this.$http.post("/user/login",this.form).then(res=>{
            //console.log(res.data);
            if (200 == res.data.code){
              this.$message({
                showClose: true,
                message: 'Login successfully',
                type: 'success'
              });
              //put user info into sessionStorage
              this.$store.commit('loginSucess',this.form)
              this.$store.commit('setToken',res.data.token)

              this.$router.replace('/block');
            }else {
              this.$message({
                showClose: true,
                message: 'You have no permission',
                type: 'error'
              });
            }
          })
            .catch((error)=>{
              console.log(error.response.status)
            })
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    //reset login form
    reset(){
      this.$refs["form"].resetFields();
    }
  },

  mounted() {
    // initial height when first loading
    this.screenWidth = window.innerWidth
    this.screenHeight = window.innerHeight
    this.bannerHeight = 960 / 1080 * this.screenHeight
    this.bannerWidth = 1080/1920 * this.screenWidth
    // window size changing
    window.onresize = () => {
      this.screenWidth = window.innerWidth
      this.screenHeight = window.innerHeight
      this.bannerHeight = 640 / 1080 * this.screenHeight
      this.bannerWidth = 960/1920 * this.screenWidth
    }
  }
}
</script>

<style scoped>
.input{
  background-color: transparent;
  /*border: 1px solid #1296db;*/
}
.background{
  width: 100%;
  height: 100%;
  /*background-image: url("../../assets/pic/protein.png");*/
  /*background: rgb(236, 234, 234);*/
  background: rgb(255, 255, 255);
  background-size: 100% 100%;
  overflow: auto;
  background-repeat: no-repeat;
  position: fixed;
  top: 0;
  left: 0;
}
.login-container {
  border-radius: 15px;
  background-clip: padding-box;
  /*margin: 90px auto;*/
  width: 380px;
  /*width: 28%;*/
  height: 320px;
  padding: 35px 35px 15px 35px;
  /*background: #fff;*/
  background: rgba(252, 249, 249, 0.5);
  /*border: 1px solid #eaeaea;*/
  box-shadow: 0 0 25px #cac6c6;
  left: 23%;
  top: 49%;
  position: absolute;
  transform: translate(-50%,-50%);
}
.logo{
  /*height: 20px;*/
  /*width: 30px;*/
  left: 19%;
  top: 10%;
  position: absolute;
}
.pic1{
  position: absolute;
}
.pic{
  top: 8%;
  position:relative;
}
.pic2{
  width: 100%;
  height: 100%;
}
.login_title {
  margin: 0px auto 40px auto;
  text-align: center;
  color: #505458;
}
.back{
  background-color: #951a4b;
}
</style>
