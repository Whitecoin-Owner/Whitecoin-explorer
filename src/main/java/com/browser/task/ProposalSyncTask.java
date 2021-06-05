package com.browser.task;

import com.browser.service.impl.ProposalService;
import com.browser.service.impl.RequestWalletService;
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
public class ProposalSyncTask {

    private static Logger logger = LoggerFactory.getLogger(ProposalSyncTask.class);

    @Value("${scheduled.task}")
    private boolean task;

    @Autowired
    private RequestWalletService requestWalletService;

    @Autowired
    private ProposalService proposalService;

    @Scheduled(cron = "0/30 * * * * ? ")
    public void syncData() {
        if (task) {
            logger.info("不开启定时任务");
            return;
        }
        logger.info("同步提案数据开始");
        try {
            proposalService.dealProposal();
        } catch (Exception e) {
            logger.error("同步提案数据异常：{}", e);
        }
        try {
            proposalService.updateBlockInfo();
        } catch (Exception e) {
            logger.error("同步提案数据异常：{}", e);
        }
        logger.info("同步提案数据结束");
    }
}
