<template>
  <el-dialog ref="dialog" v-model="dialogVisible" width="40%" >
    <el-container>
      <el-header class="el-menu__title" style="margin-bottom:2px;text-align: center" >
        <span>welcome</span>
      </el-header>
      <el-main >
        <div>
          <el-row v-show="isShow">
            <el-col style="text-align:center;margin-top:10px">
              <span>usertype：</span>
              <el-select v-model="user.role" clearable @change="isShow"
                         size="small">
                <el-option
                    v-for="item in SystemRole"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                </el-option>
              </el-select>
            </el-col>
          </el-row>
          <el-row>
            <el-col style="text-align:center;margin-top:10px">
              <span>&#12288;username：</span>
              <el-input v-model="user.username" size="small" style="width: 200px;"></el-input>
            </el-col>
          </el-row>
          <el-row>
            <el-col style="text-align:center;margin-top:10px">
              <span>pass&#12288;&#12288;word：</span>
              <el-input v-model="user.password" size="small" style="width: 200px;" show-password></el-input>
            </el-col>
          </el-row>
          <el-row>
            <el-col style="text-align:center;margin-top:10px">
              <span>confirm password：</span>
              <el-input v-model="user.confirm" size="small" style="width: 200px;" show-password></el-input>
            </el-col>
          </el-row>
          <el-row>
            <el-col style="text-align:center;margin-top:10px">
              <span>user&#12288;&#12288;name：</span>
              <el-input v-model="user.nickName" size="small" style="width: 200px;"></el-input>
            </el-col>
          </el-row>
          <el-row v-show="isShowData">
            <el-col style="text-align:center;margin-top:10px">
              <span>gender&#12288;type：</span>
              <el-select v-model="user.sex" clearable
                         size="small">
                <el-option
                    v-for="item in gender"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                </el-option>
              </el-select>
            </el-col>
          </el-row>
          <el-row v-show="isShowData">
            <el-col style="text-align:center;margin-top:10px">
              <span>your&#12288;&#12288;age：</span>
              <el-input v-model="user.age" size="small" style="width: 200px;"></el-input>
            </el-col>
          </el-row>
          <el-row v-show="isShowData">
            <el-col style="text-align:center;margin-top:10px">
              <span>phone number：</span>
              <el-input v-model="user.tel" size="small" style="width: 200px;"></el-input>
            </el-col>
          </el-row>
          <el-row v-show="isShowData">
            <el-col style="text-align:center;margin-top:10px">
              <span>home address：</span>
              <el-input v-model="user.address" size="small" style="width: 200px;"></el-input>
            </el-col>
          </el-row>
        </div>
      </el-main>
      <el-footer style="text-align: center;">
        <el-button size="mini" type="primary" @click="register()">confirm</el-button>
        <el-button size="mini" type="primary" @click="modifyBillDialogCancel()">cancel</el-button>
      </el-footer>
    </el-container>
  </el-dialog>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "Register",
  data(){
    return{
      dialogVisible:false,
      user:{},
      isShowData:true,
      isShow:true,
      SystemRole:[
        {value:"1", label:"patient"},
        {value:"2", label:"specialist"},
      ],
      gender:[
        {value:"male",label:"male"},
        {value:"female",label:"female"},
      ],
    }
  },

  methods:{
    init(e) {
      this.setDialogVisible(true);
      if(e=='1'){
        this.user.role = '1'
        this.isShow = false
      }else if(e == '2'){
        this.user.role = '2'
        this.isShow = false
      }else {
        this.isShow = true
      }
    },
    setDialogVisible(val){
      this.dialogVisible = val
      this.user = {}
    },
    modifyBillDialogCancel(){
      this.setDialogVisible(false);
      this.user = {};
    },
    isShow(val){
      if(val === '0'){
        this.isShowData = false;
        this.user= {}
        this.user.role = val
      }else if(val === '1'){
        this.isShowData = true;
        this.user= {}
        this.user.role = val
      }else {
        this.isShowData = true;
        this.user= {}
        this.user.role = val
      }
    },
    commitData() {
      debugger
      let chinese = /^([\u4e00-\u9fa5]{2,5})$/;
      //密码强度正则，最少8位，包括至少1个大写字母，1个小写字母，1个数字，1个特殊字符
      let pPattern = /^.*(?=.{8,})(?=.*\d)(?=.*[A-Z])(?=.*[a-z]).*$/;
      if (this.user.role == "" || this.user.role == undefined
          || this.user.role == null) {
        this.$alert('please select usertype', 'remind', {
          confirmButtonText: 'confirm',
          type:"warning"
        });
        return false;
      } else if (this.user.username == "" || this.user.username == undefined
          || this.user.username == null) {
        this.$alert('username is invalid', 'remind', {
          confirmButtonText: 'confirm',
          type:"warning"
        });
        return false;
      } else if (this.user.username.length <6 || this.user.username.length >20) {
        this.$alert('length of username should between 6 an 20', 'remind', {
          confirmButtonText: 'confirm',
          type:"warning"
        });
        return false;
      } else if (this.user.password == "" || this.user.password == undefined
          || this.user.password == null) {
        this.$alert('password cannot be none', 'remind', {
          confirmButtonText: 'confirm',
          type:"warning"
        });
        return false;
      } else if (!pPattern.test(this.user.password)) {
        this.$alert('password should be at least 8 digit with at least one upper, one lower and one symbol', 'remind', {
          confirmButtonText: 'confirm',
          type:"warning"
        });
        return false;
      } else if (this.user.confirm == "" || this.user.confirm == undefined
          || this.user.confirm == null) {
        this.$alert('confirm password cannot be none', 'remind', {
          confirmButtonText: 'confirm',
          type:"warning"
        });
        return false;
      } else if (this.user.password != this.user.confirm) {
        this.$alert('password inconsistent', 'remind', {
          confirmButtonText: 'confirm',
          type:"warning"
        });
        return false;
      } else if (this.user.nickName == "" || this.user.nickName == undefined
          || this.user.nickName == null) {
        this.$alert('name cannot be none', 'remind', {
          confirmButtonText: 'confirm',
          type:"warning"
        });
        return false;
      } else if (!chinese.test(this.user.nickName)) {
        this.$alert('length of name should be 2-5', 'remind', {
          confirmButtonText: 'confirm',
          type:"warning"
        });
        return false;
      }

      if(this.user.role === '1' || this.user.role === '2' ){
        let reg = /^1[3456789]\d{9}$/;
        if (this.user.sex == "" || this.user.sex == undefined
            || this.user.sex == null) {
          this.$alert('please select gender', 'remind', {
            confirmButtonText: 'confirm',
            type:"warning"
          });
          return false;
        } else if (this.user.age == "" || this.user.age == undefined
            || this.user.age == null) {
          this.$alert('please input age', 'remind', {
            confirmButtonText: 'confirm',
            type:"warning"
          });
          return false;
        } else if (this.user.age < 0 || this.user.age > 120) {
          this.$alert('age should be within 120', 'remind', {
            confirmButtonText: 'confirm',
            type:"warning"
          });
          return false;
        } else if (this.user.tel == "" || this.user.tel == undefined
            || this.user.tel == null) {
          this.$alert('please input phone number', 'remind', {
            confirmButtonText: 'confirm',
            type:"warning"
          });
          return false;
        } else if (!reg.test(this.user.tel)) {
          this.$alert('please input right number', 'remind', {
            confirmButtonText: 'confirm',
            type:"warning"
          });
          return false;
        } else if (this.user.address == "" || this.user.address == undefined
            || this.user.address == null) {
          this.$alert('please input home address', 'remind', {
            confirmButtonText: 'confirm',
            type:"warning"
          });
          return false;
        }
      }
    },
    register(){
      let f = this.commitData();
      if(f == false){
        return
      }
      let url = ""
      if(this.user.role === '0'){
        //管理员登录
        url = "/root/register"
      }else if(this.user.role === '1'){
        //病人登录
        url = "/user/patient/register"
      }else{
        //专家登录
        url = "/user/specialist/register"
      }
      request.post(url,this.user).then(res => {
        if (res.code === '0') {
          this.$message({
            type: "success",
            message: "register successful"
          })
          this.$emit('load');
          this.$emit('reload');
          this.setDialogVisible(false);
        } else {
          this.$message({
            type: "error",
            message: res.msg
          })
        }
      })
    }
  }
}
</script>

<style scoped>
.el-menu__title{
  font-family: "Microsoft YaHei";
  font-size: 1.5rem;
  color: #0682C5;
  letter-spacing: 0;
  line-height: 33px;
}
.el-badge-class {
  color: #12A0EB;
  margin-top: 10px;
  margin-left: -30px;
}
.tips-class {
  font-family: PingFangSC-Medium;
  color: #ff0000;
  letter-spacing: 0;
  margin-top: -1.5%;
  font-size: 10px;
}
</style>
