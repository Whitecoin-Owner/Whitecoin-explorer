package com.browser.dao.mapper;

import com.browser.dao.entity.BlTxContractBalanceChange;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BlTxContractBalanceChangeMapper {
    BlTxContractBalanceChange selectByPrimaryKey(@Param("id") Long id);
    List<BlTxContractBalanceChange> selectAllByCond(BlTxContractBalanceChange cond);
    int insert(BlTxContractBalanceChange record);
}
