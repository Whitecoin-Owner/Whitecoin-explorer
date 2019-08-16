package com.browser.dao.entity;

public class BlTrxStatis {
    private Integer id;

    private String statisTime;

    private Integer trxNum;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatisTime() {
        return statisTime;
    }

    public void setStatisTime(String statisTime) {
        this.statisTime = statisTime == null ? null : statisTime.trim();
    }

    public Integer getTrxNum() {
        return trxNum;
    }

    public void setTrxNum(Integer trxNum) {
        this.trxNum = trxNum;
    }
}