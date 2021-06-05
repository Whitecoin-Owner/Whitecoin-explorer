package com.browser.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.browser.tools.Constant;
import com.browser.tools.common.HttpUtil;
import com.browser.tools.common.RpcLink;
import com.browser.tools.common.WalletException;
import com.browser.wallet.beans.ContractTxReceipt;
import com.browser.wallet.beans.SimpleContractInfo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

@Service
public class RequestWalletService {
    private static Logger logger = LoggerFactory.getLogger(RequestWalletService.class);

    @Value("${wallet.url}")
    private String walletUrl;
    @Value("${wallet.caller}")
    private String walletRpcCaller;


    private static Set<String> logSet = new HashSet<>();

    static {
        logSet.add("info");
    }


    public String send(String method, List<Object> params) {

        JSONObject requestJson = new JSONObject();
        requestJson.put("jsonrpc", "2.0");
        requestJson.put("id", 1);
        requestJson.put("method", method);
        requestJson.put("params", params);
        JSONObject resultJson = null;
        String resultStr = null;
        String request = null;

        try {
            request = requestJson.toJSONString();
            logger.info("【请求钱包发送数据】:{}", request);

            String result = HttpUtil.post(walletUrl, request);
            logger.info("【请求钱包返回数据】:" + result);

            //为空重新查询
            if (StringUtils.isEmpty(result)) {
                return null;
            }
            resultJson = JSONObject.parseObject(result);
            if (resultJson != null) {
                JSONObject errorJson = resultJson.getJSONObject("error");
                if (errorJson != null) {
                    logger.error("【请求钱包返回数据】:{}", errorJson);
                    throw new WalletException(errorJson.toJSONString());
                } else {
                    resultStr = resultJson.getString("result");
                }
            }
        } catch (Exception e) {
            logger.error("请求钱包处理失败 " + walletUrl, e);
            throw new WalletException("请求钱包处理失败");
        }

        return resultStr;
    }

    public String getWalletRpcCaller() {
        return walletRpcCaller;
    }

    /**
     * 获取当前块号
     *
     * @return
     */
    public Long getBlockCount() {
        List<Object> params = new ArrayList<Object>();
        String result = send(RpcLink.BLOCK_COUNT, params);
        JSONObject resultJson = JSONObject.parseObject(result);
        return resultJson.getLong("head_block_num");
    }

    /**
     * 获取当前块信息
     *
     * @param block
     * @return
     */
    public String getBlockInfo(Long block) {
        List<Object> params = new ArrayList<Object>();
        params.add(block);
        return send(RpcLink.BLOCK_INFO, params);
    }

    /**
     * 根据交易hash查询Receipt信息
     *
     * @param trxId
     * @return
     */
    public String getTransaction(String trxId) {
        List<Object> params = new ArrayList<Object>();
        params.add(trxId);
        return send(RpcLink.BLOCK_TRX, params);
    }

    /**
     * 获取发行量
     *
     * @param
     * @return
     */
    public BigDecimal getAssetImp(String symbol) {
        if (symbol != null && "PAX".equals(symbol)) {
            symbol = "ERCPAX";
        }
        if (symbol != null && "ELF".equals(symbol)) {
            symbol = "ERCELF";
        }
        List<Object> params = new ArrayList<Object>();
        params.add(symbol);
        String result = send(RpcLink.GET_ASSET_IMP, params);
        BigDecimal supply = null;
        if (!StringUtils.isEmpty(result)) {
            JSONObject jsonObject = JSONObject.parseObject(result).getJSONObject("dynamic_data");
            Integer precision = JSONObject.parseObject(result).getInteger("precision");
            supply = jsonObject.getBigDecimal("current_supply");
            if (supply != null) {
                supply = supply.divide(new BigDecimal("10").pow(precision));
            }
        }
        return supply;
    }

    /**
     * 地址余额
     *
     * @param addr
     * @return
     */
    public String getBalances(String addr) {
        List<Object> params = new ArrayList<Object>();
        params.add(addr);
        return send(RpcLink.ADDR_BALANCES, params);
    }

    /**
     * 资产信息
     *
     * @param symbol,id
     * @return
     */
    public String getAsset(String symbol, String id) {
        List<Object> params = new ArrayList<Object>();
        params.add(symbol);
        params.add(id);
        return send(RpcLink.ASSET_INFO, params);
    }

    /**
     * 资产列表
     *
     * @param num
     * @return
     */
    public String getAssetList(Integer num) {
        List<Object> params = new ArrayList<Object>();
        params.add("");
        params.add(num);
        return send(RpcLink.LIST_ASSETS, params);
    }

    /**
     * 合约列表
     *
     * @param block
     * @return
     */
    public String getContractRegistered(Long block) {
        List<Object> params = new ArrayList<Object>();
        params.add(block);
        return send(RpcLink.CONTRACT_REGISTERED, params);
    }

    /**
     * 合约信息
     *
     * @param
     * @return
     */
    public String getContractInfo(String addr) {
        List<Object> params = new ArrayList<Object>();
        params.add(addr);
        return send(RpcLink.CONTRACT_INFO, params);
    }

    /**
     * 合约余额
     *
     * @param
     * @return
     */
    public String getContractBalance(String addr) {
        List<Object> params = new ArrayList<Object>();
        params.add(addr);
        return send(RpcLink.CONTRACT_BALANCE, params);
    }

    /**
     * 矿工信息
     *
     * @param
     * @return
     */
    public String getMiner(String minerId) {
        List<Object> params = new ArrayList<Object>();
        params.add(minerId);
        return send(RpcLink.GET_MINER, params);
    }

    /**
     * 查询账户信息
     *
     * @param
     * @return
     */
    public String getAccount(String accountId) {
        List<Object> params = new ArrayList<Object>();
        params.add(accountId);
        return send(RpcLink.GET_ACCOUNT, params);
    }

    /**
     * 查询手续费承兑单
     *
     * @param
     * @return
     */
    public String getGuarantee(String guaranteeId) {
        List<Object> params = new ArrayList<Object>();
        params.add(guaranteeId);
        return send(RpcLink.GET_GUARANTEE, params);
    }

    /**
     * 查询抵押资产
     *
     * @param
     * @return
     */
    public String getLockBalance(String accountName) {
        List<Object> params = new ArrayList<Object>();
        params.add(accountName);
        return send(RpcLink.LOCK_BALANCE, params);
    }

    /**
     * 根据地址获取账户名
     *
     * @param addr
     * @return
     */
    public String getAccountNameByAddr(String addr) {
        JSONObject obj = JSONObject.parseObject(send(RpcLink.GET_ACCOUNT_BY_ADDR, Arrays.asList(addr)));
        if (obj != null && obj.containsKey("name")) {
            return obj.getString("name");
        } else {
            return null;
        }
    }

    /**
     * 查询待领取
     *
     * @param
     * @return
     */
    public String getPaybackBalance(String addr) {
        List<Object> params = new ArrayList<Object>();
        params.add(addr);
        params.add(Constant.SYMBOL);
        return send(RpcLink.PAY_BACK_BALANCE, params);
    }

    /**
     * 查询现任senator
     *
     * @param
     * @return
     */
    public String getCurSenator() {
        List<Object> params = new ArrayList<Object>();
        params.add(0);
        params.add(1000);
        return send(RpcLink.LIST_SENATOR_MEMBERS, params);
    }

    /**
     * 查询历届senator
     *
     * @param
     * @return
     */
    public String getPreSenator() {
        List<Object> params = new ArrayList<Object>();
        params.add(0);
        params.add(1000);
        return send(RpcLink.LIST_ALL_SENATORS, params);
    }

    /**
     * 查询进行中的提案
     *
     * @param
     * @return
     */
    public String getProposal(String name) {
        List<Object> params = new ArrayList<Object>();
        params.add(name);
        return send(RpcLink.GET_REFERENDUM_FOR_VOTER, params);
    }

    /**
     * 查锁仓余额
     *
     * @param
     * @return
     */
    public String getlockedBalance(String accountName, String lockedAddress) {
        return invokeContractOffline(accountName, lockedAddress, "getUsers", "1000,0");
    }

    public String invokeContractOffline(String accountName, String contractAddress, String apiName, String apiArg) {
        List<Object> params = new ArrayList<Object>();
        params.add(accountName);
        params.add(contractAddress);
        params.add(apiName);
        params.add(apiArg);
        return send(RpcLink.INVOKE_CONTRACT_OFFLINE, params);
    }

    /**
     * 获取合约交易的receipt
     *
     * @param txid
     * @return
     */
    public List<ContractTxReceipt> getContractTxReceipt(String txid) {
        String res = send(RpcLink.GET_CONTRACT_RECEIPT, Collections.singletonList(txid));
        JSONArray array = JSON.parseArray(res);
        List<ContractTxReceipt> result = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            result.add(JSON.toJavaObject(array.getJSONObject(i), ContractTxReceipt.class));
        }
        return result;
    }

    public SimpleContractInfo getSimpleContractInfo(String contractId) {
        String res = send(RpcLink.GET_SIMPLE_CONTRACT_INFO, Collections.singletonList(contractId));
        return JSON.toJavaObject(JSON.parseObject(res), SimpleContractInfo.class);
    }

    public int getTokenPrecisionDecimals(String accountName, String contractId) {
        String res = invokeContractOffline(accountName, contractId, "precision", "");
        try {
            if (res.length() > 2 && res.startsWith("\"") && res.endsWith("\"")) {
                res = res.substring(1, res.length() - 1);
            }
            BigInteger precision = new BigInteger(res);
            if (precision.compareTo(BigInteger.ZERO) < 0) {
                throw new WalletException("invalid precision " + res + " of token contract " + contractId);
            }
            return precision.toString().length() - 1;
        } catch (Exception e) {
            throw new WalletException(e.getMessage());
        }
    }

    public BigInteger getTokenTotalSupply(String accountName, String contractId) {
        String res = invokeContractOffline(accountName, contractId, "totalSupply", "");
        try {
            if (res.length() > 2 && res.startsWith("\"") && res.endsWith("\"")) {
                res = res.substring(1, res.length() - 1);
            }
            BigInteger totalSupply = new BigInteger(res);
            if (totalSupply.compareTo(BigInteger.ZERO) < 0) {
                throw new WalletException("invalid totalSupply " + res + " of token contract " + contractId);
            }
            return totalSupply;
        } catch (Exception e) {
            throw new WalletException(e.getMessage());
        }
    }

    public String getTokenSymbol(String accountName, String contractId) {
        String res = invokeContractOffline(accountName, contractId, "tokenSymbol", "");
        try {
            if (res.length() > 2 && res.startsWith("\"") && res.endsWith("\"")) {
                res = res.substring(1, res.length() - 1);
            }
            return res;
        } catch (Exception e) {
            throw new WalletException(e.getMessage());
        }
    }

    /**
     * 查锁所有miner
     *
     * @param
     * @return
     */
    public JSONArray getAllMiners(Long miners) {
        List<Object> params = new ArrayList<Object>();
        params.add("");
        params.add(miners);
        String result = send(RpcLink.LIST_MINERS, params);
        JSONArray jsonArray = new JSONArray();
        if (result != null) {
            jsonArray = JSONObject.parseArray(result);
        }
        return jsonArray;
    }

}
