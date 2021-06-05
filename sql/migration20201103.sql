CREATE TABLE `bl_token_balance` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `addr` varchar(100) NOT NULL,
  `amount` decimal(36,18) NOT NULL,
  `token_contract` varchar(100) NOT NULL,
  `token_symbol` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `token_balance_unique_addr_contract_id` (`addr`,`token_contract`),
  KEY `token_balance_idx_contract_id` (`token_contract`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
