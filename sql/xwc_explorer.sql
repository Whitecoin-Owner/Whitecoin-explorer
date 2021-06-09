/*
 Navicat Premium Data Transfer

 Source Server         : 新的区块浏览器
 Source Server Type    : MySQL
 Source Server Version : 80025
 Source Host           : localhost:50010
 Source Schema         : xwc_browser

 Target Server Type    : MySQL
 Target Server Version : 80025
 File Encoding         : 65001

 Date: 09/06/2021 10:43:49
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for bl_address_balance
-- ----------------------------
DROP TABLE IF EXISTS `bl_address_balance`;
CREATE TABLE `bl_address_balance`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `addr` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `amount` decimal(36, 18) NOT NULL,
  `asset_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `asset_symbol` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `extra` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `bl_address_balance_idx_addr`(`addr`) USING BTREE,
  INDEX `bl_address_balance_idx_asset_id`(`asset_id`) USING BTREE,
  INDEX `bl_address_balance_idx_asset_symbol`(`asset_symbol`) USING BTREE,
  INDEX `bl_address_balance_idx_amount`(`amount`) USING BTREE,
  INDEX `bl_address_balance_idx_addr_asset_id`(`addr`, `asset_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17084 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bl_asset
-- ----------------------------
DROP TABLE IF EXISTS `bl_asset`;
CREATE TABLE `bl_asset`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `symbol` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资产标识',
  `asset_id` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资产id',
  `precision` bigint(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '多资产表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bl_block
-- ----------------------------
DROP TABLE IF EXISTS `bl_block`;
CREATE TABLE `bl_block`  (
  `block_num` bigint(0) NOT NULL COMMENT '快号',
  `block_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '本块区块hash',
  `block_size` bigint(0) NULL DEFAULT NULL COMMENT '块大小（字节）',
  `miner_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `miner_address` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `previous` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '前一个块块id',
  `prev_hash` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上轮secret',
  `next_hash` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '本轮secret的hash',
  `block_time` datetime(0) NULL DEFAULT NULL COMMENT '产快时间',
  `trx_count` int(0) NULL DEFAULT NULL,
  `amount` bigint(0) NULL DEFAULT NULL COMMENT '包含的交易总金额',
  `fee` decimal(32, 5) NULL DEFAULT NULL,
  `reward` decimal(32, 5) NULL DEFAULT NULL,
  `merkle_root` varchar(70) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `created_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`block_num`) USING BTREE,
  INDEX `blockTime`(`block_time`) USING BTREE,
  INDEX `block_block_num`(`block_num`) USING BTREE,
  INDEX `miner_address`(`miner_address`) USING BTREE,
  INDEX `reward`(`reward`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '区块链block表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bl_contract_balance
-- ----------------------------
DROP TABLE IF EXISTS `bl_contract_balance`;
CREATE TABLE `bl_contract_balance`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `contract_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `balance` decimal(30, 8) NULL DEFAULT NULL,
  `asset_id` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 77 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bl_contract_info
-- ----------------------------
DROP TABLE IF EXISTS `bl_contract_info`;
CREATE TABLE `bl_contract_info`  (
  `contract_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '合约ID\r\n',
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '合约名称',
  `owner` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '合约拥有者公钥',
  `owner_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '合约拥有者地址',
  `owner_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '合约拥有者名称',
  `description` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '合约描述',
  `reg_time` datetime(0) NULL DEFAULT NULL COMMENT '注册时间',
  `status` int(0) UNSIGNED NULL DEFAULT 1 COMMENT '注册状态\r\n0 - 销毁\r\n1 - 临时\r\n2 - 永久',
  `code` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `create_tx` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `block_num` bigint(0) NULL DEFAULT NULL,
  PRIMARY KEY (`contract_id`) USING BTREE,
  INDEX `contract_info_idx_reg_time`(`reg_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bl_guarantee
-- ----------------------------
DROP TABLE IF EXISTS `bl_guarantee`;
CREATE TABLE `bl_guarantee`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `guarantee_id` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `owner_addr` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `asset_orign` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `asset_target` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `rate` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bl_proposal
-- ----------------------------
DROP TABLE IF EXISTS `bl_proposal`;
CREATE TABLE `bl_proposal`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `proposal_id` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '提案id',
  `proposer_id` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发起人id',
  `proposer` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发起人',
  `block` bigint(0) NULL DEFAULT NULL COMMENT '提案块高',
  `proposal_time` datetime(0) NULL DEFAULT NULL COMMENT '提案时间',
  `amonut` decimal(32, 8) NULL DEFAULT NULL COMMENT '提案金额',
  `status` int(0) NULL DEFAULT NULL COMMENT '提案状态：1进行中；-1已结束',
  `create_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `proposal_id`(`proposal_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '提案表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bl_proposal_content
-- ----------------------------
DROP TABLE IF EXISTS `bl_proposal_content`;
CREATE TABLE `bl_proposal_content`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `proposal_id` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '提案id',
  `referee_id` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '推荐人id',
  `referee` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '推荐人名',
  `replaced_id` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '被替换人id',
  `replaced_person` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '被替换人名',
  `vote_rate` decimal(10, 4) NULL DEFAULT NULL COMMENT '得票率',
  `status` int(0) NULL DEFAULT NULL COMMENT '竞选状态：0进行中；1成功;-1失败',
  `address` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `flag` int(0) NULL DEFAULT NULL COMMENT '同步标记：1已同步；-1未同步',
  `symbol` int(0) NULL DEFAULT NULL COMMENT '是否为白名单配置：1',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '提案内容表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bl_proposal_info
-- ----------------------------
DROP TABLE IF EXISTS `bl_proposal_info`;
CREATE TABLE `bl_proposal_info`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `referee_id` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '推荐人id',
  `replaced_id` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '被替换人id',
  `block` bigint(0) NULL DEFAULT NULL COMMENT '提案块高',
  `block_time` datetime(0) NULL DEFAULT NULL COMMENT '提案时间',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `status` int(0) NULL DEFAULT NULL COMMENT '已同步区块信息标记;1',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '获取提案区块信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bl_scan_info
-- ----------------------------
DROP TABLE IF EXISTS `bl_scan_info`;
CREATE TABLE `bl_scan_info`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `block_num` bigint(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bl_senator_config
-- ----------------------------
DROP TABLE IF EXISTS `bl_senator_config`;
CREATE TABLE `bl_senator_config`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `senator_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `email` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'senator配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bl_senator_current
-- ----------------------------
DROP TABLE IF EXISTS `bl_senator_current`;
CREATE TABLE `bl_senator_current`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `senator_id` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `senator_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `lock_asset` decimal(32, 8) NULL DEFAULT NULL COMMENT '锁仓数量',
  `proposal_id` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '提案id',
  `address` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地址',
  `pay_back` decimal(32, 8) NULL DEFAULT NULL COMMENT '待领取收益',
  `status` int(0) NULL DEFAULT NULL COMMENT '锁仓释放状态：1是；-1否',
  `trx_id` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '锁仓释放交易id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '当前senator表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bl_senator_previous
-- ----------------------------
DROP TABLE IF EXISTS `bl_senator_previous`;
CREATE TABLE `bl_senator_previous`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `senator_id` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `senator_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `lock_asset` decimal(32, 8) NULL DEFAULT NULL COMMENT '锁仓数量',
  `proposal_id` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '提案id',
  `address` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地址',
  `pay_back` decimal(10, 8) NULL DEFAULT NULL COMMENT '待领取收益',
  `status` int(0) NULL DEFAULT NULL COMMENT '锁仓释放状态：1是；-1否',
  `trx_id` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '锁仓释放交易id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '历届senator表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bl_senator_security
-- ----------------------------
DROP TABLE IF EXISTS `bl_senator_security`;
CREATE TABLE `bl_senator_security`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `senator_id` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bl_swap_contract
-- ----------------------------
DROP TABLE IF EXISTS `bl_swap_contract`;
CREATE TABLE `bl_swap_contract`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `contract_address` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'swap合约地址',
  `token1` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'swap token1代币的代币符号或者合约地址',
  `token2` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'swap token2代币的代币符号或者合约地址',
  `verified` int(0) NOT NULL COMMENT '是否被认证过',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `contract_address_UNIQUE`(`contract_address`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'swap合约' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bl_swap_transaction
-- ----------------------------
DROP TABLE IF EXISTS `bl_swap_transaction`;
CREATE TABLE `bl_swap_transaction`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `trx_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `block_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `block_num` int(0) NOT NULL,
  `caller_address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '合约调用人',
  `op_num` int(0) NOT NULL COMMENT 'operation在tx中的索引',
  `event_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'event名称',
  `event_seq` int(0) NOT NULL COMMENT 'event在operation中的序号',
  `event_arg` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT 'event参数',
  `contract_address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'event所属合约地址',
  `trx_time` datetime(0) NOT NULL,
  `change` decimal(65, 18) NULL DEFAULT NULL COMMENT '事件中单个资产金额变化的变化数量',
  `reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '事件中的原因',
  `symbol` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '事件涉及的单个资产符号或者合约地址',
  `change_address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '事件中涉及变化的单个地址',
  `liquidity_token1_change_amount` decimal(65, 18) NULL DEFAULT NULL COMMENT '流动性变化事件中的token1的数量变化',
  `liquidity_token2_change_amount` decimal(65, 18) NULL DEFAULT NULL COMMENT '流动性变化事件中的token2的数量变化',
  `liquidity_token1` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `liquidity_token2` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `exchange_fee` decimal(65, 18) NULL DEFAULT NULL COMMENT '兑换的手续费',
  `buy_asset` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '购买的资产符号或者合约地址',
  `sell_asset` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '卖出的资产符号或者合约地址',
  `buy_amount` decimal(65, 18) NULL DEFAULT NULL COMMENT '购买的数量',
  `sell_amount` decimal(65, 18) NULL DEFAULT NULL COMMENT '卖出的数量',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `swap_tx_unique_txid_op_num_event_seq`(`trx_id`, `op_num`, `event_seq`) USING BTREE,
  INDEX `swap_tx_index_contract_address`(`contract_address`, `event_name`) USING BTREE,
  INDEX `swap_tx_index_caller_address`(`caller_address`) USING BTREE,
  INDEX `swap_tx_index_event_name`(`event_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 55518 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'swap交易流水' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bl_token
-- ----------------------------
DROP TABLE IF EXISTS `bl_token`;
CREATE TABLE `bl_token`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `contract_address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `token_symbol` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `precision` int(0) NULL DEFAULT NULL,
  `creator_address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `token_supply` decimal(65, 18) NULL DEFAULT NULL,
  `create_trx_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `create_at` datetime(0) NULL DEFAULT NULL,
  `top_sort` int(0) NULL DEFAULT NULL COMMENT '如果是置顶token，这个字段值是指定的排序',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `token_idx_contract_address`(`contract_address`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 185 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '合约代币' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bl_token_balance
-- ----------------------------
DROP TABLE IF EXISTS `bl_token_balance`;
CREATE TABLE `bl_token_balance`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `addr` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `amount` decimal(65, 18) NOT NULL,
  `token_contract` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `token_symbol` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `token_balance_unique_addr_contract_id`(`addr`, `token_contract`) USING BTREE,
  INDEX `token_balance_idx_contract_id`(`token_contract`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1956 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bl_token_balance_copy1
-- ----------------------------
DROP TABLE IF EXISTS `bl_token_balance_copy1`;
CREATE TABLE `bl_token_balance_copy1`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `addr` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `amount` decimal(36, 18) NOT NULL,
  `token_contract` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `token_symbol` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `token_balance_unique_addr_contract_id`(`addr`, `token_contract`) USING BTREE,
  INDEX `token_balance_idx_contract_id`(`token_contract`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 729 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bl_token_transaction
-- ----------------------------
DROP TABLE IF EXISTS `bl_token_transaction`;
CREATE TABLE `bl_token_transaction`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `trx_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `block_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `block_num` int(0) NOT NULL,
  `from_account` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `to_account` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `symbol` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `amount` bigint(0) NOT NULL,
  `fee` bigint(0) NOT NULL,
  `trx_time` datetime(0) NOT NULL,
  `contract_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `memo` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `event_seq` int(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `token_tx_unique_trx_id_event_seq`(`trx_id`, `event_seq`) USING BTREE,
  INDEX `token_tx_idx_trx_id`(`trx_id`) USING BTREE,
  INDEX `token_tx_idx_from_account`(`from_account`) USING BTREE,
  INDEX `token_tx_idx_to_account`(`to_account`) USING BTREE,
  INDEX `token_tx_idx_contract_id`(`contract_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 27540 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'token转账流水' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bl_transaction
-- ----------------------------
DROP TABLE IF EXISTS `bl_transaction`;
CREATE TABLE `bl_transaction`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `trx_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易ID',
  `block_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '快号ID',
  `block_num` bigint(0) NULL DEFAULT NULL COMMENT '块号',
  `from_account` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出账地址',
  `to_account` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '入账地址',
  `miner_address` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `symbol` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `asset_id` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `amount` decimal(32, 8) NULL DEFAULT NULL,
  `fee` decimal(32, 8) NULL DEFAULT NULL,
  `trx_time` datetime(0) NULL DEFAULT NULL COMMENT '交易被确认时间',
  `called_abi` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '调用的合约函数，非合约交易该字段为空',
  `abi_params` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '调用合约函数时传入的参数，非合约交易该字段为空',
  `extra_trx_id` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '结果交易id仅针对合约交易',
  `guarantee_id` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `parent_op_type` int(0) NULL DEFAULT NULL COMMENT '交易聚合类：1、转账类；2、合约类；3、充值类；4、提现类；5、工资类；6、手续费承兑单类；7、其他类',
  `op_type` int(0) NULL DEFAULT 0 COMMENT '0 - 普通转账1 - 代理领工资2 - 注册账户3 - 注册代理10 - 注册合约11 - 合约充值12 - 合约升级',
  `gas_limit` int(0) NULL DEFAULT NULL,
  `gas_cost` int(0) NULL DEFAULT NULL,
  `gas_price` decimal(36, 18) NULL DEFAULT NULL,
  `extension` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `memo` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `contract_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '如果是合约交易则记录合约ID',
  `created_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `extension1` int(0) NULL DEFAULT NULL COMMENT '提现流程状态扩展字段，0、交易请求；1、交易创建；2、交易签名；3、交易广播；4、交易成功',
  `fail` int(0) NOT NULL DEFAULT 0 COMMENT '是否是失败交易',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `trx_id`(`trx_id`) USING BTREE,
  INDEX `trx_block_num`(`block_num`) USING BTREE,
  INDEX `trx_contract`(`from_account`, `op_type`, `contract_id`) USING BTREE,
  INDEX `trx_from`(`from_account`) USING BTREE,
  INDEX `trx_to`(`to_account`) USING BTREE,
  INDEX `op_type`(`op_type`) USING BTREE,
  INDEX `trx_contract_id`(`contract_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5626205 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '区块链交易表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bl_trx_statis
-- ----------------------------
DROP TABLE IF EXISTS `bl_trx_statis`;
CREATE TABLE `bl_trx_statis`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `statis_time` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `trx_num` int(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 655 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bl_tx_contract_balance_change
-- ----------------------------
DROP TABLE IF EXISTS `bl_tx_contract_balance_change`;
CREATE TABLE `bl_tx_contract_balance_change`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `trx_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `block_num` int(0) NOT NULL,
  `change_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '在receipt中的变化字段',
  `address` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `asset_id` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `amount` bigint(0) NOT NULL,
  `change_seq` int(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `tx_contract_balance_change_idx_txid`(`trx_id`) USING BTREE,
  INDEX `tx_contract_balance_change_idx_address`(`address`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 56778 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '合约交易的原生资产变化' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bl_tx_events
-- ----------------------------
DROP TABLE IF EXISTS `bl_tx_events`;
CREATE TABLE `bl_tx_events`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `trx_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `block_num` int(0) NOT NULL,
  `op_num` int(0) NULL DEFAULT NULL,
  `caller_addr` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `contract_address` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `event_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `event_arg` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `event_seq` int(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `tx_events_idx_txid`(`trx_id`) USING BTREE,
  INDEX `tx_events_idx_contract_address`(`contract_address`, `event_name`) USING BTREE,
  INDEX `tx_events_idx_event_name`(`event_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 397846 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for st_swap_kdata_12hour
-- ----------------------------
DROP TABLE IF EXISTS `st_swap_kdata_12hour`;
CREATE TABLE `st_swap_kdata_12hour`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `ex_pair` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `k_open` decimal(20, 8) NOT NULL,
  `k_close` decimal(20, 8) NOT NULL,
  `k_low` decimal(20, 8) NOT NULL,
  `k_high` decimal(20, 8) NOT NULL,
  `volume` decimal(20, 8) NOT NULL,
  `block_num` int(0) NOT NULL,
  `timestamp` datetime(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `timestamp`(`timestamp`) USING BTREE,
  INDEX `ex_pair`(`ex_pair`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1163196 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for st_swap_kdata_15min
-- ----------------------------
DROP TABLE IF EXISTS `st_swap_kdata_15min`;
CREATE TABLE `st_swap_kdata_15min`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `ex_pair` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `k_open` decimal(20, 8) NOT NULL,
  `k_close` decimal(20, 8) NOT NULL,
  `k_low` decimal(20, 8) NOT NULL,
  `k_high` decimal(20, 8) NOT NULL,
  `volume` decimal(20, 8) NOT NULL,
  `block_num` int(0) NOT NULL,
  `timestamp` datetime(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `ex_pair`(`ex_pair`) USING BTREE,
  INDEX `timestamp`(`timestamp`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1179704 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for st_swap_kdata_1hour
-- ----------------------------
DROP TABLE IF EXISTS `st_swap_kdata_1hour`;
CREATE TABLE `st_swap_kdata_1hour`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `ex_pair` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `k_open` decimal(20, 8) NOT NULL,
  `k_close` decimal(20, 8) NOT NULL,
  `k_low` decimal(20, 8) NOT NULL,
  `k_high` decimal(20, 8) NOT NULL,
  `volume` decimal(20, 8) NOT NULL,
  `block_num` int(0) NOT NULL,
  `timestamp` datetime(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `ex_pair`(`ex_pair`) USING BTREE,
  INDEX `timestamp`(`timestamp`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1167061 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for st_swap_kdata_1min
-- ----------------------------
DROP TABLE IF EXISTS `st_swap_kdata_1min`;
CREATE TABLE `st_swap_kdata_1min`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `ex_pair` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `k_open` decimal(20, 8) NOT NULL,
  `k_close` decimal(20, 8) NOT NULL,
  `k_low` decimal(20, 8) NOT NULL,
  `k_high` decimal(20, 8) NOT NULL,
  `volume` decimal(20, 8) NOT NULL,
  `block_num` int(0) NOT NULL,
  `timestamp` datetime(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `ex_pair`(`ex_pair`) USING BTREE,
  INDEX `timestamp`(`timestamp`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1415699 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for st_swap_kdata_2hour
-- ----------------------------
DROP TABLE IF EXISTS `st_swap_kdata_2hour`;
CREATE TABLE `st_swap_kdata_2hour`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `ex_pair` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `k_open` decimal(20, 8) NOT NULL,
  `k_close` decimal(20, 8) NOT NULL,
  `k_low` decimal(20, 8) NOT NULL,
  `k_high` decimal(20, 8) NOT NULL,
  `volume` decimal(20, 8) NOT NULL,
  `block_num` int(0) NOT NULL,
  `timestamp` datetime(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `ex_pair`(`ex_pair`) USING BTREE,
  INDEX `timestamp`(`timestamp`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1164953 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for st_swap_kdata_30min
-- ----------------------------
DROP TABLE IF EXISTS `st_swap_kdata_30min`;
CREATE TABLE `st_swap_kdata_30min`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `ex_pair` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `k_open` decimal(20, 8) NOT NULL,
  `k_close` decimal(20, 8) NOT NULL,
  `k_low` decimal(20, 8) NOT NULL,
  `k_high` decimal(20, 8) NOT NULL,
  `volume` decimal(20, 8) NOT NULL,
  `block_num` int(0) NOT NULL,
  `timestamp` datetime(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `ex_pair`(`ex_pair`) USING BTREE,
  INDEX `timestamp`(`timestamp`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1171276 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for st_swap_kdata_5min
-- ----------------------------
DROP TABLE IF EXISTS `st_swap_kdata_5min`;
CREATE TABLE `st_swap_kdata_5min`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `ex_pair` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `k_open` decimal(20, 8) NOT NULL,
  `k_close` decimal(20, 8) NOT NULL,
  `k_low` decimal(20, 8) NOT NULL,
  `k_high` decimal(20, 8) NOT NULL,
  `volume` decimal(20, 8) NOT NULL,
  `block_num` int(0) NOT NULL,
  `timestamp` datetime(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `ex_pair`(`ex_pair`) USING BTREE,
  INDEX `timestamp`(`timestamp`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1213416 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for st_swap_kdata_6hour
-- ----------------------------
DROP TABLE IF EXISTS `st_swap_kdata_6hour`;
CREATE TABLE `st_swap_kdata_6hour`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `ex_pair` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `k_open` decimal(20, 8) NOT NULL,
  `k_close` decimal(20, 8) NOT NULL,
  `k_low` decimal(20, 8) NOT NULL,
  `k_high` decimal(20, 8) NOT NULL,
  `volume` decimal(20, 8) NOT NULL,
  `block_num` int(0) NOT NULL,
  `timestamp` datetime(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `ex_pair`(`ex_pair`) USING BTREE,
  INDEX `timestamp`(`timestamp`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1163547 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for st_swap_kdata_daily
-- ----------------------------
DROP TABLE IF EXISTS `st_swap_kdata_daily`;
CREATE TABLE `st_swap_kdata_daily`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `ex_pair` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `k_open` decimal(20, 8) NOT NULL,
  `k_close` decimal(20, 8) NOT NULL,
  `k_low` decimal(20, 8) NOT NULL,
  `k_high` decimal(20, 8) NOT NULL,
  `volume` decimal(20, 8) NOT NULL,
  `block_num` int(0) NOT NULL,
  `timestamp` datetime(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `ex_pair`(`ex_pair`) USING BTREE,
  INDEX `timestamp`(`timestamp`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1162988 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for st_swap_kdata_monthly
-- ----------------------------
DROP TABLE IF EXISTS `st_swap_kdata_monthly`;
CREATE TABLE `st_swap_kdata_monthly`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `ex_pair` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `k_open` decimal(20, 8) NOT NULL,
  `k_close` decimal(20, 8) NOT NULL,
  `k_low` decimal(20, 8) NOT NULL,
  `k_high` decimal(20, 8) NOT NULL,
  `volume` decimal(20, 8) NOT NULL,
  `block_num` int(0) NOT NULL,
  `timestamp` datetime(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `ex_pair`(`ex_pair`) USING BTREE,
  INDEX `timestamp`(`timestamp`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1162849 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for st_swap_kdata_weekly
-- ----------------------------
DROP TABLE IF EXISTS `st_swap_kdata_weekly`;
CREATE TABLE `st_swap_kdata_weekly`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `ex_pair` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `k_open` decimal(20, 8) NOT NULL,
  `k_close` decimal(20, 8) NOT NULL,
  `k_low` decimal(20, 8) NOT NULL,
  `k_high` decimal(20, 8) NOT NULL,
  `volume` decimal(20, 8) NOT NULL,
  `block_num` int(0) NOT NULL,
  `timestamp` datetime(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `ex_pair`(`ex_pair`) USING BTREE,
  INDEX `timestamp`(`timestamp`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1162864 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for st_swap_liquidity
-- ----------------------------
DROP TABLE IF EXISTS `st_swap_liquidity`;
CREATE TABLE `st_swap_liquidity`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `tp_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `token1_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `token2_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `token1_amount` bigint(0) NOT NULL,
  `token2_amount` bigint(0) NOT NULL,
  `stat_time` datetime(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `token1_name`(`token1_name`) USING BTREE,
  INDEX `stat_time`(`stat_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 856892 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for st_swap_stat
-- ----------------------------
DROP TABLE IF EXISTS `st_swap_stat`;
CREATE TABLE `st_swap_stat`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `swap_stat` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `swap_value` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `swap_stat`(`swap_stat`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 283228 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for st_swap_tick
-- ----------------------------
DROP TABLE IF EXISTS `st_swap_tick`;
CREATE TABLE `st_swap_tick`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `timestamp` datetime(0) NOT NULL,
  `ex_pair` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `buy_asset` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `sell_asset` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `buy_amount` decimal(20, 8) NOT NULL,
  `sell_amount` decimal(20, 8) NOT NULL,
  `fee` decimal(20, 8) NOT NULL,
  `block_num` bigint(0) NOT NULL,
  `price` decimal(20, 8) NOT NULL,
  `volume` decimal(20, 8) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `symbol`(`buy_asset`) USING BTREE,
  INDEX `block_num`(`block_num`) USING BTREE,
  CONSTRAINT `st_swap_tick_ibfk_1` FOREIGN KEY (`block_num`) REFERENCES `bl_block` (`block_num`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 67924 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tp_contract_address
-- ----------------------------
DROP TABLE IF EXISTS `tp_contract_address`;
CREATE TABLE `tp_contract_address`  (
  `id` int(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `contract_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '合约地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for wallet_asset1
-- ----------------------------
DROP TABLE IF EXISTS `wallet_asset1`;
CREATE TABLE `wallet_asset1`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '钱包地址表自增主键',
  `wallet_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '钱包唯一ID',
  `coin_addr` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '钱包货币地址',
  `coin_type` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '钱包货币地址类型，是哪种货币',
  `total_amt` decimal(30, 18) NOT NULL DEFAULT 0.000000000000000000 COMMENT '该地址的可用总余额',
  `available_amt` decimal(30, 18) NULL DEFAULT NULL COMMENT '可用余额',
  `un_confirm_amt` decimal(30, 18) NOT NULL DEFAULT 0.000000000000000000 COMMENT '待确认的总金额',
  `mortgage_amt` decimal(30, 18) NULL DEFAULT NULL COMMENT '抵押资产',
  `bak` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '备注',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '地址新建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '地址更新时间',
  `star` int(0) NULL DEFAULT NULL,
  `asset_type` int(0) NULL DEFAULT NULL,
  `nfc_number` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `account_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账户名',
  `account_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账户id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `address_cointype`(`coin_addr`, `coin_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '钱包地址表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Function structure for json_extract_c
-- ----------------------------
DROP FUNCTION IF EXISTS `json_extract_c`;
delimiter ;;
CREATE DEFINER=`root`@`%` FUNCTION `json_extract_c`(
details TEXT,
required_field VARCHAR (255)
) RETURNS text CHARSET latin1
BEGIN
SET details = SUBSTRING_INDEX(details, "{", -1);
SET details = SUBSTRING_INDEX(details, "}", 1);
RETURN TRIM(
    BOTH '"' FROM SUBSTRING_INDEX(
        SUBSTRING_INDEX(
            SUBSTRING_INDEX(
                details,
                CONCAT(
                    '"',
                    SUBSTRING_INDEX(required_field,'$.', -1),
                    '":'
                ),
                -1
            ),
            ',"',
            1
        ),
        ':',
        -1
    )
) ;
END
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
