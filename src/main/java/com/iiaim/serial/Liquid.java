package com.iiaim.serial;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author 陈杰炫
 * consist of three components:liquiddispense,sixportvalve,hplcvalve
 */
@Component(value = "liquid")
public class Liquid {
    @Autowired
    private LiquidDispense liquidDispense;
    @Autowired
    private SixPortValve sixPortValve;
    @Autowired
    private HplcValve hplcValve;

    private int startX = 1,startY = 1;
    private int distanceX,distanceY;//the current position
    private final int speedX = 86;
    private final int speedY = 104;
    private int pulse;


    @PostConstruct
    public void init(){
        liquidDispense.init();
        sixPortValve.init();
        hplcValve.switchToB();
    }

    public void drawCircle(int coordinatex,int coordinatey,int pulseZ,int volume){
        this.moveToDes(coordinatex,coordinatey,pulseZ);
        sixPortValve.turnValve(3);
        sixPortValve.draw(volume);
        liquidDispense.send_order(3, speedX, 800, 0);
        liquidDispense.send_order(3, speedX, pulseZ + 500, 0);
    }

    public void ejectCircle(int coordinatex,int coordinatey,int pulseZ,int volume){
        try {
            this.moveToDes(coordinatex,coordinatey,pulseZ);
            sixPortValve.turnValve(3);
            sixPortValve.eject(volume);
            Thread.sleep(1000);
            liquidDispense.send_order(3, speedY, pulseZ + 1000, 0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void ejectCircle(int coordinatex,int coordinatey,int pulseZ,int volume,int number){
        try {
            this.moveToDes(coordinatex,coordinatey,pulseZ);
            sixPortValve.turnValve(3);
            sixPortValve.eject(volume);
            Thread.sleep(2100);
            liquidDispense.send_order(3, speedY, pulseZ + 1000, 0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * wash procedure of liquid-station:move to waste-station - wash the tube - wash the needle inside and outside
     */
    public void washCircle() {
        try {
            this.moveToDes(14,1,6000);//move to waste
            sixPortValve.turnValve(3);
            sixPortValve.pumpReset();
            sixPortValve.turnValve(1);//turn valve to wash
            sixPortValve.draw(1000);
            sixPortValve.turnValve(3);
            sixPortValve.eject(1000);
            sixPortValve.turnValve(5);
            sixPortValve.draw(1000);
            sixPortValve.turnValve(3);
            sixPortValve.eject(1000);
            sixPortValve.turnValve(5);//second cycle
            sixPortValve.draw(1000);
            sixPortValve.turnValve(3);
            sixPortValve.eject(1000);
            sixPortValve.turnValve(5);//third cycle
            sixPortValve.draw(1000);
            sixPortValve.turnValve(3);
            sixPortValve.eject(1000);
            liquidDispense.periPumpTrigger();//wash needle
            Thread.sleep(1000);
            liquidDispense.send_order(3, speedX,7000, 0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * dispense the common-solution in reaction screening:draw the double reaction volume of solution;
     * it can allow the liquid-station to dispense liquids to two vials in one draw-dispense process
     * @param coordinatex coordinate of X axis of solution
     * @param coordinatey coordinate of Y axis of solution
     * @param pulseZ equals to drop height of Z axis
     */
    public void drawScreen(int coordinatex,int coordinatey,int pulseZ,int volume){
        this.moveToDes(coordinatex,coordinatey,pulseZ);
        sixPortValve.turnValve(3);
        sixPortValve.draw(volume);
        sixPortValve.turnValve(2);// air/waste
        sixPortValve.pumpReset();
        sixPortValve.turnValve(3);
        sixPortValve.draw(volume);
        /*sixPortValve.turnValve(2);//waste
        sixPortValve.pumpReset();
        sixPortValve.turnValve(3);
        sixPortValve.draw(900);//2550ul total*/
        liquidDispense.send_order(3, speedX, 800, 0);
        liquidDispense.send_order(3, speedX, pulseZ + 500, 0);//Z axis reset
    }


    /**
     * draw solution(whatever substrate or reaction-mixture)
     * @param coordinatex
     * @param coordinatey
     * @param pulseZ
     * @param volume the draw volume
     */
    public void drawSovlent(int coordinatex,int coordinatey,int pulseZ,int volume){
        this.moveToDes(coordinatex,coordinatey,pulseZ);
        sixPortValve.turnValve(3);
        sixPortValve.draw(volume);
        liquidDispense.send_order(3, speedX, 800, 0);
        liquidDispense.send_order(3, speedX, pulseZ + 500, 0);//Z axis reset
    }

    /**
     * used to run out of solution in one-row dispensing solution:the second vial need to dispense twice(due to the limit of syringe pump)
     * @param coordinatex
     * @param coordinatey
     * @param pulseZ
     * @param volume the volume should be dropped
     * @param pumpVolume syringe pump volume when finish draw solutions
     */
    public void ejectScreen(int coordinatex,int coordinatey,int pulseZ,int volume,int pumpVolume){
        try {
            this.moveToDes(coordinatex,coordinatey,pulseZ);
            sixPortValve.pumpReset();
            sixPortValve.turnValve(5);
            sixPortValve.draw(volume - (pumpVolume - volume));
            sixPortValve.turnValve(3);
            sixPortValve.pumpReset();
            Thread.sleep(1500);
            liquidDispense.send_order(3, speedX, pulseZ + 1000, 0);//Z axis reset
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    /**
     * draw the solution which directly connected to syringe pump equipped with six-port valve
     * @param port the port number in valve which solution connected
     */
    public void prepare(int port){
        liquidDispense.send_order(3, speedX,5500, 1);//drop the needle above waste-station
        sixPortValve.turnValve(port);
        sixPortValve.draw(1000);
        sixPortValve.turnValve(3);
        sixPortValve.eject(1000);
        sixPortValve.turnValve(port);
        sixPortValve.draw(1000);
        sixPortValve.turnValve(3);
        sixPortValve.eject(1000);
        sixPortValve.turnValve(port);
        sixPortValve.draw(1000);
        sixPortValve.turnValve(3);
        sixPortValve.eject(1000);
        sixPortValve.turnValve(port);
        sixPortValve.draw(1000);
        sixPortValve.turnValve(3);
        liquidDispense.send_order(3, speedX,6500, 0);//Z axis reset
    }

    /**
     * involve move to destination -- draw reaction mixture -- inject mixture into sample loop -- trigger the hplc analysis
     * @param coordinatex
     * @param coordinatey
     */
    public void hplcInject(int coordinatex,int coordinatey){
        try {
            this.moveToDes(coordinatex,coordinatey,4200);//move
            sixPortValve.turnValve(3);
            sixPortValve.draw(900);//draw the mixture
            Thread.sleep(800);
            liquidDispense.send_order(3, speedX, 1800, 0);
            sixPortValve.draw(100);
            sixPortValve.turnValve(2);//pump air through 2
            sixPortValve.pumpReset();
            sixPortValve.turnValve(3);
            sixPortValve.draw(1000);
            sixPortValve.turnValve(2);//waste
            sixPortValve.pumpReset();
            sixPortValve.turnValve(3);
            sixPortValve.draw(100);
            sixPortValve.turnValve(2);//waste
            sixPortValve.pumpReset();

            //clean the tube and make the tube between hplc and valve full of methanol
            sixPortValve.turnValve(1);
            sixPortValve.draw(1000);
            sixPortValve.turnValve(2);
            sixPortValve.eject(1000);

            sixPortValve.turnValve(3);
            sixPortValve.draw(480);
            sixPortValve.turnValve(2);
            hplcValve.switchToA();//hplcValve - injection state
            sixPortValve.setLowSpeed();
            sixPortValve.pumpReset();
            Thread.sleep(500);
            hplcValve.switchToB();//hplcValve - sampling state
            liquidDispense.hplcTrigger();// trigger the hplc
            sixPortValve.setHighSpeed();
            liquidDispense.send_order(3, speedX, 4000, 0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * clean the tube after hplc-injection
     */
    public void hplcClean(){
        sixPortValve.turnValve(2);//waste and clean the loop between hplcvalve and pump
        sixPortValve.pumpReset();
        sixPortValve.turnValve(1);
        sixPortValve.draw(500);
        sixPortValve.turnValve(2);
        sixPortValve.eject(500);
        //once again
        sixPortValve.turnValve(1);
        sixPortValve.draw(500);
        sixPortValve.turnValve(2);
        sixPortValve.eject(500);
        this.washCircle();
    }

    public void turnValve(int portNum){
        this.sixPortValve.turnValve(portNum);
    }

    public void draw(int volume){
        this.sixPortValve.draw(volume);
    }

    public void eject(int volume){
        this.sixPortValve.eject(volume);
    }

    public void ejectQuench(int coordinatex,int coordinatey,int pulseZ,int volume){
        try {
            this.moveToDes(coordinatex,coordinatey,pulseZ);
            sixPortValve.turnValve(3);
            sixPortValve.eject(volume);
            Thread.sleep(2000);
            sixPortValve.draw(100);
            sixPortValve.eject(100);
            liquidDispense.send_order(3, speedY, 1800, 0);
            sixPortValve.draw(100);
            sixPortValve.eject(100);
            Thread.sleep(2000);
            liquidDispense.send_order(3, speedY, pulseZ + 1000, 0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * calculate the absolute distance and move to destination
     * @param coordinatex
     * @param coordinatey
     */
    public boolean moveToDes(int coordinatex,int coordinatey){
        try {
            if (coordinatex == startX && coordinatey == startY)return true;

            int[] des = this.distance(coordinatex, coordinatey);
            if (des == null)return false;

            int spaceX = des[0] - distanceX;
            pulse = Math.abs(spaceX) *100;
            if (spaceX < 0){
                if (!liquidDispense.send_order(1, speedX, pulse, 0));
            }else if (spaceX > 0){
                if (!liquidDispense.send_order(1, speedX, pulse, 1));
            }
            Thread.sleep(200);
            int spaceY = des[1] - distanceY;
            pulse = Math.abs(spaceY) *100;
            if (spaceY < 0){
                if (!liquidDispense.send_order(2, speedY, pulse, 0));
            }else if (spaceY > 0){
                if (!liquidDispense.send_order(2, speedY, pulse, 1));
            }
            startX = coordinatex;//coordinate transfer
            startY = coordinatey;
            distanceX = des[0];//diatance transfer
            distanceY = des[1];
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * calculate the absolute distance - move to destination - drop the needle
     * @param coordinatex
     * @param coordinatey
     * @param pulseZ
     */
    public void moveToDes(int coordinatex,int coordinatey,int pulseZ){
        try {
            if (coordinatex == startX && coordinatey == startY)return;

            int[] des = this.distance(coordinatex, coordinatey);
            if (des == null)return;

            int spaceX = des[0] - distanceX;
            pulse = Math.abs(spaceX) *100;
            if (spaceX < 0){
                if (!liquidDispense.send_order(1, speedX, pulse, 0));
            }else if (spaceX > 0){
                if (!liquidDispense.send_order(1, speedX, pulse, 1));
            }
            Thread.sleep(200);
            int spaceY = des[1] - distanceY;
            pulse = Math.abs(spaceY) *100;
            if (spaceY < 0){
                if (!liquidDispense.send_order(2, speedY, pulse, 0));
            }else if (spaceY > 0){
                if (!liquidDispense.send_order(2, speedY, pulse, 1));
            }
            liquidDispense.send_order(3, speedX, pulseZ, 1);
            startX = coordinatex;//coordinate transfer
            startY = coordinatey;
            distanceX = des[0];//diatance transfer
            distanceY = des[1];
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * calculate the absolute distance to (1,1)
     * @param coordinatex
     * @param coordinatey
     * @return
     */
    public int[] distance(int coordinatex,int coordinatey){
        int[] distance = new int[2];
        int disX,disY;
        if (coordinatex >= 1 && coordinatex <= 8){
            disX = (coordinatex - 1) * 20;
            disY = (coordinatey - 1) * 20;
        }else if (coordinatex == 9){
            disX = 171;
            disY = 4 + (coordinatey - 1) * 32;
        }else if (coordinatex >= 10 && coordinatex <= 13) {
            disX = 210 + (coordinatex - 10) * 27;
            disY = 5 + (coordinatey - 1) * 27;
        }else if (coordinatex == 14){//move to waste station
            disX = 327;
            disY = distanceY;
        }else if (coordinatex == 0 && coordinatey == 0){ //(0,0) represents move to specific position that interact with robot
            disX = 100;//move Z axis to middle
            disY = 200;
        }else {
            return null;
        }
        distance[0] = disX;
        distance[1] = disY;
        return distance;
    }

}
