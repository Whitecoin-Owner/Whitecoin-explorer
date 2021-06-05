package com.browser.service.impl;

import com.browser.dao.entity.BlToken;
import com.browser.dao.mapper.BlTokenBalanceMapper;
import com.browser.dao.mapper.BlTokenMapper;
import com.browser.protocol.EUDataGridResult;
import com.browser.service.TokenService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TokenServiceImpl implements TokenService {
    @Resource
    private BlTokenMapper blTokenMapper;
    @Resource
    private BlTokenBalanceMapper blTokenBalanceMapper;

    @Override
    public List<BlToken> selectAllNotActive() {
        return blTokenMapper.selectAllNotActive();
    }

    @Override
    public List<BlToken> selectAllActive() {
        return blTokenMapper.selectAllActive();
    }

    @Override
    public EUDataGridResult getActiveTokenList(BlToken token) {
        Integer pageNum = token.getPage();
        Integer pageSize = token.getRows();
        List<BlToken> list = blTokenMapper.getActiveTokenList(token);
        // 根据合约地址和代币名称获取包含这个代币的地址数量
        // 创建一个返回值对象
        EUDataGridResult result = new EUDataGridResult();
        if (!CollectionUtils.isEmpty(list)) {
            Integer count;
            for (BlToken blToken : list) {
                count = blTokenBalanceMapper.getAddressCountByContractIdAndTokenSymbol(blToken.getContractAddress(), blToken.getTokenSymbol());
                if (null == count) {
                    blToken.setTokenAddressNum(0);
                } else {
                    blToken.setTokenAddressNum(count);
                }
            }
            if (null != pageNum && null != pageSize) {
                if (0 == pageNum) {
                    pageNum = 1;
                }
                int size = list.size();
                result.setTotal(size);
                result.setPages(size / pageSize);
                list = list.stream()
                        .sorted(Comparator.comparing(BlToken::getTokenAddressNum)
                                .reversed()).skip(pageSize * (pageNum - 1)).limit(pageSize)
                        .collect(Collectors.toList());
            } else {
                list = list.stream()
                        .sorted(Comparator.comparing(BlToken::getTokenAddressNum).reversed()).collect(Collectors.toList());
            }
        }
        result.setRows(list);
        return result;
    }

    @Override
    public List<BlToken> selectAllTopActiveTokenList() {
        return blTokenMapper.selectAllTopActiveTokenList();
    }

    @Override
    public BlToken selectByContractAddress(String contractAddress) {
        return blTokenMapper.selectByContractAddress(contractAddress);
    }

    @Override
    public int updateTokenTotalSupplyByContractAddress(String contractAddress, BigDecimal totalSupply) {
        BlToken cond = new BlToken();
        cond.setContractAddress(contractAddress);
        cond.setTokenSupply(totalSupply);
        return blTokenMapper.updateTokenTotalSupplyByContractAddress(cond);
    }
}
