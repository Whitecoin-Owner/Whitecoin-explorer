package com.browser.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.browser.config.RealData;
import com.browser.dao.entity.*;
import com.browser.service.impl.AddressBalanceServiceImpl;
import com.browser.service.impl.RedisService;
import com.browser.task.vo.PriceInfo;
import com.browser.tools.common.DateUtil;
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
		try {
			PriceInfo mainCoinUsdtPriceInfo = redisService.getMainCoinUsdtPrice();
			PriceInfo mainCoinBtcPriceInfo = redisService.getMainCoinBtcPrice();
			JSONObject coinPriceInfo = new JSONObject();
			coinPriceInfo.put("in_usdt", mainCoinUsdtPriceInfo);
			coinPriceInfo.put("in_btc", mainCoinBtcPriceInfo);
			resultMsg.setRetCode(ResultMsg.HTTP_OK);
			resultMsg.setData(coinPriceInfo);
		} catch (Exception e) {
			logger.error("系统错误", e);
			resultMsg.setRetCode(ResultMsg.HTTP_ERROR);
			resultMsg.setRetMsg(e.getMessage());
		}
		return resultMsg;
	}

	/**
	 * 获取最新统计信息
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getStatis", method = { RequestMethod.POST })
	public ResultMsg getStatis() {
		ResultMsg resultMsg = new ResultMsg();
		try {
			BlStatis data = realData.getBlStatis();
			resultMsg.setRetCode(ResultMsg.HTTP_OK);
			resultMsg.setData(data);
		} catch (Exception e) {
			logger.error("系统错误", e);
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
            logger.error("系统错误", e);
            resultMsg.setRetCode(ResultMsg.HTTP_ERROR);
            resultMsg.setRetMsg(e.getMessage());
        }
        return resultMsg;
    }

	/**
	 * 获取最新区块信息
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "blocksInfo", method = { RequestMethod.POST })
	public ResultMsg blocksInfo() {
		ResultMsg resultMsg = new ResultMsg();
		try {
			List<BlBlock> data = statisService.newBlockStatic();
			resultMsg.setRetCode(ResultMsg.HTTP_OK);
			resultMsg.setData(data);
		} catch (Exception e) {
			logger.error("系统错误", e);
			resultMsg.setRetCode(ResultMsg.HTTP_ERROR);
			resultMsg.setRetMsg(e.getMessage());
		}
		return resultMsg;
	}

	/**
	 * 获取最新交易信息
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getTrxance", method = { RequestMethod.POST })
	public ResultMsg getTrxance(HttpServletRequest request) {
		ResultMsg resultMsg = new ResultMsg();
		try {
			List<BlTransaction> data = statisService.newTransactionStatic();
			resultMsg.setRetCode(ResultMsg.HTTP_OK);
			resultMsg.setData(data);
		} catch (Exception e) {
			logger.error("系统错误", e);
			resultMsg.setRetCode(ResultMsg.HTTP_ERROR);
			resultMsg.setRetMsg(e.getMessage());
		}
		return resultMsg;
	}

	/**
	 * 获取每天图表最新交易量
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getDayTrxNum", method = { RequestMethod.POST })
	public ResultMsg getTrxNum(@RequestBody BlTransaction transaction) {
		ResultMsg resultMsg = new ResultMsg();
		if (StringUtil.isEmpty(transaction.getEndTime()) || StringUtil.isEmpty(transaction.getStartTime())) {
			resultMsg.setRetCode(ResultMsg.HTTP_CHECK_VALID);
			resultMsg.setRetMsg("查询参数不允许为空！");
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
			logger.error("系统错误", e);
			resultMsg.setRetCode(ResultMsg.HTTP_ERROR);
			resultMsg.setRetMsg(e.getMessage());
		}
		return resultMsg;
	}
	
	/**
	 * 获取每小时图表最新交易量
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getHourTrxNum", method = { RequestMethod.POST })
	public ResultMsg getHourTrxNum(@RequestBody BlTransaction transaction) {
		ResultMsg resultMsg = new ResultMsg();
		if (StringUtil.isEmpty(transaction.getEndTime()) || StringUtil.isEmpty(transaction.getStartTime())) {
			resultMsg.setRetCode(ResultMsg.HTTP_CHECK_VALID);
			resultMsg.setRetMsg("查询参数不允许为空！");
			return resultMsg;
		}
		try {
			JSONArray data = realData.getToday();
			resultMsg.setRetCode(ResultMsg.HTTP_OK);
			resultMsg.setData(data);
		} catch (Exception e) {
			logger.error("系统错误", e);
			resultMsg.setRetCode(ResultMsg.HTTP_ERROR);
			resultMsg.setRetMsg(e.getMessage());
		}
		return resultMsg;
	}
}
