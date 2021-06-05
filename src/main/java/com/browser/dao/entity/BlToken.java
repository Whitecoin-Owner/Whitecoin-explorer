package com.browser.dao.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class BlToken {
    private Long id;
    private String contractAddress;
    private String tokenSymbol;
    private Integer precision;
    private String creatorAddress;
    private String createTrxId;
    private BigDecimal tokenSupply;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date createAt;
    private Integer topSort;
    private Integer tokenAddressNum;

    // 暂存的查询参数
    private Integer page;
    private Integer rows;
}
