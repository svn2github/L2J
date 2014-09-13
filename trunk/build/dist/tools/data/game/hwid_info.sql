/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50612
Source Host           : localhost:3306
Source Database       : l2next

Target Server Type    : MYSQL
Target Server Version : 50612
File Encoding         : 65001

Date: 2013-06-22 14:09:33
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for hwid_info
-- ----------------------------
DROP TABLE IF EXISTS `hwid_info`;
CREATE TABLE `hwid_info` (
  `Account` varchar(50) DEFAULT NULL,
  `HWID` int(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
