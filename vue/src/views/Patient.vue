<template>
  <div style="padding: 10px">

    <!--    搜索-->
    <div style="margin: 10px 0px;">
      <el-input v-model="search" placeholder="please input name" clearable style="width:20%;margin-left: 950px"></el-input>
      <el-button type="primary" style="margin-left: 5px;" @click="load">search exist</el-button>
      <el-button type="primary" style="margin-left: 5px;" @click="add">add new</el-button>
    </div>
    <el-table :data="tableData" border style="width: 100%" stripe>
      <el-table-column prop="username" label="uesrname" />
      <el-table-column prop="nickName" label="name" />
      <el-table-column prop="password" label="password" />
      <el-table-column prop="age" label="age" />
      <el-table-column prop="sex" label="gender" />
      <el-table-column prop="tel" label="phone number" />
      <el-table-column prop="address" label="home adress" />
      <el-table-column label="edit">
        <template #default="scope">
          <el-button size="mini" @click="handleEidt(scope.row)">edit</el-button>
          <el-popconfirm title="confirm delete?" @confirm="handleDelete(scope.row.id)">
            <template #reference>
              <el-button size="mini" type="danger">delete</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <div style="margin: 10px 0px;float: right">
      <el-pagination
          v-model:currentPage="currentPage"
          :page-size="pageSize"
          :page-sizes="[5, 10, 20]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
      />

      <el-dialog
          v-model="dialogVisible"
          title="edit patient info"
          width="30%"
      >
        <el-form :model="form" label-width="120px">
          <el-form-item label="username">
            <el-input v-model="form.username" style="width: 160px " disabled></el-input>
          </el-form-item>
          <el-form-item label="name">
            <el-input v-model="form.nickName" style="width: 160px"></el-input>
          </el-form-item>
          <el-form-item label="password">
            <el-input v-model="form.password" style="width: 160px"></el-input>
          </el-form-item>
          <el-form-item label="age">
            <el-input v-model="form.age" style="width: 160px"></el-input>
          </el-form-item>
          <el-form-item label="phone number">
            <el-input v-model="form.tel" style="width: 160px"></el-input>
          </el-form-item>
          <el-form-item label="gender">
            <el-radio v-model="form.sex" label="male">male</el-radio>
            <el-radio v-model="form.sex" label="female">female</el-radio>
            <el-radio v-model="form.sex" label="undefined">undefined</el-radio>
          </el-form-item>
          <el-form-item label="home address">
            <el-input type="textarea" v-model="form.address" style="width: 200px"></el-input>
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">cancel</el-button>
            <el-button type="primary" @click="save">confirm</el-button>
      </span>
        </template>
      </el-dialog>


    </div>
    <register ref="register" @load="load" @reload="reload" ></register>
  </div>
</template>


<script>

import request from "@/utils/request";
import Register from "./Register.vue";

export default {
  name: "Patient",
  components: {
    Register,
  },
  data(){
    return{
      search:'',
      currentPage:1,
      pageSize:10,
      total:1,
      tableData:[],
      form:[],
      dialogVisible:false,
    }
  },
  created() {
    this.load()
  },

  methods:{

    load(){
      request.get("/user",{
        params:{
          pageNum:this.currentPage,
          pageSize:this.pageSize,
          search:this.search,
          role:1
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },

    save(){
      if(this.form.id){ //更新
        request.put("/user/patient",this.form).then(res => {
          if(res.code === '0'){
            this.$message({
              type:"success",
              message:"update successful"
            })
            this.reload()
          }else{
            this.$message({
              type:"error",
              message:res.message
            })
          }
          this.load() //刷新表格
          this.dialogVisible = false
        })
      }
    },

    handleSizeChange(val){ //改变每页个数
      this.pageSize = val
      this.load()
    },
    handleCurrentChange(val){  //改变当前页码
      this.currentPage = val
      this.load()
    },
    handleEidt(row){
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible = true
    },
    handleDelete(id){
      request.delete("/user/",{
        params:{
          id:id,
          role:1
        }
      }).then(res =>{
        if(res.code === '0'){
          this.$message({
            type:"success",
            message:"delete successful"
          })
          this.reload()
        }else{
          this.$message({
            type:"error",
            message:res.message
          })
        }
        this.load() //刷新表格
      })
    },

    reload(){
      sessionStorage.removeItem("patient")
      request.get("/user/get",{
        params:{
          role:2
        }
      }).then(res => {
        this.specialist = res
        console.log(res)
        sessionStorage.setItem("patient",JSON.stringify(res))
      })
    },

    add(){
      this.$refs.register.init(1);
      this.form = {}
    },





  },

}
</script>

<style scoped>

</style>
