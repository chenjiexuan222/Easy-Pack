<template>
  <div>
    <!--status line-->
    <div style="height: 45px;background-color: #faf9f9;margin-top: -20px">
      <el-row>
        <el-button size="medium" type="success" icon="el-icon-plus" style="margin-left: 10px;margin-top: 5px" @click="addBatch">New batch</el-button>
        <el-button size="medium" type="success" icon="el-icon-s-promotion" style="margin-left: 10px;margin-top: 5px" @click="submitTask">Submit task</el-button>
      </el-row>
    </div
    >

    <!--form-->
    <el-form :model ="batch" ref="screenForm">
      <!--form:batch unit-->
      <div class="formBorder" v-for="(it,batchIndex) in this.batch.form" >
        <el-row>
          <el-col :xl="{span:1}" :lg="{span:1}" :md="{span:1}">
            <el-button icon="el-icon-folder" type="info" size="mini" disabled>batch{{batchIndex + 1}}</el-button>
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
          <el-col :xl="{span:3}" :lg="{span:4}" :md="{span:4}">
            <el-form-item :prop="'form.'+ batchIndex +'.batchSize'" :rules="{required: true, message: 'Please enter Batchsize', trigger: 'blur'}">
              <el-input class="input" v-model="it.batchSize" placeholder="Batchsize"  clearable></el-input>
            </el-form-item>
          </el-col>
          <!--Temperature-->
          <el-col :xl="{span:4}" :lg="{span:5}" :md="{span:6}">
            <el-form-item :prop="'form.'+ batchIndex +'.reactTemp'" :rules="{required: true, message: 'Please enter Temperature', trigger: 'blur'}">
              <el-input class="input" v-model="it.reactTemp" placeholder="Temperature / ℃" clearable></el-input>
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
          <!--Buffer volume-->
          <el-col :xl="{span:3}" :lg="{span:4}" :md="{span:6}">
            <el-form-item :prop="'form.'+ batchIndex +'.bufferVolume'" :rules="{required: true, message: 'Please enter Buffer Volume', trigger: 'blur'}">
              <el-input class="input" v-model="it.bufferVolume" placeholder="Buffer volume / μl" clearable></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <!--second line-->
        <el-row gutter=10>
          <!--Block A-->
          <el-col :xl="{span:3,offset:2}" :lg="{span:4,offset:2}" :md="{span:4,offset:3}">
            <el-form-item :prop="'form.'+ batchIndex +'.substrateA'" :rules="{required: true, message: 'Please select Block A', trigger: 'blur'}">
              <el-select v-model="it.substrateA" placeholder="Block A" clearable filterable>
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
          <!--mol of Block A-->
          <el-col :xl="4" :lg="5" :md="6">
            <el-form-item :prop="'form.'+ batchIndex +'.subAN'" :rules="{required: true, message: 'Please select Block A', trigger: 'blur'}">
              <el-input class="input" v-model="it.subAN" placeholder="mol of Block A / μmol" clearable></el-input>
            </el-form-item>
          </el-col>
          <!--Eq of Block A-->
          <el-col :xl="3" :lg="4" :md="5">
            <el-form-item :prop="'form.'+ batchIndex +'.subAEq'" :rules="{required: true, message: 'Please enter Equivalent of Block A', trigger: 'blur'}">
              <el-input class="input" v-model="it.subAEq" placeholder="Eq of Block A" clearable></el-input>
            </el-form-item>
          </el-col>
          <!--Block B-->
          <el-col :xl="{span:3}" :lg="4" :md="4">
            <el-form-item :prop="'form.'+ batchIndex +'.substrateB'" :rules="{required: true, message: 'Please enter Block B', trigger: 'blur'}">
              <el-select v-model="it.substrateB" placeholder="Block B" clearable filterable>
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
          <!--Eq of Block B-->
          <el-col :xl="{span:3}" :lg="{span:4}" :md="{span:5}">
            <el-form-item :prop="'form.'+ batchIndex +'.subBEq'" :rules="{required: true, message: 'Please enter Equivalent of Block B', trigger: 'blur'}">
              <el-input class="input" v-model="it.subBEq" placeholder="Eq of Block B" clearable></el-input>
            </el-form-item>
          </el-col>

        </el-row>

        <!--third line-->
        <el-row :gutter=10>
          <el-col :xl="{span:16,offset:2}" :lg="{span:20,offset:2}" :md="{span:20,offset:3}">
            <el-form-item :prop="'form.'+ batchIndex +'.remark'">
              <el-input class="input" v-model="it.remark" placeholder="Remark" clearable></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row style="height: 45px">
          <el-tag effect="plain"  style="margin-top: 3px">screen params</el-tag>
        </el-row>
        <!--screened params-->
        <el-row class="row" v-for="(item,index) in it.parameter" :key="index" :gutter=10>
<!--          <label class="el-form-item__label label" style="width: 85px;" :style="{visibility:index===0?'visible':'hidden'}">筛选参数</label>&lt;!&ndash;设置动态标签,只在部门第一行显示&ndash;&gt;-->

          <el-col style="width: 70px" :xl="{offset:2}" :lg="{offset:2}" :md="{offset:3}">
            <el-form-item>
              <el-input class="input"  v-model="index" disabled style="text-align: center">{{index}}</el-input>
            </el-form-item>
          </el-col>

          <el-col :xl="{span:3}" :lg="{span:5}" :md="{span:4}">
            <el-form-item :prop="'form.'+ batchIndex +'.parameter.'+index+'.cat'"
                          :rules="{required: true, message: 'Please select Catalyst', trigger: 'blur'}">
              <el-select v-model="item.cat" placeholder="Catalyst" clearable filterable>
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

          <el-col :xl="{span:5}" :lg="{span:5}" :md="{span:5}">
            <el-form-item :prop="'form.'+ batchIndex +'.parameter.'+ index +'.catEq'" :rules="{
            required: true, message: 'Please enter Eq of Catalyst', trigger: 'blur'
          }">
              <el-input class="input" v-model="item.catEq" placeholder="Eq of Catalyst" clearable></el-input>
            </el-form-item>
          </el-col>

          <el-col :xl="{span:5}" :lg="{span:5}" :md="{span:6}">
            <el-form-item :prop="'form.'+ batchIndex +'.parameter.'+index+'.reactTime'" :rules="{required: true, message: 'Please enter Reaction time', trigger: 'blur'}">
              <el-input class="input" v-model="item.reactTime" placeholder="Reaction time /min" clearable></el-input>
            </el-form-item>
          </el-col>

          <el-col :xl="{span:4}" :lg="{span:4}" :md="{span:3}" style="margin-top: 9px;margin-left: 5px">
            <i class="el-icon-circle-plus" style="margin-left: 2px" v-if=" index == it.parameter.length - 1 " :v-show="it.parameter[index] == ''? false:true" @click="addDept(batchIndex)"></i>
            <i class="el-icon-remove" style="margin-left: 2px" v-if="it.parameter.length> 1" @click="delDept(batchIndex,index)"></i>
          </el-col>
        </el-row>
      </div>
    </el-form>
  </div>
</template>

<script>
export default {
  name: "ReactionScr",
  data(){
    return{
        batch: {
          form: [{
            batchSize: "",
            substrateA: '',
            subAEq: '',
            subAN: '',
            substrateB: '',
            subBEq: '',
            buffer: '',
            bufferVolume: '',
            reactTemp: '',
            remark: "",
            parameter: [{cat: '', catEq: '', reactTime: ''}]
          }]
        },
      blocks:[],
    }
  },
  methods:{
    //add reaction in one batch
    addDept(index){
      if (this.batch.form[index].parameter.length < 16){
        this.batch.form[index].parameter.push({cat:'',catEq:'',reactTime:''});
      }else {
        this.$message({
          message: 'One batch can only run 16 experiments！',
          type: 'warning'
        });
      }
    },

    //delete reaction in one batch
    delDept(index1,index2){
      this.batch.form[index1].parameter.splice(index2,1);
    },

    //submit reaction task
    submitTask(){
      console.log(this.form)
      this.$refs["screenForm"].validate((valid) => {
        if (valid) {
          this.$http.post("/screen/execute",this.batch.form).then(res=>{
            //接收参数
          })
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },

    //select all Blocks
    selectBlocks(){
      this.$http.post("/block/allBlocks").then(res=>{
        console.log(res.data);
        this.blocks = JSON.parse(res.data);
      })
    },

    //route
    beforeRouteLeave(to,from,next){
      to.meta.keepAlive = true
      next(0)
    },

    //add batch size
    addBatch(){
      this.batch.form.push(
        {
          batchSize:"",
          substrateA:'',
          subAEq:'',
          subAN:'',
          substrateB:'',
          subBEq:'',
          buffer:'',
          bufferVolume:'',
          reactTemp:'',
          remark:"",
          parameter:[{cat:'',catEq:'',reactTime:''}]
        }
      );
    },

    //delete reaction batch
    delBatch(index){
      this.batch.form.splice(index,1);
    },
  },

  //loading
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
