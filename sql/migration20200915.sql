ALTER TABLE `bl_transaction`
ADD COLUMN `fail` INT(1) NOT NULL DEFAULT 0 COMMENT '是否是失败交易' AFTER `extension1`;


CREATE TABLE `bl_tx_events` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `trx_id` VARCHAR(100) NOT NULL,
  `block_num` INT(11) NOT NULL,
  `op_num` INT NULL,
  `caller_addr` VARCHAR(100) NOT NULL,
  `contract_address` VARCHAR(100) NOT NULL,
  `event_name` VARCHAR(100) NOT NULL,
  `event_arg` TEXT NOT NULL,
  `event_seq` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `tx_events_idx_txid` (`trx_id` ASC),
  INDEX `tx_events_idx_contract_address` (`contract_address` ASC, `event_name` ASC),
  INDEX `tx_events_idx_event_name` (`event_name` ASC));

CREATE TABLE `bl_tx_contract_balance_change` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `trx_id` VARCHAR(100) NOT NULL,
  `block_num` INT(11) NOT NULL,
  `change_type` VARCHAR(100) NOT NULL COMMENT '在receipt中的变化字段',
  `address` VARCHAR(100) NOT NULL,
  `asset_id` VARCHAR(45) NOT NULL,
  `amount` BIGINT NOT NULL,
  `change_seq` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `tx_contract_balance_change_idx_txid` (`trx_id` ASC),
  INDEX `tx_contract_balance_change_idx_address` (`address` ASC))
COMMENT = '合约交易的原生资产变化';

CREATE TABLE `bl_token` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `contract_address` varchar(100) NOT NULL,
  `token_symbol` varchar(100) DEFAULT NULL,
  `precision` int(11) DEFAULT NULL,
  `creator_address` varchar(100) NOT NULL,
  `token_supply` decimal(36,18) DEFAULT NULL,
  `create_trx_id` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `token_idx_contract_address` (`contract_address`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='合约代币';


CREATE TABLE `bl_token_transaction` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `trx_id` varchar(100) NOT NULL,
  `block_id` varchar(100) NOT NULL,
  `block_num` int(11) NOT NULL,
  `from_account` varchar(100) DEFAULT NULL,
  `to_account` varchar(100) DEFAULT NULL,
  `symbol` varchar(100) NOT NULL,
  `amount` bigint(20) NOT NULL,
  `fee` bigint(20) NOT NULL,
  `trx_time` datetime NOT NULL,
  `contract_id` varchar(100) NOT NULL,
  `memo` varchar(100) DEFAULT NULL,
  `event_seq` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `token_tx_unique_trx_id_event_seq` (`trx_id`,`event_seq`),
  KEY `token_tx_idx_trx_id` (`trx_id`),
  KEY `token_tx_idx_from_account` (`from_account`),
  KEY `token_tx_idx_to_account` (`to_account`),
  KEY `token_tx_idx_contract_id` (`contract_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='token转账流水';

CREATE TABLE `bl_swap_contract` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `contract_address` VARCHAR(100) NOT NULL COMMENT 'swap合约地址',
  `token1` VARCHAR(100) NOT NULL COMMENT 'swap token1代币的代币符号或者合约地址',
  `token2` VARCHAR(100) NOT NULL COMMENT 'swap token2代币的代币符号或者合约地址',
  `verified` INT(1) NOT NULL COMMENT '是否被认证过',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `contract_address_UNIQUE` (`contract_address` ASC))
COMMENT = 'swap合约';

CREATE TABLE `bl_swap_transaction` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `trx_id` varchar(100) NOT NULL,
  `block_id` varchar(100) NOT NULL,
  `block_num` int(11) NOT NULL,
  `caller_address` varchar(100) NOT NULL COMMENT '合约调用人',
  `op_num` int(11) NOT NULL COMMENT 'operation在tx中的索引',
  `event_name` varchar(100) NOT NULL COMMENT 'event名称',
  `event_seq` int(11) NOT NULL COMMENT 'event在operation中的序号',
  `event_arg` text NULL COMMENT 'event参数',
  `contract_address` varchar(100) NOT NULL COMMENT 'event所属合约地址',
  `trx_time` datetime NOT NULL,
  `change` decimal(36,18) DEFAULT NULL COMMENT '事件中单个资产金额变化的变化数量',
  `reason` varchar(255) DEFAULT NULL COMMENT '事件中的原因',
  `symbol` varchar(100) DEFAULT NULL COMMENT '事件涉及的单个资产符号或者合约地址',
  `change_address` varchar(100) DEFAULT NULL COMMENT '事件中涉及变化的单个地址',
  `liquidity_token1_change_amount` decimal(36,18) DEFAULT NULL COMMENT '流动性变化事件中的token1的数量变化',
  `liquidity_token2_change_amount` decimal(36,18) DEFAULT NULL COMMENT '流动性变化事件中的token2的数量变化',
  `liquidity_token1` varchar(100) DEFAULT NULL COMMENT 'swap合约token1代币符号',
  `liquidity_token2` varchar(100) DEFAULT NULL COMMENT 'swap合约token2代币符号',
  `exchange_fee` decimal(36,18) DEFAULT NULL COMMENT '兑换的手续费',
  `buy_asset` varchar(100) DEFAULT NULL COMMENT '购买的资产符号或者合约地址',
  `sell_asset` varchar(100) DEFAULT NULL COMMENT '卖出的资产符号或者合约地址',
  `buy_amount` decimal(36,18) DEFAULT NULL COMMENT '购买的数量',
  `sell_amount` decimal(36,18) DEFAULT NULL COMMENT '卖出的数量',
  PRIMARY KEY (`id`),
  UNIQUE KEY `swap_tx_unique_txid_op_num_event_seq` (`trx_id`,`op_num`,`event_seq`),
  KEY `swap_tx_index_contract_address` (`contract_address`,`event_name`),
  KEY `swap_tx_index_caller_address` (`caller_address`),
  KEY `swap_tx_index_event_name` (`event_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='swap交易流水';
