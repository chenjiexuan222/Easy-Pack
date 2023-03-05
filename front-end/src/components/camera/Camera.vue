<template>
  <div>
    <el-row :gutter="40">
        <el-col  class="formBorder" :xl="11" :lg="11" :md="11" style="height: 50vh;margin-left: 30px">
          <span style="font-weight: bold;font-family: 宋体" class="el-icon-video-camera">    Robot Perspective</span>
          <!--camera-->
          <div class="box">
            <video id="videoCamera" class="canvas" :width="videoWidth" :height="videoHeight" autoPlay style="margin-top: 5px;margin-left: -3px"></video>
            <canvas id="canvasCamera" class="canvas" :width="videoWidth" :height="videoHeight"></canvas>
          </div>
        </el-col>

      <el-col  class="formBorder" :xl="{span:11,offset:1}" :lg="{span:11,offset:1}" :md="{span:11,offset:1}" style="height: 50vh">
        <span style="font-weight: bold;font-family: 宋体" class="el-icon-video-camera">  Global Perspective</span>
        <!--camera-->
        <div class="box">
          <video id="videoCamera1" class="canvas" :width="videoWidth" :height="videoHeight" autoPlay style="margin-top: 5px;margin-left: -3px"></video>
          <canvas id="canvasCamera1" class="canvas" :width="videoWidth" :height="videoHeight"></canvas>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: "Camera",
  data() {
    return {
      thisVideo: null,
      thisContext: null,
      thisCancas: null,
      video1:null,
      context1:null,
      cancas:null,
      videoWidth: 460,
      videoHeight: 265,
      cameraList:[]
    }
  },
  methods: {
    showVideo () {
      this.$nextTick(function () {
        var _this = this;
        this.thisCancas = document.getElementById('canvasCamera');
        this.thisContext = this.thisCancas.getContext('2d');
        this.thisVideo = document.getElementById('videoCamera');

        this.cancas = document.getElementById('canvasCamera1');
        this.context1 = this.cancas.getContext('2d');
        this.video1 = document.getElementById('videoCamera1');

        // 旧版本浏览器可能根本不支持mediaDevices，我们首先设置一个空对象
        //mediaDevices对象可提供对相机和麦克风等媒体输入设备的连接访问，也包括屏幕共享
        if (navigator.mediaDevices === undefined) {
          navigator.mediaDevices = {}
        }
        // 一些浏览器实现了部分mediaDevices，我们不能只分配一个对象,使用getUserMedia，因为它会覆盖现有的属性。
        // 这里，如果缺少getUserMedia属性，就添加它。
        if (navigator.mediaDevices.getUserMedia === undefined) {
          navigator.mediaDevices.getUserMedia = function (constraints) {
            // 首先获取现存的getUserMedia(如果存在)
            let getUserMedia = navigator.webkitGetUserMedia || navigator.mozGetUserMedia || navigator.getUserMedia;
            // 有些浏览器不支持，会返回错误信息
            // 保持接口一致
            if (!getUserMedia) {
              return Promise.reject(new Error('getUserMedia is not implemented in this browser'))
            }
            // 否则，使用Promise将调用包装到旧的navigator.getUserMedia
            return new Promise(function (resolve, reject) {
              getUserMedia.call(navigator, constraints, resolve, reject)
            })
          }
        }

        var enumeratorPromise = navigator.mediaDevices.enumerateDevices()
        console.log(enumeratorPromise)
        // navigator.mediaDevices.enumerateDevices()
        var constraints = { audio: false, video: { deviceId:"69948f5767f85a7048c30031e0de7e442168085d509e3cab4c7f1d20343e7379",width: this.videoWidth, height: this.videoHeight, transform: 'scaleX(-1)' } };
        var constraints1 = { audio: false, video: { deviceId:"5e52f67e38c895b98690b2938f1978aa97e0ba1faa92114d606cbccdf2bf1d4b",width: this.videoWidth, height: this.videoHeight, transform: 'scaleX(-1)' } };
        var constraints2 = { audio: false, video: { deviceId:"f7cec6910cbaa1ba9c85060f89ece75bb84e0df3b2d3735d19d1660390009ea0",width: this.videoWidth, height: this.videoHeight, transform: 'scaleX(-1)' } };
        navigator.mediaDevices.getUserMedia(constraints).then(function (stream) {
          // 旧的浏览器可能没有srcObject
          if ('srcObject' in _this.thisVideo) {
            _this.thisVideo.srcObject = stream
          } else {
            // 避免在新的浏览器中使用它，因为它正在被弃用。
            _this.thisVideo.src = window.URL.createObjectURL(stream)
          }
          _this.thisVideo.onloadedmetadata = function (e) {
            _this.thisVideo.play()
          }
        }).catch(err => {
          console.log(err)
          this.$alert(
            "机械臂摄像头暂时无法使用",
          );
        })
        navigator.mediaDevices.getUserMedia(constraints1).then(function (stream1) {
          if ('srcObject' in _this.video1) {
            _this.video1.srcObject = stream1
          } else {
            // 避免在新的浏览器中使用它，因为它正在被弃用。
            _this.video1.src = window.URL.createObjectURL(stream1)
          }
          _this.video1.onloadedmetadata = function (e) {
            _this.video1.play()
          }
        }).catch(err => {
          console.log(err)
          this.$alert(
            "平台摄像头暂时无法使用",
          );
        })
      });
    },
  },
  mounted() {
    this.showVideo()
  }
}
</script>

<style scoped>
.el-row {
  margin-bottom: 20px;
}
.formBorder{
  margin-top: 12px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  padding-top: 10px;
  padding-left: 20px;
}
</style>
