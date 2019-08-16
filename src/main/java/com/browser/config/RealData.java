package com.browser.config;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.browser.dao.entity.BlStatis;
import com.browser.service.StatisService;
import jdk.nashorn.internal.scripts.JS;
import net.sf.json.JSON;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.browser.dao.entity.BlAsset;
import com.browser.dao.entity.BlContractBalance;
import com.browser.dao.entity.BlContractInfo;
import com.browser.dao.mapper.BlAssetMapper;
import com.browser.service.impl.RedisService;
import com.browser.service.impl.RequestWalletService;
import com.browser.tools.Constant;

/**
 * 页面轮询请求的实施数据
 */
@Component
public class RealData implements InitializingBean {

    @Autowired
    private RequestWalletService requestWalletService;

    @Autowired
    private BlAssetMapper blAssetMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private StatisService statisService;

    @Value("${asset.num}")
    private Integer number;// 查询个数

    // 最新统计数据
    private String statisInfo;
    // 最新区块数据
    private String blockInfo;
    // 最新交易数据
    private String transactionInfo;

    // 注册合约
    private Set<BlContractInfo> registerContractInfo;

    // 更新的合约
    private Set<BlContractInfo> updateContractInfo;

    private List<BlContractBalance> updateContractBalance;

    private BlStatis blStatis;

    private JSONArray today;
    private JSONArray weekDay;
    private JSONArray monthDay;


    /**
     * 启动加载
     */
    @Override
    public void afterPropertiesSet() {
        registerContractInfo = new TreeSet<BlContractInfo>(contractInfoASC);
        updateContractInfo = new TreeSet<BlContractInfo>(contractInfoASC);
        updateContractBalance = new ArrayList<BlContractBalance>();
        refreshAssetInfo();

        //首页加载数据初始化
        IndexStatis();

        indexEcharts();
    }

    // 合约信息排序,块号升序
    Comparator<BlContractInfo> contractInfoASC = new Comparator<BlContractInfo>() {
        public int compare(BlContractInfo o1, BlContractInfo o2) {
            boolean flag = o1.getBlockNum() == o2.getBlockNum() && o1.getBlockNum() != 0;
            if (flag) {
                return 0;
            }
            int ret = o1.getBlockNum().compareTo(o2.getBlockNum());
            if (ret == 0) {
                return o1.getBlockNum().compareTo(o2.getBlockNum());
            } else {
                return ret;
            }
        }
    };

    /**
     * 更新合约信息
     *
     * @param tblContractInfo
     */
    public void setUpdateContractInfo(BlContractInfo tblContractInfo) {
        synchronized (updateContractInfo) {
            try {
                this.updateContractInfo.add(tblContractInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Set<BlContractInfo> getUpdateContractInfo() {
        return updateContractInfo;
    }

    /**
     * 新增注册合约信息
     *
     * @param tblContractInfo
     */
    public void setRegisterContractInfo(BlContractInfo tblContractInfo) {
        synchronized (registerContractInfo) {
            try {
                this.registerContractInfo.add(tblContractInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Set<BlContractInfo> getRegisterContractInfo() {
        return registerContractInfo;
    }

    /**
     * 更新合约余额
     *
     * @param
     */
    public List<BlContractBalance> getUpdateContractBalance() {
        return updateContractBalance;
    }

    public void setUpdateContractBalance(BlContractBalance updateContractBalance) {
        synchronized (updateContractBalance) {
            try {
                BlContractBalance balance = new BlContractBalance();
                balance.setAssetId(updateContractBalance.getAssetId());
                balance.setBalance(updateContractBalance.getBalance());
                balance.setContractId(updateContractBalance.getContractId());
                this.updateContractBalance.add(balance);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void refreshAssetInfo() {
        List<BlAsset> assetList = blAssetMapper.selectAll();
        if (assetList != null && assetList.size() > 0) {
            for (BlAsset asset : assetList) {
                redisService.putAsset(asset.getAssetId(), asset);
            }
            redisService.putCrossAsset(assetList);
        } else {
            getCrossAsset();

            List<BlAsset> listAsset = blAssetMapper.selectAll();
            redisService.putCrossAsset(listAsset);
        }
    }

    /**
     * 获取资产信息
     *
     * @param assetId
     * @return
     */
    public BlAsset getSymbolByAssetId(String assetId) {
        BlAsset asset = redisService.getAsset(assetId);
        // 为空从新查询接口
        if (asset == null) {
            getCrossAsset();
            asset = redisService.getAsset(assetId);
        }
        return asset;
    }

    /**
     * 获取链上资产信息
     *
     * @param
     * @return
     */
    private void getCrossAsset() {

        String result = requestWalletService.getAssetList(number);
        JSONArray jsa = JSONObject.parseArray(result);
        if (jsa != null && jsa.size() > 0) {
            blAssetMapper.delete();
            for (int i = 0; i < jsa.size(); i++) {
                String symb = jsa.getJSONObject(i).getString("symbol");
                if ("ERCPAX".equals(symb)) {
                    symb = "PAX";
                }
                if ("ERCELF".equals(symb)) {
                    symb = "ELF";
                }
                String id = jsa.getJSONObject(i).getString("id");
                Integer number = jsa.getJSONObject(i).getInteger("precision");
                Long precision = 0l;
                if (number != null) {
                    precision = calculation(number);
                }
                BlAsset blAsset = new BlAsset();
                blAsset.setAssetId(id);
                blAsset.setPrecision(precision);
                blAsset.setSymbol(symb);

                blAssetMapper.insert(blAsset);

                redisService.putAsset(blAsset.getAssetId(), blAsset);
            }
        }
    }

    /**
     * 清空存储数据
     */
    public void clear() {
        registerContractInfo = new TreeSet<BlContractInfo>(contractInfoASC);
        updateContractInfo = new TreeSet<BlContractInfo>(contractInfoASC);
        updateContractBalance = new ArrayList<BlContractBalance>();
    }

    private static Long calculation(Integer number) {
        Long result = 1l;
        for (int i = 0; i < number; i++) {
            result = result * 10l;
        }
        return result;
    }

    private void IndexStatis(){
        statisService.newXwcStatic();
    }

    private void indexEcharts(){
        statisService.indexEcharts();
    }


    public String getStatisInfo() {
        return statisInfo;
    }

    public void setStatisInfo(String statisInfo) {
        this.statisInfo = statisInfo;
    }

    public String getBlockInfo() {
        return blockInfo;
    }

    public void setBlockInfo(String blockInfo) {
        this.blockInfo = blockInfo;
    }

    public String getTransactionInfo() {
        return transactionInfo;
    }

    public void setTransactionInfo(String transactionInfo) {
        this.transactionInfo = transactionInfo;
    }

    public BlStatis getBlStatis() {
        return blStatis;
    }

    public void setBlStatis(BlStatis blStatis) {
        this.blStatis = blStatis;
    }

    public JSONArray getToday() {
        return today;
    }

    public void setToday(JSONArray today) {
        this.today = today;
    }

    public JSONArray getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(JSONArray weekDay) {
        this.weekDay = weekDay;
    }

    public JSONArray getMonthDay() {
        return monthDay;
    }

    public void setMonthDay(JSONArray monthDay) {
        this.monthDay = monthDay;
    }
}
