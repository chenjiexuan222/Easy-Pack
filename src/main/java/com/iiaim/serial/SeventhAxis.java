package com.iiaim.serial;

import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.msg.ModbusResponse;
import com.serotonin.modbus4j.msg.WriteRegisterRequest;
import com.serotonin.modbus4j.msg.WriteRegisterResponse;
import com.serotonin.modbus4j.msg.WriteRegistersRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @author 陈杰炫
 */
@Component(value = "seventhAxis")
public class SeventhAxis {
    @Autowired
    @Qualifier("tcpMaster")
    private ModbusMaster tcpMaster;

    private static short position;
    private static short speed;
    private static short reset = 0;
    private static short start = 1;

    public boolean setPosition(short position) throws ModbusInitException, ModbusTransportException {
        return this.writeRegister(1, 200, position);
    }

    public boolean setSpeed(short speed) throws ModbusInitException, ModbusTransportException {
        return this.writeRegister(1, 202, speed);
    }

    public void start() throws ModbusInitException, ModbusTransportException {
        this.writeRegister(1, 204, reset);
        this.writeRegister(1, 204, start);
        this.writeRegister(1, 204, reset);
    }

    /** write [03 Holding Register(4x)]  function ID = 6
     * @param slaveId
     * @param writeOffset address of Holding Register，40001-49999，0--->40001,200--->40201
     * @param writeValue
     * @return
     * @throws ModbusTransportException
     * @throws ModbusInitException
     */
    public boolean writeRegister(int slaveId, int writeOffset, short writeValue){
        try {
            WriteRegisterRequest request = null;
            request = new WriteRegisterRequest(slaveId, writeOffset, writeValue);
            WriteRegisterResponse response = (WriteRegisterResponse) tcpMaster.send(request);
            if (response.isException()) {
                System.out.println("Exception response: message=" + response.getExceptionMessage());
                return false;
            } else {
                return true;
            }
        } catch (ModbusTransportException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * write multiple[03 Holding Register(4x)] function ID=16
     * @param slaveId  slaveID of modbus
     * @param startOffset
     * @param sdata data to be written
     * @return
     * @throws ModbusTransportException
     * @throws ModbusInitException
     */
    public boolean writeRegisters(int slaveId, int startOffset, short[] sdata) throws ModbusTransportException, ModbusInitException {
        // build query object
        WriteRegistersRequest request = new WriteRegistersRequest(slaveId, startOffset, sdata);
        //send query and get response object
        ModbusResponse response = tcpMaster.send(request); //response could be null
        if (response.isException()) {
            System.out.println(response.getExceptionMessage());
            return false;
        } else {
            return true;
        }
    }
}
