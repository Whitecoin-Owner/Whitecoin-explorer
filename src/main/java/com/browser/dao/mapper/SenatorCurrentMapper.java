package com.browser.dao.mapper;

import com.browser.dao.entity.SenatorCurrent;

import java.util.List;

public interface SenatorCurrentMapper {
    int deleteByPrimaryKey(Integer id);
    int deleteByName(String name);

    int insert(SenatorCurrent record);

    int insertSelective(SenatorCurrent record);

    SenatorCurrent selectByPrimaryKey(Integer id);
    List<SenatorCurrent> selectList(SenatorCurrent record);

    int updateByPrimaryKeySelective(SenatorCurrent record);

    int updateByPrimaryKey(SenatorCurrent record);
    int updateByAddress(SenatorCurrent record);

    int updateBySenatorId(SenatorCurrent record);
}