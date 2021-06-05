package com.browser.dao.entity;

public class BlTxEvents {
    private Long id;
    private String trxId;
    private Long blockNum;
    private Integer opNum;
    private String callerAddr;
    private String contractAddress;
    private String eventName;
    private String eventArg;
    private Integer eventSeq;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTrxId() {
        return trxId;
    }

    public void setTrxId(String trxId) {
        this.trxId = trxId;
    }

    public Long getBlockNum() {
        return blockNum;
    }

    public void setBlockNum(Long blockNum) {
        this.blockNum = blockNum;
    }

    public Integer getOpNum() {
        return opNum;
    }

    public void setOpNum(Integer opNum) {
        this.opNum = opNum;
    }

    public String getCallerAddr() {
        return callerAddr;
    }

    public void setCallerAddr(String callerAddr) {
        this.callerAddr = callerAddr;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventArg() {
        return eventArg;
    }

    public void setEventArg(String eventArg) {
        this.eventArg = eventArg;
    }

    public Integer getEventSeq() {
        return eventSeq;
    }

    public void setEventSeq(Integer eventSeq) {
        this.eventSeq = eventSeq;
    }
}
