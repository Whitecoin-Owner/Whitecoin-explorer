package com.browser.dao.mapper;

import com.browser.dao.entity.BlGuarantee;

public interface BlGuaranteeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BlGuarantee record);

    int insertSelective(BlGuarantee record);

    BlGuarantee selectByPrimaryKey(Integer id);
    
    BlGuarantee selectByGuaranteeId(String guaranteeId);

    int updateByPrimaryKeySelective(BlGuarantee record);

    int updateByPrimaryKey(BlGuarantee record);
}