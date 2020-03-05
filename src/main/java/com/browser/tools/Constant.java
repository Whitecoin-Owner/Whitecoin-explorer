package com.browser.tools;

/**
 * 常量类.
 * // TODO
 */
public class Constant {

	// 交易类型
	// 普通转账
	public static final int TRX_TYPE_TRANSFER = 0;

	//账户注册
	public static final int ACCOUNT_REGISTER = 5;
	//质押
	public static final int ASSET_MORGAGE= 55;
	//赎回
	public static final int ASSET_REDEEM = 56;

	//绑定
	public static final int BINDING = 10;

	//解绑
	public static final int UNBINDING = 11;

	public static final int TRX_TYPE_CROSS_TRANSFER = 60;
	//领取分红
	public static final int TRX_TYPE_PAY_BACK = 73;

	// 提现流程
	// 请求
	public static final int TRX_TYPE_WITHDRAW_REQ = 61;
	// 创建
	public static final int TRX_TYPE_WITHDRAW_CREATE = 62;
	// 签名
	public static final int TRX_TYPE_WITHDRAW_SIGN = 63;
	// 广播
	public static final int TRX_TYPE_WITHDRAW_SEND = 64;
	// 成功
	public static final int TRX_TYPE_WITHDRAW_SUCC = 65;

	// 提现状态
	public static final int WITHDRAW_REQ = 0;
	public static final int WITHDRAW_CREATE = 1;
	public static final int WITHDRAW_SIGN = 2;
	public static final int WITHDRAW_SEND = 3;
	public static final int WITHDRAW_SUCC = 4;
	
	//交易状态
	//成功
	public static final int TRX_STATUS = 1;
	// 合约相关
	// 注册合约
	public static final int CONTRACT_REGISTER_OPERATION = 76;
	// 升级合约
	public static final int CONTRACT_UPGRADE_OPERATION = 77;
	// 调用合约
	public static final int CONTRACT_INVOKE_OPERATION = 79;
	// 合约转账
	public static final int CONTRACT_TRANSFER_OPERATION = 81;
	// 合约销毁
	// public static final int TRX_TYPE_CALL_CONTRACT = 14;

	// 提案交易
	public static final int PROPOSAL_CREATE_OPERATION = 101;
	// 提案交易
	public static final int PROPOSAL_COMPETITION_OPERATION = 102;

	// 账户改名交易
	public static final int NAME_TRANSFER_OPERATION = 125;

	// 合约状态
	public static final int DESTROY_STATE = 0;
	public static final int TEMP_STATE = 1;
	public static final int FOREVER_STATE = 2;
	
	// 手续费承兑单类型
	public static final int GURANTEE_CREATE_OPERATION = 83;
	public static final int GURANTEE_CANCEL_OPERATION = 84;
	
	// 手续费承兑单状态
	public static final int GURANTEE_INVALID = 0;
	public static final int GURANTEE_VALID = 1;

	// 精度保留长度
	public static final int PRECISION_LENGTH = 4;
	public static final long PRECISION = 100000000L;
	public static final String SYMBOL = "XWC";
	public static final String SYMBOL_BTC = "BTC";

	// 批量处理个数
	public static final Integer BATCH_LENGTH = 10000;

	public static final int RECHARGE_TYPE = -999;
	
	public static final int MAX_LENGTH = 65535;
	
	//交易类型聚合
	//转账类
	public static final int PARENT_TRANSFER = 1;
	//合约类
	public static final int PARENT_CONTRACT = 2;
	//充值类
	public static final int PARENT_RECHARGE = 3;
	//提现类
	public static final int PARENT_WIHTDRAW = 4;
	//工资类
	public static final int PARENT_WAGE = 5;
	//手续费承兑单类
	public static final int PARENT_ACCEPTANCE = 6;
	//其他类
	public static final int PARENT_OTHER = 7;
	//质押
	public static final int PARENT_MORTGAGE = 8;
	//赎回
	public static final int PARENT_FORECLOSE = 9;

	//提案状态
	public static final int PROPOSAL_ING = 1;
	public static final int PROPOSAL_END = -1;

	//senator同步状态
	public static final int SYNC_ALREADY = 1;
	public static final int SYNC_NON = -1;

	//参选人员状态
	public static final int SENATOR_ING = 0;
	public static final int SENATOR_SUCC = 1;
	public static final int SENATOR_FAIL = -1;

	//锁仓状态
	public static final int LOCK_RELEASE = 1;
	public static final int LOCK_RELEASE_NOT = -1;

	//Senator安全员标识
	public static final int SENATOR_SAFE = 1;
}
