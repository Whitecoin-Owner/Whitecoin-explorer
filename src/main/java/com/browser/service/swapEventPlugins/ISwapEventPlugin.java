package com.browser.service.swapEventPlugins;

import com.browser.dao.entity.BlSwapContract;
import com.browser.dao.entity.BlSwapTransaction;
import com.browser.dao.entity.BlTransaction;
import com.browser.wallet.beans.TxReceiptEvent;

/**
 * swap合约的各eventName的解析插件
 * 实现类需要加上bean name = swapPlugin.{eventName}
 */
public interface ISwapEventPlugin {
    /**
     * 本plugin支持解析的swap contract的event name
     */
    String eventName();

    /**
     * 解析swap合约的本plugin关注的单个event，参数或者其他信息不满足要求就返回null
     */
    BlSwapTransaction decodeSwapEvent(BlSwapContract swapContract, TxReceiptEvent receiptEvent, int eventSeq, String txId,BlTransaction trx, String blockId);

    // 创建一个新的bl_swap_transaction对象，插入基本的字段信息
    default BlSwapTransaction createBaseSwapTransaction(BlSwapContract swapContract, TxReceiptEvent receiptEvent, int eventSeq, String txId,BlTransaction trx, String blockId) {
        BlSwapTransaction swapTx = new BlSwapTransaction();
        swapTx.setTrxId(txId);
        swapTx.setBlockId(blockId);
        swapTx.setBlockNum(receiptEvent.getBlock_num());
        swapTx.setCallerAddress(trx.getFromAccount());
        swapTx.setOpNum(receiptEvent.getOp_num());
        swapTx.setEventName(receiptEvent.getEvent_name());
        swapTx.setEventArg(receiptEvent.getEvent_arg());
        swapTx.setEventSeq(eventSeq);
        swapTx.setContractAddress(receiptEvent.getContract_address());
        swapTx.setTrxTime(trx.getTrxTime());
        return swapTx;
    }
}
