package com.browser.dao.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BlTokenBalance {
    private Long id;
    private String addr;
    private BigDecimal amount;
    private String tokenAmount;
    private String tokenContract;
    private String assetId;
    private String tokenSymbol;
    private String imgUrl;
}
