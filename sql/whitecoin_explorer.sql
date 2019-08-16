-- Adminer 4.7.1 MySQL dump

SET NAMES utf8;
SET time_zone = '+00:00';
SET foreign_key_checks = 0;
SET sql_mode = 'NO_AUTO_VALUE_ON_ZERO';

DROP TABLE IF EXISTS `bl_asset`;
CREATE TABLE `bl_asset` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `symbol` varchar(32) DEFAULT NULL COMMENT '资产标识',
  `asset_id` varchar(10) DEFAULT NULL COMMENT '资产id',
  `precision` bigint(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='多资产表';


DROP TABLE IF EXISTS `bl_block`;
CREATE TABLE `bl_block` (
  `block_num` bigint(32) NOT NULL COMMENT '快号',
  `block_id` varchar(64) DEFAULT NULL COMMENT '本块区块hash',
  `block_size` bigint(16) DEFAULT NULL COMMENT '块大小（字节）',
  `miner_name` varchar(64) DEFAULT NULL,
  `miner_address` varchar(64) DEFAULT NULL,
  `previous` varchar(64) DEFAULT NULL COMMENT '前一个块块id',
  `prev_hash` varchar(64) DEFAULT NULL COMMENT '上轮secret',
  `next_hash` varchar(64) DEFAULT NULL COMMENT '本轮secret的hash',
  `block_time` datetime DEFAULT NULL COMMENT '产快时间',
  `trx_count` int(10) DEFAULT NULL,
  `amount` bigint(32) DEFAULT NULL COMMENT '包含的交易总金额',
  `fee` decimal(32,5) DEFAULT NULL,
  `reward` decimal(32,5) DEFAULT NULL,
  `merkle_root` varchar(70) DEFAULT NULL,
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`block_num`),
  KEY `blockTime` (`block_time`) USING BTREE,
  KEY `block_block_num` (`block_num`) USING BTREE,
  KEY `miner_address` (`miner_address`) USING BTREE,
  KEY `reward` (`reward`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='区块链block表';


DROP TABLE IF EXISTS `bl_contract_balance`;
CREATE TABLE `bl_contract_balance` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `contract_id` varchar(64) DEFAULT NULL,
  `balance` decimal(30,8) DEFAULT NULL,
  `asset_id` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `bl_contract_info`;
CREATE TABLE `bl_contract_info` (
  `contract_id` varchar(64) NOT NULL COMMENT '合约ID\r\n',
  `name` varchar(64) DEFAULT '' COMMENT '合约名称',
  `owner` varchar(255) DEFAULT NULL COMMENT '合约拥有者公钥',
  `owner_address` varchar(255) DEFAULT NULL COMMENT '合约拥有者地址',
  `owner_name` varchar(255) DEFAULT NULL COMMENT '合约拥有者名称',
  `description` varchar(256) DEFAULT '' COMMENT '合约描述',
  `reg_time` datetime DEFAULT NULL COMMENT '注册时间',
  `status` int(3) unsigned DEFAULT '1' COMMENT '注册状态\r\n0 - 销毁\r\n1 - 临时\r\n2 - 永久',
  `code` text,
  `create_tx` varchar(64) DEFAULT NULL,
  `block_num` bigint(32) DEFAULT NULL,
  PRIMARY KEY (`contract_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `bl_guarantee`;
CREATE TABLE `bl_guarantee` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `guarantee_id` varchar(10) NOT NULL,
  `owner_addr` varchar(64) DEFAULT NULL,
  `asset_orign` varchar(10) DEFAULT NULL,
  `asset_target` varchar(10) DEFAULT NULL,
  `rate` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `bl_proposal`;
CREATE TABLE `bl_proposal` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `proposal_id` varchar(10) DEFAULT NULL COMMENT '提案id',
  `proposer_id` varchar(10) DEFAULT NULL COMMENT '发起人id',
  `proposer` varchar(32) DEFAULT NULL COMMENT '发起人',
  `block` bigint(10) DEFAULT NULL COMMENT '提案块高',
  `proposal_time` datetime DEFAULT NULL COMMENT '提案时间',
  `amonut` decimal(32,8) DEFAULT NULL COMMENT '提案金额',
  `status` int(2) DEFAULT NULL COMMENT '提案状态：1进行中；-1已结束',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `proposal_id` (`proposal_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='提案表';


DROP TABLE IF EXISTS `bl_proposal_content`;
CREATE TABLE `bl_proposal_content` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `proposal_id` varchar(10) DEFAULT NULL COMMENT '提案id',
  `referee_id` varchar(10) DEFAULT NULL COMMENT '推荐人id',
  `referee` varchar(255) DEFAULT NULL COMMENT '推荐人名',
  `replaced_id` varchar(10) DEFAULT NULL COMMENT '被替换人id',
  `replaced_person` varchar(20) DEFAULT NULL COMMENT '被替换人名',
  `vote_rate` decimal(10,4) DEFAULT NULL COMMENT '得票率',
  `status` int(2) DEFAULT NULL COMMENT '竞选状态：0进行中；1成功;-1失败',
  `address` varchar(40) DEFAULT NULL,
  `flag` int(2) DEFAULT NULL COMMENT '同步标记：1已同步；-1未同步',
  `symbol` int(2) DEFAULT NULL COMMENT '是否为白名单配置：1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='提案内容表';


DROP TABLE IF EXISTS `bl_proposal_info`;
CREATE TABLE `bl_proposal_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `referee_id` varchar(10) DEFAULT NULL COMMENT '推荐人id',
  `replaced_id` varchar(10) DEFAULT NULL COMMENT '被替换人id',
  `block` bigint(10) DEFAULT NULL COMMENT '提案块高',
  `block_time` datetime DEFAULT NULL COMMENT '提案时间',
  `create_time` datetime DEFAULT NULL,
  `status` int(2) DEFAULT NULL COMMENT '已同步区块信息标记;1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='获取提案区块信息表';


DROP TABLE IF EXISTS `bl_senator_config`;
CREATE TABLE `bl_senator_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `senator_name` varchar(20) DEFAULT NULL,
  `email` varchar(64) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='senator配置表';


DROP TABLE IF EXISTS `bl_senator_current`;
CREATE TABLE `bl_senator_current` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `senator_id` varchar(10) DEFAULT NULL,
  `senator_name` varchar(20) DEFAULT NULL,
  `lock_asset` decimal(32,8) DEFAULT NULL COMMENT '锁仓数量',
  `proposal_id` varchar(10) DEFAULT NULL COMMENT '提案id',
  `address` varchar(40) DEFAULT NULL COMMENT '地址',
  `pay_back` decimal(32,8) DEFAULT NULL COMMENT '待领取收益',
  `status` int(2) DEFAULT NULL COMMENT '锁仓释放状态：1是；-1否',
  `trx_id` varchar(40) DEFAULT NULL COMMENT '锁仓释放交易id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='当前senator表';


DROP TABLE IF EXISTS `bl_senator_previous`;
CREATE TABLE `bl_senator_previous` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `senator_id` varchar(10) DEFAULT NULL,
  `senator_name` varchar(20) DEFAULT NULL,
  `lock_asset` decimal(32,8) DEFAULT NULL COMMENT '锁仓数量',
  `proposal_id` varchar(10) DEFAULT NULL COMMENT '提案id',
  `address` varchar(40) DEFAULT NULL COMMENT '地址',
  `pay_back` decimal(10,8) DEFAULT NULL COMMENT '待领取收益',
  `status` int(2) DEFAULT NULL COMMENT '锁仓释放状态：1是；-1否',
  `trx_id` varchar(40) DEFAULT NULL COMMENT '锁仓释放交易id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='历届senator表';


DROP TABLE IF EXISTS `bl_senator_security`;
CREATE TABLE `bl_senator_security` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `senator_id` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `bl_transaction`;
CREATE TABLE `bl_transaction` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `trx_id` varchar(64) DEFAULT NULL COMMENT '交易ID',
  `block_id` varchar(64) DEFAULT NULL COMMENT '快号ID',
  `block_num` bigint(32) DEFAULT NULL COMMENT '块号',
  `from_account` varchar(64) DEFAULT NULL COMMENT '出账地址',
  `to_account` varchar(64) DEFAULT NULL COMMENT '入账地址',
  `miner_address` varchar(64) DEFAULT NULL,
  `symbol` varchar(10) DEFAULT NULL,
  `asset_id` varchar(10) DEFAULT NULL,
  `amount` decimal(32,8) DEFAULT NULL,
  `fee` decimal(32,8) DEFAULT NULL,
  `trx_time` datetime DEFAULT NULL COMMENT '交易被确认时间',
  `called_abi` text COMMENT '调用的合约函数，非合约交易该字段为空',
  `abi_params` text COMMENT '调用合约函数时传入的参数，非合约交易该字段为空',
  `extra_trx_id` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '结果交易id仅针对合约交易',
  `guarantee_id` varchar(10) DEFAULT NULL,
  `parent_op_type` int(2) DEFAULT NULL COMMENT '交易聚合类：1、转账类；2、合约类；3、充值类；4、提现类；5、工资类；6、手续费承兑单类；7、其他类',
  `op_type` int(5) DEFAULT '0' COMMENT '0 - 普通转账1 - 代理领工资2 - 注册账户3 - 注册代理10 - 注册合约11 - 合约充值12 - 合约升级',
  `gas_limit` int(11) DEFAULT NULL,
  `gas_cost` int(10) DEFAULT NULL,
  `gas_price` decimal(10,2) DEFAULT NULL,
  `extension` text,
  `memo` text,
  `contract_id` varchar(64) DEFAULT NULL COMMENT '如果是合约交易则记录合约ID',
  `created_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `extension1` int(2) DEFAULT NULL COMMENT '提现流程状态扩展字段，0、交易请求；1、交易创建；2、交易签名；3、交易广播；4、交易成功',
  PRIMARY KEY (`id`),
  KEY `trx_block_num` (`block_num`) USING BTREE,
  KEY `trx_contract` (`from_account`,`op_type`,`contract_id`) USING BTREE,
  KEY `trx_from` (`from_account`) USING BTREE,
  KEY `trx_to` (`to_account`) USING BTREE,
  KEY `trx_id` (`trx_id`),
  KEY `op_type` (`op_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='区块链交易表';


DROP TABLE IF EXISTS `bl_trx_statis`;
CREATE TABLE `bl_trx_statis` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `statis_time` varchar(20) DEFAULT NULL,
  `trx_num` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `wallet_asset1`;
CREATE TABLE `wallet_asset1` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '钱包地址表自增主键',
  `wallet_id` varchar(128) NOT NULL DEFAULT '' COMMENT '钱包唯一ID',
  `coin_addr` varchar(128) NOT NULL DEFAULT '' COMMENT '钱包货币地址',
  `coin_type` varchar(128) NOT NULL DEFAULT '' COMMENT '钱包货币地址类型，是哪种货币',
  `total_amt` decimal(30,18) NOT NULL DEFAULT '0.000000000000000000' COMMENT '该地址的可用总余额',
  `available_amt` decimal(30,18) DEFAULT NULL COMMENT '可用余额',
  `un_confirm_amt` decimal(30,18) NOT NULL DEFAULT '0.000000000000000000' COMMENT '待确认的总金额',
  `mortgage_amt` decimal(30,18) DEFAULT NULL COMMENT '抵押资产',
  `bak` varchar(100) DEFAULT '' COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '地址新建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '地址更新时间',
  `star` int(2) DEFAULT NULL,
  `asset_type` int(11) DEFAULT NULL,
  `nfc_number` varchar(128) DEFAULT NULL,
  `account_name` varchar(64) DEFAULT NULL COMMENT '账户名',
  `account_id` varchar(64) DEFAULT NULL COMMENT '账户id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `address_cointype` (`coin_addr`,`coin_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='钱包地址表';


-- 2019-08-06 07:32:15
