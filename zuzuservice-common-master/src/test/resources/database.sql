CREATE SCHEMA `topapptrends` DEFAULT CHARACTER SET utf8mb4 ;

ALTER TABLE `topapptrends`.`master_application_index_s` KEY_BLOCK_SIZE = 8 , ROW_FORMAT = Compressed ;

ALTER TABLE `topapptrends`.`master_application_language_s` KEY_BLOCK_SIZE = 8 , ROW_FORMAT = Compressed ;

ALTER TABLE `topapptrends`.`master_application_screenshot_s` KEY_BLOCK_SIZE = 8 , ROW_FORMAT = Compressed ;

ALTER TABLE `topapptrends`.`master_application_s` KEY_BLOCK_SIZE = 8 , ROW_FORMAT = Compressed ;

ALTER TABLE `topapptrends`.`master_summary_s` KEY_BLOCK_SIZE = 8 , ROW_FORMAT = Compressed ;

ALTER TABLE `topapptrends`.`master_country_language_s` KEY_BLOCK_SIZE = 8 , ROW_FORMAT = Compressed ;

ALTER TABLE `zuzuapps`.`master_application_language_s` CHANGE COLUMN `language_code` `language_code` VARCHAR(2) NOT NULL ;

ALTER TABLE `zuzuapps`.`quarterly_report_status`  ADD PARTITION ( PARTITION p13 VALUES LESS THAN (UNIX_TIMESTAMP('2017-02-04 00:00:00'))) ;


ALTER TABLE `zuzuapps`.`quarterly_report_status`  ADD PARTITION BY HASH ( UNIX_TIMESTAMP(create_at) ) ( PARTITION p13 VALUES LESS THAN (UNIX_TIMESTAMP('2017-02-04 00:00:00'))) ;


ALTER TABLE `zuzuapps`.`master_application_index_s`  ADD PARTITION BY HASH ( UNIX_TIMESTAMP(create_at) )( PARTITION p13 VALUES LESS THAN (UNIX_TIMESTAMP('2017-02-04 00:00:00')) );

ALTER TABLE `zuzuapps`.`master_application_language_s`
DROP PRIMARY KEY,
ADD PRIMARY KEY (`id`, `app_id`);

ALTER TABLE `zuzuapps`.`master_application_language_s`
DROP PRIMARY KEY,
ADD PRIMARY KEY (`id`, `app_id`, `language_code`),
DROP INDEX `app_language_index` ;


ALTER TABLE ti PARTITION BY HASH( (YEAR(create_at) * 100) + MONTH(create_at) ) PARTITIONS 12;