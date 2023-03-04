package com.iiaim.pojo;

import java.util.List;

/**
 * @author 陈杰炫
 */
public class ClickBatch {
    private int bufferVolume;
    private String batchSize;
    private double subAEq;
    private double subAN;
    private double subBEq;
    private double reactTemp;
    private String buffer;
    private String remark;
    private double catEq;
    private String cat;
    private int reactTime;
    private List<ClickPara> parameter;

    public ClickBatch() {
    }

    public ClickBatch(int bufferVolume, String batchSize, double subAEq, double subAN, double subBEq, double reactTemp, String buffer, String remark, double catEq, String cat, int reactTime, List<ClickPara> parameter) {
        this.bufferVolume = bufferVolume;
        this.batchSize = batchSize;
        this.subAEq = subAEq;
        this.subAN = subAN;
        this.subBEq = subBEq;
        this.reactTemp = reactTemp;
        this.buffer = buffer;
        this.remark = remark;
        this.catEq = catEq;
        this.cat = cat;
        this.reactTime = reactTime;
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

    public double getCatEq() {
        return catEq;
    }

    public void setCatEq(double catEq) {
        this.catEq = catEq;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public int getReactTime() {
        return reactTime;
    }

    public void setReactTime(int reactTime) {
        this.reactTime = reactTime;
    }

    public List<ClickPara> getParameter() {
        return parameter;
    }

    public void setParameter(List<ClickPara> parameter) {
        this.parameter = parameter;
    }

    @Override
    public String toString() {
        return "ClickBatch{" +
                "bufferVolume=" + bufferVolume +
                ", batchSize='" + batchSize + '\'' +
                ", subAEq=" + subAEq +
                ", subAN=" + subAN +
                ", subBEq=" + subBEq +
                ", reactTemp=" + reactTemp +
                ", buffer='" + buffer + '\'' +
                ", remark='" + remark + '\'' +
                ", catEq=" + catEq +
                ", cat='" + cat + '\'' +
                ", reactTime=" + reactTime +
                ", parameter=" + parameter +
                '}';
    }
}
