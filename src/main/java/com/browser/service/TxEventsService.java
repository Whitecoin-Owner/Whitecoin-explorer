package com.browser.service;

import com.browser.bean.LiquidateResult;
import com.browser.dao.entity.BlTxEvents;
import com.browser.dao.entity.ResultMsg;

import java.util.List;

public interface TxEventsService {
    int insert(BlTxEvents record);
    BlTxEvents selectByTrxIdAndEventSeq(String trxId, int eventSeq);
    List<BlTxEvents> selectAllByTrxId(String trxId);

    ResultMsg selectAllLiquidateEvent(Integer currentPage, Integer pageSize);
}
