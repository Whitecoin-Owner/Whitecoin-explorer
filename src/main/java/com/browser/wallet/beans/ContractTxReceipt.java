package com.browser.wallet.beans;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;

import java.util.List;

/**
 * 合约交易回执
 */
@Data
public class ContractTxReceipt {
    private String id;
    private String trx_id;
    private Long block_num;
    private Integer op_num;
    private String api_result;
    private List<TxReceiptEvent> events;
    private Boolean exec_succeed;
    private Long acctual_fee;
    private String invoker;
    private List<Object> transfer_fees;
    // 以下4项可以转换成TxReceiptContractBalanceChange
    private List<JSONArray> contract_withdraw;
    private List<JSONArray> contract_balances;
    private List<JSONArray> deposit_to_address;
    private List<JSONArray> deposit_contract;
}
