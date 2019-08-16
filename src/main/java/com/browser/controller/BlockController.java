package com.browser.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.browser.dao.entity.BlBlock;
import com.browser.dao.entity.BlTransaction;
import com.browser.dao.entity.ResultMsg;
import com.browser.protocol.EUDataGridResult;
import com.browser.service.BlockService;
import com.browser.service.TransactionService;
import com.browser.tools.controller.BaseController;

@Controller
public class BlockController extends BaseController {

	@Autowired
	private BlockService blockService;

	@Autowired
	private TransactionService transactionService;
	
	private static Logger logger = LoggerFactory.getLogger(BlockController.class);

	@ResponseBody
	@RequestMapping(value = "queryBlockList", method = { RequestMethod.POST })
	public ResultMsg queryContractList(@RequestBody BlBlock blBlock) {
		ResultMsg resultMsg = new ResultMsg();
		try {
			EUDataGridResult data = blockService.getBlockInfoList(blBlock);
			resultMsg.setRetCode(ResultMsg.HTTP_OK);
			resultMsg.setData(data);
		} catch (Exception e) {
			logger.error("系统错误", e);
			resultMsg.setRetCode(ResultMsg.HTTP_CHECK_VALID);
			resultMsg.setRetMsg(e.getMessage());
		}
		return resultMsg;
	}
	
	@ResponseBody
	@RequestMapping(value = "queryBlockByNum", method = { RequestMethod.POST })
	public ResultMsg queryBlockByNum(@RequestBody BlBlock blBlock) {
		ResultMsg resultMsg = new ResultMsg();
		if(null==blBlock.getBlockNum()) {
			resultMsg.setRetCode(ResultMsg.HTTP_CHECK_VALID);
			resultMsg.setRetMsg("参数不能为空");
			return resultMsg;
		}
		try {
			BlBlock data = blockService.selectByBlockNum(blBlock.getBlockNum());
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
	@RequestMapping(value = "queryBlockTxNum", method = { RequestMethod.POST })
	public ResultMsg queryBlockTxNum(@RequestBody BlTransaction transaction) {
		ResultMsg resultMsg = new ResultMsg();
		if(null==transaction.getBlockNum()) {
			resultMsg.setRetCode(ResultMsg.HTTP_CHECK_VALID);
			resultMsg.setRetMsg("参数不能为空");
			return resultMsg;
		}
		try {
			EUDataGridResult data = transactionService.queryBlockTxNum(transaction);
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
