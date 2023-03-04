package com.iiaim.service;

import com.iiaim.pojo.Block;
import com.iiaim.pojo.ClickBatch;
import com.iiaim.pojo.ClickPara;
import com.iiaim.pojo.ScreenPara;
import com.iiaim.serial.HeatStirrer;
import com.iiaim.serial.Liquid;
import com.iiaim.serial.Robot;
import com.iiaim.serial.SeventhAxis;
import jssc.SerialPortException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @author 陈杰炫
 */
@Service("clickServiceImpl")
public class ClickServiceImpl implements ClickService{

    @Autowired
    private Liquid liquid;
    @Autowired
    private Robot robot;
    @Autowired
    private HeatStirrer heatStirrer;
    @Autowired
    private SeventhAxis seventhAxis;
    @Autowired
    @Qualifier("blockServiceImpl")
    private BlockServices blockServices;


    @Override
    public String doClick(ClickBatch clickBatch) {
        try {
            if (robot.ready()) robot.exeScript("plate" + robot.getBatchSize() + ".spf", 100);
            liquid.moveToDes(0,0);
            if (robot.ready()) robot.exeScript("InterimToLiquid.spf", 100);
            //liquid dispensing
            String s = this.doDispense(clickBatch);
            if (!"success".equals(s)) System.out.println("exception in liquid-dispensing");
            if (robot.ready()) robot.exeScript("LiquidToInterim.spf", 100);
            //move seventh axis
            if (robot.ready()) robot.exeScript("cap" + (robot.getBatchSize() % 3) + ".spf", 100);
            //seventh axis returning
            if (robot.ready()) robot.exeScript("capBatch3.spf", 100);
            heatStirrer.startReaction((int) clickBatch.getReactTemp(),800);
            //在这里需要进行更改：指定时间后进行以下的动作，看看筛选后的时间需要多久

            liquid.moveToDes(0,0);
            if (robot.ready()) robot.exeScript("LiquidToInterim.spf", 100);
            if (robot.ready()) robot.exeScript("InterimToPlate" + robot.getBatchSize() + ".spf", 100);
            robot.setBatchSize((robot.getBatchSize() + 1) % 6);
            return "batch end";
        } catch (IOException e) {
            e.printStackTrace();
            return "exception occur";
        } catch (InterruptedException e) {
            e.printStackTrace();
            return "exception occur";
        } catch (SerialPortException e) {
            e.printStackTrace();
            return "exception occur";
        }
    }

    @Override
    public String doDispense(ClickBatch clickBatch) {
        liquid.washCircle();
        //dispense buffer;port 4
        liquid.prepare(4);
        int bufferVolume = clickBatch.getBufferVolume();
        for (int i = 1; i <= 4 ; i++) {
            for (int j = 10; j < 14; j++) {
                if (i != 4){
                    liquid.ejectCircle(j,i,3000,bufferVolume);
                    liquid.turnValve(4);
                    liquid.draw(bufferVolume);
                }else {
                    //liquid.ejectCircle(j,i,3000,bufferVolume);
                    if (j == 10) liquid.ejectCircle(j,i,3000,bufferVolume);
                    if (j == 11){
                        liquid.ejectScreen(j,i,3000,bufferVolume,1000);
                    }else {
                        liquid.turnValve(5);
                        liquid.draw(bufferVolume);
                        liquid.ejectCircle(j,i,3000,bufferVolume);
                    }
                }
            }
        }
        liquid.washCircle();

        //dispense cat;port 6
        liquid.prepare(6);
        Block cat = blockServices.getCoordinate(clickBatch.getCat());
        double catN = clickBatch.getCatEq() / clickBatch.getSubAEq() * clickBatch.getSubAN();
        int catVolume = (int) (catN / cat.getBlockConc() * 1000);
        for (int i = 1; i <= 4 ; i++) {
            for (int j = 10; j < 14; j++) {
                if (i != 4){
                    liquid.ejectCircle(j,i,3000,catVolume);
                    liquid.turnValve(6);
                    liquid.draw(catVolume);
                }else {
                    if (j == 10) liquid.ejectCircle(j,i,3000,catVolume);
                    if (j == 11){
                        liquid.ejectScreen(j,i,3000,catVolume,1000);
                    }else {
                        liquid.turnValve(5);
                        liquid.draw(catVolume);
                        liquid.ejectCircle(j,i,3000,catVolume);
                    }
                }
            }
        }
        liquid.washCircle();

        //dispense sovlent

        //dispense substrateA
        int corX = 10;
        int corY = 1;
        for (ClickPara para:clickBatch.getParameter()) {
            Block subA = blockServices.getCoordinate(para.getSubstrateA());
            double volume = clickBatch.getSubAN() / subA.getBlockConc();
            liquid.drawCircle(subA.getAxisX(), subA.getAxisY(), 5000, (int) volume);
            liquid.ejectCircle(corX++,corY,3000, (int) volume);
            liquid.washCircle();
            if (corX == 14){
                corX = 10;
                corY++;
            }
        }

        //dispense substrateB
        for (ClickPara para:clickBatch.getParameter()) {
            //依据策略进行调整，观察是否需要进行基元B的合并
            //一批如果会使用多个B基元的话可能就需要进行分类，如果只使用一个的话就没必要进行分类
            //多个B基元方法：遍历B基元构建一个map：做一个B与在list中对应索引的映射；同时做一个索引与坐标数组的map映射0-[1,1],1-[2,1]...
            //两个map映射确定以后，通过遍历B map实现一类B统一进行加样
        }
            return "success";
    }

    @Override
    public void doTimeScr(List<ScreenPara> list) throws InterruptedException {

    }
}
