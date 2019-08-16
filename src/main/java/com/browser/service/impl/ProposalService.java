package com.browser.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.browser.config.CitizenWeightsInit;
import com.browser.dao.entity.*;
import com.browser.dao.mapper.*;
import com.browser.protocol.EUDataGridResult;
import com.browser.tools.Constant;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Auther: admin
 * @Date: 2018/12/20 17:37
 * @Description:
 */
@Service
public class ProposalService {

    private static Logger logger = LoggerFactory.getLogger(ProposalService.class);

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
    private CitizenWeightsInit citizenWeightsInit;

    @Value("${proposal.senator.name}")
    private String name;

    public EUDataGridResult selecCurtList(Proposal proposal) {
        // 分页处理
        PageHelper.startPage(proposal.getPage(), proposal.getRows());
        List<Proposal> list = proposalMapper.selectCurList(proposal);
        dealCurDate(list);
        // 创建一个返回值对象
        EUDataGridResult result = new EUDataGridResult();
        result.setRows(list);
        // 取记录总条数
        PageInfo<Proposal> pageInfo = new PageInfo<>(list);
        result.setTotal(pageInfo.getTotal());
        result.setPages(pageInfo.getPages());
        return result;
    }

    public Proposal proposalDetail(Proposal pro) {
        Proposal proposal = proposalMapper.selectByProposalId(pro.getProposalId());
        if(proposal!=null){
            proposal.setAmonutStr(proposal.getAmonut().stripTrailingZeros().toPlainString() + " " + Constant.SYMBOL);
            proposal.setStatusStr(proposal.getStatus() == Constant.PROPOSAL_ING ? "processing" : "over");

            ProposalContent proposalContent = new ProposalContent();
            proposalContent.setProposalId(proposal.getProposalId());
            List<ProposalContent> contentList = proposalContentMapper.selectList(proposalContent);

            if (contentList != null && contentList.size() > 0) {
                proposal.setContent(contentList);
            }
        }
        return proposal;
    }

    public EUDataGridResult selecPretList(Proposal proposal) {
        // 分页处理
        PageHelper.startPage(proposal.getPage(), proposal.getRows());
        List<Proposal> list = proposalMapper.selectPreList(proposal);
        dealPreDate(list);
        // 创建一个返回值对象
        EUDataGridResult result = new EUDataGridResult();
        result.setRows(list);
        // 取记录总条数
        PageInfo<Proposal> pageInfo = new PageInfo<>(list);
        result.setTotal(pageInfo.getTotal());
        result.setPages(pageInfo.getPages());
        return result;
    }

    private void dealCurDate(List<Proposal> list) {
        if (list != null && list.size() > 0) {
            for (Proposal proposal : list) {
                proposal.setAmonutStr(proposal.getAmonut().stripTrailingZeros().toPlainString() + " " + Constant.SYMBOL);

                ProposalContent proposalContent = new ProposalContent();
                proposalContent.setProposalId(proposal.getProposalId());
                List<ProposalContent> contentList = proposalContentMapper.selectList(proposalContent);

                if (contentList != null && contentList.size() > 0) {
                    proposal.setContent(contentList);
                }
            }
        }
    }

    private void dealPreDate(List<Proposal> list) {
        if (list != null && list.size() > 0) {
            for (Proposal proposal : list) {
                proposal.setAmonutStr(proposal.getAmonut().stripTrailingZeros().toPlainString() + " " + Constant.SYMBOL);
                proposal.setStatusStr(proposal.getStatus() == Constant.PROPOSAL_ING ? "processing" : "over");
            }
        }
    }

    @Transactional
    public void dealProposal() {

        List<Proposal> proposalList = new ArrayList<>();
        List<ProposalContent> contentList = new ArrayList<>();
        String result = requestWalletService.getProposal(name);
        if (result != null) {
            JSONArray proposalArray = JSONArray.parseArray(result);
            if (proposalArray != null && proposalArray.size() > 0) {
                for (int i = 0; i < proposalArray.size(); i++) {
                    Proposal proposal = new Proposal();
                    String id = proposalArray.getJSONObject(i).getString("id");
                    proposal.setProposalId(id);
                    proposal.setProposerId(proposalArray.getJSONObject(i).getString("proposer"));
                    proposal.setProposer(getName(proposalArray.getJSONObject(i).getString("proposer")));

                    BigDecimal amount = proposalArray.getJSONObject(i).getBigDecimal("pledge");
                    amount = amount.divide(new BigDecimal(Constant.PRECISION), 5, BigDecimal.ROUND_HALF_UP);
                    proposal.setAmonut(amount);
                    proposal.setStatus(Constant.PROPOSAL_ING);
                    proposal.setCreateTime(new Date());

                    proposalList.add(proposal);

                    BigDecimal approvedValue = BigDecimal.ZERO;
                    BigDecimal disapprovedValue = BigDecimal.ZERO;
                    BigDecimal requiredValue = BigDecimal.ZERO;

                    JSONArray approved = proposalArray.getJSONObject(i).getJSONArray("approved_key_approvals");
                    JSONArray disapproved = proposalArray.getJSONObject(i).getJSONArray("disapproved_key_approvals");
                    JSONArray required = proposalArray.getJSONObject(i).getJSONArray("required_account_approvals");

                    Map<String ,BigDecimal> weightMap =citizenWeightsInit.getWeightMap();

                    if (approved != null) {
//                        approvedValue = new BigDecimal(approved.size());
                        for (int j = 0; j < approved.size(); j++) {
                            if(weightMap.containsKey(approved.getString(j))){
                                approvedValue = approvedValue.add(weightMap.get(approved.getString(j)));
                            }else{
                                BigDecimal value = updateCitizenWeight(approved.getString(j));
                                approvedValue = approvedValue.add(value);
                            }
                        }
                    }
                    if (disapproved != null) {
//                        disapprovedValue = new BigDecimal(disapproved.size());
                        for (int j = 0; j < disapproved.size(); j++) {
                            if(weightMap.containsKey(disapproved.getString(j))){
                                disapprovedValue = disapprovedValue.add(weightMap.get(disapproved.getString(j)));
                            }else{
                                BigDecimal value = updateCitizenWeight(disapproved.getString(j));
                                disapprovedValue = disapprovedValue.add(value);
                            }
                        }
                    }
                    if (required != null) {
//                        requiredValue = new BigDecimal(required.size());
                        for (int j = 0; j < required.size(); j++) {
                            if(weightMap.containsKey(required.getString(j))){
                                requiredValue = requiredValue.add(weightMap.get(required.getString(j)));
                            }else{
                                BigDecimal value = updateCitizenWeight(required.getString(j));
                                requiredValue = requiredValue.add(value);
                            }
                        }
                    }

                    BigDecimal rate = (approvedValue.add(disapprovedValue)).
                            divide(requiredValue, 4, BigDecimal.ROUND_HALF_UP);

                    JSONArray operation = proposalArray.getJSONObject(i).getJSONObject("proposed_transaction").
                            getJSONArray("operations");

                    List<ProposalContent> contents = dealContent(operation, id, rate);
                    contentList.addAll(contents);
                }
            }
        }

        if (proposalList.size() > 0) {
            List<String> ids = new ArrayList<>();
            for (Proposal proposal : proposalList) {
                Integer id = proposalMapper.selectId(proposal.getProposalId());
                if (id == null) {
                    proposalMapper.insertSelective(proposal);
                }

                ids.add(proposal.getProposalId());
            }

            if (ids != null && ids.size() > 0) {
                proposalMapper.updateStatus(ids);
            }
        } else {
            proposalMapper.updateAll();
        }

        if (contentList.size() > 0) {
            List<String> contentIds = new ArrayList<>();
            for (ProposalContent proposalContent : contentList) {
                //TODO 加入缓存提高速度
                Integer id = proposalContentMapper.selectId(proposalContent.getProposalId());
                if (id == null) {
                    proposalContentMapper.insertSelective(proposalContent);
                } else {
                    proposalContent.setStatus(null);
                    proposalContentMapper.updateByProposalIdSelective(proposalContent);
                }
                contentIds.add(proposalContent.getReferee());
            }

            if (contentIds != null && contentIds.size() > 0) {
                ProposalContent proposalContent = new ProposalContent();
                proposalContent.setStatus(Constant.SENATOR_ING);
                proposalContent.setIds(contentIds);
                List<ProposalContent> proList = proposalContentMapper.selectReferee(proposalContent);
                updateSenatorStatus(proList);
            }
        } else {
            ProposalContent proposalContent = new ProposalContent();
            proposalContent.setStatus(Constant.SENATOR_ING);
            List<ProposalContent> proList = proposalContentMapper.selectReferee(proposalContent);
            updateSenatorStatus(proList);
        }

    }

    private void updateSenatorStatus(List<ProposalContent> proList) {
        if (proList != null && proList.size() > 0) {
            String curResult = requestWalletService.getCurSenator();
            if (curResult != null) {
                JSONArray curArray = JSONArray.parseArray(curResult);
                if (curArray != null && curArray.size() > 0) {

                    List<String> curSenatorName =new ArrayList<>();
                    for (int i = 0; i < curArray.size(); i++) {
                        curSenatorName.add(curArray.getJSONArray(i).getString(0));
                    }

                    for (ProposalContent proContent : proList) {
                        if(curSenatorName!=null && curSenatorName.size()>0){

                            if (curSenatorName.contains(proContent.getReferee())) {

                                ProposalContent update = new ProposalContent();
                                update.setId(proContent.getId());
                                update.setStatus(Constant.SENATOR_SUCC);

                                proposalContentMapper.updateByPrimaryKeySelective(update);

                                //投票成功，推荐人成为正式senator，删除替换人记录
                                senatorCurrentMapper.deleteByName(proContent.getReplacedPerson());
//                                senatorPreviousMapper.deleteByName(proContent.getReplacedPerson());
                                redisService.deleteSingleCurSenatorMap(proContent.getReplacedPerson());
                            } else {

                                ProposalContent update = new ProposalContent();
                                update.setId(proContent.getId());
                                update.setStatus(Constant.SENATOR_FAIL);

                                proposalContentMapper.updateByPrimaryKeySelective(update);
                            }
                        }
                    }
                }
            }
        }
    }

    @Transactional
    public void updateBlockInfo() {
        List<String> list = proposalMapper.selectProposalId();
        if (list != null && list.size() > 0) {
            for (String proId : list) {
                String refereeId = proposalContentMapper.selectRelationId(proId);
                if (refereeId != null) {
                    ProposalInfo proposalInfo = proposalInfoMapper.selectByRefereeId(refereeId);
                    if (proposalInfo != null) {
                        Proposal proposal = new Proposal();
                        proposal.setProposalId(proId);
                        proposal.setBlock(proposalInfo.getBlock());
                        proposal.setProposalTime(proposalInfo.getBlockTime());
                        proposalMapper.updateBlock(proposal);

                        ProposalInfo info =new ProposalInfo();
                        info.setId(proposalInfo.getId());
                        info.setStatus(1);//标记该条数据已处理
                        proposalInfoMapper.updateByPrimaryKeySelective(info);
                    }
                }
            }
        }
    }

    private List<ProposalContent> dealContent(JSONArray operation, String id, BigDecimal rate) {
        List<ProposalContent> contentList = new ArrayList<>();
        if (operation != null && operation.size() > 0) {
            for (int i = 0; i < operation.size(); i++) {
                JSONObject content = operation.getJSONArray(i).getJSONObject(1);
                JSONArray replaceQuene = content.getJSONArray("replace_queue");
                for (int j = 0; j < replaceQuene.size(); j++) {
                    ProposalContent proposalContent = new ProposalContent();
                    proposalContent.setProposalId(id);
                    proposalContent.setRefereeId(replaceQuene.getJSONArray(i).getString(0));
                    proposalContent.setReferee(getName(replaceQuene.getJSONArray(i).getString(0)));
                    proposalContent.setReplacedId(replaceQuene.getJSONArray(i).getString(1));
                    proposalContent.setReplacedPerson(getName(replaceQuene.getJSONArray(i).getString(1)));
                    proposalContent.setVoteRate(rate);
                    proposalContent.setStatus(Constant.SENATOR_ING);
                    proposalContent.setAddress(getAddress(replaceQuene.getJSONArray(i).getString(0)));
                    proposalContent.setFlag(Constant.SYNC_NON);

                    contentList.add(proposalContent);
                }
            }
        }
        return contentList;
    }

    private String getName(String id) {
        String name = redisService.getSenatorName(id);

        if (name == null) {
            String result = requestWalletService.getAccount(id);
            if (result != null) {
                JSONObject account = JSONObject.parseObject(result);
                name = account.getString("name");
                String address = account.getString("addr");

                redisService.putSenatorAddress(id, address);
                redisService.putSenatorName(id, name);
            }
        }
        return name;
    }

    private String getAddress(String id) {
        String address = redisService.getSenatorAddress(id);

        if (address == null) {
            String result = requestWalletService.getAccount(id);
            if (result != null) {
                JSONObject account = JSONObject.parseObject(result);
                address = account.getString("addr");
                String name = account.getString("name");

                redisService.putSenatorAddress(id, address);
                redisService.putSenatorName(id, name);
            }
        }
        return address;
    }

    private BigDecimal updateCitizenWeight(String adddress){
        String accountName = redisService.getAccountName(adddress);
        String citizenInfo = requestWalletService.getMiner(accountName);
        if(StringUtils.isEmpty(citizenInfo)){
            return BigDecimal.ZERO;
        }
        JSONObject citizenObject =JSONObject.parseObject(citizenInfo);
        Map<String,BigDecimal> weightMap = citizenWeightsInit.getWeightMap();
        weightMap.put(adddress,citizenObject.getBigDecimal("pledge_weight"));

        return citizenObject.getBigDecimal("pledge_weight");
    }
}
