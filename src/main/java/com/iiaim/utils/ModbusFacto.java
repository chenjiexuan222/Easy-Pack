package com.iiaim.utils;


import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.ip.IpParameters;

/**
 * @author 陈杰炫
 */
public class ModbusFacto {
    static ModbusFactory modbusFactory;

    static {
        if (modbusFactory == null) {
            modbusFactory = new ModbusFactory();
        }
    }

    /**
     * get tcpMaster
     * @return
     * @throws ModbusInitException
     */
    public static ModbusMaster getMaster(String ip) throws ModbusInitException {
        IpParameters params = new IpParameters();
        params.setHost(ip);//local host
        params.setPort(502);
        ModbusMaster tcpMaster = modbusFactory.createTcpMaster(params, false);
        tcpMaster.init();
        return tcpMaster;
    }
}
