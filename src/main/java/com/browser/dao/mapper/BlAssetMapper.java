package com.browser.dao.mapper;

import java.util.List;

import com.browser.dao.entity.BlAsset;

public interface BlAssetMapper {
    int deleteByPrimaryKey(Integer id);
    int delete();

    int insert(BlAsset record);

    int insertSelective(BlAsset record);

    BlAsset selectByPrimaryKey(Integer id);
    
    BlAsset selectByAsset(String asset);
    BlAsset selectBySymbol(String symbol);

    List<BlAsset> selectAll();

    int updateByPrimaryKeySelective(BlAsset record);

    int updateByPrimaryKey(BlAsset record);
}