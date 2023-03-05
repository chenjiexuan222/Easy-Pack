package com.iiaim.pojo;

/**
 * @author 陈杰炫
 */
public class ClickReaction {
    private int id;
    private int bufferVolume;
    private String batchSize;
    private String substrateA;
    private double subAEq;
    private double subAN;
    private double subBEq;
    private double catEq;
    private int reactTime;
    private double reactTemp;
    private String substrateB;
    private String buffer;
    private String cat;
    private String remark;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public double getCatEq() {
        return catEq;
    }

    public void setCatEq(double catEq) {
        this.catEq = catEq;
    }

    public int getReactTime() {
        return reactTime;
    }

    public void setReactTime(int reactTime) {
        this.reactTime = reactTime;
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

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "ClickReaction{" +
                "id=" + id +
                ", bufferVolume=" + bufferVolume +
                ", batchSize='" + batchSize + '\'' +
                ", substrateA='" + substrateA + '\'' +
                ", subAEq=" + subAEq +
                ", subAN=" + subAN +
                ", subBEq=" + subBEq +
                ", catEq=" + catEq +
                ", reactTime=" + reactTime +
                ", reactTemp=" + reactTemp +
                ", substrateB='" + substrateB + '\'' +
                ", buffer='" + buffer + '\'' +
                ", cat='" + cat + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
