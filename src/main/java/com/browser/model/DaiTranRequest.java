package com.browser.model;

/**
 * dai合约交易对象
 *
 * @author lmc
 */
public class DaiTranRequest {

    private String walletAddress;

    private String contractAddress;

    private Long blockNum;

    private Long beginBlockNum;

    private Long endBlockNum;

    public Long getBlockNum() {
        return blockNum;
    }

    public void setBlockNum(Long blockNum) {
        this.blockNum = blockNum;
    }

    public Long getBeginBlockNum() {
        return beginBlockNum;
    }

    public void setBeginBlockNum(Long beginBlockNum) {
        this.beginBlockNum = beginBlockNum;
    }

    public Long getEndBlockNum() {
        return endBlockNum;
    }

    public void setEndBlockNum(Long endBlockNum) {
        this.endBlockNum = endBlockNum;
    }

    public String getWalletAddress() {
        return walletAddress;
    }

    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }
}
