package com.browser.wallet.beans;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class TxReceiptContractBalanceChange {
    private String address;
    private String assetId;
    private Long amount;

    /**
     * 从tx receipt中的contract_withdraw/contract_balances/deposit_to_address/deposit_contract转换得来
     */
    public static List<TxReceiptContractBalanceChange> fromTxReceiptChangeJson(List<JSONArray> changeJson) {
        if(changeJson == null) {
            return null;
        }
        if(changeJson.isEmpty()) {
            return Collections.emptyList();
        }
        return changeJson.stream().map(change -> {
            JSONArray item1 = change.getJSONArray(0);
            Long amount = change.getLong(1);
            String address = item1.getString(0);
            String assetId = item1.getString(1);
            TxReceiptContractBalanceChange txReceiptContractBalanceChange = new TxReceiptContractBalanceChange();
            txReceiptContractBalanceChange.setAddress(address);
            txReceiptContractBalanceChange.setAssetId(assetId);
            txReceiptContractBalanceChange.setAmount(amount);
            return txReceiptContractBalanceChange;
        }).collect(Collectors.toList());
    }
}
