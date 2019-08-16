package com.browser.dao.entity;

import java.util.Date;

public class BlContractStatis {
    private String  contractAddress;
    
    private String onwerAddress;

    private Integer  type;

    private Integer  callTimes;

    private Date createTime;
    
    private Date lastTime;
    
	public String getOnwerAddress() {
		return onwerAddress;
	}

	public void setOnwerAddress(String onwerAddress) {
		this.onwerAddress = onwerAddress;
	}

	public String getContractAddress() {
		return contractAddress;
	}

	public void setContractAddress(String contractAddress) {
		this.contractAddress = contractAddress;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getCallTimes() {
		return callTimes;
	}

	public void setCallTimes(Integer callTimes) {
		this.callTimes = callTimes;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastTime() {
		return lastTime;
	}

	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}
    
}