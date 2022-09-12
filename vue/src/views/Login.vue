<template>
  <div style="width: 100%; height: 100vh; background-color: darkgreen; overflow:hidden">
    <div style="width: 400px; margin: 150px auto">
      <div style="color: #cccccc; font-size: 30px; text-align: center; padding:30px 0 ">Blood Vessel Diagnose Cloud Platform</div>
      <el-form ref="formLogin" :model="form" size="normal" :rules="rulesLogin">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" placeholder="password" show-password></el-input>
        </el-form-item>
        <el-form-item prop="role">
          <el-radio v-model="form.role" label="0" style="width: 31%">
            <span style="color:#cccccc;">
              admin
            </span>
          </el-radio>
          <el-radio v-model="form.role" label="1" style="width: 31%">
            <span style="color:#cccccc;">
              patient
            </span>
          </el-radio>
          <el-radio v-model="form.role" label="2">
            <span style="color:#cccccc;">
              specialist
            </span>
          </el-radio>
        </el-form-item>
        <el-form-item>
          <el-button style = "width:100%" type="primary" @click="login">login</el-button>
        </el-form-item>
        <el-form-item>
          <el-button style = "width:100%" type="primary" @click="register">register</el-button>
        </el-form-item>
      </el-form>
    </div>


  <register ref="register" ></register>
  </div>
</template>

<script>
import request from "@/utils/request";
import Register from "./Register.vue";

export default {
  name: "Login",
  components: {
    Register,
  },
  data(){
    return {
      specialist:[],
      patient:[],
      form:{},
      rulesLogin: {
        username: [
          { required: true, message: 'please input username', trigger: 'blur'},
        ],
        password: [
          { required: true, message: 'please input password', trigger: 'blur'},
        ],
        role: [
          { required: true, message: 'please select usertype', trigger: 'blur'},
        ],
      },
    }
  },
  created() {
    sessionStorage.removeItem("user")
    this.get()
  },
  methods:{
    login(){
      this.$refs['formLogin'].validate((valid) => {
        if (valid) {
          let url = ""
          let path = ""
          if(this.form.role === '0'){
            //管理员登录
            url = "/root/login"
            path = "/patient"
          }else if(this.form.role === '1'){
            //病人登录
            url = "/user/patient/login"
            path = "/img"
          }else{
            //专家登录
            url = "/user/specialist/login"
            path = "/img"
          }
          request.post(url,this.form).then(res => {
            if (res.code === '0') {
              this.$message({
                type: "success",
                message: "login success"
              })

              sessionStorage.setItem("user",JSON.stringify(res.data))

              this.$router.push(path)  //登陆成功后进行页面跳转，跳转到主页
            } else {
              this.$message({
                type: "error",
                message: res.msg
              })
            }
          })
        }
      })
    },

    get(){
      // request.get("/specialist/get",{
      // }).then(res => {
      //   this.specialist = res
      //   sessionStorage.setItem("specialist",JSON.stringify(res))
      // })
      // request.get("/patient/get",{
      // }).then(res => {
      //   this.patient = res
      //   sessionStorage.setItem("patient",JSON.stringify(res))
      // })
      request.get("/user/get",{
        params:{
          role:1
        }
      }).then(res => {
        this.patient = res
        console.log(res)
        sessionStorage.setItem("patient",JSON.stringify(res))
      })
      request.get("/user/get",{
        params:{
          role:2
        }
      }).then(res => {
        this.specialist = res
        console.log(res)
        sessionStorage.setItem("specialist",JSON.stringify(res))
      })
    },


    register(){
      this.$refs.register.init();
      this.form = {}
    },

  }
}
</script>

<style scoped>
</style>
