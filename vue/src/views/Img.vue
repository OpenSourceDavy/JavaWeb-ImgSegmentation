<template>
  <div style="padding: 10px">

    <!--    搜索-->
    <div style="margin: 10px 0px;">
      <el-autocomplete
          v-model="search"
          :fetch-suggestions="querySearch"
          placeholder="please input status"
          clearable style="width:20%;margin-left: 950px"
      >
        <template #default="{ item }">
          <div class="value">{{ item.value }}</div>
        </template>
      </el-autocomplete>
      <el-button type="primary" style="margin-left: 5px;" @click="load">search</el-button>
      <el-button type="primary" style="margin-left: 5px;" @click="upload" v-show="isShow">upload</el-button>
    </div>
    <el-table :data="tableData" border style="width: 100%" stripe>
      <el-table-column prop="id" label="X-PIC ID" sortable/>
      <el-table-column prop="PName" label="patient name" />
      <el-table-column prop="SName" label="specialist name" />
      <el-table-column label="X-PIC">
        <template #default="scope">
          <el-image
              style="width: 100px; height: 100px"
              :src="scope.row.img"
              :preview-src-list="[scope.row.img]"
              fit="cover"
              preview-teleported="true"
          />
        </template>
      </el-table-column>
      <el-table-column label="diagnose result">
        <template #default="scope">
          <el-button size="mini" @click="check(scope.row)">view</el-button>
          <el-button size="mini" @click="downLoad(scope.row)">download</el-button>
        </template>
      </el-table-column>
      <el-table-column prop="state" label="diagnose status" sortable/>
      <el-table-column prop="createTime" label="upload time" sortable/>
      <el-table-column label="modify">
        <template #default="scope">
          <el-button size="mini" @click="handleEidt(scope.row)">edit</el-button>
          <el-popconfirm title="Are you sure to delete?" @confirm="handleDelete(scope.row.id)">
            <template #reference>
              <el-button size="mini" type="danger" v-show="!isS">delete</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
      <el-table-column label="AI diagnose">
        <template #default="scope">
          <el-button size="mini" @click="toPython(scope.row)">diagnose</el-button>
          <el-button size="mini" @click="openAiUrl(scope.row)">view</el-button>
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
          title="upload or update"
          width="30%"
      >
        <el-form :model="form" label-width="120px">
          <el-form-item label="X-PIC ID" v-show="isNotShow">
            <el-input v-model="form.id" style="width: 160px" disabled></el-input>
          </el-form-item>
          <el-form-item label="Patient ID" v-show="isNotShow">
            <el-input v-model="form.pid" style="width: 160px" disabled></el-input>
          </el-form-item>
          <el-form-item label="please select specialist">
            <el-select v-model="form.SName" clearable @change="changeS"
                       size="small">
              <el-option
                  v-for="item in specialist"
                  :key="item.id"
                  :label="item.nickName"
                  :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="diagnose result" v-show="isNotShow">
            <el-input v-model="form.result" style="width: 160px" disabled></el-input>
          </el-form-item>
          <el-form-item label="diagnose status" v-show="isNotShow">
            <el-radio v-model="form.state" label="diagnosed">diagnosed</el-radio>
            <el-radio v-model="form.state" label="undiagnosed">undiagnosed</el-radio>
          </el-form-item>
          <el-form-item label="X-PIC" style="width: 300px">
            <el-upload
                ref="upload"
                :action = filesUploadUrl
                :on-success="fileUpload"
                style="width: 300px"
            >
              <el-button type="primary">upload</el-button>
            </el-upload>
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">cancel</el-button>
            <el-button type="primary" @click="save">confirm</el-button>
      </span>
        </template>
      </el-dialog>

      <el-dialog
          v-model="isShowResult"
          title="diagnose result"
          width="50%"
      >
        <el-input
            type="textarea"
            :rows="20"
            placeholder="please comment"
            v-model="result" disabled>
        </el-input>
      </el-dialog>

      <el-dialog
          v-model="isShowAi"
          title="AI diagnose result"
          width="50%"
      >
        <el-image
            style="width: 100px; height: 100px"
            :src="aiUrl"
            :preview-src-list="[aiUrl]"
            fit="cover"
            preview-teleported="true"
        />
      </el-dialog>


    </div>
  </div>
</template>

<script>

import request from "@/utils/request";


export default {
  name: "Img",
  components: {
  },
  data(){
    return{
      querySearch:[
        {value:'diagnosed'},
        {value:'undiagnosed'},
      ],
      search:'',
      currentPage:1,
      pageSize:10,
      total:1,
      tableData:[],
      form: {},
      dialogVisible:false,
      isShowResult:false,
      isShowAi:false,
      aiUrl:"",
      user:{},
      isShow:false,
      isNotShow:true,
      specialist:[],
      patient:[],
      result:'',
      isS:false,
      isP:false,
      isA:false,
      filesUploadUrl:"http://" + window.server.filesUploadUrl + ":9090/files/upload",
      time:'',
    }
  },
  created() {
    let str = sessionStorage.getItem("user") || "{}"
    this.user = JSON.parse(str)
    let specialist = sessionStorage.getItem("specialist") || "{}"
    this.specialist = JSON.parse(specialist)
    let patient = sessionStorage.getItem("patient") || "{}"
    this.patient = JSON.parse(patient)
    if(this.user.role === '0'){
      this.isA = true
      this.isS = false
      this.isP = false
    }else if(this.user.role === '1'){
      this.isShow = true
      this.isNotShow = false

      this.isA = false
      this.isS = false
      this.isP = true
    }else{
      this.isA = false
      this.isS = true
      this.isP = false
    }
    this.load()
  },

  methods:{


    toPython(row){
      if (this.time) {
        clearTimeout(this.time); //清除
      }
      this.time = setTimeout(()=>{
        let str = row.img.split('/')
        request.put("/AI",str[str.length-1]).then(res => {
          if(res.code === '0'){
            this.$message({
              type:"success",
              message:"update successful"
            })
            row.aiUrl = res.data
            console.log(res)
          }else{
            this.$message({
              type:"error",
              message:res.message
            })
          }
          this.dialogVisible = false
        })
      },300)
    },

    openAiUrl(row){
      if(!row.aiUrl){
        this.$message({
          type:"warning",
          message:"please diagnose first"
        })
        return false
      }
      let str = row.aiUrl.split("/")
      let url = "http://localhost:9876/api/files/AI/" + str[str.length-1]

      // let features = "height=700,width=1200,top=100,left=200,resizable=yes,scrollings=yes,status=no";
      // window.open(url,"AI识别结果",features);
      this.aiUrl = url
      this.isShowAi = true
    },


    load(){
      this.tableData = []
      let sid = ''
      let pid = ''
      if(this.user.role === '0'){
        //admin
        sid = ''
        pid = ''
      }else if(this.user.role === '1'){
        //patient
        sid = ''
        pid = this.user.id
      }else if(this.user.role === '2'){
        //specialist
        sid = this.user.id
        pid = ''
      }
      request.get("/img",{
        params:{
          pageNum:this.currentPage,
          pageSize:this.pageSize,
          search:this.search,
          sid:sid,
          pid:pid
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
        this.tableData.forEach(td=>{
          this.specialist.forEach(s=>{
            if(td.sid == s.id){
              td.SName = s.nickName
            }
          })
          this.patient.forEach(p=>{
            if(td.pid == p.id){
              td.PName = p.nickName
            }
          })
        })
      })
    },

    fileUpload(res){
      debugger
      this.form.img = res.data
    },

    save(){
      if(this.form.id){ //uodate
        request.put("/img",this.form).then(res => {
          if(res.code === '0'){
            this.$message({
              type:"success",
              message:"update successful"
            })
          }else{
            this.$message({
              type:"error",
              message:res.message
            })
          }
          this.load() //refresg
          this.dialogVisible = false
        })
      }else{ //新增
        request.post("/img",this.form).then(res => {
          if(res.code === '0'){
            this.$message({
              type:"success",
              message:"add successful"
            })
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
    upload(){

      if(this.tableData!=""){
        this.form = this.tableData[0];
        this.form.id = ''
      }
      this.form.pid = this.user.id
      this.form.state = 'undiagnosed'
      this.form.result = ''
      this.dialogVisible = true
      this.$refs['upload'].clearFiles() //清除历史文件列表
    },

    changeS(s){
      this.form.sid = s
    },

    check(row){
      this.isShowResult = true
      this.result = row.result
    },

    downLoad(row){
      if(row.state == "undiagnosed"){
        this.$alert('not diagnosed, please wait', 'remind', {
          confirmButtonText: 'confirm',
          type:"warning"
        });
        return false
      }
      if(row.result == "diagnosed"){
        this.$alert('not commented, please wait', 'remind', {
          confirmButtonText: 'confirm',
          type:"warning"
        });
        return false
      }
      this.form = JSON.parse(JSON.stringify(row))
      let flag = ''
      request.post("/files/integration",this.form).then(res => {
        flag = res.data
        //let url = "http://175.24.204.121:9090/files/"+flag  //cloud server
        let url = "http://localhost:9876/api/files/"+flag
        window.open(url)
      })
    },

    handleEidt(row){
      this.form = JSON.parse(JSON.stringify(row))
      if(this.user.role === '0'){
        this.$nextTick(() =>{
          this.$refs['upload'].clearFiles() //清除历史文件列表
        })
        //管理员
        this.dialogVisible = true
      }else if(this.user.role === '1'){
        this.$nextTick(() =>{
          this.$refs['upload'].clearFiles() //清除历史文件列表
        })
        //病人
        this.dialogVisible = true
      }else if(this.user.role === '2'){
        //专家
        sessionStorage.setItem("img",JSON.stringify(row))
        this.$router.push("/canvas")
      }
    },
    handleDelete(id){
      request.delete("/img/"+id).then(res =>{
        if(res.code === '0'){
          this.$message({
            type:"success",
            message:"delete successful"
          })
        }else{
          this.$message({
            type:"error",
            message:res.message
          })
        }
        this.load() //刷新表格
      })
    },





  },

}
</script>

<style scoped>

</style>
