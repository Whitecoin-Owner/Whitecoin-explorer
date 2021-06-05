package com.browser.model;

import java.util.List;

/**
 * dai合约交易对象
 */
public class DaiTransaction {

    private String cdcId;

    /**
     * 2021.05.14新增字段
     */

    /**
     * 已抵押
     */
    private String alreadyMortgageAmount;

    /**
     * 已借贷
     */
    private String alreadyLoanAmount;

    /**
     * 清算罚金收入
     */
    private String liquidationFineAmount;


    /* =================== 2020.05.14新增字段 ==========*/

    private List<DaiTranRecord> tranRecords;

    public String getCdcId() {
        return cdcId;
    }

    public void setCdcId(String cdcId) {
        this.cdcId = cdcId;
    }

    public String getAlreadyMortgageAmount() {
        return alreadyMortgageAmount;
    }

    public void setAlreadyMortgageAmount(String alreadyMortgageAmount) {
        this.alreadyMortgageAmount = alreadyMortgageAmount;
    }

    public String getAlreadyLoanAmount() {
        return alreadyLoanAmount;
    }

    public void setAlreadyLoanAmount(String alreadyLoanAmount) {
        this.alreadyLoanAmount = alreadyLoanAmount;
    }

    public String getLiquidationFineAmount() {
        return liquidationFineAmount;
    }

    public void setLiquidationFineAmount(String liquidationFineAmount) {
        this.liquidationFineAmount = liquidationFineAmount;
    }

    public List<DaiTranRecord> getTranRecords() {
        return tranRecords;
    }

    public void setTranRecords(List<DaiTranRecord> tranRecords) {
        this.tranRecords = tranRecords;
    }
}
