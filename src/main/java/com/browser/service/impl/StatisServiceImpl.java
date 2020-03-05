package com.browser.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.browser.config.RealData;
import com.browser.dao.entity.BlAsset;
import com.browser.dao.entity.BlBlock;
import com.browser.dao.entity.BlContractBalance;
import com.browser.dao.entity.BlContractDetail;
import com.browser.dao.entity.BlContractInfo;
import com.browser.dao.entity.BlMinerStatis;
import com.browser.dao.entity.BlStatis;
import com.browser.dao.entity.BlTransaction;
import com.browser.dao.entity.BlTrxStatis;
import com.browser.dao.mapper.BlBlockMapper;
import com.browser.dao.mapper.BlContractBalanceMapper;
import com.browser.dao.mapper.BlContractInfoMapper;
import com.browser.dao.mapper.BlTransactionMapper;
import com.browser.dao.mapper.BlTrxStatisMapper;
import com.browser.service.StatisService;
import com.browser.tools.Constant;
import com.browser.tools.common.DateUtil;
import com.browser.tools.common.StringUtil;

@Service
public class StatisServiceImpl implements StatisService {

    @Autowired
    private BlTransactionMapper blTransactionMapper;

    @Autowired
    private BlBlockMapper blBlockMapper;

    @Autowired
    private BlContractInfoMapper blContractInfoMapper;

    @Autowired
    private BlContractBalanceMapper blContractBalanceMapper;
    @Autowired
    private BlTrxStatisMapper trxStatisMapper;

    @Autowired
    private RealData realData;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RequestWalletService requestWalletService;

    @Override
    public List<BlBlock> newBlockStatic() {
        List<BlBlock> blockList = blBlockMapper.selectNewBlockInfo();
        if (null != blockList && blockList.size() > 0) {
            for (BlBlock block : blockList) {
                if (null != block.getReward()) {
                    block.setRewards(block.getReward().divide(new BigDecimal(Constant.PRECISION)).stripTrailingZeros()
                            .toPlainString() + " " + Constant.SYMBOL);
                }
            }
        }
        return blockList;
    }

    @Override
    public List<BlTransaction> newTransactionStatic() {
        List<BlTransaction> txList = blTransactionMapper.selectNewTrxInfo();
        if (null != txList && txList.size() > 0) {
            for (BlTransaction trx : txList) {
                handleAmountData(trx);
            }
        }
        return txList;
    }

    @Override
    public BlStatis newBlockLinkStatic() {
        BlStatis blStatis = new BlStatis();

        BigDecimal totalReward = redisService.getTotalReward();
        if(totalReward ==null){
            totalReward = blBlockMapper.queryBlockRewards();
        }
        if (null != totalReward) {
            String rewards = totalReward.divide(new BigDecimal(Constant.PRECISION)).stripTrailingZeros().toPlainString()
                    + " " + Constant.SYMBOL;
            blStatis.setReward(rewards);
        }

        Integer trxNums = redisService.getTotalTrx();
        if(trxNums ==null){
            trxNums = blTransactionMapper.countTrxNum();
        }

        Long blockNum = blBlockMapper.queryBlockNum();

        BigDecimal totalSupply = redisService.getCurIssueNum();
        if(totalSupply==null){
            totalSupply = requestWalletService.getAssetImp(Constant.SYMBOL);
        }

        blStatis.setHeight(blockNum);
        blStatis.setTotalTxNum(trxNums);
        blStatis.setTotalAmount(totalSupply);

        String crossAssets = redisService.getCrossAssetNum();
        if(crossAssets==null){
            //获取跨链资产
            List<BlAsset> aList = redisService.getCrossAsset();
            StringBuffer sb =new StringBuffer();
            if(aList!=null && aList.size()>0){
                for (BlAsset blAsset: aList) {
                    if(!Constant.SYMBOL.equals(blAsset.getSymbol())){
                        BigDecimal crossSupply = requestWalletService.getAssetImp(blAsset.getSymbol());
                        if(crossSupply!=null){
                            if("ERCPAX".equals(blAsset.getSymbol())){
                                blAsset.setSymbol("PAX");
                            }
                            if("ERCELF".equals(blAsset.getSymbol())){
                                blAsset.setSymbol("ELF");
                            }

                            if(!StringUtils.contains(sb.toString(),blAsset.getSymbol())){
                                sb.append(crossSupply+" "+blAsset.getSymbol()+", ");
                            }
                        }
                    }
                }
                String crossAssetStr = sb.toString();
                if(crossAssetStr.length()>=crossAssetStr.length()-2 && crossAssetStr.length()>2) {
                    redisService.putCrossAssetNum(crossAssetStr.substring(0,crossAssetStr.length()-2));
                    blStatis.setCrossAsset(crossAssetStr.substring(0,crossAssetStr.length()-2));
                }
            }
        }else {
            blStatis.setCrossAsset(crossAssets);
        }

        realData.setBlStatis(blStatis);

        return blStatis;
    }




    @Override
    public JSONArray newTrxNumByDateStatic(BlTransaction record) {
        JSONArray array = new JSONArray();
        String maxDay = blTransactionMapper.selectCurDay();
        List<String> date = DateUtil.eachDayStatis(record.getStartTime(), record.getEndTime());
        for (String day : date) {
            BlTrxStatis trxStatis = trxStatisMapper.selectByTime(day);
            if (trxStatis != null) {
                updateTrxStatis(day, array, trxStatis.getTrxNum(),maxDay);

            } else {
                insertTrxStatis(day, array);
            }
        }
        return array;
    }

    private void insertTrxStatis(String day, JSONArray array) {
        BlTransaction transaction = new BlTransaction();
        transaction.setStartTime(DateUtil.getStartTime(day));
        transaction.setEndTime(DateUtil.getEndTime(day));
        BlTransaction tx = blTransactionMapper.countTrxNumByDate(transaction);

        BlTransaction preTransaction = new BlTransaction();
        String preDay = DateUtil.getPreTime(day);
        preTransaction.setStartTime(DateUtil.getStartTime(preDay));
        preTransaction.setEndTime(DateUtil.getEndTime(preDay));
        BlTransaction preTx = blTransactionMapper.countTrxNumByDate(preTransaction);

        if (tx != null) {
            BlTrxStatis trxStatis = new BlTrxStatis();
            trxStatis.setStatisTime(tx.getQueryTime());
            trxStatis.setTrxNum(Integer.valueOf(tx.getTrxNum()));
            trxStatisMapper.insert(trxStatis);

            BlTrxStatis preTrxStatis = new BlTrxStatis();
            preTrxStatis.setStatisTime(preTx.getQueryTime());
            preTrxStatis.setTrxNum(Integer.valueOf(preTx.getTrxNum()));
            trxStatisMapper.updateByTime(preTrxStatis);

            JSONArray data = new JSONArray();
            data.add(tx.getQueryTime());
            data.add(Integer.valueOf(tx.getTrxNum()));
            array.add(data);
        } else {
            BlTrxStatis trxStatis = new BlTrxStatis();
            trxStatis.setStatisTime(day);
            trxStatis.setTrxNum(0);
            trxStatisMapper.insert(trxStatis);
            JSONArray data = new JSONArray();
            data.add(day);
            data.add(0);
            array.add(data);
        }
    }

    private void updateTrxStatis(String day, JSONArray array, Integer trxNum,String maxDay) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if(maxDay!=null){
                if(maxDay.equals(sdf.format(new Date()))){
                    if(maxDay.equals(day)){
                        BlTransaction transaction = new BlTransaction();
                        transaction.setStartTime(DateUtil.getStartTime(day));
                        transaction.setEndTime(DateUtil.getEndTime(day));
                        BlTransaction tx = blTransactionMapper.countTrxNumByDate(transaction);
                        if (tx != null) {
                            BlTrxStatis trxStatis = new BlTrxStatis();
                            trxStatis.setStatisTime(tx.getQueryTime());
                            trxStatis.setTrxNum(Integer.valueOf(tx.getTrxNum()));
                            trxStatisMapper.updateByTime(trxStatis);
                            JSONArray data = new JSONArray();
                            data.add(tx.getQueryTime());
                            data.add(Integer.valueOf(tx.getTrxNum()));
                            array.add(data);
                        } else {
                            JSONArray data = new JSONArray();
                            data.add(day);
                            data.add(trxNum);
                            array.add(data);
                        }
                    }else{
                        JSONArray data = new JSONArray();
                        data.add(day);
                        data.add(trxNum);
                        array.add(data);
                    }
                }else{
                    BlTransaction transaction = new BlTransaction();
                    transaction.setStartTime(DateUtil.getStartTime(day));
                    transaction.setEndTime(DateUtil.getEndTime(day));
                    BlTransaction tx = blTransactionMapper.countTrxNumByDate(transaction);
                    if (tx != null) {
                        BlTrxStatis trxStatis = new BlTrxStatis();
                        trxStatis.setStatisTime(tx.getQueryTime());
                        trxStatis.setTrxNum(Integer.valueOf(tx.getTrxNum()));
                        trxStatisMapper.updateByTime(trxStatis);
                        JSONArray data = new JSONArray();
                        data.add(tx.getQueryTime());
                        data.add(Integer.valueOf(tx.getTrxNum()));
                        array.add(data);
                    } else {
                        JSONArray data = new JSONArray();
                        data.add(day);
                        data.add(trxNum);
                        array.add(data);
                    }
                }
            }

    }

    @Override
    public JSONArray newTrxNumByHourStatic(BlTransaction record) {

        List<BlTransaction> txList = blTransactionMapper.countTrxNumByHour(record);
        JSONArray array = new JSONArray();
        Map<String, Integer> map = new HashMap<String, Integer>();
        List<String> date = DateUtil.eachHourStatis(record.getEndTime());
        if (date != null && date.size() > 0) {
            if (txList != null && txList.size() > 0) {
                for (BlTransaction trx : txList) {
                    map.put(trx.getQueryTime(), Integer.valueOf(trx.getTrxNum()));
                }
            }
        }
        for (String str : date) {
            JSONArray data = new JSONArray();
            if (map.containsKey(str)) {
                data.add(str);
                data.add(map.get(str));
            } else {
                data.add(str);
                data.add(0);
            }
            array.add(data);
        }
        return array;
    }

    @Override
    public BlMinerStatis getMinerStatis(BlMinerStatis blMinerStatis) {
        BlMinerStatis minerStatis = new BlMinerStatis();
        BlBlock block = blBlockMapper.statisByMinerName(blMinerStatis.getName());
        if (null != block) {
            minerStatis.setName(block.getMinerName());
            minerStatis.setAddress(block.getMinerAddress());
            Integer addrTrxNum = blTransactionMapper.countAddrTrxNum(block.getMinerAddress());
            minerStatis.setTransaction(addrTrxNum == null ? 0 : addrTrxNum);
//            String rewards = block.getCountRewards().divide(new BigDecimal(Constant.PRECISION)).stripTrailingZeros()
//                    .toPlainString() + " " + Constant.SYMBOL;
//            minerStatis.setRewards(rewards);

            BigDecimal reward = blTransactionMapper.selectRewards(block.getMinerAddress());
            if(reward!=null){
                minerStatis.setRewards(reward.divide(new BigDecimal(Constant.PRECISION),5,BigDecimal.ROUND_HALF_UP).
                        stripTrailingZeros().toPlainString()+" "+Constant.SYMBOL);
            }else {
                minerStatis.setRewards("0 "+Constant.SYMBOL);
            }

            Integer contracts = blContractInfoMapper.countContracts(block.getMinerAddress());
            minerStatis.setContracts(contracts == null ? 0 : contracts);

            List<String> balanceList = new ArrayList<>();
            String balanceStr = requestWalletService.getBalances(block.getMinerAddress());
            if (balanceStr != null) {
                JSONArray balances = JSONObject.parseArray(balanceStr);
                for (int i = 0; i < balances.size(); i++) {
                    JSONObject jsonObject = balances.getJSONObject(i);
                    BlAsset blAsset = realData.getSymbolByAssetId(jsonObject.getString("asset_id"));
                    String balance = jsonObject.getBigDecimal("amount").
                            divide(new BigDecimal(blAsset.getPrecision()), 8, BigDecimal.ROUND_HALF_UP).
                            stripTrailingZeros().toPlainString();
                    balanceList.add(balance + " " + blAsset.getSymbol());

                }
            }

            List<String> lockList = new ArrayList<>();
            Map<String, BigDecimal> map = new HashMap<>();
            String lockBalanceStr = requestWalletService.getLockBalance(block.getMinerName());
            if (lockBalanceStr != null) {
                JSONArray balances = JSONObject.parseArray(lockBalanceStr);
                for (int i = 0; i < balances.size(); i++) {
                    JSONObject jsonObject = balances.getJSONObject(i);
                    String assetId = jsonObject.getString("lock_asset_id");

                    BlAsset blAsset = realData.getSymbolByAssetId(assetId);
                    BigDecimal balance = jsonObject.getBigDecimal("lock_asset_amount").
                            divide(new BigDecimal(blAsset.getPrecision()), 8, BigDecimal.ROUND_HALF_UP);

                    if (!map.containsKey(blAsset.getSymbol())) {
                        map.put(blAsset.getSymbol(), balance);
                    } else {
                        map.put(blAsset.getSymbol(), map.get(blAsset.getSymbol()).add(balance));
                    }

                }

                for (Map.Entry<String, BigDecimal> entry : map.entrySet()) {
                    lockList.add(entry.getValue().stripTrailingZeros().toPlainString() + " " + entry.getKey());
                }

            }

            List<String> paybackList = new ArrayList<>();
            Map<String, BigDecimal> paybackMap = new HashMap<>();
            String paybackStr = requestWalletService.getPaybackBalance(block.getMinerAddress());
            if (paybackStr != null) {
                JSONArray balances = JSONObject.parseArray(paybackStr);
                for (int i = 0; i < balances.size(); i++) {
                    JSONObject jsonObject = balances.getJSONArray(i).getJSONObject(1);
                    String assetId = jsonObject.getString("asset_id");

                    BlAsset blAsset = realData.getSymbolByAssetId(assetId);
                    BigDecimal balance = jsonObject.getBigDecimal("amount").
                            divide(new BigDecimal(blAsset.getPrecision()), 8, BigDecimal.ROUND_HALF_UP);

                    if (!paybackMap.containsKey(blAsset.getSymbol())) {
                        paybackMap.put(blAsset.getSymbol(), balance);
                    } else {
                        paybackMap.put(blAsset.getSymbol(), paybackMap.get(blAsset.getSymbol()).add(balance));
                    }

                }

                for (Map.Entry<String, BigDecimal> entry : paybackMap.entrySet()) {
                    paybackList.add(entry.getValue().stripTrailingZeros().toPlainString() + " " + entry.getKey());
                }

            }

            minerStatis.setBalances(balanceList);
            minerStatis.setLockBalances(lockList);
            minerStatis.setPaybackBalances(paybackList);


        }
        return minerStatis;
    }

    @Override
    public BlContractDetail getContractsStatis(BlContractInfo contractInfo) {
        BlContractDetail contractDetail = new BlContractDetail();
        BlContractInfo blContractInfo = blContractInfoMapper.selectByPrimaryKey(contractInfo.getContractId());
        if (blContractInfo == null) {
            return null;
        }
        contractDetail.setContractAddress(blContractInfo.getContractId());
        contractDetail.setOnwerAddress(blContractInfo.getOwnerAddress());

        Integer trxNum = blTransactionMapper.countContractTrxNum(contractInfo.getContractId());
        String createTxId = blTransactionMapper.getContractCreateTxId(contractInfo.getContractId());

        contractDetail.setCreateTxId(createTxId);
        contractDetail.setTransactions(trxNum);

        List<String> balanceStr = new ArrayList<String>();
        List<BlContractBalance> balanceList = blContractBalanceMapper.selectByContractId(contractInfo.getContractId());
        if (balanceList != null && balanceList.size() > 0) {
            for (BlContractBalance balance : balanceList) {
                BlAsset blAsset = realData.getSymbolByAssetId(balance.getAssetId());
                if (null != balance.getBalance() && balance.getBalance().compareTo(new BigDecimal(0)) > 0) {
                    String amountStr = balance.getBalance().divide(new BigDecimal(blAsset.getPrecision()))
                            .stripTrailingZeros().toPlainString() + " " + blAsset.getSymbol();
                    balanceStr.add(amountStr);
                }
            }
            contractDetail.setBalance(balanceStr);
        }
        return contractDetail;
    }

    @Override
    public BlMinerStatis getAddrStatis(BlMinerStatis blMinerStatis) {
        BlMinerStatis addrStatis = new BlMinerStatis();
        String address = redisService.getAccountAddr(blMinerStatis.getAddress());
        if (!StringUtil.isEmpty(address)) {
            addrStatis.setName(blMinerStatis.getAddress());
            addrStatis.setAddress(address);
        }else {
            addrStatis.setAddress(blMinerStatis.getAddress());
        }

        Integer addrTrxNum = blTransactionMapper.countAddrTrxNum(addrStatis.getAddress());
        addrStatis.setTransaction(addrTrxNum == null ? 0 : addrTrxNum);
        BlBlock block = blBlockMapper.getBlockByAddr(addrStatis.getAddress());
        if (null != block) {
            addrStatis.setName(block.getMinerName());
        }

        BigDecimal reward = blTransactionMapper.selectRewards(addrStatis.getAddress());
        if(reward!=null){
            addrStatis.setRewards(reward.divide(new BigDecimal(Constant.PRECISION),5,BigDecimal.ROUND_HALF_UP).
                    stripTrailingZeros().toPlainString()+" "+Constant.SYMBOL);
        }else {
            addrStatis.setRewards("0 "+Constant.SYMBOL);
        }

        Integer contracts = blContractInfoMapper.countContracts(addrStatis.getAddress());
        addrStatis.setContracts(contracts == null ? 0 : contracts);

        List<String> balanceList = new ArrayList<>();
        String balanceStr = requestWalletService.getBalances(addrStatis.getAddress());
        if (balanceStr != null) {
            JSONArray balances = JSONObject.parseArray(balanceStr);
            for (int i = 0; i < balances.size(); i++) {
                JSONObject jsonObject = balances.getJSONObject(i);
                BlAsset blAsset = realData.getSymbolByAssetId(jsonObject.getString("asset_id"));
                String balance = jsonObject.getBigDecimal("amount").
                        divide(new BigDecimal(blAsset.getPrecision()), 8, BigDecimal.ROUND_HALF_UP).
                        stripTrailingZeros().toPlainString();
                balanceList.add(balance + " " + blAsset.getSymbol());

            }
        }


        String accountName = redisService.getAccountName(addrStatis.getAddress());

        if(accountName == null || accountName.isEmpty()) {
            try {
                String foundAccountName = requestWalletService.getAccountNameByAddr(addrStatis.getAddress());
                if(foundAccountName!=null && !foundAccountName.isEmpty()) {
                    accountName = foundAccountName;
                    redisService.putAccountName(addrStatis.getAddress(), accountName);
                }
            } catch (Exception e) {

            }
        }

        if(addrStatis.getName()==null && accountName!=null && accountName.length()>0){
            addrStatis.setName(accountName);
        }

        List<String> lockList = new ArrayList<>();
        if(accountName!=null&&accountName.length()>0) {
            Map<String, BigDecimal> map = new HashMap<>();
            String lockBalanceStr = requestWalletService.getLockBalance(accountName);
            if (lockBalanceStr != null) {
                JSONArray balances = JSONObject.parseArray(lockBalanceStr);
                for (int i = 0; i < balances.size(); i++) {
                    JSONObject jsonObject = balances.getJSONObject(i);
                    String assetId = jsonObject.getString("lock_asset_id");

                    BlAsset blAsset = realData.getSymbolByAssetId(assetId);
                    BigDecimal balance = jsonObject.getBigDecimal("lock_asset_amount").
                            divide(new BigDecimal(blAsset.getPrecision()), 8, BigDecimal.ROUND_HALF_UP);

                    if (!map.containsKey(blAsset.getSymbol())) {
                        map.put(blAsset.getSymbol(), balance);
                    } else {
                        map.put(blAsset.getSymbol(), map.get(blAsset.getSymbol()).add(balance));
                    }

                }

                for (Map.Entry<String, BigDecimal> entry : map.entrySet()) {
                    lockList.add(entry.getValue().stripTrailingZeros().toPlainString() + " " + entry.getKey());
                }

            }
        }
        List<String> paybackList = new ArrayList<>();
        if(accountName!=null&&accountName.length()>0) {
            Map<String, BigDecimal> paybackMap = new HashMap<>();
            String paybackStr = requestWalletService.getPaybackBalance(addrStatis.getAddress());
            if (paybackStr != null) {
                JSONArray balances = JSONObject.parseArray(paybackStr);
                for (int i = 0; i < balances.size(); i++) {
                    JSONObject jsonObject = balances.getJSONArray(i).getJSONObject(1);
                    String assetId = jsonObject.getString("asset_id");

                    BlAsset blAsset = realData.getSymbolByAssetId(assetId);
                    BigDecimal balance = jsonObject.getBigDecimal("amount").
                            divide(new BigDecimal(blAsset.getPrecision()), 8, BigDecimal.ROUND_HALF_UP);

                    if (!paybackMap.containsKey(blAsset.getSymbol())) {
                        paybackMap.put(blAsset.getSymbol(), balance);
                    } else {
                        paybackMap.put(blAsset.getSymbol(), paybackMap.get(blAsset.getSymbol()).add(balance));
                    }

                }

                for (Map.Entry<String, BigDecimal> entry : paybackMap.entrySet()) {
                    paybackList.add(entry.getValue().stripTrailingZeros().toPlainString() + " " + entry.getKey());
                }

            }
        }

        addrStatis.setBalances(balanceList);
        addrStatis.setLockBalances(lockList);
        addrStatis.setPaybackBalances(paybackList);
        if(addrStatis.getName()==null && addrTrxNum == 0){
            return null;
        }

        return addrStatis;
    }

    @Override
    public BlMinerStatis addrjudge(BlMinerStatis blMinerStatis) {
        BlMinerStatis addrStatis = new BlMinerStatis();
        String address = redisService.getAccountAddr(blMinerStatis.getAddress());
        if (!StringUtil.isEmpty(address)) {
            addrStatis.setName(blMinerStatis.getAddress());
            addrStatis.setAddress(address);
            return addrStatis;
        }else {
            addrStatis.setAddress(blMinerStatis.getAddress());
        }
        BlTransaction addr = blTransactionMapper.judgeAddr(addrStatis.getAddress());
        if(addr==null){
            return null;
        }

        return addrStatis;
    }

    private void handleAmountData(BlTransaction trx) {

        if (!StringUtil.isEmpty(trx.getAssetId())) {
            BlAsset blAsset = realData.getSymbolByAssetId(trx.getAssetId());

            if(trx.getOpType() == Constant.ASSET_MORGAGE || trx.getOpType() == Constant.ASSET_REDEEM){
                trx.setAmountStr(trx.getAmount().divide(new BigDecimal(blAsset.getPrecision())).stripTrailingZeros()
                        .toPlainString()  +" " + blAsset.getSymbol());
            }else{
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

    @Override
    public void indexEcharts(){
        BlTransaction record =new BlTransaction();
        SimpleDateFormat result = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        record.setStartTime(DateUtil.getStartTime(result.format(new Date())));
        record.setEndTime(result.format(new Date()));
        JSONArray hour =this.newTrxNumByHourStatic(record);
        realData.setToday(hour);


        record.setStartTime(DateUtil.getPreWeekDay());
        record.setEndTime(DateUtil.getToDay());
        JSONArray week = this.newTrxNumByDateStatic(record);
        realData.setWeekDay(week);

        record.setStartTime(DateUtil.getPreMonthDay());
        record.setEndTime(DateUtil.getToDay());
        JSONArray month = this.newTrxNumByDateStatic(record);
        realData.setMonthDay(month);
    }


    @Async("myAsync")
    @Override
    public void statisReward(BlBlock block){
        BigDecimal totalReward = redisService.getTotalReward();
        if(totalReward!=null){
            totalReward =totalReward.add(block.getReward());
            redisService.putTotalReward(totalReward);
        }else{
            BigDecimal reward = blBlockMapper.queryBlockRewards();
            redisService.putTotalReward(reward);
        }

    }

    @Async("myAsync")
    @Override
    public void statisTrxNum(Integer trxNum){

        Integer trxNums = redisService.getTotalTrx();
        if(trxNums!=null){
            trxNums =trxNums+trxNum;
            redisService.putTotalTrx(trxNums);
        }else{
            Integer statisNum = blTransactionMapper.countTrxNum();
            redisService.putTotalTrx(statisNum);
        }
    }

}
