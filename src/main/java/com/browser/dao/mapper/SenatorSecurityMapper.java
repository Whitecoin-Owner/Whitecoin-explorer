package com.browser.dao.mapper;

import com.browser.dao.entity.SenatorSecurity;

import java.util.List;

public interface SenatorSecurityMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SenatorSecurity record);

    int insertSelective(SenatorSecurity record);

    SenatorSecurity selectByPrimaryKey(Integer id);
    List<String> selectList();

    int updateByPrimaryKeySelective(SenatorSecurity record);

    int updateByPrimaryKey(SenatorSecurity record);
}