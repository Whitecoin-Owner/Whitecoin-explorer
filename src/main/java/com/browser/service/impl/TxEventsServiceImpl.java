package com.browser.service.impl;

import com.browser.bean.LiquidateBean;
import com.browser.bean.LiquidateResult;
import com.browser.dao.entity.BlTransaction;
import com.browser.dao.entity.BlTxEvents;
import com.browser.dao.entity.ResultMsg;
import com.browser.dao.mapper.BlTransactionMapper;
import com.browser.dao.mapper.BlTxEventsMapper;
import com.browser.service.TxEventsService;
import com.browser.tools.common.DateUtil;
import com.google.gson.Gson;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.browser.dao.entity.ResultMsg.HTTP_CHECK_VALID;

@Service
public class TxEventsServiceImpl implements TxEventsService {

    private static Logger logger = LoggerFactory.getLogger(TxEventsServiceImpl.class);
    @Resource
    private BlTxEventsMapper txEventsMapper;

    @Resource
    private BlTransactionMapper blTransactionMapper;

    @Override
    public int insert(BlTxEvents record) {
        return txEventsMapper.insert(record);
    }

    @Override
    public BlTxEvents selectByTrxIdAndEventSeq(String trxId, int eventSeq) {
        if (trxId == null) {
            return null;
        }
        BlTxEvents cond = new BlTxEvents();
        cond.setTrxId(trxId);
        cond.setEventSeq(eventSeq);
        List<BlTxEvents> events = txEventsMapper.selectAllByCond(cond);
        if (events.isEmpty()) {
            return null;
        } else {
            return events.get(0);
        }
    }

    @Override
    public List<BlTxEvents> selectAllByTrxId(String trxId) {
        if (trxId == null) {
            return Collections.emptyList();
        }
        BlTxEvents cond = new BlTxEvents();
        cond.setTrxId(trxId);
        return txEventsMapper.selectAllByCond(cond);
    }

    @Override
    public ResultMsg selectAllLiquidateEvent(Integer currentPage, Integer pageSize) {
        ResultMsg resultMsg = new ResultMsg();
        List<LiquidateBean> result = new ArrayList<>();
        BlTxEvents cond = new BlTxEvents();
        cond.setEventName("Liquidate");
        List<BlTxEvents> blTxEvents = new ArrayList<>();
        blTxEvents = txEventsMapper.selectAllByCond(cond);
        logger.info("query liquidate event nums:{}", blTxEvents.size());
        if (CollectionUtils.isEmpty(blTxEvents)) {
            logger.info("not found liquidate events");
            resultMsg.setRetCode(HTTP_CHECK_VALID);
            resultMsg.setRetMsg("没有查询到数据");
            return resultMsg;
        }
        int pageNo = 0;
        if (currentPage != null && pageSize != null) {
            pageNo = (blTxEvents.size() + pageSize - 1) / pageSize;
            blTxEvents = blTxEvents.stream().limit(currentPage * pageSize).skip((currentPage - 1) * pageSize).collect(Collectors.toList());
        }
        blTxEvents.forEach(blTxEvent -> {
            String eventArg = blTxEvent.getEventArg();
            LiquidateBean.EventArg eventArg1 = new Gson().fromJson(eventArg, LiquidateBean.EventArg.class);
            cal(eventArg1);
            LiquidateBean liquidateBean = new LiquidateBean();
            try {
                BeanUtils.copyProperties(liquidateBean, eventArg1);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            concurrentLiquidate(liquidateBean, blTxEvent, eventArg1);
            result.add(liquidateBean);
        });
        LiquidateResult liquidateResult = new LiquidateResult();
        liquidateResult.setRows(result);
        if (pageNo != 0) {
            liquidateResult.setTotalPages(pageNo);
            liquidateResult.setCurrentPage(currentPage);
        }
        resultMsg.setRetCode(HttpStatus.SC_OK);
        resultMsg.setData(liquidateResult);
        return resultMsg;
    }

    private void concurrentLiquidate(LiquidateBean liquidateBean, BlTxEvents blTxEvent, LiquidateBean.EventArg eventArg1) {
        liquidateBean.setBlockNum(blTxEvent.getBlockNum());
        liquidateBean.setEventSeq(blTxEvent.getEventSeq());
        liquidateBean.setCallerAddr(blTxEvent.getCallerAddr());
        liquidateBean.setId(blTxEvent.getId());
        liquidateBean.setContractAddress(blTxEvent.getContractAddress());
        liquidateBean.setEventName(blTxEvent.getEventName());
        liquidateBean.setOpNum(blTxEvent.getOpNum());
        liquidateBean.setTrxId(blTxEvent.getTrxId());
        liquidateBean.setSecSinceEpoch(DateUtil.convertTimeToString(Long.valueOf(eventArg1.getSecSinceEpoch())));
        //查询交易时间和交易结果状态
        BlTransaction blTransaction = new BlTransaction();
        blTransaction.setTrxId(blTxEvent.getTrxId());
        List<BlTransaction> transactionByTxId = blTransactionMapper.getTransactionByTxId(blTransaction);
        BlTransaction transactionResp = new BlTransaction();
        if (!CollectionUtils.isEmpty(transactionByTxId)) {
            transactionResp = transactionByTxId.get(0);
        }
        liquidateBean.setTradeResultStatus(transactionResp.getFail() ? "失败" : "成功");
        liquidateBean.setTradeTime(DateUtil.convertTimeToString(transactionResp.getTrxTime().getTime() / 1000));
        liquidateBean.setTradeResultType("Liquidate");
    }

    private void cal(LiquidateBean.EventArg eventArg1) {
        eventArg1.setReturnAmount(dividePow(eventArg1.getReturnAmount()));
        eventArg1.setStabilityFee(dividePow(eventArg1.getStabilityFee()));
        eventArg1.setCollateralAmount(dividePow(eventArg1.getCollateralAmount()));
        eventArg1.setStableTokenAmount(dividePow(eventArg1.getStableTokenAmount()));
        eventArg1.setRepayStableTokenAmount(dividePow(eventArg1.getRepayStableTokenAmount()));
        eventArg1.setAuctionCollateralAmount(dividePow(eventArg1.getAuctionCollateralAmount()));
        eventArg1.setPenaltyAmount(dividePow(eventArg1.getPenaltyAmount()));
    }

    private String dividePow(String originVal) {
        return new BigDecimal(originVal).divide(BigDecimal.valueOf(Math.pow(10, 8))).stripTrailingZeros().toPlainString();
    }
}
