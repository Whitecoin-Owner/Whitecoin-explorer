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
public class CitizenWeightsInit implements InitializingBean {

    private static Logger logger = LoggerFactory.getLogger(CitizenWeightsInit.class);

    @Autowired
    private RequestWalletService requestWalletService;

    @Autowired
    private RedisService redisService;

    @Value("${citizen.number}")
    private Long citizens;

    // citizen权重
    private Map<String,BigDecimal> weightMap =new ConcurrentHashMap<>();


    public void syncCitizenData() {
        logger.info("启动citizen权重初始化线程");

        JSONArray result = requestWalletService.getAllCitizen(citizens);
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
                logger.info("citizen权重初始化线程结束");
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
                    String citizenInfo = requestWalletService.getMiner(name);
                    if(StringUtils.isEmpty(citizenInfo)){
                        continue;
                    }
                    JSONObject accountObject =JSONObject.parseObject(accountInfo);
                    JSONObject citizenObject =JSONObject.parseObject(citizenInfo);

                    weightMap.put(accountObject.getString("addr"),citizenObject.getBigDecimal("pledge_weight"));
//                    logger.info("内存数据：{}",accountObject.getString("addr")+":"+citizenObject.getBigDecimal("pledge_weight"));

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
        Thread t1 = new Thread(this::syncCitizenData);
        t1.setName("citizen权重初始化");
//        t1.setDaemon(true);
        t1.start();
    }

    public Map<String,BigDecimal> getWeightMap(){
        return  weightMap;
    }

}
