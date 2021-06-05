package com.browser.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.browser.service.impl.RedisService;
import com.browser.task.vo.PriceInfo;
import com.browser.tools.common.HttpUtil;
import kong.unirest.Unirest;
import kong.unirest.UnirestInstance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
public class PriceFetchTask {
    @Autowired
    private RedisService redisService;

    private PriceInfo fetchXwcPrice(String quoteAsset, String baseAsset) {
        String url = String.format("https://kline.xt.com/api/data/v1/ticker?marketName=%s_%s", quoteAsset, baseAsset);
        UnirestInstance unirestInstance = Unirest.spawnInstance();
        try {
            String resStr = unirestInstance.get(url).asString().getBody();
            JSONObject resJson = JSON.parseObject(resStr);
            log.info("xwc price response: {}", resStr);
            JSONArray datasJson = resJson.getJSONArray("datas");
            if (datasJson.size() < 2) {
                return null;
            }
            BigDecimal current = datasJson.getBigDecimal(1);
            BigDecimal high = datasJson.getBigDecimal(2);
            BigDecimal low = datasJson.getBigDecimal(3);
            BigDecimal volume = datasJson.getBigDecimal(4);
            BigDecimal change = datasJson.getBigDecimal(5);
            PriceInfo priceInfo = new PriceInfo();
            priceInfo.setPrice(current);
            priceInfo.setHigh(high);
            priceInfo.setLow(low);
            priceInfo.setVolume(volume);
            priceInfo.setChange(change);
            return priceInfo;
        } finally {
            unirestInstance.close();
        }
    }

//    @Scheduled(cron = "0 * * * * ? ")
//    public void fetchCoinPrice() {
//        log.info("start fetchCoinPrice");
//        try {
//            PriceInfo xwcUsdtPriceInfo = fetchXwcPrice("XWC", "USDT");
//            if(xwcUsdtPriceInfo != null) {
//                redisService.putMainCoinUsdtPrice(xwcUsdtPriceInfo);
//            }
//            PriceInfo xwcBtcPriceInfo = fetchXwcPrice("XWC", "BTC");
//            if(xwcBtcPriceInfo != null) {
//                redisService.putMainCoinBtcPrice(xwcBtcPriceInfo);
//            }
//        } catch (Exception e) {
//            log.error("fetchCoinPrice error", e);
//        } finally {
//            log.info("end fetchCoinPrice");
//        }
//    }
}
