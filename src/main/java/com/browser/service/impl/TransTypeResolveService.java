package com.browser.service.impl;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;

import com.browser.dao.mapper.BlAssetMapper;
import com.browser.tools.DecodeMemo;
import com.browser.tools.common.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.browser.config.ApplicationContextRegister;
import com.browser.config.RealData;
import com.browser.dao.entity.BlAsset;
import com.browser.dao.entity.BlContractInfo;
import com.browser.dao.entity.BlGuarantee;
import com.browser.dao.entity.BlTransaction;
import com.browser.dao.mapper.BlBlockMapper;
import com.browser.dao.mapper.BlContractInfoMapper;
import com.browser.dao.mapper.BlGuaranteeMapper;
import com.browser.tools.Constant;
import com.browser.tools.common.StringUtil;

@Service
public class TransTypeResolveService {

    private static Logger logger = LoggerFactory.getLogger(TransTypeResolveService.class);

    @Autowired
    private RealData realData;

    @Autowired
    private RequestWalletService requestWalletService;

    @Autowired
    private BlBlockMapper blockMapper;

    @Autowired
    private BlContractInfoMapper blContractInfoMapper;

    @Autowired
    private BlGuaranteeMapper guaranteeMapper;

    @Autowired
    private BlAssetMapper assetMapper;

    @Autowired
    private RedisService redisService;

    public JSONObject transTypeResolve(Integer opType, JSONObject json, BlTransaction transaction) {
        JSONObject jsb = null;
        try {
            ApplicationContext context = ApplicationContextRegister.getApplicationContext();
            Object bean = context.getBean("transTypeResolveService");
            Method method = bean.getClass().getMethod("transOpType" + opType,
                    new Class<?>[]{JSONObject.class, BlTransaction.class});

            jsb = (JSONObject) method.invoke(bean, new Object[]{json, transaction});
        } catch (Exception e) {
            logger.error("交易类型解析失败,op类型："+opType, e);
        }
        return jsb;
    }

    /**
     * 合约充值
     *
     * @param json
     * @return
     */
    public JSONObject transOpType81(JSONObject json, BlTransaction transaction) {
        JSONObject result = new JSONObject();
        result.put("contractAddress", transaction.getContractId());
        result.put("callerAddress", transaction.getFromAccount());
        result.put("value", transaction.getAmountStr());
        result.put("gasUsed", transaction.getGasCost());
        result.put("gasPrice", feeStr(transaction.getGasPrice()));
        result.put("fee", transaction.getFeeStr());
        if (null != transaction.getGuaranteeId()) {
            BlGuarantee guarantee = getGuarantee(transaction.getGuaranteeId());
            if (null != guarantee) {
                result.put("guarantee", guarantee);
            }
        }
        return result;
    }

    /**
     * 合约调用
     *
     * @param json
     * @return
     */
    public JSONObject transOpType79(JSONObject json, BlTransaction transaction) {
        JSONObject result = new JSONObject();
        result.put("contractAddress", transaction.getContractId());
        result.put("callerAddress", transaction.getFromAccount());
        result.put("gasUsed", transaction.getGasCost());
        result.put("gasPrice", feeStr(transaction.getGasPrice()));
        result.put("fee", transaction.getFeeStr());
        result.put("function", transaction.getCalledAbi());
        result.put("params", transaction.getAbiParams());
        if (null != transaction.getGuaranteeId()) {
            BlGuarantee guarantee = getGuarantee(transaction.getGuaranteeId());
            if (null != guarantee) {
                result.put("guarantee", guarantee);
            }
        }
        return result;
    }

    /**
     * 合约注册
     *
     * @param json
     * @return
     */
    public JSONObject transOpType76(JSONObject json, BlTransaction transaction) {
        JSONObject result = new JSONObject();
        BlContractInfo contractInfo = blContractInfoMapper.selectByPrimaryKey(transaction.getContractId());
        result.put("contractAddress", transaction.getContractId());
        if (null != contractInfo) {
            result.put("authorAddress", contractInfo.getOwnerAddress());
        }
        result.put("inheritFrom", transaction.getExtraTrxId());
        result.put("gasUsed", transaction.getGasCost());
        result.put("gasPrice", feeStr(transaction.getGasPrice()));
        result.put("fee", transaction.getFeeStr());
        if (null != transaction.getGuaranteeId()) {
            BlGuarantee guarantee = getGuarantee(transaction.getGuaranteeId());
            if (null != guarantee) {
                result.put("guarantee", guarantee);
            }
        }
        return result;
    }

    /**
     * 合约升级
     *
     * @param json
     * @return
     */
    public JSONObject transOpType77(JSONObject json, BlTransaction transaction) {
        JSONObject result = new JSONObject();
        BlContractInfo contractInfo = blContractInfoMapper.selectByPrimaryKey(transaction.getContractId());
        result.put("contractAddress", transaction.getContractId());
        result.put("callerAddress", transaction.getFromAccount());
        result.put("contractAddress", transaction.getContractId());
        if (null != contractInfo) {
            result.put("contractName", contractInfo.getName());
            result.put("description", contractInfo.getDescription());
        }
        result.put("gasUsed", transaction.getGasCost());
        result.put("gasPrice", feeStr(transaction.getGasPrice()));
        result.put("fee", transaction.getFeeStr());
        if (null != transaction.getGuaranteeId()) {
            BlGuarantee guarantee = getGuarantee(transaction.getGuaranteeId());
            if (null != guarantee) {
                result.put("guarantee", guarantee);
            }
        }
        return result;
    }

    /**
     * 提现结果
     *
     * @param json
     * @return
     */
    public JSONObject transOpType65(JSONObject json, BlTransaction transaction) {
        JSONObject result = new JSONObject();
        String name = blockMapper.getMinerName(transaction.getMinerAddress());
        result.put("fromAddress", transaction.getFromAccount());
        result.put("toAddress", transaction.getToAccount());
        result.put("value", transaction.getAmountStr());
        result.put("minerName", name);
        result.put("minerAddr", transaction.getMinerAddress());
        result.put("fee", transaction.getFeeStr());
        if (null != transaction.getGuaranteeId()) {
            BlGuarantee guarantee = getGuarantee(transaction.getGuaranteeId());
            if (null != guarantee) {
                result.put("guarantee", guarantee);
            }
        }
        return result;
    }
    /**
     * 提现交易广播
     *
     * @param json
     * @return
     */
    public JSONObject transOpType64(JSONObject json, BlTransaction transaction) {
        JSONObject result = new JSONObject();
        result.put("crosschainTrxId", json.getString("crosschain_trx_id"));
        result.put("withdrawTrx", json.getString("withdraw_trx"));
        result.put("minerAddress", json.getString("miner_address"));
        result.put("assetSymbol", json.getString("asset_symbol"));

        JSONObject minerInfo = redisService.getMinerInfo(json.getString("miner_broadcast"));
        if(minerInfo!=null){
            result.put("minerBroadcast", minerInfo.getString("name"));
        }
        return result;
    }
    /**
     * 提现交易签名
     *
     * @param json
     * @return
     */
    public JSONObject transOpType63(JSONObject json, BlTransaction transaction) {
        JSONObject result = new JSONObject();
        String name = blockMapper.getMinerName(transaction.getMinerAddress());
        result.put("assetSymbol", json.getString("asset_symbol"));
        result.put("guardAddress", json.getString("guard_address"));

        return result;
    }
    /**
     * 矿工确认
     *
     * @param json
     * @return
     */
    public JSONObject transOpType62(JSONObject json, BlTransaction transaction) {
        JSONObject result = new JSONObject();
        String name = blockMapper.getMinerName(transaction.getMinerAddress());
        result.put("assetSymbol", json.getString("asset_symbol"));
        result.put("minerAddress", json.getString("miner_address"));

        JSONObject minerInfo = redisService.getMinerInfo(json.getString("miner_broadcast"));
        if(minerInfo!=null){
            result.put("minerBroadcast", minerInfo.getString("name"));
        }
        return result;
    }

    /**
     * 提现请求
     *
     * @param json
     * @return
     */
    public JSONObject transOpType61(JSONObject json, BlTransaction transaction) {
        JSONObject result = new JSONObject();
        result.put("fromAddress", transaction.getFromAccount());
        result.put("toAddress", transaction.getToAccount());
        result.put("value", transaction.getAmountStr());
        result.put("fee", transaction.getFeeStr());
        if (null != transaction.getGuaranteeId()) {
            BlGuarantee guarantee = getGuarantee(transaction.getGuaranteeId());
            if (null != guarantee) {
                result.put("guarantee", guarantee);
            }
        }
        return result;
    }

    /**
     * 充值
     *
     * @param json
     * @return
     */
    public JSONObject transOpType60(JSONObject json, BlTransaction transaction) {
        JSONObject result = new JSONObject();
        String name = blockMapper.getMinerName(transaction.getMinerAddress());
        result.put("fromAddress", transaction.getFromAccount());
        result.put("toAddress", transaction.getToAccount());
        result.put("value", transaction.getAmountStr());
        result.put("minerName", name);
        result.put("minerAddr", transaction.getMinerAddress());
        result.put("fee", transaction.getFeeStr());
        if (null != transaction.getGuaranteeId()) {
            BlGuarantee guarantee = getGuarantee(transaction.getGuaranteeId());
            if (null != guarantee) {
                result.put("guarantee", guarantee);
            }
        }
        return result;
    }

    /**
     * 转账
     *
     * @param json
     * @return
     */
    public JSONObject transOpType0(JSONObject json, BlTransaction transaction) {
        JSONObject result = new JSONObject();
        result.put("fromAddress", transaction.getFromAccount());
        result.put("toAddress", transaction.getToAccount());
        result.put("value", transaction.getAmountStr());
        result.put("fee", transaction.getFeeStr());
        String memo = new String(DecodeMemo.toByteArray(transaction.getMemo()));
        result.put("memo", memo.trim());
        if (null != transaction.getGuaranteeId()) {
            BlGuarantee guarantee = getGuarantee(transaction.getGuaranteeId());
            if (null != guarantee) {
                result.put("guarantee", guarantee);
            }
        }
        return result;
    }

    /**
     * 用户注册
     *
     * @param json
     * @return
     */
    public JSONObject transOpType5(JSONObject json, BlTransaction transaction) {
        JSONObject result = new JSONObject();
        result.put("name", transaction.getExtension());
        result.put("from", transaction.getFromAccount());
        result.put("fee", feeStr(transaction.getFee()));
        String guaranteeId = transaction.getGuaranteeId();
        if (null != guaranteeId && !"".equals(guaranteeId)) {
            BlGuarantee guarantee = getGuarantee(guaranteeId);
            if (null != guarantee) {
                result.put("guarantee", guarantee);
            }
        }
        return result;
    }

    /**
     * 绑定tunnel地址
     *
     * @param json
     * @return
     */
    public JSONObject transOpType10(JSONObject json, BlTransaction transaction) {
        JSONObject result = new JSONObject();
        result.put("tunnelAddress", json.getString("tunnel_address"));
        String asset = json.getString("crosschain_type");
        if("ERCPAX".equals(asset)){
            asset = "PAX";
        }
        if("ERCELF".equals(asset)){
            asset = "ELF";
        }
        result.put("asset", asset);
        result.put("addr", json.getString("addr"));
        result.put("fee", feeStr(json.getJSONObject("fee").getBigDecimal("amount")));
        String guaranteeId = json.getString("guarantee_id");
        if (null != guaranteeId && !"".equals(guaranteeId)) {
            BlGuarantee guarantee = getGuarantee(guaranteeId);
            if (null != guarantee) {
                result.put("guarantee", guarantee);
            }
        }
        return result;
    }

    //TODO

    /**
     * 解绑tunnel地址
     *
     * @param json
     * @return
     */
    public JSONObject transOpType11(JSONObject json, BlTransaction transaction) {
        JSONObject result = new JSONObject();
        result.put("tunnelAddress", json.getString("crosschain_type"));
        result.put("address", json.getString("addr"));
        result.put("tunnelAddress", json.getString("tunnel_address"));

        return result;
    }

    /**
     * 创建Miner
     *
     * @param json
     * @return
     */
    public JSONObject transOpType24(JSONObject json, BlTransaction transaction) {
        JSONObject result = new JSONObject();
        result.put("fee", feeStr(json.getJSONObject("fee").getBigDecimal("amount")));
        result.put("minerAddress", json.getString("miner_address"));
        String name = blockMapper.getMinerName(json.getString("miner_address"));
        result.put("minerName", name);
        String guaranteeId = json.getString("guarantee_id");
        if (null != guaranteeId && !"".equals(guaranteeId)) {
            BlGuarantee guarantee = getGuarantee(guaranteeId);
            if (null != guarantee) {
                result.put("guarantee", guarantee);
            }
        }
        return result;
    }

    //TODO

    /**
     * 创建多签地址
     *
     * @param json
     * @return
     */
    public JSONObject transOpType12(JSONObject json, BlTransaction transaction) {
        JSONObject result = new JSONObject();
        result.put("fee", feeStr(json.getJSONObject("fee").getBigDecimal("amount")));
        result.put("crosschainType", json.getString("crosschain_type"));
        result.put("addr", json.getString("addr"));

        String account_id = json.getString("account_id");
        String accountInfo = requestWalletService.getAccount(account_id);// 获取name
        if (accountInfo != null) {
            JSONObject jsonObject = JSONObject.parseObject(accountInfo);
            result.put("accountName", jsonObject.getString("name"));
        }

        return result;
    }

    /**
     * Miner生成多签地址
     *
     * @param json
     * @return
     */
    public JSONObject transOpType26(JSONObject json, BlTransaction transaction) {
        JSONObject result = new JSONObject();
        result.put("fee", feeStr(json.getJSONObject("fee").getBigDecimal("amount")));
        result.put("addressCold", json.getString("multi_address_cold"));
        result.put("addressHot", json.getString("multi_address_hot"));
        result.put("minerAddress", json.getString("miner_address"));
        result.put("asset", json.getString("chain_type"));
        String name = blockMapper.getMinerName(json.getString("miner_address"));
        result.put("minerName", name);
        String guaranteeId = json.getString("guarantee_id");
        if (null != guaranteeId && !"".equals(guaranteeId)) {
            BlGuarantee guarantee = getGuarantee(guaranteeId);
            if (null != guarantee) {
                result.put("guarantee", guarantee);
            }
        }
        return result;
    }

    /**
     * 领取工资
     *
     * @param json
     * @return
     */
    public JSONObject transOpType73(JSONObject json, BlTransaction transaction) {
        JSONObject result = new JSONObject();
        JSONArray balanceArr = new JSONArray();
        JSONArray array = json.getJSONArray("pay_back_balance");
        BigDecimal amount = BigDecimal.ZERO;
        if (array != null && array.size() > 0) {
            for (int i = 0; i < array.size(); i++) {
                JSONArray balanceArray = array.getJSONArray(i);
                amount = amount.add(balanceArray.getJSONObject(1).getBigDecimal("amount"));
            }
        }
        amount = amount.divide(new BigDecimal(Constant.PRECISION));
        balanceArr.add(amount.stripTrailingZeros().toPlainString() + " " + Constant.SYMBOL);

        result.put("balance", balanceArr);
        result.put("ownerAddr", json.getString("pay_back_owner"));
        result.put("fee", feeStr(json.getJSONObject("fee").getBigDecimal("amount")));
        String guaranteeId = json.getString("guarantee_id");
        if (null != guaranteeId && !"".equals(guaranteeId)) {
            BlGuarantee guarantee = getGuarantee(guaranteeId);
            if (null != guarantee) {
                result.put("guarantee", guarantee);
            }
        }
        return result;
    }


    /**
     * 创建手续费承兑单
     *
     * @param json
     * @return
     */
    public JSONObject transOpType82(JSONObject json, BlTransaction transaction) {
        JSONObject result = new JSONObject();
        result.put("ownerAddr", json.getString("owner_addr"));
        result.put("fee", feeStr(json.getJSONObject("fee").getBigDecimal("amount")));
        result.put("originSymbol", getSymbol(json.getJSONObject("asset_origin").getString("asset_id")));
        result.put("targetSymbol", getSymbol(json.getJSONObject("asset_target").getString("asset_id")));
        BigDecimal originAmount = json.getJSONObject("asset_origin").getBigDecimal("amount");
        BigDecimal targetAmount = json.getJSONObject("asset_target").getBigDecimal("amount");
        String targetId = json.getJSONObject("asset_target").getString("asset_id");
        String value = getBalance(targetId, targetAmount);
        result.put("value", value);
        BigDecimal rate = targetAmount.divide(originAmount).setScale(5, BigDecimal.ROUND_HALF_UP);
        result.put("rate", "1:" + rate.stripTrailingZeros().toPlainString());
        return result;
    }

    /**
     * 取消手续费承兑单
     *
     * @param json
     * @return
     */
    public JSONObject transOpType83(JSONObject json, BlTransaction transaction) {
        JSONObject result = new JSONObject();
        result.put("fee", feeStr(json.getJSONObject("fee").getBigDecimal("amount")));
        result.put("ownerAddr", json.getString("owner_addr"));
        result.put("guaranteeId", json.getString("cancel_guarantee_id"));
        return result;
    }

    /**
     * 质押资产
     *
     * @param json
     * @return
     */
    public JSONObject transOpType55(JSONObject json, BlTransaction transaction) {
        JSONObject result = new JSONObject();
        result.put("fromAddress", transaction.getFromAccount());
        result.put("value", getBalance(transaction.getAssetId(), transaction.getAmount()));
        result.put("fee", feeStr(transaction.getFee()));
        if (transaction.getToAccount() != null) {
            result.put("toAddress", transaction.getToAccount());
            result.put("citizen", transaction.getExtension());
        }

        return result;
    }

    /**
     * 赎回资产
     *
     * @param json
     * @return
     */
    public JSONObject transOpType56(JSONObject json, BlTransaction transaction) {
        JSONObject result = new JSONObject();
        result.put("toAddress", transaction.getToAccount());
        result.put("value", getBalance(transaction.getAssetId(), transaction.getAmount()));
        result.put("fee", feeStr(transaction.getFee()));
        if (transaction.getFromAccount() != null) {
            result.put("fromAddress", transaction.getFromAccount());
            result.put("citizen", transaction.getExtension());
        }

        return result;
    }

    //TODO

    /**
     * guard 撤销某些交易
     *
     * @param json
     * @return
     */
    public JSONObject transOpType84(JSONObject json, BlTransaction transaction) {
        JSONObject result = new JSONObject();
        result.put("ownerAddress", json.getString("owner_addr"));
        result.put("cancelGuaranteeId", json.getString("cancel_guarantee_id"));
        result.put("fee", feeStr(json.getJSONObject("fee").getBigDecimal("amount")));
        return result;
    }

    //TODO

    /**
     * 分红
     *
     * @param json
     * @return
     */
    public JSONObject transOpType88(JSONObject json, BlTransaction transaction) {
        JSONObject result = new JSONObject();
        result.put("bonusOwner", json.getString("bonus_owner"));

        JSONArray jsonResult = new JSONArray();
        JSONArray jsonArray = json.getJSONArray("bonus_balance");

        if (jsonArray != null && jsonArray.size() > 0) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONArray balanceArray = jsonArray.getJSONArray(i);
                String balance = getBalanceBySympbol(balanceArray.getString(0), balanceArray.getBigDecimal(1));
                jsonResult.add(balance);
            }
        }

        result.put("bonus_balance", jsonResult);
        result.put("fee", feeStr(json.getJSONObject("fee").getBigDecimal("amount")));
        return result;
    }

    //TODO

    /**
     * 调整citizen挖矿
     *
     * @param json
     * @return
     */
    public JSONObject transOpType6(JSONObject json, BlTransaction transaction) {
        JSONObject result = new JSONObject();
        result.put("fee", feeStr(json.getJSONObject("fee").getBigDecimal("amount")));
        result.put("addr", json.getString("addr"));

        String account_id = json.getString("account");
        String accountInfo = requestWalletService.getAccount(account_id);// 获取name
        if (accountInfo != null) {
            JSONObject jsonObject =JSONObject.parseObject(accountInfo);
            result.put("accountName", jsonObject.getString("name"));
        }

        return result;
    }

    //TODO

    /**
     * 指定喂价人
     *
     * @param json
     * @return
     */
    public JSONObject transOpType23(JSONObject json, BlTransaction transaction) {
        JSONObject result = new JSONObject();
        result.put("fee", feeStr(json.getJSONObject("fee").getBigDecimal("amount")));
        result.put("publisherAddress", json.getString("publisher_addr"));

        String account_id = json.getString("publisher");
        String accountInfo = requestWalletService.getAccount(account_id);// 获取name
        if (accountInfo != null) {
            JSONObject jsonObject = JSONObject.parseObject(accountInfo);
            result.put("publisher", jsonObject.getString("name"));
        }

        return result;
    }

    //TODO

    /**
     * 创建提案
     *
     * @param json
     * @return
     */
    public JSONObject transOpType28(JSONObject json, BlTransaction transaction) {
        JSONObject result = new JSONObject();
        result.put("fee", feeStr(json.getJSONObject("fee").getBigDecimal("amount")));
        result.put("feePayingAccount", json.getString("fee_paying_account"));

        Date date = DateUtil.parseDate(json.getString("expiration_time"), "yyyy-MM-dd'T'HH:mm:ss");
        result.put("expirationTime", new Date(date.getTime() + 8 * 60 * 60 * 1000L));

        String account_id = json.getString("proposer");
        String accountInfo = requestWalletService.getAccount(account_id);// 获取name
        if (accountInfo != null) {
            JSONObject jsonObject = JSONObject.parseObject(accountInfo);
            result.put("proposer", jsonObject.getString("name"));
        }

        return result;
    }

    //TODO

    /**
     * 对提案进行表决
     *
     * @param json
     * @return
     */
    public JSONObject transOpType29(JSONObject json, BlTransaction transaction) {
        JSONObject result = new JSONObject();
        result.put("fee", feeStr(json.getJSONObject("fee").getBigDecimal("amount")));
        result.put("feePayingAccount", json.getString("fee_paying_account"));
        result.put("proposal", json.getString("proposal"));

        return result;
    }

    //TODO
    /**
     * 创建HIOU资产
     *
     * @param json
     * @return
     */
    public JSONObject transOpType75 (JSONObject json, BlTransaction transaction) {
        JSONObject result = new JSONObject();
        result.put("fee", feeStr(json.getJSONObject("fee").getBigDecimal("amount")));
        result.put("symbol", json.getString("symbol"));
        result.put("issuerAddr", json.getString("issuer_addr"));

        String account_id = json.getString("issuer");
        String accountInfo = requestWalletService.getAccount(account_id);// 获取name
        if (accountInfo != null) {
            JSONObject jsonObject = JSONObject.parseObject(accountInfo);
            result.put("issuer", jsonObject.getString("name"));
        }

        return result;
    }

    //TODO
    /**
     * 创建eth 合约
     *
     * @param json
     * @return
     */
    public JSONObject transOpType95 (JSONObject json, BlTransaction transaction) {
        JSONObject result = new JSONObject();
        result.put("fee", feeStr(json.getJSONObject("fee").getBigDecimal("amount")));

        result.put("minerBroadcastAddrss", json.getString("miner_broadcast_addrss"));
        result.put("guardSignHotAddress", json.getString("guard_sign_hot_address"));
        result.put("guardSignColdAddress", json.getString("guard_sign_cold_address"));
        result.put("chainType", json.getString("chain_type"));

        JSONObject minerInfo = redisService.getMinerInfo(json.getString("miner_broadcast"));
        if(minerInfo!=null){
            result.put("minerBroadcast", minerInfo.getString("name"));
        }

        return result;
    }
    //TODO
    /**
     * eth多签合约创建
     *
     * @param json
     * @return
     */
    public JSONObject transOpType96 (JSONObject json, BlTransaction transaction) {
        JSONObject result = new JSONObject();
        result.put("fee", feeStr(json.getJSONObject("fee").getBigDecimal("amount")));
        result.put("miner_address", json.getString("miner_address"));
        result.put("chainType", json.getString("chain_type"));
        result.put("multiPubkeyType", json.getString("multi_pubkey_type"));

        JSONObject minerInfo = redisService.getMinerInfo(json.getString("miner_broadcast"));
        if(minerInfo!=null){
            result.put("minerBroadcast", minerInfo.getString("name"));
        }

        return result;
    }
    //TODO
    /**
     * eth 多签合约签名
     *
     * @param json
     * @return
     */
    public JSONObject transOpType97 (JSONObject json, BlTransaction transaction) {
        JSONObject result = new JSONObject();
        result.put("fee", feeStr(json.getJSONObject("fee").getBigDecimal("amount")));
        result.put("guardSignAddress", json.getString("guard_sign_address"));
        result.put("chainType", json.getString("chain_type"));

        return result;
    }

    //TODO
    /**
     * eth 多签合约确认签名
     *
     * @param json
     * @return
     */
    public JSONObject transOpType98 (JSONObject json, BlTransaction transaction) {
        JSONObject result = new JSONObject();
        result.put("fee", feeStr(json.getJSONObject("fee").getBigDecimal("amount")));
        result.put("chainType", json.getString("chain_type"));
        result.put("guardAddress", json.getString("guard_address"));

        return result;
    }

    private String getBalanceBySympbol(String symbol, BigDecimal value) {
        String result = null;
        BlAsset asset = assetMapper.selectBySymbol(symbol);
        if (asset != null) {
            result = value.divide(new BigDecimal(asset.getPrecision())).stripTrailingZeros().toPlainString()
                    + " " + asset.getSymbol();
        }
        return result;
    }


    private String feeStr(BigDecimal value) {
        String feeStr = null;
        if (null != value) {
            feeStr = value.divide(new BigDecimal(Constant.PRECISION)).stripTrailingZeros().toPlainString()
                    + " " + Constant.SYMBOL;
        }
        return feeStr;
    }

    private String getSymbol(String assetId) {
        BlAsset asset = realData.getSymbolByAssetId(assetId);
        String symbol = null;
        if (null != asset) {
            symbol = asset.getSymbol();
        }
        return symbol;
    }


    private String getBalance(String assetId, BigDecimal value) {
        BlAsset asset = realData.getSymbolByAssetId(assetId);
        String result = null;
        if (null != asset) {
            result = value.divide(new BigDecimal(asset.getPrecision())).stripTrailingZeros().toPlainString()
                    + " " + asset.getSymbol();
        }
        return result;
    }

    private BlGuarantee getGuarantee(String guaranteeId) {
        BlGuarantee guarantee = guaranteeMapper.selectByGuaranteeId(guaranteeId);
        if (null == guarantee) {
            String guaranteeMsg = null;
            try {
                guaranteeMsg = requestWalletService.getGuarantee(guaranteeId);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("访问底层钱包出错", e);
            }
            if (!StringUtil.isEmpty(guaranteeMsg)) {
                BlGuarantee gua = new BlGuarantee();
                JSONObject json = JSONObject.parseObject(guaranteeMsg);
                BigDecimal originAmount = json.getJSONObject("asset_orign").getBigDecimal("amount");
                BigDecimal targetAmount = json.getJSONObject("asset_target").getBigDecimal("amount");
                BigDecimal rate = targetAmount.divide(originAmount, 5, BigDecimal.ROUND_HALF_UP);
                String result = "1:" + rate.stripTrailingZeros().toPlainString();

                gua.setAssetOrign(getSymbol(json.getJSONObject("asset_orign").getString("asset_id")));
                gua.setAssetTarget(getSymbol(json.getJSONObject("asset_target").getString("asset_id")));
                gua.setGuaranteeId(guaranteeId);
                gua.setOwnerAddr(json.getString("owner_addr"));
                gua.setRate(result);
                guaranteeMapper.insert(gua);
                return gua;
            }

        }
        return guarantee;
    }
}
