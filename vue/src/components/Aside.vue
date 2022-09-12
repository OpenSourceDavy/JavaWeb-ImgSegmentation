<template>
  <div>
    <el-menu
        style="width: 200px;min-height: calc(100vh - 50px)"
        :default-active="path"
        router
        class="el-menu-vertical-demo"
    >
      <el-sub-menu index="1" v-show="isShow">
        <template #title>user manege</template>
        <el-menu-item index="/patient">patient manage</el-menu-item>
        <el-menu-item index="/specialist">specialist manage</el-menu-item>
      </el-sub-menu>
      <el-menu-item index="/img">
        <span>Vessel pic manage</span>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<script>
import {
  Document,
  Menu as IconMenu,
  Location,
  Setting,
} from '@element-plus/icons-vue'
export default {
  name: "Aside",
  components: {
    Document,
    IconMenu,
    Location,
    Setting,
  },
  data(){
    return{
      path:this.$route.path,
      isShow:false,
      user:[]
    }
  },
  created() {
    console.log(this.$route.path)
    let str = sessionStorage.getItem("user") || "{}"
    this.user = JSON.parse(str)
    if(this.user.role === '0'){
      this.isShow = true
    }
  },
}
</script>

<style scoped>

</style>
