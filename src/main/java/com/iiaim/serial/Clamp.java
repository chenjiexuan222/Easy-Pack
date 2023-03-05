package com.iiaim.serial;

import com.iiaim.utils.Myutils;
import jssc.SerialPort;
import jssc.SerialPortException;

/**
 * @author 陈杰炫
 */
public class Clamp extends BaseSeriport {
    private final byte[] init = new byte[]{(byte)0x01, 0x06, 0x01, 0x00, 0x00, 0x01, 0x49, (byte)0xF6};
    private final byte[] order_strength = new byte[]{0x01, 0x06, 0x01, 0x01, 0x00, 0x1E, 0x59, (byte)0xFE};
    private final byte[] set_pos = new byte[]{(byte)0x01, 0x06, 0x01, 0x03, 0x01, (byte)0xF4, (byte)0x78, (byte)0x21};
    private final byte[] set_speed = new byte[]{(byte)0x01, 0x06, 0x01, 0x04, 0x00, 0x02, (byte)0x48, (byte)0x32};

    public Clamp(String portname) throws SerialPortException{
        super("clamp",portname);
        currentPort.setParams(SerialPort.BAUDRATE_115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
    }

    public void gesture(int position,int speed,int strength) throws SerialPortException, InterruptedException {
        this.setPos(position);
        this.setSpeed(speed);
        this.setStrength(strength);
    }

    public void gestureVial() throws SerialPortException, InterruptedException {
        this.setPos(420);
        this.setSpeed(60);
        this.setStrength(80);
    }

    public void gestureVialScrew() throws SerialPortException, InterruptedException {
        this.setPos(240);
        this.setSpeed(60);
        this.setStrength(80);
    }

    public void gestureCap() throws SerialPortException, InterruptedException {
        this.setPos(495);
        this.setSpeed(60);
        this.setStrength(30);
    }

    public void gestureStandBy() throws SerialPortException, InterruptedException {
        this.setPos(650);
        this.setSpeed(30);
        this.setStrength(40);
    }

    public boolean init() throws SerialPortException, InterruptedException {
        try {
            if(this.send_order(init)) {
                Thread.sleep(100);
                String data = this.rec_data();
                System.out.println(data);
                return Myutils.byteArrayToHexStr(init).equals(data.replace(" ", ""));
            }else return false;
            }catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            } catch (SerialPortException e) {
                e.printStackTrace();
                return false;
            }
    }

    public boolean setPos(int pos) throws SerialPortException, InterruptedException {
        byte[] s = Myutils.intToByte(pos);
        set_pos[4] = s[2];
        set_pos[5] = s[3];
        byte[] new_order = new byte[6];
        System.arraycopy(set_pos, 0, new_order, 0, 6);
        byte[] crc = getCRC(new_order);
        set_pos[6] = crc[1];
        set_pos[7] = crc[0];
        return send_order(set_pos);
    }

    public boolean setSpeed(int speed) throws SerialPortException, InterruptedException {
        byte[] s = Myutils.intToByte(speed);
        set_speed[4] = s[2];
        set_speed[5] = s[3];
        byte[] new_order = new byte[6];
        System.arraycopy(set_speed, 0, new_order, 0, 6);
        byte[] crc = getCRC(new_order);
        set_speed[6] = crc[1];
        set_speed[7] = crc[0];
        return send_order(set_speed);
    }

    public boolean setStrength(int strength) throws InterruptedException, SerialPortException {
        byte[] s = Myutils.intToByte(strength);
        order_strength[4] = s[2];
        order_strength[5] = s[3];
        byte[] new_order = new byte[6];
        System.arraycopy(order_strength, 0, new_order, 0, 6);
        byte[] crc = getCRC(new_order);
        order_strength[6] = crc[1];
        order_strength[7] = crc[0];
        return send_order(order_strength);
    }

    public byte[] getOrder(byte[] order,int param){
        byte[] s = Myutils.intToByte(param);
        order[4] = s[2];
        order[5] = s[3];
        byte[] new_order = new byte[6];
        System.arraycopy(order, 0, new_order, 0, 6);//get six data in the front of array to caculate CRC
        byte[] crc = getCRC(new_order);
        order[6] = crc[1];
        order[7] = crc[0];
        return order;
    }

    public static byte[] getCRC(byte[] bytes) {
        int CRC = 0x0000ffff;
        int POLYNOMIAL = 0x0000a001;

        int i, j;
        for (i = 0; i < bytes.length; i++) {
            CRC ^= ((int) bytes[i] & 0x000000ff);
            for (j = 0; j < 8; j++) {
                if ((CRC & 0x00000001) != 0) {
                    CRC >>= 1;
                    CRC ^= POLYNOMIAL;
                } else {
                    CRC >>= 1;
                }
            }
        }
        return Myutils.HexString2Bytes(Integer.toHexString(CRC));
    }
}
