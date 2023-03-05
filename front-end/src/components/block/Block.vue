<template>
  <div>
    <!--Status Bar-->
    <div style="height: 50px;background-color: #f5f5f5;margin-top: -20px">
      <el-row>
        <el-col :xl="15" :lg="16" :md="20">
          <el-form :inline="true" :model="block" class="demo-form-inline" size="small" style="margin-top: 9px;margin-left: 10px">
            <el-button plain size="medium" type="danger" icon="el-icon-delete" style="margin-top: -2px" @click="deleteBlocks">Delete</el-button>
            <el-form-item style="margin-left: 15px">
              <el-input v-model="block.blockName" placeholder="BlockName" clearable></el-input>
            </el-form-item>
            <el-form-item>
              <el-input v-model="block.remark" placeholder="Remark" clearable></el-input>
            </el-form-item>
            <el-form-item style="margin-top: -1px">
              <el-button type="primary" @click="onSubmit">Search</el-button>
            </el-form-item>
          </el-form>
        </el-col>
        <el-col :xl="{span:1,offset:7}" :lg="{span:1,offset:5}" :md="{span:1}">
          <el-button size="medium" type="success" icon="el-icon-plus" style="margin-left:20px; margin-top: 6px" @click="addBlock">Add</el-button>
        </el-col>
      </el-row>
    </div>
    <!--form-->
    <div style="margin-top: 10px">
      <template>
        <el-table
          :data="tableData"
          style="width: 100%"
          :header-cell-style="{background:'#eef1f6',color:'#606266'}"
          :default-sort = "{prop: 'axisX'}"
          align="center"
          @selection-change="handleSelectionChange">
          <el-table-column
            type="selection"
            align = "center">
          </el-table-column>
          <el-table-column
            prop="blockName"
            label="BlockName"
            align = "center">
          </el-table-column>
          <el-table-column
            prop="blockConc"
            label="CONC /mM"
            align = "center">
          </el-table-column>
          <el-table-column
            prop="blockVolume"
            label="Volume /ml"
            align = "center">
          </el-table-column>
          <el-table-column
            prop="axisX"
            label="X Coordinate"
            sortable
            align = "center">
          </el-table-column>
          <el-table-column
            prop="axisY"
            label="Y Coordinate"
            sortable
            align = "center">
          </el-table-column>
          <el-table-column
            prop="remark"
            label="Remark"
            align = "center">
          </el-table-column>
          <el-table-column label="Edit" align="center" width="140px">
              <template slot-scope="scope">
              <el-button  icon="el-icon-edit" circle size="mini" @click="handleEdit(scope.$index, scope.row)"></el-button>
              <el-button  icon="el-icon-plus" circle size="mini" @click="plus(scope.$index, scope.row)"></el-button>
              </template>
          </el-table-column>
        </el-table>
      </template>

    </div>
    <!--edit form-->
    <div>
      <el-dialog  :visible.sync="dialogFormVisible" width="500px" @close="closeDialog">
        <el-form :model="block" label-position="right" label-width="130px" :rules="rules">
          <el-form-item label="blockName:">
            <el-input v-model="block.blockName" size="small"></el-input>
          </el-form-item>
          <el-form-item label="blockVolume/ml:">
            <el-input v-model="block.blockVolume" size="small" oninput="value=value.replace(/[^0-9.]/g,'').replace(/^\./g, '').replace('.', 'dollar#dollar').replace(/\./g, '').replace('dollar#dollar', '.');"></el-input>
          </el-form-item>
          <el-form-item label="CONC /mM:">
            <el-input v-model="block.blockConc" size="small" oninput="value=value.replace(/[^0-9.]/g,'').replace(/^\./g, '').replace('.', 'dollar#dollar').replace(/\./g, '').replace('dollar#dollar', '.');"></el-input>
          </el-form-item>
          <el-form-item label="X Coordinate:">
            <el-select v-model="block.axisX" placeholder="Please select X Coordinate">
              <el-option label="1" value="1"></el-option>
              <el-option label="2" value="2"></el-option>
              <el-option label="3" value="3"></el-option>
              <el-option label="4" value="4"></el-option>
              <el-option label="5" value="5"></el-option>
              <el-option label="6" value="6"></el-option>
              <el-option label="7" value="7"></el-option>
              <el-option label="8" value="8"></el-option>
              <el-option label="9" value="9"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="Y Coordinate:" prop="axisY">
            <el-select v-model="block.axisY" placeholder="Please select Y Coordinate">
              <el-option label="1" value="1"></el-option>
              <el-option label="2" value="2"></el-option>
              <el-option label="3" value="3"></el-option>
              <el-option label="4" value="4"></el-option>
              <el-option label="5" value="5"></el-option>
              <el-option label="6" value="6"></el-option>
              <el-option label="7" value="7"></el-option>
              <el-option label="8" value="8"></el-option>
              <el-option label="9" value="9"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="Remark:">
            <el-input v-model="block.remark" size="small"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="cancel">Cnncel</el-button>
          <el-button type="primary" @click="update">Submit</el-button>
        </div>
      </el-dialog>
    </div>
    <!--add dialog-->
    <div>
      <el-dialog  :visible.sync="dialogFormVisible2" width="500px" @close="closeDialog">
        <el-form :model="block" label-position="right" label-width="135px" :rules="rules" ref="block">
          <el-form-item label="blockName:">
            <el-input v-model="block.blockName" size="small"></el-input>
          </el-form-item>
          <el-form-item label="blockVolume/ml:">
            <el-input v-model="block.blockVolume" size="small" oninput="value=value.replace(/[^0-9.]/g,'').replace(/^\./g, '').replace('.', 'dollar#dollar').replace(/\./g, '').replace('dollar#dollar', '.');"></el-input>
          </el-form-item>
          <el-form-item label="CONC/mM:">
            <el-input v-model="block.blockConc" size="small" oninput="value=value.replace(/[^0-9.]/g,'').replace(/^\./g, '').replace('.', 'dollar#dollar').replace(/\./g, '').replace('dollar#dollar', '.');"></el-input>
          </el-form-item>
          <el-row :gutter="10">
            <el-col :span="12">
              <el-form-item label="X Coordinate" >
                <el-select v-model="block.axisX" placeholder="X Coordinate">
                  <el-option label="1" value="1"></el-option>
                  <el-option label="2" value="2"></el-option>
                  <el-option label="3" value="3"></el-option>
                  <el-option label="4" value="4"></el-option>
                  <el-option label="5" value="5"></el-option>
                  <el-option label="6" value="6"></el-option>
                  <el-option label="7" value="7"></el-option>
                  <el-option label="8" value="8"></el-option>
                  <el-option label="9" value="9"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="11">
              <el-form-item label="Y Coordinate" size="medium" prop="axisY" label-width="100px">
                <el-select v-model="block.axisY" placeholder="Y Coordinate">
                  <el-option label="1" value="1"></el-option>
                  <el-option label="2" value="2"></el-option>
                  <el-option label="3" value="3"></el-option>
                  <el-option label="4" value="4"></el-option>
                  <el-option label="5" value="5"></el-option>
                  <el-option label="6" value="6"></el-option>
                  <el-option label="7" value="7"></el-option>
                  <el-option label="8" value="8"></el-option>
                  <el-option label="9" value="9"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="Remark:">
            <el-input v-model="block.remark" size="small"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer">
          <el-button @click="dialogFormVisible2 = false">Cancel</el-button>
          <el-button type="primary" @click="addBlocks">Submit</el-button>
        </div>
      </el-dialog>
    </div>
    <!--pagesize-->
    <div style="margin-top: 10px">
      <el-pagination
        background
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-sizes="[10, 15, 20, 30, 40]"
        :page-size="pageSize"
        layout="total, prev, pager, next,jumper, sizes"
        :total="total">
      </el-pagination>
    </div>
  </div>
</template>

<script>
export default {
  name: "Block",
  data() {
    var validateCord = (rule, value, callback) => {
      this.$http.post("/block/verify",this.block).then(res=>{
        console.log(res.data)
        if ("fail" == res.data){
          callback(new Error('this coordinate has been used!'));
        }else {
          callback();
        }
      }).catch(error=>{
      })
    };
    return {
      //edit dialog
      dialogFormVisible:false,
      //add dialog
      dialogFormVisible2:false,
      //table params
      tableData:[],
      //block
      block:{
        blockName:'',
        blockConc:'',
        blockVolume:'',
        axisX:'',
        axisY:'',
        remark:""
      },

      multipleSelection: [],
      multiSelectIds:[],
      currentPage: 1,
      pageSize:20,
      total:10,
      rules: {
        axisY:[
          {validator: validateCord, trriger: "blur"}
        ]
      },
    }
  },
  methods: {
    //add block
    addBlock(){
      this.dialogFormVisible2 = true;
    },
    addBlocks(){
      this.$http.post("/block/add",this.block).then(res=>{
        this.dialogFormVisible2 = false;
        if (res.data == "success"){
          this.$message({
            message: 'Add successfully',
            type: 'success'
          });
          this.selectByPage();
          this.block = {};
        }
      })
    },

    //delete in batch
    deleteBlocks(){
      this.$confirm('This will delete the selected data, go on?', 'Infofm', {
        confirmButtonText: 'Yes',
        cancelButtonText: 'No',
        type: 'warning'
      }).then(() => {
        for (let i = 0; i < this.multipleSelection.length; i++) {
          this.multiSelectIds[i] = this.multipleSelection[i].id;
        }
        this.$http.post("/block/delete",this.multiSelectIds).then(res=>{
          if (res.data == "success"){
            this.$message({
              message: 'Delete successfully',
              type: 'success'
            });
            this.selectByPage();
          }else {
            this.$message.error('Delete failed');
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: 'The delete is canceled'
        });
      });
    },

    handleSelectionChange(val) {
      this.multipleSelection = val;
    },

    //select all data to show
    selectAll(){
      this.$http.post("/block/allBlocks").then(res=>{
        this.tableData = JSON.parse(res.data);
      })
    },

    //select data to show by pagasize
    selectByPage(){
      this.$http.get("/block/page",{params:{currentPage:this.currentPage,pageSize:this.pageSize}}).then(res=>{
        console.log(res.data);
        var blockData = JSON.parse(res.data);
        console.log(blockData);
        this.total = blockData.total;
        this.tableData = blockData.blocks;
      })
    },

    //select data to show under some conditions
    onSubmit() {
      this.$http.post("/block/selectBlocks",{currentPage:this.currentPage,pageSize:this.pageSize,block:this.block}).then(res=>{
        let data = JSON.parse(res.data);
        this.tableData = data.blocks;
        this.total =data.totals;
      })
    },

    //edit data in form line
    handleEdit(index, row) {
      // console.log(index, row);
      this.dialogFormVisible = true;
      this.block = row;
    },

    //duplicate data in forms
    plus(index, row) {
      this.block = row;
      this.block.axisX = 0;
      this.block.axisY = 0;
      this.$http.post("/block/add",this.block).then(res=>{
        if (res.data == "success"){
          this.$message({
            message: 'Duplicate successfully',
            type: 'success'
          });
          this.selectByPage();
          this.block={};
        }
      })
    },
    //update data
    update(){
      this.dialogFormVisible = false;
      this.$http.post("/block/update",this.block).then(res=>{
        if ( "success" == res.data){
          this.$message({
            message: 'Edit successfully',
            type: 'success'
          });
          this.selectByPage();
        }else {
          this.$message.error('Edit failed');
        }
      })
    },
    // close dialog
    closeDialog(){
      this.block = {};
    },

    handleSizeChange(val) {
      this.pageSize = val;
      this.selectByPage();
      console.log(`每页 ${val} 条`);
    },
    handleCurrentChange(val) {
      this.currentPage = val;
      this.selectByPage();
      console.log(`当前页: ${val}`);
    },
    cancel(){
      this.dialogFormVisible = false;
      this.selectByPage();
    }
  },
  mounted() {
    //this.selectAll();
    this.selectByPage();
  }
}
</script>

<style>
</style>
