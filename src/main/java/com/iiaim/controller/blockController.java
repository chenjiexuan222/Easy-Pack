package com.iiaim.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.iiaim.pojo.Block;
import com.iiaim.service.BlockServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 陈杰炫
 */
@RestController
@CrossOrigin
@RequestMapping("/block")
public class blockController {
    @Autowired
    @Qualifier("blockServiceImpl")
    private BlockServices blockServices;

    @RequestMapping(value = "/allBlocks",produces = "application/json; charset=utf-8")
    public String selectAll(){
        List<Block> blocks = blockServices.selectAllBlocks();
        String blocksString = JSON.toJSONString(blocks);
        System.out.println(blocksString);
        return blocksString;
    }

    @PostMapping("/selectBlocks")
    public String selectBlocks(@RequestBody Map<String,String> map){
        Integer currentPage = Integer.parseInt(map.get("currentPage"));
        Integer pageSize = Integer.parseInt(map.get("pageSize"));
        Block block = JSON.parseObject(map.get("block"), Block.class);

        Page<Block> page =blockServices.selectBlocks(block,currentPage,pageSize);
        Map<String, Object> map1 = new HashMap<>();
        map1.put("blocks", page);
        System.out.println(page.getTotal());
        map1.put("totals", page.getTotal());
        String jsonString = JSON.toJSONString(map1);
        System.out.println(jsonString);
        return jsonString;
    }

    @PostMapping("/add")
    public String addBlock(@RequestBody Block block){
        blockServices.addBlock(block);
        return "success";
    }

    @RequestMapping("/update")
    public String updateBlock(@RequestBody String json) throws IOException {
        //System.out.println(json);
        Block block = JSON.parseObject(json, Block.class);
        blockServices.updateBlock(block);
        return "success";
    }

    @PostMapping("/delete")
    public String deleteBlocks(@RequestBody String deleteIds){
        int[] ids= JSON.parseObject(deleteIds, int[].class);
        blockServices.deleteBlocksByIds(ids);
        return "success";
    }

    @GetMapping("/page")
    public String selectByPage(@RequestParam Integer currentPage,@RequestParam Integer pageSize){
        //statr index
        int begin = (currentPage - 1) * pageSize;
        //query number
        List<Block> blocks = blockServices.selectByPage(begin, pageSize);
        //total count
        Long total = blockServices.findTotal();
        Map<String, Object> map1 = new HashMap<>();
        map1.put("total", total);
        map1.put("blocks", blocks);
        String jsonString = JSON.toJSONString(map1);
        System.out.println(jsonString);
        return jsonString;
    }

    @PostMapping("/verify")
    public String verify(@RequestBody Block block){
        System.out.println("verify");
        Block block1 = blockServices.verify(block);
        System.out.println(block);
        if (block1 != null) return "fail";
        return "success";
    }
}