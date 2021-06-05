package com.browser.dao.mapper;

import java.math.BigDecimal;
import java.util.List;

import com.browser.dao.entity.BlBlock;
import org.apache.ibatis.annotations.Param;

public interface BlBlockMapper {
    int deleteByPrimaryKey(String blockId);
    
    int deleteByBlockNum(Long blockNum);

    int insert(BlBlock record);
    
    int insertBatch(List<BlBlock> record);

    int insertSelective(BlBlock record);

    BlBlock selectByPrimaryKey(String blockId);
    
    BlBlock selectByBlockNum(Long blockNum);
    
    BlBlock statisByMinerName(String minerName);
    
    List<BlBlock> selectNewBlockInfo();
    
    List<BlBlock> getBlockInfoList(BlBlock record);

    List<BlBlock> getBlockInfoListByRange(@Param("startBlockNum") Long startBlockNum, @Param("endBlockNum") Long endBlockNum);
    
    BlBlock getBlockByAddr(String address);
    
    Long queryBlockNum();
    
    BigDecimal queryBlockRewards();

    int updateByPrimaryKeySelective(BlBlock record);

    int updateByPrimaryKey(BlBlock record);
    
    String getMinerName(String address);

}