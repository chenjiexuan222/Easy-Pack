package com.iiaim.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.iiaim.mapper.ClickReactionMapper;
import com.iiaim.pojo.*;
import com.iiaim.serial.HeatStirrer;
import com.iiaim.serial.Liquid;
import com.iiaim.serial.Robot;
import com.iiaim.serial.SeventhAxis;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import jssc.SerialPortException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 陈杰炫
 */
@Service("clickReactionServiceImpl")
public class ClickReactionServiceImpl implements ClickReactionServices{

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
    @Autowired
    private ClickReactionMapper clickMapper;

    @Override
    public void addReaction(ClickReaction clickReaction) {
        clickMapper.addReaction(clickReaction);
    }

    @Override
    public List<ClickReaction> allReaction(int begin,int size) {
        return clickMapper.allReaction(begin,size);
    }

    @Override
    public Long findTotal() {
        return clickMapper.findTotal();
    }

    @Override
    public Page<ClickReaction> selectReaction(ClickReaction reaction, int currentPage, int pageSize) {
        Page<ClickReaction> page = PageHelper.startPage(currentPage, pageSize,true);
        clickMapper.selectReaction(reaction);
        return page;
    }

    @Override
    public String doOneBatch(ClickBatch clickBatch) {
        try {
            robot.exeScript("plate" + robot.getBatchSize() + ".spf", 30);
            liquid.moveToDes(0,0);
            if (robot.ready()) {
                robot.exeScript("InterimToLiquid.spf", 35);
            }
            if (robot.ready()) {
                //移液站进行指定的液体分配工作
                String mes = this.doDispense(clickBatch);
                if (!"success".equals(mes)) System.out.println("error in liquid-dispense");
                System.out.println(" dispense-end");
            }
            robot.exeScript("LiquidToInterim.spf", 32);

            if (robot.ready()) {
                //移动轨道的移动
                seventhAxis.setPosition((short) 1997);
                seventhAxis.start();
            }
            robot.exeScript("cap" + (robot.getBatchSize() % 3) + ".spf", 35);

            if (robot.ready()) {
                //移动轨道移回
                seventhAxis.setPosition((short) 1500);
                seventhAxis.start();
            }

            robot.exeScript("capBatch3.spf", 30);
            if (robot.ready()) {
                heatStirrer.startReaction((int) clickBatch.getReactTemp(),800);
                this.doAnalysis(clickBatch.getReactTime());
                liquid.moveToDes(0,0);
            }
            robot.exeScript("LiquidToInterim-cap.spf", 30);
            if (robot.ready()) robot.exeScript("plateback" + robot.getBatchSize() + ".spf", 30);
            robot.setBatchSize((robot.getBatchSize() + 1) % 6);
            return "batch end";
        } catch (IOException e) {
            e.printStackTrace();
            return "exception occur";
        } catch (InterruptedException e) {
            e.printStackTrace();
            return "exception occur";
        } catch (ModbusInitException e) {
            e.printStackTrace();
            return "exception occur";
        } catch (ModbusTransportException e) {
            e.printStackTrace();
            return "exception occur";
        } catch (SerialPortException e) {
            e.printStackTrace();
            return "exception occur";
        }
    }

    @Override
    public String doDispense(ClickBatch clickBatch) {

        //dispense substrateA
        //apply to only one substrateA in one group
        List<ClickPara> paras = clickBatch.getParameter();
        Block substrateA = blockServices.getCoordinate(paras.get(0).getSubstrateA());
        int volumeA = (int) (clickBatch.getSubAN()/substrateA.getBlockConc() * 1000);
        // one circle ---> four vials
        for (int row = 1; row <= 4 ; row++) {
            //draw double substrateA
            liquid.drawSovlent(substrateA.getAxisX(),substrateA.getAxisY(),5000,4*volumeA);
            //liquid.drawScreen(substrateA.getAxisX(), substrateA.getAxisY(), 8500,volumeA);
            for (int column = 10; column <=13 ; column++) {
                liquid.ejectCircle(column,row,2000,volumeA,0);
            }
        }
        liquid.washCircle();
        System.out.println("A end");

        int cory = 1;
        int corx = 10;
        /*for (ClickPara para:clickBatch.getParameter()) {
            Block substrateA = blockServices.getCoordinate(para.getSubstrateA());
            //volume unit:ul; amount of substrate:umol
            int volumeA = (int) (clickBatch.getSubAN()/substrateA.getBlockConc() * 1000);
            liquid.drawSovlent(substrateA.getAxisX(), substrateA.getAxisY(), 8500,volumeA);
            liquid.ejectCircle(corx++,cory,2000,volumeA);
            if (corx == 14){
                corx = 10;
                cory++;
            }
            liquid.washCircle();
        }*/

        // dispense buffer:port 4
        liquid.prepare(4);
        int bufferVolume = clickBatch.getBufferVolume();
        for (int i = 1; i <= 4 ; i++) {
            for (int j = 10; j < 14; j++) {
                if (i != 4){
                    liquid.ejectCircle(j,i,2000,bufferVolume);
                    liquid.turnValve(4);
                    liquid.draw(bufferVolume);
                    continue;
                }else {
                    liquid.ejectCircle(j,i,2000,bufferVolume);
                }
            }
        }
        liquid.washCircle();
        System.out.println("buffer end");


        //dispense substrateB
        cory = 1;
        corx = 10;
        double subBN = clickBatch.getSubBEq() / clickBatch.getSubAEq() * clickBatch.getSubAN();
        for (ClickPara para:clickBatch.getParameter()) {
            Block substrateB = blockServices.getCoordinate(para.getSubstrateB());
            //volume unit:ul; amount of substrate:umol
            int volumeB = (int) (subBN/substrateB.getBlockConc() * 1000);
            liquid.drawSovlent(substrateB.getAxisX(), substrateB.getAxisY(), 5050,volumeB);
            liquid.ejectCircle(corx++,cory,2000,volumeB,0);
            if (corx == 14){
                corx = 10;
                cory++;
            }
            liquid.washCircle();
        }
        System.out.println("B end");


        //dispense cat
        double catN = clickBatch.getCatEq()/clickBatch.getSubAEq()*clickBatch.getSubAN();
        Block cat = blockServices.getCoordinate(clickBatch.getCat());
        int catVolume = (int) (catN / cat.getBlockConc() * 1000);
        for (int row = 1; row <= 4 ; row++) {
            //draw four solvents
            liquid.drawSovlent(cat.getAxisX(), cat.getAxisY(), 5000,4*catVolume);
            for (int column = 10; column <= 13 ; column++) {
                liquid.ejectCircle(column,row,2000,catVolume,0);
            }
        }
        liquid.washCircle();
        System.out.println("cat end");


        //dispense solvent:port 6
        liquid.prepare(6);
        Block solvent = blockServices.getCoordinate("solvent");
        int solventVolume = (int) (440 - catVolume);
        for (int i = 1; i <= 4 ; i++) {
            for (int j = 10; j < 14; j++) {
                liquid.ejectCircle(j,i,2000, solventVolume);
                liquid.turnValve(6);
                liquid.draw(solventVolume);
            }
        }
        liquid.washCircle();
        System.out.println("dispense end");


        liquid.moveToDes(0,0);
        return "success";
    }

    @Override
    public void doAnalysis(int reactionTime) {
        try {
            final CountDownLatch latch = new CountDownLatch(1);
            final ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(16);
            //wait for reaction ended
            scheduledExecutorService.schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("reaction-ending");
                    boolean move = liquid.moveToDes(0, 0);
                    robot.exeScript("hplc.spf", 30);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                latch.countDown();
                 }
            },reactionTime, TimeUnit.MINUTES);

            latch.await();
            int intervalTime = -1;
            final CountDownLatch latch2 = new CountDownLatch(16);
            for (int i = 1; i < 5; i++) {
                for (int j = 10; j <14; j++) {
                    intervalTime += 1;
                    final int finalI = i;
                    final int finalJ = j;
                    scheduledExecutorService.schedule(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                robot.send_order("status");
                                /*liquid.hplcInject(finalJ, finalI);
                                liquid.hplcClean();*/
                                System.out.print(finalJ);
                                System.out.print(finalI);
                                System.out.println();
                                latch2.countDown();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }, intervalTime, TimeUnit.MINUTES);
                }
            }
            latch2.await();
            scheduledExecutorService.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * scrap the plate of batch2 to Interim station 50 min before batch1-reaction end
     * @param reactionTime batch1 reactionTime
     * @param secondBatchSize the batchSize of batch2
     */
    @Override
    public void doSubTask(int reactionTime,final int secondBatchSize) {
        final CountDownLatch latch = new CountDownLatch(1);
        final ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1);
        //operation of batch 2
        scheduledExecutorService.schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("batch2 start");
                    robot.exeScript("plateback" + robot.getBatchSize() + ".spf", 30);
                    if (robot.ready()){
                        robot.exeScript("plate" + secondBatchSize + ".spf", 30);
                        liquid.moveToDes(0,0);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                latch.countDown();
            }
        },reactionTime - 50, TimeUnit.MINUTES);//50min before batch1 reaction end
        try {
            latch.await();
            scheduledExecutorService.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * multi-thread to do 2 batches simultaneously
     * @param list
     * @return
     */
    @Override
    public String doComplexBatch(List<ClickBatch> list) {
        try {
            ClickBatch clickBatch1 = list.get(0);
            ClickBatch clickBatch2 = list.get(1);
            int secondBatchSize = (robot.getBatchSize() + 1) % 6;

            //batch 1----pre-reaction
            robot.exeScript("plate" + robot.getBatchSize() + ".spf", 30);
            liquid.moveToDes(0,0);
            if (robot.ready()) {
                robot.exeScript("InterimToLiquid.spf", 35);
            }
            if (robot.ready()) {
                //liquid dispensing
                //String mes = this.doDispense(clickBatch1);
                //if (!"success".equals(mes)) System.out.println("error in liquid-dispense");
                System.out.println("batch 1 dispense-end");
                Thread.sleep(10000);
                System.out.println("dispense end");
            }
            robot.exeScript("LiquidToInterim.spf", 32);

            if (robot.ready()) {
                seventhAxis.setPosition((short) 1997);
                seventhAxis.start();
            }
            robot.exeScript("cap" + (robot.getBatchSize() % 3) + ".spf", 35);

            if (robot.ready()) {
                seventhAxis.setPosition((short) 1500);
                seventhAxis.start();
            }

            robot.exeScript("capBatch3.spf", 30);
            if (robot.ready()) {
                heatStirrer.startReaction((int) clickBatch1.getReactTemp(),800);
                //this.doAnalysis(clickBatch1.getReactTime());
                this.doSubTask(clickBatch1.getReactTime(),secondBatchSize);
            }
            //do subTask
            if (robot.ready())robot.exeScript("InterimToLiquid.spf", 35);
            if (robot.ready()){
                //String mes = this.doDispense(clickBatch2);
                //if (!"success".equals(mes)) System.out.println("error in liquid-dispense");
                Thread.sleep(10000);
                System.out.println(" batch2 dispense-end");
                liquid.moveToDes(0,0);
            }
            robot.exeScript("LiquidToInterim.spf", 32);
            //transfer the vial after reaction of batch 1 to liquid-station
            if (robot.ready()) {
                heatStirrer.stopReaction();
                robot.exeScript("hplc.spf", 32);
            }
            /*if (robot.ready()){
                for (int i = 1; i <= 4 ; i++) {
                    for (int j = 10; j < 14 ; j++) {
                        liquid.drawCircle(9,1,8500,300);
                        liquid.ejectQuench(j,i,4000,300);
                        liquid.washCircle();
                    }
                }
            }*/
            final CountDownLatch latch = new CountDownLatch(1);
            if (robot.ready()){
                this.doBinaryBatch(secondBatchSize, (int) clickBatch2.getReactTemp(),latch);
            }
            latch.await();
            robot.exeScript("LiquidToInterim-cap.spf", 30);
            if (robot.ready()) robot.exeScript("plateback" + secondBatchSize + ".spf", 30);
            robot.setBatchSize((secondBatchSize + 1) % 6);
            return "batch end";
        } catch (IOException e) {
            e.printStackTrace();
            return "exception occur";
        } catch (InterruptedException e) {
            e.printStackTrace();
            return "exception occur";
        } catch (ModbusInitException e) {
            e.printStackTrace();
            return "exception occur";
        } catch (ModbusTransportException e) {
            e.printStackTrace();
            return "exception occur";
        } catch (SerialPortException e) {
            e.printStackTrace();
            return "exception occur";
        }
    }

    @Override
    public void doBinaryBatch(final int secondBatchSize, int temp,CountDownLatch latch1) {
        try {
            final CountDownLatch latch = new CountDownLatch(1);
            final ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(17);
            //hplc anylysis of batch 1
            scheduledExecutorService.schedule(new Runnable() {
                @Override
                public void run() {
                    try {
                        final CountDownLatch latch2 = new CountDownLatch(16);
                        //final ScheduledExecutorService scheduledExecutorService2 = new ScheduledThreadPoolExecutor(16);
                        int intervalTime = 23;
                        for (int i = 1; i < 5; i++) {
                            for (int j = 10; j < 14; j++) {
                                intervalTime += 1;
                                final int finalI = i;
                                final int finalJ = j;
                                scheduledExecutorService.schedule(new Runnable() {
                                    @Override
                                    public void run() {
                                        /*liquid.hplcInject(finalJ, finalI);
                                        liquid.hplcClean();*/
                                        System.out.println(System.currentTimeMillis());
                                        System.out.println(" "+finalJ+""+finalI);
                                        latch2.countDown();
                                    }
                                }, intervalTime, TimeUnit.MINUTES);
                            }
                        }
                        for (int i = 1; i <= 4 ; i++) {
                            for (int j = 10; j < 14 ; j++) {
                                liquid.drawCircle(9,2,8500,300);
                                liquid.ejectQuench(j,i,4000,300);
                                liquid.washCircle();
                            }
                        }
                        latch2.await();
                        //scheduledExecutorService2.shutdown();
                        liquid.moveToDes(0,0);
                        robot.exeScript("LiquidToInterim-cap.spf", 30);
                        latch.countDown();
                        } catch (InterruptedException | IOException e) {
                            e.printStackTrace();
                        }
                    }},20,TimeUnit.SECONDS);

            seventhAxis.setPosition((short) 1997);
            seventhAxis.start();
            //capping and reaction of batch 2
            robot.exeScript("cap" + (secondBatchSize % 3) + ".spf", 35);
            System.out.println("second batch cap end");
            if (robot.ready()) {
                //seventh axis returns to 1500 position
                seventhAxis.setPosition((short) 1500);
                seventhAxis.start();
                robot.exeScript("capBatch3.spf", 30);
            }
            if (robot.ready())heatStirrer.startReaction(temp,800);
            System.out.println("second batch reaction start");
            //switch the plate
            robot.exeScript("plateback" + secondBatchSize + ".spf", 30);
            if (robot.ready()) robot.exeScript("plate" + robot.getBatchSize() + ".spf", 30);
            if (robot.ready())robot.exeScript("InterimtoReset.spf", 30);

            if (robot.ready()) {
                latch.await();
                //hplc anylysis of batch 2
                heatStirrer.stopReaction();
                robot.exeScript("hplc.spf", 32);
                /*if (robot.ready()){
                    for (int i = 1; i <= 4 ; i++) {
                        for (int j = 10; j < 14 ; j++) {
                            liquid.drawCircle(9,1,8500,300);
                            liquid.ejectQuench(j,i,4000,300);
                            liquid.washCircle();
                        }
                    }
                }*/
                final CountDownLatch latch3 = new CountDownLatch(16);
                if (robot.ready()) {
                    int intervalTime = -1;
                    for (int i = 1; i < 5; i++) {
                        for (int j = 10; j < 14; j++) {
                            intervalTime += 1;
                            final int finalI = i;
                            final int finalJ = j;
                            scheduledExecutorService.schedule(new Runnable() {
                                @Override
                                public void run() {
                                /*liquid.hplcInject(finalJ, finalI);
                                liquid.hplcClean();*/
                                    System.out.println(System.currentTimeMillis());
                                    System.out.println(" "+finalJ+" "+finalI);
                                    latch3.countDown();
                                }
                            }, intervalTime, TimeUnit.MINUTES);
                        }
                    }
                    for (int i = 1; i <= 4 ; i++) {
                        for (int j = 10; j < 14 ; j++) {
                            liquid.drawCircle(9,2,8500,300);
                            liquid.ejectQuench(j,i,4000,300);
                            liquid.washCircle();
                        }
                    }
                }

                //switch the plate
                robot.exeScript("plateback" + robot.getBatchSize() + ".spf", 30);
                if (robot.ready())robot.exeScript("plate" + secondBatchSize + ".spf", 30);
                if (robot.ready())robot.exeScript("InterimtoReset.spf", 30);
                latch3.await();
                latch1.countDown();
                liquid.moveToDes(0,0);
            }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ModbusInitException e) {
                e.printStackTrace();
            } catch (ModbusTransportException e) {
                e.printStackTrace();
            } catch (SerialPortException e) {
                e.printStackTrace();
            }
    }
}
