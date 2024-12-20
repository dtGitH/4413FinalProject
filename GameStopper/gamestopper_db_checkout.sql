CREATE DATABASE  IF NOT EXISTS `gamestopper_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `gamestopper_db`;
-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: gamestopper_db
-- ------------------------------------------------------
-- Server version	8.0.40

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
-- Table structure for table `checkout`
--

DROP TABLE IF EXISTS `checkout`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `checkout` (
  `checkout_id` int NOT NULL AUTO_INCREMENT,
  `user_uuid` varchar(36) NOT NULL,
  `billing_address` text NOT NULL,
  `shipping_address` text NOT NULL,
  `credit_card` varchar(16) NOT NULL,
  `total_amount` decimal(10,2) NOT NULL,
  `status` varchar(10) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`checkout_id`),
  KEY `user_uuid` (`user_uuid`),
  CONSTRAINT `checkout_ibfk_1` FOREIGN KEY (`user_uuid`) REFERENCES `users` (`user_uuid`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `checkout`
--

LOCK TABLES `checkout` WRITE;
/*!40000 ALTER TABLE `checkout` DISABLE KEYS */;
INSERT INTO `checkout` VALUES (1,'d5f03c97-aade-4fc6-85ea-b7fc5c5035f7','a','a','1456565658074',179.97,'ACCEPT','2024-12-20 08:47:15','2024-12-20 08:47:15'),(2,'d5f03c97-aade-4fc6-85ea-b7fc5c5035f7','a','a','2343241',59.99,'ACCEPT','2024-12-20 08:47:54','2024-12-20 08:47:54'),(3,'d5f03c97-aade-4fc6-85ea-b7fc5c5035f7','a','a','2343241',59.99,'ACCEPT','2024-12-20 08:48:11','2024-12-20 08:48:11'),(4,'d5f03c97-aade-4fc6-85ea-b7fc5c5035f7','a','a','2343241',59.99,'ACCEPT','2024-12-20 08:48:25','2024-12-20 08:48:25'),(5,'d5f03c97-aade-4fc6-85ea-b7fc5c5035f7','s','a','1456565658074',39.99,'ACCEPTED','2024-12-20 08:52:33','2024-12-20 08:52:33'),(6,'d5f03c97-aade-4fc6-85ea-b7fc5c5035f7','a','a','1456565658074',69.99,'ACCEPTED','2024-12-20 08:52:45','2024-12-20 08:52:45'),(7,'d5f03c97-aade-4fc6-85ea-b7fc5c5035f7','a','a','2343241',69.99,'DECLINED','2024-12-20 08:53:03','2024-12-20 08:53:03'),(8,'fd3ccab1-5fe1-469e-b3dd-e68d3b4fdc0e','a','a','1456565658074',59.99,'ACCEPTED','2024-12-20 11:02:11','2024-12-20 11:02:11'),(9,'fd3ccab1-5fe1-469e-b3dd-e68d3b4fdc0e','a','a','1456565658074',129.98,'ACCEPTED','2024-12-20 11:03:04','2024-12-20 11:03:04');
/*!40000 ALTER TABLE `checkout` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-12-20  7:10:37
