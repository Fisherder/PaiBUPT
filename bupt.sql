-- MySQL dump 10.13  Distrib 8.0.39, for Win64 (x86_64)
--
-- Host: localhost    Database: shop-second
-- ------------------------------------------------------
-- Server version	8.0.39

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `goods`
--

DROP TABLE IF EXISTS `goods`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `goods` (
  `goods_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL COMMENT '发布人id',
  `category_id` int DEFAULT NULL COMMENT '分类id',
  `type` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '0:闲置 1：求购',
  `goods_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '商品名称',
  `goods_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '简介',
  `goods_price` decimal(10,2) DEFAULT NULL COMMENT '价格',
  `user_name` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '联系人',
  `phone` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '联系电话',
  `image` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT '图片',
  `wx_num` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '微信号',
  `status` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '0' COMMENT '0:上架 1：下架',
  `sell_status` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '0' COMMENT '0:未出售 1：已出售',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `set_index` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '0' COMMENT '0:未推荐 1：推荐到首页',
  `address` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT '发布/求购\r\n地址',
  `delete_status` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '0' COMMENT '0:未删除 1：删除',
  PRIMARY KEY (`goods_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `goods`
--

LOCK TABLES `goods` WRITE;
/*!40000 ALTER TABLE `goods` DISABLE KEYS */;
INSERT INTO `goods` VALUES (43,1,6,'0','1','1',1.00,'1','1','http://localhost:8089/images/c5d0948a-ee80-4abc-bff1-e950a0c5bf82.png','1','1','0','2025-02-13 19:02:00','0','1','1'),(44,1,6,'0','2','2',2.00,'2','2','http://localhost:8089/images/a2777bf1-1d85-442d-b341-ae1101952073.png','2','0','0','2025-02-13 22:53:02','0','2','0'),(45,1,6,'0','1','1',1.00,'1','1','http://localhost:8089/images/5b0645c3-b8e3-40a5-bc6a-32a87f3a4721.png','1','0','0','2025-02-14 00:26:54','0','1','0'),(46,1,6,'1','3','1',1.00,'1','1','http://localhost:8089/images/5b0645c3-b8e3-40a5-bc6a-32a87f3a4721.png','1','0','0','2025-02-14 00:27:02','0','1','0'),(47,1,6,'2','10','1',1.00,'1','1','http://localhost:8089/images/0eb6ac97-6220-4987-889f-738a94498d9a.png','1','0','0','2025-02-26 14:19:38','0','1','0');
/*!40000 ALTER TABLE `goods` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `goods_category`
--

DROP TABLE IF EXISTS `goods_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `goods_category` (
  `category_id` int NOT NULL AUTO_INCREMENT,
  `category_name` varchar(36) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '名称',
  `order_num` int DEFAULT NULL COMMENT '序号',
  PRIMARY KEY (`category_id` DESC)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `goods_category`
--

LOCK TABLES `goods_category` WRITE;
/*!40000 ALTER TABLE `goods_category` DISABLE KEYS */;
INSERT INTO `goods_category` VALUES (6,'食物',3),(5,'书籍',2),(4,'衣物',1),(2,'电子产品',0);
/*!40000 ALTER TABLE `goods_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_banner`
--

DROP TABLE IF EXISTS `sys_banner`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_banner` (
  `ban_id` int NOT NULL AUTO_INCREMENT COMMENT '广告id',
  `goods_id` int DEFAULT NULL COMMENT '菜品id',
  `title` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '标题',
  `images` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '图片',
  `status` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT ' 0:下架 1：上架',
  `order_num` int DEFAULT NULL COMMENT '序号',
  PRIMARY KEY (`ban_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_banner`
--

LOCK TABLES `sys_banner` WRITE;
/*!40000 ALTER TABLE `sys_banner` DISABLE KEYS */;
INSERT INTO `sys_banner` VALUES (4,45,'test','http://localhost:8089/images/32362e11-6890-4370-93c3-a8ded5143386.jpeg','0',1),(5,44,'ceshi','http://localhost:8089/images/324587cd-1612-4f60-8f8d-97d7cb169ed1.jpeg','0',2);
/*!40000 ALTER TABLE `sys_banner` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_menu`
--

DROP TABLE IF EXISTS `sys_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_menu` (
  `menu_id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `parent_id` int DEFAULT NULL COMMENT '上级id',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '菜单名称',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '权限字段',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '图标',
  `order_num` int DEFAULT NULL COMMENT '序号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `parent_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '上级菜单名称',
  `type` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '1:菜单 2：按钮',
  `path` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '路由名称',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_menu`
--

LOCK TABLES `sys_menu` WRITE;
/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;
INSERT INTO `sys_menu` VALUES (40,0,'用户管理','sys:userList','Wallet',2,'2025-02-19 23:48:59','顶级菜单','1','/userList'),(41,0,'菜单管理','sys:menuList','Menu',3,'2025-02-19 23:49:34','顶级菜单','1','/menuList'),(42,0,'商品分类','sys:goodsType','UserFilled',4,'2025-02-19 23:49:50','顶级菜单','1','/goodsType'),(43,0,'商品管理','sys:unusedList','Memo',5,'2025-02-19 23:50:08','顶级菜单','1','/unusedList'),(44,0,'订单管理','sys:unusedOrder','Monitor',6,'2025-02-19 23:50:22','顶级菜单','1','/unusedOrder'),(45,0,'广告管理','sys:bannerList','Calendar',7,'2025-02-19 23:50:37','顶级菜单','1','/bannerList'),(46,0,'评论管理','sys:commentList','Calendar',8,'2025-02-19 23:50:49','顶级菜单','1','/commentList'),(47,0,'管理员管理','sys:adminUser','Histogram',1,'2025-02-20 00:03:20','顶级菜单','1','/adminUser'),(48,47,'新增','sys:adminUser:add','',1,'2025-02-20 00:03:55','管理员管理','2',''),(49,40,'新增','sys:userList:add','',1,'2025-02-20 00:08:58','用户管理','2',''),(50,41,'新增','sys:menuList:add','',1,'2025-02-20 00:09:58','菜单管理','2',''),(51,41,'编辑','sys:menuList:edit','',2,'2025-02-20 00:10:25','菜单管理','2',''),(52,41,'删除','sys:menuList','',3,'2025-02-20 00:10:59','菜单管理','2','');
/*!40000 ALTER TABLE `sys_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user` (
  `user_id` int NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '登录账户',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '登录密码，密码暂设为与用户名一致',
  `nick_name` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '姓名',
  `phone` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '电话',
  `sex` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '性别',
  `status` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '状态 0：启用 1：停用',
  `is_admin` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`user_id` DESC) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES (3,'1','c4ca4238a0b923820dcc509a6f75849b','超级管理员','11','0','0','1'),(2,'123','202cb962ac59075b964b07152d234b70','管理员','123','0','0',NULL);
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_menu`
--

DROP TABLE IF EXISTS `user_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_menu` (
  `user_menu_id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int DEFAULT NULL COMMENT '用户id',
  `menu_id` int DEFAULT NULL COMMENT '菜单id',
  PRIMARY KEY (`user_menu_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_menu`
--

LOCK TABLES `user_menu` WRITE;
/*!40000 ALTER TABLE `user_menu` DISABLE KEYS */;
INSERT INTO `user_menu` VALUES (37,2,44),(38,2,45);
/*!40000 ALTER TABLE `user_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wx_user`
--

DROP TABLE IF EXISTS `wx_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `wx_user` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `nick_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '昵称',
  `name` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '姓名',
  `picture` text COLLATE utf8mb4_bin COMMENT '头像',
  `phone` varchar(18) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '电话',
  `username` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '登录用户',
  `password` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '密码',
  `status` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '0' COMMENT '0：停用 1：启用',
  PRIMARY KEY (`user_id` DESC)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wx_user`
--

LOCK TABLES `wx_user` WRITE;
/*!40000 ALTER TABLE `wx_user` DISABLE KEYS */;
INSERT INTO `wx_user` VALUES (5,NULL,NULL,NULL,'1','3','f379eaf3c831b04de153469d1bec345e','0'),(3,NULL,NULL,NULL,'3','2','f379eaf3c831b04de153469d1bec345e','0'),(2,NULL,NULL,NULL,'12','12','c20ad4d76fe97759aa27a0c99bff6710','0'),(1,NULL,NULL,NULL,'1','1','c4ca4238a0b923820dcc509a6f75849b','0');
/*!40000 ALTER TABLE `wx_user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-02-26 17:20:41
