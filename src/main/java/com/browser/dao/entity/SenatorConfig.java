package com.browser.dao.entity;

import java.util.Date;

public class SenatorConfig {
    private Integer id;

    private String senatorName;

    private String email;

    private Date createTime;

    private String address;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSenatorName() {
        return senatorName;
    }

    public void setSenatorName(String senatorName) {
        this.senatorName = senatorName == null ? null : senatorName.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}