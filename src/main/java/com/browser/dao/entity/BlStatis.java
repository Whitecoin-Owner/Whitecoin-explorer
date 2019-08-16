package com.browser.dao.entity;

import java.math.BigDecimal;

public class BlStatis {
    private BigDecimal totalAmount;

    private Long  height;

    private Integer totalTxNum;

    private String reward;

    private String crossAsset;

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Long getHeight() {
		return height;
	}

	public void setHeight(Long height) {
		this.height = height;
	}

	public Integer getTotalTxNum() {
		return totalTxNum;
	}

	public void setTotalTxNum(Integer totalTxNum) {
		this.totalTxNum = totalTxNum;
	}

	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}

	public String getCrossAsset() {
		return crossAsset;
	}

	public void setCrossAsset(String crossAsset) {
		this.crossAsset = crossAsset;
	}
}