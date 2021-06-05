ALTER TABLE `bl_token`
ADD COLUMN `top_sort` INT(11) NULL COMMENT '如果是置顶token，这个字段值是指定的排序' AFTER `create_trx_id`;
