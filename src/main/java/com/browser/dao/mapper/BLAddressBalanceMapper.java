package com.browser.dao.mapper;

import com.browser.dao.entity.BLAddressBalance;

import java.util.List;
import java.util.Map;

public interface BLAddressBalanceMapper {
    int deleteByPrimaryKey(Long id);
    int delete();

    int insert(BLAddressBalance record);

    int insertSelective(BLAddressBalance record);

    BLAddressBalance selectByPrimaryKey(Long id);

    List<BLAddressBalance> selectByAsset(String assetId);

    List<BLAddressBalance> selectTopTichList(Map<String, Object> cond);

    BLAddressBalance selectByAddrAndAssetId(BLAddressBalance condition);

    List<String> selectAllAddressInTransactionsNotInAddressBalance();

    List<String> selectAllAddressInTransactions();

    int updateByPrimaryKeySelective(BLAddressBalance record);

    int updateByPrimaryKey(BLAddressBalance record);
}
