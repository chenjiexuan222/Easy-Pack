<template>
  <div>
    <!--status line-->
    <div style="height: 50px;background-color: #f5f5f5;margin-top: -20px">
      <el-row :gutter="10">
          <el-form  :model="reaction" class="demo-form-inline" size="medium" style="margin-top: 8px;">
            <el-col :xl="3" :lg="3" :md="4">
              <el-form-item style="margin-left: 10px">
                <el-input v-model="reaction.batchSize" placeholder="Batchsize" clearable></el-input>
              </el-form-item>
            </el-col>

            <el-col :xl="3" :lg="3" :md="3">
              <el-form-item>
                <!--this keywords is used to fuzzy search two substrates-->
                <el-input v-model="reaction.substrateA" placeholder="Block" clearable></el-input>
              </el-form-item>
            </el-col>

            <el-col :xl="3" :lg="3" :md="3">
              <el-form-item>
                <el-input v-model="reaction.buffer" placeholder="Buffer" clearable></el-input>
              </el-form-item>
            </el-col>

            <el-col :xl="3" :lg="3" :md="4">
              <el-form-item>
                <el-input v-model="reaction.cat" placeholder="Catalyst" clearable></el-input>
              </el-form-item>
            </el-col>

            <el-col :xl="3" :lg="4" :md="3">
              <el-form-item>
                <el-input v-model="reaction.reactTime" placeholder="Reaction time" clearable></el-input>
              </el-form-item>
            </el-col>

            <el-col :xl="3" :lg="5" :md="3">
              <el-form-item>
                <el-input v-model="reaction.reactTemp" placeholder="Reaction temperature" clearable></el-input>
              </el-form-item>
            </el-col >
          </el-form>
        <el-col :xl="{span:1}" :lg="{span:1}" :md="{span:1}">
          <el-button type="primary" style="margin-top: -3px;margin-left: 5px" @click="onSubmit">Search</el-button>
        </el-col>
      </el-row>
    </div>
    <!--reaction form-->
    <div>
      <div style="margin-top: 10px">
        <template>
          <el-table :data="tableData" style="width: 100%" :header-cell-style="{background:'#eef1f6',color:'#606266'}" align="center">
            <el-table-column
              prop="batchSize"
              label="Batchsize"
              width = "100"
              align = "center">
            </el-table-column>
            <el-table-column
              prop="substrateA"
              label="Block A"
              width = "80"
              align = "center">
            </el-table-column>
            <el-table-column
              prop="subAEq"
              label="eq of Block A"
              width="115"
              align = "center">
            </el-table-column>
            <el-table-column
              prop="subAN"
              label="mol of Block A /umol"
              width="130"
              align = "center">
            </el-table-column>
            <el-table-column
              prop="substrateB"
              label="Block B"
              align = "center">
            </el-table-column>
            <el-table-column
              prop="subBEq"
              label="eq of Block B"
              width="115"
              align = "center">
            </el-table-column>
            <el-table-column
              prop="buffer"
              label="buffer"
              align = "center">
            </el-table-column>
            <el-table-column
              prop="bufferVolume"
              label="buffer volume /ml"
              width="125"
              align = "center">
            </el-table-column>
            <el-table-column
              prop="cat"
              label="catalyst"
              align = "center">
            </el-table-column>
            <el-table-column
              prop="catEq"
              label="eq of catalyst"
              width="115"
              align = "center">
            </el-table-column>
            <el-table-column
              prop="reactTime"
              label="Reaction time /min"
              width="125"
              align = "center">
            </el-table-column>
            <el-table-column
              prop="reactTemp"
              label="Reaction temperature /℃"
              width="100"
              align = "center">
            </el-table-column>
            <el-table-column
              prop="remark"
              label="Remark"
              align = "center">
            </el-table-column>
          </el-table>
        </template>

      </div>
    </div>
    <!--pagesize-->
    <div style="margin-top: 10px">
      <el-pagination
        background
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-sizes="[16, 32, 48, 64]"
        :page-size="pageSize"
        layout="total, prev, pager, next,jumper, sizes"
        :total="total">
      </el-pagination>
    </div>
  </div>

</template>

<script>
export default {
  name: "Reactionlib",
  data(){
    return{
      reaction:{
        batchSize:'',
        substrateA:'',
        subAEq:'',
        subAN:'',
        substrateB:'',
        subBEq:'',
        buffer:'',
        bufferVolume:'',
        cat:'',
        catEq:'',
        reactTime:'',
        reactTemp:'',
        remark:""
      },
      tableData:[],
      currentPage: 1,
      pageSize:32,
      total:0,
    }
  },
  methods:{
    //select data under some conditions
    onSubmit(){
      this.$http.post("/click/select",{currentPage:this.currentPage,pageSize:this.pageSize,reaction:this.reaction}).then(res=>{
        console.log(res.data);
        let data = JSON.parse(res.data);
        this.total = data.totals;
        this.tableData = data.reactions;
      })
    },
    //select data by pagesize
    selectAll(){
      this.$http.get("/click/allReactions",{params:{currentPage:this.currentPage,pageSize:this.pageSize}}).then(res=>{
        console.log(res.data);
        let reactionData = JSON.parse(res.data);
        console.log(reactionData);
        this.total = reactionData.total;
        this.tableData = reactionData.reactions;
      })
    },
    handleSizeChange(val) {
      this.pageSize = val;
      this.onSubmit();
      console.log(`每页 ${val} 条`);
    },
    handleCurrentChange(val) {
      this.currentPage = val;
      this.onSubmit();
      console.log(`当前页: ${val}`);
    }
  },
  mounted() {
    this.selectAll();
  }
}
</script>

<style scoped>

</style>
