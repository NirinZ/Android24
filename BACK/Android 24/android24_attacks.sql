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
-- Table structure for table `attacks`
--

DROP TABLE IF EXISTS `attacks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `attacks` (
  `AttackName` varchar(100) NOT NULL,
  `AttackAbbreviated` varchar(16) NOT NULL,
  `AttackPowerUp` int unsigned NOT NULL DEFAULT '1',
  `DefencePowerUp` int unsigned NOT NULL DEFAULT '1',
  `SpeedPowerUp` int unsigned NOT NULL DEFAULT '1',
  `KiConsumption` int unsigned NOT NULL DEFAULT '1',
  `Counter` tinyint unsigned NOT NULL DEFAULT '0',
  `AttackType` enum('Strike','Ki','Defence','Charge') NOT NULL,
  PRIMARY KEY (`AttackName`,`AttackAbbreviated`),
  UNIQUE KEY `attackscol_UNIQUE` (`AttackAbbreviated`),
  UNIQUE KEY `AttackName_UNIQUE` (`AttackName`),
  CONSTRAINT `attacks_AttackName_shop_Name` FOREIGN KEY (`AttackName`) REFERENCES `shop` (`Name`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='The table that will describe all the attacks';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attacks`
--

LOCK TABLES `attacks` WRITE;
/*!40000 ALTER TABLE `attacks` DISABLE KEYS */;
INSERT INTO `attacks` VALUES ('Charge','Charge',1,1,1,1,0,'Charge'),('Death Ball','dbl',10,1,1,200,0,'Ki'),('Defence','Defence',1,1,1,1,0,'Defence'),('Kamehameha','kameha',5,2,2,100,0,'Ki'),('Ki','Ki',1,1,1,1,0,'Ki'),('Special Beam Cannon','spbmcnn',13,1,5,30,0,'Ki'),('Strike','Strike',1,1,1,1,0,'Strike'),('Tri-Beam','3bm',3,1,3,20,0,'Ki');
/*!40000 ALTER TABLE `attacks` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-04-29 15:51:25
