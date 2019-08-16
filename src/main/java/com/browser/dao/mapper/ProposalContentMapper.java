package com.browser.dao.mapper;

import com.browser.dao.entity.ProposalContent;

import java.util.List;

public interface ProposalContentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ProposalContent record);

    int insertSelective(ProposalContent record);

    ProposalContent selectByPrimaryKey(Integer id);
    ProposalContent selectJoinSenator(String referee);

    List<ProposalContent> selectList(ProposalContent record);
    List<ProposalContent> selectReferee(ProposalContent record);
    List<ProposalContent> selectJoinList(ProposalContent record);

    Integer selectId(String id);
    String selectRelationId(String proposalId);

    int updateByPrimaryKeySelective(ProposalContent record);

    int updateByPrimaryKey(ProposalContent record);

    int updateFlag(ProposalContent record);

    int updateByProposalIdSelective(ProposalContent record);

    int updateSuccStatus(ProposalContent record);
    int updateFailStatus(ProposalContent record);
    int updateAll();
}