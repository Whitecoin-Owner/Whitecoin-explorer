package com.browser.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.browser.config.MinerWeightsInit;
import com.browser.config.RealData;
import com.browser.dao.entity.*;
import com.browser.dao.mapper.BlBlockMapper;
import com.browser.dao.mapper.BlContractInfoMapper;
import com.browser.dao.mapper.BlTokenMapper;
import com.browser.dao.mapper.BlTransactionMapper;
import com.browser.service.*;
import com.browser.service.swapEventPlugins.ISwapEventPlugin;
import com.browser.task.plugins.UpdateBalanceSyncPlugin;
import com.browser.tools.Constant;
import com.browser.tools.common.DateUtil;
import com.browser.tools.common.StringUtil;
import com.browser.wallet.OperationUtils;
import com.browser.wallet.PrecisionUtils;
import com.browser.wallet.beans.ContractTxReceipt;
import com.browser.wallet.beans.SimpleContractInfo;
import com.browser.wallet.beans.TxReceiptContractBalanceChange;
import com.browser.wallet.beans.TxReceiptEvent;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

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
    private TxEventsService txEventsService;
    @Autowired
    private TxContractBalanceChangeService txContractBalanceChangeService;
    @Autowired
    private BlTokenMapper blTokenMapper;
    @Autowired
    private TokenTransactionService tokenTransactionService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private MinerWeightsInit minerWeightsInit;

    @Autowired
    private UpdateBalanceSyncPlugin updateBalanceSyncPlugin;

    @Autowired
    private SwapContractService swapContractService;
    @Autowired
    private SwapTransactionService swapTransactionService;

    @Autowired
    private TokenBalanceService tokenBalanceService;

    @Value("${wallet.caller}")
    private String walletRpcCaller;

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

    private ConcurrentHashMap<String, BlToken> activeTokens = null; // contractAddress => token

    // // 添加合约events到数据库
    private void syncTxEventsToDb(List<TxReceiptEvent> txReceiptEvents, String txId, Long blockNum) {
        if (txReceiptEvents == null) {
            return;
        }
        for (int k = 0; k < txReceiptEvents.size(); k++) {
            TxReceiptEvent receiptEvent = txReceiptEvents.get(k);
            if (txEventsService.selectByTrxIdAndEventSeq(txId, k) != null) {
                continue;
            }
            BlTxEvents blTxEvents = new BlTxEvents();
            blTxEvents.setTrxId(txId);
            blTxEvents.setEventSeq(k);
            blTxEvents.setBlockNum(blockNum);
            blTxEvents.setCallerAddr(receiptEvent.getCaller_addr());
            blTxEvents.setContractAddress(receiptEvent.getContract_address());
            blTxEvents.setEventName(receiptEvent.getEvent_name());
            blTxEvents.setEventArg(receiptEvent.getEvent_arg());
            blTxEvents.setOpNum(receiptEvent.getOp_num());
            txEventsService.insert(blTxEvents);
        }
    }

    private void syncReceiptTxContractBalanceChangesToDb(ContractTxReceipt txReceipt, String txId, Long blockNum) {
        List<TxReceiptContractBalanceChange> contract_withdraw =
                TxReceiptContractBalanceChange.fromTxReceiptChangeJson(txReceipt.getContract_withdraw());
        List<TxReceiptContractBalanceChange> contract_balances =
                TxReceiptContractBalanceChange.fromTxReceiptChangeJson(txReceipt.getContract_balances());
        List<TxReceiptContractBalanceChange> deposit_to_address =
                TxReceiptContractBalanceChange.fromTxReceiptChangeJson(txReceipt.getDeposit_to_address());
        List<TxReceiptContractBalanceChange> deposit_contract =
                TxReceiptContractBalanceChange.fromTxReceiptChangeJson(txReceipt.getDeposit_contract());
        Map<String, List<TxReceiptContractBalanceChange>> allNativeChangesInReceipt = new HashMap<>();
        allNativeChangesInReceipt.put("contract_withdraw", contract_withdraw);
        allNativeChangesInReceipt.put("contract_balances", contract_balances);
        allNativeChangesInReceipt.put("deposit_to_address", deposit_to_address);
        allNativeChangesInReceipt.put("deposit_contract", deposit_contract);
        for (String changeType : allNativeChangesInReceipt.keySet()) {
            List<TxReceiptContractBalanceChange> changes = allNativeChangesInReceipt.get(changeType);
            if (changes == null) {
                continue;
            }
            for (int k = 0; k < changes.size(); k++) {
                TxReceiptContractBalanceChange changeItem = changes.get(k);
                if (txContractBalanceChangeService.selectByTrxIdAndChangeTypeAndChangeSeq(txId, changeType, k) != null) {
                    continue;
                }
                BlTxContractBalanceChange blTxContractBalanceChange = new BlTxContractBalanceChange();
                blTxContractBalanceChange.setChangeSeq(k);
                blTxContractBalanceChange.setChangeType(changeType);
                blTxContractBalanceChange.setTrxId(txId);
                blTxContractBalanceChange.setAddress(changeItem.getAddress());
                blTxContractBalanceChange.setAmount(changeItem.getAmount());
                blTxContractBalanceChange.setAssetId(changeItem.getAssetId());
                blTxContractBalanceChange.setBlockNum(blockNum);
                txContractBalanceChangeService.insert(blTxContractBalanceChange);
            }
        }

    }

    // token合约的Transfer事件，增加流水到token_transaction
    private void syncTokenTransferEventsToDb(List<TxReceiptEvent> txReceiptEvents, String txId, Long blockNum, BlTransaction trx) {
        if (txReceiptEvents == null) {
            return;
        }
        for (int k = 0; k < txReceiptEvents.size(); k++) {
            TxReceiptEvent receiptEvent = txReceiptEvents.get(k);
            if (activeTokens == null) {
                continue;
            }
            String contractId = trx.getContractId();
            if (receiptEvent.getEvent_name().equals("Transfer") || receiptEvent.getEvent_name().equals("OwnerChanged") || receiptEvent.getEvent_name().equals("Inited")) {
                // 如果发现非现有token合约的Transfer事件，则可能是新token合约
                SimpleContractInfo contractInfo = requestWalletService.getSimpleContractInfo(contractId);
                if (contractInfo == null) {
                    logger.error("can't find contract {}", contractId);
                    continue;
                }
                // 即使没初始化好的token合约也立刻添加，另外加上定时任务更新
                Integer precision = 0;
                BigDecimal totalSupply = BigDecimal.ZERO;
                String tokenSymbol = null;
                try {
                    precision = requestWalletService.getTokenPrecisionDecimals(walletRpcCaller, contractId);
                    BigInteger totalSupplyFull = requestWalletService.getTokenTotalSupply(walletRpcCaller, contractId);
                    totalSupply = new BigDecimal(totalSupplyFull).setScale(precision, RoundingMode.FLOOR)
                            .divide(PrecisionUtils.decimalsToPrecision(precision), RoundingMode.FLOOR);
                } catch (Exception e) {
                }
                try {
                    tokenSymbol = requestWalletService.getTokenSymbol(walletRpcCaller, contractId);
                } catch (Exception e) {
                }
                BlToken newToken = new BlToken();
                newToken.setContractAddress(contractInfo.getId());
                newToken.setCreateTrxId(contractInfo.getRegistered_trx());
                newToken.setCreatorAddress(contractInfo.getOwner_address());
                newToken.setPrecision(precision);
                newToken.setTokenSupply(totalSupply);
                newToken.setTokenSymbol(tokenSymbol);
                newToken.setCreateAt(new Date());
                if (blTokenMapper.selectByContractAddress(contractId) == null) {
                    blTokenMapper.insert(newToken);
                } else {
                    Set<String> contractAddressList = blTokenMapper.getTpContractAddressList();
                    if (contractAddressList.contains(newToken.getContractAddress())) {
                        newToken.setTokenSymbol(null);
//                        newToken.setPrecision(null);
                    }
                    blTokenMapper.updateBlToken(newToken);
                }
                // 如果newToken初始化成功，则加入activeTokens
                if (newToken.getTokenSymbol() != null && newToken.getTokenSupply() != null && newToken.getPrecision() != null) {
                    activeTokens.put(newToken.getContractAddress(), newToken);
                }
            }

            BlToken token = activeTokens.get(receiptEvent.getContract_address());
            if (token == null) {
                continue;
            }
            if (tokenTransactionService.selectByTrxIdAndEventSeq(txId, k) != null) {
                continue;
            }
            if (!"Transfer".equals(receiptEvent.getEvent_name())) {
                continue;
            }
            // token Transfer事件
            try {
                JSONObject transferArg = JSON.parseObject(receiptEvent.getEvent_arg());
                String transferFrom = transferArg.getString("from");
                String transferTo = transferArg.getString("to");
                String transferMemo = transferArg.getString("memo");
                String transferAmountStr = transferArg.getString("amount");
                if (transferFrom != null && transferFrom.isEmpty()) {
                    transferFrom = null;
                }
                if (transferTo != null && transferTo.isEmpty()) {
                    transferTo = null;
                }
                if (transferMemo == null) {
                    transferMemo = "";
                }
                if (transferAmountStr == null) {
                    continue;
                }
                long transferAmount = Long.parseLong(transferAmountStr);
                if (transferAmount < 0) {
                    continue;
                }
                BlTokenTransaction blTokenTransaction = new BlTokenTransaction();
                blTokenTransaction.setTrxId(txId);
                blTokenTransaction.setEventSeq(k);
                blTokenTransaction.setAmount(transferAmount);
                blTokenTransaction.setFee(trx.getFee() != null ? trx.getFee().longValue() : 0L);
                blTokenTransaction.setBlockId(trx.getBlockId());
                blTokenTransaction.setBlockNum(blockNum.intValue());
                blTokenTransaction.setContractId(token.getContractAddress());
                blTokenTransaction.setSymbol(token.getTokenSymbol());
                blTokenTransaction.setTrxTime(trx.getTrxTime());
                blTokenTransaction.setFromAccount(transferFrom);
                blTokenTransaction.setToAccount(transferTo);
                blTokenTransaction.setMemo(transferMemo);
                tokenTransactionService.insert(blTokenTransaction);

                // update token balances
                try {
                    if (token.getPrecision() != null) {
                        if (transferFrom != null && transferFrom.length() > 10) {
                            String fromBalanceStr = requestWalletService.invokeContractOffline(requestWalletService.getWalletRpcCaller(),
                                    token.getContractAddress(), "balanceOf", transferFrom);
                            if (fromBalanceStr != null && fromBalanceStr.length() > 2 && fromBalanceStr.startsWith("\"") && fromBalanceStr.endsWith("\"")) {
                                fromBalanceStr = fromBalanceStr.substring(1, fromBalanceStr.length() - 1);
                            }
                            BigDecimal fromBalance = PrecisionUtils.fullAmountToDecimal(new BigInteger(fromBalanceStr), token.getPrecision());
                            // update/insert balances to db
                            tokenBalanceService.updateOrInsertTokenBalance(transferFrom, token.getContractAddress(), token.getTokenSymbol(), fromBalance);
                        }
                        if (transferTo != null && transferTo.length() > 10) {
                            String toBalanceStr = requestWalletService.invokeContractOffline(requestWalletService.getWalletRpcCaller(),
                                    token.getContractAddress(), "balanceOf", transferTo);
                            if (toBalanceStr != null && toBalanceStr.length() > 2 && toBalanceStr.startsWith("\"") && toBalanceStr.endsWith("\"")) {
                                toBalanceStr = toBalanceStr.substring(1, toBalanceStr.length() - 1);
                            }
                            BigDecimal toBalance = PrecisionUtils.fullAmountToDecimal(new BigInteger(toBalanceStr), token.getPrecision());
                            // update/insert balances to db
                            tokenBalanceService.updateOrInsertTokenBalance(transferTo, token.getContractAddress(), token.getTokenSymbol(), toBalance);
                        }
                    }
                } catch (Exception e) {
                    logger.error("query and update token balance error", e);
                }
            } catch (Exception e) {
                logger.error("parse transfer log of txid {} error", txId, e);
            }
        }
    }

    private LoadingCache<String, Map<String, BlSwapContract>> swapContractsCache = CacheBuilder.newBuilder()
            .maximumSize(1)
            .expireAfterWrite(Duration.ofSeconds(60))
            .build(new CacheLoader<String, Map<String, BlSwapContract>>() {
                @Override
                public Map<String, BlSwapContract> load(String s) throws Exception {
                    List<BlSwapContract> swapContracts = swapContractService.selectAllActive();
                    Map<String, BlSwapContract> map = new HashMap<>();
                    for (BlSwapContract swapContract : swapContracts) {
                        map.put(swapContract.getContractAddress(), swapContract);
                    }
                    return map;
                }
            });
    private String swapContractsCacheKey = "swapContractsKey";

    private Map<String, BlSwapContract> getSwapContractsMap() {
        try {
            return swapContractsCache.get(swapContractsCacheKey);
        } catch (ExecutionException e) {
            List<BlSwapContract> swapContracts = swapContractService.selectAllActive();
            Map<String, BlSwapContract> map = new HashMap<>();
            for (BlSwapContract swapContract : swapContracts) {
                map.put(swapContract.getContractAddress(), swapContract);
            }
            return map;
        }
    }

    @Resource
    private Map<String, ISwapEventPlugin> swapEventPluginMap;

    // 解析swap合约的单个event内容并插入swap_transaction
    private void syncSwapEventToDb(BlSwapContract swapContract, TxReceiptEvent receiptEvent, int eventSeq, String txId, int opNum, String blockId, Long blockNum, BlTransaction trx) {
        String eventName = receiptEvent.getEvent_name();
        String swapEventPluginName = "swapPlugin." + eventName;
        ISwapEventPlugin swapEventPlugin = swapEventPluginMap.get(swapEventPluginName);
        if (swapEventPlugin == null) {
            return;
        }
        BlSwapTransaction swapTx = swapEventPlugin.decodeSwapEvent(swapContract, receiptEvent, eventSeq, txId, trx, blockId);
        if (swapTx == null) {
            // 不是关注的event或者不是正确的event参数格式
            return;
        }
        swapTransactionService.insert(swapTx);
    }

    // 发现swap合约的相关事件，增加流水到swap_transaction
    private void syncSwapEventsToDb(List<TxReceiptEvent> txReceiptEvents, String txId, int opNum, String blockId, Long blockNum, BlTransaction trx) {
        Map<String, BlSwapContract> swapContractsMap = getSwapContractsMap();
        for (int k = 0; k < txReceiptEvents.size(); k++) {
            TxReceiptEvent receiptEvent = txReceiptEvents.get(k);
            String eventContractAddr = receiptEvent.getContract_address();
            // 检查是否已经插入过数据库
            if (swapTransactionService.selectByTrxIdAndOpNumAndEventSeq(txId, opNum, k) != null) {
                continue;
            }
            BlSwapContract swapContract = swapContractsMap.get(eventContractAddr);
            if (swapContract == null) {
                // 不是swap合约的event
                continue;
            }
            // 识别不同的swap的eventName,解析不同参数， 插入swap流水
            syncSwapEventToDb(swapContract, receiptEvent, k, txId, opNum, blockId, blockNum, trx);
        }
    }

    private void processContractTransaction(BlBlock bc, Long blockNum, List<BlTransaction> transactionList, String txId, Integer opType, JSONObject opJson, int opNum) {
        BlTransaction trx = contractTransaction(opJson, txId, opType);
        trx.setBlockId(bc.getBlockId());
        trx.setBlockNum(bc.getBlockNum());
        trx.setTrxTime(bc.getBlockTime());
        transactionList.add(trx);

        ContractTxReceipt txReceipt = trx.getReceipt();
        if (txReceipt != null) {
            if (txReceipt.getExec_succeed() != null && txReceipt.getExec_succeed()) {
                // 合约执行成功
                trx.setFail(false);
                // 添加合约events和balance changes到数据库
                List<TxReceiptEvent> txReceiptEvents = txReceipt.getEvents();

                syncTxEventsToDb(txReceiptEvents, txId, blockNum);
                syncReceiptTxContractBalanceChangesToDb(txReceipt, txId, blockNum);

                // 发现token合约的Transfer事件，增加流水到token_transaction
                syncTokenTransferEventsToDb(txReceiptEvents, txId, blockNum, trx);

                // 发现swap合约的相关事件，增加流水到swap_transaction
                syncSwapEventsToDb(txReceiptEvents, txId, opNum, bc.getBlockId(), blockNum, trx);
            } else {
                // 合约执行失败，标记交易状态
                trx.setFail(true);
            }
        }
    }

    /**
     * 同步块中的交易
     *
     * @param blockNum 块号
     */
    public void blockSync(Long blockNum) {

        if (activeTokens == null) {
            List<BlToken> tokens = blTokenMapper.selectAllActive();
            activeTokens = new ConcurrentHashMap<>();
            for (BlToken token : tokens) {
                activeTokens.put(token.getContractAddress(), token);
            }
        }

        // 获取队列数据，请求
        BlBlock bc = new BlBlock();
        List<BlTransaction> transactionList = new ArrayList<>();
        Integer trxNum = 0;
        try {

            String blockMsg = null;
            for (int i = 0; i < 3; i++) {
                blockMsg = requestWalletService.getBlockInfo(blockNum); // 获取块信息
                if (!StringUtil.isEmpty(blockMsg)) {
                    break;
                }
            }

            if (StringUtils.isEmpty(blockMsg)) {
                logger.error("empty blockMsg：{}", blockMsg);
                return;
            }

            logger.info("块：{}的数据：{}", blockNum, blockMsg);
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
            if (bc.getBlockId() == null) {
                logger.error("invalid block json:{}", blockMsg);
            }

            // 批量交易处理
            JSONArray trxIds = blockJson.getJSONArray("transaction_ids");
            trxNum = trxIds.size();

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
                                if (OperationUtils.isContractOp(opType)) {
                                    processContractTransaction(bc, blockNum, transactionList, txId, opType, json, j);
                                }

                                // 账户改名交易处理
                                if (Constant.NAME_TRANSFER_OPERATION == opType) {
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

            if (bc != null) {
                // 保存批量数据
                commonService.insertBatchBlockData(bc, transactionList, trxNum);
                // 保存合约信息
                commonService.insertBatchContractData();
            }

        }
    }

    /**
     * 账户改名交易信息解析
     *
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
        if ("XWCCZxWGXvncYcemJB52ZyreHAQDUtRshjmky".equals(contractId)) {
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
                String contractApi = jsa.getString("contract_api");
                contractTrx.setCalledAbi(contractApi);
                contractTrx.setAbiParams(jsa.getString("contract_arg"));
                if ("change_owner".equalsIgnoreCase(contractApi) || "init_token".equalsIgnoreCase(contractApi)) {
                    BlContractInfo contractInfo = new BlContractInfo();
                    // 合约id
                    contractInfo.setContractId(contractTrx.getContractId()); // 时间转换
                    String registerTime = jsa.getString("register_time");
                    Date date;
                    if (!StringUtils.isEmpty(registerTime)) {
                        date = DateUtil.parseDate(registerTime, "yyyy-MM-dd'T'HH:mm:ss");
                    } else {
                        date = DateUtil.parseDate(new Date(), "yyyy-MM-dd'T'HH:mm:ss");
                    }
                    contractInfo.setRegTime(new Date(date.getTime() + Constant.UTC_WAREHOUSING_TIME_INTERVAL * 60 * 60 * 1000L));
                    String contractMsg = requestWalletService.getContractInfo(contractTrx.getContractId());
                    if (!StringUtil.isEmpty(contractMsg) && contractMsg.startsWith("{") && contractMsg.startsWith("}")) {
                        JSONObject contractJson = JSON.parseObject(contractMsg);
                        if (null != contractJson) {
                            if (contractJson.containsKey("name")) {
                                contractInfo.setName(contractJson.getString("name"));
                            }
                            if (contractJson.containsKey("owner")) {
                                contractInfo.setOwner(contractJson.getString("owner"));
                            }
                            if (contractJson.containsKey("owner_address")) {
                                contractInfo.setOwnerAddress(contractJson.getString("owner_address"));
                            }
                            if (contractJson.containsKey("owner_name")) {
                                contractInfo.setOwnerName(contractJson.getString("owner_name"));
                            }
                            if (contractJson.containsKey("description")) {
                                contractInfo.setDescription(contractJson.getString("description"));
                            }
                            if (contractJson.containsKey("registered_block")) {
                                contractInfo.setBlockNum(contractJson.getLong("registered_block"));
                            }
                            if (contractJson.containsKey("code_printable")) {
                                JSONObject jsonObject = contractJson.getJSONObject("code_printable");
                                if (null != jsonObject) {
                                    JSONObject contract = new JSONObject();
                                    JSONArray abi = jsonObject.getJSONArray("abi");
                                    JSONArray events = jsonObject.getJSONArray("events");
                                    contract.put("abi", abi);
                                    contract.put("events", events);
                                    contractInfo.setCode(contract.toJSONString());
                                }
                            }

                        }
                    }
                    contractInfo.setStatus(Constant.TEMP_STATE);
                    // 添加到内存
                    realData.setRegisterContractInfo(contractInfo);
                }
            }
        }

        switch (contractTrx.getOpType()) {
            // 注册合约
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
                contractInfo.setRegTime(new Date(date.getTime() + Constant.UTC_WAREHOUSING_TIME_INTERVAL * 60 * 60 * 1000L));
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
        // get contract receipt
        List<ContractTxReceipt> txReceipts = requestWalletService.getContractTxReceipt(trxId);
        if (txReceipts != null && !txReceipts.isEmpty()) {
            // 目前只处理交易的第一个receipt。正常构造的合约交易只有一个op
            ContractTxReceipt txReceipt = txReceipts.get(0);
            contractTrx.setReceipt(txReceipt);
        }

        return contractTrx;
    }

    /**
     * decode common and crosschain tx
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
        // common transaction
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
        } else {// cross-chain tx
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
     * cross-chain withdraw tx decode
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
                // withdraw request
                withdrawTransaction.setFromAccount(json.getString("withdraw_account"));
                withdrawTransaction.setToAccount(json.getString("crosschain_account"));
                withdrawTransaction.setAmount(json.getBigDecimal("amount"));
                withdrawTransaction.setAssetId(json.getString("asset_id"));
                withdrawTransaction.setMemo(json.getString("memo"));
                withdrawTransaction.setExtension1(Constant.WITHDRAW_REQ);

                // cache withdraw request state
                redisService.putWithdrawStatus(trxId, Constant.WITHDRAW_REQ);
                // cache withdraw to addr
                redisService.putCrosschainAddr(trxId, withdrawTransaction.getToAccount());
                break;

            case Constant.TRX_TYPE_WITHDRAW_CREATE:
                // withdraw tx create
                // origin withdraw txid collection
                JSONArray withdrawTxId = json.getJSONArray("ccw_trx_ids");
                if (withdrawTxId != null && withdrawTxId.size() > 0) {
                    withdrawTransaction.setExtraTrxId(json.getJSONArray("ccw_trx_ids").toJSONString());

                    updateWithdrawStatus(withdrawTxId, Constant.WITHDRAW_CREATE);

                    // cache created withdraw txid
                    redisService.putUnSignTxId(trxId, withdrawTxId.toJSONString());
                }
                withdrawTransaction.setExtension(LengthCheck(json.toJSONString()));

                break;

            case Constant.TRX_TYPE_WITHDRAW_SIGN:
                // withdraw tx sign
                // create withdraw tx id
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
                // broadcast withdraw tx
                // created withdraw tx id
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
                // withdraw tx success
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
     * decode other tx types
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

        // register
        if (Constant.ACCOUNT_REGISTER == opType) {
            blTransaction.setParentOpType(Constant.PARENT_OTHER);
            blTransaction.setFromAccount(json.getString("payer"));
            blTransaction.setExtension(json.getString("name"));

            redisService.putAccountName(json.getString("payer"), json.getString("name"));
            redisService.putAccountAddr(json.getString("name"), json.getString("payer"));
        }

        // mortgage
        if (Constant.ASSET_MORGAGE == opType) {
            blTransaction.setParentOpType(Constant.PARENT_MORTGAGE);
            blTransaction.setFromAccount(json.getString("lock_balance_addr"));
            JSONObject jsonObject = getMinerAddr(json.getString("lockto_miner_account"));
            if (jsonObject != null) {
                blTransaction.setToAccount(jsonObject.getString("addr"));
                blTransaction.setExtension(jsonObject.getString("name"));

                // update weight of miner in memory
                updateMinerWeight(jsonObject.getString("addr"));
            }

            blTransaction.setAssetId(json.getString("lock_asset_id"));
            blTransaction.setAmount(json.getBigDecimal("lock_asset_amount"));
            blTransaction.setFee(json.getJSONObject("fee").getBigDecimal("amount"));


        }

        // foreclose
        if (Constant.ASSET_REDEEM == opType) {
            blTransaction.setParentOpType(Constant.PARENT_FORECLOSE);
            JSONObject jsonObject = getMinerAddr(json.getString("foreclose_miner_account"));
            if (jsonObject != null) {
                blTransaction.setFromAccount(jsonObject.getString("addr"));
                blTransaction.setExtension(jsonObject.getString("name"));

                //更新内存中miner权重
                updateMinerWeight(jsonObject.getString("addr"));
            }

            blTransaction.setToAccount(json.getString("foreclose_addr"));
            blTransaction.setAssetId(json.getString("foreclose_asset_id"));
            blTransaction.setAmount(json.getBigDecimal("foreclose_asset_amount"));
            blTransaction.setFee(json.getJSONObject("fee").getBigDecimal("amount"));


        }

        // account bind and unbind
        if (Constant.BINDING == opType || Constant.UNBINDING == opType) {
            blTransaction.setParentOpType(Constant.PARENT_OTHER);
            blTransaction.setFromAccount(json.getString("addr"));
        }

        // payback
        if (Constant.TRX_TYPE_PAY_BACK == opType) {
            blTransaction.setParentOpType(Constant.PARENT_WAGE);
            blTransaction.setToAccount(json.getString("pay_back_owner"));

            JSONArray jsonArray = json.getJSONArray("pay_back_balance");
            BigDecimal reward = BigDecimal.ZERO;
            if (jsonArray != null && jsonArray.size() > 0) {

                for (int i = 0; i < jsonArray.size(); i++) {
                    reward = reward.add(jsonArray.getJSONArray(i).getJSONObject(1).getBigDecimal("amount"));
                }
            }
            blTransaction.setAmount(reward);
        }

        return blTransaction;
    }

    /**
     * update withdraw request state
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
     * filter message length
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

    private void updateMinerWeight(String adddress) {
        String accountName = redisService.getAccountName(adddress);
        if (accountName == null) {
            return;
        }
        String minerInfo = requestWalletService.getMiner(accountName);
        if (StringUtils.isEmpty(minerInfo)) {
            return;
        }
        JSONObject minerObject = JSONObject.parseObject(minerInfo);
        Map<String, BigDecimal> weightMap = minerWeightsInit.getWeightMap();
        weightMap.put(adddress, minerObject.getBigDecimal("pledge_weight"));
    }

    private JSONObject getMinerAddr(String minerId) {
        return redisService.getMinerInfo(minerId);
    }

}
