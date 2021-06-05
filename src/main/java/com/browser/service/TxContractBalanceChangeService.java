package com.browser.service;

import com.browser.dao.entity.BlTxContractBalanceChange;

import java.util.List;

public interface TxContractBalanceChangeService {
    int insert(BlTxContractBalanceChange record);
    BlTxContractBalanceChange selectByTrxIdAndChangeTypeAndChangeSeq(String trxId, String changeType, int changeSeq);
    List<BlTxContractBalanceChange> selectAllByTrxId(String trxId);
}
