package com.browser.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.browser.tools.TimeTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.browser.dao.entity.BlBlock;
import com.browser.dao.mapper.BlBlockMapper;
import com.browser.protocol.EUDataGridResult;
import com.browser.service.BlockService;
import com.browser.tools.Constant;
import com.browser.tools.common.StringUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.util.CollectionUtils;

@Service
public class BlockServiceImpl implements BlockService {

    @Value("${utcTimeZone}")
    private String TIME_ZONE;

    @Value("${utcTimeInterval}")
    private int TIME_INTERVAL;

    @Autowired
    private BlBlockMapper blBlockMapper;

    @Override
    public BlBlock selectByBlockNum(Long blocknUm) {
        BlBlock block = blBlockMapper.selectByBlockNum(blocknUm);
        if (null != block) {
            block.setTimeZone(TIME_ZONE);
            // 时间统一使用UTC，减去8小时
            Date blockTime = block.getBlockTime();
            if (null != blockTime) {
                String intervalTime = TimeTool.getIntervalTimeStr(blockTime, new Date());
                block.setIntervalTime(intervalTime);
                blockTime = new Date(blockTime.getTime() - TIME_INTERVAL * 60 * 60 * 1000L);
                block.setBlockTime(blockTime);
            }
            Date createTime = block.getCreatedTime();
            if (null != createTime) {
                createTime = new Date(createTime.getTime() - TIME_INTERVAL * 60 * 60 * 1000L);
                block.setCreatedTime(createTime);
            }
            if (null != block.getReward()) {
                block.setRewards(block.getReward().divide(new BigDecimal(Constant.PRECISION))
                        .stripTrailingZeros().toPlainString() + " " + Constant.SYMBOL);
            }
        }
        return block;
    }

    @Override
    public void insertBatchBlock(List<BlBlock> list) {
        blBlockMapper.insertBatch(list);
    }

    @Override
    public void insertSelective(BlBlock blBlock) {
        blBlockMapper.insertSelective(blBlock);
    }

    @Override
    public Long queryBlockNum() {
        return blBlockMapper.queryBlockNum();
    }

    @Override
    public EUDataGridResult getBlockInfoList(BlBlock blBlock) {
        // 分页处理
        // PageHelper.startPage(blBlock.getPage(), blBlock.getRows());
        // List<BlBlock> list = blBlockMapper.getBlockInfoList(blBlock); // 不能内存分页做
        Integer limit = blBlock.getRows();
        if (limit == null || limit <= 0) {
            limit = 10;
        }
        Integer offset = 0;
        if (blBlock.getPage() != null && blBlock.getPage() > 0) {
            offset = (blBlock.getPage() - 1) * limit;
        }
        // 根据最新区块高度，offset, limit，推算出查询的block_num范围
        Long latestBlockNumInDb = blBlockMapper.queryBlockNum();
        if (latestBlockNumInDb == null) {
            latestBlockNumInDb = 0L;
        }
        long endBlockNum = latestBlockNumInDb - offset + 1;
        if (endBlockNum < 1L) {
            endBlockNum = 1L;
        }
        long startBlockNum = latestBlockNumInDb - offset - limit + 1;
        if (startBlockNum <= 0) {
            startBlockNum = 1L;
        }
        List<BlBlock> list = blBlockMapper.getBlockInfoListByRange(startBlockNum, endBlockNum);
        if (!CollectionUtils.isEmpty(list)) {
            Date blockTime;
            Date createTime;
            String intervalTime;
            for (BlBlock block : list) {
                block.setTimeZone(TIME_ZONE);
                // 时间统一使用UTC，减去8小时
                blockTime = block.getBlockTime();
                if (null != blockTime) {
                    intervalTime = TimeTool.getIntervalTimeStr(blockTime, new Date());
                    block.setIntervalTime(intervalTime);
                    blockTime = new Date(blockTime.getTime() - TIME_INTERVAL * 60 * 60 * 1000L);
                    block.setBlockTime(blockTime);
                }
                createTime = block.getCreatedTime();
                if (null != createTime) {
                    createTime = new Date(createTime.getTime() - TIME_INTERVAL * 60 * 60 * 1000L);
                    block.setCreatedTime(createTime);
                }
                if (null != block.getReward()) {
                    block.setRewards(block.getReward().divide(new BigDecimal(Constant.PRECISION))
                            .stripTrailingZeros().toPlainString() + " " + Constant.SYMBOL);
                }
            }
        }
        // 创建一个返回值对象
        EUDataGridResult result = new EUDataGridResult();
        result.setRows(list);
        // 取记录总条数
        PageInfo<BlBlock> pageInfo = new PageInfo<>(list);
        result.setTotal(latestBlockNumInDb);
        result.setPages(pageInfo.getPages());
        return result;
    }

}
