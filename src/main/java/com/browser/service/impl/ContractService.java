package com.browser.service.impl;

import java.math.BigDecimal;
import java.util.*;

import com.browser.tools.TimeTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.browser.config.RealData;
import com.browser.dao.entity.BlAsset;
import com.browser.dao.entity.BlContractInfo;
import com.browser.dao.entity.BlContractStatis;
import com.browser.dao.entity.BlTransaction;
import com.browser.dao.mapper.BlContractInfoMapper;
import com.browser.protocol.EUDataGridResult;
import com.browser.service.TransactionService;
import com.browser.tools.Constant;
import com.browser.tools.common.StringUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.util.CollectionUtils;

@Service
public class ContractService {

    @Value("${utcTimeZone}")
    private String TIME_ZONE;

    @Value("${utcTimeInterval}")
    private int TIME_INTERVAL;

    @Autowired
    private RealData realData;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private BlContractInfoMapper blContractInfoMapper;

    private static Map<Integer, String> contractStatusMap = null;

    static {
        contractStatusMap = new HashMap<>();
        contractStatusMap.put(0, "销毁");
        contractStatusMap.put(1, "临时");
        contractStatusMap.put(2, "永久");
    }

    /**
     * 根据地址查询拥有的合约信息
     *
     * @param blContractInfo
     * @return
     */
    public EUDataGridResult getContractList(BlContractInfo blContractInfo) {
        // 分页处理
//    	PageHelper.startPage(blContractInfo.getPage(), blContractInfo.getRows());
//    	List<BlContractInfo> list = blContractInfoMapper.selectContractList(blContractInfo);
        int offset = 0;
        int limit = 20;
        if (blContractInfo.getPage() != null && blContractInfo.getRows() != null) {
            offset = (blContractInfo.getPage() - 1) * blContractInfo.getRows();
            if (offset < 0) {
                offset = 0;
            }
            limit = blContractInfo.getRows();
            if (limit <= 0) {
                limit = 20;
            }
        }
        List<BlContractInfo> list = blContractInfoMapper.selectContractListPage(blContractInfo, offset, limit);
        if (!CollectionUtils.isEmpty(list)) {
            Date createTime;
            Date regTime;
            Date lastTime;
            Date trxTime;
            String intervalTime;
            for (BlContractInfo contractInfo : list) {
                contractInfo.setTimeZone(TIME_ZONE);
                regTime = contractInfo.getRegTime();
                if (null != regTime) {
                    regTime = new Date(regTime.getTime() - TIME_INTERVAL * 60 * 60 * 1000L);
                    contractInfo.setRegTime(regTime);
                }
                createTime = contractInfo.getCreateTime();
                if (null != createTime) {
                    createTime = new Date(createTime.getTime() - TIME_INTERVAL * 60 * 60 * 1000L);
                    contractInfo.setCreateTime(createTime);
                }
//                lastTime = contractInfo.getLastTime();
//                if (null != lastTime) {
//                    lastTime = new Date(lastTime.getTime() - TIME_INTERVAL * 60 * 60 * 1000L);
//                    contractInfo.setLastTime(lastTime);
//                }
//				BlContractStatis blContractStatis=new BlContractStatis();
                contractInfo.setContractAddress(contractInfo.getContractId());
                contractInfo.setOnwerAddress(contractInfo.getOwnerAddress());
                contractInfo.setCreateTime(contractInfo.getRegTime());

                BlTransaction transaction = new BlTransaction();
                transaction.setContractId(contractInfo.getContractId());

                List<BlTransaction> trxList = transactionService.selectCalledContract(transaction);
                if (!CollectionUtils.isEmpty(trxList)) {
                    contractInfo.setCallTimes(trxList.size());
                    trxTime = trxList.get(0).getTrxTime();
                    if (null != trxTime) {
                        intervalTime = TimeTool.getIntervalTimeStr(trxTime, new Date());
                        contractInfo.setIntervalTime(intervalTime);
                        trxTime = new Date(trxTime.getTime() - TIME_INTERVAL * 60 * 60 * 1000L);
                        contractInfo.setLastTime(trxTime);
                    }
                } else {
                    contractInfo.setCallTimes(0);
                }
            }
        }
        // 创建一个返回值对象
        EUDataGridResult result = new EUDataGridResult();
        result.setRows(list);
        // 取记录总条数
        int total;
        if (StringUtil.isEmpty(blContractInfo.getOnwerAddress())) {
            total = blContractInfoMapper.countAll();
        } else {
            total = blContractInfoMapper.countContracts(blContractInfo.getOnwerAddress());
        }
        result.setTotal(total);
        result.setPages((total + 1) / limit);
//    	PageInfo<BlContractInfo> pageInfo = new PageInfo<>(list);
//    	result.setTotal(pageInfo.getTotal());
//    	result.setPages(pageInfo.getPages());
        return result;
    }

    /**
     * 根据合约地址查询交易里合约被调用的次数
     *
     * @param
     * @return
     */
    public EUDataGridResult getContractTrxList(BlTransaction transaction) {
        // 分页处理
        PageHelper.startPage(transaction.getPage(), transaction.getRows());
        List<BlTransaction> trxList = transactionService.selectCalledContract(transaction);
        if (trxList != null && trxList.size() > 0) {
            for (BlTransaction trx : trxList) {
                if (!StringUtil.isEmpty(trx.getAssetId())) {
                    BlAsset blAsset = realData.getSymbolByAssetId(trx.getAssetId());
                    if (null != trx.getAmount() && Constant.SYMBOL.equals(blAsset.getSymbol())) {
                        trx.setAmountStr(trx.getAmount().divide(new BigDecimal(blAsset.getPrecision()))
                                .stripTrailingZeros().toPlainString() + " " + blAsset.getSymbol());
                    }
                    if (null != trx.getAmount() && !Constant.SYMBOL.equals(blAsset.getSymbol())) {
                        trx.setAmountStr(trx.getAmount().stripTrailingZeros().toPlainString() + " " + blAsset.getSymbol());
                    }
                }
                if (StringUtil.isEmpty(trx.getAssetId()) && !StringUtil.isEmpty(trx.getSymbol())) {
                    if (null != trx.getAmount() && !Constant.SYMBOL.equals(trx.getSymbol())) {
                        trx.setAmountStr(trx.getAmount().stripTrailingZeros().toPlainString() + " " + trx.getSymbol());
                    }
                }
                if (null != trx.getFee()) {
                    trx.setFeeStr(trx.getFee().divide(new BigDecimal(Constant.PRECISION))
                            .stripTrailingZeros().toPlainString() + " " + Constant.SYMBOL);
                }
            }
        }
        // 创建一个返回值对象
        EUDataGridResult result = new EUDataGridResult();
        result.setRows(trxList);
        // 取记录总条数
        PageInfo<BlTransaction> pageInfo = new PageInfo<>(trxList);
        result.setTotal(pageInfo.getTotal());
        result.setPages(pageInfo.getPages());
        return result;
    }

    public JSONObject getAbi(BlContractInfo blContractInfo) {
        JSONObject json = new JSONObject();
        BlContractInfo contractInfo = blContractInfoMapper.selectByPrimaryKey(blContractInfo.getContractId());
        JSONArray abi = JSONObject.parseObject(contractInfo.getCode()).getJSONArray("abi");
        json.put("abi", abi);
        return json;
    }

    public JSONObject getEvents(BlContractInfo blContractInfo) {
        JSONObject json = new JSONObject();
        BlContractInfo contractInfo = blContractInfoMapper.selectByPrimaryKey(blContractInfo.getContractId());
        JSONArray events = JSONObject.parseObject(contractInfo.getCode()).getJSONArray("events");
        json.put("events", events);
        return json;
    }
}
