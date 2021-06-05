package com.browser.tools.common;

import com.alibaba.fastjson.JSONObject;
import com.browser.dao.entity.BlBlock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;


public class StringUtil {

    private static final Logger logger = LoggerFactory.getLogger(StringUtil.class);

    public static String mapToStr(List<Map<?, ?>> params, String sign) {
        StringBuffer result = new StringBuffer();

        if (null != params && params.size() > 0) {
            for (Map<?, ?> map : params) {
                result.append(mapToStr(map, sign));
            }
        }

        return result.toString();
    }

    public static String mapToStr(Map<?, ?> params, String sign) {
        StringBuffer result = new StringBuffer();
        int sum = 1;
        for (Map.Entry<?, ?> entry : params.entrySet()) {
            if (!"".equals(entry.getValue()) && null != entry.getValue()) {
                if (sum == params.size()) {
                    result.append(entry.getValue());
                } else {
                    result.append(entry.getValue()).append(sign);
                }
            } else {
                if (sum == params.size()) {
                    result.append("");
                } else {
                    result.append("").append(sign);
                }
            }
            sum++;
        }
        return result.toString();
    }

    public static String beanToStr(List<?> params, String sign) throws Exception {
        StringBuffer result = new StringBuffer();

        if (null != params && params.size() > 0) {
            for (int i = 0; i < params.size(); i++) {
                result.append(beanToStr(params.get(i), sign));
            }
        }

        return result.toString();
    }

    public static <T> String beanToStr(T t, String sign) throws Exception {
        StringBuffer result = new StringBuffer();

        // java 反射获取bean属性
        Field[] fields = t.getClass().getDeclaredFields();
        // 属性被声明为private的,需要将setAccessible设置为true,默认的值为false
        Field.setAccessible(fields, true);
        for (int j = 0; j < fields.length; j++) {
            if (null != fields[j].get(t)) {
                if (j == fields.length - 1) {
                    result.append(fields[j].get(t));
                } else {
                    result.append(fields[j].get(t)).append(sign);
                }
            } else {
                if (j == fields.length - 1) {
                    result.append("");
                } else {
                    result.append("").append(sign);
                }
            }
        }

        return result.toString();
    }

    public static String bulidFileName(String fileflowno, String type, String suffix) {
        StringBuffer result = new StringBuffer();
        result.append(fileflowno).append("_");
        result.append(DateUtil.parseStr(new Date(), DateUtil.C_TIMES_PATTON_DEFAULT)).append("_");
        result.append(type).append(suffix);
        return result.toString();
    }

    public static List<Map<String, Object>> strToMap(String[] title, String content) {
        List<Map<String, Object>> resultMap = new ArrayList<Map<String, Object>>();
        if (null != title && title.length > 0) {
            if (content.contains("|")) {
                String[] cont = content.split("\\|");
                if (title.length == cont.length) {
                    for (int i = 0; i < title.length; i++) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put(title[i], cont[i]);
                        resultMap.add(map);
                    }
                }
            }
        }
        return resultMap;
    }

    public static <T> List<T> strTobean(T t, String content) throws Exception {
        List<T> resultList = new ArrayList<T>();
        if (content.contains("|")) {
            String[] cont = content.split("\\|");
            for (int i = 0; i < cont.length; i++) {
                Field[] fields = t.getClass().getDeclaredFields();
                Field.setAccessible(fields, true);
                fields[i].set(t, cont[i]);
            }
            resultList.add(t);
        }
        return resultList;
    }

    public static String updToString(String spl, int sum, String value) {
        String[] ct = spl.split("\\|");
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < ct.length; i++) {
            if (i == sum) {
                ct[i] = value;
                sb.append(ct[i]).append("|");
            } else {
                sb.append(ct[i]).append("|");
            }
        }
        return sb.toString();
    }

    public static String getRatiso(int num, int sum) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(2);
        String result = numberFormat.format((float) num / (float) sum * 100);
        return result + "%";
    }


    public static String creatBrow(String source) {
        if (source != null && source.length() > 0) {
            return source.replaceAll("[\ud800\udc00-\udbff\udfff\ud800-\udfff]", "");
        } else {
            return source;
        }
    }

    public static String getTimestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }

    public static String getNonceStr(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFHIJKLMNOPSUVWXYZ";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public static String getUUIDStr() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static BlBlock getBlockInfo(JSONObject bc, JSONObject acc) {
        BlBlock block = new BlBlock();
        block.setBlockId(bc.getString("block_id"));
        block.setBlockNum(bc.getLong("number"));
        block.setCreatedTime(new Date());
        block.setNextHash(bc.getString("next_secret_hash"));
        block.setPrevious(bc.getString("previous"));
        block.setPrevHash(bc.getString("previous_secret"));
        block.setMinerName(acc.getString("name"));
        block.setMinerAddress(acc.getString("addr"));

        Date date = DateUtil.parseDate(bc.getString("timestamp"), "yyyy-MM-dd'T'HH:mm:ss");
        //utc时间转北京时间，解决链上时间的时间差问题
        block.setBlockTime(new Date(date.getTime() + 8 * 60 * 60 * 1000L));
        return block;
    }

    public static String fiveFee(Double fee) {
        DecimalFormat df = new DecimalFormat("###.00000");
        return df.format(fee);
    }

    public static BigDecimal div(BigDecimal b1, BigDecimal b2, int index) {
        return b1.divide(b2, index, RoundingMode.HALF_EVEN);
    }

    public static String getBlockchainBalance(String message) {
        JSONObject balance = JSONObject.parseObject(message);
        String fromAddress = null;
        if (balance != null) {
            fromAddress = balance.getJSONObject("condition").getJSONObject("data").getString("owner");
        }
        return fromAddress;
    }

    public static boolean isEmpty(String str) {
        if (str == null || "".equals(str)) {
            return true;
        }
        return false;
    }

//    public static void main(String[] args) {
//        Date date = DateUtil.parseDate("2021-05-26T09:17:24", "yyyy-MM-dd'T'HH:mm:ss");
//        //utc时间转北京时间
//        System.out.println(new Date(date.getTime() + 8 * 60 * 60 * 1000L));
//    }
}
