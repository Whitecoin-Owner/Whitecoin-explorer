package com.browser.dao.mapper;

import com.browser.dao.entity.BlTokenTransaction;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BlTokenTransactionMapper {
    BlTokenTransaction selectByPrimaryKey(@Param("id") Long id);
    List<BlTokenTransaction> selectAllByCond(BlTokenTransaction cond);
    List<BlTokenTransaction> selectListByUserAddress(BlTokenTransaction cond);
    List<BlTokenTransaction> selectTrxList(BlTokenTransaction cond);
    int insert(BlTokenTransaction record);
}
