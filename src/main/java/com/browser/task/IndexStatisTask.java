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
public class IndexStatisTask {

    private static Logger logger = LoggerFactory.getLogger(IndexStatisTask.class);

    @Autowired
    private StatisService statisService;

    @Value("${scheduled.task}")
    private boolean task;

    @Scheduled(cron = "0/5 * * * * ? ")
    public void syncData() {
        if (task) {
            logger.info("不开启定时任务");
            return;
        }
        logger.info("【首页数据缓存】");
        try {
            statisService.newBlockLinkStatic();
        } catch (Exception e) {
            logger.error("首页数据缓存异常：{}", e);
        }
    }
}
