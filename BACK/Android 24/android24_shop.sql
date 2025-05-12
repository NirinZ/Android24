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
-- Table structure for table `shop`
--

DROP TABLE IF EXISTS `shop`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `shop` (
  `Name` varchar(100) NOT NULL,
  `ForcedRace` varchar(100) DEFAULT NULL,
  `Depended` varchar(100) DEFAULT NULL,
  `LinkedTo` varchar(100) DEFAULT NULL,
  `Storey` tinyint unsigned DEFAULT NULL,
  `Group` varchar(45) DEFAULT NULL,
  `Cost` bigint unsigned NOT NULL DEFAULT '0',
  `MinimalLevel` int unsigned NOT NULL DEFAULT '0',
  `Description` varchar(2083) DEFAULT 'There is no descriptoin avalible',
  `Gif` varchar(2083) DEFAULT NULL,
  `Display` tinyint NOT NULL DEFAULT '1',
  PRIMARY KEY (`Name`),
  UNIQUE KEY `Depended_UNIQUE` (`Depended`),
  KEY `shop_LinkedTo_shop_Name` (`LinkedTo`) /*!80000 INVISIBLE */,
  KEY `shop_Depended_shop_Name` (`Depended`),
  KEY `shop_ForcedRace_races_RaceName` (`ForcedRace`),
  CONSTRAINT `shop_Depended_shop_Name` FOREIGN KEY (`Depended`) REFERENCES `shop` (`Name`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `shop_ForcedRace_races_RaceName` FOREIGN KEY (`ForcedRace`) REFERENCES `races` (`RaceName`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `shop_LinkedTo_shop_Name` FOREIGN KEY (`LinkedTo`) REFERENCES `shop` (`Name`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='The table that will describe all the items';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shop`
--

LOCK TABLES `shop` WRITE;
/*!40000 ALTER TABLE `shop` DISABLE KEYS */;
INSERT INTO `shop` VALUES ('Charge',NULL,NULL,NULL,NULL,NULL,0,0,'There is no descriptoin avalible',NULL,0),('Death Ball',NULL,NULL,NULL,NULL,NULL,700,6,'Frieza planet bluster','https://static1.srcdn.com/wordpress/wp-content/uploads/2016/12/Frieza-Death-Ball-attack-Dragon-Ball-Z.jpg?q=50&fit=crop&w=740&h=370&dpr=1.5',1),('Defence',NULL,NULL,NULL,NULL,NULL,0,0,'There is no descriptoin avalible',NULL,0),('Kamehameha',NULL,NULL,NULL,NULL,NULL,1000,10,'The most iconic attack','https://c.tenor.com/hiXlfSMxgkUAAAAC/big-kamehameha-gogeta-blue.gif',1),('Ki',NULL,NULL,NULL,NULL,NULL,0,0,'There is no descriptoin avalible',NULL,0),('Special Beam Cannon',NULL,NULL,NULL,NULL,NULL,1600,10,'Picollo drail gun - makankosapado','https://i.pinimg.com/originals/c0/0d/0f/c00d0f07bcef248eb6a0ee0aa8eb1556.gif',1),('Strike',NULL,NULL,NULL,NULL,NULL,0,0,'There is no descriptoin avalible',NULL,0),('Super Saiyan 1','Saiyan',NULL,NULL,1,NULL,3000,10,'The super saiyan','https://i0.wp.com/comicbookdebate.com/wp-content/uploads/2019/07/GrotesqueGorgeousAmurratsnake-size_restricted.gif?resize=533%2C300&ssl=1',1),('Super Saiyan 2','Saiyan',NULL,'Super Saiyan 1',2,NULL,5000,15,'The super Saiyan that second pass the super saiyan','http://pa1.narvii.com/6754/b8878a43571f22a30fb7965ac4103f6d1d8e786b_00.gif',1),('Super Saiyan 3','Saiyan',NULL,'Super Saiyan 2',3,NULL,20000,25,'The super saiyan that second pass the super saiyan 2. This is super sayian 3','https://i.pinimg.com/originals/1c/7e/15/1c7e15ad9d023d3bd78203f74ca59af2.gif',1),('Super Saiyan 4','Saiyan',NULL,'Super Saiyan 3',4,NULL,100000,30,'The ultimate saiyan form combined with a grate ape!','https://c.tenor.com/L1cDjAxDr9MAAAAC/breno-bruhzil.gif',1),('Super Saiyan Blue','Saiyan',NULL,'Super Saiyan God',5,NULL,5000000,60,'A super saiyan god that became a super saiyan again!','https://i.pinimg.com/originals/ac/dd/f5/acddf555f70a09a4ee6e03c0487490ab.gif',1),('Super Saiyan God','Saiyan',NULL,'Super Saiyan 3',4,NULL,1000000,50,'The god legend of the saiyans','https://c.tenor.com/_zcC_B_34kwAAAAC/super-saiyan-god-fire.gif',1),('Tri-Beam',NULL,NULL,NULL,NULL,NULL,50,2,'Tien\'s original attack.','https://static1.srcdn.com/wordpress/wp-content/uploads/2016/12/Tien-Tri-Beam-Cell-Dragon-Ball-Z.jpg?q=50&fit=crop&w=740&h=370&dpr=1.5',1);
/*!40000 ALTER TABLE `shop` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-04-29 15:51:18
