package com.browser.controller;

import com.browser.dao.entity.TransOpTypeRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.browser.dao.entity.BlTransaction;
import com.browser.dao.entity.ResultMsg;
import com.browser.protocol.EUDataGridResult;
import com.browser.service.TransactionService;
import com.browser.tools.controller.BaseController;

@Controller
public class TransactionController extends BaseController {

	@Autowired
	private TransactionService transactionService;
	
	private static Logger logger = LoggerFactory.getLogger(TransactionController.class);

	@ResponseBody
	@RequestMapping(value = "queryTransactionList", method = { RequestMethod.POST })
	public ResultMsg getTransactionList(@RequestBody BlTransaction transaction) {
		ResultMsg resultMsg = new ResultMsg();
		try {
			EUDataGridResult data = transactionService.getTransactionList(transaction);
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
	@RequestMapping(value = "queryTransactionDetail", method = { RequestMethod.POST })
	public ResultMsg queryTransactionDetail(@RequestBody BlTransaction transaction) {
		ResultMsg resultMsg = new ResultMsg();
		if(null==transaction.getTrxId()||null==transaction.getOpType()) {
			resultMsg.setRetCode(ResultMsg.HTTP_CHECK_VALID);
			resultMsg.setRetMsg("参数不能为空");
			return resultMsg;
		}
		try {
			TransOpTypeRes data = transactionService.getOperationDetail(transaction);
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
	@RequestMapping(value = "queryTrxByAddr", method = { RequestMethod.POST })
	public ResultMsg queryTrxByAddr(@RequestBody BlTransaction transaction) {
		ResultMsg resultMsg = new ResultMsg();
		if(null==transaction.getAddress()) {
			resultMsg.setRetCode(ResultMsg.HTTP_CHECK_VALID);
			resultMsg.setRetMsg("参数不能为空");
			return resultMsg;
		}
		try {
			EUDataGridResult data = transactionService.selectMinerTrxList(transaction);
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
