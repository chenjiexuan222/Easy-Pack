package com.iiaim.pojo;

/**
 * @author 陈杰炫
 */
public class Block{
    private int id;
    private int blockVolume;
    private int axisX;
    private int axisY;
    private double blockConc;
    private String blockName;
    private String remark;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBlockVolume() {
        return blockVolume;
    }

    public void setBlockVolume(int blockVolume) {
        this.blockVolume = blockVolume;
    }

    public int getAxisX() {
        return axisX;
    }

    public void setAxisX(int axisX) {
        this.axisX = axisX;
    }

    public int getAxisY() {
        return axisY;
    }

    public void setAxisY(int axisY) {
        this.axisY = axisY;
    }

    public double getBlockConc() {
        return blockConc;
    }

    public void setBlockConc(double blockConc) {
        this.blockConc = blockConc;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "Block{" +
                "id=" + id +
                ", blockVolume=" + blockVolume +
                ", axisX=" + axisX +
                ", axisY=" + axisY +
                ", blockConc=" + blockConc +
                ", blockName='" + blockName + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
