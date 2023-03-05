<template>
  <div>
    <!--status bar-->
    <div style="height: 50px;background-color: #faf9f9;margin-top: -20px">
      <el-row :gutter=2>
        <el-col  :xl="{span:2}" :lg="{span:3}" :md="{span:4}" offset=0.5 style="margin-top: 6px;margin-left: 10px">
          <el-button size="medium" type="success" icon="el-icon-plus" @click="addBatch">Result</el-button>
        </el-col>
        <el-col span=4 style="margin-top: 6px">
          <el-button size="medium" type="success" icon="el-icon-s-promotion"  @click="submitTask">Submit task</el-button>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
export default {
  name: "Result",
  data() {
    return {
    }
  }
}
</script>

<style scoped>

</style>
