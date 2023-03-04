package com.iiaim.serial;

import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

/**
 * @author 陈杰炫
 *  * protocol：head addr function params  tail
 *  * --------------------------------------------
 *  *        5a a5   ff   1byte 2bytes a5 5a
 */
public class HplcValve extends BaseSeriport {

    private static byte[] order = new byte[8];
    private static byte[] response = new byte[8];
    private static final byte[] turnToB = new byte[]{(byte) 0x5a, (byte) 0xa5, (byte) 0xff, 0x03, 0x02, 0x00, (byte) 0xa5, (byte) 0x5a};
    private static final byte[] turnToA = new byte[]{(byte) 0x5a, (byte) 0xa5, (byte) 0xff, 0x03, 0x01, 0x00, (byte) 0xa5, (byte) 0x5a};
    private static final byte[] turnToC = new byte[]{(byte) 0x5a, (byte) 0xa5, (byte) 0xff, 0x03, 0x03, 0x00, (byte) 0xa5, (byte) 0x5a};
    private static final byte[] vavleStatus = new byte[]{(byte) 0x5a, (byte) 0xa5, (byte) 0xff, 0x01, 0x00, 0x00, (byte) 0xa5, (byte) 0x5a};

    public HplcValve(String portname){
        super("HplcValve", portname);
    }

    public String switchToA() {
        try {
            String s = this.vavleStatus();
            if ("A".equals(s)) return "already at A";
            else if ("error".equals(s)) return "error";
            else {
                send_order(turnToA);
                Thread.sleep(timeInterval);
                //response = this.rec_data_bytes();
                byte[] response = this.rec_data_bytes(16,3000);
                if (response[11] == 3 && response[12] == 1 && response[13] == 0) return "success";
            }return "fail";
        } catch (SerialPortException e) {
            e.printStackTrace();
            return "port error";
        }catch (InterruptedException | SerialPortTimeoutException e) {
            e.printStackTrace();
            return "port error";
        }
    }

    public String switchToB() {
        try {
            String s = this.vavleStatus();
            if ("B".equals(s)) return "already at B";
            else if ("error".equals(s)) return "error";
            else {
                send_order(turnToB);
                Thread.sleep(timeInterval);
                //response = this.rec_data_bytes();
                byte[] response = this.rec_data_bytes(16,3000);
                if (response[11] == 3 && response[12] == 2 && response[13] == 0) return "success";
            }return "fail";
        } catch (SerialPortException e) {
            e.printStackTrace();
            return "port error";
        }catch (InterruptedException | SerialPortTimeoutException e) {
            e.printStackTrace();
            return "port error";
        }
    }

    public String switchToC() {
        try {
            String s = this.vavleStatus();
            if ("C".equals(s)) return "already at C";
            else if ("error".equals(s)) return "error";
            else {
                send_order(turnToC);
                Thread.sleep(timeInterval);
                //response = this.rec_data_bytes();
                byte[] response = this.rec_data_bytes(16,3000);
                if (response[11] == 3 && response[12] == 3 && response[13] == 0) return "success";
            }return "fail";
        } catch (SerialPortException e) {
            e.printStackTrace();
            return "port error";
        } catch (InterruptedException | SerialPortTimeoutException e) {
            e.printStackTrace();
            return "port error";
        }
    }

    /**
     * query for status of valve
     * @return
     */
    public String vavleStatus(){
            try {
                send_order(vavleStatus);
                Thread.sleep(timeInterval);
                byte[] response = this.rec_data_bytes(8,2000);
                if (response[3] == 01) {
                    if (response[4] == 01) return "A";
                    if (response[4] == 02) return "B";
                    if (response[4] == 03) return "C";
                }
                return "error";
            } catch (SerialPortException e) {
                e.printStackTrace();
                return "error";
            } catch (InterruptedException | SerialPortTimeoutException e) {
                e.printStackTrace();
                return "port error";
            }
    }
    }
