package com.browser.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.browser.dao.entity.BlBlock;
import com.browser.dao.mapper.BlBlockMapper;
import com.browser.protocol.EUDataGridResult;
import com.browser.service.BlockService;
import com.browser.tools.Constant;
import com.browser.tools.common.StringUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class BlockServiceImpl implements BlockService {

	private static Logger logger = LoggerFactory.getLogger(BlockServiceImpl.class);

	@Autowired
	private BlBlockMapper blBlockMapper;

	@Override
	public BlBlock selectByBlockNum(Long blocknUm) {
		BlBlock block = blBlockMapper.selectByBlockNum(blocknUm);
		if (block != null) {
			if (null!=block.getReward()) {
				block.setRewards(block.getReward().divide(new BigDecimal(Constant.PRECISION))
						.stripTrailingZeros().toPlainString() +" " + Constant.SYMBOL);
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
		PageHelper.startPage(blBlock.getPage(), blBlock.getRows());
		List<BlBlock> list = blBlockMapper.getBlockInfoList(blBlock);
		if (list != null && list.size() > 0) {
			for (BlBlock block : list) {
				if (null!=block.getReward()) {
					block.setRewards(block.getReward().divide(new BigDecimal(Constant.PRECISION))
							.stripTrailingZeros().toPlainString() +" " +Constant.SYMBOL);
				}
			}
		}
		// 创建一个返回值对象
		EUDataGridResult result = new EUDataGridResult();
		result.setRows(list);
		// 取记录总条数
		PageInfo<BlBlock> pageInfo = new PageInfo<>(list);
		result.setTotal(pageInfo.getTotal());
		result.setPages(pageInfo.getPages());
		return result;
	}

}
