package com.browser.service;

import com.browser.dao.entity.BlSwapContract;
import com.browser.dao.entity.BlSwapTransaction;

import java.util.List;

public interface SwapContractService {
    List<BlSwapContract> selectAllActive();
    BlSwapContract selectByContractAddress(String contractAddress);
    int insert(BlSwapContract swapContract);
}
