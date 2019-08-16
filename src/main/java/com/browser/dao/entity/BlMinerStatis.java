package com.browser.dao.entity;

import java.math.BigDecimal;
import java.util.List;

public class BlMinerStatis {
    private String  name;

    private Integer  transaction;

    private String  address;

    private String rewards;
    
    private Integer contracts;

	private List<String> balances;
	private List<String> lockBalances;
	private List<String> paybackBalances;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getTransaction() {
		return transaction;
	}

	public void setTransaction(Integer transaction) {
		this.transaction = transaction;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRewards() {
		return rewards;
	}

	public void setRewards(String rewards) {
		this.rewards = rewards;
	}

	public Integer getContracts() {
		return contracts;
	}

	public void setContracts(Integer contracts) {
		this.contracts = contracts;
	}

	public List<String> getBalances() {
		return balances;
	}

	public void setBalances(List<String> balances) {
		this.balances = balances;
	}

	public List<String> getLockBalances() {
		return lockBalances;
	}

	public void setLockBalances(List<String> lockBalances) {
		this.lockBalances = lockBalances;
	}

	public List<String> getPaybackBalances() {
		return paybackBalances;
	}

	public void setPaybackBalances(List<String> paybackBalances) {
		this.paybackBalances = paybackBalances;
	}
}