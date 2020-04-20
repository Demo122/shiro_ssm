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

 Date: 20/04/2020 18:13:16
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `category` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `url` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `menu` tinyint(1) UNSIGNED ZEROFILL NOT NULL,
  `desc_` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 48 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of permission
-- ----------------------------
INSERT INTO `permission` VALUES (1, '增加产品', '产品', '/addProduct', 0, 'addProduct');
INSERT INTO `permission` VALUES (2, '删除产品', '产品', '/deleteProduct', 0, 'deleteProduct');
INSERT INTO `permission` VALUES (3, '编辑产品', '产品', '/editeProduct', 0, 'sd');
INSERT INTO `permission` VALUES (4, '修改产品', '产品', '/updateProduct', 0, 'updateProduct');
INSERT INTO `permission` VALUES (5, '查看产品', '产品', '/listProduct', 0, 'listProduct');
INSERT INTO `permission` VALUES (6, '增加订单', '订单', '/addOrder', 0, 'addOrder');
INSERT INTO `permission` VALUES (7, '删除订单', '订单', '/deleteOrder', 0, 'deleteOrder');
INSERT INTO `permission` VALUES (8, '编辑订单', '订单', '/editeOrder', 0, 'editeOrder');
INSERT INTO `permission` VALUES (9, '修改订单', '订单', '/updateOrder', 0, 'updateOrder');
INSERT INTO `permission` VALUES (10, '查看订单', '订单', '/listOrder', 0, 'listOrder');
INSERT INTO `permission` VALUES (11, '查看用户列表', '用户管理', '/user/listUser', 0, 'listUser');
INSERT INTO `permission` VALUES (12, '添加用户', '用户管理', '/user/addUser', 0, 'addUser');
INSERT INTO `permission` VALUES (13, '用户管理', '用户管理', '/listUser', 1, 'listUser');
INSERT INTO `permission` VALUES (14, '角色管理', '角色管理', '/listRole', 1, 'listRole');
INSERT INTO `permission` VALUES (15, '权限管理', '权限管理', '/listPermission', 1, 'listPermission');
INSERT INTO `permission` VALUES (16, '添加用户页面', '用户管理', '/user/addUserPage', 0, 'addUserPage');
INSERT INTO `permission` VALUES (17, '删除用户', '用户管理', '/user/deleteUser', 0, 'deleteUser');
INSERT INTO `permission` VALUES (19, '删除选中用户', '用户管理', '/user/deleteSelectUser', 0, 'deleteSelectUser');
INSERT INTO `permission` VALUES (20, '编辑用户页面', '用户管理', '/editUserPage', 0, 'editUserPage');
INSERT INTO `permission` VALUES (21, '更新用户', '用户管理', '/user/updateUser', 0, 'updateUser');
INSERT INTO `permission` VALUES (22, '更新选中的一组用户的状态', '用户管理', '/user/updateSelectUserStatus', 0, 'updateSelectUserStatus');
INSERT INTO `permission` VALUES (23, '添加权限页面', '权限管理', '/permission/addPermissionPage', 0, 'addPermissionPage');
INSERT INTO `permission` VALUES (24, '添加权限', '权限管理', '/permission/addPermission', 0, 'addPermission');
INSERT INTO `permission` VALUES (25, '更新单个用户的状态', '用户管理', '/user/updateUserStatus', 0, '/user/updateUserStatus');

SET FOREIGN_KEY_CHECKS = 1;
