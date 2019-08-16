package com.browser.dao.mapper;

import com.browser.dao.entity.ProposalInfo;

import java.util.List;

public interface ProposalInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ProposalInfo record);

    int insertSelective(ProposalInfo record);

    ProposalInfo selectByPrimaryKey(Integer id);

    List<ProposalInfo> selectList();

    ProposalInfo selectByRefereeId(String id);

    int updateByPrimaryKeySelective(ProposalInfo record);

    int updateByPrimaryKey(ProposalInfo record);
}