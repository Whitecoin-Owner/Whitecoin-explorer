package com.browser.dao.entity;

/**
 */
public class ResultMsg {

    // success
    public static final int HTTP_OK = 200;

    // server error
    public static final int HTTP_ERROR = 500;

    // login fail
    public static final int HTTP_REQUEST_VALID = 400;

    // params invalid
    public static final int HTTP_CHECK_VALID = 300;

    /**
     * error code
     */
    private int retCode;

    /**
     * error message
     */
    private String retMsg;

    private int version;

    /**
     * result data
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
