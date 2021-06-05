package com.browser.service.swapEventPlugins.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.browser.dao.entity.*;
import com.browser.service.AssetService;
import com.browser.service.swapEventPlugins.ISwapEventPlugin;
import com.browser.tools.Constant;
import com.browser.wallet.ContractArgDecodeException;
import com.browser.wallet.PrecisionUtils;
import com.browser.wallet.beans.TxReceiptEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;

@Slf4j
@Component("swapPlugin.NativeBalanceChange")
public class NativeBalanceChangeSwapEventPlugin implements ISwapEventPlugin {
    @Resource
    private AssetService assetService;

    @Override
    public String eventName() {
        return "NativeBalanceChange";
    }

    private Integer getTokenDecimals(String token) {
        if(Constant.SYMBOL.equals(token)) {
            return Constant.PRECISION_LENGTH;
        }
        BlAsset asset = assetService.selectByAssetSymbol(token);
        if(asset != null) {
            return asset.getPrecision().toString().length()-1;
        }
        return null;
    }


    @Override
    public BlSwapTransaction decodeSwapEvent(BlSwapContract swapContract, TxReceiptEvent receiptEvent, int eventSeq, String txId, BlTransaction trx, String blockId) {
        try {
            String eventArg = receiptEvent.getEvent_arg();
            JSONObject eventArgJson = JSON.parseObject(eventArg);
            BigInteger changeFull = eventArgJson.getBigInteger("change");
            String reason = eventArgJson.getString("reason");
            String symbol = eventArgJson.getString("symbol");
            String address = eventArgJson.getString("address");
            if(symbol == null || changeFull == null || address == null) {
                throw new ContractArgDecodeException("decode " + eventName() + " arg error " + eventArg);
            }
            Integer tokenDecimals = getTokenDecimals(symbol);
            if(tokenDecimals == null) {
                throw new ContractArgDecodeException("decode " + eventName() + " arg error unknown asset symbol " + eventArg);
            }
            BigDecimal change = PrecisionUtils.fullAmountToDecimal(changeFull, tokenDecimals);

            BlSwapTransaction swapTx = createBaseSwapTransaction(swapContract, receiptEvent, eventSeq, txId, trx, blockId);
            swapTx.setChange(change);
            swapTx.setSymbol(symbol);
            swapTx.setChangeAddress(address);
            swapTx.setReason(reason);

            return swapTx;
        } catch(Exception e) {
            log.error("decodeSwapEvent " + eventName() + " error", e);
            return null;
        }
    }
}
