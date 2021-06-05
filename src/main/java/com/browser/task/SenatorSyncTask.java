package com.browser.task;

import com.browser.service.impl.RequestWalletService;
import com.browser.service.impl.SenatorService;
import com.browser.service.impl.SyncSenatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 提案信息查询
 */
@Component
public class SenatorSyncTask {

    private static Logger logger = LoggerFactory.getLogger(SenatorSyncTask.class);

    @Value("${scheduled.task}")
    private boolean task;

    @Autowired
    private RequestWalletService requestWalletService;

    @Autowired
    private SenatorService senatorService;

    @Autowired
    private SyncSenatorService syncSenatorService;

    @Scheduled(cron = "0 0/5 * * * ? ")
    public void syncData() {
        if (task) {
            logger.info("不开启定时任务");
            return;
        }
        logger.info("同步senator数据开始");
        try {
            senatorService.dealSenator();
        } catch (Exception e) {
            logger.error("同步senator数据异常：{}", e);
        }

        try {
            syncSenatorService.syncSenator();
        } catch (Exception e) {
            logger.error("同步senator数据异常：{}", e);
        }

        logger.info("同步senator数据结束");
    }
}
