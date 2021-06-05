package com.browser.tools;

/**
 * redis key constants
 */
public class RedisKeyConstants {

    /**
     * coin price,a_b
     */
    public static final String LINK_PRICE_INFO = "link:price_info:";

    /**
     *  withdraw state
     */
    public static final String LINK_WITHDRAW_STATUS = "link:withdraw:txId:";
    
    /**
     * withdraw target addr
     */
    public static final String LINK_CROSSCHAIN_ADDR = "link:crosschain:addr:";

    /**
     * addr registered
     */
    public static final String LINK_REGISTER_ADDR = "link:register:addr:";
    /**
     * account name bind
     */
    public static final String LINK_REGISTER_NAME = "link:register:name:";

    /**
     * miner info
     */
    public static final String LINK_MINERINFO = "link:minerInfo";

    /**
     * created cross withdraw txid
     */
    public static final String LINK_CROSSCHAIN_TXID = "link:crosschain:txId:";
    
    /**
     * withdraw sign id
     */
    public static final String LINK_SIGN_TXID = "link:sign:txId:";
    
    /**
     * asset info
     */
    public static final String LINK_ASSET = "link:asset:";

    /**
     * cross chain asset info
     */
    public static final String LINK_CROSS_ASSET = "link:crossAsset:";

    /**
     * cross chain asset number
     */
    public static final String LINK_CROSS_ASSET_NUM = "link:crossAssetNum:";

    /**
     * current issued supply
     */
    public static final String LINK_CURRENT_ISSUE= "link:curIssue:";

    /**
     * cached senator
     */
    public static final String LINK_SENATOR = "link:senator:";
    /**
     * cached senator
     */
    public static final String LINK_SENATOR_NAME = "link:senator:name:";
    /**
     * cached senator
     */
    public static final String LINK_SENATOR_ADDRESS = "link:senator:address:";
    /**
     * cached current senator
     */
    public static final String LINK_SENATOR_CURRENT = "link:senator:current:";
    /**
     * cached history senator
     */
    public static final String LINK_SENATOR_PREVIOUS = "link:senator:previous:";

    /**
     * current total rewards
     */
    public static final String LINK_BLOCK_REWARDS = "link:block:rewards:";
    /**
     * current total transactions count
     */
    public static final String LINK_TRX_TRXNUMS = "link:trx:trxNums:";

}
