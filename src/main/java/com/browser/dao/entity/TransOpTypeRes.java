package com.browser.dao.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

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
}
