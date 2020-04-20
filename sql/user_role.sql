/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 50527
 Source Host           : localhost:3306
 Source Schema         : shiro

 Target Server Type    : MySQL
 Target Server Version : 50527
 File Encoding         : 65001

 Date: 20/04/2020 18:14:03
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `uid` bigint(20) NULL DEFAULT NULL,
  `rid` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 165 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES (50, 6, 4);
INSERT INTO `user_role` VALUES (88, 1, 1);
INSERT INTO `user_role` VALUES (103, 2, 4);
INSERT INTO `user_role` VALUES (104, 2, 2);
INSERT INTO `user_role` VALUES (145, 37, 3);
INSERT INTO `user_role` VALUES (146, 37, 1);
INSERT INTO `user_role` VALUES (147, 37, 2);
INSERT INTO `user_role` VALUES (148, 37, 4);
INSERT INTO `user_role` VALUES (149, 101, 2);
INSERT INTO `user_role` VALUES (154, 103, 4);
INSERT INTO `user_role` VALUES (155, 103, 3);
INSERT INTO `user_role` VALUES (156, 103, 2);
INSERT INTO `user_role` VALUES (162, 110, 3);
INSERT INTO `user_role` VALUES (163, 110, 2);
INSERT INTO `user_role` VALUES (164, 110, 1);

SET FOREIGN_KEY_CHECKS = 1;
