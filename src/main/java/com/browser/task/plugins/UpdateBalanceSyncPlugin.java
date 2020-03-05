package com.browser.task.plugins;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.browser.config.RealData;
import com.browser.dao.entity.BLAddressBalance;
import com.browser.dao.entity.BlAsset;
import com.browser.service.impl.AddressBalanceServiceImpl;
import com.browser.service.impl.RequestWalletService;
import com.browser.task.ISyncPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 找到可能导致地址余额变动的交易并相应更新用户资产余额
 */
@Component
public class UpdateBalanceSyncPlugin implements ISyncPlugin {

    private static Logger logger = LoggerFactory.getLogger(UpdateBalanceSyncPlugin.class);

    @Autowired
    private RequestWalletService requestWalletService;
    @Autowired
    private RealData realData;
    @Autowired
    private AddressBalanceServiceImpl addressBalanceService;

    @Override
    public String pluginName() {
        return "UpdateBalanceSyncPlugin";
    }

    public void updateAddrCoinToDb(String addr, String assetId) {
        String balancesStr = requestWalletService.getBalances(addr);
        String accountName = null;
        try {
            accountName = requestWalletService.getAccountNameByAddr(addr);
        } catch (Exception e) {

        }
        JSONArray lockBalances = null;

        if(accountName!=null&& !accountName.isEmpty()) {
            // 如果有账户名，获取质押的余额
            String lockBalancesStr = requestWalletService.getLockBalance(accountName);
            if(lockBalancesStr != null) {
                lockBalances = JSON.parseArray(lockBalancesStr);
            }
        }
        if (balancesStr != null) {
            JSONArray balances = JSONObject.parseArray(balancesStr);

            // 把锁仓余额加入到用户资产中
            Map<String, BigDecimal> lockAmounts = new HashMap<>();
            if(lockBalances!=null) {
                for(int i=0;i<lockBalances.size();i++) {
                    JSONObject lockItem = lockBalances.getJSONObject(i);
                    String lockAssetId = lockItem.getString("lock_asset_id");
                    String lockAmountFullStr = lockItem.getString("lock_asset_amount");
                    BigDecimal lockAmountFullBn = new BigDecimal(lockAmountFullStr);
                    if(!lockAmounts.containsKey(lockAssetId)) {
                        lockAmounts.put(lockAssetId, lockAmountFullBn);
                    } else {
                        lockAmounts.put(lockAssetId, lockAmountFullBn.add(lockAmounts.get(lockAssetId)));
                    }
                }
            }

            for (int i = 0; i < balances.size(); i++) {
                JSONObject jsonObject = balances.getJSONObject(i);
                BlAsset blAsset = realData.getSymbolByAssetId(assetId);
                BigDecimal amountFullBn = jsonObject.getBigDecimal("amount");
                String balanceAssetId = jsonObject.getString("asset_id");
                if(balanceAssetId==null || !balanceAssetId.equals(assetId)) {
                    continue;
                }
                if(lockAmounts.containsKey(assetId)) {
                    amountFullBn = amountFullBn.add(lockAmounts.get(assetId));
                }
                BigDecimal balance = amountFullBn
                        .divide(new BigDecimal(blAsset.getPrecision()), blAsset.getPrecision().toString().length()-1, BigDecimal.ROUND_HALF_UP)
                        .stripTrailingZeros();
                String assetSymbol = blAsset.getSymbol();

                // update to db
                BLAddressBalance addressBalance = addressBalanceService.findByAddrAndAsset(addr, assetId);
                if(addressBalance==null) {
                    addressBalance = new BLAddressBalance();
                    addressBalance.setAddr(addr);
                    addressBalance.setName(accountName);
                    addressBalance.setAmount(balance);
                    addressBalance.setAssetId(assetId);
                    addressBalance.setAssetSymbol(assetSymbol);
                    addressBalanceService.saveAddressBalance(addressBalance);
                } else {
                    addressBalance.setName(accountName);
                    addressBalance.setAmount(balance);
                    addressBalanceService.updateAddressBalance(addressBalance);
                }
                logger.info("updated addr " + addr + (accountName!=null ? ("(" + accountName + ")") : "") + " balances done");
            }
        }
    }

    @Override
    public void applyOperation(JSONObject block, String txid, int opNum, int opType, String opTypeName, JSONObject opJSON, JSONObject receipt) {
        String[] tryAddrProps = new String[]{"addr", "caller_addr", "lock_balance_addr", "foreclose_addr", "from_addr", "to_addr", "payer"};
        String[] tryAssetIdProps = new String[] {"asset_id", "lock_asset_id", "foreclose_asset_id"};
        Set<String> addrs = new HashSet<>();
        boolean ok = false;
        for(String prop : tryAddrProps) {
            if(opJSON.containsKey(prop)) {
                addrs.add(opJSON.getString(prop));
                ok = true;
                break;
            }
        }
        if(!ok || addrs.isEmpty()) {
            return;
        }
        String assetId = null;
        ok = false;
        for(String prop : tryAssetIdProps) {
            if(opJSON.containsKey(prop)) {
                assetId = opJSON.getString(prop);
                ok = true;
                break;
            }
        }
        if(!ok || assetId == null || assetId.isEmpty()) {
            if(!opJSON.containsKey("amount") && !opJSON.containsKey("fee")) {
                return;
            }
            JSONObject amountMap;
            JSONObject feeMap;
            try {
                if(opJSON.containsKey("amount")) {
                    amountMap = opJSON.getJSONObject("amount");
                    if(!amountMap.containsKey("asset_id")) {
                        return;
                    }
                    assetId = amountMap.getString("asset_id");
                }
//                if(opJSON.containsKey("fee")) {
//                    feeMap = opJSON.getJSONObject("fee");
//                    if(!feeMap.containsKey("asset_id")) {
//                        return;
//                    }
//                    assetId = feeMap.getString("asset_id");
//                }
                if(assetId == null) {
                    assetId = "1.3.0";
                }
            }catch (Exception e) {
                return;
            }
        }
        // update addresses balances to db
        for(String addr : addrs) {
            try {
                updateAddrCoinToDb(addr, assetId);
            } catch (Exception e) {
                logger.error("update addr " + addr + " balances error", e);
            }
        }
    }
}
