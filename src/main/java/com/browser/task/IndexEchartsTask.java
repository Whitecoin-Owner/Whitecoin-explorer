package com.browser.task;

import com.alibaba.fastjson.JSONArray;
import com.browser.config.RealData;
import com.browser.dao.entity.BlTransaction;
import com.browser.service.StatisService;
import com.browser.tools.common.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 首页数据缓存
 */
@Component
public class IndexEchartsTask {

    private static Logger logger = LoggerFactory.getLogger(IndexEchartsTask.class);


    @Autowired
    private StatisService statisService;


    @Scheduled(cron = "0 0/59 * * * ? ")
    public void syncData() {
        logger.info("【首页图表数据缓存】");
        try {
            statisService.indexEcharts();
        } catch (Exception e) {
            logger.error("首页图表数据缓存异常：{}",e);
        }
    }
}
