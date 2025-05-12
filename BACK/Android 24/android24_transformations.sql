-- MySQL dump 10.13  Distrib 8.0.24, for Win64 (x86_64)
--
-- Host: 159.89.111.155    Database: android24
-- ------------------------------------------------------
-- Server version	8.0.28-0ubuntu0.20.04.3

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `transformations`
--

DROP TABLE IF EXISTS `transformations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transformations` (
  `TransformationName` varchar(100) NOT NULL,
  `TransformationAbbreviated` varchar(16) NOT NULL,
  `AttackPowerUp` int unsigned NOT NULL DEFAULT '1',
  `DefencePowerUp` int unsigned NOT NULL DEFAULT '1',
  `SpeedPowerUp` int unsigned NOT NULL DEFAULT '1',
  `KiConsumption` decimal(9,2) unsigned NOT NULL DEFAULT '1.00',
  `SoloTransformation` tinyint unsigned NOT NULL DEFAULT '1',
  `Color` int unsigned DEFAULT NULL,
  PRIMARY KEY (`TransformationName`,`TransformationAbbreviated`),
  UNIQUE KEY `TransformationName_UNIQUE` (`TransformationName`),
  UNIQUE KEY `TransformationShortcut_UNIQUE` (`TransformationAbbreviated`),
  CONSTRAINT `transformations_TransformationName_shop_Name` FOREIGN KEY (`TransformationName`) REFERENCES `shop` (`Name`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transformations`
--

LOCK TABLES `transformations` WRITE;
/*!40000 ALTER TABLE `transformations` DISABLE KEYS */;
INSERT INTO `transformations` VALUES ('Super Saiyan 1','ss1',50,50,50,1.30,1,16776960),('Super Saiyan 2','ss2',100,100,100,1.60,1,16776960),('Super Saiyan 3','ss3',400,400,400,2.70,1,16776960),('Super Saiyan 4','ss4',40000,40000,40000,4.00,1,14942215),('Super Saiyan Blue','ssb',50000000,50000000,50000000,2.20,1,5289413),('Super Saiyan God','ssg',1000000,1000000,1000000,1.70,1,16141131);
/*!40000 ALTER TABLE `transformations` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-04-29 15:51:23
