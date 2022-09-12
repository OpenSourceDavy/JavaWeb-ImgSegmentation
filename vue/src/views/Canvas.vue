<template>
  <div>
    <el-row>
      <el-col :span="12">
        <!--canvas居中：display设置为block-->
        <canvas ref="cont" style="border: 2px solid #aaaaaa;display: block;"
                @click="handleEvent"
        >
        </canvas>
      </el-col>
      <el-col :span="2" style="margin-left: 20px">
        <span>专家意见：</span>
      </el-col>
      <el-col :span="9" style="text-align:left">
        <el-input
            type="textarea"
            :rows="26"
            placeholder="请输入内容"
            v-model="this.form.result">
        </el-input>
        <el-button type="primary" style="margin-left: 430px;text-align:right;" @click="$router.push('/Img')">返回</el-button>
        <el-button type="primary" style="margin-left: 20px;text-align:right;" @click="submit">提交</el-button>
      </el-col>
    </el-row>
  </div>
  <div style="margin: 10px 0px;display: flex">
    <el-button type="primary" style="margin-left: 5px;text-align:right;" @click="cancel" v-show="!isPolygon">撤销</el-button>
    <el-button type="primary" style="margin-left: 5px;text-align:right;" @click="resetMap">重置</el-button>
    <el-button type="primary" style="margin-left: 5px;text-align:right;" @click="download">下载图片</el-button>
    <span style="margin-left: 5px">选择绘制图案：</span>
    <el-select v-model="tool" clearable @change="changeTool"
               size="small">
      <el-option
          v-for="item in tools"
          :key="item.value"
          :label="item.label"
          :value="item.value">
      </el-option>
    </el-select>
    <div>
      <span style="margin-left: 5px;text-align:right;" v-show="isLine">最新两条直线的夹角为：{{angle}}</span>
      <el-button type="primary" style="margin-left: 5px;text-align:right;" @click="drawPolygon"
                 v-show="isPolygon"
      >画多边形</el-button>
      <span style="margin-left: 5px;text-align:right;" v-show="isArea">最新图形的面积为：{{area}}</span>
    </div>

  </div>
  <div v-show="isLine">
    <span style="margin-left: 10px;text-align:right;">请输入三个Cobb角判断脊椎侧凸类型：</span>
    <span style="margin-left: 10px;text-align:right;">上胸弯：</span>
    <el-input v-model="PT" style="width: 80px;"></el-input>
    <span style="margin-left: 10px;text-align:right;">主胸弯：</span>
    <el-input v-model="MT" style="width: 80px;"></el-input>
    <span style="margin-left: 10px;text-align:right;">腰胸弯/腰弯：</span>
    <el-input v-model="TL" style="width: 80px;"></el-input>
    <el-button type="primary" style="margin-left: 10px;text-align:right;" @click="judge">判断</el-button>
  </div>

  <div>
    <div v-show="isArea">
      <span style="margin-left: 10px">是否填充所画区域：</span>
      <el-select v-model="fill" clearable @change="changeFill"
                 size="small">
        <el-option
            v-for="item in fills"
            :key="item.value"
            :label="item.label"
            :value="item.value">
        </el-option>
      </el-select>
      <span style="margin-left: 10px" v-show="isColor">请选择填充颜色：</span>
      <el-color-picker v-model="color" v-show="isColor"></el-color-picker>
    </div>
  </div>

  <div v-show="isPoint">
    <span style="margin-left: 10px;text-align:right;">请顺序打点描出脊椎图案</span>
    <el-button type="primary" style="margin-left: 10px;text-align:right;" @click="outline">描出脊椎</el-button>
  </div>


</template>

<script>
import request from "@/utils/request";

let history = []
function Point (x, y, type) {
  this.x = x
  this.y = y
  this.type = type // 左击 1  右击 3
}
function windowToCanvas (e, mycanvas) {
  // 返回元素的大小以及位置
  let rect = mycanvas.getBoundingClientRect()
  // rect 的宽度会加上 canvas 的 border 会影响精度
  return new Point(e.offsetX - rect.left * (mycanvas.width / rect.width),
      e.offsetY - rect.top * (mycanvas.height / rect.height), e.which)
}
function showLastHistory (ctx, history) {
  ctx.putImageData(history[history.length -1].data, 0, 0)
}
function addHistoy (history, ctx, mycanvas) {
  history.push({
    data: ctx.getImageData(0, 0, mycanvas.width, mycanvas.height)
  })
}
export default {
  name: "Canvas",
  data(){
    return{
      ctx:null,
      mycanvas:null,
      textarea:'',
      tool:'',
      tempPos:[],
      posArray:[],
      form: {},
      Cwidth:(window.screen.width)*0.5,
      Cheight:(window.screen.height)*0.7,
      tools:[
        {value:"3", label:"直线"},
        {value:"1", label:"矩形"},
        {value:"2", label:"圆形"},
        {value:"4", label:"任意多边形"},
        {value:"5", label:"点"},
      ],
      point:[],
      angle:'',
      area:'',
      isPolygon:false,
      isLine:false,
      isArea:false,
      isPoint:false,
      fill:'',
      fills:[
        {value:"0", label:"不填充"},
        {value:"1", label:"填充"},
      ],
      color:'',
      isColor:false,
      PT:'',
      MT:'',
      TL:'',
      imgUrl:'',
    }
  },

  mounted() {
    //this.();
    let str = sessionStorage.getItem("img") || "{}"
    this.form = JSON.parse(str)

    this.mycanvas = this.$refs.cont
    this.ctx = this.mycanvas.getContext('2d');
    this.mycanvas.width = (window.screen.width)*0.5
    this.mycanvas.height = (window.screen.height)*0.7
    this.drawImg()
    this.drawer();

  },

  methods:{

    download(){
      let img = this.mycanvas.toDataURL("image/png");//转换城图片的流
      this.imgUrl = img
      let a = document.createElement('a');    // 创建一个a节点插入的document
      let event = new MouseEvent('click') ;      // 模拟鼠标click点击事件
      a.download = 'labelPicture'          // 设置a节点的download属性值
      a.href = this.imgUrl;                   // 将图片的src赋值给a节点的href
      a.dispatchEvent(event);    // 触发鼠标点击事件
    },

    submit(){
      this.form.state = "已评阅"
      request.put("/img",this.form).then(res => {
        if(res.code === '0'){
          this.$message({
            type:"success",
            message:"提交成功"
          })
        }else{
          this.$message({
            type:"error",
            message:res.message
          })
        }
        sessionStorage.removeItem("img")
        this.$router.push("/img")
      })
    },

    judge(){
      if(this.PT == "" || this.MT == "" || this.TL == ""){
        this.$alert('请输入三个Cobb角后再点击按钮', '提示', {
          confirmButtonText: '确定',
          type:"warning"
        });
        return false
      }else{
        if(this.PT < 25 && this.MT >=25 && this.TL <25){
          this.$alert('脊椎侧凸类型：主胸弯', '判断结果', {
            confirmButtonText: '确定',
            type:"info"
          });
        }else if(this.PT >= 25 && this.MT >= 25 && this.TL < 25 && this.MT > this.PT){
          this.$alert('脊椎侧凸类型：双胸弯', '判断结果', {
            confirmButtonText: '确定',
            type:"info"
          });
        }else if(this.PT < 25 && this.MT >= 25 && this.TL >= 25 && this.MT >= this.TL){
          this.$alert('脊椎侧凸类型：双主弯', '判断结果', {
            confirmButtonText: '确定',
            type:"info"
          });
        }else if(this.PT >= 25 && this.MT >= 25 && this.TL >= 25 && this.MT > this.PT && this.TL > this.PT){
          this.$alert('脊椎侧凸类型：三主弯', '判断结果', {
            confirmButtonText: '确定',
            type:"info"
          });
        }else if(this.PT < 25 && this.MT < 25 && this.TL >= 25){
          this.$alert('脊椎侧凸类型：胸腰弯/腰弯', '判断结果', {
            confirmButtonText: '确定',
            type:"info"
          });
        }else if(this.PT < 25 && this.MT >= 25 && this.TL >= 25 && this.TL > this.MT){
          this.$alert('脊椎侧凸类型：胸腰弯/腰弯-主胸弯', '判断结果', {
            confirmButtonText: '确定',
            type:"info"
          });
        }else{
          this.$alert('没有该类型的侧凸', '提示', {
            confirmButtonText: '确定',
            type:"warning"
          });
        }
      }
    },

    handleEvent(e){
      this.drawer();
    },

    changeFill(){
      if(this.fill == 0){
        this.isColor = false
      }else{
        this.isColor = true
      }
    },

    changeTool(t){
      this.point = [];
      this.angle = '';
      this.drawer();
      if(t == '1'){
        //画矩形
        this.isArea = true
        this.isLine = false
        this.isPolygon = false
        this.fill = ''
        this.isColor =false
        this.isPoint = false
      }else if(t == '2'){
        //画圆
        this.isArea = true
        this.isPolygon = false
        this.isLine = false
        this.fill = ''
        this.isColor =false
        this.isPoint = false
      }else if(t == '3'){
        //画直线
        this.isArea = false
        this.isPolygon = false
        this.isLine = true
        this.isColor = false
        this.isPoint = false
      }else if(t == '4'){
        //画多边形
        this.isArea = true
        this.isPolygon = true
        this.isLine = false
        this.fill = ''
        this.isColor =false
        this.isPoint = false
      }else if(t == '5'){
        //画点
        this.isArea = false
        this.isPolygon = false
        this.isLine = false
        this.isColor = false
        this.isPoint = true
      }
    },

    drawImg(){
      let img = new Image();
      img.src = this.form.img;
      img.crossOrigin = '';
      img.onload = () => {
        this.ctx.drawImage(img, this.Cwidth/3, 0, 300, this.Cheight);
      }
    },

    getTan(p1,p2,p3,p4){
      let k1 = (p2.y - p1.y) / (p2.x - p1.x)
      let k2 = (p4.y - p3.y) / (p4.x - p3.x)

      let tan = (k1-k2) / (1+k1*k2)
      return Math.abs(tan);
    },

    myAngle(tan){
      //Math.atan,传入tan值，计算弧度，角度等于弧度乘以180/π
      let result = Math.atan(tan) * 180 / Math.PI;
      return result;
    },

    getPolygonArea(){
      let area = 0
      //多边形面积公式 s = (x1y2-x2y1)/2 + (x2y3-x3y2)/2 +......+ (xny1-x1yn)/2
      //(a[i]*b[(i+1)%n]-a[(i+1)%n]*b[i])
      let l = this.point.length
      for(let i = 0; i < l; i++){
        area += this.point[i].x * this.point[(i+1)%l].y - this.point[(i+1)%l].x * this.point[i].y
      }
      this.area = Math.abs(area)/2
      this.area = this.area/100 //数值太大，统一缩小100倍
    },

    //  绘制脊椎轮廓
    outline(){
      let myPoints = this.point
      let length = myPoints.length

      this.ctx.save()
      this.ctx.globalAlpha = 1;
      this.ctx.fillStyle = "#100f0f";
      this.ctx.rect(this.Cwidth/3, 0, this.Cwidth/2.5, this.Cheight)
      this.ctx.fill();

      this.ctx.beginPath()
      this.ctx.lineWidth = '2' // 画笔粗细
      this.ctx.strokeStyle = '#100f0f'// 画笔颜色
      this.ctx.moveTo(myPoints[0].x,myPoints[0].y)
      for(let i = 0; i<length  ; i++){
        this.ctx.lineTo(myPoints[i].x,myPoints[i].y)
      }
      this.ctx.lineTo(myPoints[0].x,myPoints[0].y)
      this.ctx.stroke()
      this.ctx.closePath()
      this.ctx.globalAlpha = 1;
      this.ctx.fillStyle = "#f3f2ec";
      this.ctx.fill();
      this.ctx.restore()


      //画完清空
      this.point = []
      myPoints = []
    },


    // 绘制矩形
    drawerRect (ctx, left, top, w, h) {
      ctx.strokeStyle = '#f00000'// 画笔颜色
      if(this.fill == 1){
        //是否填充图形区域
        ctx.fillStyle=this.color;//设置填充颜色
        ctx.fill();//开始填充
      }
      ctx.lineWidth = '2' // 画笔粗细
      ctx.save()
      ctx.beginPath()
      ctx.rect(left, top, w, h)
      ctx.stroke()
      ctx.restore()
      return {
        data: [left, top, w, h]
      }
    },
    // 绘制圆
    drawerCircle (ctx, x, y, r) {
      ctx.strokeStyle = '#f00000'// 画笔颜色
      if(this.fill == 1){
        //是否填充图形区域
        ctx.fillStyle=this.color;//设置填充颜色
        ctx.fill();//开始填充
      }
      ctx.lineWidth = '2' // 画笔粗细
      ctx.beginPath()// 开始路径
      ctx.arc(x, y, r, 0, Math.PI * 2, true)// 参数依次为圆心坐标x,y，半径，开始结束角，绘制方向顺时针
      ctx.stroke()
      ctx.restore()
      return {
        data: [x, y, r]
      }
    },
    // 绘制直线
    drawerLine (ctx, x, y, z, n) {
      ctx.save()
      ctx.fillStyle = '#f00000'
      ctx.lineWidth = '2' // 画笔粗细
      ctx.strokeStyle = '#f00000'// 画笔颜色
      ctx.beginPath()
      ctx.moveTo(x, y)
      ctx.lineTo(z, n)
      ctx.stroke()
      ctx.restore()
      return {
        data: [x, y, z, n]
      }
    },

    // 绘制点
    drawerPoint (ctx, x, y) {
      ctx.save()
      ctx.fillStyle = '#f00000'
      ctx.strokeStyle = '#f00000'// 画笔颜色
      ctx.beginPath()
      ctx.arc(x, y, 3, 0, Math.PI * 2, true)
      ctx.closePath()
      ctx.fill()
      ctx.restore()
      this.posArray.push({data: [x, y]})
    },

    //  多边形个顶点连线，画多边形
    drawPolygon(){
      //排序，决定个点之间的连线顺序，尽量避免各边相交
      let ps = this.sortPoints(this.point);
    },

    //对多边形各个顶点排序
    sortPoints(points){
      if(points == ""){
        return false
      }

      //让点按x轴升序排序
      let tempPoints = points.sort((p1,p2) =>{
        return  p1.x-p2.x
      })

      //找到x轴的左右极点
      let firstP = tempPoints[0]  //x轴最左端
      let lastP = tempPoints[tempPoints.length-1]  //x轴最右端

      //如果多个x轴的左右极点，则按y轴排出左右极点
      let smallXP = tempPoints.filter(ball => ball.x === firstP.x)
      let bigXP = tempPoints.filter(ball => ball.x === lastP.x)
      // 处理左右极点有多个的情况,y轴降序
      if (smallXP.length > 1) {
        smallXP.sort((ballA, ballB) => {
          return ballB.y - ballA.y
        })
      }
      if (bigXP.length > 1) {
        bigXP.sort((ballA, ballB) => {
          return ballB.y - ballA.y
        })
      }
      firstP = smallXP[0]
      lastP = bigXP[0]

      // 获得两个极点连线的角度
      let splitLineAngle = Math.atan2(lastP.y - firstP.y, lastP.x - firstP.x);
      let upperP = []   //上点  小于splitLineAngle的都是上点  X轴降序连接
      let lowerP = []   //下点  大于splitLineAngle的都是下点  X轴升序连接
      //计算弧度  分为上下点数组
      tempPoints.forEach(p => {
        if (p === firstP || p === lastP) {
          return false
        }
        let angle = Math.atan2(p.y - firstP.y, p.x - firstP.x);
        if (angle > splitLineAngle) {
          lowerP.push(p)
        } else {
          upperP.push(p)
        }
      })

      //升序
      lowerP = lowerP.sort((p1, p2) => {
        if (p1.x !== p2.x) {  // 处理X轴相同情况的排序
          return p1.x - p2.x
        }
        return p2.y - p1.y
      })
      //降序
      upperP = upperP.sort((p1, p2) => {
        if (p1.x !== p2.x) {  // 处理X轴相同情况的排序
          return p2.x - p1.x
        }
        return p2.y - p1.x
      })

      // 逆时针连接所有的点
      let myPoints = [firstP].concat(lowerP, [lastP], upperP)
      //调用画直线的方法，进行连线
      let length = myPoints.length
      this.ctx.save()
      this.ctx.beginPath()
      this.ctx.lineWidth = '2' // 画笔粗细
      this.ctx.strokeStyle = '#f00000'// 画笔颜色
      this.ctx.moveTo(myPoints[0].x,myPoints[0].y)
      for(let i = 0; i<length  ; i++){
        this.ctx.lineTo(myPoints[i].x,myPoints[i].y)
      }
      this.ctx.lineTo(myPoints[0].x,myPoints[0].y)
      this.ctx.stroke()
      if(this.fill == 1){
        //是否填充图形区域
        this.ctx.fillStyle=this.color;//设置填充颜色
        this.ctx.fill();//开始填充
      }
      this.ctx.restore()


      this.getPolygonArea() //计算多边形体积

      //画完清空
      this.point = []
      myPoints = []
    },




    // 绘制的方法及事件，根据当前选择的工具进行不同的方法绘制图形
    drawer () {
      let mycanvas = this.$refs.cont
      let ctx = mycanvas.getContext('2d')
      let that = this
      if (that.tool === '1') {
        //画矩形
        mycanvas.onclick = null
        mycanvas.onmousedown = function (e) {
          that.tempPos = []
          e.preventDefault()
          let mousedown = windowToCanvas(e, mycanvas)
          mycanvas.onmousemove = function (e) {
            e.preventDefault()
            showLastHistory(ctx, history) // 每次绘制先清除上一次
            let point = windowToCanvas(e, mycanvas)
            let w = Math.abs(point.x - mousedown.x)
            let h = Math.abs(point.y - mousedown.y)
            let left = point.x > mousedown.x ? mousedown.x : point.x
            let top = point.y > mousedown.y ? mousedown.y : point.y
            let pos = that.drawerRect(ctx, left, top, w, h)
            that.area = (w*h)/100 //计算矩形面积 数值太大，统一缩小100倍
            that.tempPos.push(pos)
          }
          mycanvas.onmouseup = function (e) {
            e.preventDefault()
            //addHistoy(history, ctx, mycanvas) // 保存上一次数据
            mycanvas.onmousemove = null
            that.posArray.push(that.tempPos[that.tempPos.length - 1])
          }
        }
        addHistoy(history, ctx, mycanvas) // 添加一张默认的数据
      } else if (that.tool === '2') {
        // 画圆
        mycanvas.onmousedown = null
        mycanvas.onmousemove = null
        mycanvas.onmouseup = null
        mycanvas.onclick = null
        mycanvas.onmousedown = function (e) {
          that.tempPos = []
          e.preventDefault()
          let mousedown = windowToCanvas(e, mycanvas)
          mycanvas.onmousemove = function (e) {
            e.preventDefault()
            showLastHistory(ctx, history) // 每次绘制先清除上一次
            let point = windowToCanvas(e, mycanvas)
            //圆上两点，其连线过圆心，长度是直径
            let rx = (point.x - mousedown.x) / 2
            let ry = (point.y - mousedown.y) / 2
            let r = Math.sqrt(rx * rx + ry * ry)  //a平方 + b平方 = c平方
            //求圆面积
            that.area = (Math.PI * r * r) /100  //数值太大，统一缩小100倍
            that.area = Math.round(that.area * 1000) / 1000 //取小数点后三位
            let pos = that.drawerCircle(ctx, rx + mousedown.x, ry + mousedown.y, r)
            that.tempPos.push(pos)
          }
          mycanvas.onmouseup = function (e) {
            e.preventDefault()
            //addHistoy(history, ctx, mycanvas) // 保存上一次数据
            mycanvas.onmousemove = null
            that.posArray.push(that.tempPos[that.tempPos.length - 1])
          }
        }
        addHistoy(history, ctx, mycanvas) // 添加一张默认的数据
      } else if (that.tool === '3') {
        //画直线
        mycanvas.onmousedown = null
        mycanvas.onmousemove = null
        mycanvas.onmouseup = null
        mycanvas.onclick = null
        mycanvas.onmousedown = function (e) {
          that.tempPos = []
          e.preventDefault()
          let mousedown = windowToCanvas(e, mycanvas)
          mycanvas.onmousemove = function (e) {
            e.preventDefault()
            showLastHistory(ctx, history) // 每次绘制先清除上一次
            let point = windowToCanvas(e, mycanvas)
            let pos = that.drawerLine(ctx, mousedown.x, mousedown.y, point.x, point.y)
            that.tempPos.push(pos)
          }
          mycanvas.onmouseup = function (e) {
            let mouseup = windowToCanvas(e, mycanvas)
            //存直线的起始点和终点，有四个点则确定有两条直线，再求其的夹角
            that.point.push(mouseup)
            that.point.push(mousedown)
            this.point = that.point
            let l = that.point.length
            if(l >= 4){
              //计算tan的值
              let tan = that.getTan(that.point[l-4],that.point[l-3],that.point[l-2],that.point[l-1])
              //根据tan值求角度
              let angle = that.myAngle(tan)
              that.angle = Math.round(angle*1000)/1000  //取小数点后三位
              that.angle = that.angle+"°" //加上角度
              //that.point = []
            }
            e.preventDefault()
            //addHistoy(history, ctx, mycanvas) // 保存上一次数据
            mycanvas.onmousemove = null
            that.posArray.push(that.tempPos[that.tempPos.length - 1])
          }
        }
        addHistoy(history, ctx, mycanvas) // 添加一张默认的数据
      } else if (that.tool === '4') {
        //画多边形
        mycanvas.onmousedown = null
        mycanvas.onmousemove = null
        mycanvas.onmouseup = null
        mycanvas.onclick = function (e) {
          let point = windowToCanvas(e, mycanvas)
          that.point.push(point)
          that.drawerPoint(ctx, point.x, point.y)
        }
        addHistoy(history, ctx, mycanvas) // 添加一张默认的数据
      } else if (that.tool === '5') {
        //画点
        mycanvas.onmousedown = null
        mycanvas.onmousemove = null
        mycanvas.onmouseup = null
        mycanvas.onclick = function (event) {
          let point = windowToCanvas(event, mycanvas)
          that.point.push(point)
          that.drawerPoint(ctx, point.x, point.y)
        }
        addHistoy(history, ctx, mycanvas) // 添加一张默认的数据
      }
    },

    // 重置
    resetMap () {
      // 标注的信息都放在这个数组中
      this.posArray = []
      history = [history[0]]
      let mycanvas = this.$refs.cont
      let ctx = mycanvas.getContext('2d')
      ctx.clearRect(0, 0, mycanvas.width, mycanvas.height)
      addHistoy(history, ctx, mycanvas)
      this.drawImg();
      this.tool = '';
      this.point = []
      this.isPolygon = false
      this.isLine = false
      this.isArea = false
      this.isColor = false
      this.isPoint = false
    },
    // 取消上一步操作
    cancel () {
      if (history.length > 1) {
        this.posArray.pop()
        history.pop()
        if(this.tool == 3){
          this.point.pop()
          this.point.pop()
        }
        let mycanvas = this.$refs.cont
        let ctx = mycanvas.getContext('2d')
        showLastHistory(ctx, history)
      }else{
        this.drawImg();
      }
    },



  }

}
</script>

<style scoped>

</style>
