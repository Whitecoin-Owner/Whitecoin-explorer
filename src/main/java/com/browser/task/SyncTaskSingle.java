package com.browser.task;

import com.browser.service.BlockService;
import com.browser.service.ScanInfoService;
import com.browser.service.impl.RequestWalletService;
import com.browser.service.impl.SyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

/**
 * schedule task of sync blocks one by one
 */
@Component
public class SyncTaskSingle {

    private static Logger logger = LoggerFactory.getLogger(SyncTaskSingle.class);

    @Autowired
    private BlockService blockService;

    @Autowired
    private RequestWalletService requestWalletService;

    @Autowired
    private SyncService syncService;
    @Autowired
    private ScanInfoService scanInfoService;

    @Value("${safe.block}")
    private Integer safeBlock;

    @Value("${scheduled.task}")
    private boolean task;

    // scan from this block height once(only some blocks, not until latest)
    public static AtomicLong tmpScanFromBlockNum = new AtomicLong(0L);

    @Scheduled(cron = "0/3 * * * * ? ")
    public void syncData() {
        if (task) {
            logger.info("不开启定时任务");
            return;
        }
        logger.info("begin sync data");
        // 获取已经同步过来的最大的块号
        Long blockNumSynced = blockService.queryBlockNum();
        Long blockNum = scanInfoService.queryBlockNum();
        if (null == blockNum) {
            blockNum = 0L;
        }
        if (blockNumSynced != null && blockNum > blockNumSynced) {
            blockNum = blockNumSynced;
        }
        Long tmpScanFromBlock = tmpScanFromBlockNum.getAndSet(0L);
        if (tmpScanFromBlock > 0L) {
            blockNum = tmpScanFromBlock;
        }

        Long total = requestWalletService.getBlockCount();
        total = total - safeBlock;

        logger.info("总的块数：{}，已经同步了{}块", total, blockNum);

        if (total > blockNum) {
            for (Long i = blockNum; i < total; i++) {
                logger.info("sync block " + (i + 1));
                syncService.blockSync(i + 1);
                scanInfoService.updateOrInsertBlockNum(i + 1);
            }
        }

        logger.info("end data sync");
    }
}
