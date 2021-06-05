package com.browser.service;

import com.browser.dao.entity.BlTokenBalance;

import java.math.BigDecimal;
import java.util.List;

public interface TokenBalanceService {

    BlTokenBalance findTokenBalanceByAddrAndTokenContract(String addr, String tokenContract);

    List<BlTokenBalance> findAllTokenBalancesByAddr(String addr);

    List<BlTokenBalance> getBlTokenBalanceListByContractAddress(String contractAddr);

    List<BlTokenBalance> findAllTokenBalancesByTokenContract(String tokenContract);

    int updateOrInsertTokenBalance(String addr, String tokenContract, String tokenSymbol, BigDecimal amount);

    BigDecimal getSumBalanceByTokenContract(String tokenContract);
}
