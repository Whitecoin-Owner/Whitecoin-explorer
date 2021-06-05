package com.browser.dao.entity;

import com.browser.wallet.beans.ContractTxReceipt;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

public class BlTransaction {

    private Integer id;

    private String trxId;

    private String blockId;

    private Long blockNum;

    private String fromAccount;

    private String toAccount;

    private String minerAddress;

    private BigDecimal amount;

    private String amountStr;

    private String assetId;

    private String symbol;

    private BigDecimal fee;

    private String feeStr;

    private Date trxTime;

    private String extraTrxId;

    private String guaranteeId;

    private Integer opType;

    private Integer parentOpType;

    private Integer gasLimit;

    private Integer gasCost;

    private BigDecimal gasPrice;

    private String contractId;

    private Date createdTime;

    private String calledAbi;

    private String abiParams;

    private String extension;

    private Integer extension1;

    private String memo;

    private Boolean fail = false; // 是否失败交易

    // 这个不直接存入数据库，取数据后的暂存值
    private ContractTxReceipt receipt;

    //状态
    private Integer status;

    private String authorAddr;

    private String exchange;

    private boolean guaranteeUse;

    //查询统计参数
    private String trxNum;
    private String queryTime;
    private String startTime;
    private String endTime;
    private Integer page;
    private Integer rows;
    private Integer transactiopns;
    private Integer rewards;
    private String address;
    private String eventArg;

    private Set<String> toAddrList;
    private Set<String> fromAddrList;

    public String getEventArg() {
        return eventArg;
    }

    public void setEventArg(String eventArg) {
        this.eventArg = eventArg;
    }

    public boolean isGuaranteeUse() {
        return guaranteeUse;
    }

    public void setGuaranteeUse(boolean guaranteeUse) {
        this.guaranteeUse = guaranteeUse;
    }

    public String getGuaranteeId() {
        return guaranteeId;
    }

    public void setGuaranteeId(String guaranteeId) {
        this.guaranteeId = guaranteeId;
    }

    public String getMinerAddress() {
        return minerAddress;
    }

    public void setMinerAddress(String minerAddress) {
        this.minerAddress = minerAddress;
    }

    public String getAuthorAddr() {
        return authorAddr;
    }

    public void setAuthorAddr(String authorAddr) {
        this.authorAddr = authorAddr;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getTransactiopns() {
        return transactiopns;
    }

    public void setTransactiopns(Integer transactiopns) {
        this.transactiopns = transactiopns;
    }

    public Integer getRewards() {
        return rewards;
    }

    public void setRewards(Integer rewards) {
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

    public String getAmountStr() {
        return amountStr;
    }

    public void setAmountStr(String amountStr) {
        this.amountStr = amountStr;
    }

    public String getFeeStr() {
        return feeStr;
    }

    public void setFeeStr(String feeStr) {
        this.feeStr = feeStr;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTrxId() {
        return trxId;
    }

    public void setTrxId(String trxId) {
        this.trxId = trxId == null ? null : trxId.trim();
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

    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount == null ? null : fromAccount.trim();
    }

    public String getToAccount() {
        return toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount == null ? null : toAccount.trim();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public Date getTrxTime() {
        return trxTime;
    }

    public void setTrxTime(Date trxTime) {
        this.trxTime = trxTime;
    }

    public String getExtraTrxId() {
        return extraTrxId;
    }

    public void setExtraTrxId(String extraTrxId) {
        this.extraTrxId = extraTrxId == null ? null : extraTrxId.trim();
    }

    public Integer getOpType() {
        return opType;
    }

    public void setOpType(Integer opType) {
        this.opType = opType;
    }

    public Integer getGasLimit() {
        return gasLimit;
    }

    public void setGasLimit(Integer gasLimit) {
        this.gasLimit = gasLimit;
    }

    public Integer getGasCost() {
        return gasCost;
    }

    public void setGasCost(Integer gasCost) {
        this.gasCost = gasCost;
    }

    public BigDecimal getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(BigDecimal gasPrice) {
        this.gasPrice = gasPrice;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId == null ? null : contractId.trim();
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getCalledAbi() {
        return calledAbi;
    }

    public void setCalledAbi(String calledAbi) {
        this.calledAbi = calledAbi == null ? null : calledAbi.trim();
    }

    public String getAbiParams() {
        return abiParams;
    }

    public void setAbiParams(String abiParams) {
        this.abiParams = abiParams == null ? null : abiParams.trim();
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension == null ? null : extension.trim();
    }

    public Set<String> getToAddrList() {
        return toAddrList;
    }

    public void setToAddrList(Set<String> toAddrList) {
        this.toAddrList = toAddrList;
    }

    public Set<String> getFromAddrList() {
        return fromAddrList;
    }

    public void setFromAddrList(Set<String> fromAddrList) {
        this.fromAddrList = fromAddrList;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getTrxNum() {
        return trxNum;
    }

    public void setTrxNum(String trxNum) {
        this.trxNum = trxNum;
    }

    public String getQueryTime() {
        return queryTime;
    }

    public void setQueryTime(String queryTime) {
        this.queryTime = queryTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getExtension1() {
        return extension1;
    }

    public void setExtension1(Integer extension1) {
        this.extension1 = extension1;
    }

    public Integer getParentOpType() {
        return parentOpType;
    }

    public void setParentOpType(Integer parentOpType) {
        this.parentOpType = parentOpType;
    }

    public ContractTxReceipt getReceipt() {
        return receipt;
    }

    public void setReceipt(ContractTxReceipt receipt) {
        this.receipt = receipt;
    }

    public Boolean getFail() {
        return fail;
    }

    public void setFail(Boolean fail) {
        this.fail = fail;
    }

}