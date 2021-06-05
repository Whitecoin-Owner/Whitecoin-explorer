package com.browser.dao.mapper;

import java.util.List;

import com.browser.dao.entity.BlContractInfo;
import org.apache.ibatis.annotations.Param;

public interface BlContractInfoMapper {
    int deleteByPrimaryKey(String contractId);
    
    int deleteByBlockNum(Long blockNum);

    int insert(BlContractInfo record);

    int insertSelective(BlContractInfo record);

    BlContractInfo selectByPrimaryKey(String contractId);
    
    List<BlContractInfo> selectContractList(BlContractInfo record);

    List<BlContractInfo> selectContractListPage(@Param("record") BlContractInfo record, @Param("offset") Integer offset, @Param("limit") Integer limit);


    int updateByPrimaryKeySelective(BlContractInfo record);

    int updateByPrimaryKeyWithBLOBs(BlContractInfo record);

    int updateByPrimaryKey(BlContractInfo record);

    int countAll();
    
    int countContracts(String  address);
    
}