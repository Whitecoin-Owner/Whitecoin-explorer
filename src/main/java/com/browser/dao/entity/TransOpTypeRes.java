package com.browser.dao.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.List;

/**
 * 交易类型返回
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransOpTypeRes {

    private String txHash;

    private Integer txStatus;

    private Integer txType;

    private Long blockHeight;

    private Date timeStamp;

    private Object operationData;

    private String timeZone;

    private String intervalTime;

    private List<BlTxEvents> events;
    private List<BlTxContractBalanceChange> txContractBalanceChanges;
    private List<BlTokenTransaction> tokenTransactions;
    private List<BlSwapTransaction> swapTransactions;
    private Boolean fail;

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public Integer getTxStatus() {
        return txStatus;
    }

    public void setTxStatus(Integer txStatus) {
        this.txStatus = txStatus;
    }

    public Integer getTxType() {
        return txType;
    }

    public void setTxType(Integer txType) {
        this.txType = txType;
    }

    public Long getBlockHeight() {
        return blockHeight;
    }

    public void setBlockHeight(Long blockHeight) {
        this.blockHeight = blockHeight;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Object getOperationData() {
        return operationData;
    }

    public void setOperationData(Object operationData) {
        this.operationData = operationData;
    }

    public List<BlTxEvents> getEvents() {
        return events;
    }

    public void setEvents(List<BlTxEvents> events) {
        this.events = events;
    }

    public List<BlTxContractBalanceChange> getTxContractBalanceChanges() {
        return txContractBalanceChanges;
    }

    public void setTxContractBalanceChanges(List<BlTxContractBalanceChange> txContractBalanceChanges) {
        this.txContractBalanceChanges = txContractBalanceChanges;
    }

    public List<BlTokenTransaction> getTokenTransactions() {
        return tokenTransactions;
    }

    public void setTokenTransactions(List<BlTokenTransaction> tokenTransactions) {
        this.tokenTransactions = tokenTransactions;
    }

    public List<BlSwapTransaction> getSwapTransactions() {
        return swapTransactions;
    }

    public void setSwapTransactions(List<BlSwapTransaction> swapTransactions) {
        this.swapTransactions = swapTransactions;
    }

    public Boolean getFail() {
        return fail;
    }

    public void setFail(Boolean fail) {
        this.fail = fail;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(String intervalTime) {
        this.intervalTime = intervalTime;
    }
}
