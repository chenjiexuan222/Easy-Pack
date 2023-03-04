package com.iiaim.pojo;

import java.util.List;

/**
 * @author 陈杰炫
 */
public class ScreenBatch {
    private int bufferVolume;
    private String batchSize;
    private String substrateA;
    private double subAEq;
    private double subAN;
    private double subBEq;
    private double reactTemp;
    private String substrateB;
    private String buffer;
    private String remark;
    private List<ScreenPara> parameter;

    public ScreenBatch() {
    }

    public ScreenBatch(int bufferVolume, String batchSize, String substrateA, double subAEq, double subAN, double subBEq, double reactTemp, String substrateB, String buffer, String remark, List<ScreenPara> parameter) {
        this.bufferVolume = bufferVolume;
        this.batchSize = batchSize;
        this.substrateA = substrateA;
        this.subAEq = subAEq;
        this.subAN = subAN;
        this.subBEq = subBEq;
        this.reactTemp = reactTemp;
        this.substrateB = substrateB;
        this.buffer = buffer;
        this.remark = remark;
        this.parameter = parameter;
    }

    public int getBufferVolume() {
        return bufferVolume;
    }

    public void setBufferVolume(int bufferVolume) {
        this.bufferVolume = bufferVolume;
    }

    public String getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(String batchSize) {
        this.batchSize = batchSize;
    }

    public String getSubstrateA() {
        return substrateA;
    }

    public void setSubstrateA(String substrateA) {
        this.substrateA = substrateA;
    }

    public double getSubAEq() {
        return subAEq;
    }

    public void setSubAEq(double subAEq) {
        this.subAEq = subAEq;
    }

    public double getSubAN() {
        return subAN;
    }

    public void setSubAN(double subAN) {
        this.subAN = subAN;
    }

    public double getSubBEq() {
        return subBEq;
    }

    public void setSubBEq(double subBEq) {
        this.subBEq = subBEq;
    }

    public double getReactTemp() {
        return reactTemp;
    }

    public void setReactTemp(double reactTemp) {
        this.reactTemp = reactTemp;
    }

    public String getSubstrateB() {
        return substrateB;
    }

    public void setSubstrateB(String substrateB) {
        this.substrateB = substrateB;
    }

    public String getBuffer() {
        return buffer;
    }

    public void setBuffer(String buffer) {
        this.buffer = buffer;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<ScreenPara> getParameter() {
        return parameter;
    }

    public void setParameter(List<ScreenPara> parameter) {
        this.parameter = parameter;
    }

    @Override
    public String toString() {
        return "ScreenBatch{" +
                "bufferVolume=" + bufferVolume +
                ", batchSize='" + batchSize + '\'' +
                ", substrateA='" + substrateA + '\'' +
                ", subAEq=" + subAEq +
                ", subAN=" + subAN +
                ", subBEq=" + subBEq +
                ", reactTemp=" + reactTemp +
                ", substrateB='" + substrateB + '\'' +
                ", buffer='" + buffer + '\'' +
                ", remark='" + remark + '\'' +
                ", parameter=" + parameter +
                '}';
    }
}
