package com.browser.service;

import com.browser.dao.entity.BlAsset;

public interface AssetService {
    BlAsset selectByAssetId(String assetId);
    BlAsset selectByAssetSymbol(String assetSymbol);
}
