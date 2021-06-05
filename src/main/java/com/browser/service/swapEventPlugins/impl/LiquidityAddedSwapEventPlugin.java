package com.browser.service.swapEventPlugins.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.browser.dao.entity.*;
import com.browser.service.AssetService;
import com.browser.service.TokenService;
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
import java.math.RoundingMode;

@Slf4j
@Component("swapPlugin.LiquidityAdded")
public class LiquidityAddedSwapEventPlugin implements ISwapEventPlugin {
    @Resource
    private AssetService assetService;
    @Resource
    private TokenService tokenService;

    @Override
    public String eventName() {
        return "LiquidityAdded";
    }

    private Integer getTokenDecimals(String token) {
        if(Constant.SYMBOL.equals(token)) {
            return Constant.PRECISION_LENGTH;
        }
        BlAsset asset = assetService.selectByAssetSymbol(token);
        if(asset != null) {
            return asset.getPrecision().toString().length()-1;
        }
        BlToken blToken = tokenService.selectByContractAddress(token);
        if(blToken != null) {
            return blToken.getPrecision();
        }
        return null;
    }

    private String getTokenSymbol(String token) {
        if(Constant.SYMBOL.equals(token)) {
            return token;
        }
        BlAsset asset = assetService.selectByAssetSymbol(token);
        if(asset != null) {
            return asset.getSymbol();
        }
        BlToken blToken = tokenService.selectByContractAddress(token);
        if(blToken != null) {
            return blToken.getTokenSymbol();
        }
        return null;
    }

    @Override
    public BlSwapTransaction decodeSwapEvent(BlSwapContract swapContract, TxReceiptEvent receiptEvent, int eventSeq, String txId, BlTransaction trx, String blockId) {
        try {
            String eventArg = receiptEvent.getEvent_arg();
            JSONObject argJson = JSON.parseObject(eventArg);
            String token1 = swapContract.getToken1();
            String token2 = swapContract.getToken2();
            BigInteger token1AmountFull = argJson.getBigInteger(token1);
            BigInteger token2AmountFull = argJson.getBigInteger(token2);
            if(token1AmountFull == null || token1AmountFull.compareTo(BigInteger.ZERO)<=0
            || token2AmountFull == null || token2AmountFull.compareTo(BigInteger.ZERO) <= 0) {
                throw new ContractArgDecodeException("decode " + eventName() + " arg " + eventArg + " error");
            }
            Integer token1Decimals = getTokenDecimals(token1);
            Integer token2Decimals = getTokenDecimals(token2);
            if(token1Decimals == null || token2Decimals == null) {
                throw new ContractArgDecodeException("decode " + eventName() + " arg " + eventArg + " error invalid token decimals");
            }
            BigDecimal token1Amount = PrecisionUtils.fullAmountToDecimal(token1AmountFull, token1Decimals);
            BigDecimal token2Amount = PrecisionUtils.fullAmountToDecimal(token2AmountFull, token2Decimals);

            BlSwapTransaction swapTx = createBaseSwapTransaction(swapContract, receiptEvent, eventSeq, txId, trx, blockId);
            swapTx.setLiquidityToken1ChangeAmount(token1Amount);
            swapTx.setLiquidityToken2ChangeAmount(token2Amount);

            swapTx.setLiquidityToken1(getTokenSymbol(token1));
            swapTx.setLiquidityToken2(getTokenSymbol(token2));

            return swapTx;
        } catch(Exception e) {
            log.error("decodeSwapEvent error", e);
            return null;
        }
    }
}
