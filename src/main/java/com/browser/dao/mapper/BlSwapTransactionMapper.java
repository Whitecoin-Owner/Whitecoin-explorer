package com.browser.dao.mapper;

import com.browser.dao.entity.BlSwapTransaction;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BlSwapTransactionMapper {
        BlSwapTransaction selectByPrimaryKey(@Param("id") Long id);
        List<BlSwapTransaction> selectAllByCond(BlSwapTransaction cond);
        int insert(BlSwapTransaction record);

        List<BlSwapTransaction> selectListByUserAddress(BlSwapTransaction cond);
}
