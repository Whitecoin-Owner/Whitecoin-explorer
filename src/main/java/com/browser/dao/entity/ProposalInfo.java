package com.browser.dao.entity;

import java.util.Date;

public class ProposalInfo {
    private Integer id;

    private String refereeId;

    private String replacedId;

    private Long block;

    private Date blockTime;

    private Date createTime;

    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRefereeId() {
        return refereeId;
    }

    public void setRefereeId(String refereeId) {
        this.refereeId = refereeId == null ? null : refereeId.trim();
    }

    public String getReplacedId() {
        return replacedId;
    }

    public void setReplacedId(String replacedId) {
        this.replacedId = replacedId == null ? null : replacedId.trim();
    }

    public Long getBlock() {
        return block;
    }

    public void setBlock(Long block) {
        this.block = block;
    }

    public Date getBlockTime() {
        return blockTime;
    }

    public void setBlockTime(Date blockTime) {
        this.blockTime = blockTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}