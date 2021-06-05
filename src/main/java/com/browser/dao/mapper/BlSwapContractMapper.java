package com.browser.dao.mapper;

import com.browser.dao.entity.BlSwapContract;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BlSwapContractMapper {
    BlSwapContract selectByPrimaryKey(@Param("id") Long id);
    BlSwapContract selectByContractAddress(@Param("contractAddress") String contractAddress);
    List<BlSwapContract> selectAll();
    List<BlSwapContract> selectAllByCond(BlSwapContract cond);
    int insert(BlSwapContract record);
}
