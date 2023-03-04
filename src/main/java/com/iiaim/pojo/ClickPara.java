package com.iiaim.pojo;

/**
 * @author 陈杰炫
 */
public class ClickPara {
    private String substrateA;
    private String substrateB;

    public ClickPara() {
    }

    public ClickPara(String substrateA, String substrateB) {
        this.substrateA = substrateA;
        this.substrateB = substrateB;
    }

    public String getSubstrateA() {
        return substrateA;
    }

    public void setSubstrateA(String substrateA) {
        this.substrateA = substrateA;
    }

    public String getSubstrateB() {
        return substrateB;
    }

    public void setSubstrateB(String substrateB) {
        this.substrateB = substrateB;
    }

    @Override
    public String toString() {
        return "ClickPara{" +
                "substrateA='" + substrateA + '\'' +
                ", substrateB='" + substrateB + '\'' +
                '}';
    }
}
