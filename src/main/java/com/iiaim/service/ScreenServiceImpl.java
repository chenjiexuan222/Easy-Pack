package com.iiaim.service;

import com.iiaim.pojo.Block;
import com.iiaim.pojo.ScreenBatch;
import com.iiaim.pojo.ScreenPara;
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
@Service("screenServiceImpl")
public class ScreenServiceImpl implements ScreenService{

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
    public  String doScreen(ScreenBatch screenBatch) {
       try {
            //0:robot is free now
            if (robot.getFlag() == 0) {
                robot.setFlag(1);
                robot.exeScript("plate" + robot.getBatchSize() + ".spf", 30);
                liquid.moveToDes(0,0);
                if (robot.ready()) {
                    robot.exeScript("InterimToLiquid.spf", 35);
                }
                if (robot.ready()) {
                    //liquid-dispensing process
                    String mes = this.doDispense(screenBatch);
                    if (!"success".equals(mes)) System.out.println("error in dispensing");
                    System.out.println(" dispense end");
                }
                liquid.moveToDes(0, 0);
                robot.exeScript("LiquidToInterim.spf", 32);

                if (robot.ready()) {
                    //move robot axis
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
                    heatStirrer.startReaction((int) screenBatch.getReactTemp(),1000);
                    heatStirrer.setSpeed(800);
                    Thread.sleep(500);
                    heatStirrer.startStirring();
                    this.doTimeScr(screenBatch);
                    liquid.moveToDes(0,0);
                }
                liquid.moveToDes(0, 0);
                robot.exeScript("LiquidToInterim-cap.spf", 30);
                if (robot.ready()) robot.exeScript("plateback" + robot.getBatchSize() + ".spf", 30);
                robot.setBatchSize((robot.getBatchSize() + 1) % 6);
                robot.setFlag(0);
                return "batch end";
            }else {
                return "script is running now";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "exception occur";
        } catch (InterruptedException e) {
            e.printStackTrace();
            return "exception occur";
        } catch (SerialPortException e) {
            e.printStackTrace();
            return "serialException occur";
        } catch (ModbusInitException e) {
            e.printStackTrace();
            return "exception occur";
        } catch (ModbusTransportException e) {
            e.printStackTrace();
            return "exception occur";
        }
    }

    /**
     * various kinds of substrates and cats solution dispensing
     * @param screenBatch
     * @return
     */
    @Override
    public String doDispense(ScreenBatch screenBatch) {
        //dispense substrateA
        Block substrateA = blockServices.getCoordinate(screenBatch.getSubstrateA());
        //volume unit:ul; amount of substrate:umol
        int volumeA = (int) (screenBatch.getSubAN()/substrateA.getBlockConc() * 1000);
        // one circle ---> two vials
        for (int row = 1; row <= 4 ; row++) {
            //draw double substrateA
            liquid.drawScreen(substrateA.getAxisX(), substrateA.getAxisY(), 8500,volumeA);
            for (int column = 10; column <=11 ; column++) {
                liquid.ejectCircle(column,row,2000,volumeA,0);
                if (column == 10){
                    liquid.turnValve(5);
                    liquid.draw(volumeA);
                }
            }
            //reaction screen procedure - liquid dispensing
            liquid.drawScreen(substrateA.getAxisX(), substrateA.getAxisY(), 8500,volumeA);
            for (int column = 12; column <=13 ; column++) {
                liquid.ejectCircle(column,row,2000,volumeA,0);
                if (column == 12){
                    liquid.turnValve(5);
                    liquid.draw(volumeA);
                }
            }
        }
        liquid.washCircle();

        //dispense substrateB
        Block substrateB = blockServices.getCoordinate(screenBatch.getSubstrateB());
        double subBN = screenBatch.getSubBEq() / screenBatch.getSubAEq() * screenBatch.getSubAN();
        int volumeB = (int) (subBN/substrateB.getBlockConc() * 1000);
        for (int row = 1; row <= 4 ; row++) {
            //draw double substrateB
            liquid.drawScreen(substrateB.getAxisX(), substrateB.getAxisY(), 8500,volumeB);
            for (int column = 10; column <=11 ; column++) {
                liquid.ejectCircle(column,row,2000,volumeB,0);
                if (column == 10){
                    liquid.turnValve(5);
                    liquid.draw(volumeB);
                }
            }

            liquid.drawScreen(substrateB.getAxisX(), substrateB.getAxisY(), 8500,volumeB);
            for (int column = 12; column <=13 ; column++) {
                liquid.ejectCircle(column,row,2000,volumeB,0);
                if (column == 12){
                    liquid.turnValve(5);
                    liquid.draw(volumeB);
                }
            }
        }
        liquid.washCircle();

        //dispense buffer:port 4
        liquid.prepare(4);
        int bufferVolume = screenBatch.getBufferVolume();
        for (int i = 1; i <= 4 ; i++) {
            for (int j = 10; j < 14; j++) {
                if (i != 4){
                    liquid.ejectCircle(j,i,2000,bufferVolume);
                    liquid.turnValve(4);
                    liquid.draw(bufferVolume);
                    continue;
                }else {
                    if (j == 10){
                        liquid.ejectCircle(j,i,2000,bufferVolume);
                        continue;
                    }
                    else if (j == 11){
                        liquid.ejectScreen(j,i,2000,bufferVolume,1000);
                        //continue;
                    }else {
                        liquid.turnValve(5);
                        liquid.draw(bufferVolume);
                        liquid.ejectCircle(j,i,2000,bufferVolume);
                    }
                }
            }
        }
        liquid.washCircle();

        //dispense cat
        int cory = 1, corx = 10, flag = 0;
        int[] volumeSov = new int[16];//solvent volume array:log the volume of pure solvent
        //catalyst is same for all vials in reaction screening
        Block cat = blockServices.getCoordinate(screenBatch.getParameter().get(0).getCat());
        for (ScreenPara para: screenBatch.getParameter()) {
            double catN = para.getCatEq() / screenBatch.getSubAEq() * screenBatch.getSubAN();
            //Block cat = blockServices.getCoordinate(para.getCat());
            double catVolume = catN / cat.getBlockConc() * 1000;
            volumeSov[flag++] = (int) (1020 - catVolume);
            liquid.drawCircle(cat.getAxisX(), cat.getAxisY(), 8500, (int) catVolume);
            liquid.ejectCircle(corx++,cory,2000, (int) catVolume);
            if (corx == 14) {
                corx = 10;
                cory++;
            }
        }
        liquid.washCircle();

        //dispense DMSO/H2O pure solvent:port 6
        Block solvent = blockServices.getCoordinate("solvent");
        liquid.prepare(6);
        int flag2 = 0;
        for (int i = 1; i <= 4 ; i++) {
            for (int j = 10; j < 14 ; j++) {
                int volume = volumeSov[flag2++];
                if (volume > 1000)volume = 1000;
                liquid.ejectCircle(j,i,2000,volume);
                liquid.turnValve(6);
                liquid.draw(volume);
            }
        }
        liquid.washCircle();
        liquid.moveToDes(0,0);
        return "success";
    }

    /**
     * used to complete the delayed task such as reaction time screening
     * @param screenBatch
     * @throws InterruptedException
     */
    @Override
    public void doTimeScr(ScreenBatch screenBatch) throws InterruptedException {
        //get different reaction times in screen process
        int[] ints = new int[4];
        final int[] quenchVolume = new int[16];
        int index = 0,index1 = 0;
        for (ScreenPara para: screenBatch.getParameter()) {
            if (index == 0 || para.getReactTime() != ints[index - 1]) ints[index++] = para.getReactTime();
            double catN = para.getCatEq() / screenBatch.getSubAEq() * screenBatch.getSubAN();
            Block cat = blockServices.getCoordinate(para.getCat());
            double catVolume = catN / cat.getBlockConc() * 1000;
            //double concentration against catalyst
            quenchVolume[index1++] = (int) catVolume / 2;
        }
        index = 0;
        //counter:decrease to 0 represents other await process could go on
        final CountDownLatch latch = new CountDownLatch(4);
        final ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(4);
        //first batch of vial
        scheduledExecutorService.schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("first hplc");
                    liquid.moveToDes(0, 0);
                    robot.exeScript("hplc1.spf", 30);
                    if (robot.ready()){
                        int index = 0;
                        for (int i = 10; i < 14 ; i++) {
                            liquid.drawCircle(9,1,8500,quenchVolume[index]);
                            liquid.ejectQuench(i,1,4000,quenchVolume[index++]);
                            liquid.washCircle();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                latch.countDown();
            }
        },ints[index++], TimeUnit.MINUTES);

        //second batch of vial
        scheduledExecutorService.schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("second hplc");
                    liquid.moveToDes(0,0);
                    robot.exeScript("hplc2.spf", 30);
                    if (robot.ready()){
                        ScheduledExecutorService scheduledExecutorService2 = new ScheduledThreadPoolExecutor(8);
                        int timeInterval = -2;
                        for (int i = 1; i < 3; i++) {
                            for (int j = 10; j < 14; j++) {
                                final int x = j;
                                final int y = i;
                                //hplc timeinterval
                                timeInterval+=12;
                                scheduledExecutorService2.schedule(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            robot.send_order("status");
                                            liquid.hplcInject(x,y);
                                            liquid.hplcClean();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                },timeInterval,TimeUnit.MINUTES);
                            }
                        }
                        int index = 4;
                        for (int i = 10; i < 14 ; i++) {
                            liquid.drawCircle(9,1,8500,quenchVolume[index]);
                            liquid.ejectQuench(i,2,4000,quenchVolume[index++]);
                            liquid.washCircle();
                        }
                        scheduledExecutorService2.shutdown();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                latch.countDown();
            }
        },ints[index++], TimeUnit.MINUTES);

        //third batch of vial
        scheduledExecutorService.schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("third hplc");
                    boolean move = liquid.moveToDes(0, 0);
                    if (move)robot.exeScript("hplc3.spf", 30);
                    if (robot.ready()){
                        ScheduledExecutorService scheduledExecutorService3 = new ScheduledThreadPoolExecutor(4);
                        int timeInterval = -2;
                        for (int i = 10; i < 14; i++) {
                            final int x = i;
                            timeInterval+=12;
                            scheduledExecutorService3.schedule(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        robot.send_order("status");
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    liquid.hplcInject(x,3);
                                    liquid.hplcClean();
                                }
                            },timeInterval,TimeUnit.MINUTES);
                        }
                        int index = 8;
                        for (int i = 10; i < 14 ; i++) {
                            liquid.drawCircle(9,1,8500,quenchVolume[index]);
                            liquid.ejectQuench(i,3,4000,quenchVolume[index++]);
                            liquid.washCircle();
                        }
                        scheduledExecutorService3.shutdown();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                latch.countDown();
            }
        },ints[index++], TimeUnit.MINUTES);

        //forth batch of vial
        scheduledExecutorService.schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("forth hplc");
                    liquid.moveToDes(0,0);
                    //heatStirrer.stopReaction();
                    heatStirrer.stopStirring();
                    robot.exeScript("hplc4.spf", 30);
                    if (robot.ready()){
                        final CountDownLatch latch2 = new CountDownLatch(4);
                        ScheduledExecutorService scheduledExecutorService4 = new ScheduledThreadPoolExecutor(4);
                        int timeInterval = -2;
                        for (int i = 10; i < 14; i++) {
                            final int x = i;
                            timeInterval+=12;
                            scheduledExecutorService4.schedule(new Runnable() {
                                @Override
                                public void run() {
                                    liquid.hplcInject(x, 4);
                                    liquid.hplcClean();
                                    latch2.countDown();
                                }
                            },timeInterval,TimeUnit.MINUTES);
                        }
                        int index = 12;
                        for (int i = 10; i < 14 ; i++) {
                            liquid.drawCircle(9,1,8500,quenchVolume[index]);
                            liquid.ejectQuench(i,4,4000,quenchVolume[index++]);
                            liquid.washCircle();
                        }
                        latch2.await();
                        scheduledExecutorService4.shutdown();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (SerialPortException e) {
                    e.printStackTrace();
                }
                latch.countDown();
                //scheduledExecutorService.shutdown();
            }
        },ints[index++], TimeUnit.MINUTES);

        latch.await();
        scheduledExecutorService.shutdown();
    }
}
