package com.browser.dao.mapper;

import com.alibaba.fastjson.JSONObject;
import com.browser.dao.entity.BlTxEvents;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BlTxEventsMapper {
    BlTxEvents selectByPrimaryKey(@Param("id") Long id);

    Integer existsCloseCdcByCdcId(@Param("cdcId") String cdcId);

    List<BlTxEvents> selectAllByCond(BlTxEvents cond);

    List<String> getEventArgListByEventName(@Param("eventName") String eventName, @Param("contractAddress") String contractAddress);

    List<BlTxEvents> getDaiTxOpenCdcEventListByCondition(@Param("callerAddr") String callerAddr, @Param("contractAddress") String contractAddress);

    List<BlTxEvents> getDaiTxcEventListByCondition(@Param("callerAddr") String callerAddr, @Param("contractAddress") String contractAddress);

//    List<BlTxEvents> getDaiTxEventListByCondition(@Param("callerAddr") String callerAddr, @Param("contractAddress") String contractAddress);

    List<BlTxEvents> getDaiTxLiquidateEventListByCondition(@Param("contractAddress") String contractAddress);

    List<BlTxEvents> getBlTxEventListByCdcIdNotExistOpenCdc(@Param("cdcId") String cdcId);

    int insert(BlTxEvents record);

    JSONObject queryAllSiteInfo();

}
