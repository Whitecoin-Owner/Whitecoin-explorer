package com.browser.task;

import com.browser.dao.entity.BlToken;
import com.browser.service.TokenBalanceService;
import com.browser.service.TokenService;
import com.browser.service.impl.RequestWalletService;
import com.browser.wallet.PrecisionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;

/**
 * 因为有的token的supply一直变化，所以需要增加定时任务去更新
 */
@Slf4j
@Component
public class TokenSupplySyncTask {
    @Resource
    private TokenService tokenService;
    @Resource
    private RequestWalletService requestWalletService;
    @Resource
    private TokenBalanceService tokenBalanceService;

    @Value("${wallet.caller}")
    private String walletRpcCaller;

    @Value("${scheduled.task}")
    private boolean task;

    @Scheduled(cron = "0 0/10 * * * ? ")
    public void updateTokenSupplyTask() {
        if (task) {
            log.info("不开启定时任务");
            return;
        }
        log.info("updateTokenSupplyTask");
        try {
            doUpdateTokenSupplyTask();
            log.info("updateTokenSupplyTask done");
        } catch (Exception e) {
            log.error("updateTokenSupplyTask err", e);
        }
    }

    public void doUpdateTokenSupplyTask() {
        List<BlToken> tokens = tokenService.selectAllActive();
        for (BlToken token : tokens) {
            try {
                BigInteger totalSupplyFull = requestWalletService.getTokenTotalSupply(walletRpcCaller, token.getContractAddress());
                BigDecimal totalSupply = new BigDecimal(totalSupplyFull).setScale(token.getPrecision(), RoundingMode.FLOOR)
                        .divide(PrecisionUtils.decimalsToPrecision(token.getPrecision()), RoundingMode.FLOOR);
                if (token.getTokenSymbol().equals("TKSP")) {
                    // TKSP's total supply not updated in contract, so we need to calc it from token_balance records
                    totalSupply = tokenBalanceService.getSumBalanceByTokenContract(token.getContractAddress());
                    if (totalSupply.compareTo(new BigDecimal("100000")) > 0) {
                        totalSupply = new BigDecimal("100000");
                    }
                }
                if (!totalSupply.equals(token.getTokenSupply())) {
                    token.setTokenSupply(totalSupply);
                    tokenService.updateTokenTotalSupplyByContractAddress(token.getContractAddress(), totalSupply);
                }
            } catch (Exception e) {
                log.error("update token {} total supply error", token.getContractAddress(), e);
            }
        }
    }
}
