package com.browser.wallet;

import com.browser.wallet.beans.CodePrintable;
import com.browser.wallet.beans.SimpleContractInfo;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ContractUtils {
    public static boolean maybeTokenContract(SimpleContractInfo contractInfo) {
        Set<String> allApis = new HashSet<>();
        CodePrintable codePrintable = contractInfo.getCode_printable();
        if(codePrintable!=null) {
            if(codePrintable.getAbi()!=null) {
                allApis.addAll(codePrintable.getAbi());
            }
            if(codePrintable.getOffline_abi()!=null) {
                allApis.addAll(codePrintable.getOffline_abi());
            }
        }
        // token合约必须包含的api列表
        List<String> tokenMustHaveApis = Arrays.asList("balanceOf", "transfer", "transferFrom", "approve", "approvedBalanceFrom", "tokenSymbol", "precision", "totalSupply");
        for(String mustApi : tokenMustHaveApis) {
            if(!allApis.contains(mustApi)) {
                return false;
            }
        }
        return true;
    }
}
