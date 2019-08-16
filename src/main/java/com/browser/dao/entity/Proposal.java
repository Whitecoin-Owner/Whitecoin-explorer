package com.browser.dao.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Proposal {
    private Integer id;

    private String proposalId;

    private String proposerId;

    private String proposer;

    private Long block;

    private Date proposalTime;

    private BigDecimal amonut;

    private String amonutStr;

    private Integer status;
    private String statusStr;

    private Date createTime;

    private List<ProposalContent> content;

    private Integer page;
    private Integer rows;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProposalId() {
        return proposalId;
    }

    public void setProposalId(String proposalId) {
        this.proposalId = proposalId == null ? null : proposalId.trim();
    }

    public String getProposerId() {
        return proposerId;
    }

    public void setProposerId(String proposerId) {
        this.proposerId = proposerId == null ? null : proposerId.trim();
    }

    public String getProposer() {
        return proposer;
    }

    public void setProposer(String proposer) {
        this.proposer = proposer == null ? null : proposer.trim();
    }

    public Long getBlock() {
        return block;
    }

    public void setBlock(Long block) {
        this.block = block;
    }

    public Date getProposalTime() {
        return proposalTime;
    }

    public void setProposalTime(Date proposalTime) {
        this.proposalTime = proposalTime;
    }

    public BigDecimal getAmonut() {
        return amonut;
    }

    public void setAmonut(BigDecimal amonut) {
        this.amonut = amonut;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public List<ProposalContent> getContent() {
        return content;
    }

    public void setContent(List<ProposalContent> content) {
        this.content = content;
    }

    public String getAmonutStr() {
        return amonutStr;
    }

    public void setAmonutStr(String amonutStr) {
        this.amonutStr = amonutStr;
    }

    public String getStatusStr() {
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }
}