package com.browser.dao.mapper;

import com.browser.dao.entity.BlScanInfo;

public interface BlScanInfoMapper {
    BlScanInfo selectFirst();
    int insert(BlScanInfo record);
    int updateBlockNumByPrimaryKey(BlScanInfo record);
}
