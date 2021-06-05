package com.browser.task;

import com.browser.service.StatisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 首页数据缓存
 */
@Component
public class IndexEchartsTask {

    private static Logger logger = LoggerFactory.getLogger(IndexEchartsTask.class);


    @Autowired
    private StatisService statisService;

    @Value("${scheduled.task}")
    private boolean task;

    @Scheduled(cron = "0 0/2 * * * ? ")
    public void syncData() {
        if (task) {
            logger.info("不开启定时任务");
            return;
        }
        logger.info("【首页图表数据缓存】");
        try {
            statisService.indexEcharts();
        } catch (Exception e) {
            logger.error("首页图表数据缓存异常：", e);
        }
    }
}
