package com.browser.tools.common;

public class RpcLink {
	
	// 获取最新块号
	public static final String BLOCK_COUNT = "info";
	
	// 获取块信息接口
	public static final String BLOCK_INFO = "get_block";
	
	// 获取块交易信息
	public static final String BLOCK_TRX = "get_transaction";

	// 获取发行量
	public static final String GET_ASSET_IMP = "get_asset_imp";

	// 获取矿工信息
	public static final String GET_MINER = "get_miner";
	
	// 获取矿工地址
	public static final String GET_ACCOUNT = "get_account";
	
	// 地址余额
	public static final String ADDR_BALANCES = "get_addr_balances";

	// 根据地址获取账户名
	public static final String GET_ACCOUNT_BY_ADDR = "get_account_by_addr";

	// 质押资产
	public static final String LOCK_BALANCE = "get_account_lock_balance";

	// 待领取资产
	public static final String PAY_BACK_BALANCE = "get_address_pay_back_balance";

	// 资产信息
	public static final String ASSET_INFO = "get_asset";
	
	// 资产列表
	public static final String LIST_ASSETS = "list_assets";
	
	// 从某一块开始注册合约列表
	public static final String CONTRACT_REGISTERED = "get_contract_registered";
	
	// 合约余额
	public static final String CONTRACT_BALANCE = "get_contract_balance";	

	//获取合约信息
	public static final String CONTRACT_INFO = "get_contract_info";
	
	//获取手续费承兑单
	public static final String GET_GUARANTEE = "get_guarantee_order";

	//获取现任wallfacer
	public static final String LIST_SENATOR_MEMBERS = "list_wallfacer_members";

	//获取历届wallfacer
	public static final String LIST_ALL_SENATORS = "list_all_wallfacers";

	//获取进行中的提案
	public static final String GET_REFERENDUM_FOR_VOTER = "get_referendum_for_voter";

	//调合约获取锁仓数量
	public static final String INVOKE_CONTRACT_OFFLINE = "invoke_contract_offline";
	//查询所有miner
	public static final String LIST_MINERS = "list_miners";

	// 获取合约交易的receipt
	public static final String GET_CONTRACT_RECEIPT = "get_contract_invoke_object";

	// 获取合约的基本信息
	public static final String GET_SIMPLE_CONTRACT_INFO = "get_simple_contract_info";
}


