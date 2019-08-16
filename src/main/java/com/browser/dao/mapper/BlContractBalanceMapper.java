package com.browser.dao.mapper;

import java.util.List;

import com.browser.dao.entity.BlContractBalance;

public interface BlContractBalanceMapper {
    int deleteByPrimaryKey(Integer id);
    
    int deleteByBlockNum(Long blockNum);

    int insert(BlContractBalance record);

    int insertSelective(BlContractBalance record);

    BlContractBalance selectByPrimaryKey(Integer id);
    
    List<BlContractBalance> selectByContractId(String contractId);

    int updateByPrimaryKeySelective(BlContractBalance record);

    int updateByPrimaryKey(BlContractBalance record);
}