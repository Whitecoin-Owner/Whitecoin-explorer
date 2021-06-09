package com.browser.dao.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BlBlock {
    private String blockId;

    private Long blockNum;

    private Long blockSize;

    private String minerName;

    private String minerAddress;

    private String previous;

    private String prevHash;

    private String nextHash;

    private Date blockTime;

    private Integer trxCount;

    private Long amount;

    private Long seconds;

    private BigDecimal fee;

    private BigDecimal reward;

    private String merkleRoot;

    private Date createdTime;

    private String timeZone;

    private String intervalTime;

    //查询页码
    private Integer page;
    private Integer rows;
    //统计
    private Integer transactions;
    private BigDecimal countRewards;
    private String rewards;

    public Long getSeconds() {
        return seconds;
    }

    public void setSeconds(Long seconds) {
        this.seconds = seconds;
    }

    public BigDecimal getCountRewards() {
        return countRewards;
    }

    public void setCountRewards(BigDecimal countRewards) {
        this.countRewards = countRewards;
    }

    public Integer getTransactions() {
        return transactions;
    }

    public void setTransactions(Integer transactions) {
        this.transactions = transactions;
    }

    public String getRewards() {
        return rewards;
    }

    public void setRewards(String rewards) {
        this.rewards = rewards;
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

    public String getBlockId() {
        return blockId;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId == null ? null : blockId.trim();
    }

    public Long getBlockNum() {
        return blockNum;
    }

    public void setBlockNum(Long blockNum) {
        this.blockNum = blockNum;
    }

    public Long getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(Long blockSize) {
        this.blockSize = blockSize;
    }

    public String getMinerName() {
        return minerName;
    }

    public void setMinerName(String minerName) {
        this.minerName = minerName == null ? null : minerName.trim();
    }

    public String getMinerAddress() {
        return minerAddress;
    }

    public void setMinerAddress(String minerAddress) {
        this.minerAddress = minerAddress == null ? null : minerAddress.trim();
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous == null ? null : previous.trim();
    }

    public String getPrevHash() {
        return prevHash;
    }

    public void setPrevHash(String prevHash) {
        this.prevHash = prevHash == null ? null : prevHash.trim();
    }

    public String getNextHash() {
        return nextHash;
    }

    public void setNextHash(String nextHash) {
        this.nextHash = nextHash == null ? null : nextHash.trim();
    }

    public Date getBlockTime() {
        return blockTime;
    }

    public void setBlockTime(Date blockTime) {
        this.blockTime = blockTime;
    }

    public Integer getTrxCount() {
        return trxCount;
    }

    public void setTrxCount(Integer trxCount) {
        this.trxCount = trxCount;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public BigDecimal getReward() {
        return reward;
    }

    public void setReward(BigDecimal reward) {
        this.reward = reward;
    }

    public String getMerkleRoot() {
        return merkleRoot;
    }

    public void setMerkleRoot(String merkleRoot) {
        this.merkleRoot = merkleRoot == null ? null : merkleRoot.trim();
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
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