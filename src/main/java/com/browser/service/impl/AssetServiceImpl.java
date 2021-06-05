package com.browser.service.impl;

import com.browser.dao.entity.BlAsset;
import com.browser.dao.mapper.BlAssetMapper;
import com.browser.service.AssetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class AssetServiceImpl implements AssetService {
    @Resource
    private BlAssetMapper blAssetMapper;


    @Override
    public BlAsset selectByAssetId(String assetId) {
        return blAssetMapper.selectByAsset(assetId);
    }

    @Override
    public BlAsset selectByAssetSymbol(String assetSymbol) {
        return blAssetMapper.selectBySymbol(assetSymbol);
    }
}
