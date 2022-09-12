<template>
  <div>
    <el-card style="width: 40%;margin: 10px">
      <el-form ref="form" :model="form" label-width="80px">
        <el-form-item label="username">
          <el-input v-model="form.username" disabled></el-input>
        </el-form-item>
        <el-form-item label="name">
          <el-input v-model="form.nickName"></el-input>
        </el-form-item>
        <el-form-item label="password">
          <el-input v-model="form.password"></el-input>
        </el-form-item>
        <el-form-item label="age" v-show="this.isShow">
          <el-input v-model="form.age"></el-input>
        </el-form-item>
        <el-form-item label="gender" v-show="this.isShow">
          <el-radio v-model="form.sex" label="male">male</el-radio>
          <el-radio v-model="form.sex" label="female">female</el-radio>
          <el-radio v-model="form.sex" label="undefined">undefined</el-radio>
        </el-form-item>
        <el-form-item label="phone number" v-show="this.isShow">
          <el-input v-model="form.tel"></el-input>
        </el-form-item>
        <el-form-item label="home address"  v-show="this.isShow">
          <el-input v-model="form.address"></el-input>
        </el-form-item>
      </el-form>
      <div style="text-align: center">
        <el-button type="primary" @click="update">save</el-button>
      </div>

    </el-card>

  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "Person",

  data(){
    return{
      form:[],
      isShow:true,
    }
  },
  created() {
    let str = sessionStorage.getItem("user") || "{}"
    this.form = JSON.parse(str)
    if(this.form.role === '0'){
      //管理员登录
      this.isShow = false
    }else{
      this.isShow = true
    }
  },
  methods:{
    update(){
      let url = ''
      if(this.form.role === '0'){
        //管理员登录
        url = "/root"
      }else if(this.form.role === '1'){
        //病人登录
        url = "/user/patient"
      }else{
        //专家登录
        url = "/user/specialist"
      }
      request.put(url,this.form).then(res => {
        if(res.code === '0'){
          this.$message({
            type:"success",
            message:"update successful"
          })
          location.reload()
          sessionStorage.setItem("user",JSON.stringify(this.form))
        }else{
          this.$message({
            type:"error",
            message:res.message
          })
        }
      })
    }
  },
}
</script>

<style scoped>

</style>
