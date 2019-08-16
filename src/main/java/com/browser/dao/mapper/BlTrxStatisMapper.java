package com.browser.dao.mapper;

import com.browser.dao.entity.BlTrxStatis;

public interface BlTrxStatisMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BlTrxStatis record);

    int insertSelective(BlTrxStatis record);

    BlTrxStatis selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BlTrxStatis record);

    int updateByPrimaryKey(BlTrxStatis record);
    
    int updateByTime(BlTrxStatis record);
    
    BlTrxStatis selectByTime(String time);
}