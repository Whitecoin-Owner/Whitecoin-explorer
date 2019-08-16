package com.browser.dao.mapper;

import com.browser.dao.entity.SenatorPrevious;

import java.util.List;

public interface SenatorPreviousMapper {
    int deleteByPrimaryKey(Integer id);
    int deleteByName(String name);

    int insert(SenatorPrevious record);

    int insertSelective(SenatorPrevious record);

    SenatorPrevious selectByPrimaryKey(Integer id);

    SenatorPrevious selectByName(String name);

    List<SenatorPrevious> selectList(SenatorPrevious record);

    int updateByPrimaryKeySelective(SenatorPrevious record);

    int updateByPrimaryKey(SenatorPrevious record);
    int updateByAddress(SenatorPrevious record);

    int updateBySenatorId(SenatorPrevious record);
}