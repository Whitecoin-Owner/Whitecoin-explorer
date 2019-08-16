package com.browser.controller;

import com.browser.dao.entity.BlTransaction;
import com.browser.dao.entity.Proposal;
import com.browser.dao.entity.ResultMsg;
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

/**
 * @Auther: admin
 * @Date: 2018/12/20 17:38
 * @Description:
 */
@Controller
public class ProposalController {

    @Autowired
    private ProposalService proposalService;

    @Autowired
    private SenatorService senatorService;

    private static Logger logger = LoggerFactory.getLogger(ProposalController.class);

    @ResponseBody
    @RequestMapping(value = "curProposalList", method = { RequestMethod.POST })
    public ResultMsg curProposalList(@RequestBody Proposal Proposal) {
        ResultMsg resultMsg = new ResultMsg();
        try {
            EUDataGridResult data = proposalService.selecCurtList(Proposal);
            resultMsg.setRetCode(ResultMsg.HTTP_OK);
            resultMsg.setData(data);
        } catch (Exception e) {
            logger.error("查询当前提案异常", e);
            resultMsg.setRetCode(ResultMsg.HTTP_ERROR);
            resultMsg.setRetMsg(e.getMessage());
        }
        return resultMsg;
    }

    @ResponseBody
    @RequestMapping(value = "preProposalList", method = { RequestMethod.POST })
    public ResultMsg preProposalList(@RequestBody Proposal Proposal) {
        ResultMsg resultMsg = new ResultMsg();
        try {
            EUDataGridResult data = proposalService.selecPretList(Proposal);
            resultMsg.setRetCode(ResultMsg.HTTP_OK);
            resultMsg.setData(data);
        } catch (Exception e) {
            logger.error("查询历届提案异常", e);
            resultMsg.setRetCode(ResultMsg.HTTP_ERROR);
            resultMsg.setRetMsg(e.getMessage());
        }
        return resultMsg;
    }

    @ResponseBody
    @RequestMapping(value = "proposalDetail", method = { RequestMethod.POST })
    public ResultMsg proposalDetail(@RequestBody Proposal Proposal) {
        ResultMsg resultMsg = new ResultMsg();
        try {
            Proposal data = proposalService.proposalDetail(Proposal);
            resultMsg.setRetCode(ResultMsg.HTTP_OK);
            resultMsg.setData(data);
        } catch (Exception e) {
            logger.error("查询提案详情异常", e);
            resultMsg.setRetCode(ResultMsg.HTTP_ERROR);
            resultMsg.setRetMsg(e.getMessage());
        }
        return resultMsg;
    }

}
