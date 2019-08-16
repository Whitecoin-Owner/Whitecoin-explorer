package com.browser.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.browser.dao.entity.BlAsset;
import com.browser.dao.entity.ProposalContent;
import com.browser.dao.entity.SenatorCurrent;
import com.browser.dao.entity.SenatorPrevious;
import com.browser.dao.mapper.*;
import com.browser.tools.Constant;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Auther: admin
 * @Date: 2018/12/20 17:37
 * @Description:
 */
@Service
public class SyncSenatorService {

    private static Logger logger = LoggerFactory.getLogger(SyncSenatorService.class);

    @Autowired
    private ProposalContentMapper proposalContentMapper;

    @Autowired
    private ProposalInfoMapper proposalInfoMapper;

    @Autowired
    private ProposalMapper proposalMapper;

    @Autowired
    private SenatorConfigMapper senatorConfigMapper;

    @Autowired
    private SenatorCurrentMapper senatorCurrentMapper;

    @Autowired
    private SenatorPreviousMapper senatorPreviousMapper;

    @Autowired
    private BlAssetMapper assetMapper;

    @Autowired
    private RequestWalletService requestWalletService;

    @Autowired
    private RedisService redisService;

    @Value("${lockBalance.senator.name}")
    private String accountName;
    @Value("${lockBalance.contract}")
    private String lockAddress;

    @Transactional
    public void syncSenator() {
        ProposalContent proposalContent = new ProposalContent();
        proposalContent.setStatus(Constant.PROPOSAL_END);
        proposalContent.setFlag(Constant.SYNC_NON);
        List<ProposalContent> list = proposalContentMapper.selectList(proposalContent);
        if (list != null && list.size() > 0) {
            for (ProposalContent content : list) {
                String curId = redisService.getSingleCurSenatorMap(content.getReferee());
                if (curId != null) {
                    SenatorCurrent senatorCurrent = new SenatorCurrent();
                    senatorCurrent.setSenatorId(curId);
                    senatorCurrent.setProposalId(content.getProposalId());
                    senatorCurrentMapper.updateBySenatorId(senatorCurrent);

                    content.setFlag(Constant.SYNC_ALREADY);
                    proposalContentMapper.updateFlag(content);

                }

                String preId = redisService.getSinglePreSenatorMap(content.getReferee());
                if (preId != null) {
                    SenatorPrevious senatorPrevious = new SenatorPrevious();
                    senatorPrevious.setSenatorId(preId);
                    senatorPrevious.setProposalId(content.getProposalId());
                    senatorPreviousMapper.updateBySenatorId(senatorPrevious);

                    content.setFlag(Constant.SYNC_ALREADY);
                    proposalContentMapper.updateFlag(content);
                }
            }
        }
    }

    public void lockBalance() {
        String result = requestWalletService.getlockedBalance(accountName, lockAddress);
        if (result != null) {
            result = result.replace("\\", "").replace("\"{", "{").replace("}\"", "}");
        }

        String address =null;
        String symbol =null;
        BigDecimal amount =null;
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (jsonObject != null) {
            for(Object map : jsonObject.entrySet()){
                JSONObject addressObject = (JSONObject)((Map.Entry)map).getValue();

                if(addressObject!=null){
                    for(Object addressMap : addressObject.entrySet()){
                        address = (String) ((Map.Entry)addressMap).getKey();
                        JSONObject symbolObject = (JSONObject) ((Map.Entry)addressMap).getValue();

                        if(symbolObject!=null){
                            for(Object symbolMap : symbolObject.entrySet()){
                                symbol = (String) ((Map.Entry)symbolMap).getKey();
                                JSONObject amoutObject = (JSONObject) ((Map.Entry)symbolMap).getValue();
                                amount = amoutObject.getBigDecimal("amount");
                            }
                        }
                    }
                }

                if("HC".equals(symbol)){
                    BlAsset asset = assetMapper.selectBySymbol(symbol);
                    if(asset!=null){
                        BigDecimal balance = amount.divide(new BigDecimal(asset.getPrecision()),8,BigDecimal.ROUND_HALF_UP);
                        SenatorCurrent senatorCurrent = new SenatorCurrent();
                        senatorCurrent.setAddress(address);
                        senatorCurrent.setLockAsset(balance);
                        if(balance.compareTo(BigDecimal.ZERO)==0){
                            senatorCurrent.setStatus(Constant.LOCK_RELEASE);
                        }else{
                            senatorCurrent.setStatus(Constant.LOCK_RELEASE_NOT);
                        }

                        SenatorPrevious senatorPrevious = new SenatorPrevious();
                        senatorPrevious.setAddress(address);
                        senatorPrevious.setLockAsset(balance);
                        if(balance.compareTo(BigDecimal.ZERO)==0){
                            senatorPrevious.setStatus(Constant.LOCK_RELEASE);
                        }else{
                            senatorPrevious.setStatus(Constant.LOCK_RELEASE_NOT);
                        }

                        senatorCurrentMapper.updateByAddress(senatorCurrent);
                        senatorPreviousMapper.updateByAddress(senatorPrevious);
                    }
                }
            }
        }
    }

}
