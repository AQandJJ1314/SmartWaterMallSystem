/*
 Navicat Premium Dump SQL

 Source Server         : ewew
 Source Server Type    : MySQL
 Source Server Version : 50728 (5.7.28)
 Source Host           : localhost:3306
 Source Schema         : gulimall_sms

 Target Server Type    : MySQL
 Target Server Version : 50728 (5.7.28)
 File Encoding         : 65001

 Date: 14/01/2025 16:50:09
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sms_coupon
-- ----------------------------
DROP TABLE IF EXISTS `sms_coupon`;
CREATE TABLE `sms_coupon`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `coupon_type` tinyint(1) NULL DEFAULT NULL COMMENT '优惠卷类型[0->全场赠券；1->会员赠券；2->购物赠券；3->注册赠券]',
  `coupon_img` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '优惠券图片',
  `coupon_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '优惠卷名字',
  `num` int(11) NULL DEFAULT NULL COMMENT '数量',
  `amount` decimal(18, 4) NULL DEFAULT NULL COMMENT '金额',
  `per_limit` int(11) NULL DEFAULT NULL COMMENT '每人限领张数',
  `min_point` decimal(18, 4) NULL DEFAULT NULL COMMENT '使用门槛',
  `start_time` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
  `use_type` tinyint(1) NULL DEFAULT NULL COMMENT '使用类型[0->全场通用；1->指定分类；2->指定商品]',
  `note` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `publish_count` int(11) NULL DEFAULT NULL COMMENT '发行数量',
  `use_count` int(11) NULL DEFAULT NULL COMMENT '已使用数量',
  `receive_count` int(11) NULL DEFAULT NULL COMMENT '领取数量',
  `enable_start_time` datetime NULL DEFAULT NULL COMMENT '可以领取的开始日期',
  `enable_end_time` datetime NULL DEFAULT NULL COMMENT '可以领取的结束日期',
  `code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '优惠码',
  `member_level` tinyint(1) NULL DEFAULT NULL COMMENT '可以领取的会员等级[0->不限等级，其他-对应等级]',
  `publish` tinyint(1) NULL DEFAULT NULL COMMENT '发布状态[0-未发布，1-已发布]',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '优惠券信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sms_coupon
-- ----------------------------

-- ----------------------------
-- Table structure for sms_coupon_history
-- ----------------------------
DROP TABLE IF EXISTS `sms_coupon_history`;
CREATE TABLE `sms_coupon_history`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `coupon_id` bigint(20) NULL DEFAULT NULL COMMENT '优惠券id',
  `member_id` bigint(20) NULL DEFAULT NULL COMMENT '会员id',
  `member_nick_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '会员名字',
  `get_type` tinyint(1) NULL DEFAULT NULL COMMENT '获取方式[0->后台赠送；1->主动领取]',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `use_type` tinyint(1) NULL DEFAULT NULL COMMENT '使用状态[0->未使用；1->已使用；2->已过期]',
  `use_time` datetime NULL DEFAULT NULL COMMENT '使用时间',
  `order_id` bigint(20) NULL DEFAULT NULL COMMENT '订单id',
  `order_sn` bigint(20) NULL DEFAULT NULL COMMENT '订单号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '优惠券领取历史记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sms_coupon_history
-- ----------------------------

-- ----------------------------
-- Table structure for sms_coupon_spu_category_relation
-- ----------------------------
DROP TABLE IF EXISTS `sms_coupon_spu_category_relation`;
CREATE TABLE `sms_coupon_spu_category_relation`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `coupon_id` bigint(20) NULL DEFAULT NULL COMMENT '优惠券id',
  `category_id` bigint(20) NULL DEFAULT NULL COMMENT '产品分类id',
  `category_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产品分类名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '优惠券分类关联' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sms_coupon_spu_category_relation
-- ----------------------------

-- ----------------------------
-- Table structure for sms_coupon_spu_relation
-- ----------------------------
DROP TABLE IF EXISTS `sms_coupon_spu_relation`;
CREATE TABLE `sms_coupon_spu_relation`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `coupon_id` bigint(20) NULL DEFAULT NULL COMMENT '优惠券id',
  `spu_id` bigint(20) NULL DEFAULT NULL COMMENT 'spu_id',
  `spu_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'spu_name',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '优惠券与产品关联' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sms_coupon_spu_relation
-- ----------------------------

-- ----------------------------
-- Table structure for sms_home_adv
-- ----------------------------
DROP TABLE IF EXISTS `sms_home_adv`;
CREATE TABLE `sms_home_adv`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名字',
  `pic` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片地址',
  `start_time` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '状态',
  `click_count` int(11) NULL DEFAULT NULL COMMENT '点击数',
  `url` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '广告详情连接地址',
  `note` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  `publisher_id` bigint(20) NULL DEFAULT NULL COMMENT '发布者',
  `auth_id` bigint(20) NULL DEFAULT NULL COMMENT '审核者',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '首页轮播广告' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sms_home_adv
-- ----------------------------

-- ----------------------------
-- Table structure for sms_home_subject
-- ----------------------------
DROP TABLE IF EXISTS `sms_home_subject`;
CREATE TABLE `sms_home_subject`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '专题名字',
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '专题标题',
  `sub_title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '专题副标题',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '显示状态',
  `url` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '详情连接',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  `img` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '专题图片地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '首页专题表【jd首页下面很多专题，每个专题链接新的页面，展示专题商品信息】' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sms_home_subject
-- ----------------------------

-- ----------------------------
-- Table structure for sms_home_subject_spu
-- ----------------------------
DROP TABLE IF EXISTS `sms_home_subject_spu`;
CREATE TABLE `sms_home_subject_spu`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '专题名字',
  `subject_id` bigint(20) NULL DEFAULT NULL COMMENT '专题id',
  `spu_id` bigint(20) NULL DEFAULT NULL COMMENT 'spu_id',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '专题商品' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sms_home_subject_spu
-- ----------------------------

-- ----------------------------
-- Table structure for sms_member_price
-- ----------------------------
DROP TABLE IF EXISTS `sms_member_price`;
CREATE TABLE `sms_member_price`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `sku_id` bigint(20) NULL DEFAULT NULL COMMENT 'sku_id',
  `member_level_id` bigint(20) NULL DEFAULT NULL COMMENT '会员等级id',
  `member_level_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '会员等级名',
  `member_price` decimal(18, 4) NULL DEFAULT NULL COMMENT '会员对应价格',
  `add_other` tinyint(1) NULL DEFAULT NULL COMMENT '可否叠加其他优惠[0-不可叠加优惠，1-可叠加]',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商品会员价格' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sms_member_price
-- ----------------------------

-- ----------------------------
-- Table structure for sms_seckill_promotion
-- ----------------------------
DROP TABLE IF EXISTS `sms_seckill_promotion`;
CREATE TABLE `sms_seckill_promotion`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '活动标题',
  `start_time` datetime NULL DEFAULT NULL COMMENT '开始日期',
  `end_time` datetime NULL DEFAULT NULL COMMENT '结束日期',
  `status` tinyint(4) NULL DEFAULT NULL COMMENT '上下线状态',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '秒杀活动' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sms_seckill_promotion
-- ----------------------------

-- ----------------------------
-- Table structure for sms_seckill_session
-- ----------------------------
DROP TABLE IF EXISTS `sms_seckill_session`;
CREATE TABLE `sms_seckill_session`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '场次名称',
  `start_time` datetime NULL DEFAULT NULL COMMENT '每日开始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '每日结束时间',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '启用状态',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '秒杀活动场次' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sms_seckill_session
-- ----------------------------

-- ----------------------------
-- Table structure for sms_seckill_sku_notice
-- ----------------------------
DROP TABLE IF EXISTS `sms_seckill_sku_notice`;
CREATE TABLE `sms_seckill_sku_notice`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `member_id` bigint(20) NULL DEFAULT NULL COMMENT 'member_id',
  `sku_id` bigint(20) NULL DEFAULT NULL COMMENT 'sku_id',
  `session_id` bigint(20) NULL DEFAULT NULL COMMENT '活动场次id',
  `subcribe_time` datetime NULL DEFAULT NULL COMMENT '订阅时间',
  `send_time` datetime NULL DEFAULT NULL COMMENT '发送时间',
  `notice_type` tinyint(1) NULL DEFAULT NULL COMMENT '通知方式[0-短信，1-邮件]',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '秒杀商品通知订阅' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sms_seckill_sku_notice
-- ----------------------------

-- ----------------------------
-- Table structure for sms_seckill_sku_relation
-- ----------------------------
DROP TABLE IF EXISTS `sms_seckill_sku_relation`;
CREATE TABLE `sms_seckill_sku_relation`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `promotion_id` bigint(20) NULL DEFAULT NULL COMMENT '活动id',
  `promotion_session_id` bigint(20) NULL DEFAULT NULL COMMENT '活动场次id',
  `sku_id` bigint(20) NULL DEFAULT NULL COMMENT '商品id',
  `seckill_price` decimal(10, 0) NULL DEFAULT NULL COMMENT '秒杀价格',
  `seckill_count` decimal(10, 0) NULL DEFAULT NULL COMMENT '秒杀总量',
  `seckill_limit` decimal(10, 0) NULL DEFAULT NULL COMMENT '每人限购数量',
  `seckill_sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '秒杀活动商品关联' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sms_seckill_sku_relation
-- ----------------------------

-- ----------------------------
-- Table structure for sms_sku_full_reduction
-- ----------------------------
DROP TABLE IF EXISTS `sms_sku_full_reduction`;
CREATE TABLE `sms_sku_full_reduction`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `sku_id` bigint(20) NULL DEFAULT NULL COMMENT 'spu_id',
  `full_price` decimal(18, 4) NULL DEFAULT NULL COMMENT '满多少',
  `reduce_price` decimal(18, 4) NULL DEFAULT NULL COMMENT '减多少',
  `add_other` tinyint(1) NULL DEFAULT NULL COMMENT '是否参与其他优惠',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商品满减信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sms_sku_full_reduction
-- ----------------------------
INSERT INTO `sms_sku_full_reduction` VALUES (1, 3, 10000.0000, 500.0000, NULL);
INSERT INTO `sms_sku_full_reduction` VALUES (2, 4, 10000.0000, 500.0000, NULL);
INSERT INTO `sms_sku_full_reduction` VALUES (3, 4, 10000.0000, 500.0000, NULL);
INSERT INTO `sms_sku_full_reduction` VALUES (4, 4, 1000.0000, 500.0000, NULL);
INSERT INTO `sms_sku_full_reduction` VALUES (5, 4, 10000.0000, 500.0000, NULL);
INSERT INTO `sms_sku_full_reduction` VALUES (6, 4, 10000.0000, 500.0000, NULL);
INSERT INTO `sms_sku_full_reduction` VALUES (7, 4, 10000.0000, 500.0000, NULL);
INSERT INTO `sms_sku_full_reduction` VALUES (8, 6, 1000.0000, 50.0000, NULL);
INSERT INTO `sms_sku_full_reduction` VALUES (9, 6, 1000.0000, 50.0000, NULL);
INSERT INTO `sms_sku_full_reduction` VALUES (10, 6, 1000.0000, 50.0000, NULL);
INSERT INTO `sms_sku_full_reduction` VALUES (11, 6, 1000.0000, 50.0000, NULL);

-- ----------------------------
-- Table structure for sms_sku_ladder
-- ----------------------------
DROP TABLE IF EXISTS `sms_sku_ladder`;
CREATE TABLE `sms_sku_ladder`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `sku_id` bigint(20) NULL DEFAULT NULL COMMENT 'spu_id',
  `full_count` int(11) NULL DEFAULT NULL COMMENT '满几件',
  `discount` decimal(4, 2) NULL DEFAULT NULL COMMENT '打几折',
  `price` decimal(18, 4) NULL DEFAULT NULL COMMENT '折后价',
  `add_other` tinyint(1) NULL DEFAULT NULL COMMENT '是否叠加其他优惠[0-不可叠加，1-可叠加]',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商品阶梯价格' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sms_sku_ladder
-- ----------------------------
INSERT INTO `sms_sku_ladder` VALUES (1, 3, 2, 0.98, NULL, 1);
INSERT INTO `sms_sku_ladder` VALUES (2, 4, 3, 0.95, NULL, 0);
INSERT INTO `sms_sku_ladder` VALUES (3, 4, 3, 0.95, NULL, 0);
INSERT INTO `sms_sku_ladder` VALUES (4, 4, 3, 0.95, NULL, 0);
INSERT INTO `sms_sku_ladder` VALUES (5, 4, 3, 0.95, NULL, 0);
INSERT INTO `sms_sku_ladder` VALUES (6, 4, 3, 0.95, NULL, 0);
INSERT INTO `sms_sku_ladder` VALUES (7, 4, 3, 0.95, NULL, 0);
INSERT INTO `sms_sku_ladder` VALUES (8, 6, 1, 1.00, NULL, 0);
INSERT INTO `sms_sku_ladder` VALUES (9, 6, 1, 1.00, NULL, 0);
INSERT INTO `sms_sku_ladder` VALUES (10, 6, 1, 1.00, NULL, 0);
INSERT INTO `sms_sku_ladder` VALUES (11, 6, 1, 1.00, NULL, 0);

-- ----------------------------
-- Table structure for sms_spu_bounds
-- ----------------------------
DROP TABLE IF EXISTS `sms_spu_bounds`;
CREATE TABLE `sms_spu_bounds`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `spu_id` bigint(20) NULL DEFAULT NULL,
  `grow_bounds` decimal(18, 4) NULL DEFAULT NULL COMMENT '成长积分',
  `buy_bounds` decimal(18, 4) NULL DEFAULT NULL COMMENT '购物积分',
  `work` tinyint(1) NULL DEFAULT NULL COMMENT '优惠生效情况[1111（四个状态位，从右到左）;0 - 无优惠，成长积分是否赠送;1 - 无优惠，购物积分是否赠送;2 - 有优惠，成长积分是否赠送;3 - 有优惠，购物积分是否赠送【状态位0：不赠送，1：赠送】]',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商品spu积分设置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sms_spu_bounds
-- ----------------------------
INSERT INTO `sms_spu_bounds` VALUES (1, 2, 1.0000, 1.0000, NULL);
INSERT INTO `sms_spu_bounds` VALUES (2, 3, 1.0000, 1.0000, NULL);
INSERT INTO `sms_spu_bounds` VALUES (3, 4, 400.0000, 400.0000, NULL);
INSERT INTO `sms_spu_bounds` VALUES (4, 6, 2.0000, 3.0000, NULL);

SET FOREIGN_KEY_CHECKS = 1;
