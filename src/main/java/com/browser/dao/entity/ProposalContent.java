package com.browser.dao.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProposalContent {
    private Integer id;

    private String proposalId;

    private String refereeId;

    private String referee;

    private String replacedId;

    private String replacedPerson;

    private BigDecimal voteRate;

    private Integer status;

    private String address;

    private Integer flag;
    private Integer symbol;

    private List<String> ids;

    private Integer page;
    private Integer rows;

    private Long block;
    private String proposer;
    private Date proposalTime;
    private BigDecimal amonut;
    private String lockNum;
    private String lockAddress;
    private List<ProposalContent> proList;

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

    public String getRefereeId() {
        return refereeId;
    }

    public void setRefereeId(String refereeId) {
        this.refereeId = refereeId == null ? null : refereeId.trim();
    }

    public String getReferee() {
        return referee;
    }

    public void setReferee(String referee) {
        this.referee = referee == null ? null : referee.trim();
    }

    public String getReplacedId() {
        return replacedId;
    }

    public void setReplacedId(String replacedId) {
        this.replacedId = replacedId;
    }

    public String getReplacedPerson() {
        return replacedPerson;
    }

    public void setReplacedPerson(String replacedPerson) {
        this.replacedPerson = replacedPerson == null ? null : replacedPerson.trim();
    }

    public BigDecimal getVoteRate() {
        return voteRate;
    }

    public void setVoteRate(BigDecimal voteRate) {
        this.voteRate = voteRate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
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

    public Integer getSymbol() {
        return symbol;
    }

    public void setSymbol(Integer symbol) {
        this.symbol = symbol;
    }

    public String getLockNum() {
        return lockNum;
    }

    public void setLockNum(String lockNum) {
        this.lockNum = lockNum;
    }

    public List<ProposalContent> getProList() {
        return proList;
    }

    public void setProList(List<ProposalContent> proList) {
        this.proList = proList;
    }

    public String getProposer() {
        return proposer;
    }

    public void setProposer(String proposer) {
        this.proposer = proposer;
    }

    public String getLockAddress() {
        return lockAddress;
    }

    public void setLockAddress(String lockAddress) {
        this.lockAddress = lockAddress;
    }
}