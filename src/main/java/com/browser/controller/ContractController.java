package com.browser.controller;

import com.alibaba.fastjson.JSONObject;
import com.browser.dao.entity.*;
import com.browser.protocol.EUDataGridResult;
import com.browser.service.StatisService;
import com.browser.service.SwapContractService;
import com.browser.service.TokenBalanceService;
import com.browser.service.TokenService;
import com.browser.service.impl.ContractService;
import com.browser.service.impl.RequestWalletService;
import com.browser.tools.common.HttpUtil;
import com.browser.tools.common.StringUtil;
import com.browser.wallet.PrecisionUtils;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 合约信息处理入口
 * Created by Administrator on 2017/12/8 0008.
 */
@Controller
public class ContractController {

    @Autowired
    private ContractService contractService;

    @Autowired
    private StatisService statisService;

    @Autowired
    private TokenService tokenService;
    @Autowired
    private SwapContractService swapContractService;
    @Autowired
    private RequestWalletService requestWalletService;
    @Autowired
    private TokenBalanceService tokenBalanceService;

    @Value("${swap_contract.admin.password}")
    private String swapContractAdminPassword;

    @Value("${img.url}")
    private String imgUrl;

    private static Logger logger = LoggerFactory.getLogger(ContractController.class);

    @ResponseBody
    @GetMapping("add_swap_contract/{contractAddress}/{token1}/{token2}/{password}")
    public String addSwapContract(@PathVariable("contractAddress") String contractAddress,
                                  @PathVariable("token1") String token1, @PathVariable("token2") String token2,
                                  @PathVariable("password") String password) {
        if (StringUtil.isEmpty(contractAddress)) {
            return "invalid contract address";
        }
        if (StringUtil.isEmpty(token1)) {
            return "invalid token1";
        }
        if (StringUtil.isEmpty(token2)) {
            return "invalid token2";
        }
        if (token1.equals(token2)) {
            return "token1 should not be equal to token2";
        }
        if (StringUtil.isEmpty(password)) {
            return "empty password";
        }
        if (!password.equals(swapContractAdminPassword)) {
            return "invalid password";
        }
        contractAddress = contractAddress.trim();
        token1 = token1.trim();
        token2 = token2.trim();
        BlSwapContract swapContract = swapContractService.selectByContractAddress(contractAddress);
        if (swapContract != null) {
            return "added before";
        }
        swapContract = new BlSwapContract();
        swapContract.setContractAddress(contractAddress);
        swapContract.setToken1(token1);
        swapContract.setToken2(token2);
        swapContract.setVerified(true);
        swapContractService.insert(swapContract);
        return "added";
    }

    @ResponseBody
    @RequestMapping(value = "queryContractList", method = {RequestMethod.POST})
    public ResultMsg queryContractList(@RequestBody BlContractInfo contractInfo) {
        ResultMsg resultMsg = new ResultMsg();
        try {
            EUDataGridResult data = contractService.getContractList(contractInfo);
            resultMsg.setRetCode(ResultMsg.HTTP_OK);
            resultMsg.setData(data);
        } catch (Exception e) {
            logger.error("system error", e);
            resultMsg.setRetCode(ResultMsg.HTTP_ERROR);
            resultMsg.setRetMsg(e.getMessage());
        }
        return resultMsg;
    }

    @ResponseBody
    @RequestMapping(value = "getContractTrxList", method = {RequestMethod.POST})
    public ResultMsg getContractTrxList(@RequestBody BlTransaction transaction) {
        ResultMsg resultMsg = new ResultMsg();
        if (null == transaction.getContractId()) {
            resultMsg.setRetCode(ResultMsg.HTTP_CHECK_VALID);
            resultMsg.setRetMsg("param can't be empty");
            return resultMsg;
        }
        try {
            EUDataGridResult data = contractService.getContractTrxList(transaction);
            resultMsg.setRetCode(ResultMsg.HTTP_OK);
            resultMsg.setData(data);
        } catch (Exception e) {
            logger.error("system error", e);
            resultMsg.setRetCode(ResultMsg.HTTP_ERROR);
            resultMsg.setRetMsg(e.getMessage());
        }
        return resultMsg;
    }

    @ResponseBody
    @RequestMapping(value = "getAbi", method = {RequestMethod.POST})
    public ResultMsg getAbi(@RequestBody BlContractInfo contractInfo) {
        ResultMsg resultMsg = new ResultMsg();
        if (null == contractInfo.getContractId()) {
            resultMsg.setRetCode(ResultMsg.HTTP_CHECK_VALID);
            resultMsg.setRetMsg("param can't be empty");
            return resultMsg;
        }
        try {
            JSONObject data = contractService.getAbi(contractInfo);
            resultMsg.setRetCode(ResultMsg.HTTP_OK);
            resultMsg.setData(data);
        } catch (Exception e) {
            logger.error("system error", e);
            resultMsg.setRetCode(ResultMsg.HTTP_ERROR);
            resultMsg.setRetMsg(e.getMessage());
        }
        return resultMsg;
    }

    @ResponseBody
    @RequestMapping(value = "getEvents", method = {RequestMethod.POST})
    public ResultMsg getEvents(@RequestBody BlContractInfo contractInfo) {
        ResultMsg resultMsg = new ResultMsg();
        if (null == contractInfo.getContractId()) {
            resultMsg.setRetCode(ResultMsg.HTTP_CHECK_VALID);
            resultMsg.setRetMsg("param can't be empty");
            return resultMsg;
        }
        try {
            JSONObject data = contractService.getEvents(contractInfo);
            resultMsg.setRetCode(ResultMsg.HTTP_OK);
            resultMsg.setData(data);
        } catch (Exception e) {
            logger.error("system error", e);
            resultMsg.setRetCode(ResultMsg.HTTP_ERROR);
            resultMsg.setRetMsg(e.getMessage());
        }
        return resultMsg;
    }

    @ResponseBody
    @RequestMapping(value = "getContractStatis", method = {RequestMethod.POST})
    public ResultMsg getContractTrxList(@RequestBody BlContractInfo contractInfo) {
        ResultMsg resultMsg = new ResultMsg();
        if (null == contractInfo.getContractId()) {
            resultMsg.setRetCode(ResultMsg.HTTP_CHECK_VALID);
            resultMsg.setRetMsg("param can't be empty");
            return resultMsg;
        }
        try {
            BlContractDetail data = statisService.getContractsStatis(contractInfo);
            if (data != null) {
                BlToken token = tokenService.selectByContractAddress(contractInfo.getContractId());
                data.setTokenContract(token);
            }
            resultMsg.setRetCode(ResultMsg.HTTP_OK);
            resultMsg.setData(data);
        } catch (Exception e) {
            logger.error("system error", e);
            resultMsg.setRetCode(ResultMsg.HTTP_ERROR);
            resultMsg.setRetMsg(e.getMessage());
        }
        return resultMsg;
    }

    @Data
    public static class TokenBalanceVo extends BlToken {
        private String address;
        private BigDecimal balance;

        public static TokenBalanceVo fromToken(BlToken token) {
            TokenBalanceVo item = new TokenBalanceVo();
            BeanUtils.copyProperties(token, item);
            return item;
        }
    }

    @ResponseBody
    @RequestMapping(value = "user_tokens/{addr}", method = {RequestMethod.GET})
    public ResultMsg getUserTokenBalances(@PathVariable("addr") String addr) {
        ResultMsg resultMsg = new ResultMsg();
        resultMsg.setRetCode(ResultMsg.HTTP_OK);
        NewBalance data = new NewBalance();


        BlMinerStatis blMinerStatis = new BlMinerStatis();
        blMinerStatis.setAddress(addr);

        try {

            if (StringUtil.isEmpty(addr)) {
                resultMsg.setData(new ArrayList<>());
                return resultMsg;
            }
            List<BlTokenBalance> tokenBalances = tokenBalanceService.findAllTokenBalancesByAddr(addr);
            BlMinerStatis balanceData = statisService.getAddrStatis(blMinerStatis);
            List<String> balanceList = new ArrayList<>();
            if (null != balanceData) {
                balanceList = balanceData.getBalances();
            }
            if (!CollectionUtils.isEmpty(balanceList)) {
                List<String> newBalanceList = balanceList.stream().filter(item -> item.contains("XWC")).collect(Collectors.toList());
                balanceData.setBalances(newBalanceList);
            }
            data.setXwcBanlance(balanceData);

            if (!CollectionUtils.isEmpty(balanceList)) {

                Iterator<String> it = balanceList.iterator();

                while (it.hasNext()) {
                    String typeBalance = it.next();

                    if (typeBalance.contains("XWC")) {
                        it.remove();
                    }
                }

                if (!CollectionUtils.isEmpty(balanceList)) {
                    for (String typeBalance : balanceList) {

                        BlTokenBalance blTokenBalance = new BlTokenBalance();
                        String[] chars = typeBalance.split(" ");

                        blTokenBalance.setTokenSymbol(chars[1]);
                        blTokenBalance.setAmount(new BigDecimal(chars[0]));
                        blTokenBalance.setAssetId(chars[2]);
                        tokenBalances.add(blTokenBalance);

                    }
                }

            }

            List<String> contractAddrList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(tokenBalances)) {
                for (BlTokenBalance blTokenBalance : tokenBalances) {
                    logger.info("blTokenBalance.contractAddr:{}", blTokenBalance.getTokenContract() + " " + blTokenBalance.getTokenSymbol());
                    if (!StringUtils.isEmpty(blTokenBalance.getTokenContract())) {
                        contractAddrList.add(blTokenBalance.getTokenContract());
                    } else {
                        contractAddrList.add(blTokenBalance.getAssetId());
                    }

                }

                String httpStr = HttpUtil.post(imgUrl + "/lightwallet/thirdParty/getLogoUrl", JSONObject.toJSONString(contractAddrList));
                logger.info("图片地址请求接口地址:{}", imgUrl + "/lightwallet/thirdParty/getLogoUrl");

                logger.info("图片接口响应:{}", httpStr);
                if (!StringUtils.isEmpty(httpStr)) {
                    Map<String, Object> resMap = (Map) JSONObject.parse(httpStr);
                    Map<String, String> urlMap = (Map) resMap.get("data");

                    if (null != urlMap) {
                        for (BlTokenBalance blTokenBalance : tokenBalances) {
                            if (StringUtils.isEmpty(blTokenBalance.getAddr())) {
                                blTokenBalance.setImgUrl(urlMap.get(blTokenBalance.getAssetId()));
                            } else {
                                blTokenBalance.setImgUrl(urlMap.get(blTokenBalance.getTokenContract()));
                            }

                        }
                    }

                }
            }


            data.setTokenBalances(tokenBalances);
            resultMsg.setRetCode(ResultMsg.HTTP_OK);
            resultMsg.setData(data);
        } catch (Exception e) {
            logger.error("system error", e);
            resultMsg.setRetCode(ResultMsg.HTTP_ERROR);
            resultMsg.setRetMsg(e.getMessage());
        }

        return resultMsg;

    }

    @ResponseBody
    @RequestMapping(value = "token_holders/{tokenContract}", method = {RequestMethod.GET})
    public ResultMsg getTokenHolders(@PathVariable("tokenContract") String tokenContract) {
        ResultMsg resultMsg = new ResultMsg();
        resultMsg.setRetCode(ResultMsg.HTTP_OK);
        try {
            if (StringUtil.isEmpty(tokenContract)) {
                resultMsg.setData(new ArrayList<>());
            } else {
                List<BlTokenBalance> tokenBalances = tokenBalanceService.findAllTokenBalancesByTokenContract(tokenContract);
                resultMsg.setData(tokenBalances);
            }
        } catch (Exception e) {
            logger.error("system error", e);
            resultMsg.setRetCode(ResultMsg.HTTP_ERROR);
            resultMsg.setRetMsg(e.getMessage());
        }
        return resultMsg;
    }

    @ResponseBody
    @RequestMapping(value = "top_tokens", method = RequestMethod.GET)
    public ResultMsg getTopTokens(HttpServletRequest request) {
        ResultMsg resultMsg = new ResultMsg();
        resultMsg.setRetCode(ResultMsg.HTTP_OK);
        List<BlToken> topActiveTokens = tokenService.selectAllTopActiveTokenList();
        String address = request.getParameter("address");
        if (StringUtil.isEmpty(address)) {
            resultMsg.setData(topActiveTokens);
        } else {
            List<TokenBalanceVo> tokenBalances = new ArrayList<>();
            for (BlToken token : topActiveTokens) {
                TokenBalanceVo balanceItem = TokenBalanceVo.fromToken(token);
                balanceItem.setAddress(address);
                balanceItem.setBalance(BigDecimal.ZERO);
                // TODO: query balance use cache
                // query balance
                try {
                    String res = requestWalletService.invokeContractOffline(requestWalletService.getWalletRpcCaller(), token.getContractAddress(), "balanceOf", address);
                    if (res.length() > 2 && res.startsWith("\"") && res.endsWith("\"")) {
                        res = res.substring(1, res.length() - 1); // 如果返回包含双引号，去掉
                    }
                    BigInteger fullBalance = new BigInteger(res);
                    if (fullBalance.compareTo(BigInteger.ZERO) < 0) {
                        fullBalance = BigInteger.ZERO;
                    }
                    BigDecimal balance = PrecisionUtils.fullAmountToDecimal(fullBalance, token.getPrecision());
                    balanceItem.setBalance(balance);
                } catch (Exception e) {

                }
                tokenBalances.add(balanceItem);
            }
            resultMsg.setData(tokenBalances);
        }
        return resultMsg;
    }

    /**
     * 通过合约地址查询用户代币余额信息列表
     *
     * @param contractAddress 合约地址
     * @return 用户代币余额信息列表
     */
    @ResponseBody
    @RequestMapping(value = "token_contract_balances", method = {RequestMethod.GET})
    public ResultMsg getTokenContractBalances(@RequestParam("contractAddress") String contractAddress,
                                              @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                              @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        ResultMsg resultMsg = new ResultMsg();
        resultMsg.setRetCode(ResultMsg.HTTP_OK);
        try {
            Map<String, Object> map = new HashMap<>();
            List<BlTokenBalance> blTokenBalanceList = tokenBalanceService.getBlTokenBalanceListByContractAddress(contractAddress);
            if (!CollectionUtils.isEmpty(blTokenBalanceList)) {
                blTokenBalanceList.forEach(item -> {
                    BigDecimal amount = item.getAmount();
                    if (null == amount) {
                        item.setTokenAmount("0");
                    } else {
                        item.setTokenAmount(amount.setScale(8, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString());
                    }
                    item.setAmount(null);
                });
                if (null != pageNum && null != pageSize) {
                    if (pageNum < 1) {
                        pageNum = 1;
                    }
                    map.put("total", blTokenBalanceList.size());
                    blTokenBalanceList = blTokenBalanceList.stream()
                            .skip(pageSize * (pageNum - 1)).limit(pageSize)
                            .collect(Collectors.toList());
                }
            }
            map.put("rows", blTokenBalanceList);
            resultMsg.setData(map);
        } catch (Exception e) {
            logger.error("system error", e);
            resultMsg.setRetCode(ResultMsg.HTTP_ERROR);
            resultMsg.setRetMsg(e.getMessage());
        }
        return resultMsg;
    }


//    public static void main(String[] args) {
//
//
//    }

}
