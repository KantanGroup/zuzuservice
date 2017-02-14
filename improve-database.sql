ALTER DATABASE topapptrends CHARACTER SET utf8mb4; # DBのutf8化
ALTER TABLE master_application_s CONVERT TO CHARACTER SET utf8mb4; #テーブルのutf8mb化

ALTER TABLE `topapptrends`.`master_application_language_s`
DROP COLUMN `update_at`,
DROP COLUMN `title`,
DROP COLUMN `summary`,
DROP COLUMN `description_html`,
DROP COLUMN `description`;

ALTER TABLE `topapptrends`.`master_application_s`
CHANGE COLUMN `content_rating` `content_rating` VARCHAR(64) NULL DEFAULT NULL ;

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
CHANGE COLUMN `description` `description` TEXT CHARACTER SET 'utf8mb4' NULL DEFAULT NULL ,
CHANGE COLUMN `description_html` `description_html` TEXT CHARACTER SET 'utf8mb4' NULL DEFAULT NULL ,
CHANGE COLUMN `summary` `summary` TEXT CHARACTER SET 'utf8mb4' NULL DEFAULT NULL ;
