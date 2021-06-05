package com.browser.task;

import com.browser.service.impl.AddressBalanceServiceImpl;
import com.browser.task.plugins.UpdateBalanceSyncPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BalanceSyncTask {
    private static Logger logger = LoggerFactory.getLogger(BalanceSyncTask.class);

    @Autowired
    private AddressBalanceServiceImpl addressBalanceService;
    @Autowired
    private UpdateBalanceSyncPlugin updateBalanceSyncPlugin;

    @Value("${scheduled.task}")
    private boolean task;

    @Scheduled(cron = "0/59 * * * * ? ")
    public void syncData() {
        if (task) {
            logger.info("不开启定时任务");
            return;
        }
        logger.info("同步地址余额开始");
        // 查询交易中出现了地址但是没在地址余额表中有记录的数据
        List<String> addresses = addressBalanceService.selectAllAddressInTransactions();
        for (String addr : addresses) {
            if (!addr.startsWith("XWC")) {
                continue;
            }
            try {
                updateBalanceSyncPlugin.updateAddrCoinToDb(addr, "1.3.0");
            } catch (Exception e) {
                logger.error("update balances of addr " + addr + " error", e);
            }
        }

        logger.info("同步地址余额结束");
    }

}
