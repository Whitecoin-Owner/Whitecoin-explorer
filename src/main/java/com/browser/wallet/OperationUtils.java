package com.browser.wallet;

import com.browser.tools.Constant;

public class OperationUtils {
    public static boolean isContractOp(Integer opType) {
        if(opType == null) {
            return false;
        }
        return Constant.CONTRACT_REGISTER_OPERATION == opType
                || Constant.CONTRACT_UPGRADE_OPERATION == opType
                || Constant.CONTRACT_INVOKE_OPERATION == opType
                || Constant.CONTRACT_TRANSFER_OPERATION == opType;
    }

    public static boolean isContractCreateOp(Integer opType) {
        if(opType == null) {
            return false;
        }
        return Constant.CONTRACT_REGISTER_OPERATION == opType;
    }
}
