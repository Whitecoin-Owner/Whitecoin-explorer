package com.browser.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.browser.dao.entity.BlContractInfo;
import com.browser.dao.entity.BlMinerStatis;
import com.browser.dao.entity.ResultMsg;
import com.browser.protocol.EUDataGridResult;
import com.browser.service.StatisService;
import com.browser.service.impl.ContractService;
import com.browser.tools.controller.BaseController;

@Controller
public class MinerController extends BaseController {

	@Autowired
	private StatisService statisService;
	
	@Autowired
	private ContractService contractService;
	
	private static Logger logger = LoggerFactory.getLogger(MinerController.class);

	

	/**
	 * 统计矿工信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "minerInfo", method = { RequestMethod.POST })
	public ResultMsg minerInfo(@RequestBody BlMinerStatis blMinerStatis) {
		ResultMsg resultMsg = new ResultMsg();
		if(null==blMinerStatis.getName()) {
			resultMsg.setRetCode(ResultMsg.HTTP_CHECK_VALID);
			resultMsg.setRetMsg("参数不能为空");
			return resultMsg;
		}
		try {
			//			BlMinerStatis data = statisService.getMinerStatis(blMinerStatis);
			BlMinerStatis blMiner =new BlMinerStatis();
			blMiner.setAddress(blMinerStatis.getName());
			BlMinerStatis data = statisService.getAddrStatis(blMiner);
			resultMsg.setRetCode(ResultMsg.HTTP_OK);
			resultMsg.setData(data);
		} catch (Exception e) {
			logger.error("system error", e);
			resultMsg.setRetCode(ResultMsg.HTTP_ERROR);
			resultMsg.setRetMsg(e.getMessage());
		}
		return resultMsg;
	}
	
	/**
	 * address statistic data
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "addrStatis", method = { RequestMethod.POST })
	public ResultMsg addrStatis(@RequestBody BlMinerStatis blMinerStatis) {
		ResultMsg resultMsg = new ResultMsg();
		if(null==blMinerStatis.getAddress()) {
			resultMsg.setRetCode(ResultMsg.HTTP_CHECK_VALID);
			resultMsg.setRetMsg("param can't be empty");
			return resultMsg;
		}
		try {
			BlMinerStatis data = statisService.getAddrStatis(blMinerStatis);
			resultMsg.setRetCode(ResultMsg.HTTP_OK);
			resultMsg.setData(data);
		} catch (Exception e) {
			logger.error("system error", e);
			resultMsg.setRetCode(ResultMsg.HTTP_ERROR);
			resultMsg.setRetMsg(e.getMessage());
		}
		return resultMsg;
	}

	/**
	 * check existence of address/account name
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "addrjudge", method = { RequestMethod.POST })
	public ResultMsg addrjudge(@RequestBody BlMinerStatis blMinerStatis) {
		ResultMsg resultMsg = new ResultMsg();
		try {
			BlMinerStatis data = statisService.addrjudge(blMinerStatis);
			resultMsg.setRetCode(ResultMsg.HTTP_OK);
			resultMsg.setData(data);
		} catch (Exception e) {
			logger.error("system error", e);
			resultMsg.setRetCode(ResultMsg.HTTP_ERROR);
			resultMsg.setRetMsg(e.getMessage());
		}
		return resultMsg;
	}

	/**
	 * contracts statistic owned by address
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "addrContracts", method = { RequestMethod.POST })
	public ResultMsg minerContracts(@RequestBody BlContractInfo BlContractInfo) {
		ResultMsg resultMsg = new ResultMsg();
		if(null==BlContractInfo.getOwnerAddress()) {
			resultMsg.setRetCode(ResultMsg.HTTP_CHECK_VALID);
			resultMsg.setRetMsg("param can't be empty");
			return resultMsg;
		}
		try {
			EUDataGridResult data = contractService.getContractList(BlContractInfo);
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
