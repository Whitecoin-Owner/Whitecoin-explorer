package com.browser.service.swapEventPlugins.impl;

import com.browser.dao.entity.BlSwapContract;
import com.browser.dao.entity.BlSwapTransaction;
import com.browser.dao.entity.BlTransaction;
import com.browser.service.swapEventPlugins.ISwapEventPlugin;
import com.browser.wallet.beans.TxReceiptEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("swapPlugin.Transfer")
public class TransferSwapEventPlugin implements ISwapEventPlugin {

    @Override
    public String eventName() {
        return "Transfer";
    }

    @Override
    public BlSwapTransaction decodeSwapEvent(BlSwapContract swapContract, TxReceiptEvent receiptEvent, int eventSeq, String txId, BlTransaction trx, String blockId) {
        // token解析中已经解析了swap token的Transfer事件了
        return null;
    }
}
