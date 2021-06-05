package com.browser.service.impl;

import com.browser.dao.entity.BlScanInfo;
import com.browser.dao.mapper.BlScanInfoMapper;
import com.browser.service.ScanInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class ScanInfoServiceImpl implements ScanInfoService {
    @Resource
    private BlScanInfoMapper scanInfoMapper;

    @Override
    public Long queryBlockNum() {
        BlScanInfo record = scanInfoMapper.selectFirst();
        if(record!=null) {
            return record.getBlockNum();
        }
        return null;
    }

    @Override
    public int updateOrInsertBlockNum(long blockNum) {
        BlScanInfo record = scanInfoMapper.selectFirst();
        if(record==null){
            record = new BlScanInfo();
            record.setBlockNum(blockNum);
            return scanInfoMapper.insert(record);
        }
        record.setBlockNum(blockNum);
        return scanInfoMapper.updateBlockNumByPrimaryKey(record);
    }
}
