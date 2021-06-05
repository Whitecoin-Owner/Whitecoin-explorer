package com.browser.service.swapEventPlugins.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.browser.dao.entity.*;
import com.browser.service.AssetService;
import com.browser.service.TokenService;
import com.browser.service.swapEventPlugins.ISwapEventPlugin;
import com.browser.tools.Constant;
import com.browser.wallet.PrecisionUtils;
import com.browser.wallet.beans.TxReceiptEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;

@Slf4j
@Component("swapPlugin.Exchanged")
public class ExchangedSwapEventPlugin implements ISwapEventPlugin {

    @Resource
    private AssetService assetService;
    @Resource
    private TokenService tokenService;

    @Override
    public String eventName() {
        return "Exchanged";
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
            JSONObject argJson = JSON.parseObject(receiptEvent.getEvent_arg());
            BigDecimal fee = argJson.getBigDecimal("fee");
            String addr = argJson.getString("addr");
            String buyAsset = argJson.getString("buy_asset");
            BigInteger buyAmountFull = argJson.getBigInteger("buy_amount");
            String sellAsset = argJson.getString("sell_asset");
            BigInteger sellAmountFull = argJson.getBigInteger("sell_amount");

            if(addr == null || buyAsset == null || buyAmountFull == null || sellAsset == null || sellAmountFull == null) {
                log.error("invalid {} event arg {}", eventName(), receiptEvent.getEvent_arg());
                return null;
            }

            // buyAsset/sellAsset转成tokenSymbol, buyAmount/sellAmount转成带精度的金额
            String buyAssetSymbol = getTokenSymbol(buyAsset);
            String sellAssetSymbol = getTokenSymbol(sellAsset);
            Integer buyAssetDecimals = getTokenDecimals(buyAsset);
            Integer sellAssetDecimals = getTokenDecimals(sellAsset);
            if(buyAssetSymbol == null || sellAssetSymbol == null
            || buyAssetDecimals == null || sellAssetDecimals == null) {
                log.error("invalid {} event arg {}", eventName(), receiptEvent.getEvent_arg());
                return null;
            }
            BigDecimal buyAmount = PrecisionUtils.fullAmountToDecimal(buyAmountFull, buyAssetDecimals);
            BigDecimal sellAmount = PrecisionUtils.fullAmountToDecimal(sellAmountFull, sellAssetDecimals);


            BlSwapTransaction swapTx = createBaseSwapTransaction(swapContract, receiptEvent, eventSeq, txId, trx, blockId);
            swapTx.setChangeAddress(addr);
            swapTx.setBuyAsset(buyAsset);
            swapTx.setBuyAmount(buyAmount);
            swapTx.setSellAsset(sellAsset);
            swapTx.setSellAmount(sellAmount);
            swapTx.setExchangeFee(fee);
            return swapTx;
        } catch(Exception e) {
            log.error("decodeSwapEvent error", e);
            return null;
        }
    }
}
