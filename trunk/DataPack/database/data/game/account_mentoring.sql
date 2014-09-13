CREATE TABLE IF NOT EXISTS `account_mentoring` (
  `accountId` VARCHAR(45) NOT NULL DEFAULT '',
  `status` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`accountId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;