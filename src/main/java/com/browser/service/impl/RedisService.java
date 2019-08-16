package com.browser.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.browser.dao.entity.BlAsset;
import com.browser.tools.RedisKeyConstants;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Redis存取服务类
 */
@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public String getWithdrawStatus(String trxId) {
        Object result = redisTemplate.opsForValue().get(RedisKeyConstants.LINK_WITHDRAW_STATUS + trxId);
        if (result == null) {
            return null;
        } else {
            return (String) result;
        }
    }
    
    public void putWithdrawStatus(String trxId,Integer status) {
        redisTemplate.opsForValue().set(RedisKeyConstants.LINK_WITHDRAW_STATUS + trxId, status);
    }

    public void putCrosschainAddr(String trxId,String addr) {
        redisTemplate.opsForValue().set(RedisKeyConstants.LINK_CROSSCHAIN_ADDR + trxId, addr);
    }
    
    public String getCrosschainAddr(String trxId) {
        Object result = redisTemplate.opsForValue().get(RedisKeyConstants.LINK_CROSSCHAIN_ADDR + trxId);
        if (result == null) {
            return null;
        } else {
            return (String) result;
        }
    }

       public void putAccountName(String addr,String accountName) {
        redisTemplate.opsForValue().set(RedisKeyConstants.LINK_REGISTER_ADDR + addr, accountName);
    }

    public String getAccountName(String addr) {
        Object result = redisTemplate.opsForValue().get(RedisKeyConstants.LINK_REGISTER_ADDR + addr);
        if (result == null) {
            return null;
        } else {
            return (String) result;
        }
    }

    public void putAccountAddr(String accountName,String addr) {
        redisTemplate.opsForValue().set(RedisKeyConstants.LINK_REGISTER_NAME + accountName, addr);
    }

    public String getAccountAddr(String accountName) {
        Object result = redisTemplate.opsForValue().get(RedisKeyConstants.LINK_REGISTER_NAME + accountName);
        if (result == null) {
            return null;
        } else {
            return (String) result;
        }
    }

    public void putMinerInfo(String minerId,JSONObject jsonObject) {
        JSONObject json =new JSONObject();
        json.put("name",jsonObject.getString("name"));
        json.put("addr",jsonObject.getString("addr"));
        redisTemplate.opsForValue().set(RedisKeyConstants.LINK_MINERINFO + minerId, json);
    }

    public JSONObject getMinerInfo(String minerId) {
        Object result = redisTemplate.opsForValue().get(RedisKeyConstants.LINK_MINERINFO + minerId);
        if (result == null) {
            return null;
        } else {
            return (JSONObject) result;
        }
    }

    public void putCrosschainTxId(String trxId,String withdrawIds) {
    	redisTemplate.opsForValue().set(RedisKeyConstants.LINK_CROSSCHAIN_ADDR + trxId, withdrawIds);
    }
    
    public String getCrosschainTxId(String trxId) {
    	Object result = redisTemplate.opsForValue().get(RedisKeyConstants.LINK_CROSSCHAIN_ADDR + trxId);
    	if (result == null) {
    		return null;
    	} else {
    		return (String) result;
    	}
    }

    public String getUnSignTxId(String signTrxId) {
    	Object result = redisTemplate.opsForValue().get(RedisKeyConstants.LINK_CROSSCHAIN_TXID + signTrxId);
    	if (result == null) {
    		return null;
    	} else {
    		return (String) result;
    	}
    }
    
    public void putUnSignTxId(String signTrxId,String withdrawTxIds) {
    	redisTemplate.opsForValue().set(RedisKeyConstants.LINK_CROSSCHAIN_TXID + signTrxId, withdrawTxIds);
    }
    
    public BlAsset getAsset(String assetId) {
    	Object result = redisTemplate.opsForValue().get(RedisKeyConstants.LINK_ASSET + assetId);
    	if (result == null) {
    		return null;
    	} else {
    		return (BlAsset) result;
    	}
    }
    
    public void putAsset(String assetId,BlAsset blAsset) {
    	redisTemplate.opsForValue().set(RedisKeyConstants.LINK_ASSET + assetId, blAsset);
    }

    public List<BlAsset> getCrossAsset() {
    	Object result = redisTemplate.opsForValue().get(RedisKeyConstants.LINK_CROSS_ASSET);
    	if (result == null) {
    		return null;
    	} else {
    		return (List<BlAsset>) result;
    	}
    }

    public void putCrossAsset(List<BlAsset> list) {
    	redisTemplate.opsForValue().set(RedisKeyConstants.LINK_CROSS_ASSET,list);
    }

    public String getCrossAssetNum() {
    	Object result = redisTemplate.opsForValue().get(RedisKeyConstants.LINK_CROSS_ASSET_NUM);
    	if (result == null) {
    		return null;
    	} else {
    		return (String) result;
    	}
    }

    public void putCrossAssetNum(String assetInfo) {
    	redisTemplate.opsForValue().set(RedisKeyConstants.LINK_CROSS_ASSET_NUM, assetInfo,60, TimeUnit.SECONDS);
    }

    public BigDecimal getCurIssueNum() {
    	Object result = redisTemplate.opsForValue().get(RedisKeyConstants.LINK_CURRENT_ISSUE);
    	if (result == null) {
    		return null;
    	} else {
    		return (BigDecimal) result;
    	}
    }

    public void putCurIssueNum(BigDecimal totalSupply) {
    	redisTemplate.opsForValue().set(RedisKeyConstants.LINK_CURRENT_ISSUE, totalSupply,60, TimeUnit.SECONDS);
    }

    public void putSenatorName(String name) {
    	redisTemplate.opsForValue().set(RedisKeyConstants.LINK_SENATOR, name);
    }

    public String getSenatorName() {
        Object result = redisTemplate.opsForValue().get(RedisKeyConstants.LINK_SENATOR);
        if (result == null) {
            return null;
        } else {
            return (String) result;
        }
    }

    public void putSenatorName(String id,String name) {
    	redisTemplate.opsForValue().set(RedisKeyConstants.LINK_SENATOR_NAME+id, name);
    }

    public String getSenatorName(String id) {
        Object result = redisTemplate.opsForValue().get(RedisKeyConstants.LINK_SENATOR_NAME+id);
        if (result == null) {
            return null;
        } else {
            return (String) result;
        }
    }

    public void putSenatorAddress(String id,String address) {
    	redisTemplate.opsForValue().set(RedisKeyConstants.LINK_SENATOR_ADDRESS+id, address);
    }

    public String getSenatorAddress(String id) {
        Object result = redisTemplate.opsForValue().get(RedisKeyConstants.LINK_SENATOR_ADDRESS+id);
        if (result == null) {
            return null;
        } else {
            return (String) result;
        }
    }

    public void putCurSenatorMap(Map<String,String> curMap) {
        redisTemplate.opsForHash().putAll(RedisKeyConstants.LINK_SENATOR_CURRENT,curMap);
    }

    public void putSingleCurSenatorMap(String key,String value) {
        redisTemplate.opsForHash().put(RedisKeyConstants.LINK_SENATOR_CURRENT,key,value);
    }

    public String getSingleCurSenatorMap(String key) {
        Object result = redisTemplate.opsForHash().get(RedisKeyConstants.LINK_SENATOR_CURRENT,key);
        if (result == null) {
            return null;
        } else {
            return (String) result;
        }
    }

    public void deleteSingleCurSenatorMap(String key) {
       redisTemplate.opsForHash().delete(RedisKeyConstants.LINK_SENATOR_CURRENT,key);
    }

    public void putPreSenatorMap(Map<String,String> curMap) {
        redisTemplate.opsForHash().putAll(RedisKeyConstants.LINK_SENATOR_PREVIOUS,curMap);
    }

    public void putSinglePreSenatorMap(String key,String value) {
        redisTemplate.opsForHash().put(RedisKeyConstants.LINK_SENATOR_PREVIOUS,key,value);
    }

    public String getSinglePreSenatorMap(String key) {
        Object result = redisTemplate.opsForHash().get(RedisKeyConstants.LINK_SENATOR_PREVIOUS,key);
        if (result == null) {
            return null;
        } else {
            return (String) result;
        }
    }


    public void putTotalReward(BigDecimal reward) {
        redisTemplate.opsForValue().set(RedisKeyConstants.LINK_BLOCK_REWARDS, reward);
    }

    public BigDecimal getTotalReward() {
        Object result = redisTemplate.opsForValue().get(RedisKeyConstants.LINK_BLOCK_REWARDS);
        if (result == null) {
            return null;
        } else {
            return (BigDecimal) result;
        }
    }

    public void putTotalTrx(Integer trxNum) {
        redisTemplate.opsForValue().set(RedisKeyConstants.LINK_TRX_TRXNUMS, trxNum);
    }

    public Integer getTotalTrx() {
        Object result = redisTemplate.opsForValue().get(RedisKeyConstants.LINK_TRX_TRXNUMS);
        if (result == null) {
            return null;
        } else {
            return (Integer) result;
        }
    }

}
