package com.browser.service.impl;

import com.browser.dao.entity.BlSwapContract;
import com.browser.dao.mapper.BlSwapContractMapper;
import com.browser.service.SwapContractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class SwapContractServiceImpl implements SwapContractService {
    @Resource
    private BlSwapContractMapper blSwapContractMapper;

    @Override
    public List<BlSwapContract> selectAllActive() {
        return blSwapContractMapper.selectAll();
    }

    @Override
    public BlSwapContract selectByContractAddress(String contractAddress) {
        return blSwapContractMapper.selectByContractAddress(contractAddress);
    }

    @Override
    public int insert(BlSwapContract swapContract) {
        return blSwapContractMapper.insert(swapContract);
    }
}
