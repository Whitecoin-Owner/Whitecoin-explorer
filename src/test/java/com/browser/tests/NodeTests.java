package com.browser.tests;

import com.alibaba.fastjson.JSON;
import com.browser.service.impl.RequestWalletService;
import com.browser.wallet.beans.ContractTxReceipt;
import com.browser.wallet.beans.TxReceiptContractBalanceChange;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NodeTests {
    private Logger log = LoggerFactory.getLogger(NodeTests.class);

    @Resource
    private RequestWalletService requestWalletService;

    @Test
    public void testGetTxReceipt() {
        String txid = "89116a93c7107d81cb0b9874a755693e91998c88";
        List<ContractTxReceipt> contractTxReceipts = requestWalletService.getContractTxReceipt(txid);
        log.info("tx receipt: {}", JSON.toJSONString(contractTxReceipts));
        if(contractTxReceipts.isEmpty()) {
            return;
        }
        List<TxReceiptContractBalanceChange> contractWithdraws = TxReceiptContractBalanceChange.fromTxReceiptChangeJson(contractTxReceipts.get(0).getContract_withdraw());
        log.info("contractWithdraws: {}", JSON.toJSONString(contractWithdraws));
    }
}
