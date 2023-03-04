package com.iiaim.service;

import com.github.pagehelper.Page;
import com.iiaim.pojo.ClickBatch;
import com.iiaim.pojo.ClickReaction;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author 陈杰炫
 */

public interface ClickReactionServices{

    void addReaction(ClickReaction clickReaction);

    List<ClickReaction> allReaction(int begin, int size);

    //get the total number of data
    Long findTotal();


    Page<ClickReaction> selectReaction(ClickReaction reaction, int currentPage, int pageSize);

    String doOneBatch(ClickBatch clickBatch);

    String doDispense(ClickBatch clickBatch);

    void doAnalysis(int reactionTime);

    void doSubTask(int reactionTime,int secondBatchSize);

    void doBinaryBatch(int secondBatchSize, int temp,CountDownLatch latch);

    String doComplexBatch(List<ClickBatch> list);

}
