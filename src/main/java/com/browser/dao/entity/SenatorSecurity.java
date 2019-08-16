package com.browser.dao.entity;

public class SenatorSecurity {
    private Integer id;

    private String senatorId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSenatorId() {
        return senatorId;
    }

    public void setSenatorId(String senatorId) {
        this.senatorId = senatorId == null ? null : senatorId.trim();
    }
}