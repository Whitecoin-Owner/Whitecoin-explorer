package com.browser.bean;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Description: whitecoin-browser
 * Created by moloq on 2021/5/14 14:31
 */
@Data
@Getter
@Setter
public class LiquidateBean {
    private Long id;
    private String trxId;
    private Long blockNum;
    private Integer opNum;
    private String callerAddr;
    private String contractAddress;
    private String eventName;
    private Integer eventSeq;
    private String penaltyAmount;
    private String cdcId;
    private String owner;
    private String curPrice;
    private String auctionPrice;
    private String returnAmount;
    private String stabilityFee;
    private String secSinceEpoch;
    private String collateralAmount;
    private String stableTokenAmount;
    private String repayStableTokenAmount;
    private String auctionCollateralAmount;
    private String tradeResultStatus;
    private String tradeResultType;
    private String tradeTime;

    @Data
    @Setter
    @Getter
    public class EventArg{
        private String cdcId;
        private String owner;
        private String curPrice;
        private String auctionPrice;
        private String returnAmount;
        private String stabilityFee;
        private String secSinceEpoch;
        private String collateralAmount;
        private String stableTokenAmount;
        private String repayStableTokenAmount;
        private String auctionCollateralAmount;
        private String penaltyAmount;
    }
}
