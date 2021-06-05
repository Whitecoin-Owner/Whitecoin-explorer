package com.browser.dao.entity;

import lombok.Data;

@Data
public class BlTxContractBalanceChange {
    private Long id;
    private String trxId;
    private Long blockNum;
    private String changeType;
    private String address;
    private String assetId;
    private Long amount;
    private Integer changeSeq;

    // 临时字段
    private String amountStr;
    private String assetSymbol;
}
