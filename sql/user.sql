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

 Date: 20/04/2020 18:13:54
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `salt` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `status` tinyint(1) UNSIGNED ZEROFILL NOT NULL DEFAULT 0,
  `description` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `activeCode` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 118 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'zhang3', 'a7d59dfc5332749cb801f86a24f5f590', 'e5ykFiNwShfCXvBRPr3wXg==', 1, '管理员', NULL);
INSERT INTO `user` VALUES (2, 'li4', '43e28304197b9216e45ab1ce8dac831b', 'jPz19y7arvYIGhuUjsb6sQ==', 1, 'asasd点点滴滴都是', NULL);
INSERT INTO `user` VALUES (37, '123', '6ca32017e74b263e79b651390ed514f5', 'k8JonRLw3oZtrXfM8UvV7g==', 1, 'adad', NULL);
INSERT INTO `user` VALUES (101, '123466ff', 'f6a5d31d23f3b2617cc34b94963e581a', 'pYD7gE/Y8ia73Alx9ld0SQ==', 1, '22222', NULL);
INSERT INTO `user` VALUES (103, 'fasdasdd', 'c066f9bfa1dbb3a3184657eee613c426', '10mfiTvVEKXLkZ3sGUz2dQ==', 0, '', NULL);
INSERT INTO `user` VALUES (109, 'weqeeqweq', 'ba2371963a09b87cdac3100bf372c9fb', 'EjoI58jYirRe+n7Y0/+dVw==', 0, NULL, NULL);
INSERT INTO `user` VALUES (110, '22222222222大苏打', 'f243eac59808a04c21040bc8253a3285', 'OuDuNlbuUiimgYuqI81/UQ==', 0, '', NULL);

SET FOREIGN_KEY_CHECKS = 1;
