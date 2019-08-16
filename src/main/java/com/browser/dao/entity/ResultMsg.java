package com.browser.dao.entity;

/**
 * Created by mayakui on 2018/2/24 0024.
 * 返回前台的统一实体
 */
public class ResultMsg {

    //成功
    public static final int HTTP_OK = 200;

    //服务异常
    public static final int HTTP_ERROR = 500;

    //登陆失效
    public static final int HTTP_REQUEST_VALID = 400;

    //参数等校验失败
    public static final int HTTP_CHECK_VALID = 300;

    /**
     * 错误码
     */
    private int retCode;

    /**
     * 错误信息
     */
    private String retMsg;

    private int version;

    /**
     * 返回数据
     */
    private Object data;

    public int getRetCode() {
        return retCode;
    }

    public void setRetCode(int retCode) {
        this.retCode = retCode;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public static ResultMsg build(int retCode, String retMsg){
        ResultMsg result = new ResultMsg();
        result.setRetCode(retCode);
        result.setRetMsg(retMsg);

        return result;
    }
}
