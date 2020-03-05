package com.browser.service.impl;

import com.browser.dao.entity.BLAddressBalance;
import com.browser.dao.mapper.BLAddressBalanceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AddressBalanceServiceImpl {
    private static Logger logger = LoggerFactory.getLogger(AddressBalanceServiceImpl.class);

    @Autowired
    BLAddressBalanceMapper addressBalanceMapper;

    public int saveAddressBalance(BLAddressBalance addressBalance) {
        return addressBalanceMapper.insert(addressBalance);
    }

    public BLAddressBalance findById(long id) {
        return addressBalanceMapper.selectByPrimaryKey(id);
    }

    public BLAddressBalance findByAddrAndAsset(String addr, String assetId) {
        BLAddressBalance cond = new BLAddressBalance();
        cond.setAddr(addr);
        cond.setAssetId(assetId);
        return addressBalanceMapper.selectByAddrAndAssetId(cond);
    }

    public List<String> selectAllAddressInTransactionsNotInAddressBalance() {
        return addressBalanceMapper.selectAllAddressInTransactionsNotInAddressBalance();
    }

    public List<String> selectAllAddressInTransactions() {
        return addressBalanceMapper.selectAllAddressInTransactions();
    }

    public List<BLAddressBalance> selectTopTichList(String assetId, int topN) {
        Map<String, Object> cond = new HashMap<>();
        cond.put("assetId", assetId);
        cond.put("n", topN);
        return addressBalanceMapper.selectTopTichList(cond);
    }

    public int updateAddressBalance(BLAddressBalance addressBalance) {
        return addressBalanceMapper.updateByPrimaryKey(addressBalance);
    }
}
