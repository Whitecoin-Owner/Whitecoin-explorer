package com.browser.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.browser.config.RealData;
import com.browser.dao.entity.*;
import com.browser.service.impl.AddressBalanceServiceImpl;
import com.browser.service.impl.RedisService;
import com.browser.task.vo.PriceInfo;
import com.browser.tools.common.DateUtil;
import com.google.gson.JsonObject;
import kong.unirest.Unirest;
import net.sf.json.util.JSONUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSONArray;
import com.browser.service.StatisService;
import com.browser.tools.common.StringUtil;
import com.browser.tools.controller.BaseController;

@Controller
public class IndexController extends BaseController {

	@Autowired
	private StatisService statisService;
	@Autowired
    private AddressBalanceServiceImpl addressBalanceService;
	@Autowired
	private RedisService redisService;

	@Autowired
	private RealData realData;

	private static Logger logger = LoggerFactory.getLogger(IndexController.class);

	@RequestMapping("")
	public String index() {
		return "index";
	}

	@ResponseBody
	@GetMapping("mainCoinPrice")
	public ResultMsg mainCoinPrice() {
		ResultMsg resultMsg = new ResultMsg();
		JSONObject coinPriceInfo = new JSONObject();


		try {

			String urlNameStringXwcUsdt = "https://api.xt.pub/data/api/v1/getTicker?market=xwc_usdt";
			String urlNameStringBtcUsdt = "https://api.xt.pub/data/api/v1/getTicker?market=btc_usdt";


			String usdtResult="";
			String btcResult="";

			// 获取当前客户端对象
			HttpClient httpClient = new DefaultHttpClient();
			PriceInfo mainCoinUsdtPriceInfo = null;
			try {
				// 根据地址获取请求
				HttpGet requestUsdt = new HttpGet(urlNameStringXwcUsdt);
				requestUsdt.setHeader("Content-Type", "application/x-www-form-urlencoded");
				// 通过请求对象获取响应对象
				HttpResponse response = httpClient.execute(requestUsdt);
				logger.info("调用聚合行情接口返回:{}", response);

				// 判断网络连接状态码是否正常(0--200都数正常)
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					usdtResult = EntityUtils.toString(response.getEntity(), "utf-8");
//					usdtResult = Unirest.get(urlNameStringUsdt).asString().getBody();
					logger.info("调用聚合行情接口返回str: {}", usdtResult);
					JSONObject jsStr = JSONObject.parseObject(usdtResult);
					BigDecimal price = new BigDecimal(jsStr.get("price").toString());
					BigDecimal rate = new BigDecimal(jsStr.get("rate").toString());
					BigDecimal low = new BigDecimal(jsStr.get("low").toString());
					BigDecimal high = new BigDecimal(jsStr.get("high").toString());
					mainCoinUsdtPriceInfo = new PriceInfo();
					mainCoinUsdtPriceInfo.setChange(rate);
					mainCoinUsdtPriceInfo.setPrice(price);
					mainCoinUsdtPriceInfo.setLow(low);
					mainCoinUsdtPriceInfo.setHigh(high);
					coinPriceInfo.put("in_usdt", mainCoinUsdtPriceInfo);
				}
			} catch (Exception e) {
				logger.error("fetch usdt price error", e);
			}

			if(mainCoinUsdtPriceInfo != null) {
				try {
					// 根据地址获取请求
					HttpGet requestBtc = new HttpGet(urlNameStringBtcUsdt);
					requestBtc.setHeader("Content-Type", "application/x-www-form-urlencoded");

					// 通过请求对象获取响应对象
					HttpResponse responseBtc = httpClient.execute(requestBtc);
					logger.info("调用聚合行情接口返回:{}", responseBtc);

					// 判断网络连接状态码是否正常(0--200都数正常)
					if (responseBtc.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						btcResult = EntityUtils.toString(responseBtc.getEntity(), "utf-8");
//					btcResult = Unirest.get(urlNameStringBtc).asString().getBody();
						logger.info("调用聚合行情接口返回str: {}", btcResult);
						JSONObject jsStr = JSONObject.parseObject(btcResult);
						BigDecimal price = new BigDecimal(jsStr.get("price").toString());
						BigDecimal rate = new BigDecimal(jsStr.get("rate").toString());
						BigDecimal low = new BigDecimal(jsStr.get("low").toString());
						BigDecimal high = new BigDecimal(jsStr.get("high").toString());
						if(price.compareTo(BigDecimal.ZERO) > 0) {
							PriceInfo mainCoinBtcPriceInfo = new PriceInfo();
							mainCoinBtcPriceInfo.setChange(BigDecimal.ZERO);
							mainCoinBtcPriceInfo.setPrice(mainCoinUsdtPriceInfo.getPrice().setScale(8, RoundingMode.FLOOR).divide(price, RoundingMode.FLOOR));
							mainCoinBtcPriceInfo.setLow(mainCoinUsdtPriceInfo.getLow().setScale(8, RoundingMode.FLOOR).divide(low, RoundingMode.FLOOR));
							mainCoinBtcPriceInfo.setHigh(mainCoinUsdtPriceInfo.getHigh().setScale(8, RoundingMode.FLOOR).divide(high, RoundingMode.FLOOR));
							coinPriceInfo.put("in_btc", mainCoinBtcPriceInfo);
						}
					}

				} catch (Exception e) {
					logger.error("get btc price error", e);
				}
			}

			resultMsg.setRetCode(ResultMsg.HTTP_OK);
			resultMsg.setData(coinPriceInfo);
		} catch (Exception e) {
			logger.error("system error", e);
			resultMsg.setRetCode(ResultMsg.HTTP_ERROR);
			resultMsg.setRetMsg(e.getMessage());
		}
		return resultMsg;
	}

	@ResponseBody
	@RequestMapping(value = "getStatis", method = { RequestMethod.POST })
	public ResultMsg getStatis() {
		ResultMsg resultMsg = new ResultMsg();
		try {
			BlStatis data = realData.getBlStatis();
			resultMsg.setRetCode(ResultMsg.HTTP_OK);
			resultMsg.setData(data);
		} catch (Exception e) {
			logger.error("system error", e);
			resultMsg.setRetCode(ResultMsg.HTTP_ERROR);
			resultMsg.setRetMsg(e.getMessage());
		}
		return resultMsg;
	}

	@ResponseBody
    @RequestMapping(value = "richlist", method = RequestMethod.GET)
    public ResultMsg richList() {
        ResultMsg resultMsg = new ResultMsg();
        try {
            List<BLAddressBalance> topBalances = addressBalanceService.selectTopTichList("1.3.0", 100);
            resultMsg.setRetCode(ResultMsg.HTTP_OK);
            resultMsg.setData(topBalances);
        } catch (Exception e) {
            logger.error("system error", e);
            resultMsg.setRetCode(ResultMsg.HTTP_ERROR);
            resultMsg.setRetMsg(e.getMessage());
        }
        return resultMsg;
    }

	@ResponseBody
	@RequestMapping(value = "blocksInfo", method = { RequestMethod.POST })
	public ResultMsg blocksInfo() {
		ResultMsg resultMsg = new ResultMsg();
		try {
			List<BlBlock> data = statisService.newBlockStatic();
			resultMsg.setRetCode(ResultMsg.HTTP_OK);
			resultMsg.setData(data);
		} catch (Exception e) {
			logger.error("system error", e);
			resultMsg.setRetCode(ResultMsg.HTTP_ERROR);
			resultMsg.setRetMsg(e.getMessage());
		}
		return resultMsg;
	}

	@ResponseBody
	@RequestMapping(value = "getTrxance", method = { RequestMethod.POST })
	public ResultMsg getTrxance(HttpServletRequest request) {
		ResultMsg resultMsg = new ResultMsg();
		try {
			List<BlTransaction> data = statisService.newTransactionStatic();
			resultMsg.setRetCode(ResultMsg.HTTP_OK);
			resultMsg.setData(data);
		} catch (Exception e) {
			logger.error("system error", e);
			resultMsg.setRetCode(ResultMsg.HTTP_ERROR);
			resultMsg.setRetMsg(e.getMessage());
		}
		return resultMsg;
	}

	@ResponseBody
	@RequestMapping(value = "getDayTrxNum", method = { RequestMethod.POST })
	public ResultMsg getTrxNum(@RequestBody BlTransaction transaction) {
		ResultMsg resultMsg = new ResultMsg();
		if (StringUtil.isEmpty(transaction.getEndTime()) || StringUtil.isEmpty(transaction.getStartTime())) {
			resultMsg.setRetCode(ResultMsg.HTTP_CHECK_VALID);
			resultMsg.setRetMsg("param can't be empty");
			return resultMsg;
		}
		try {
			long day = DateUtil.getDaySub(transaction.getStartTime(),transaction.getEndTime());
			JSONArray data =new JSONArray();
			if(day>15){
				data = realData.getMonthDay();
			}else{
				data = realData.getWeekDay();
			}
			resultMsg.setRetCode(ResultMsg.HTTP_OK);
			resultMsg.setData(data);
		} catch (Exception e) {
			logger.error("system error", e);
			resultMsg.setRetCode(ResultMsg.HTTP_ERROR);
			resultMsg.setRetMsg(e.getMessage());
		}
		return resultMsg;
	}

	@ResponseBody
	@RequestMapping(value = "getHourTrxNum", method = { RequestMethod.POST })
	public ResultMsg getHourTrxNum(@RequestBody BlTransaction transaction) {
		ResultMsg resultMsg = new ResultMsg();
		if (StringUtil.isEmpty(transaction.getEndTime()) || StringUtil.isEmpty(transaction.getStartTime())) {
			resultMsg.setRetCode(ResultMsg.HTTP_CHECK_VALID);
			resultMsg.setRetMsg("param can't be empty");
			return resultMsg;
		}
		try {
			JSONArray data = realData.getToday();
			resultMsg.setRetCode(ResultMsg.HTTP_OK);
			resultMsg.setData(data);
		} catch (Exception e) {
			logger.error("system error", e);
			resultMsg.setRetCode(ResultMsg.HTTP_ERROR);
			resultMsg.setRetMsg(e.getMessage());
		}
		return resultMsg;
	}


}
