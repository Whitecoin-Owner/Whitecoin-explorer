package com.browser.dao.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class BlSwapTransaction {
    private Long id;
    private String trxId;
    private String blockId;
    private Long blockNum;
    private String callerAddress;
    private Integer opNum;
    private String eventName;
    private Integer eventSeq;
    private String eventArg;
    private String contractAddress;
    private Date trxTime;
    private BigDecimal change;
    private String reason;
    private String symbol;
    private String changeAddress;
    private BigDecimal liquidityToken1ChangeAmount;
    private BigDecimal liquidityToken2ChangeAmount;
    private String liquidityToken1;
    private String liquidityToken2;
    private BigDecimal exchangeFee;
    private String buyAsset;
    private String sellAsset;
    private BigDecimal buyAmount;
    private BigDecimal sellAmount;

    // 临时的查询用的字段
    private Integer page;
    private Integer rows;
    private String address;
}
