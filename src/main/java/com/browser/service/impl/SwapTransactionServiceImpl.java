package com.browser.service.impl;

import com.browser.dao.entity.BlSwapTransaction;
import com.browser.dao.entity.BlTokenTransaction;
import com.browser.dao.mapper.BlSwapTransactionMapper;
import com.browser.protocol.EUDataGridResult;
import com.browser.service.SwapTransactionService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class SwapTransactionServiceImpl implements SwapTransactionService {
    @Resource
    private BlSwapTransactionMapper blSwapTransactionMapper;

    @Override
    public BlSwapTransaction selectByTrxIdAndOpNumAndEventSeq(String trxId, int opNum, int eventSeq) {
        BlSwapTransaction cond = new BlSwapTransaction();
        cond.setTrxId(trxId);
        cond.setOpNum(opNum);
        cond.setEventSeq(eventSeq);
        List<BlSwapTransaction> records = blSwapTransactionMapper.selectAllByCond(cond);
        if(records.isEmpty()) {
            return null;
        }
        return records.get(0);
    }

    @Override
    public int insert(BlSwapTransaction record) {
        return blSwapTransactionMapper.insert(record);
    }

    @Override
    public List<BlSwapTransaction> selectAllByTrxId(String trxId) {
        BlSwapTransaction cond = new BlSwapTransaction();
        cond.setTrxId(trxId);
        return blSwapTransactionMapper.selectAllByCond(cond);
    }

    @Override
    public EUDataGridResult selectListByUserAddress(BlSwapTransaction transaction) {
        // 分页处理
        PageHelper.startPage(transaction.getPage(), transaction.getRows());
        List<BlSwapTransaction> list = blSwapTransactionMapper.selectListByUserAddress(transaction);
        // 创建一个返回值对象
        EUDataGridResult result = new EUDataGridResult();
        result.setRows(list);
        // 取记录总条数
        PageInfo<BlSwapTransaction> pageInfo = new PageInfo<>(list);
        result.setTotal(pageInfo.getTotal());
        result.setPages(pageInfo.getPages());
        return result;
    }
}
