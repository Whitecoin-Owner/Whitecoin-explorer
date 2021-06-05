package com.browser.dao.mapper;

import com.browser.dao.entity.BlTransaction;
import com.browser.model.DaiTranRequest;

import java.math.BigDecimal;
import java.util.List;

public interface BlTransactionMapper {
    int deleteByPrimaryKey(String trxId);

    int deleteByBlockNum(Long blockNum);

    int insert(BlTransaction record);

    int insertBatch(List<BlTransaction> record);

    int insertSelective(BlTransaction record);

    BigDecimal selectRewards(String address);

    BlTransaction selectByPrimaryKey(String trxId);

    BlTransaction judgeAddr(String address);

    List<BlTransaction> selectNewTrxInfo();

    List<BlTransaction> getTranListByOldContractAddressAndOldToAddr(DaiTranRequest daiTranRequest);

    List<BlTransaction> getTransactionList(BlTransaction record);

    BlTransaction countTrxNumByDate(BlTransaction record);

    String selectCurDay();

    List<BlTransaction> countTrxNumByHour(BlTransaction record);

    List<BlTransaction> selectMinerTrxList(String minerAddress);

    List<BlTransaction> selectCalledContract(BlTransaction record);

    Integer countContractTrxNum(String contractId);

    String getContractCreateTxId(String contractId);

    String selectextraTrxId(BlTransaction record);

    String selectCrossAddr(BlTransaction record);

    String selectExtension(BlTransaction record);

    String selectLockTrxId(BlTransaction record);

    List<BlTransaction> queryBlockTxNum(Long blockNum);

    List<BlTransaction> getTransactionByTxId(BlTransaction record);

    int updateByPrimaryKeySelective(BlTransaction record);

    int updateByPrimaryKey(BlTransaction record);

    int updateByTxIdAndType(BlTransaction record);

    int updateGuranteeStatus(BlTransaction record);

    int countTrxNum();

    int countAddrTrxNum(String address);

}