package com.browser.service;

public interface ScanInfoService {
    Long queryBlockNum();

    int updateOrInsertBlockNum(long blockNum);
}
