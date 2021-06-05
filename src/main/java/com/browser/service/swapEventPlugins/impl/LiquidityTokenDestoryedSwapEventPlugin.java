package com.browser.service.swapEventPlugins.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.browser.dao.entity.BlSwapContract;
import com.browser.dao.entity.BlSwapTransaction;
import com.browser.dao.entity.BlToken;
import com.browser.dao.entity.BlTransaction;
import com.browser.service.TokenService;
import com.browser.service.swapEventPlugins.ISwapEventPlugin;
import com.browser.wallet.ContractArgDecodeException;
import com.browser.wallet.PrecisionUtils;
import com.browser.wallet.beans.TxReceiptEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;

@Slf4j
@Component("swapPlugin.LiquidityTokenDestoryed")
public class LiquidityTokenDestoryedSwapEventPlugin implements ISwapEventPlugin {
    @Resource
    private TokenService tokenService;

    @Override
    public String eventName() {
        return "LiquidityTokenDestoryed";
    }

    @Override
    public BlSwapTransaction decodeSwapEvent(BlSwapContract swapContract, TxReceiptEvent receiptEvent, int eventSeq, String txId, BlTransaction trx, String blockId) {
        try {
            String eventArg = receiptEvent.getEvent_arg();
            BigInteger tokenFullAmount = new BigInteger(eventArg);
            if(tokenFullAmount.compareTo(BigInteger.ZERO)<0) {
                throw new ContractArgDecodeException("decode " + eventName() + " arg error " + eventArg);
            }
            BlToken blToken = tokenService.selectByContractAddress(swapContract.getContractAddress());
            if(blToken == null) {
                log.error("swap contract token " + swapContract.getContractAddress() + " not in bl_token table");
                return null;
            }
            BigDecimal tokenAmount = PrecisionUtils.fullAmountToDecimal(tokenFullAmount, blToken.getPrecision());

            BlSwapTransaction swapTx = createBaseSwapTransaction(swapContract, receiptEvent, eventSeq, txId, trx, blockId);
            swapTx.setChange(tokenAmount);
            swapTx.setSymbol(blToken.getTokenSymbol());

            return swapTx;
        } catch(Exception e) {
            log.error("decodeSwapEvent error", e);
            return null;
        }
    }
}
