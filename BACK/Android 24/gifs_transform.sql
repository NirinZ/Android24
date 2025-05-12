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
-- Table structure for table `transform`
--

DROP TABLE IF EXISTS `transform`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transform` (
  `GifID` int unsigned NOT NULL AUTO_INCREMENT,
  `Race` varchar(100) NOT NULL,
  `From` varchar(16) DEFAULT NULL,
  `To` varchar(16) DEFAULT NULL,
  `Length` smallint unsigned NOT NULL DEFAULT '0',
  `Gif` varchar(2083) NOT NULL,
  PRIMARY KEY (`GifID`),
  KEY `races_gifs_T_idx` (`Race`),
  KEY `gifs_t_from_idx` (`From`),
  KEY `gifs_t_to_idx` (`To`),
  CONSTRAINT `gifs_t_from` FOREIGN KEY (`From`) REFERENCES `android24`.`transformations` (`TransformationAbbreviated`) ON UPDATE CASCADE,
  CONSTRAINT `gifs_t_races` FOREIGN KEY (`Race`) REFERENCES `android24`.`races` (`RaceName`) ON UPDATE CASCADE,
  CONSTRAINT `gifs_t_to` FOREIGN KEY (`To`) REFERENCES `android24`.`transformations` (`TransformationAbbreviated`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transform`
--

LOCK TABLES `transform` WRITE;
/*!40000 ALTER TABLE `transform` DISABLE KEYS */;
INSERT INTO `transform` VALUES (1,'Saiyan',NULL,'ss1',2,'https://media1.giphy.com/media/GRSnxyhJnPsaQy9YLn/200.gif'),(2,'Saiyan',NULL,'ss1',2,'https://c.tenor.com/TwjM4RJMcB0AAAAC/super-saiyan-transformation.gif'),(3,'Saiyan',NULL,'ss1',2,'https://steamuserimages-a.akamaihd.net/ugc/397805114598458777/97E522E3C9522419B2E2EF30F5B6430720139E8A/?imw=5000&imh=5000&ima=fit&impolicy=Letterbox&imcolor=%23000000&letterbox=false'),(4,'Saiyan',NULL,'ss2',4,'https://64.media.tumblr.com/3f6baa5e517f496de46463a962e73214/tumblr_inline_nlet51hZX31rx7wfq.gif'),(5,'Saiyan','ss1','ss2',3,'https://tenor.com/bi7DI.gif'),(6,'Saiyan',NULL,'ss1',2,'https://64.media.tumblr.com/241f6897f820d7c3b3c9681d7c607e5d/78b1fe1421677ed0-85/s500x750/4d9537aa470ee345501f28b538637f71857d860f.gif'),(7,'Saiyan','ss1','ss2',11,'https://tenor.com/bqlZP.gif'),(8,'Saiyan',NULL,'ss2',2,'https://i.pinimg.com/originals/f4/e1/2b/f4e12b0c918319cde0daa56a90b8572f.gif'),(9,'Saiyan','ss1','ss2',5,'https://thumbs.gfycat.com/DefenselessGiddyAtlasmoth-max-1mb.gif'),(10,'Saiyan','ss1','ss3',1,'https://images-wixmp-ed30a86b8c4ca887773594c2.wixmp.com/f/033f6ad0-d791-43ca-9735-7a895f690b59/dbkklwm-e370fa84-345b-4a55-a380-92454188208c.gif?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1cm46YXBwOjdlMGQxODg5ODIyNjQzNzNhNWYwZDQxNWVhMGQyNmUwIiwiaXNzIjoidXJuOmFwcDo3ZTBkMTg4OTgyMjY0MzczYTVmMGQ0MTVlYTBkMjZlMCIsIm9iaiI6W1t7InBhdGgiOiJcL2ZcLzAzM2Y2YWQwLWQ3OTEtNDNjYS05NzM1LTdhODk1ZjY5MGI1OVwvZGJra2x3bS1lMzcwZmE4NC0zNDViLTRhNTUtYTM4MC05MjQ1NDE4ODIwOGMuZ2lmIn1dXSwiYXVkIjpbInVybjpzZXJ2aWNlOmZpbGUuZG93bmxvYWQiXX0.DBHyI6jwNU_Zwbnx7JqxherPft7JLsQSO6TRU-T3tNM'),(11,'saiyan','ss2','ss3',5,'https://c.tenor.com/Z3DYCffBSt4AAAAM/super-saiyan3-goku.gif'),(12,'saiyan',NULL,'ss1',1,'https://i.imgur.com/NvGsk2Q.gif'),(13,'saiyan',NULL,'ss3',5,'https://c.tenor.com/Dj49ConR8AwAAAAC/goku-angry.gif'),(14,'saiyan',NULL,'ss3',6,'https://cdn.discordapp.com/attachments/728653495900569603/890952385780912188/gotenks-fusion.gif'),(15,'saiyan','ss3','ss3',1,'https://c.tenor.com/LjLQOiXlnkUAAAAC/dbz-goku.gif'),(16,'saiyan','ss2','ss2',1,'https://c.tenor.com/atrxJ4ck5QgAAAAC/dragon-ball-z-power.gif'),(17,'saiyan',NULL,'ss1',6,'https://c.tenor.com/990mRL_YxcsAAAAC/gohan-son-gohan.gif'),(18,'saiyan','ss1','ss1',1,'https://c.tenor.com/PVhZJiIDE_AAAAAC/fv-goku.gif'),(19,'saiyan','ss1','ss1',1,'https://cdn.discordapp.com/attachments/774939690783604797/890258643117805659/1479423138_tumblr_odeumekEiJ1r2hy3ro2_540.gif'),(20,'saiyan','ss2','ss2',1,'https://c.tenor.com/GIeOTZX2dkEAAAAC/gohan-ssj2.gif'),(21,'saiyan','ss4','ss4',1,'https://cdn.discordapp.com/attachments/728653495900569603/900005772765835324/unknown.gif'),(22,'saiyan',NULL,'ss4',3,'http://pa1.narvii.com/6541/bb283c13619363cee3c221128cd83b59b3a343f0_00.gif'),(23,'Saiyan','ss4',NULL,2,'https://24.media.tumblr.com/a49ec9d4e581b82c8a7e682eb18d05c9/tumblr_n4x6f6m7SZ1s21c3eo1_500.gif'),(24,'saiyan',NULL,'ssb',9,'https://thumbs.gfycat.com/BeneficialThoseIncatern-max-1mb.gif'),(25,'saiyan','ss2','ss3',7,'http://pa1.narvii.com/6241/b172989c31f3b6531c948195d6269c573d978542_00.gif'),(26,'saiyan',NULL,'ss4',7,'https://cdnb.artstation.com/p/assets/images/images/003/302/935/original/timothy-de-guzman-saiyan-transformation.gif?1472196810&dl=1'),(27,'saiyan','ss2','ss4',6,'https://i.makeagif.com/media/12-27-2015/Fi7kxa.gif'),(28,'Saiyan','ss1','ssg',15,'https://c.tenor.com/1IGWuMpgKsUAAAAM/transformation-super-saiyan-god.gif'),(29,'Saiyan','ss2','ssg',7,'http://shrani.si/f/T/ce/4z1caxoF/ssg-red-2.gif'),(30,'Saiyan','ss1','ssg',8,'https://c.tenor.com/PM1KoxaucnIAAAAM/goku-dbs-broly.gif'),(31,'Saiyan',NULL,'ssb',16,'https://i.makeagif.com/media/1-16-2016/F3Bcbw.gif'),(32,'Saiyan','ssg','ssb',10,'https://thumbs.gfycat.com/GrayBadArawana-size_restricted.gif'),(33,'Saiyan',NULL,'ss1',7,'https://cdn.discordapp.com/attachments/890544910057480213/902486559046127636/giphy.gif'),(34,'Saiyan',NULL,'ss1',1,'https://i.pinimg.com/originals/3a/54/04/3a540407424edf0fe6013187288960bd.gif');
/*!40000 ALTER TABLE `transform` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-04-29 15:51:31
