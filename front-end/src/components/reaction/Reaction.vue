<template>
  <div>
    <!--status line-->
    <div style="height: 46px;background-color: #faf9f9;margin-top: -20px">
      <el-row>
        <el-button size="medium" type="success" icon="el-icon-plus" style="margin-left: 10px;margin-top: 6px" @click="addBatch">New batch</el-button>
        <el-button size="medium" type="success" icon="el-icon-s-promotion" style="margin-left: 10px;margin-top: 6px" @click="submitTask">Submit task</el-button>
      </el-row>
    </div>

    <el-form :model ="batch">
      <!--form-->
      <div class="formBorder" v-for="(it,batchIndex) in this.batch.form" >
          <el-row>
            <el-col :xl="{span:1}" :lg="{span:1}" :md="{span:1}">
              <el-button icon="el-icon-edit-outline" type="info" size="mini" disabled>batch{{batchIndex + 1}}</el-button>
            </el-col>
            <el-col :xl="{span:2,offset:21}" :lg="{span:2,offset:20}" :md="{span:2,offset:19}">
              <el-button icon="el-icon-delete" type="danger" size="mini"  @click="delBatch(batchIndex)" v-if="batchIndex >= 1">Delete batch</el-button>
            </el-col>
          </el-row>
          <!--first line-->
          <el-row gutter=10 style="margin-top: 10px">

            <el-col :xl="{span:2}" :lg="{span:2}" :md="{span:3}">
              <el-tag effect="plain"  style="margin-top: 3px">params</el-tag>
            </el-col>
            <!--Batchsize-->
            <el-col :xl="{span:4}" :lg="{span:5}" :md="{span:4}">
              <el-form-item :prop="'form.'+ batchIndex +'.batchSize'" :rules="{required: true, message: 'Please enter Batchsize', trigger: 'blur'}">
                <el-input class="input" v-model="it.batchSize" placeholder="Batchsize"  clearable></el-input>
              </el-form-item>
            </el-col>
            <!--Temperature-->
            <el-col :xl="{span:4}" :lg="{span:4}" :md="{span:4}">
              <el-form-item :prop="'form.'+ batchIndex +'.reactTemp'" :rules="{required: true, message: 'Please enter Temperature', trigger: 'blur'}">
                <el-input class="input" v-model="it.reactTemp" placeholder="Temperature / ℃" clearable></el-input>
              </el-form-item>
            </el-col>
            <!--Reaction time-->
            <el-col :xl="{span:4}" :lg="{span:4}" :md="{span:4}">
              <el-form-item :prop="'form.'+ batchIndex +'.reactTime'" :rules="{required: true, message: 'Please enter Reaction time', trigger: 'blur'}">
                <el-input class="input" v-model="it.reactTime" placeholder="Reaction time/min" clearable></el-input>
              </el-form-item>
            </el-col>
            <!--buffer-->
            <el-col :xl="{span:3}" :lg="{span:4}" :md="{span:4}">
              <el-form-item :prop="'form.'+ batchIndex +'.buffer'" :rules="{required: true, message: 'Please select Buffer', trigger: 'blur'}">
                <el-select v-model="it.buffer" placeholder="Buffer" clearable filterable>
                  <el-option
                    v-for="item in blocks"
                    :key="item.blockName"
                    :label="item.blockName"
                    :value="item.blockName">
                    <span style="float: left">{{ item.blockName }}</span>
                    <span style="float: right; color: #8492a6; font-size: 13px">{{ item.blockVolume}} ml</span>
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <!--buffer volume-->
            <el-col :xl="{span:4}" :lg="{span:4}" :md="{span:4}">
              <el-form-item :prop="'form.'+ batchIndex +'.bufferVolume'" :rules="{required: true, message: 'Please enter Buffer volume', trigger: 'blur'}">
                <el-input class="input" v-model="it.bufferVolume" placeholder="Buffer volume/μl" clearable></el-input>
              </el-form-item>
            </el-col>
          </el-row>

          <!--second line-->
          <el-row gutter=10>

            <!--mol of Block A-->
            <el-col :xl="{span:4,offset:2}" :lg="{span:5,offset:2}" :md="{span:4,offset:3}">
              <el-form-item :prop="'form.'+ batchIndex +'.subAN'" :rules="{required: true, message: 'Please enter mol of Block A', trigger: 'blur'}">
                <el-input class="input" v-model="it.subAN" placeholder="mol of Block A / μmol" clearable></el-input>
              </el-form-item>
            </el-col>
            <!--Eq of Block A-->
            <el-col :xl="4" :lg="4" :md="4">
              <el-form-item :prop="'form.'+ batchIndex +'.subAEq'" :rules="{required: true, message: 'Please enter Equivalent of Block A', trigger: 'blur'}">
                <el-input class="input" v-model="it.subAEq" placeholder="Eq of Block A" clearable></el-input>
              </el-form-item>
            </el-col>

            <!--Eq of Block B-->
            <el-col :xl="4" :lg="4" :md="4">
              <el-form-item :prop="'form.'+ batchIndex +'.subBEq'" :rules="{required: true, message: 'Please enter Equivalent of Block B', trigger: 'blur'}">
                <el-input class="input" v-model="it.subBEq" placeholder="Eq of Block B" clearable></el-input>
              </el-form-item>
            </el-col>

            <!--cat-->
            <el-col :xl="{span:3}" :lg="4" :md="4">
              <el-form-item :prop="'form.'+ batchIndex +'.cat'" :rules="{required: true, message: 'Please select Catalyst', trigger: 'blur'}">
                <el-select v-model="it.cat" placeholder="Catalyst" clearable filterable>-->
                  <el-option
                    v-for="items in blocks"
                    :key="items.blockName"
                    :label="items.blockName"
                    :value="items.blockName">
                    <span style="float: left">{{ items.blockName }}</span>
                    <span style="float: right; color: #8492a6; font-size: 13px">{{ items.blockVolume}} ml</span>
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>

            <!--Eq of Catalyst-->
            <el-col :xl="4" :lg="4" :md="4">
              <el-form-item :prop="'form.'+ batchIndex +'.catEq'" :rules="{required: true, message: 'Please enter Equivalent of Catalyst', trigger: 'blur'}">
                <el-input class="input" v-model="it.catEq" placeholder="Eq of Catalyst" clearable></el-input>
              </el-form-item>
            </el-col>

          </el-row>
          <!--Remark-->
          <el-row :gutter=10>
            <el-col :xl="{span:16,offset:2}" :lg="{span:19,offset:2}" :md="{span:18,offset:3}">
              <el-form-item :prop="'form.'+ batchIndex +'.remark'">
                <el-input class="input" v-model="it.remark" placeholder="Remark" clearable></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        <el-row style="height: 46px">
          <el-tag effect="plain"  style="margin-top: 3px">Screen params</el-tag>
        </el-row>
          <!--screened params-->
          <el-row class="row" v-for="(item,index) in it.parameter" :key="index" :gutter=10>
<!--            <label class="el-form-item__label label" style="width: 86px;" :style="{visibility:index===0?'visible':'hidden'}">筛选参数</label>&lt;!&ndash;设置动态标签,只在部门第一行显示&ndash;&gt;-->

            <el-col style="width: 70px" :xl="{offset:2}" :lg="{offset:2}" :md="{offset:3}">
              <el-form-item>
                <el-input class="input"  v-model="index" disabled style="text-align: center">{{index}}</el-input>
              </el-form-item>
            </el-col>

            <!--Block A-->
            <el-col :xl="{span:3}" :lg="{span:6}" :md="{span:7}">
              <el-form-item :prop="'form.'+ batchIndex +'.parameter.'+ index +'.substrateA'" :rules="{required: true, message: 'Please select Block A', trigger: 'blur'}">
                <el-select v-model="item.substrateA" placeholder="Block A" clearable filterable>
                  <el-option
                    v-for="items in blocks"
                    :key="items.blockName"
                    :label="items.blockName"
                    :value="items.blockName">
                    <span style="float: left">{{items.blockName }}</span>
                    <span style="float: right; color: #8492a6; font-size: 13px">{{items.blockVolume}} ml</span>
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>

            <!--Block B-->
            <el-col :span="6" :xl="{span:3}" :lg="{span:6}" :md="{span:7}">
              <el-form-item :prop="'form.'+ batchIndex +'.parameter.'+ index +'.substrateB'" :rules="{required: true, message: 'Please select Block B', trigger: 'blur'}">
                <el-select v-model="item.substrateB" placeholder="Block B" clearable filterable>
                  <el-option
                    v-for="items in blocks"
                    :key="items.blockName"
                    :label="items.blockName"
                    :value="items.blockName">
                    <span style="float: left">{{ items.blockName }}</span>
                    <span style="float: right; color: #8492a6; font-size: 13px">{{ items.blockVolume}} ml</span>
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>

            <el-col :span="4" style="margin-top: 9px;margin-left: 5px">
              <i class="el-icon-circle-plus"  v-if=" index == it.parameter.length - 1 " :v-show="it.parameter[index] == ''? false:true" @click="addDept(batchIndex)"></i>
              <i class="el-icon-remove" style="margin-left: 10px" v-if="it.parameter.length> 1" @click="delDept(batchIndex,index)"></i>
            </el-col>
          </el-row>
      </div>
    </el-form>

  </div>
</template>

<script>
export default {
  name: "Reaction",
  data(){
    return{
      batch: {
        form: [{
          batchSize: "",
          subAEq: '',
          subAN: '',
          subBEq: '',
          buffer: '',
          bufferVolume: '',
          reactTemp: '',
          remark: "",
          reactTime: '',
          cat: '',
          catEq: '',
          parameter: [{substrateA: '', substrateB: '',}]
        }]
      },
      blocks:[],
    }
  },
  methods:{
    addDept(index){
      if (this.batch.form[index].parameter.length < 16){
        this.batch.form[index].parameter.push({substrateA:'',substrateB:''});
      }else {
        this.$message({
          message: 'One batch can only run 16 experiments！',
          type: 'warning'
        });
      }
    },

    delDept(index1,index2){
      this.batch.form[index1].parameter.splice(index2,1);
    },

    submitTask(){
      console.log(this.batch)
      this.$http.post("/click/execute",this.batch.form).then(res=>{
        //接收参数
      })
    },

    selectBlocks(){
      this.$http.post("/block/allBlocks").then(res=>{
        console.log(res.data);
        this.blocks = JSON.parse(res.data);
      })
    },
    beforeRouteLeave(to,from,next){
      to.meta.keepAlive = true
      next(0)
    },
    addBatch(){
      this.batch.form.push(
        {
          batchSize:"",
          subAEq:'',
          subAN:'',
          subBEq:'',
          buffer:'',
          bufferVolume:'',
          reactTemp:'',
          remark:"",
          reactTime:'',
          cat:'',
          catEq:'',
          parameter:[{substrateA:'',substrateB:'',}]
        }
        );
    },
    delBatch(index){
      this.batch.form.splice(index,1);
    },
  },
  mounted() {
    this.selectBlocks()
  },
}
</script>

<style scoped>
.formBorder{
  margin-top: 12px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  padding-top: 10px;
  padding-left: 20px;
}
</style>
