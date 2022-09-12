<template>
  <div style="height: 50px; line-height: 50px;border-bottom: 1px solid #ccc;display: flex">
    <div style="width: 200px;padding-left: 30px;font-weight: bold;color: dodgerblue">Blood Vessel Diagnose Cloud Platform</div>
    <div style="flex: 1"></div>
    <div style="width: 100px;">

      <el-dropdown>
        <span class="el-dropdown-link" style="line-height: 50px">
          {{user.nickName}}
          <el-icon class="el-icon--right">
            <arrow-down />
          </el-icon>
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item @click="toPerson">personal info</el-dropdown-item>
            <el-dropdown-item @click="$router.push('/login')">logout</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
import { ArrowDown } from '@element-plus/icons-vue'
export default {
  name: "Head",
  components: {
    ArrowDown,
  },
  data(){
    return{
      user:[]
    }
  },
  created() {
    let str = sessionStorage.getItem("user") || "{}"
    this.user = JSON.parse(str)
  },
  methods:{
    toPerson(){
      this.$router.push("/person")
    }
  },
}
</script>

<style scoped>
.example-showcase .el-dropdown-link {
  cursor: pointer;
  color: var(--el-color-primary);
  display: flex;
  align-items: center;
}

</style>
