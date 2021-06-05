CREATE TABLE `bl_scan_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `block_num` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `bl_contract_info`
ADD INDEX `contract_info_idx_reg_time` (`reg_time` DESC);
;

ALTER TABLE `bl_transaction`
ADD INDEX `trx_contract_id` (`contract_id` ASC);
;
