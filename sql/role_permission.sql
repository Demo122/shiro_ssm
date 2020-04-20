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

 Date: 20/04/2020 18:13:42
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for role_permission
-- ----------------------------
DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE `role_permission`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `rid` bigint(20) NULL DEFAULT NULL,
  `pid` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 102 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of role_permission
-- ----------------------------
INSERT INTO `role_permission` VALUES (11, 2, 1);
INSERT INTO `role_permission` VALUES (12, 2, 2);
INSERT INTO `role_permission` VALUES (13, 2, 3);
INSERT INTO `role_permission` VALUES (14, 2, 4);
INSERT INTO `role_permission` VALUES (15, 2, 5);
INSERT INTO `role_permission` VALUES (56, 5, 11);
INSERT INTO `role_permission` VALUES (58, 3, 10);
INSERT INTO `role_permission` VALUES (59, 3, 9);
INSERT INTO `role_permission` VALUES (60, 4, 11);
INSERT INTO `role_permission` VALUES (61, 1, 11);
INSERT INTO `role_permission` VALUES (62, 1, 10);
INSERT INTO `role_permission` VALUES (63, 1, 9);
INSERT INTO `role_permission` VALUES (64, 1, 8);
INSERT INTO `role_permission` VALUES (65, 1, 7);
INSERT INTO `role_permission` VALUES (66, 1, 6);
INSERT INTO `role_permission` VALUES (67, 1, 5);
INSERT INTO `role_permission` VALUES (68, 1, 4);
INSERT INTO `role_permission` VALUES (69, 1, 3);
INSERT INTO `role_permission` VALUES (70, 1, 2);
INSERT INTO `role_permission` VALUES (71, 1, 1);
INSERT INTO `role_permission` VALUES (72, 1, 13);
INSERT INTO `role_permission` VALUES (73, 1, 14);
INSERT INTO `role_permission` VALUES (74, 1, 15);
INSERT INTO `role_permission` VALUES (75, 1, 16);
INSERT INTO `role_permission` VALUES (77, 1, 12);
INSERT INTO `role_permission` VALUES (79, 1, 19);
INSERT INTO `role_permission` VALUES (80, 1, 20);
INSERT INTO `role_permission` VALUES (82, 1, 22);
INSERT INTO `role_permission` VALUES (84, 1, 24);
INSERT INTO `role_permission` VALUES (99, 1, 21);
INSERT INTO `role_permission` VALUES (101, 1, 25);

SET FOREIGN_KEY_CHECKS = 1;
