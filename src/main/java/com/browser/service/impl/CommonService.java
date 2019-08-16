package com.browser.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.browser.dao.entity.*;
import com.browser.dao.mapper.ProposalInfoMapper;
import com.browser.service.StatisService;
import com.browser.tools.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.browser.config.RealData;
import com.browser.dao.mapper.BlContractBalanceMapper;
import com.browser.dao.mapper.BlContractInfoMapper;
import com.browser.service.BlockService;
import com.browser.service.TransactionService;
import com.browser.tools.exception.BrowserException;

@Service
public class CommonService {

    private static Logger logger = LoggerFactory.getLogger(CommonService.class);

    @Autowired
    private BlockService blockService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private StatisService statisService;

    @Autowired
    private RealData realData;

    @Autowired
    private BlContractInfoMapper blContractInfoMapper;

    @Autowired
    private BlContractBalanceMapper blContractBalanceMapper;

    @Autowired
    private ProposalInfoMapper proposalInfoMapper;

    /**
     * 同步完成后批量插入合约相关信息
     */
    @Transactional
    public void insertBatchContractData() {
        try {
            Set<BlContractInfo> contractInfoSet = realData.getRegisterContractInfo();
            if (contractInfoSet != null) {
                for (BlContractInfo register : contractInfoSet) {
                    blContractInfoMapper.insert(register);
                }
            }

            Set<BlContractInfo> contractInfoUpdateSet = realData.getUpdateContractInfo();
            if (contractInfoUpdateSet != null) {
                for (BlContractInfo update : contractInfoUpdateSet) {
                    blContractInfoMapper.updateByPrimaryKeySelective(update);
                }
            }
            List<BlContractBalance> contractBalanceUpdateSet = realData.getUpdateContractBalance();
            if (contractBalanceUpdateSet != null) {
                for (BlContractBalance update : contractBalanceUpdateSet) {
                    contractBalanceUpdate(update);
                }
            }
            //清空数据
            realData.clear();

        } catch (Exception e) {
            logger.error("批量插入合约数据失败", e);
            throw new BrowserException("批量插入合约数据失败");
        }
    }

    /**
     * 合约余额更新
     */
    private void contractBalanceUpdate(BlContractBalance update) {
        int result = blContractBalanceMapper.updateByPrimaryKeySelective(update);
        if (result < 1) {
            blContractBalanceMapper.insert(update);
        }
    }

    /**
     * 批量插入线程同步完的区块相关数据
     *
     * @param
     * @param transactionList
     * @param
     */
    @Transactional
    public void insertBatchBlockData(BlBlock blBlock, List<BlTransaction> transactionList,Integer trxNum) {
        try {
            if (blBlock != null) {
                blockService.insertSelective(blBlock);
            }
            if (transactionList != null && transactionList.size() > 0) {
                transactionService.insertBatchTransaction(transactionList);

                for (BlTransaction transaction : transactionList) {
                    if (transaction.getOpType() == Constant.PROPOSAL_CREATE_OPERATION) {
                        List<ProposalInfo> info = getInfo(transaction.getExtension(),blBlock.getBlockNum(),blBlock.getBlockTime());
                        if(info!=null && info.size()>0){
                            for (ProposalInfo proposalInfo : info) {
                                proposalInfoMapper.insert(proposalInfo);
                            }
                        }
                    }
                }
            }

//            statisService.statisReward(blBlock);
//            statisService.statisTrxNum(trxNum);
        } catch (Exception e) {
            logger.error("批量插入区块数据失败", e);
            throw new BrowserException("批量插入区块数据失败");
        }
    }

    private List<ProposalInfo> getInfo(String extension, Long block, Date time) {
        List<ProposalInfo> list = new ArrayList<>();
        JSONObject jsonObject = JSONObject.parseObject(extension);
        JSONArray jsonArray = jsonObject.getJSONArray("proposed_ops");

        if (jsonArray != null && jsonArray.size() > 0) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject ops = jsonArray.getJSONObject(i);
                JSONArray op = ops.getJSONArray("op");

                if (ops != null && op.getInteger(0) == Constant.PROPOSAL_COMPETITION_OPERATION) {
                    JSONArray replaceQuene = op.getJSONObject(1).getJSONArray("replace_queue");
                    for (int j = 0; j < replaceQuene.size(); j++) {
                        String from = replaceQuene.getJSONArray(i).getString(0);
                        String to = replaceQuene.getJSONArray(i).getString(1);

                        ProposalInfo proposalInfo = new ProposalInfo();
                        proposalInfo.setRefereeId(from);
                        proposalInfo.setReplacedId(to);
                        proposalInfo.setBlock(block);
                        proposalInfo.setBlockTime(time);
                        proposalInfo.setCreateTime(new Date());

                        list.add(proposalInfo);
                        break;
                    }
                }
            }
        }
        return list;
    }

}
