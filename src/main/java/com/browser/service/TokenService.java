package com.browser.service;

import com.browser.dao.entity.BlToken;
import com.browser.protocol.EUDataGridResult;

import java.math.BigDecimal;
import java.util.List;

public interface TokenService {
    List<BlToken> selectAllNotActive();
    List<BlToken> selectAllActive();
    EUDataGridResult getActiveTokenList(BlToken token);
    List<BlToken> selectAllTopActiveTokenList();
    BlToken selectByContractAddress(String contractAddress);
    int updateTokenTotalSupplyByContractAddress(String contractAddress, BigDecimal totalSupply);
}
