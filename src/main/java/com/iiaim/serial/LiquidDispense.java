package com.iiaim.serial;

import com.iiaim.utils.Myutils;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

/**
 * @author 陈杰炫
 */
public class LiquidDispense extends BaseSeriport{
    protected final int timeInterval = 200;
    private static byte[] order = new byte[9];
    private static final byte[] hplcTrigger = new byte[]{(byte) 0xaa, 0x04, 0x00, 0x01, 0x00, 0x01, 0x01, 0x55, 0x06};

    public LiquidDispense() {
    }

    public LiquidDispense(String name, String port) throws SerialPortException {
        super(name, port);
    }

    /**
     * init：points to (1,1)
     */
    public void init() {
        if (this.send_order(3, 100, 10000, 0));
        if (this.send_order(1, 100, 32000, 0));
        if (this.send_order(2, 100, 30000, 0));
        if (this.send_order(1, 100, 180, 1));
        if (this.send_order(2, 100, 1780, 1)) ;
    }

    /**
     * send order to control MCU through serial
     * @param fun
     * @param speed
     * @param pulse
     * @param dir
     * @return true if success communicate
     */
    public boolean send_order(int fun, int speed, int pulse, int dir) {
        try {
            orderShape(fun, speed, pulse, dir); //revise the member varirable order
            this.send_order(order);
            Thread.sleep(timeInterval);
            byte[] response = this.rec_data_bytes(2,20000);//S,E ---> 83 69
            /*
            String response = this.rec_data();
            long startTime = System.currentTimeMillis();
            while (response == null || response.charAt(response.length() - 1) != 'E'){
                if (System.currentTimeMillis()-startTime > 20000)return false;
                response += this.rec_data();
            }
             */
            return (response[1] == 69);
        } catch (SerialPortException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException | SerialPortTimeoutException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * transform the input parameter into correct order
     * @param fun function mode:12345
     * @param speed motorSpeed,2bytes
     * @param pulse motorPulse,2bytes
     * @param dir motorDirection
     * @return order(byte[])
     */
    public void orderShape(int fun, int speed, int pulse, int dir) {
        byte[] tmp;
        order[0] = (byte) 0xaa;//head
        order[1] = (byte) fun;
        tmp = Myutils.intToByte(speed);
        order[2] = tmp[2];
        order[3] = tmp[3];
        tmp = Myutils.intToByte(pulse);
        order[4] = tmp[2];
        order[5] = tmp[3];
        order[6] = (byte) dir;
        order[7] = 0x55; //end
        order[8] = Myutils.sumCheck2(order, order.length - 1);//check，ADD8
    }

    /**
     * trigger the hplc to run the sequence
     * @return
     */
    public boolean hplcTrigger(){
        try {
            this.send_order(hplcTrigger);
            Thread.sleep(timeInterval);
            byte[] response = this.rec_data_bytes(2,5000);
            return (response[1] == 69);
        } catch (SerialPortException e) {
            e.printStackTrace();
            return false;
        } catch (SerialPortTimeoutException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * trigger the peristaltic pump to wash the needle
     * @return
     */
    public boolean periPumpTrigger(){
        return this.send_order(5, 1, 1, 1);
    }
}

