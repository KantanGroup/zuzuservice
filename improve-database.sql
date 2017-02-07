ALTER TABLE `topapptrends`.`master_application_comment_s`
KEY_BLOCK_SIZE = 8 , ROW_FORMAT = Compressed ;

ALTER TABLE `topapptrends`.`master_application_index_s`
KEY_BLOCK_SIZE = 8 , ROW_FORMAT = Compressed ;

ALTER TABLE `topapptrends`.`master_application_language_s`
KEY_BLOCK_SIZE = 8 , ROW_FORMAT = Compressed ;

ALTER TABLE `topapptrends`.`master_application_s`
KEY_BLOCK_SIZE = 8 , ROW_FORMAT = Compressed ;

ALTER TABLE `topapptrends`.`master_application_screenshot_s`
KEY_BLOCK_SIZE = 8 , ROW_FORMAT = Compressed ;

ALTER TABLE `topapptrends`.`master_summary_s`
KEY_BLOCK_SIZE = 8 , ROW_FORMAT = Compressed ;

ALTER TABLE `topapptrends`.`master_country_language_s`
KEY_BLOCK_SIZE = 8 , ROW_FORMAT = Compressed ;

ALTER TABLE `topapptrends`.`master_application_language_s`
CHANGE COLUMN `app_id` `app_id` VARCHAR(128) CHARACTER SET 'utf8mb4' NOT NULL ,
CHANGE COLUMN `description` `description` TEXT CHARACTER SET 'utf8mb4' NULL DEFAULT NULL ,
CHANGE COLUMN `description_html` `description_html` TEXT CHARACTER SET 'utf8mb4' NULL DEFAULT NULL ,
CHANGE COLUMN `summary` `summary` TEXT CHARACTER SET 'utf8mb4' NULL DEFAULT NULL ;
