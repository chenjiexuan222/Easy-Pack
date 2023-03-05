package com.iiaim.serial;

import jssc.SerialPortException;
import org.springframework.stereotype.Component;

/**
 * @author 陈杰炫
 */

@Component(value = "heatStirrer")
public class HeatStirrer extends BaseSeriport {

    private static String readSpeed = "IN_PV_4 \r \n";
    private static String readOutTemp = "IN_PV_1 \r \n";
    private static String readTemplateTemp = "IN_PV_2 \r \n";
    private static String startHeating = "START_1 \r \n";
    private static String stopHeating = "STOP_1 \r \n";
    private static String startStirring = "START_4 \r \n";
    private static String stopStirring = "STOP_4 \r \n";

    public HeatStirrer(){}

    public HeatStirrer(String portname) throws SerialPortException {
        super("heatStirrer",portname);
    }

    public void startReaction(int temp,int speed) throws SerialPortException{
        this.setSpeed(speed);
        this.setTemp(temp);
        this.startHeating();
        this.startStirring();
        this.startStirring();
   }

   public void stopReaction() throws SerialPortException {
        this.stopHeating();
        this.stopStirring();
   }

    public boolean setTemp(int temperature) throws SerialPortException{
        String tem = String.valueOf(temperature);
        return send_order("OUT_SP_1 " + tem + " \r \n");
    }

    public boolean setSpeed(int speed) throws SerialPortException{
        String spe = String.valueOf(speed);
        return send_order("OUT_SP_4 " + spe + " \r \n");
    }

    public boolean startHeating() throws SerialPortException{
        return send_order(startHeating);
    }

    public boolean stopHeating() throws SerialPortException{
        return send_order(stopHeating);
    }

    public boolean startStirring() throws SerialPortException{
        return send_order(startStirring);
    }

    public boolean stopStirring() throws SerialPortException{
        return send_order(stopStirring);
    }

    public boolean readSpeed() throws SerialPortException{
        return send_order(readSpeed.getBytes());
    }

    public boolean readOutTemp() throws SerialPortException{
        return send_order(readOutTemp);
    }

    public boolean readTemplateTemp() throws SerialPortException{
        return send_order(readTemplateTemp);
    }
}
