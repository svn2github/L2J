CREATE TABLE IF NOT EXISTS `character_inzone_history` (
	`character_id` INT NOT NULL DEFAULT '0',
	`instance_id` INT NOT NULL DEFAULT '0',
	`instance_use_time` BIGINT(20) NOT NULL DEFAULT '0',
	`instance_status` INT NOT NULL DEFAULT '0',
	`party_id` INT NOT NULL DEFAULT '0',
	PRIMARY KEY(`character_id`)
) ENGINE=MyISAM;