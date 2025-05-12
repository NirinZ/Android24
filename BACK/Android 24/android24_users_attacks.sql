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
-- Table structure for table `users_attacks`
--

DROP TABLE IF EXISTS `users_attacks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users_attacks` (
  `UserID` bigint unsigned NOT NULL,
  `AttackAbbreviated` varchar(16) NOT NULL,
  PRIMARY KEY (`UserID`,`AttackAbbreviated`),
  KEY `users_attacks_AttackName_attacks_AttackName` (`AttackAbbreviated`),
  CONSTRAINT `users_attacks_AttackName_attacks_AttackName` FOREIGN KEY (`AttackAbbreviated`) REFERENCES `attacks` (`AttackAbbreviated`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `users_attacks_UserID_users_data_UserID` FOREIGN KEY (`UserID`) REFERENCES `users_data` (`UserID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='The table that is connecting the users to their attacks';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users_attacks`
--

LOCK TABLES `users_attacks` WRITE;
/*!40000 ALTER TABLE `users_attacks` DISABLE KEYS */;
INSERT INTO `users_attacks` VALUES (739532349280354404,'3bm'),(739532349280354404,'dbl'),(836541404691038279,'dbl'),(739532349280354404,'kameha'),(739532349280354404,'spbmcnn');
/*!40000 ALTER TABLE `users_attacks` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-04-29 15:51:26
