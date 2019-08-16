package com.browser.dao.mapper;

import com.browser.dao.entity.Proposal;

import java.util.List;

public interface ProposalMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Proposal record);

    int insertSelective(Proposal record);

    Proposal selectByPrimaryKey(Integer id);
    Proposal selectByProposalId(String id);
    List<Proposal> selectCurList(Proposal record);
    List<Proposal> selectPreList(Proposal record);

    List<String> selectProposalId();

    Integer selectId(String id);

    int updateByPrimaryKeySelective(Proposal record);

    int updateByPrimaryKey(Proposal record);

    int updateStatus(List<String> ids);
    int updateAll();
    int updateBlock(Proposal record);
}