package com.iiaim.pojo;

/**
 * @author 陈杰炫
 */
public class ScreenPara {
    private double catEq;
    private String cat;
    private int reactTime;

    public ScreenPara() {
    }

    public ScreenPara(double catEq, String cat, int reactTime) {
        this.catEq = catEq;
        this.cat = cat;
        this.reactTime = reactTime;
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

    @Override
    public String toString() {
        return "ScreenPara{" +
                "catEq=" + catEq +
                ", cat='" + cat + '\'' +
                ", reactTime=" + reactTime +
                '}';
    }
}
