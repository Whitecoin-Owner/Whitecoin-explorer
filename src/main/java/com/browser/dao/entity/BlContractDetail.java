package com.browser.dao.entity;

import java.util.List;

public class BlContractDetail {
    private String  contractAddress;
    
    private String onwerAddress;

    private Integer  transactions;

    private String  createTxId;

    private List<String> balance;

	public String getContractAddress() {
		return contractAddress;
	}

	public void setContractAddress(String contractAddress) {
		this.contractAddress = contractAddress;
	}

	public String getOnwerAddress() {
		return onwerAddress;
	}

	public void setOnwerAddress(String onwerAddress) {
		this.onwerAddress = onwerAddress;
	}

	public Integer getTransactions() {
		return transactions;
	}

	public void setTransactions(Integer transactions) {
		this.transactions = transactions;
	}

	public String getCreateTxId() {
		return createTxId;
	}

	public void setCreateTxId(String createTxId) {
		this.createTxId = createTxId;
	}

	public List<String> getBalance() {
		return balance;
	}

	public void setBalance(List<String> balance) {
		this.balance = balance;
	}
    
}