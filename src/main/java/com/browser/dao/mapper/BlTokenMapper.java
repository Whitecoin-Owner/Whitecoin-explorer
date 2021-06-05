package com.browser.dao.mapper;

import com.browser.dao.entity.BlToken;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface BlTokenMapper {
    BlToken selectByPrimaryKey(@Param("id") Long id);
    List<BlToken> selectAllByCond(BlToken cond);
    BlToken selectByContractAddress(@Param("contractAddress") String contractAddress);
    int insert(BlToken record);
    int updateBlToken(BlToken record);
    Set<String> getTpContractAddressList();
    int updateTokenTotalSupplyByContractAddress(BlToken cond);
    List<BlToken> selectAll();
    List<BlToken> selectAllActive();
    List<BlToken> selectAllNotActive();
    List<BlToken> getActiveTokenList(BlToken cond);
    List<BlToken> selectAllTopActiveTokenList();
    Integer getPrecisionByContractAddress(@Param("contractAddress") String contractAddress);
}
