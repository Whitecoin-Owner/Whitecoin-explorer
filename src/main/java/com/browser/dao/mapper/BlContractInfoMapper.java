package com.browser.dao.mapper;

import java.util.List;

import com.browser.dao.entity.BlContractInfo;

public interface BlContractInfoMapper {
    int deleteByPrimaryKey(String contractId);
    
    int deleteByBlockNum(Long blockNum);

    int insert(BlContractInfo record);

    int insertSelective(BlContractInfo record);

    BlContractInfo selectByPrimaryKey(String contractId);
    
    List<BlContractInfo> selectContractList(BlContractInfo record);
    
    int updateByPrimaryKeySelective(BlContractInfo record);

    int updateByPrimaryKeyWithBLOBs(BlContractInfo record);

    int updateByPrimaryKey(BlContractInfo record);
    
    int countContracts(String  address);
    
}