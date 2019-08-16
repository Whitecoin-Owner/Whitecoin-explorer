package com.browser.dao.entity;

public class BlGuarantee {
    private Integer id;

    private String guaranteeId;

    private String ownerAddr;

    private String assetOrign;

    private String assetTarget;

    private String rate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGuaranteeId() {
        return guaranteeId;
    }

    public void setGuaranteeId(String guaranteeId) {
        this.guaranteeId = guaranteeId == null ? null : guaranteeId.trim();
    }

    public String getOwnerAddr() {
        return ownerAddr;
    }

    public void setOwnerAddr(String ownerAddr) {
        this.ownerAddr = ownerAddr == null ? null : ownerAddr.trim();
    }

    public String getAssetOrign() {
        return assetOrign;
    }

    public void setAssetOrign(String assetOrign) {
        this.assetOrign = assetOrign == null ? null : assetOrign.trim();
    }

    public String getAssetTarget() {
        return assetTarget;
    }

    public void setAssetTarget(String assetTarget) {
        this.assetTarget = assetTarget == null ? null : assetTarget.trim();
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate == null ? null : rate.trim();
    }
}