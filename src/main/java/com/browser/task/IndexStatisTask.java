package com.browser.task;

import com.browser.config.RealData;
import com.browser.service.StatisService;
import com.browser.service.impl.RequestWalletService;
import com.browser.service.impl.SyncSenatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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



    @Scheduled(cron = "0/5 * * * * ? ")
    public void syncData() {
        logger.info("【首页数据缓存】");
        try {
            statisService.newXwcStatic();
        } catch (Exception e) {
            logger.error("首页数据缓存异常：{}",e);
        }
    }
}
