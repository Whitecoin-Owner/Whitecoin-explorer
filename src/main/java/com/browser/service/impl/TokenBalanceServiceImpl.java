package com.browser.service.impl;

import com.browser.dao.entity.BlTokenBalance;
import com.browser.dao.mapper.BlTokenBalanceMapper;
import com.browser.service.TokenBalanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
public class TokenBalanceServiceImpl implements TokenBalanceService {
    @Resource
    private BlTokenBalanceMapper tokenBalanceMapper;

    @Override
    public BlTokenBalance findTokenBalanceByAddrAndTokenContract(String addr, String tokenContract) {
        BlTokenBalance cond = new BlTokenBalance();
        cond.setAddr(addr);
        cond.setTokenContract(tokenContract);
        return tokenBalanceMapper.selectByAddrAndTokenContract(cond);
    }

    @Override
    public List<BlTokenBalance> findAllTokenBalancesByAddr(String addr) {
        BlTokenBalance cond = new BlTokenBalance();
        cond.setAddr(addr);
        return tokenBalanceMapper.selectAllByAddr(cond);
    }

    @Override
    public List<BlTokenBalance> getBlTokenBalanceListByContractAddress(String contractAddr) {
        return tokenBalanceMapper.getBlTokenBalanceListByContractAddress(contractAddr);
    }

    @Override
    public List<BlTokenBalance> findAllTokenBalancesByTokenContract(String tokenContract) {
        BlTokenBalance cond = new BlTokenBalance();
        cond.setTokenContract(tokenContract);
        return tokenBalanceMapper.selectAllByTokenContract(cond);
    }

    @Override
    public int updateOrInsertTokenBalance(String addr, String tokenContract, String tokenSymbol, BigDecimal amount) {
        BlTokenBalance cond = new BlTokenBalance();
        cond.setAddr(addr);
        cond.setTokenContract(tokenContract);
        BlTokenBalance record = tokenBalanceMapper.selectByAddrAndTokenContract(cond);
        if (record != null) {
            record.setAmount(amount);
            return tokenBalanceMapper.updateByPrimaryKeySelective(record);
        } else {
            record = new BlTokenBalance();
            record.setAmount(amount);
            record.setAddr(addr);
            record.setTokenContract(tokenContract);
            record.setTokenSymbol(tokenSymbol);
            return tokenBalanceMapper.insert(record);
        }
    }

    @Override
    public BigDecimal getSumBalanceByTokenContract(String tokenContract) {
        BlTokenBalance cond = new BlTokenBalance();
        cond.setTokenContract(tokenContract);
        BlTokenBalance sumRecord = tokenBalanceMapper.getSumBalanceByTokenContract(cond);
        if (sumRecord == null) {
            return BigDecimal.ZERO;
        }
        return sumRecord.getAmount() != null ? sumRecord.getAmount() : BigDecimal.ZERO;
    }
}
