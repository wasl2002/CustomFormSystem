/*
 Navicat Premium Data Transfer

 Source Server         : 10.12.111.24-测试库
 Source Server Type    : MySQL
 Source Server Version : 50743
 Source Host           : 10.12.111.24:3306
 Source Schema         : treasurybond_pm

 Target Server Type    : MySQL
 Target Server Version : 50743
 File Encoding         : 65001

 Date: 10/11/2025 15:45:31
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for form_config
-- ----------------------------
DROP TABLE IF EXISTS `form_config`;
CREATE TABLE `form_config`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `form_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '表单唯一标识',
  `form_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '表单名称',
  `form_description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '表单描述',
  `form_version` int(11) NULL DEFAULT 1 COMMENT '表单版本',
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '父版本ID',
  `is_latest` tinyint(4) NULL DEFAULT 1 COMMENT '是否最新版本：0-否，1-是',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '状态：1-草稿，2-已发布，3-停用',
  `created_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `updated_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_form_key`(`form_key`) USING BTREE,
  INDEX `idx_parent_id`(`parent_id`) USING BTREE,
  INDEX `idx_is_latest`(`is_latest`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '表单配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of form_config
-- ----------------------------
INSERT INTO `form_config` VALUES (1, 'employee_info', '员工信息表', '用于收集员工基本信息的表单', 1, NULL, 0, 2, NULL, '2025-10-31 13:23:51', NULL, '2025-10-31 13:23:51');
INSERT INTO `form_config` VALUES (2, 'employee_info', '员工信息表', '用于收集员工基本信息的表单', 2, 1, 0, 2, NULL, NULL, NULL, NULL);
INSERT INTO `form_config` VALUES (3, 'employee_info', '员工信息表', '用于收集员工基本信息的表单', 3, 2, 1, 2, NULL, NULL, NULL, NULL);
INSERT INTO `form_config` VALUES (4, 'form_1761900890127', '111', '111', 1, NULL, 0, 2, NULL, NULL, NULL, NULL);
INSERT INTO `form_config` VALUES (5, 'form_1761900890127', '11111111', '111', 2, 4, 0, 2, NULL, NULL, NULL, NULL);
INSERT INTO `form_config` VALUES (6, 'form_1761900890127', '111', '111', 2, 4, 0, 2, NULL, NULL, NULL, NULL);
INSERT INTO `form_config` VALUES (7, 'form_1761900890127', '111', '111', 3, 6, 0, 2, NULL, NULL, NULL, NULL);
INSERT INTO `form_config` VALUES (8, 'form_1761900890127', '111', '111', 4, 7, 0, 2, NULL, NULL, NULL, NULL);
INSERT INTO `form_config` VALUES (9, 'form_1761900890127', '111', '111', 5, 8, 0, 2, NULL, NULL, NULL, NULL);
INSERT INTO `form_config` VALUES (10, 'form_1761900890127', '111', '111', 6, 9, 0, 2, NULL, NULL, NULL, NULL);
INSERT INTO `form_config` VALUES (11, 'form_1761900890127', '111', '111', 7, 10, 0, 2, NULL, NULL, NULL, NULL);
INSERT INTO `form_config` VALUES (12, 'form_1761900890127', '111', '111', 8, 11, 0, 2, NULL, NULL, NULL, NULL);
INSERT INTO `form_config` VALUES (13, 'form_1761900890127', '111', '111', 9, 12, 0, 2, NULL, NULL, NULL, NULL);
INSERT INTO `form_config` VALUES (14, 'form_1761900890127', '111', '111', 10, 13, 0, 2, NULL, NULL, NULL, NULL);
INSERT INTO `form_config` VALUES (15, 'form_1761900890127', '111', '111', 11, 14, 0, 2, NULL, NULL, NULL, NULL);
INSERT INTO `form_config` VALUES (16, 'form_1761900890127', '111', '111', 12, 15, 0, 2, NULL, NULL, NULL, NULL);
INSERT INTO `form_config` VALUES (17, 'form_1761900890127', '111', '111', 13, 16, 0, 2, NULL, NULL, NULL, NULL);
INSERT INTO `form_config` VALUES (18, 'form_1761900890127', '111', '111', 14, 17, 0, 2, NULL, NULL, NULL, NULL);
INSERT INTO `form_config` VALUES (19, 'form_1761900890127', '111', '111', 15, 18, 0, 2, NULL, NULL, NULL, NULL);
INSERT INTO `form_config` VALUES (20, 'form_1761900890127', '111', '111', 16, 19, 1, 2, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for form_data
-- ----------------------------
DROP TABLE IF EXISTS `form_data`;
CREATE TABLE `form_data`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `form_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '表单唯一标识',
  `form_version` int(11) NULL DEFAULT 1 COMMENT '表单版本',
  `form_data` json NOT NULL COMMENT '表单数据（JSON格式）',
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_form_key`(`form_key`) USING BTREE,
  INDEX `idx_form_version`(`form_version`) USING BTREE,
  INDEX `idx_created_time`(`created_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '表单数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of form_data
-- ----------------------------
INSERT INTO `form_data` VALUES (1, 'form_1761900890127', 1, '{\"input_1761900891976_7356\": \"1\", \"textarea_1761900893642_8269\": \"2\"}', NULL, NULL);
INSERT INTO `form_data` VALUES (2, 'form_1761900890127', 1, '{\"input_1761900891976_7356\": \"2\", \"textarea_1761900893642_8269\": \"3\"}', NULL, NULL);
INSERT INTO `form_data` VALUES (3, 'form_1761900890127', 2, '{\"input_1761900891976_7356\": \"1\", \"textarea_1761900893642_8269\": \"3\"}', NULL, NULL);
INSERT INTO `form_data` VALUES (4, 'form_1761900890127', 2, '{\"input_1761900891976_7356\": \"1\", \"textarea_1761900893642_8269\": \"2\"}', NULL, NULL);
INSERT INTO `form_data` VALUES (5, 'form_1761900890127', 4, '{\"date_1761900901696_7645\": \"2025-11-05T08:02:47.570Z\", \"radio_1761900896742_877\": 1, \"input_1761900891976_7356\": \"1\", \"input_1762329125055_5944\": \"1\", \"select_1761900917943_4860\": 1, \"checkbox_1761900899347_1420\": [\"1\", \"2\"], \"textarea_1761900893642_8269\": \"1\"}', NULL, NULL);
INSERT INTO `form_data` VALUES (6, 'form_1761900890127', 4, '{\"date_1761900901696_7645\": \"2025-11-05T08:02:47.570Z\", \"radio_1761900896742_877\": 2, \"input_1761900891976_7356\": \"2\", \"input_1762329125055_5944\": \"2\", \"select_1761900917943_4860\": 2, \"checkbox_1761900899347_1420\": [\"1\", \"2\"], \"textarea_1761900893642_8269\": \"222\"}', NULL, NULL);

-- ----------------------------
-- Table structure for form_field
-- ----------------------------
DROP TABLE IF EXISTS `form_field`;
CREATE TABLE `form_field`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `form_id` bigint(20) NOT NULL COMMENT '关联表单ID',
  `field_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字段唯一标识',
  `field_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字段名称',
  `field_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字段类型：input-输入框，textarea-文本域，select-下拉框，radio-单选框，checkbox-复选框，date-日期，datetime-日期时间等',
  `field_options` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '字段选项配置(JSON格式)',
  `field_validations` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '字段校验规则(JSON格式)',
  `field_properties` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '字段属性配置(JSON格式)',
  `is_required` tinyint(4) NULL DEFAULT 0 COMMENT '是否必填：0-非必填，1-必填',
  `sort_order` int(11) NULL DEFAULT 0 COMMENT '排序号',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '状态：1-启用，2-禁用',
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_form_id`(`form_id`) USING BTREE,
  INDEX `idx_field_key`(`field_key`) USING BTREE,
  INDEX `idx_sort_order`(`sort_order`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 113 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '表单字段配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of form_field
-- ----------------------------
INSERT INTO `form_field` VALUES (1, 3, 'name', '姓名', 'input', '', '', '', 1, 1, 1, '2025-10-31 13:23:51', '2025-10-31 17:17:16');
INSERT INTO `form_field` VALUES (2, 3, 'age', '年龄', 'input', '', '', '', 1, 2, 1, '2025-10-31 13:23:51', '2025-10-31 17:17:16');
INSERT INTO `form_field` VALUES (3, 3, 'gender', '性别', 'radio', '', '', '', 1, 3, 1, '2025-10-31 13:23:51', '2025-10-31 17:17:16');
INSERT INTO `form_field` VALUES (4, 3, 'department', '部门', 'select', '', '', '', 1, 4, 1, '2025-10-31 13:23:51', '2025-10-31 17:17:16');
INSERT INTO `form_field` VALUES (5, 3, 'hire_date', '入职日期', 'date', '', '', '', 0, 5, 1, '2025-10-31 13:23:51', '2025-10-31 17:17:16');
INSERT INTO `form_field` VALUES (6, 3, 'email', '邮箱', 'input', '', '', '', 0, 6, 1, '2025-10-31 13:23:51', '2025-10-31 17:17:16');
INSERT INTO `form_field` VALUES (7, 4, 'input_1761900891976_7356', '输入框', 'input', '', '', '', 0, 0, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (8, 4, 'textarea_1761900893642_8269', '文本域', 'textarea', '', '', '', 0, 1, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (9, 4, 'select_1761900917943_4860', '下拉框', 'select', '', '', '', 0, 2, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (10, 4, 'radio_1761900896742_877', '单选框', 'radio', '', '', '', 0, 3, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (11, 4, 'checkbox_1761900899347_1420', '复选框', 'checkbox', '', '', '', 0, 4, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (12, 4, 'date_1761900901696_7645', '日期选择', 'date', '', '', '', 0, 5, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (14, 6, 'input_1762329125055_5944', '输入框1', 'input', '', '', '', 0, 0, 1, NULL, NULL);
INSERT INTO `form_field` VALUES (15, 6, 'input_1761900891976_7356', '输入框', 'input', '', '', '', 0, 1, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (16, 6, 'textarea_1761900893642_8269', '文本域', 'textarea', '', '', '', 0, 2, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (17, 6, 'select_1761900917943_4860', '下拉框', 'select', '', '', '', 0, 3, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (18, 6, 'radio_1761900896742_877', '单选框', 'radio', '', '', '', 0, 4, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (19, 6, 'checkbox_1761900899347_1420', '复选框', 'checkbox', '', '', '', 0, 5, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (20, 6, 'date_1761900901696_7645', '日期选择', 'date', '', '', '', 0, 6, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (21, 7, 'input_1762329125055_5944', '输入框1', 'input', '', '', '', 0, 0, 1, NULL, NULL);
INSERT INTO `form_field` VALUES (22, 7, 'input_1761900891976_7356', '输入框', 'input', '', '', '', 0, 1, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (23, 7, 'textarea_1761900893642_8269', '文本域', 'textarea', '', '', '', 0, 2, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (24, 7, 'select_1761900917943_4860', '下拉框', 'select', '', '', '', 0, 3, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (25, 7, 'radio_1761900896742_877', '单选框', 'radio', '1=a\n2=b', '', '', 0, 4, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (26, 7, 'checkbox_1761900899347_1420', '复选框', 'checkbox', '', '', '', 0, 5, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (27, 7, 'date_1761900901696_7645', '日期选择', 'date', '', '', '', 0, 6, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (28, 8, 'input_1762329125055_5944', '输入框1', 'input', '', '', '', 0, 0, 1, NULL, NULL);
INSERT INTO `form_field` VALUES (29, 8, 'input_1761900891976_7356', '输入框', 'input', '', '', '', 0, 1, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (30, 8, 'textarea_1761900893642_8269', '文本域', 'textarea', '', '', '', 0, 2, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (31, 8, 'select_1761900917943_4860', '下拉框', 'select', '1=aa\n2=bb', '', '', 0, 3, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (32, 8, 'radio_1761900896742_877', '单选框', 'radio', '1=a\n2=b', '', '', 0, 4, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (33, 8, 'checkbox_1761900899347_1420', '复选框', 'checkbox', '1=aaa\n2=bbb', '', '', 0, 5, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (34, 8, 'date_1761900901696_7645', '日期选择', 'date', '', '', '', 0, 6, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (35, 9, 'input_1762329125055_5944', '输入框1', 'input', '', '', '', 0, 0, 1, NULL, NULL);
INSERT INTO `form_field` VALUES (36, 9, 'input_1761900891976_7356', '输入框', 'input', '', '', '', 0, 1, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (37, 9, 'textarea_1761900893642_8269', '文本域', 'textarea', '', '', '', 0, 2, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (38, 9, 'select_1761900917943_4860', '下拉框', 'select', '1=aa\n2=bb', '', '', 0, 3, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (39, 9, 'radio_1761900896742_877', '单选框', 'radio', '1=a\n2=b', '', '{\"defaultValue\":\"1\"}', 0, 4, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (40, 9, 'checkbox_1761900899347_1420', '复选框', 'checkbox', '1=aaa\n2=bbb', '', '{\"defaultValue\":[\"2\"]}', 0, 5, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (41, 9, 'date_1761900901696_7645', '日期选择', 'date', '', '', '', 0, 6, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (42, 10, 'input_1761900891976_7356', '输入框', 'input', '', '', '', 0, 1, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (43, 10, 'textarea_1761900893642_8269', '文本域', 'textarea', '', '', '', 0, 2, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (44, 10, 'select_1761900917943_4860', '下拉框', 'select', '1=aa\n2=bb', '', '', 0, 3, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (45, 10, 'radio_1761900896742_877', '单选框', 'radio', '1=a\n2=b', '', '{\"defaultValue\":\"1\"}', 0, 4, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (46, 10, 'checkbox_1761900899347_1420', '复选框', 'checkbox', '1=aaa\n2=bbb', '', '{\"defaultValue\":[\"2\"]}', 0, 5, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (47, 10, 'date_1761900901696_7645', '日期选择', 'date', '', '', '', 0, 6, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (48, 11, 'textarea_1761900893642_8269', '文本域', 'textarea', '', '', '', 0, 2, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (49, 11, 'input_1761900891976_7356', '输入框', 'input', '', '', '', 0, 1, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (50, 11, 'select_1761900917943_4860', '下拉框', 'select', '1=aa\n2=bb', '', '', 0, 3, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (51, 11, 'radio_1761900896742_877', '单选框', 'radio', '1=a\n2=b', '', '{\"defaultValue\":\"1\"}', 0, 4, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (52, 11, 'checkbox_1761900899347_1420', '复选框', 'checkbox', '1=aaa\n2=bbb', '', '{\"defaultValue\":[\"2\"]}', 0, 5, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (53, 11, 'date_1761900901696_7645', '日期选择', 'date', '', '', '', 0, 6, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (54, 12, 'textarea_1761900893642_8269', '文本域', 'textarea', '', '', '', 0, 2, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (55, 12, 'input_1761900891976_7356', '输入框', 'input', '', '', '', 0, 1, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (56, 12, 'select_1761900917943_4860', '下拉框', 'select', '1=aa\n2=bb', '', '', 0, 3, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (57, 12, 'radio_1761900896742_877', '单选框', 'radio', '1=a\n2=b', '', '{\"defaultValue\":\"1\"}', 0, 4, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (58, 12, 'checkbox_1761900899347_1420', '复选框', 'checkbox', '1=aaa\n2=bbb', '', '{\"defaultValue\":[\"2\"]}', 0, 5, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (59, 12, 'date_1761900901696_7645', '日期选择', 'date', '', '', '', 0, 6, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (60, 13, 'input_1761900891976_7356', '输入框', 'input', '', '', '', 0, 1, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (61, 13, 'textarea_1761900893642_8269', '文本域', 'textarea', '', '', '', 0, 2, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (62, 13, 'radio_1761900896742_877', '单选框', 'radio', '1=a\n2=b', '', '{\"defaultValue\":\"1\"}', 0, 4, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (63, 13, 'select_1761900917943_4860', '下拉框', 'select', '1=aa\n2=bb', '', '', 0, 3, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (64, 13, 'checkbox_1761900899347_1420', '复选框', 'checkbox', '1=aaa\n2=bbb', '', '{\"defaultValue\":[\"2\"]}', 0, 5, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (65, 13, 'date_1761900901696_7645', '日期选择', 'date', '', '', '', 0, 6, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (66, 14, 'date_1761900901696_7645', '日期选择', 'date', '', '', '', 0, 6, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (67, 14, 'input_1761900891976_7356', '输入框', 'input', '', '', '', 0, 1, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (68, 14, 'textarea_1761900893642_8269', '文本域', 'textarea', '', '', '', 0, 2, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (69, 14, 'select_1761900917943_4860', '下拉框', 'select', '1=aa\n2=bb', '', '', 0, 3, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (70, 14, 'radio_1761900896742_877', '单选框', 'radio', '1=a\n2=b', '', '{\"defaultValue\":\"1\"}', 0, 4, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (71, 14, 'checkbox_1761900899347_1420', '复选框', 'checkbox', '1=aaa\n2=bbb', '', '{\"defaultValue\":[\"2\"]}', 0, 5, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (72, 15, 'input_1761900891976_7356', '输入框', 'input', '', '', '', 0, 1, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (73, 15, 'textarea_1761900893642_8269', '文本域', 'textarea', '', '', '', 0, 2, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (74, 15, 'select_1761900917943_4860', '下拉框', 'select', '1=aa\n2=bb', '', '', 0, 3, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (75, 15, 'radio_1761900896742_877', '单选框', 'radio', '1=a\n2=b', '', '{\"defaultValue\":\"1\"}', 0, 4, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (76, 15, 'date_1761900901696_7645', '日期选择', 'date', '', '', '', 0, 6, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (77, 15, 'checkbox_1761900899347_1420', '复选框', 'checkbox', '1=aaa\n2=bbb', '', '{\"defaultValue\":[\"2\"]}', 0, 5, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (78, 16, 'field_1762752822876', '省市区选择', 'region-field', '', NULL, NULL, 0, 0, 1, NULL, NULL);
INSERT INTO `form_field` VALUES (79, 16, 'input_1761900891976_7356', '输入框', 'input', '', '', '', 0, 1, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (80, 16, 'textarea_1761900893642_8269', '文本域', 'textarea', '', '', '', 0, 2, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (81, 16, 'select_1761900917943_4860', '下拉框', 'select', '1=aa\n2=bb', '', '', 0, 3, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (82, 16, 'radio_1761900896742_877', '单选框', 'radio', '1=a\n2=b', '', '{\"defaultValue\":\"1\"}', 0, 4, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (83, 16, 'checkbox_1761900899347_1420', '复选框', 'checkbox', '1=aaa\n2=bbb', '', '{\"defaultValue\":[\"2\"]}', 0, 5, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (84, 16, 'date_1761900901696_7645', '日期选择', 'date', '', '', '', 0, 6, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (85, 17, 'field_1762752822876', '省市区选择', 'region-field', '', NULL, NULL, 0, 0, 1, NULL, NULL);
INSERT INTO `form_field` VALUES (86, 17, 'textarea_1761900893642_8269', '文本域', 'textarea', '', '', '', 0, 2, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (87, 17, 'input_1761900891976_7356', '输入框', 'input', '', '', '', 0, 1, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (88, 17, 'select_1761900917943_4860', '下拉框', 'select', '1=aa\n2=bb', '', '', 0, 3, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (89, 17, 'radio_1761900896742_877', '单选框', 'radio', '1=a\n2=b', '', '{\"defaultValue\":\"1\"}', 0, 4, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (90, 17, 'checkbox_1761900899347_1420', '复选框', 'checkbox', '1=aaa\n2=bbb', '', '{\"defaultValue\":[\"2\"]}', 0, 5, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (91, 17, 'date_1761900901696_7645', '日期选择', 'date', '', '', '', 0, 6, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (92, 18, 'input_1761900891976_7356', '输入框', 'input', '', '', '', 0, 1, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (93, 18, 'field_1762752822876', '省市区选择', 'region-field', '', NULL, NULL, 0, 0, 1, NULL, NULL);
INSERT INTO `form_field` VALUES (94, 18, 'textarea_1761900893642_8269', '文本域', 'textarea', '', '', '', 0, 2, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (95, 18, 'select_1761900917943_4860', '下拉框', 'select', '1=aa\n2=bb', '', '', 0, 3, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (96, 18, 'radio_1761900896742_877', '单选框', 'radio', '1=a\n2=b', '', '{\"defaultValue\":\"1\"}', 0, 4, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (97, 18, 'checkbox_1761900899347_1420', '复选框', 'checkbox', '1=aaa\n2=bbb', '', '{\"defaultValue\":[\"2\"]}', 0, 5, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (98, 18, 'date_1761900901696_7645', '日期选择', 'date', '', '', '', 0, 6, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (99, 19, 'input_1761900891976_7356', '输入框', 'input', '', '', '', 0, 1, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (100, 19, 'textarea_1761900893642_8269', '文本域', 'textarea', '', '', '', 0, 2, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (101, 19, 'field_1762752822876', '省市区选择', 'region-field', '', NULL, NULL, 0, 3, 1, NULL, NULL);
INSERT INTO `form_field` VALUES (102, 19, 'select_1761900917943_4860', '下拉框', 'select', '1=aa\n2=bb', '', '', 0, 4, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (103, 19, 'radio_1761900896742_877', '单选框', 'radio', '1=a\n2=b', '', '{\"defaultValue\":\"1\"}', 0, 5, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (104, 19, 'checkbox_1761900899347_1420', '复选框', 'checkbox', '1=aaa\n2=bbb', '', '{\"defaultValue\":[\"2\"]}', 0, 6, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (105, 19, 'date_1761900901696_7645', '日期选择', 'date', '', '', '', 0, 7, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (106, 20, 'input_1761900891976_7356', '输入框', 'input', '', '', '', 0, 1, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (107, 20, 'textarea_1761900893642_8269', '文本域', 'textarea', '', '', '', 0, 2, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (108, 20, 'field_1762752822876', '省市区选择', 'region-field', '', NULL, NULL, 0, 3, 1, NULL, NULL);
INSERT INTO `form_field` VALUES (109, 20, 'select_1761900917943_4860', '下拉框', 'select', '1=aa\n2=bb', '', '', 0, 4, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (110, 20, 'radio_1761900896742_877', '单选框', 'radio', '1=a\n2=b', '', '{\"defaultValue\":\"1\"}', 0, 5, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (111, 20, 'checkbox_1761900899347_1420', '复选框', 'checkbox', '1=aaa\n2=bbb', '', '{\"defaultValue\":[\"2\"]}', 0, 6, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');
INSERT INTO `form_field` VALUES (112, 20, 'date_1761900901696_7645', '日期选择', 'date', '', '', '', 0, 7, 1, '2025-10-31 13:23:51', '2025-10-31 17:05:00');

SET FOREIGN_KEY_CHECKS = 1;
