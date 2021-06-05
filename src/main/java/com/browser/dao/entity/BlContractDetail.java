package com.browser.dao.entity;

import lombok.Data;

import java.util.List;

@Data
public class BlContractDetail {
    private String  contractAddress;
    
    private String onwerAddress;

    private Integer  transactions;

    private String  createTxId;

    private List<String> balance;

	private BlToken tokenContract;
    
}