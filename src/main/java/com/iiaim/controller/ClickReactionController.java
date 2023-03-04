package com.iiaim.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.iiaim.pojo.ClickBatch;
import com.iiaim.pojo.ClickPara;
import com.iiaim.pojo.ClickReaction;
import com.iiaim.service.BlockServices;
import com.iiaim.service.ClickReactionServices;
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
@RequestMapping("/click")
public class ClickReactionController {

    @Autowired
    @Qualifier("blockServiceImpl")
    private BlockServices blockServices;

    @Autowired
    @Qualifier("clickReactionServiceImpl")
    private ClickReactionServices clickReactionServices;

    @GetMapping("/allReactions")
    public String selectAll(@RequestParam Integer currentPage, @RequestParam Integer pageSize){
        //statr index
        int begin = (currentPage - 1) * pageSize;
        //query number
        List<ClickReaction> clickReactions = clickReactionServices.allReaction(begin, pageSize);
        //total count
        Long total = clickReactionServices.findTotal();
        Map<String, Object> map1 = new HashMap<>();
        map1.put("total", total);
        map1.put("reactions", clickReactions);
        String jsonString = JSON.toJSONString(map1);
        System.out.println(jsonString);
        return jsonString;
    }

    @RequestMapping("/select")
    public String select(@RequestBody Map<String,String> map){
        Integer currentPage = Integer.parseInt(map.get("currentPage"));
        Integer pageSize = Integer.parseInt(map.get("pageSize"));
        ClickReaction reaction = JSON.parseObject(map.get("reaction"), ClickReaction.class);

        Page<ClickReaction> page = clickReactionServices.selectReaction(reaction, currentPage, pageSize);
        Map<String, Object> map1 = new HashMap<>();
        map1.put("reactions", page);
        System.out.println(page.getTotal());
        map1.put("totals", page.getTotal());
        String jsonString = JSON.toJSONString(map1);
        System.out.println(jsonString);
        return jsonString;
    }

    @PostMapping("/execute")
    public String executeExperiment(@RequestBody List<ClickBatch> list) throws IOException {
        if (list == null) return "null param";
        //add reaction to reaction DB
        for (ClickBatch batch:list) {
            for (ClickPara para:batch.getParameter()) {
                ClickReaction reaction = new ClickReaction();
                reaction.setBatchSize(batch.getBatchSize());
                reaction.setSubstrateA(para.getSubstrateA());
                reaction.setSubAEq(batch.getSubAEq());
                reaction.setSubAN(batch.getSubAN());
                reaction.setSubstrateB(para.getSubstrateB());
                reaction.setSubBEq(batch.getSubBEq());
                reaction.setBuffer(batch.getBuffer());
                reaction.setBufferVolume(batch.getBufferVolume());
                reaction.setReactTemp(batch.getReactTemp());
                reaction.setRemark(batch.getRemark());
                reaction.setCat(batch.getCat());
                reaction.setCatEq(batch.getCatEq());
                reaction.setReactTime(batch.getReactTime());
                clickReactionServices.addReaction(reaction);
            }
        }
        //use 1batch process if there is just 1batch
        if (list.size() == 1)clickReactionServices.doOneBatch(list.get(0));

        //use 2batch process if there are 2 batches or more
        if (list.size() == 2)clickReactionServices.doComplexBatch(list);

        return "success";
    }
}