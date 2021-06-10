package com.browser.service.impl;

import java.math.BigDecimal;
import java.util.*;

import com.browser.tools.DecodeMemo;
import com.browser.tools.TimeTool;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.browser.config.RealData;
import com.browser.dao.entity.BlAsset;
import com.browser.dao.entity.BlContractInfo;
import com.browser.dao.entity.BlTransaction;
import com.browser.dao.entity.TransOpTypeRes;
import com.browser.dao.mapper.BlBlockMapper;
import com.browser.dao.mapper.BlContractInfoMapper;
import com.browser.dao.mapper.BlTransactionMapper;
import com.browser.protocol.EUDataGridResult;
import com.browser.service.TransactionService;
import com.browser.tools.Constant;
import com.browser.tools.common.StringUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.util.CollectionUtils;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Value("${utcTimeZone}")
    private String TIME_ZONE;

    @Value("${utcTimeInterval}")
    private int TIME_INTERVAL;

    @Autowired
    private RealData realData;

    @Autowired
    private RedisService redisService;

    @Autowired
    private TransTypeResolveService transTypeResolveService;

    @Autowired
    private BlTransactionMapper blTransactionMapper;

    @Autowired
    private BlContractInfoMapper blContractInfoMapper;

    @Autowired
    private BlBlockMapper blockMapper;

    @Autowired
    private RequestWalletService requestWalletService;

//    private static Set<Integer> typeSet = new HashSet<Integer>();
//
//    static {
//        typeSet.add(10);
//        typeSet.add(84);
//        typeSet.add(83);
//        typeSet.add(82);
//        typeSet.add(81);
//        typeSet.add(79);
//        typeSet.add(77);
//        typeSet.add(76);
//        typeSet.add(73);
//        typeSet.add(65);
//        typeSet.add(61);
//        typeSet.add(60);
//        typeSet.add(26);
//        typeSet.add(24);
//        typeSet.add(5);
//        typeSet.add(0);
//        typeSet.add(55);
//        typeSet.add(56);
//    }

    @Override
    public void insertBatchTransaction(List<BlTransaction> list) {
        for (int j = 0; j < list.size(); j = j + Constant.BATCH_LENGTH) {
            List<BlTransaction> sub = null;
            if (j + Constant.BATCH_LENGTH > list.size()) {
                sub = list.subList(j, list.size());
            } else {
                sub = list.subList(j, j + Constant.BATCH_LENGTH);
            }
            blTransactionMapper.insertBatch(sub);
        }
    }

    @Override
    public EUDataGridResult getTransactionList(BlTransaction transaction) {
        Integer sortByIdOrBlockNum = transaction.getSortByIdOrBlockNum();
        if (null == sortByIdOrBlockNum) {
            sortByIdOrBlockNum = 1;
        } else {
            sortByIdOrBlockNum = 2;
        }
        transaction.setSortByIdOrBlockNum(sortByIdOrBlockNum);
        // 分页处理
        PageHelper.startPage(transaction.getPage(), transaction.getRows());
        List<BlTransaction> list = blTransactionMapper.getTransactionList(transaction);
        if (!CollectionUtils.isEmpty(list)) {
            Date txTime;
            String intervalTime;
            for (BlTransaction trx : list) {
                trx.setTimeZone(TIME_ZONE);
                txTime = trx.getTrxTime();
                // 时间统一使用UTC，减去8小时
                if (null != txTime) {
                    intervalTime = TimeTool.getIntervalTimeStr(txTime, new Date());
                    trx.setIntervalTime(intervalTime);
                    txTime = new Date(txTime.getTime() - TIME_INTERVAL * 60 * 60 * 1000L);
                    trx.setTrxTime(txTime);
                }
                handleAmountData(trx);
                handleDiffOpTypeData(trx);
                trx.setGuaranteeUse(false);
                if (null != trx.getGuaranteeId()) {
                    trx.setGuaranteeUse(true);
                }

                if (null != trx.getOpType() && trx.getOpType() == 73) {
                    trx.setFromAccount("Mining");
                }


            }
        }
        // 创建一个返回值对象
        EUDataGridResult result = new EUDataGridResult();
        result.setRows(list);
        // 取记录总条数
        PageInfo<BlTransaction> pageInfo = new PageInfo<>(list);
        result.setTotal(pageInfo.getTotal());
        result.setPages(pageInfo.getPages());
        return result;
    }

    /**
     * 根据地址查询交易信息
     */
    @Override
    public EUDataGridResult selectMinerTrxList(BlTransaction transaction) {
        // 分页处理
        PageHelper.startPage(transaction.getPage(), transaction.getRows());
        List<BlTransaction> list = blTransactionMapper.selectMinerTrxList(transaction.getAddress());
        if (!CollectionUtils.isEmpty(list)) {
            Date txTime;
            Date createTime;
            String intervalTime;
            String memo;
            for (BlTransaction trx : list) {
                handleAmountData(trx);
                memo = trx.getMemo();
                if (!StringUtil.isEmpty(memo)) {
                    memo = new String(DecodeMemo.toByteArray(memo));
                    trx.setMemo(memo.trim());
                }
                trx.setTimeZone(TIME_ZONE);
                txTime = trx.getTrxTime();
                if (null != txTime) {
                    intervalTime = TimeTool.getIntervalTimeStr(txTime, new Date());
                    trx.setIntervalTime(intervalTime);
                    txTime = new Date(txTime.getTime() - TIME_INTERVAL * 60 * 60 * 1000L);
                    trx.setTrxTime(txTime);
                }
                createTime = trx.getCreatedTime();
                if (null != createTime) {
                    createTime = new Date(createTime.getTime() - TIME_INTERVAL * 60 * 60 * 1000L);
                    trx.setCreatedTime(createTime);
                }
                if (StringUtils.isEmpty(trx.getFromAccount())) {
                    trx.setFromAccount("Mining");
                }
            }

        }
        // 创建一个返回值对象
        EUDataGridResult result = new EUDataGridResult();
        result.setRows(list);
        // 取记录总条数
        PageInfo<BlTransaction> pageInfo = new PageInfo<>(list);
        result.setTotal(pageInfo.getTotal());
        result.setPages(pageInfo.getPages());
        return result;
    }

    /**
     * 根据合约地址查询交易
     */
    @Override
    public List<BlTransaction> selectCalledContract(BlTransaction record) {
        return blTransactionMapper.selectCalledContract(record);
    }

    @Override
    public EUDataGridResult queryBlockTxNum(BlTransaction transaction) {
        // 分页处理
        PageHelper.startPage(transaction.getPage(), transaction.getRows());
        List<BlTransaction> list = blTransactionMapper.queryBlockTxNum(transaction.getBlockNum());
        if (list != null && list.size() > 0) {
            for (BlTransaction trx : list) {
                handleAmountData(trx);
            }
        }
        // 创建一个返回值对象
        EUDataGridResult result = new EUDataGridResult();
        result.setRows(list);
        // 取记录总条数
        PageInfo<BlTransaction> pageInfo = new PageInfo<>(list);
        result.setTotal(pageInfo.getTotal());
        result.setPages(pageInfo.getPages());
        return result;
    }

    @Override
    public TransOpTypeRes getOperationDetail(BlTransaction transaction) {
        TransOpTypeRes transOpTypeRes = null;
        List<JSONObject> data = new ArrayList<JSONObject>();
        // List<String> dataStr = new ArrayList<String>();
        List<BlTransaction> trx = blTransactionMapper.getTransactionByTxId(transaction);
        if (!CollectionUtils.isEmpty(trx)) {
            Date txTime;
            String intervalTime;
            for (BlTransaction trans : trx) {
                transOpTypeRes = new TransOpTypeRes();
                transOpTypeRes.setTxHash(trans.getTrxId());
                transOpTypeRes.setTxType(trans.getOpType());
                transOpTypeRes.setTxStatus(Constant.TRX_STATUS);
                if (Constant.TRX_TYPE_WITHDRAW_REQ == trans.getOpType()
                        || Constant.GURANTEE_CREATE_OPERATION == trans.getOpType()) {
                    transOpTypeRes.setTxStatus(trans.getExtension1());
                }
                // 时间统一使用UTC，减去8小时
                txTime = trans.getTrxTime();
                if (null != txTime) {
                    intervalTime = TimeTool.getIntervalTimeStr(txTime, new Date());
                    transOpTypeRes.setIntervalTime(intervalTime);
                    txTime = new Date(txTime.getTime() - TIME_INTERVAL * 60 * 60 * 1000L);
                }
                transOpTypeRes.setTimeStamp(txTime);
                transOpTypeRes.setTimeZone(TIME_ZONE);
                transOpTypeRes.setBlockHeight(trans.getBlockNum());
                handleAmountData(trans);

                JSONObject json = new JSONObject();
                if (trans.getOpType() != Constant.ACCOUNT_REGISTER) {
                    try {
                        json = JSONObject.parseObject(trans.getExtension());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                JSONObject result = transTypeResolveService.transTypeResolve(trans.getOpType(), json, trans);
                if (result != null) {
                    if (Constant.TRX_TYPE_WITHDRAW_REQ == trans.getOpType()) {
                        JSONObject withdrawResult = handleWithdrawData(trans);
                        result.put("withdrawResult", withdrawResult);
                    }
                    data.add(result);
                } else {
                    data.add(json);
                }
                transOpTypeRes.setFail(trans.getFail());

            }
            transOpTypeRes.setOperationData(data);
        }
        return transOpTypeRes;
    }

    @Override
    public String selectextraTrxId(String trxId, Integer opType) {
        BlTransaction trans = new BlTransaction();
        trans.setTrxId(trxId);
        trans.setOpType(opType);
        return blTransactionMapper.selectextraTrxId(trans);
    }

    @Override
    public String selectCrossAddr(String trxId, Integer opType) {
        BlTransaction trans = new BlTransaction();
        trans.setTrxId(trxId);
        trans.setOpType(opType);
        return blTransactionMapper.selectCrossAddr(trans);
    }

    @Override
    public String selectExtension(String extraTrxId, Integer opType) {
        BlTransaction trans = new BlTransaction();
        trans.setExtraTrxId(extraTrxId);
        trans.setOpType(opType);
        return blTransactionMapper.selectExtension(trans);
    }

    @Override
    public int updateByTxIdAndType(String trxId, Integer status) {
        BlTransaction trans = new BlTransaction();
        trans.setTrxId(trxId);
        trans.setExtension1(status);
        return blTransactionMapper.updateByTxIdAndType(trans);
    }

    @Override
    public int updateGuranteeStatus(String addr, Integer status) {
        BlTransaction trans = new BlTransaction();
        trans.setFromAccount(addr);
        trans.setExtension1(status);
        return blTransactionMapper.updateGuranteeStatus(trans);
    }

    private void handleAmountData(BlTransaction trx) {

        if (!StringUtil.isEmpty(trx.getAssetId())) {
            BlAsset blAsset = realData.getSymbolByAssetId(trx.getAssetId());
            if (trx.getOpType() == Constant.ASSET_MORGAGE || trx.getOpType() == Constant.ASSET_REDEEM) {
                trx.setAmountStr(trx.getAmount().divide(new BigDecimal(blAsset.getPrecision())).stripTrailingZeros()
                        .toPlainString() + " " + blAsset.getSymbol());
            } else {

                if (null != trx.getAmount() && Constant.SYMBOL.equals(blAsset.getSymbol())) {
                    trx.setAmountStr(trx.getAmount().divide(new BigDecimal(blAsset.getPrecision())).stripTrailingZeros()
                            .toPlainString() + " " + blAsset.getSymbol());
                }
                if (null != trx.getAmount() && !Constant.SYMBOL.equals(blAsset.getSymbol())) {
                    trx.setAmountStr(trx.getAmount().stripTrailingZeros().toPlainString() + " " + blAsset.getSymbol());
                }
            }
        }

        if (StringUtil.isEmpty(trx.getAssetId()) && !StringUtil.isEmpty(trx.getSymbol())) {
            if (null != trx.getAmount() && !Constant.SYMBOL.equals(trx.getSymbol())) {
                trx.setAmountStr(trx.getAmount().stripTrailingZeros().toPlainString() + " " + trx.getSymbol());
            }
        }
        if (Constant.TRX_TYPE_TRANSFER == trx.getOpType() || Constant.CONTRACT_TRANSFER_OPERATION == trx.getOpType()) {
            BlAsset blAsset = realData.getSymbolByAssetId(trx.getAssetId());
            trx.setAmountStr(
                    trx.getAmount().divide(new BigDecimal(blAsset.getPrecision())).stripTrailingZeros().toPlainString()
                            + " " + blAsset.getSymbol());
        }

        if (Constant.TRX_TYPE_PAY_BACK == trx.getOpType()) {
            trx.setAmountStr(
                    trx.getAmount().divide(new BigDecimal(Constant.PRECISION)).stripTrailingZeros().toPlainString()
                            + " " + Constant.SYMBOL);
        }

        if (null != trx.getFee()) {
            trx.setFeeStr(trx.getFee().divide(new BigDecimal(Constant.PRECISION)).stripTrailingZeros().toPlainString()
                    + " " + Constant.SYMBOL);
        }
    }

    private void handleDiffOpTypeData(BlTransaction trx) {
        if (Constant.PARENT_CONTRACT == trx.getParentOpType()) {
            BlContractInfo contractInfo = blContractInfoMapper.selectByPrimaryKey(trx.getContractId());
            if (contractInfo != null) {
                trx.setAuthorAddr(contractInfo.getOwnerAddress());
            }
        }
        if (Constant.GURANTEE_CREATE_OPERATION == trx.getOpType()) {
            JSONObject json = JSONObject.parseObject(trx.getExtension());
            JSONObject result = transTypeResolveService.transTypeResolve(trx.getOpType(), json, trx);
            String exchange = result.getString("originSymbol") + ":" + result.getString("targetSymbol") + "  "
                    + result.getString("rate");
            trx.setExchange(exchange);
        }
    }

    private JSONObject handleWithdrawData(BlTransaction trx) {
        JSONObject json = new JSONObject();
        if (Constant.TRX_TYPE_WITHDRAW_REQ == trx.getOpType()) {
            BlTransaction transResult = new BlTransaction();
            transResult.setOpType(Constant.TRX_TYPE_WITHDRAW_SUCC);
            List<BlTransaction> tx = blTransactionMapper.getTransactionList(transResult);
            if (tx != null && tx.size() > 0) {
                for (BlTransaction trans : tx) {

                    String withdrawTrx = redisService.getCrosschainTxId(trans.getExtraTrxId());
                    if (StringUtil.isEmpty(withdrawTrx)) {
                        String extension = selectExtension(trans.getExtraTrxId(), Constant.TRX_TYPE_WITHDRAW_SEND);
                        JSONObject extensionJson = JSONObject.parseObject(extension);
                        withdrawTrx = extensionJson.getString("withdraw_trx");
                    }

                    String withdrawTrxIds = redisService.getUnSignTxId(withdrawTrx);
                    if (StringUtil.isEmpty(withdrawTrxIds)) {
                        withdrawTrxIds = selectextraTrxId(withdrawTrx, Constant.TRX_TYPE_WITHDRAW_CREATE);
                    }

                    JSONArray jsa = JSONObject.parseArray(withdrawTrxIds);
                    if (jsa != null && jsa.size() > 0) {
                        for (int i = 0; i < jsa.size(); i++) {
                            if (jsa.contains(trx.getTrxId())) {
                                json.put("txHash", trans.getExtraTrxId());
                                json.put("minerAddr", trans.getMinerAddress());
                                String name = blockMapper.getMinerName(trans.getMinerAddress());
                                json.put("minerName", name);
                            }
                        }
                    }
                }
            }
        }
        return json;
    }


}
