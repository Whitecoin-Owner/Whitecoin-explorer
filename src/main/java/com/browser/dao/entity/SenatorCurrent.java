package com.browser.dao.entity;

import java.math.BigDecimal;
import java.util.List;

public class SenatorCurrent {
    private Integer id;

    private String senatorId;

    private String senatorName;

    private BigDecimal lockAsset;

    private String proposalId;

    private String address;

    private BigDecimal payBack;

    private Integer status;

    private String trxId;

    private Integer page;
    private Integer rows;

    private String lockAddress;
    private String email;
    private List<String> payBackList;
    private List<ProposalContent> proList;

    //安全员状态
    private Integer safeStatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSenatorId() {
        return senatorId;
    }

    public void setSenatorId(String senatorId) {
        this.senatorId = senatorId == null ? null : senatorId.trim();
    }

    public String getSenatorName() {
        return senatorName;
    }

    public void setSenatorName(String senatorName) {
        this.senatorName = senatorName == null ? null : senatorName.trim();
    }

    public BigDecimal getLockAsset() {
        return lockAsset;
    }

    public void setLockAsset(BigDecimal lockAsset) {
        this.lockAsset = lockAsset;
    }

    public String getProposalId() {
        return proposalId;
    }

    public void setProposalId(String proposalId) {
        this.proposalId = proposalId == null ? null : proposalId.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public BigDecimal getPayBack() {
        return payBack;
    }

    public void setPayBack(BigDecimal payBack) {
        this.payBack = payBack;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTrxId() {
        return trxId;
    }

    public void setTrxId(String trxId) {
        this.trxId = trxId == null ? null : trxId.trim();
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public String getLockAddress() {
        return lockAddress;
    }

    public void setLockAddress(String lockAddress) {
        this.lockAddress = lockAddress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getPayBackList() {
        return payBackList;
    }

    public void setPayBackList(List<String> payBackList) {
        this.payBackList = payBackList;
    }

    public List<ProposalContent> getProList() {
        return proList;
    }

    public void setProList(List<ProposalContent> proList) {
        this.proList = proList;
    }

    public Integer getSafeStatus() {
        return safeStatus;
    }

    public void setSafeStatus(Integer safeStatus) {
        this.safeStatus = safeStatus;
    }
}