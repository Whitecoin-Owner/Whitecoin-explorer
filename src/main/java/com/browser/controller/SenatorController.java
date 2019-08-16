package com.browser.controller;

import com.browser.dao.entity.*;
import com.browser.protocol.EUDataGridResult;
import com.browser.service.StatisService;
import com.browser.service.impl.ContractService;
import com.browser.service.impl.ProposalService;
import com.browser.service.impl.SenatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Auther: admin
 * @Date: 2018/12/20 17:38
 * @Description:
 */
@Controller
public class SenatorController {

    @Autowired
    private ProposalService proposalService;

    @Autowired
    private SenatorService senatorService;

    private static Logger logger = LoggerFactory.getLogger(SenatorController.class);

    @ResponseBody
    @RequestMapping(value = "curSenatorList", method = { RequestMethod.POST })
    public ResultMsg getCurList(@RequestBody SenatorCurrent senatorCurrent) {
        ResultMsg resultMsg = new ResultMsg();
        try {
            EUDataGridResult data = senatorService.selectCurList(senatorCurrent);
            resultMsg.setRetCode(ResultMsg.HTTP_OK);
            resultMsg.setData(data);
        } catch (Exception e) {
            logger.error("查询现任senator异常", e);
            resultMsg.setRetCode(ResultMsg.HTTP_ERROR);
            resultMsg.setRetMsg(e.getMessage());
        }
        return resultMsg;
    }

    @ResponseBody
    @RequestMapping(value = "preSenatorList", method = { RequestMethod.POST })
    public ResultMsg getPreList(@RequestBody SenatorPrevious senatorPrevious) {
        ResultMsg resultMsg = new ResultMsg();
        try {
            EUDataGridResult data = senatorService.selectPreList(senatorPrevious);
            resultMsg.setRetCode(ResultMsg.HTTP_OK);
            resultMsg.setData(data);
        } catch (Exception e) {
            logger.error("查询历届senator异常", e);
            resultMsg.setRetCode(ResultMsg.HTTP_ERROR);
            resultMsg.setRetMsg(e.getMessage());
        }
        return resultMsg;
    }

    @ResponseBody
    @RequestMapping(value = "joinSenator", method = { RequestMethod.POST })
    public ResultMsg joinSenator(@RequestBody SenatorPrevious senatorPrevious) {
        ResultMsg resultMsg = new ResultMsg();
        try {
            List<ProposalContent> data = senatorService.selectJoinSenator(senatorPrevious);
            resultMsg.setRetCode(ResultMsg.HTTP_OK);
            resultMsg.setData(data);
        } catch (Exception e) {
            logger.error("查询当前参选senator异常", e);
            resultMsg.setRetCode(ResultMsg.HTTP_ERROR);
            resultMsg.setRetMsg(e.getMessage());
        }
        return resultMsg;
    }

    @ResponseBody
    @RequestMapping(value = "joinSenatorList", method = { RequestMethod.POST })
    public ResultMsg joinSenatorList(@RequestBody ProposalContent proposalContent) {
        ResultMsg resultMsg = new ResultMsg();
        try {
            EUDataGridResult data = senatorService.selectJoinList(proposalContent);
            resultMsg.setRetCode(ResultMsg.HTTP_OK);
            resultMsg.setData(data);
        } catch (Exception e) {
            logger.error("查询历届参选senator异常", e);
            resultMsg.setRetCode(ResultMsg.HTTP_ERROR);
            resultMsg.setRetMsg(e.getMessage());
        }
        return resultMsg;
    }

    @ResponseBody
    @RequestMapping(value = "joinSenatorDetail", method = { RequestMethod.POST })
    public ResultMsg joinSenatorDetail(@RequestBody ProposalContent proposalContent) {
        ResultMsg resultMsg = new ResultMsg();
        try {
            ProposalContent data = senatorService.selectJoinDetail(proposalContent);
            resultMsg.setRetCode(ResultMsg.HTTP_OK);
            resultMsg.setData(data);
        } catch (Exception e) {
            logger.error("查询参选senator详情异常", e);
            resultMsg.setRetCode(ResultMsg.HTTP_ERROR);
            resultMsg.setRetMsg(e.getMessage());
        }
        return resultMsg;
    }

    @ResponseBody
    @RequestMapping(value = "nonMatchedSenator", method = { RequestMethod.POST })
    public ResultMsg joinSenatorDetail(@RequestBody SenatorConfig senatorConfig) {
        ResultMsg resultMsg = new ResultMsg();
        try {
            SenatorConfig data = senatorService.selectNoMatchedJoinDetail(senatorConfig);
            resultMsg.setRetCode(ResultMsg.HTTP_OK);
            resultMsg.setData(data);
        } catch (Exception e) {
            logger.error("查询参选senator详情异常", e);
            resultMsg.setRetCode(ResultMsg.HTTP_ERROR);
            resultMsg.setRetMsg(e.getMessage());
        }
        return resultMsg;
    }

    @ResponseBody
    @RequestMapping(value = "preSenatorDetail", method = { RequestMethod.POST })
    public ResultMsg preSenatorDetail(@RequestBody SenatorPrevious senatorPrevious) {
        ResultMsg resultMsg = new ResultMsg();
        try {
            SenatorPrevious data = senatorService.selectPreSenatorDetail(senatorPrevious);
            resultMsg.setRetCode(ResultMsg.HTTP_OK);
            resultMsg.setData(data);
        } catch (Exception e) {
            logger.error("查询senator详情异常", e);
            resultMsg.setRetCode(ResultMsg.HTTP_ERROR);
            resultMsg.setRetMsg(e.getMessage());
        }
        return resultMsg;
    }

    @ResponseBody
    @RequestMapping(value = "curSenatorDetail", method = { RequestMethod.POST })
    public ResultMsg curSenatorDetail(@RequestBody SenatorCurrent senatorCurrent) {
        ResultMsg resultMsg = new ResultMsg();
        try {
            SenatorCurrent data = senatorService.selectCurSenatorDetail(senatorCurrent);
            resultMsg.setRetCode(ResultMsg.HTTP_OK);
            resultMsg.setData(data);
        } catch (Exception e) {
            logger.error("查询senator详情异常", e);
            resultMsg.setRetCode(ResultMsg.HTTP_ERROR);
            resultMsg.setRetMsg(e.getMessage());
        }
        return resultMsg;
    }
}
