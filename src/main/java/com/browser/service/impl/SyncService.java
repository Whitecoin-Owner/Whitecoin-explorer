package com.browser.service.impl;

import java.math.BigDecimal;
import java.util.*;

import com.browser.config.CitizenWeightsInit;
import com.browser.task.plugins.UpdateBalanceSyncPlugin;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.browser.config.RealData;
import com.browser.dao.entity.BlBlock;
import com.browser.dao.entity.BlContractBalance;
import com.browser.dao.entity.BlContractInfo;
import com.browser.dao.entity.BlTransaction;
import com.browser.dao.mapper.BlBlockMapper;
import com.browser.dao.mapper.BlContractInfoMapper;
import com.browser.dao.mapper.BlTransactionMapper;
import com.browser.service.BlockService;
import com.browser.service.TransactionService;
import com.browser.tools.Constant;
import com.browser.tools.common.DateUtil;
import com.browser.tools.common.StringUtil;

@Service
public class SyncService {

    private static Logger logger = LoggerFactory.getLogger(SyncService.class);

    @Autowired
    private BlockService blockService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CommonService commonService;

    @Autowired
    private RequestWalletService requestWalletService;

    @Autowired
    private RealData realData;

    @Autowired
    private BlBlockMapper blBlockMapper;

    @Autowired
    private BlTransactionMapper blBcTransactionMapper;

    @Autowired
    private BlContractInfoMapper blContractInfoMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private CitizenWeightsInit citizenWeightsInit;

    @Autowired
    private UpdateBalanceSyncPlugin updateBalanceSyncPlugin;

    // @Autowired
    // private BlContractBalanceMapper blContractBalanceMapper;

    private static Set<Integer> typeSet = new HashSet<Integer>();

    static {
        // TODO: magic number change
        typeSet.add(0);
        typeSet.add(60);
        typeSet.add(61);
        typeSet.add(62);
        typeSet.add(63);
        typeSet.add(64);
        typeSet.add(65);
        typeSet.add(76);
        typeSet.add(77);
        typeSet.add(79);
        typeSet.add(81);

//        typeSet.add(Constant.NAME_TRANSFER_OPERATION);
    }

    public void blockSync(Long blockNum) {

        // 获取队列数据，请求
        BlBlock bc =new BlBlock();
        List<BlTransaction> transactionList = new ArrayList<>();
        Integer trxNum=0;
        try {

            String blockMsg = null;
            for(int i=0;i<3;i++) {
                blockMsg = requestWalletService.getBlockInfo(blockNum); // 获取块信息
                if(!StringUtil.isEmpty(blockMsg)) {
                    break;
                }
            }

            if (StringUtils.isEmpty(blockMsg)) {
                logger.error("empty blockMsg " + blockMsg);
                return;
            }

            JSONObject blockJson = JSONObject.parseObject(blockMsg);
            String minerId = blockJson.getString("miner");

            JSONObject jsonObject = redisService.getMinerInfo(minerId);
            if (jsonObject == null) {

                String minerInfo = requestWalletService.getMiner(minerId);// 获取矿工信息
                if (StringUtils.isEmpty(minerInfo)) {
                    return;
                }
                String accountInfo = requestWalletService
                        .getAccount(JSONObject.parseObject(minerInfo).getString("miner_account"));// 获取矿工地址
                if (StringUtils.isEmpty(accountInfo)) {
                    return;
                }

                jsonObject = JSONObject.parseObject(accountInfo);
                redisService.putMinerInfo(minerId, jsonObject);
                redisService.putAccountAddr(jsonObject.getString("name"), jsonObject.getString("addr"));
            }

            // 存入链表，后续批量操作
            bc = StringUtil.getBlockInfo(blockJson, jsonObject);
            if(bc.getBlockId()==null) {
                logger.error("invalid block json " + blockMsg);
            }

            // 批量交易处理
            JSONArray trxIds = blockJson.getJSONArray("transaction_ids");
            trxNum =trxIds.size();

            JSONArray transactions = blockJson.getJSONArray("transactions");
//            BigDecimal fee = new BigDecimal(0);
            if (transactions != null && transactions.size() > 0) {
                for (int i = 0; i < transactions.size(); i++) {
                    // 处理单个交易信息
                    String txId = (String) trxIds.get(i);
                    JSONObject trans = transactions.getJSONObject(i); // 获取块交易信息

                    // 交易op列表处理、每个op生成一条交易记录，暂处理合约、转账、提现
                    JSONArray operations = trans.getJSONArray("operations");
                    if (operations != null && operations.size() > 0) {
                        for (int j = 0; j < operations.size(); j++) {
                            Integer opType = operations.getJSONArray(j).getInteger(0);
                            String opTypeName = "TODO"; // TODO
                            JSONObject json = operations.getJSONArray(j).getJSONObject(1);
//                            fee = fee.add(json.getJSONObject("fee").getBigDecimal("amount"));
                            if (typeSet.contains(opType)) {

                                // 普通交易转账处理
                                if (Constant.TRX_TYPE_TRANSFER == opType
                                        || Constant.TRX_TYPE_CROSS_TRANSFER == opType) {
                                    BlTransaction trx = commonTransaction(json, opType);
                                    trx.setTrxId(txId);
                                    trx.setBlockId(bc.getBlockId());
                                    trx.setBlockNum(bc.getBlockNum());
                                    trx.setTrxTime(bc.getBlockTime());
                                    transactionList.add(trx);
                                }

                                // 提现交易处理
                                if (Constant.TRX_TYPE_WITHDRAW_REQ == opType
                                        || Constant.TRX_TYPE_WITHDRAW_CREATE == opType
                                        || Constant.TRX_TYPE_WITHDRAW_SIGN == opType
                                        || Constant.TRX_TYPE_WITHDRAW_SEND == opType
                                        || Constant.TRX_TYPE_WITHDRAW_SUCC == opType) {
                                    BlTransaction trx = crossWithdrawTransaction(json, txId, opType);
                                    trx.setBlockId(bc.getBlockId());
                                    trx.setBlockNum(bc.getBlockNum());
                                    trx.setTrxTime(bc.getBlockTime());
                                    transactionList.add(trx);
                                }

                                // 合约交易处理
                                if (Constant.CONTRACT_REGISTER_OPERATION == opType
                                        || Constant.CONTRACT_UPGRADE_OPERATION == opType
                                        || Constant.CONTRACT_INVOKE_OPERATION == opType
                                        || Constant.CONTRACT_TRANSFER_OPERATION == opType) {
                                    BlTransaction trx = contractTransaction(json, txId, opType);
                                    trx.setBlockId(bc.getBlockId());
                                    trx.setBlockNum(bc.getBlockNum());
                                    trx.setTrxTime(bc.getBlockTime());
                                    transactionList.add(trx);
                                }

                                // 账户改名交易处理
                                if(Constant.NAME_TRANSFER_OPERATION == opType) {
                                    // TODO
                                }

                            } else {// 其他类型交易处理
                                BlTransaction trx = unparsedTransaction(json, opType);
                                trx.setTrxId(txId);
                                trx.setOpType(opType);
                                trx.setBlockId(bc.getBlockId());
                                trx.setBlockNum(bc.getBlockNum());
                                trx.setTrxTime(bc.getBlockTime());
                                transactionList.add(trx);
                            }

                            JSONObject opReceipt = null;

                            updateBalanceSyncPlugin.applyOperation(blockJson, txId, j, opType, opTypeName, json, opReceipt);
                        }
                    }
                }
            }
            bc.setTrxCount(trxIds.size());
            if (trxIds != null && trxIds.size() > 0) {
                bc.setMerkleRoot(blockJson.getString("transaction_merkle_root"));
            }
            BigDecimal reward = blockJson.getBigDecimal("reward");
            bc.setReward(reward);// 区块奖励

        } catch (Exception e) {
            logger.error("{}块处理数据出错", blockNum, e);
        } finally {

            // 保存批量数据
            commonService.insertBatchBlockData(bc, transactionList,trxNum);

            // 保存合约信息
            commonService.insertBatchContractData();
        }
    }

    /**
     * 账户改名交易信息解析
     * @param jsa
     * @param trxId
     * @param opType
     * @return
     */
    private BlTransaction nameTransferTransaction(JSONObject jsa, String trxId, Integer opType) {
        return null; // TODO
    }

    /**
     * 合约交易信息解析
     *
     * @param jsa
     */
    private BlTransaction contractTransaction(JSONObject jsa, String trxId, Integer opType) {

        BlTransaction contractTrx = new BlTransaction();
        contractTrx.setTrxId(trxId);
        contractTrx.setOpType(opType);
        contractTrx.setParentOpType(Constant.PARENT_CONTRACT);
        contractTrx.setFee(jsa.getJSONObject("fee").getBigDecimal("amount"));

        String contractId = jsa.getString("contract_id");
        if("XWCCZxWGXvncYcemJB52ZyreHAQDUtRshjmky".equals(contractId)){
            contractId = "XWCCZebgCspNfXZ6tzv7bWYzs7Yj6FYiucHJd";
        }
        contractTrx.setContractId(contractId);
        contractTrx.setGuaranteeId(jsa.getString("guarantee_id"));
        // 非注册合约处理
        if (opType != Constant.CONTRACT_REGISTER_OPERATION) {

            contractTrx.setFromAccount(jsa.getString("caller_addr"));
            contractTrx.setToAccount(jsa.getString("contract_id"));
            contractTrx.setGasCost(jsa.getInteger("invoke_cost"));
            contractTrx.setGasPrice(jsa.getBigDecimal("gas_price"));

            // 合约转账
            if (opType == Constant.CONTRACT_TRANSFER_OPERATION) {
                contractTrx.setAmount(jsa.getJSONObject("amount").getBigDecimal("amount"));
                contractTrx.setAssetId(jsa.getJSONObject("amount").getString("asset_id"));
            }

            // 合约调用
            if (opType == Constant.CONTRACT_INVOKE_OPERATION) {
                contractTrx.setCalledAbi(jsa.getString("contract_api"));
                contractTrx.setAbiParams(jsa.getString("contract_arg"));
            }
        }

        switch (contractTrx.getOpType()) {
            case Constant.CONTRACT_REGISTER_OPERATION:

                // 注册合约金额、手续费
                contractTrx.setGasCost(jsa.getInteger("init_cost"));
                contractTrx.setGasPrice(jsa.getBigDecimal("gas_price"));
                contractTrx.setExtraTrxId(jsa.getString("inherit_from"));// 继承合约
                // 注册合约处理
                String contractMsg = requestWalletService.getContractInfo(contractTrx.getContractId());
                JSONObject contractJson = JSONObject.parseObject(contractMsg);
                BlContractInfo contractInfo = new BlContractInfo();
                // 合约id
                contractInfo.setContractId(contractTrx.getContractId());
                // 时间转换
                Date date = DateUtil.parseDate(jsa.getString("register_time"), "yyyy-MM-dd'T'HH:mm:ss");
                contractInfo.setRegTime(new Date(date.getTime() + 8 * 60 * 60 * 1000L));
                contractInfo.setName(contractJson.getString("name"));
                contractInfo.setOwner(contractJson.getString("owner"));
                contractInfo.setOwnerAddress(contractJson.getString("owner_address"));
                contractInfo.setOwnerName(contractJson.getString("owner_name"));
                contractInfo.setDescription(contractJson.getString("description"));
                contractInfo.setBlockNum(contractJson.getLong("registered_block"));
                JSONArray abi = contractJson.getJSONObject("code_printable").getJSONArray("abi");
                JSONArray events = contractJson.getJSONObject("code_printable").getJSONArray("events");
                JSONObject contract = new JSONObject();
                contract.put("abi", abi);
                contract.put("events", events);
                contractInfo.setCode(contract.toJSONString());
                contractInfo.setStatus(Constant.TEMP_STATE);

                // 添加到内存
                realData.setRegisterContractInfo(contractInfo);

                break;

            case Constant.CONTRACT_UPGRADE_OPERATION:// 合约更新
                String contractUpdate = requestWalletService.getContractInfo(contractTrx.getContractId());
                JSONObject updateJson = JSONObject.parseObject(contractUpdate);
                BlContractInfo contractInfoUpdate = new BlContractInfo();
                contractInfoUpdate.setContractId(contractTrx.getContractId());
                contractInfoUpdate.setBlockNum(updateJson.getLong("registered_block"));
                contractInfoUpdate.setName(jsa.getString("contract_name"));
                contractInfoUpdate.setDescription(jsa.getString("contract_desc"));
                contractInfoUpdate.setStatus(Constant.FOREVER_STATE);

                realData.setUpdateContractInfo(contractInfoUpdate);

                break;
        }

        // 非注册合约需要更新balance
        if (contractTrx.getOpType() != Constant.CONTRACT_REGISTER_OPERATION) {
            String balanceMsgUpdate = requestWalletService.getContractBalance(contractTrx.getToAccount());
            JSONArray balance = JSONArray.parseArray(balanceMsgUpdate);
            BlContractBalance contractInfo = new BlContractBalance();
            if (balance != null && balance.size() > 0) {
                for (int i = 0; i < balance.size(); i++) {
                    contractInfo.setContractId(contractTrx.getToAccount());
                    contractInfo.setBalance(balance.getJSONObject(i).getBigDecimal("amount"));
                    contractInfo.setAssetId(balance.getJSONObject(i).getString("asset_id"));
                    realData.setUpdateContractBalance(contractInfo);
                }
            }

        }
        return contractTrx;
    }

    /**
     * 普通、跨链交易解析
     *
     * @param json
     * @param opType
     * @return
     */
    private BlTransaction commonTransaction(JSONObject json, Integer opType) {
        BlTransaction blTransaction = new BlTransaction();
        blTransaction.setOpType(opType);
        blTransaction.setFee(json.getJSONObject("fee").getBigDecimal("amount"));
        blTransaction.setGuaranteeId(json.getString("guarantee_id"));
        // 普通link链交易
        if (Constant.TRX_TYPE_TRANSFER == opType) {
            blTransaction.setAmount(json.getJSONObject("amount").getBigDecimal("amount"));
            blTransaction.setAssetId(json.getJSONObject("amount").getString("asset_id"));
            blTransaction.setFromAccount(json.getString("from_addr"));
            blTransaction.setToAccount(json.getString("to_addr"));
            JSONObject jsonObject = json.getJSONObject("memo");
            if (null != jsonObject) {
                blTransaction.setMemo(LengthCheck(jsonObject.getString("message")));
            }
            blTransaction.setParentOpType(Constant.PARENT_TRANSFER);
        } else {// 跨链交易
            blTransaction.setAmount(json.getJSONObject("cross_chain_trx").getBigDecimal("amount"));
            blTransaction.setAssetId(json.getString("asset_id"));
            blTransaction.setFromAccount(json.getJSONObject("cross_chain_trx").getString("from_account"));
            blTransaction.setToAccount(json.getString("deposit_address"));
            blTransaction.setMinerAddress(json.getString("miner_address"));
            blTransaction.setParentOpType(Constant.PARENT_RECHARGE);
        }
        return blTransaction;
    }

    /**
     * 提现流程解析
     *
     * @param json
     * @param opType
     * @return
     */
    private BlTransaction crossWithdrawTransaction(JSONObject json, String trxId, Integer opType) {
        BlTransaction withdrawTransaction = new BlTransaction();
        withdrawTransaction.setTrxId(trxId);
        withdrawTransaction.setOpType(opType);
        withdrawTransaction.setParentOpType(Constant.PARENT_WIHTDRAW);
        withdrawTransaction.setFee(json.getJSONObject("fee").getBigDecimal("amount"));
        withdrawTransaction.setGuaranteeId(json.getString("guarantee_id"));

        switch (opType) {
            case Constant.TRX_TYPE_WITHDRAW_REQ:
                // 交易请求
                withdrawTransaction.setFromAccount(json.getString("withdraw_account"));
                withdrawTransaction.setToAccount(json.getString("crosschain_account"));
                withdrawTransaction.setAmount(json.getBigDecimal("amount"));
                withdrawTransaction.setAssetId(json.getString("asset_id"));
                withdrawTransaction.setMemo(json.getString("memo"));
                withdrawTransaction.setExtension1(Constant.WITHDRAW_REQ);

                // 缓存提现流程状态
                redisService.putWithdrawStatus(trxId, Constant.WITHDRAW_REQ);
                // 缓存提现跨链方地址、后续更改流程状态使用
                redisService.putCrosschainAddr(trxId, withdrawTransaction.getToAccount());
                break;

            case Constant.TRX_TYPE_WITHDRAW_CREATE:
                // 交易创建
                // 交易创建收集的原始提现请求交易id集合
                JSONArray withdrawTxId = json.getJSONArray("ccw_trx_ids");
                if (withdrawTxId != null && withdrawTxId.size() > 0) {
                    withdrawTransaction.setExtraTrxId(json.getJSONArray("ccw_trx_ids").toJSONString());

                    updateWithdrawStatus(withdrawTxId, Constant.WITHDRAW_CREATE);

                    // 缓存创建提现交易的id、后续更改流程状态使用
                    redisService.putUnSignTxId(trxId, withdrawTxId.toJSONString());
                }
                withdrawTransaction.setExtension(LengthCheck(json.toJSONString()));

                break;

            case Constant.TRX_TYPE_WITHDRAW_SIGN:
                // 交易签名
                // 创建提现交易的id
                String signedTxId = json.getString("ccw_trx_id");

                String withdrawTrxId = redisService.getUnSignTxId(signedTxId);
                if (StringUtil.isEmpty(withdrawTrxId)) {
                    withdrawTrxId = transactionService.selectextraTrxId(signedTxId, Constant.TRX_TYPE_WITHDRAW_CREATE);
                }
                JSONArray withdrawArray = JSONObject.parseArray(withdrawTrxId);

                updateWithdrawStatus(withdrawArray, Constant.WITHDRAW_SIGN);

                withdrawTransaction.setExtraTrxId(signedTxId);
                withdrawTransaction.setExtension(LengthCheck(json.toJSONString()));

                break;

            case Constant.TRX_TYPE_WITHDRAW_SEND:
                // 交易广播
                // 创建提现交易的id
                String sendTxId = json.getString("withdraw_trx");

                String sendWithdrawTrxId = redisService.getUnSignTxId(sendTxId);
                if (StringUtil.isEmpty(sendWithdrawTrxId)) {
                    sendWithdrawTrxId = transactionService.selectextraTrxId(sendTxId, Constant.TRX_TYPE_WITHDRAW_CREATE);
                }
                JSONArray sendWithdrawArray = JSONObject.parseArray(sendWithdrawTrxId);

                updateWithdrawStatus(sendWithdrawArray, Constant.WITHDRAW_SEND);

                withdrawTransaction.setExtraTrxId(json.getString("crosschain_trx_id"));
                withdrawTransaction.setExtension(LengthCheck(json.toJSONString()));

                redisService.putCrosschainTxId(withdrawTransaction.getExtraTrxId(), json.getString("withdraw_trx"));
                break;

            case Constant.TRX_TYPE_WITHDRAW_SUCC:
                // 交易成功
                JSONObject cross = json.getJSONObject("cross_chain_trx");
                withdrawTransaction.setFromAccount(cross.getString("from_account"));
                withdrawTransaction.setToAccount(cross.getString("to_account"));
                withdrawTransaction.setAmount(cross.getBigDecimal("amount"));
                withdrawTransaction.setSymbol(cross.getString("asset_symbol"));
                withdrawTransaction.setMinerAddress(json.getString("miner_address"));
                withdrawTransaction.setExtraTrxId(cross.getString("trx_id"));
                withdrawTransaction.setExtension(LengthCheck(json.toJSONString()));

                String withdrawTrx = redisService.getCrosschainTxId(withdrawTransaction.getExtraTrxId());
                if (StringUtil.isEmpty(withdrawTrx)) {
                    String extension = transactionService.selectExtension(withdrawTransaction.getExtraTrxId(),
                            Constant.TRX_TYPE_WITHDRAW_SEND);
                    JSONObject extensionJson = JSONObject.parseObject(extension);
                    withdrawTrx = extensionJson.getString("withdraw_trx");
                }

                String withdrawTrxIds = redisService.getUnSignTxId(withdrawTrx);
                if (StringUtil.isEmpty(withdrawTrxIds)) {
                    withdrawTrxIds = transactionService.selectextraTrxId(withdrawTrx, Constant.TRX_TYPE_WITHDRAW_CREATE);
                }

                JSONArray jsa = JSONObject.parseArray(withdrawTrxIds);
                if (jsa != null && jsa.size() > 0) {
                    for (int i = 0; i < jsa.size(); i++) {
                        String addr = redisService.getCrosschainAddr(jsa.getString(i));

                        if (StringUtil.isEmpty(withdrawTrxIds)) {
                            addr = transactionService.selectCrossAddr(jsa.getString(i), Constant.TRX_TYPE_WITHDRAW_REQ);
                        }
                        if (addr.equals(withdrawTransaction.getToAccount())) {
                            transactionService.updateByTxIdAndType(jsa.getString(i), Constant.WITHDRAW_SUCC);
                            redisService.putWithdrawStatus(jsa.getString(i), Constant.WITHDRAW_SUCC);
                        }
                    }
                }

                break;
        }
        return withdrawTransaction;
    }

    /**
     * 其他交易类型
     *
     * @param json
     * @param opType
     * @return
     */
    private BlTransaction unparsedTransaction(JSONObject json, Integer opType) {
        BlTransaction blTransaction = new BlTransaction();
        blTransaction.setFee(json.getJSONObject("fee").getBigDecimal("amount"));
        blTransaction.setOpType(opType);

        blTransaction.setExtension(LengthCheck(json.toJSONString()));
        blTransaction.setParentOpType(Constant.PARENT_OTHER);
        blTransaction.setGuaranteeId(json.getString("guarantee_id"));
        if (Constant.GURANTEE_CREATE_OPERATION == opType) {
            blTransaction.setExtension1(Constant.GURANTEE_VALID);
            blTransaction.setFromAccount(json.getString("owner_addr"));
            blTransaction.setParentOpType(Constant.PARENT_ACCEPTANCE);

        }
        if (Constant.GURANTEE_CANCEL_OPERATION == opType) {
            blTransaction.setParentOpType(Constant.PARENT_ACCEPTANCE);
            transactionService.updateGuranteeStatus(json.getString("owner_addr"), Constant.GURANTEE_INVALID);
        }

        //注册
        if (Constant.ACCOUNT_REGISTER == opType) {
            blTransaction.setParentOpType(Constant.PARENT_OTHER);
            blTransaction.setFromAccount(json.getString("payer"));
            blTransaction.setExtension(json.getString("name"));

            redisService.putAccountName(json.getString("payer"), json.getString("name"));
            redisService.putAccountAddr(json.getString("name"), json.getString("payer"));
        }

        //质押
        if (Constant.ASSET_MORGAGE == opType) {
            blTransaction.setParentOpType(Constant.PARENT_MORTGAGE);
            blTransaction.setFromAccount(json.getString("lock_balance_addr"));
            JSONObject jsonObject = getMinerAddr(json.getString("lockto_miner_account"));
            if(jsonObject!=null){
                blTransaction.setToAccount(jsonObject.getString("addr"));
                blTransaction.setExtension(jsonObject.getString("name"));

                //更新内存中citizen权重
                updateCitizenWeight(jsonObject.getString("addr"));
            }

            blTransaction.setAssetId(json.getString("lock_asset_id"));
            blTransaction.setAmount(json.getBigDecimal("lock_asset_amount"));
            blTransaction.setFee(json.getJSONObject("fee").getBigDecimal("amount"));


        }

        //赎回
        if (Constant.ASSET_REDEEM == opType) {
            blTransaction.setParentOpType(Constant.PARENT_FORECLOSE);
            JSONObject jsonObject = getMinerAddr(json.getString("foreclose_miner_account"));
            if(jsonObject!=null){
                blTransaction.setFromAccount(jsonObject.getString("addr"));
                blTransaction.setExtension(jsonObject.getString("name"));

                //更新内存中citizen权重
                updateCitizenWeight(jsonObject.getString("addr"));
            }

            blTransaction.setToAccount(json.getString("foreclose_addr"));
            blTransaction.setAssetId(json.getString("foreclose_asset_id"));
            blTransaction.setAmount(json.getBigDecimal("foreclose_asset_amount"));
            blTransaction.setFee(json.getJSONObject("fee").getBigDecimal("amount"));


        }

        //绑定、解绑
        if (Constant.BINDING == opType || Constant.UNBINDING == opType) {
            blTransaction.setParentOpType(Constant.PARENT_OTHER);
            blTransaction.setFromAccount(json.getString("addr"));
        }

        //领取工资
        if (Constant.TRX_TYPE_PAY_BACK == opType) {
            blTransaction.setParentOpType(Constant.PARENT_WAGE);
            blTransaction.setToAccount(json.getString("pay_back_owner"));

            JSONArray jsonArray =json.getJSONArray("pay_back_balance");
            BigDecimal reward = BigDecimal.ZERO;
            if(jsonArray!=null && jsonArray.size()>0){

                for (int i = 0; i < jsonArray.size(); i++) {
                    reward = reward.add(jsonArray.getJSONArray(i).getJSONObject(1).getBigDecimal("amount"));
                }
            }
            blTransaction.setAmount(reward);
        }

        return blTransaction;
    }

    /**
     * 更新提现流程状态
     *
     * @param array
     * @param status
     */
    private void updateWithdrawStatus(JSONArray array, Integer status) {
        if (array != null && array.size() > 0) {
            for (int i = 0; i < array.size(); i++) {
                transactionService.updateByTxIdAndType(array.getString(i), status);
                redisService.putWithdrawStatus(array.getString(i), status);
            }
        }
    }

    /**
     * 信息长度过滤
     *
     * @param str
     * @return
     */
    private String LengthCheck(String str) {
        if (!StringUtil.isEmpty(str)) {
            int length = str.length();
            if (length > Constant.MAX_LENGTH) {
                return null;
            }
        }
        return str;
    }

    private void updateCitizenWeight(String adddress){
        String accountName = redisService.getAccountName(adddress);
        if(accountName==null){
            return;
        }
        String citizenInfo = requestWalletService.getMiner(accountName);
        if(StringUtils.isEmpty(citizenInfo)){
           return;
        }
        JSONObject citizenObject =JSONObject.parseObject(citizenInfo);
        Map<String,BigDecimal> weightMap = citizenWeightsInit.getWeightMap();
        weightMap.put(adddress,citizenObject.getBigDecimal("pledge_weight"));
    }

    private JSONObject getMinerAddr(String minerId) {
        return redisService.getMinerInfo(minerId);
    }

}
