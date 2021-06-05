package com.browser.service;

import com.browser.dao.entity.BlTokenTransaction;
import com.browser.dao.entity.BlTransaction;
import com.browser.model.DaiTranRequest;
import com.browser.model.DaiTransaction;
import com.browser.protocol.EUDataGridResult;

import java.util.List;

public interface TokenTransactionService {
    int insert(BlTokenTransaction record);
    BlTokenTransaction selectByTrxIdAndEventSeq(String trxId, int eventSeq);
    EUDataGridResult getTokenTransactionList(BlTokenTransaction transaction);
    DaiTransaction getDaiTransactionList(String walletAddress, String contractAddress);
    List<BlTransaction> getTranListByOldContractAddressAndOldToAddr(DaiTranRequest daiTranRequest);
    List<String> getDaiEventCdcIdList(String eventName, String contractAddress);
    EUDataGridResult selectListByUserAddress(BlTokenTransaction transaction);
    EUDataGridResult selectTrxList(BlTokenTransaction transaction);
    List<BlTokenTransaction> selectAllByTrxId(String trxId);
}
