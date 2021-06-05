package com.browser.dao.entity;

import lombok.Data;

import java.util.Date;

@Data
public class BlTokenTransaction {
    private Long id;
    private String trxId;
    private String blockId;
    private Integer blockNum;
    private String fromAccount;
    private String toAccount;
    private String symbol;
    private Long amount;
    private Long fee;
    private Date trxTime;
    private String contractId;
    private String memo;
    private Integer eventSeq;

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

    // 暂存数据
    private BlToken tokenInfo;
    private String amountStr;
    private String feeStr;
}
