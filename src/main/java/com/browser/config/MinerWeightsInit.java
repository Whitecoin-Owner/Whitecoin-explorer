package com.browser.config;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.browser.service.impl.RedisService;
import com.browser.service.impl.RequestWalletService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 页面轮询请求的实施数据
 */
@Component
public class MinerWeightsInit implements InitializingBean {

    private static Logger logger = LoggerFactory.getLogger(MinerWeightsInit.class);

    @Autowired
    private RequestWalletService requestWalletService;

    @Autowired
    private RedisService redisService;

    @Value("${miner.number}")
    private Long miners;

    // miner权重
    private Map<String,BigDecimal> weightMap =new ConcurrentHashMap<>();


    public void syncMinerData() {
        logger.info("启动miner权重初始化线程");

        JSONArray result = requestWalletService.getAllMiners(miners);
        BlockingQueue<String> queue = new LinkedBlockingQueue<String>();
        for(int i=0;i<result.size();i++){
            try {
                //存入块号
                queue.put(result.getJSONArray(i).getString(0));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        ExecutorService exe = Executors.newFixedThreadPool(11);
        for (int i = 1; i <= 10; i++) {
            exe.execute(new SyncThread(queue));
        }
        exe.shutdown();
        while (true) {
            //阻塞本次任务并判断是否结束
            if (exe.isTerminated()) {
                logger.info("miner权重初始化线程结束");
                break;
            }
        }
    }

    private class SyncThread implements Runnable {
        BlockingQueue<String> queue;

        public SyncThread(BlockingQueue<String> queue) {
            this.queue = queue;
        }

        public void run() {
            String name=null;
            while(true){
                name=queue.poll();
                if(null!=name){
                    String accountInfo = requestWalletService.getAccount(name);
                    if(StringUtils.isEmpty(accountInfo)){
                        continue;
                    }
                    String minerInfo = requestWalletService.getMiner(name);
                    if(StringUtils.isEmpty(minerInfo)){
                        continue;
                    }
                    JSONObject accountObject =JSONObject.parseObject(accountInfo);
                    JSONObject minerObject =JSONObject.parseObject(minerInfo);

                    weightMap.put(accountObject.getString("addr"),minerObject.getBigDecimal("pledge_weight"));
//                    logger.info("内存数据：{}",accountObject.getString("addr")+":"+minerObject.getBigDecimal("pledge_weight"));

                }else {
                    break;
                }
            }
        }
    }
    /**
     * 启动加载
     */
    @Override
    public void afterPropertiesSet() {
        Thread t1 = new Thread(this::syncMinerData);
        t1.setName("miner权重初始化");
//        t1.setDaemon(true);
        t1.start();
    }

    public Map<String,BigDecimal> getWeightMap(){
        return  weightMap;
    }

}
