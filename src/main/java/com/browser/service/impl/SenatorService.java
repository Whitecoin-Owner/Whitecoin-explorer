package com.browser.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.browser.config.RealData;
import com.browser.dao.entity.*;
import com.browser.dao.mapper.*;
import com.browser.protocol.EUDataGridResult;
import com.browser.tools.Constant;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: admin
 * @Date: 2018/12/20 17:37
 * @Description:
 */
@Service
public class SenatorService {

    private static Logger logger = LoggerFactory.getLogger(SenatorService.class);

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
    private RequestWalletService requestWalletService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RealData realData;

    @Autowired
    private BlTransactionMapper blTransactionMapper;

    @Autowired
    private SenatorSecurityMapper senatorSecurityMapper;

    @Value("${lockBalance.address}")
    private String address;

    @Value("${lockBalance.contract}")
    private String contractId;


    public EUDataGridResult selectCurList(SenatorCurrent senatorCurrent) {
        // 分页处理
        PageHelper.startPage(senatorCurrent.getPage(), senatorCurrent.getRows());
        List<SenatorCurrent> list = senatorCurrentMapper.selectList(senatorCurrent);

        checkSafeStatus(list);

        // 创建一个返回值对象
        EUDataGridResult result = new EUDataGridResult();
        result.setRows(list);
        // 取记录总条数
        PageInfo<SenatorCurrent> pageInfo = new PageInfo<>(list);
        result.setTotal(pageInfo.getTotal());
        result.setPages(pageInfo.getPages());
        return result;
    }

    private void checkSafeStatus(List<SenatorCurrent> list){
        List<String> ssList = senatorSecurityMapper.selectList();
        if(ssList!=null && ssList.size()>0){
            if(list!=null && list.size()>0){
                for (SenatorCurrent senatorCurrent: list) {
                    if(ssList.contains(senatorCurrent.getSenatorId())){
                        senatorCurrent.setSafeStatus(Constant.SENATOR_SAFE);
                    }
                }
            }
        }
    }

    public EUDataGridResult selectPreList(SenatorPrevious senatorPrevious) {
        // 分页处理
        PageHelper.startPage(senatorPrevious.getPage(), senatorPrevious.getRows());
        List<SenatorPrevious> list = senatorPreviousMapper.selectList(senatorPrevious);

        // 创建一个返回值对象
        EUDataGridResult result = new EUDataGridResult();
        result.setRows(list);
        // 取记录总条数
        PageInfo<SenatorPrevious> pageInfo = new PageInfo<>(list);
        result.setTotal(pageInfo.getTotal());
        result.setPages(pageInfo.getPages());
        return result;
    }

    public EUDataGridResult selectJoinList(ProposalContent proposalContent) {
        // 分页处理
        PageHelper.startPage(proposalContent.getPage(), proposalContent.getRows());
        List<ProposalContent> list = proposalContentMapper.selectJoinList(proposalContent);

        // 创建一个返回值对象
        EUDataGridResult result = new EUDataGridResult();
        result.setRows(list);
        // 取记录总条数
        PageInfo<ProposalContent> pageInfo = new PageInfo<>(list);
        result.setTotal(pageInfo.getTotal());
        result.setPages(pageInfo.getPages());
        return result;
    }

    public List<ProposalContent> selectJoinSenator(SenatorPrevious senatorPrevious) {

        List<ProposalContent> contentList = new ArrayList<>();
        List<SenatorConfig> list = senatorConfigMapper.selectList();
        if (list != null && list.size() > 0) {
            for (SenatorConfig senatorConfig : list) {
                ProposalContent proposalContent = proposalContentMapper.selectJoinSenator(senatorConfig.getSenatorName());
                if (proposalContent != null) {
                    contentList.add(proposalContent);

                    if (proposalContent.getSymbol() == null) {
                        ProposalContent content = new ProposalContent();
                        content.setId(proposalContent.getId());
                        content.setSymbol(1);//白名单配置项
                        proposalContentMapper.updateByPrimaryKeySelective(content);
                    }
                }else{
                    ProposalContent content =new ProposalContent();
                    content.setId(senatorConfig.getId());
                    content.setReferee(senatorConfig.getSenatorName());
                    contentList.add(content);
                }
            }
        }
        return contentList;
    }

    public ProposalContent selectJoinDetail(ProposalContent content) {

        ProposalContent proposalContent = proposalContentMapper.selectByPrimaryKey(content.getId());
        if(proposalContent!=null){
            proposalContent.setLockAddress(address);

            SenatorPrevious senatorPrevious = senatorPreviousMapper.selectByName(proposalContent.getReferee());
            if(senatorPrevious!=null){
                if(senatorPrevious.getLockAsset()!=null){
                    proposalContent.setLockNum(senatorPrevious.getLockAsset().stripTrailingZeros().toPlainString()+" HC");
                }
            }
            ProposalContent pc =new ProposalContent();
            pc.setReferee(proposalContent.getReferee());
            List<ProposalContent> proList = proposalContentMapper.selectJoinList(pc);
            if(proList!=null && proList.size()>0){
                proposalContent.setProList(proList);
            }
        }

        return proposalContent;
    }

    public SenatorConfig selectNoMatchedJoinDetail(SenatorConfig senatorConfig) {

        SenatorConfig config = senatorConfigMapper.selectByPrimaryKey(senatorConfig.getId());
        if(config!=null){
            String address = getAddress(config.getSenatorName());
            config.setAddress(address);
        }
        return config;
    }

    public SenatorPrevious selectPreSenatorDetail(SenatorPrevious previous) {

        SenatorPrevious senatorPrevious = senatorPreviousMapper.selectByPrimaryKey(previous.getId());
        if(senatorPrevious!=null){
            senatorPrevious.setLockAddress(address);

            SenatorConfig senatorConfig = senatorConfigMapper.selectByName(senatorPrevious.getSenatorName());
            if(senatorConfig!=null){
                senatorPrevious.setEmail(senatorConfig.getEmail());
            }

            List<String> paybackList = new ArrayList<>();
            Map<String, BigDecimal> paybackMap = new HashMap<>();
            String paybackStr = requestWalletService.getPaybackBalance(senatorPrevious.getAddress());
            if (paybackStr != null) {
                JSONArray balances = JSONObject.parseArray(paybackStr);
                for (int i = 0; i < balances.size(); i++) {
                    JSONObject jsonObject = balances.getJSONArray(i).getJSONObject(1);
                    String assetId = jsonObject.getString("asset_id");

                    BlAsset blAsset = realData.getSymbolByAssetId(assetId);
                    BigDecimal balance = jsonObject.getBigDecimal("amount").
                            divide(new BigDecimal(blAsset.getPrecision()), 8, BigDecimal.ROUND_HALF_UP);

                    if (!paybackMap.containsKey(blAsset.getSymbol())) {
                        paybackMap.put(blAsset.getSymbol(), balance);
                    } else {
                        paybackMap.put(blAsset.getSymbol(), paybackMap.get(blAsset.getSymbol()).add(balance));
                    }

                }

                for (Map.Entry<String, BigDecimal> entry : paybackMap.entrySet()) {
                    paybackList.add(entry.getValue().stripTrailingZeros().toPlainString() + " " + entry.getKey());
                }
            }
            senatorPrevious.setPayBackList(paybackList);

            BlTransaction transaction =new BlTransaction();
            transaction.setFromAccount(senatorPrevious.getAddress());
            transaction.setToAccount(contractId);
            transaction.setCalledAbi("withdraw");
            String trxId = blTransactionMapper.selectLockTrxId(transaction);
            if(trxId!=null){
                senatorPrevious.setTrxId(trxId);
            }

            ProposalContent proposalContent =new ProposalContent();
            proposalContent.setReferee(senatorPrevious.getSenatorName());
            List<ProposalContent> proList = proposalContentMapper.selectJoinList(proposalContent);
            if(proList!=null && proList.size()>0){
                senatorPrevious.setProList(proList);
            }
        }

        return senatorPrevious;
    }

    public SenatorCurrent selectCurSenatorDetail(SenatorCurrent current) {

        SenatorCurrent senatorCurrent = senatorCurrentMapper.selectByPrimaryKey(current.getId());
        if(senatorCurrent!=null){
            senatorCurrent.setLockAddress(address);

            SenatorConfig senatorConfig = senatorConfigMapper.selectByName(senatorCurrent.getSenatorName());
            if(senatorConfig!=null){
                senatorCurrent.setEmail(senatorConfig.getEmail());
            }

            List<String> paybackList = new ArrayList<>();
            Map<String, BigDecimal> paybackMap = new HashMap<>();
            String paybackStr = requestWalletService.getPaybackBalance(senatorCurrent.getAddress());
            if (paybackStr != null) {
                JSONArray balances = JSONObject.parseArray(paybackStr);
                for (int i = 0; i < balances.size(); i++) {
                    JSONObject jsonObject = balances.getJSONArray(i).getJSONObject(1);
                    String assetId = jsonObject.getString("asset_id");

                    BlAsset blAsset = realData.getSymbolByAssetId(assetId);
                    BigDecimal balance = jsonObject.getBigDecimal("amount").
                            divide(new BigDecimal(blAsset.getPrecision()), 8, BigDecimal.ROUND_HALF_UP);

                    if (!paybackMap.containsKey(blAsset.getSymbol())) {
                        paybackMap.put(blAsset.getSymbol(), balance);
                    } else {
                        paybackMap.put(blAsset.getSymbol(), paybackMap.get(blAsset.getSymbol()).add(balance));
                    }

                }

                for (Map.Entry<String, BigDecimal> entry : paybackMap.entrySet()) {
                    paybackList.add(entry.getValue().stripTrailingZeros().toPlainString() + " " + entry.getKey());
                }
            }
            senatorCurrent.setPayBackList(paybackList);

            BlTransaction transaction =new BlTransaction();
            transaction.setFromAccount(senatorCurrent.getAddress());
            transaction.setToAccount(contractId);
            transaction.setCalledAbi("withdraw");
            String trxId = blTransactionMapper.selectLockTrxId(transaction);
            if(trxId!=null){
                senatorCurrent.setTrxId(trxId);
            }

            ProposalContent proposalContent =new ProposalContent();
            proposalContent.setReferee(senatorCurrent.getSenatorName());
            List<ProposalContent> proList = proposalContentMapper.selectJoinList(proposalContent);
            if(proList!=null && proList.size()>0){
                senatorCurrent.setProList(proList);
            }
        }

        return senatorCurrent;
    }


    public void dealSenator() {

        String curResult = requestWalletService.getCurSenator();
        if (curResult != null) {
            JSONArray curArray = JSONArray.parseArray(curResult);
            if (curArray != null && curArray.size() > 0) {
                for (int i = 0; i < curArray.size(); i++) {
                    String name = redisService.getSingleCurSenatorMap(curArray.getJSONArray(i).getString(0));
                    if (name == null) {
                        SenatorCurrent senatorCurrent = new SenatorCurrent();
                        senatorCurrent.setSenatorId(curArray.getJSONArray(i).getString(1));
                        senatorCurrent.setSenatorName(curArray.getJSONArray(i).getString(0));
                        senatorCurrent.setAddress(getAddress(curArray.getJSONArray(i).getString(0)));
                        try {
                            senatorCurrentMapper.insertSelective(senatorCurrent);
                        } catch (Exception e) {
                            logger.error("插入数据异常：{}", e);
                        }

                        redisService.putSingleCurSenatorMap(senatorCurrent.getSenatorName(), senatorCurrent.getSenatorId());
                    }
                }
            }
        }

        String preResult = requestWalletService.getPreSenator();
        if (preResult != null) {
            JSONArray preArray = JSONArray.parseArray(preResult);
            if (preArray != null && preArray.size() > 0) {
                for (int i = 0; i < preArray.size(); i++) {
                    String name = redisService.getSinglePreSenatorMap(preArray.getJSONArray(i).getString(0));
                    if (name == null) {
                        SenatorPrevious senatorPrevious = new SenatorPrevious();
                        senatorPrevious.setSenatorId(preArray.getJSONArray(i).getString(1));
                        senatorPrevious.setSenatorName(preArray.getJSONArray(i).getString(0));
                        senatorPrevious.setAddress(getAddress(preArray.getJSONArray(i).getString(0)));
                        try {
                            senatorPreviousMapper.insertSelective(senatorPrevious);
                        } catch (Exception e) {
                            logger.error("插入数据异常：{}", e);
                        }

                        redisService.putSinglePreSenatorMap(senatorPrevious.getSenatorName(), senatorPrevious.getSenatorId());
                    }
                }
            }
        }
    }

    private String getAddress(String name) {
        String address = redisService.getSenatorAddress(name);

        if (address == null) {
            String result = requestWalletService.getAccount(name);
            if (result != null) {
                JSONObject account = JSONObject.parseObject(result);
                address = account.getString("addr");

                redisService.putSenatorAddress(name, address);
            }
        }
        return address;
    }
}
