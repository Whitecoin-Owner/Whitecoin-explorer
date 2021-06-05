package com.browser.service;

import com.browser.dao.entity.BlSwapTransaction;
import com.browser.protocol.EUDataGridResult;

import java.util.List;

public interface SwapTransactionService {
    BlSwapTransaction selectByTrxIdAndOpNumAndEventSeq(String trxId, int opNum, int eventSeq);
    int insert(BlSwapTransaction record);
    List<BlSwapTransaction> selectAllByTrxId(String trxId);

    EUDataGridResult selectListByUserAddress(BlSwapTransaction transaction);
}
