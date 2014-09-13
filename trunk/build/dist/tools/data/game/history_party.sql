CREATE TABLE IF NOT EXISTS `party_inzone_history` (
	`character_id` INT NOT NULL DEFAULT '0',
	`party_id` INT NOT NULL DEFAULT '0',
	PRIMARY KEY(`character_id`)
) ENGINE=MyISAM;