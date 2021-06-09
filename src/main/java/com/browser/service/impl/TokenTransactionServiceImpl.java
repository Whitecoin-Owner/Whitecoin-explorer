package com.browser.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.browser.dao.entity.*;
import com.browser.dao.mapper.*;
import com.browser.model.DaiTranRecord;
import com.browser.model.DaiTranRequest;
import com.browser.model.DaiTransaction;
import com.browser.protocol.EUDataGridResult;
import com.browser.service.TokenTransactionService;
import com.browser.tools.TimeTool;
import com.browser.tools.common.DateUtil;
import com.browser.tools.common.StringUtil;
import com.browser.wallet.PrecisionUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TokenTransactionServiceImpl implements TokenTransactionService {

    @Value("${utcTimeZone}")
    private String TIME_ZONE;

    @Value("${utcTimeInterval}")
    private int TIME_INTERVAL;

    @Resource
    private BlTokenTransactionMapper blTokenTransactionMapper;
    @Resource
    private BlTokenMapper blTokenMapper;

    @Resource
    private BlAssetMapper blAssetMapper;

    @Resource
    private BlTxEventsMapper blTxEventsMapper;

    @Resource
    private BlTransactionMapper blTransactionMapper;

    @Override
    public int insert(BlTokenTransaction record) {
        return blTokenTransactionMapper.insert(record);
    }

    @Override
    public BlTokenTransaction selectByTrxIdAndEventSeq(String trxId, int eventSeq) {
        BlTokenTransaction cond = new BlTokenTransaction();
        cond.setTrxId(trxId);
        cond.setEventSeq(eventSeq);
        List<BlTokenTransaction> records = blTokenTransactionMapper.selectAllByCond(cond);
        if (records.isEmpty()) {
            return null;
        }
        handleAmountData(records.get(0));
        return records.get(0);
    }


    private void handleAmountData(BlTokenTransaction trx) {

        String contractId = trx.getContractId();
        if (StringUtil.isEmpty(contractId)) {
            return;
        }
        BlToken token = blTokenMapper.selectByContractAddress(contractId);
        if (token == null || token.getTokenSymbol() == null || token.getPrecision() == null) {
            return;
        }
        trx.setTokenInfo(token);
        BigDecimal precision = PrecisionUtils.decimalsToPrecision(token.getPrecision());
        if (trx.getAmount() != null) {
            trx.setAmountStr(new BigDecimal(trx.getAmount()).setScale(token.getPrecision(), RoundingMode.FLOOR).divide(precision, RoundingMode.FLOOR).toString());
        }
        if (trx.getFee() != null) {
            trx.setFeeStr(new BigDecimal(trx.getFee()).setScale(token.getPrecision(), RoundingMode.FLOOR).divide(precision, RoundingMode.FLOOR).toString());
        }
    }


    @Override
    public EUDataGridResult getTokenTransactionList(BlTokenTransaction transaction) {
        // 分页处理
        PageHelper.startPage(transaction.getPage(), transaction.getRows());
        List<BlTokenTransaction> list = blTokenTransactionMapper.selectAllByCond(transaction);
        if (!CollectionUtils.isEmpty(list)) {
            Date txTime;
            String intervalTime;
            for (BlTokenTransaction trx : list) {
                handleAmountData(trx);
                trx.setTimeZone(TIME_ZONE);
                txTime = trx.getTrxTime();
                if (null != txTime) {
                    intervalTime = TimeTool.getIntervalTimeStr(txTime, new Date());
                    trx.setIntervalTime(intervalTime);
                    txTime = new Date(txTime.getTime() - TIME_INTERVAL * 60 * 60 * 1000L);
                    trx.setTrxTime(txTime);
                }
            }
        }
        // 创建一个返回值对象
        EUDataGridResult result = new EUDataGridResult();
        result.setRows(list);
        // 取记录总条数
        PageInfo<BlTokenTransaction> pageInfo = new PageInfo<>(list);
        result.setTotal(pageInfo.getTotal());
        result.setPages(pageInfo.getPages());
        return result;
    }

    @Override
    public DaiTransaction getDaiTransactionList(String walletAddress, String contractAddress) {
        List<BlTxEvents> blTxEventList = blTxEventsMapper.getDaiTxOpenCdcEventListByCondition(walletAddress, contractAddress);
        DaiTransaction daiTransaction = new DaiTransaction();
        if (!CollectionUtils.isEmpty(blTxEventList)) {
            String eventArg;
            JSONObject eventArgJson;
            String cdcId;
            Integer closeCdc;
            List<BlTxEvents> blTxEventListNotCloseCdc = new ArrayList<>(blTxEventList.size());
            for (BlTxEvents blTxEvents : blTxEventList) {
                eventArg = blTxEvents.getEventArg();
                if (!StringUtils.isEmpty(eventArg)) {
                    eventArgJson = JSONObject.parseObject(eventArg);
                    if (null != eventArgJson) {
                        if (eventArgJson.containsKey("cdcId")) {
                            cdcId = eventArgJson.getString("cdcId");
                            if (!StringUtils.isEmpty(cdcId)) {
                                closeCdc = blTxEventsMapper.existsCloseCdcByCdcId(cdcId);
                                if (null == closeCdc) {
                                    blTxEventListNotCloseCdc.add(blTxEvents);
                                }
                            }
                        }
                    }
                }
            }
            if (!CollectionUtils.isEmpty(blTxEventListNotCloseCdc)) {
                blTxEventListNotCloseCdc = blTxEventListNotCloseCdc.stream()
                        .sorted(Comparator.comparing(BlTxEvents::getBlockNum)
                                .reversed()).collect(Collectors.toList());
                BlTxEvents blTxEvents = blTxEventListNotCloseCdc.get(0);
                String eventArgs = blTxEvents.getEventArg();
                if (!StringUtils.isEmpty(eventArgs)) {
                    eventArgJson = JSONObject.parseObject(eventArgs);
                    if (null != eventArgJson) {
                        if (eventArgJson.containsKey("cdcId")) {
                            cdcId = eventArgJson.getString("cdcId");
                            daiTransaction.setCdcId(cdcId);
                        } else {
                            daiTransaction.setCdcId(null);
                        }
                    } else {
                        daiTransaction.setCdcId(null);
                    }
                } else {
                    daiTransaction.setCdcId(null);
                }

            } else {
                daiTransaction.setCdcId(null);
            }
        } else {
            daiTransaction.setCdcId(null);
        }

        List<BlTxEvents> blTxEventAllList = blTxEventsMapper.getDaiTxcEventListByCondition(walletAddress, contractAddress);
        List<DaiTranRecord> daiTranRecords = new ArrayList<>(blTxEventAllList.size());
        if (!CollectionUtils.isEmpty(blTxEventAllList)) {
            DaiTranRecord daiTranRecord;
            List<BlTransaction> transactionList;
            BlTransaction record;
            BigDecimal amount;
            String amountStr = "0";
            String eventName;
            String eventArgsStr;
            JSONObject eventArgsJson = null;
            BigDecimal collateralAmount;
            BigDecimal stableTokenAmount;
            String collateralAmountStr = "0";
            String stableTokenAmountStr = "0";
            Integer xusdPrecision;
            Integer xwcPrecision;
            Integer xwcPrecisionLen;
            Long xwcPrecisionLong;
            BlAsset blAsset;
            // 加入清算账单
            List<BlTxEvents> blTxLiquidateEventList = blTxEventsMapper.getDaiTxLiquidateEventListByCondition(contractAddress);
            if (!CollectionUtils.isEmpty(blTxLiquidateEventList)) {
                String eventArg;
                List<BlTxEvents> liquidateEventList = new ArrayList<>(blTxLiquidateEventList.size());
                for (BlTxEvents blTxEvents : blTxLiquidateEventList) {
                    eventArg = blTxEvents.getEventArg();
                    if (!StringUtils.isEmpty(eventArg)) {
                        eventArgsJson = JSONObject.parseObject(eventArg);
                        if (eventArgsJson.containsKey("owner") && walletAddress.equalsIgnoreCase(eventArgsJson.getString("owner"))) {
                            liquidateEventList.add(blTxEvents);
                        }
                    }
                }
                blTxEventAllList.addAll(liquidateEventList);
            }
            for (BlTxEvents txEvents : blTxEventAllList) {
                xusdPrecision = blTokenMapper.getPrecisionByContractAddress(txEvents.getContractAddress());
                if (null == xusdPrecision) {
                    xusdPrecision = 8;
                }
                blAsset = blAssetMapper.selectBySymbol("XWC");
                if (null != blAsset) {
                    xwcPrecisionLong = blAsset.getPrecision();
                    if (null == xwcPrecisionLong) {
                        xwcPrecision = 8;
                    } else {
                        xwcPrecisionLen = String.valueOf(xwcPrecisionLong).length();
                        if (xwcPrecisionLen > 1) {
                            xwcPrecision = xwcPrecisionLen - 1;
                        } else {
                            xwcPrecision = 8;
                        }
                    }
                } else {
                    xwcPrecision = 8;
                }
                daiTranRecord = new DaiTranRecord();
                daiTranRecord.setAddress(txEvents.getCallerAddr());
                daiTranRecord.setTxHash(txEvents.getTrxId());
                eventName = txEvents.getEventName();
                eventArgsStr = txEvents.getEventArg();
                if (!StringUtils.isEmpty(eventArgsStr)) {
                    eventArgsJson = JSONObject.parseObject(eventArgsStr);
                }
                if ("OpenCdc".equalsIgnoreCase(eventName) || "CloseCdc".equalsIgnoreCase(eventName)) {
                    if (null != eventArgsJson) {
                        if (eventArgsJson.containsKey("stableTokenAmount")) {
                            stableTokenAmount = eventArgsJson.getBigDecimal("stableTokenAmount");
                            if (null != stableTokenAmount) {
                                stableTokenAmountStr = stableTokenAmount.divide(BigDecimal.valueOf(Math.pow(10, xusdPrecision))).stripTrailingZeros().toPlainString();
                            } else {
                                stableTokenAmountStr = "0";
                            }
                        }
                        if (eventArgsJson.containsKey("collateralAmount")) {
                            collateralAmount = eventArgsJson.getBigDecimal("collateralAmount");
                            if (null != collateralAmount) {
                                collateralAmountStr = collateralAmount.divide(BigDecimal.valueOf(Math.pow(10, xwcPrecision))).stripTrailingZeros().toPlainString();
                            } else {
                                collateralAmountStr = "0";
                            }
                        }
                    } else {
                        stableTokenAmountStr = "0";
                        collateralAmountStr = "0";
                    }
                } else {
                    stableTokenAmountStr = "0";
                    collateralAmountStr = "0";
                }
                daiTranRecord.setStableTokenAmount(stableTokenAmountStr);
                daiTranRecord.setCollateralAmount(collateralAmountStr);
                daiTranRecord.setCollateralCoinType("XWC");
                daiTranRecord.setStableTokeCoinType("XUSD");
                // TODO 事由：这个先根据事件类型去判断，后面加入其它币再说，前期主要是XUSD和XWC，处理人：李明成
                if ("ExpandLoan".equalsIgnoreCase(eventName)) {
                    daiTranRecord.setAssetType("XUSD");
                    if (null != eventArgsJson) {
                        if (eventArgsJson.containsKey("expandLoanAmount")) {
                            amount = eventArgsJson.getBigDecimal("expandLoanAmount");
                            if (null != amount) {
                                amountStr = amount.divide(BigDecimal.valueOf(Math.pow(10, xusdPrecision))).stripTrailingZeros().toPlainString();
                            } else {
                                amountStr = "0";
                            }
                        }
                    }
                }
                if ("PayBack".equalsIgnoreCase(eventName)) {
                    daiTranRecord.setAssetType("XUSD");
                    if (null != eventArgsJson) {
                        if (eventArgsJson.containsKey("payBackAmount")) {
                            amount = eventArgsJson.getBigDecimal("payBackAmount");
                            if (null != amount) {
                                amountStr = amount.divide(BigDecimal.valueOf(Math.pow(10, xusdPrecision))).stripTrailingZeros().toPlainString();
                            } else {
                                amountStr = "0";
                            }
                        }
                    }
                }
                if ("WidrawCollateral".equalsIgnoreCase(eventName)) {
                    daiTranRecord.setAssetType("XWC");
                    if (null != eventArgsJson) {
                        if (eventArgsJson.containsKey("widrawCollateralAmount")) {
                            amount = eventArgsJson.getBigDecimal("widrawCollateralAmount");
                            if (null != amount) {
                                amountStr = amount.divide(BigDecimal.valueOf(Math.pow(10, xwcPrecision))).stripTrailingZeros().toPlainString();
                            } else {
                                amountStr = "0";
                            }
                        }
                    }
                }
                if ("AddCollateral".equalsIgnoreCase(eventName)) {
                    daiTranRecord.setAssetType("XWC");
                    if (null != eventArgsJson) {
                        if (eventArgsJson.containsKey("addAmount")) {
                            amount = eventArgsJson.getBigDecimal("addAmount");
                            if (null != amount) {
                                amountStr = amount.divide(BigDecimal.valueOf(Math.pow(10, xwcPrecision))).stripTrailingZeros().toPlainString();
                            } else {
                                amountStr = "0";
                            }
                        }
                    }
                }
                if ("Liquidate".equalsIgnoreCase(eventName)) {
                    daiTranRecord.setAssetType("XWC");
                    if (null != eventArgsJson) {
                        if (eventArgsJson.containsKey("returnAmount")) {
                            amount = eventArgsJson.getBigDecimal("returnAmount");
                            if (null != amount) {
                                amountStr = amount.divide(BigDecimal.valueOf(Math.pow(10, xwcPrecision))).stripTrailingZeros().toPlainString();
                            } else {
                                amountStr = "0";
                            }
                        }
                    }
                }
                daiTranRecord.setType(eventName);
                record = new BlTransaction();
                record.setTrxId(txEvents.getTrxId());
                transactionList = blTransactionMapper.getTransactionList(record);
                if (!CollectionUtils.isEmpty(transactionList)) {
                    record = transactionList.get(0);
                    daiTranRecord.setTime(DateUtil.parseStr(record.getTrxTime(), DateUtil.C_TIME_PATTON_DEFAULT));
                    daiTranRecord.setAmount(amountStr);
                }
                daiTranRecords.add(daiTranRecord);
            }
        }
        if (!CollectionUtils.isEmpty(daiTranRecords)) {
            daiTranRecords = daiTranRecords.stream()
                    .sorted(Comparator.comparing(DaiTranRecord::getTime)
                            .reversed()).collect(Collectors.toList());
        }
        daiTransaction.setTranRecords(daiTranRecords);
        /** 获取全站信息 2021.05，17 moloqu新增 **/
        queryAllSiteInfo(daiTransaction);
        return daiTransaction;
    }

    private void queryAllSiteInfo(DaiTransaction daiTransaction) {
        JSONObject allSiteInfo = blTxEventsMapper.queryAllSiteInfo();
        if (allSiteInfo == null) {
            return;
        }
        BigDecimal realPayBackAmount = allSiteInfo.getBigDecimal("realPayBackAmount");
        BigDecimal addCollateralAmount = allSiteInfo.getBigDecimal("addCollateralAmount");
        BigDecimal closeCdcXUSDAmount = allSiteInfo.getBigDecimal("closeCdcXUSDAmount");
        BigDecimal expandLoanAmount = allSiteInfo.getBigDecimal("expandLoanAmount");
        BigDecimal openCdcXWCAmount = allSiteInfo.getBigDecimal("openCdcXWCAmount");
        BigDecimal openCdcXUSDAmount = allSiteInfo.getBigDecimal("openCdcXUSDAmount");
        BigDecimal liquidateXWCAmount = allSiteInfo.getBigDecimal("liquidateXWCAmount");
        BigDecimal closeCdcXWCAmount = allSiteInfo.getBigDecimal("closeCdcXWCAmount");
        BigDecimal widrawCollateralAmount = allSiteInfo.getBigDecimal("widrawCollateralAmount");
        BigDecimal liquidateXUSEAmount = allSiteInfo.getBigDecimal("liquidateXUSEAmount");

        /** 已抵押资产
         *
         * 已抵押资产: 开仓XWC数量 +抵押XWC数量 -提现XWC数量 -移仓XWC数量 -清算XWC数量
         * **/
        BigDecimal alreadyMortgageAmount = (openCdcXWCAmount.add(addCollateralAmount)
                .subtract(widrawCollateralAmount).subtract(closeCdcXWCAmount)
                .subtract(liquidateXWCAmount)).divide(BigDecimal.valueOf(Math.pow(10, 8)));

        /** 已借贷资产
         *
         * 已借贷资产：开仓XUSD数量+借贷XUSD数量-返还XUSD数量-移仓XUSD数量 -清算XUSD数量
         * **/
        BigDecimal alreadyLoanAmount = (openCdcXUSDAmount.add(expandLoanAmount)
                .subtract(realPayBackAmount).subtract(closeCdcXUSDAmount)
                .subtract(liquidateXUSEAmount)).divide(BigDecimal.valueOf(Math.pow(10, 8)));

        /** 清算罚款收入：所有被清算的罚款金额相加 **/
        BigDecimal liquidationFineAmount = allSiteInfo.getBigDecimal("penaltyAmount").divide(BigDecimal.valueOf(Math.pow(10, 8)));
        daiTransaction.setAlreadyMortgageAmount(alreadyMortgageAmount.stripTrailingZeros().toPlainString());
        daiTransaction.setAlreadyLoanAmount(alreadyLoanAmount.stripTrailingZeros().toPlainString());
        daiTransaction.setLiquidationFineAmount(liquidationFineAmount.stripTrailingZeros().toPlainString());

    }

    @Override
    public List<BlTransaction> getTranListByOldContractAddressAndOldToAddr(DaiTranRequest daiTranRequest) {
        List<BlTransaction> blTransactionList = blTransactionMapper.getTranListByOldContractAddressAndOldToAddr(daiTranRequest);
        if (!CollectionUtils.isEmpty(blTransactionList)) {
            blTransactionList.forEach(blTransaction -> {
                String eventArg = blTransaction.getEventArg();
                BigDecimal fee = BigDecimal.ZERO;
                BigDecimal amount = BigDecimal.ZERO;
                if (!StringUtils.isEmpty(eventArg) && eventArg.startsWith("{") && eventArg.endsWith("}")) {
                    JSONObject eventArgsJson = JSON.parseObject(eventArg);
                    if (null != eventArgsJson) {
                        if (eventArgsJson.containsKey("amount")) {
                            amount = eventArgsJson.getBigDecimal("amount");
                        }
                        if (eventArgsJson.containsKey("fee")) {
                            fee = eventArgsJson.getBigDecimal("fee");
                        }
                        if (eventArgsJson.containsKey("to")) {
                            blTransaction.setToAccount(eventArgsJson.getString("to"));
                        }
                    }
                }
                BlAsset blAsset = blAssetMapper.selectBySymbol("XWC");
                int xwcPrecision;
                if (null != blAsset) {
                    Long xwcPrecisionLong = blAsset.getPrecision();
                    if (null == xwcPrecisionLong) {
                        xwcPrecision = 8;
                    } else {
                        int xwcPrecisionLen = String.valueOf(xwcPrecisionLong).length();
                        if (xwcPrecisionLen > 1) {
                            xwcPrecision = xwcPrecisionLen - 1;
                        } else {
                            xwcPrecision = 8;
                        }
                    }
                } else {
                    xwcPrecision = 8;
                }
                blTransaction.setAmount(amount);
                if (null == amount) {
                    blTransaction.setAmountStr("0");
                } else {
                    String amountStr = amount.divide(BigDecimal.valueOf(Math.pow(10, xwcPrecision))).stripTrailingZeros().toPlainString();
                    blTransaction.setAmountStr(amountStr);
                }
                blTransaction.setFee(fee);
                if (null == fee) {
                    blTransaction.setFeeStr("0");
                } else {
                    String feeStr = fee.divide(BigDecimal.valueOf(Math.pow(10, xwcPrecision))).stripTrailingZeros().toPlainString();
                    blTransaction.setFeeStr(feeStr);
                }
            });
        }
        return blTransactionList;
    }

    @Override
    public List<String> getDaiEventCdcIdList(String eventName, String contractAddress) {
        List<String> eventArgList = blTxEventsMapper.getEventArgListByEventName(eventName, contractAddress);
        List<String> cdcIdList = new ArrayList<>(eventArgList.size());
        if (!CollectionUtils.isEmpty(eventArgList)) {
            JSONObject eventArgJson;
            String cdcId;
            for (String s : eventArgList) {
                if (s.startsWith("{") && s.endsWith("}")) {
                    eventArgJson = JSONObject.parseObject(s);
                    if (null != eventArgJson) {
                        if (eventArgJson.containsKey("cdcId")) {
                            cdcId = eventArgJson.getString("cdcId");
                            cdcIdList.add(cdcId);
                        }
                    }
                }

            }

        }
        if (!CollectionUtils.isEmpty(cdcIdList)) {
            cdcIdList = cdcIdList.stream().distinct().collect(Collectors.toList());
        }
        return cdcIdList;
    }

    @Override
    public EUDataGridResult selectListByUserAddress(BlTokenTransaction transaction) {
        // 分页处理
        Integer page = transaction.getPage();
        if (page <= 0) {
            page = 1;
        }
        Integer pageSize = transaction.getRows();
        if (null == pageSize) {
            pageSize = 10;
        }
//        PageHelper.startPage(page, transaction.getRows());
        List<BlTokenTransaction> list = blTokenTransactionMapper.selectListByUserAddress(transaction);
        int size = 0;
        if (!CollectionUtils.isEmpty(list)) {
            Date txTime;
            String intervalTime;
            for (BlTokenTransaction trx : list) {
                handleAmountData(trx);
                trx.setTimeZone(TIME_ZONE);
                txTime = trx.getTrxTime();
                if (null != txTime) {
                    intervalTime = TimeTool.getIntervalTimeStr(txTime, new Date());
                    trx.setIntervalTime(intervalTime);
                    txTime = new Date(txTime.getTime() - TIME_INTERVAL * 60 * 60 * 1000L);
                    trx.setTrxTime(txTime);
                }
            }
            size = list.size();
            list = list.stream()
                    .sorted(Comparator.comparing(BlTokenTransaction::getId)
                            .reversed()).skip(pageSize * (page - 1)).limit(pageSize)
                    .collect(Collectors.toList());
        }
        // 创建一个返回值对象
        EUDataGridResult result = new EUDataGridResult();
        result.setRows(list);
        // 取记录总条数
//        PageInfo<BlTokenTransaction> pageInfo = new PageInfo<>(list);
        result.setTotal(size);
        long pages = size / pageSize;
        result.setPages(pages);
        return result;
    }

    @Override
    public EUDataGridResult selectTrxList(BlTokenTransaction transaction) {
        // 分页处理
        PageHelper.startPage(transaction.getPage(), transaction.getRows());
        List<BlTokenTransaction> list = blTokenTransactionMapper.selectTrxList(transaction);
        if (!CollectionUtils.isEmpty(list)) {
            Date txTime;
            for (BlTokenTransaction trx : list) {
                trx.setTimeZone(TIME_ZONE);
                txTime = trx.getTrxTime();
                // 时间统一使用UTC，减去8小时
                if (null != txTime) {
                    txTime = new Date(txTime.getTime() - TIME_INTERVAL * 60 * 60 * 1000L);
                    trx.setTrxTime(txTime);
                }
                handleAmountData(trx);
            }
        }
        // 创建一个返回值对象
        EUDataGridResult result = new EUDataGridResult();
        result.setRows(list);
        // 取记录总条数
        PageInfo<BlTokenTransaction> pageInfo = new PageInfo<>(list);
        result.setTotal(pageInfo.getTotal());
        result.setPages(pageInfo.getPages());
        return result;
    }

    @Override
    public List<BlTokenTransaction> selectAllByTrxId(String trxId) {
        BlTokenTransaction cond = new BlTokenTransaction();
        cond.setTrxId(trxId);
        List<BlTokenTransaction> list = blTokenTransactionMapper.selectAllByCond(cond);
        if (list != null && list.size() > 0) {
            for (BlTokenTransaction trx : list) {
                handleAmountData(trx);
            }
        }
        return list;
    }
}
