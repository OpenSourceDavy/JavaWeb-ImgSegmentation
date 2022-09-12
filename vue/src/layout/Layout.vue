<template>
  <div>
    <!--    头部-->
    <Head />

    <!--      主体-->
    <div style="display: flex">
      <!--      侧边栏-->
      <Aside />
      <!--      内容区域-->
      <router-view style="flex:1" />
    </div>
  </div>
</template>

import Head from "@/components/Head";
import Aside from "@/components/Aside";
<script>
import Head from "@/components/Head";
import Aside from "@/components/Aside";

export default {
  name: "Layout",
  components: {
    Head,
    Aside,
  }
}
</script>

<style scoped>

</style>
