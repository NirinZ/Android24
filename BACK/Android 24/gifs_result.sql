-- MySQL dump 10.13  Distrib 8.0.24, for Win64 (x86_64)
--
-- Host: 159.89.111.155    Database: gifs
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
-- Table structure for table `result`
--

DROP TABLE IF EXISTS `result`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `result` (
  `GifID` int unsigned NOT NULL AUTO_INCREMENT,
  `ARace` varchar(100) DEFAULT NULL,
  `ATransformation` varchar(16) DEFAULT NULL,
  `AAttack` varchar(16) NOT NULL,
  `DRace` varchar(100) DEFAULT NULL,
  `DTransformation` varchar(16) DEFAULT NULL,
  `DAttack` varchar(16) DEFAULT NULL,
  `Power` tinyint NOT NULL DEFAULT '0',
  `Length` smallint NOT NULL DEFAULT '0',
  `Gif` varchar(2083) NOT NULL,
  PRIMARY KEY (`GifID`),
  KEY `result_a_race_races_idx` (`ARace`),
  KEY `result_a_transforamtion_abbreviated_idx` (`ATransformation`),
  KEY `result_a_attack_attcks_abbreviated_idx` (`AAttack`),
  KEY `result_d_race_races_idx` (`DRace`),
  KEY `result_d_transforamtion_abbreviated_idx` (`DTransformation`),
  KEY `result_d_attack_attcks_abbreviated_idx` (`DAttack`),
  CONSTRAINT `result_a_attack_attcks_abbreviated` FOREIGN KEY (`AAttack`) REFERENCES `android24`.`attacks` (`AttackAbbreviated`),
  CONSTRAINT `result_a_race_races` FOREIGN KEY (`ARace`) REFERENCES `android24`.`races` (`RaceName`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `result_a_transforamtion_transformations_abbreviated` FOREIGN KEY (`ATransformation`) REFERENCES `android24`.`transformations` (`TransformationAbbreviated`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `result_d_attack_attcks_abbreviated` FOREIGN KEY (`DAttack`) REFERENCES `android24`.`attacks` (`AttackAbbreviated`),
  CONSTRAINT `result_d_race_races` FOREIGN KEY (`DRace`) REFERENCES `android24`.`races` (`RaceName`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `result_d_transforamtion_transformations_abbreviated` FOREIGN KEY (`DTransformation`) REFERENCES `android24`.`transformations` (`TransformationAbbreviated`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `result`
--

LOCK TABLES `result` WRITE;
/*!40000 ALTER TABLE `result` DISABLE KEYS */;
INSERT INTO `result` VALUES (1,'saiyan','ss1','Strike',NULL,NULL,'Strike',50,2,'https://jctunesmusic.files.wordpress.com/2015/06/tumblr_n9v7czpk5g1rgagxfo1_500.gif?w=473&zoom=2'),(18,'saiyan','ss2','Strike','saiyan','ss2','Strike',70,2,'https://c.tenor.com/58beV6x1LV8AAAAC/fighting-goku.gif'),(19,NULL,NULL,'Strike',NULL,NULL,'Defence',0,7,'https://thumbs.gfycat.com/ElegantSplendidAbyssiniancat-size_restricted.gif'),(20,'saiyan','ss1','Strike','frieza',NULL,'Strike',55,1,'https://c.tenor.com/7EM0meM4cusAAAAC/baston-dbz.gif'),(21,'saiyan',NULL,'Strike','frieza',NULL,'Strike',75,2,'https://c.tenor.com/J3Lc1VDcZ4UAAAAC/dbz-dragon-ball-z.gif'),(22,'saiyan','ss3','Strike',NULL,NULL,'Strike',50,1,'https://c.tenor.com/O2fQFpxxKL0AAAAC/dbz.gif'),(23,'saiyan','ss1','Strike',NULL,NULL,'Defence',-1,1,'https://c.tenor.com/Xmmw9wig9DkAAAAC/android17-dragonball-z.gif'),(24,NULL,NULL,'Strike',NULL,NULL,NULL,70,3,'https://c.tenor.com/zKCvzLmL3_sAAAAC/android-android17.gif'),(25,NULL,NULL,'Strike',NULL,NULL,'Defence',-1,1,'https://c.tenor.com/JmpPGHmsHnQAAAAC/dbz-android17.gif'),(26,NULL,NULL,'Strike','saiyan','ss1','Strike',100,3,'https://c.tenor.com/kYjc64VU9G8AAAAC/anime-fight-fight.gif'),(27,'frieza',NULL,'Ki','saiyan',NULL,'Defence',10,4,'https://c.tenor.com/ZUniFYV3oXAAAAAC/ki-freeza.gif'),(28,'saiyan',NULL,'Strike',NULL,NULL,'Strike',50,5,'https://c.tenor.com/XIKDIjl9SxcAAAAC/vegeta-fighting.gif'),(29,'saiyan',NULL,'kameha',NULL,NULL,NULL,100,5,'https://c.tenor.com/AqvAjdnCbOgAAAAC/goku-dragon-ball-gt.gif'),(30,'Saiyan','ssb','Strike','Saiyan',NULL,'Strike',70,4,'https://c.tenor.com/J0w5NhhzcXAAAAAd/dragon-ball.gif');
/*!40000 ALTER TABLE `result` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-04-29 15:51:32
