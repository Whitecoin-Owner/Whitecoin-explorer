package com.browser.service;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.browser.dao.entity.BlBlock;
import com.browser.dao.entity.BlContractDetail;
import com.browser.dao.entity.BlContractInfo;
import com.browser.dao.entity.BlMinerStatis;
import com.browser.dao.entity.BlStatis;
import com.browser.dao.entity.BlTransaction;

public interface StatisService {
	
	List<BlBlock> newBlockStatic();

	List<BlTransaction> newTransactionStatic();
	
	BlStatis newXwcStatic();
	
	JSONArray newTrxNumByDateStatic(BlTransaction record);
	
	JSONArray newTrxNumByHourStatic(BlTransaction record);
	
	BlMinerStatis getMinerStatis(BlMinerStatis blMinerStatis);
	
	BlMinerStatis getAddrStatis(BlMinerStatis blMinerStatis);
	BlMinerStatis addrjudge(BlMinerStatis blMinerStatis);

	BlContractDetail getContractsStatis(BlContractInfo contractInfo);

	void indexEcharts();
	void statisReward(BlBlock block);
	void statisTrxNum(Integer trxNum);
}
