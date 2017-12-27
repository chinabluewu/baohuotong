package com.demo.app.bean;

/**
 * Created by wu on 2016/3/1.
 */
public class ChargeHistory extends Base {
     private String HistoryNo;
     private String ChargeVaule;
    private String ChargeTime;

    public String getHistoryNo(){return HistoryNo;}
    public void setHistoryNo(String historyNo){this.HistoryNo = historyNo;}

    public String getChargeVaule(){return ChargeVaule;}
    public void setChargeVaule(String chargeVaule){this.ChargeVaule = chargeVaule;}

    public String getChargeTime(){return ChargeTime;}
    public void setChargeTime(String chargeTime){this.ChargeTime = chargeTime; }

}
