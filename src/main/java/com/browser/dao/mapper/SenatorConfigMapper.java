package com.browser.dao.mapper;

import com.browser.dao.entity.SenatorConfig;

import java.util.List;

public interface SenatorConfigMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SenatorConfig record);

    int insertSelective(SenatorConfig record);

    SenatorConfig selectByPrimaryKey(Integer id);
    SenatorConfig selectByName(String name);

    List<SenatorConfig> selectList();

    int updateByPrimaryKeySelective(SenatorConfig record);

    int updateByPrimaryKey(SenatorConfig record);
}