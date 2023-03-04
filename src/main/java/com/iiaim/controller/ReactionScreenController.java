package com.iiaim.controller;

import com.iiaim.pojo.ClickReaction;
import com.iiaim.pojo.ScreenBatch;
import com.iiaim.pojo.ScreenPara;
import com.iiaim.service.BlockServices;
import com.iiaim.service.ClickReactionServices;
import com.iiaim.service.ScreenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * @author 陈杰炫
 */
@RestController
@CrossOrigin
@RequestMapping("/screen")
public class ReactionScreenController {

    @Autowired
    @Qualifier("blockServiceImpl")
    private BlockServices blockServices;

    @Autowired
    @Qualifier("clickReactionServiceImpl")
    private ClickReactionServices clickReactionServices;

    @Autowired
    @Qualifier("screenServiceImpl")
    private ScreenService screenService;


    @PostMapping("/execute")
    public String executeExperiment(@RequestBody List<ScreenBatch> list) throws IOException {
        System.out.println(list.size());
        //add reaction to reaction DB
        for (ScreenBatch batch:list) {
            for (ScreenPara para:batch.getParameter()) {
                ClickReaction reaction = new ClickReaction();
                reaction.setBatchSize(batch.getBatchSize());
                reaction.setSubstrateA(batch.getSubstrateA());
                reaction.setSubAEq(batch.getSubAEq());
                reaction.setSubAN(batch.getSubAN());
                reaction.setSubstrateB(batch.getSubstrateB());
                reaction.setSubBEq(batch.getSubBEq());
                reaction.setBuffer(batch.getBuffer());
                reaction.setBufferVolume(batch.getBufferVolume());
                reaction.setReactTemp(batch.getReactTemp());
                reaction.setRemark(batch.getRemark());
                reaction.setCat(para.getCat());
                reaction.setCatEq(para.getCatEq());
                reaction.setReactTime(para.getReactTime());
                clickReactionServices.addReaction(reaction);
            }
            String s = screenService.doScreen(batch);
            if ("batch end".equals(s))continue;
            else return "fail";
        }
        return "success";
    }
}