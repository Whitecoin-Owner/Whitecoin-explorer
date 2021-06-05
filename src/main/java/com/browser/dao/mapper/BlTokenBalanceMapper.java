package com.browser.dao.mapper;

import com.browser.dao.entity.BlTokenBalance;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BlTokenBalanceMapper {

    int insert(BlTokenBalance record);

    BlTokenBalance selectByAddrAndTokenContract(BlTokenBalance cond);

    List<BlTokenBalance> selectAllByAddr(BlTokenBalance cond);

    List<BlTokenBalance> selectAllByTokenContract(BlTokenBalance cond);

    BlTokenBalance getSumBalanceByTokenContract(BlTokenBalance cond);

    int updateByPrimaryKeySelective(BlTokenBalance record);

    int updateByPrimaryKey(BlTokenBalance record);

    Integer getAddressCountByContractIdAndTokenSymbol(@Param("contractAddress") String contractAddress, @Param("tokenSymbol") String tokenSymbol);

    List<BlTokenBalance> getBlTokenBalanceListByContractAddress(@Param("contractAddr") String contractAddr);
}
