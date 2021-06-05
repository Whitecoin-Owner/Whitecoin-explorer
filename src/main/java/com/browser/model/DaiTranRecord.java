package com.browser.model;

/**
 * dai合约交易对象
 */
public class DaiTranRecord {

    private String address;

    private String type;

    private String time;

    private String amount;

    private String assetType;

    private String txHash;

    private String collateralAmount;

    private String collateralCoinType;

    private String stableTokenAmount;

    private String stableTokeCoinType;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public String getCollateralAmount() {
        return collateralAmount;
    }

    public void setCollateralAmount(String collateralAmount) {
        this.collateralAmount = collateralAmount;
    }

    public String getCollateralCoinType() {
        return collateralCoinType;
    }

    public void setCollateralCoinType(String collateralCoinType) {
        this.collateralCoinType = collateralCoinType;
    }

    public String getStableTokenAmount() {
        return stableTokenAmount;
    }

    public void setStableTokenAmount(String stableTokenAmount) {
        this.stableTokenAmount = stableTokenAmount;
    }

    public String getStableTokeCoinType() {
        return stableTokeCoinType;
    }

    public void setStableTokeCoinType(String stableTokeCoinType) {
        this.stableTokeCoinType = stableTokeCoinType;
    }
}
