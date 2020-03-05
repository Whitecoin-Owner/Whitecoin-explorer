package com.browser.task;

import com.alibaba.fastjson.JSONObject;

/**
 * 扫描区块时一些插件，用来加强扫描区块功能，比如拆分出一些扫描的业务逻辑
 */
public interface ISyncPlugin {
    String pluginName();

    void applyOperation(JSONObject block, String txid, int opNum, int opType, String opTypeName, JSONObject opJSON, JSONObject receipt);
}
