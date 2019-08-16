package com.browser.service;

import java.util.List;

import com.browser.dao.entity.BlTransaction;
import com.browser.dao.entity.TransOpTypeRes;
import com.browser.protocol.EUDataGridResult;

public interface TransactionService {

	// BlTransaction queryByTrxId(String trxId);

	EUDataGridResult getTransactionList(BlTransaction transaction);

	void insertBatchTransaction(List<BlTransaction> list);

	EUDataGridResult selectMinerTrxList(BlTransaction transaction);

	List<BlTransaction> selectCalledContract(BlTransaction transaction);

	EUDataGridResult queryBlockTxNum(BlTransaction transaction);

	TransOpTypeRes getOperationDetail(BlTransaction transaction);

	// 提现流程状
	String selectextraTrxId(String trxId,Integer opType);

	String selectCrossAddr(String trxId,Integer opType);

	String selectExtension(String extraTrxId,Integer opType);
	
	int updateByTxIdAndType(String trxId,Integer opType);
	
	int updateGuranteeStatus(String addr,Integer status);

}
