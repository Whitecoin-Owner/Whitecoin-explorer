package com.browser.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.browser.dao.entity.BlContractDetail;
import com.browser.dao.entity.BlContractInfo;
import com.browser.dao.entity.BlTransaction;
import com.browser.dao.entity.ResultMsg;
import com.browser.protocol.EUDataGridResult;
import com.browser.service.StatisService;
import com.browser.service.impl.ContractService;

/** 合约信息处理入口
 * Created by Administrator on 2017/12/8 0008.
 */
@Controller
public class ContractController {

    @Autowired
    private ContractService contractService;
    
    @Autowired
    private StatisService statisService;
    
    private static Logger logger = LoggerFactory.getLogger(ContractController.class);

    @ResponseBody
	@RequestMapping(value = "queryContractList", method = { RequestMethod.POST })
    public ResultMsg queryContractList(@RequestBody BlContractInfo contractInfo) {
    	ResultMsg resultMsg = new ResultMsg();
    	try {
			EUDataGridResult data = contractService.getContractList(contractInfo);
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
	@RequestMapping(value = "getContractTrxList", method = { RequestMethod.POST })
    public ResultMsg getContractTrxList(@RequestBody BlTransaction transaction) {
    	ResultMsg resultMsg = new ResultMsg();
    	if(null==transaction.getContractId()) {
			resultMsg.setRetCode(ResultMsg.HTTP_CHECK_VALID);
			resultMsg.setRetMsg("参数不能为空");
			return resultMsg;
		}
    	try {
			EUDataGridResult data = contractService.getContractTrxList(transaction);
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
    @RequestMapping(value = "getAbi", method = { RequestMethod.POST })
    public ResultMsg getAbi(@RequestBody BlContractInfo contractInfo) {
    	ResultMsg resultMsg = new ResultMsg();
    	if(null==contractInfo.getContractId()) {
			resultMsg.setRetCode(ResultMsg.HTTP_CHECK_VALID);
			resultMsg.setRetMsg("参数不能为空");
			return resultMsg;
		}
    	try {
			JSONObject data = contractService.getAbi(contractInfo);
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
    @RequestMapping(value = "getEvents", method = { RequestMethod.POST })
    public ResultMsg getEvents(@RequestBody BlContractInfo contractInfo) {
    	ResultMsg resultMsg = new ResultMsg();
    	if(null==contractInfo.getContractId()) {
			resultMsg.setRetCode(ResultMsg.HTTP_CHECK_VALID);
			resultMsg.setRetMsg("参数不能为空");
			return resultMsg;
		}
    	try {
			JSONObject data = contractService.getEvents(contractInfo);
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
    @RequestMapping(value = "getContractStatis", method = { RequestMethod.POST })
    public ResultMsg getContractTrxList(@RequestBody BlContractInfo contractInfo) {
    	ResultMsg resultMsg = new ResultMsg();
    	if(null==contractInfo.getContractId()) {
			resultMsg.setRetCode(ResultMsg.HTTP_CHECK_VALID);
			resultMsg.setRetMsg("参数不能为空");
			return resultMsg;
		}
    	try {
			BlContractDetail data = statisService.getContractsStatis(contractInfo);
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
