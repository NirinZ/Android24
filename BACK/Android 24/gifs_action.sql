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
-- Table structure for table `action`
--

DROP TABLE IF EXISTS `action`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `action` (
  `GifID` int unsigned NOT NULL AUTO_INCREMENT,
  `Race` varchar(100) NOT NULL,
  `Transformation` varchar(16) DEFAULT NULL,
  `Attack` varchar(16) NOT NULL,
  `Length` smallint NOT NULL DEFAULT '0',
  `Gif` varchar(2083) NOT NULL,
  PRIMARY KEY (`GifID`),
  KEY `action_race_races_idx` (`Race`),
  KEY `action_transforamtion_abbreviated_idx` (`Transformation`),
  KEY `action_attack_attcks_abbreviated_idx` (`Attack`),
  CONSTRAINT `action_attack_attcks_abbreviated` FOREIGN KEY (`Attack`) REFERENCES `android24`.`attacks` (`AttackAbbreviated`),
  CONSTRAINT `action_race_races` FOREIGN KEY (`Race`) REFERENCES `android24`.`races` (`RaceName`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `action_transforamtion_transformations_abbreviated` FOREIGN KEY (`Transformation`) REFERENCES `android24`.`transformations` (`TransformationAbbreviated`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `action`
--

LOCK TABLES `action` WRITE;
/*!40000 ALTER TABLE `action` DISABLE KEYS */;
INSERT INTO `action` VALUES (1,'Saiyan','ss2','kameha',4,'https://giffiles.alphacoders.com/105/105477.gif'),(2,'saiyan','ss1','kameha',8,'https://thumbs.gfycat.com/ExcellentAgileIberianlynx-size_restricted.gif'),(3,'saiyan',NULL,'kameha',4,'https://i.pinimg.com/originals/27/5b/27/275b276a3c26ca30ed6d787bd8ca5a02.gif'),(4,'saiyan',NULL,'kameha',1,'https://c.tenor.com/IM60lqQKKY0AAAAC/kid-goku-kamehameha.gif'),(5,'saiyan','ss1','kameha',2,'https://thumbs.gfycat.com/ComposedBleakIndusriverdolphin-size_restricted.gif'),(6,'saiyan','ss3','kameha',3,'https://i2.wp.com/media.giphy.com/media/hUgw1hdzmZjAA/giphy.gif?w=696&ssl=1'),(7,'Saiyan',NULL,'kameha',1,'http://pa1.narvii.com/6508/efb54c311e5e5a1436d06f8aa18502bf0a32ce5c_00.gif'),(8,'saiyan',NULL,'kameha',1,'http://pa1.narvii.com/6544/49b9f8a6c99e9c2e03db9374b07db877d8bf0c74_00.gif'),(9,'frieza',NULL,'dbl',5,'https://i.makeagif.com/media/5-09-2015/5l8N5A.gif'),(10,'saiyan','ss3','kameha',5,'https://i.pinimg.com/originals/31/37/89/3137899f774569326119b5992d3a4409.gif'),(11,'saiyan','ss1','Strike',1,'https://c.tenor.com/Y0g04IROckMAAAAC/goten-dragon-ball-z.gif'),(12,'saiyan',NULL,'Ki',1,'https://c.tenor.com/SOpsxBfR7iwAAAAM/good-morning-holidays-ki-blast.gif'),(13,'saiyan','ss2','Ki',2,'http://pa1.narvii.com/6547/6c1fa2ad459fe78544242fbc7a9208401568d9da_00.gif'),(14,'saiyan','ss2','Ki',2,'https://c.tenor.com/QNSrTsVS8dgAAAAM/ki-blast.gif'),(15,'saiyan','ss1','Ki',1,'http://pa1.narvii.com/6784/0884d89f3dbbbbccac4db34a0db9d0ad2e84598f_00.gif'),(16,'saiyan','ss1','Ki',2,'https://i.pinimg.com/originals/49/6c/7a/496c7add1c95db9abb7c3d13b9c42113.gif'),(17,'Saiyan','ssg','kameha',2,'https://cdn190.picsart.com/232231023010202.gif?to=min&r=640');
/*!40000 ALTER TABLE `action` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-04-29 15:51:29
