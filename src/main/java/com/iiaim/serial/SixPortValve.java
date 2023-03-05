package com.iiaim.serial;

import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

/**
 * @author 陈杰炫
 * protocol：head addr function params  tail
 * --------------------------------------------
 *          cc   00   1byte  2bytes dd 2bytes
 */
public class SixPortValve extends BaseSeriport {

    private static byte[] order = new byte[8];
    private static byte[] response = new byte[8];
    private static final byte[] valveState = new byte[]{(byte) 0xcc, 0x00, (byte) 0xae, 0x00, 0x00, (byte) 0xdd, 0x57, 0x02};
    private static final byte[] pumpStatus = new byte[]{(byte) 0xcc, 0x00, 0x66, 0x00, 0x00, (byte) 0xdd, 0x0f, 0x02};
    private static final byte[] valveReset = new byte[]{(byte) 0xcc, 0x00, (byte) 0x4c, 0x00, 0x00, (byte) 0xdd, (byte) 0xf5, 0x01};
    private static final byte[] pumpReset = new byte[]{(byte) 0xcc, 0x00, 0x45, 0x00, 0x00, (byte) 0xdd, (byte) 0xee, 0x01};
    private static final byte[] stop = new byte[]{(byte) 0xcc, 0x00, 0x49, 0x00, 0x00, (byte) 0xdd, (byte) 0xf2, 0x01};
    private static final byte[] highSpeed = new byte[]{(byte) 0xcc, 0x00, (byte) 0x4b, (byte) 0xc8, 0x00, (byte)0xdd, (byte) 0xbc,0x02};
    private static final byte[] lowSpeed = new byte[]{(byte) 0xcc, 0x00, (byte) 0x4b, 0x23, 0x00, (byte)0xdd,0x17,0x02};


    public SixPortValve(String portname){
        super("SixPortValve", portname);
    }

    /**
     * init the syringe pump
     * @return
     */
    public boolean init(){
        if (!this.valveReset())return false;
        if (!this.pumpReset())return false;
        return true;
    }

    /**
     * switch the port
     * @param portNum
     * @return
     */
    public boolean turnValve(int portNum){
        return this.sendOrder(0, (byte) 0x44, portNum);
    }

    /**
     * eject solution
     * @param volume the volume unit:ul
     * @return
     */
    public boolean eject(int volume){
        try {
            int data;
            data = (int) Math.round(volume/0.3333);
            Boolean mes = this.sendOrder(0, (byte) 0x42, data);
            Thread.sleep(500);
            return mes;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * draw solution(about 1.5s wait process after draw)
     * @param volume the volume unit:ul
     * @return
     */
    public boolean draw(int volume){
        try {
            int data;
            data = (int) Math.round(volume/0.3333);
            Boolean mes = this.sendOrder(0, (byte) 0x43, data);
            Thread.sleep(1550);
            return mes;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean setHighSpeed(){
        try{
            this.send_order(highSpeed);
            Thread.sleep(timeInterval);
            byte[] response = this.rec_data_bytes(8,6000);
            System.out.println(response.length);
            return response[2] == 0x00;
        } catch (SerialPortTimeoutException e) {
            e.printStackTrace();
            return false;
        } catch (SerialPortException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean setLowSpeed(){
        try{
            this.send_order(lowSpeed);
            Thread.sleep(timeInterval);
            byte[] response = this.rec_data_bytes(8,6000);
            System.out.println(response.length);
            return response[2] == 0x00;
        } catch (SerialPortTimeoutException e) {
            e.printStackTrace();
            return false;
        } catch (SerialPortException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * send order through serial
     * @param addr
     * @param fun
     * @param data
     * @return
     */
    public boolean sendOrder(int addr,byte fun,int data){
        order[0] = (byte) 0xcc;
        order[1] = (byte) addr;
        order[2] = fun;
        order[3] = (byte)(data & 0xFF);
        order[4] = (byte)((data>>8) & 0xFF);
        order[5] = (byte) 0xdd;
        byte[] bytes = sumCheck16(order);
        try {
            this.send_order(bytes);
            Thread.sleep(timeInterval);
            byte[] response = this.rec_data_bytes(8,6000);
            System.out.println(response.length);
            return response[2] == 0x00;
        } catch (SerialPortException e) {
            e.printStackTrace();
            return false;
        }catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        } catch (SerialPortTimeoutException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * valve reset
     * @return
     */
    public boolean valveReset(){
        try{
            this.send_order(valveReset);
            Thread.sleep(timeInterval);
            byte[] response = this.rec_data_bytes(8,6000);
            return response[2] == 0x00;
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
     * syringe pump reset
     * @return
     */
    public boolean pumpReset() {
        try {
            this.send_order(pumpReset);
            Thread.sleep(timeInterval);
            byte[] response = this.rec_data_bytes(8,30000);
            return response[2] == 0x00;
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
     * read pump state
     * @return
     */
    public String valveState(){
        try {
            if (send_order(valveState)) {
                response = this.rec_data_bytes();
                byte low = response[3];
                int valveState = low & 0xff;
                return String.valueOf(valveState);
            }return "";
        } catch (SerialPortException e) {
            e.printStackTrace();
            return "port error";
        }
    }

    /**
     * read pump status
     * @return the left volume of liquids(unit:ul)
     */
    public String pumpStatus(){
        try {
            if (send_order(pumpStatus)) {
                response = this.rec_data_bytes();
                byte high = response[4];
                byte low = response[3];
                int pumpStatus = (high<<8) & 0xff + low & 0xff;
                pumpStatus *= 0.3333;
                return String.valueOf(pumpStatus);
            }return "";
        } catch (SerialPortException e) {
            e.printStackTrace();
            return "port error";
        }
    }

    /**
     * stop in emergency
     * @return
     */
    public boolean stop() throws SerialPortException {
        return send_order(stop);
    }

    /**
     * calculate the sum check:low is front
     * @param order
     * @return
     */
    private byte[] sumCheck16(byte[] order) {
        int sum = 0;
        int len = order.length;
        for (int i = 0; i < len - 2; i++) {
            sum += order[i] & 0xff; //convert to int
        }
        byte low = (byte) (sum & 0xff);//low
        byte high = (byte) ((sum >> 8) & 0xFF);//high
        order[len - 1] = high;
        order[len - 2] = low;
        return order;
    }
}
