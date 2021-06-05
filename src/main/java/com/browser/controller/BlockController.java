package com.browser.controller;

import com.browser.dao.entity.BlBlock;
import com.browser.dao.entity.BlTransaction;
import com.browser.dao.entity.ResultMsg;
import com.browser.protocol.EUDataGridResult;
import com.browser.service.BlockService;
import com.browser.service.ScanInfoService;
import com.browser.service.TransactionService;
import com.browser.task.SyncTaskSingle;
import com.browser.tools.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class BlockController extends BaseController {

    @Autowired
    private BlockService blockService;

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private ScanInfoService scanInfoService;

    private static Logger logger = LoggerFactory.getLogger(BlockController.class);

    /**
     * 单独从某个高度开始重扫一段区块（并不会彻底从这里重扫，只是重扫这段区块一次（重扫的区块数量不确定）
     *
     * @param blockNum
     * @return
     */
    @ResponseBody
    @GetMapping("/set_scan_from_block_once/{blockNum}")
    public String setScanFromBlockOnce(@PathVariable("blockNum") Long blockNum) {
        if (blockNum == null || blockNum <= 0) {
            return "error blockNum " + blockNum;
        }
        SyncTaskSingle.tmpScanFromBlockNum.set(blockNum);
        return "ok";
    }

    @ResponseBody
    @GetMapping("/set_scan_from_block/{blockNum}")
    public String setScanFromBlock(@PathVariable("blockNum") Long blockNum) {
        if (blockNum == null || blockNum <= 0) {
            return "error blockNum " + blockNum;
        }
        scanInfoService.updateOrInsertBlockNum(blockNum);
        return "ok";
    }

    @ResponseBody
    @RequestMapping(value = "queryBlockList", method = {RequestMethod.POST})
    public ResultMsg queryContractList(@RequestBody BlBlock blBlock) {
        ResultMsg resultMsg = new ResultMsg();
        try {
            EUDataGridResult data = blockService.getBlockInfoList(blBlock);
            resultMsg.setRetCode(ResultMsg.HTTP_OK);
            resultMsg.setData(data);
        } catch (Exception e) {
            logger.error("system error", e);
            resultMsg.setRetCode(ResultMsg.HTTP_CHECK_VALID);
            resultMsg.setRetMsg(e.getMessage());
        }
        return resultMsg;
    }

    @ResponseBody
    @RequestMapping(value = "queryBlockByNum", method = {RequestMethod.POST})
    public ResultMsg queryBlockByNum(@RequestBody BlBlock blBlock) {
        ResultMsg resultMsg = new ResultMsg();
        if (null == blBlock.getBlockNum()) {
            resultMsg.setRetCode(ResultMsg.HTTP_CHECK_VALID);
            resultMsg.setRetMsg("param can't be empty");
            return resultMsg;
        }
        try {
            BlBlock data = blockService.selectByBlockNum(blBlock.getBlockNum());
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
    @RequestMapping(value = "queryBlockTxNum", method = {RequestMethod.POST})
    public ResultMsg queryBlockTxNum(@RequestBody BlTransaction transaction) {
        ResultMsg resultMsg = new ResultMsg();
        if (null == transaction.getBlockNum()) {
            resultMsg.setRetCode(ResultMsg.HTTP_CHECK_VALID);
            resultMsg.setRetMsg("param can't be empty");
            return resultMsg;
        }
        try {
            EUDataGridResult data = transactionService.queryBlockTxNum(transaction);
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
