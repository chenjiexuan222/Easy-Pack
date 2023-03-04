package com.iiaim.serial;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

/**
 * @author 陈杰炫
 */
public class BaseSeriport {

    protected SerialPort currentPort;
    protected String portName;
    protected final int timeInterval = 200;
    public BaseSeriport(){}

    /**
     * init the port
     * @param name port name
     * @param port port number
     */
    public BaseSeriport(String name, String port){
        try {
            portName = name;
            currentPort =new SerialPort(port);
            currentPort.openPort();
            currentPort.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }

    public boolean send_order(byte[] order) throws SerialPortException {
        return this.currentPort.writeBytes(order);
    }

    public boolean send_order(String order) throws SerialPortException {
        return currentPort.writeString(order);
    }

    public String rec_data() throws SerialPortException {
        return this.currentPort.readString();
    }

    public byte[] rec_data_bytes() throws SerialPortException {
        return this.currentPort.readBytes();
    }

    /**
     * read the data from equippment port with timeout
     * @param bytecount
     * @param timeout
     * @return
     * @throws SerialPortException
     * @throws SerialPortTimeoutException
     */
    public byte[] rec_data_bytes(int bytecount,int timeout) throws SerialPortException, SerialPortTimeoutException {
        return this.currentPort.readBytes(bytecount,timeout);
    }
}
