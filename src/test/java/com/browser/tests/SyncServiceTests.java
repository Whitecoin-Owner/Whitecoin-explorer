package com.browser.tests;

import com.browser.service.impl.SyncService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SyncServiceTests {
    private Logger log = LoggerFactory.getLogger(SyncServiceTests.class);

    @Resource
    private SyncService syncService;

    @Test
    public void testSyncBlock() {
//        long blockNum = 4714030L;
//        long blockNum = 4716743L;
        long blockNum = 4710974L;
        syncService.blockSync(blockNum);
    }
}
