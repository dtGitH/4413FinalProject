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

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `product_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `description` text NOT NULL,
  `category` enum('action','adventure','rpg','strategy','shooter') NOT NULL,
  `platform` enum('pc','xbox','playstation','nintendo') NOT NULL,
  `brand` varchar(50) NOT NULL,
  `release_date` date NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `quantity` int NOT NULL,
  `image_url` varchar(255) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (1,'The Witcher 3: Wild Hunt','The Witcher 3: Wild Hunt is an open-world role-playing game set in a visually stunning fantasy universe full of meaningful choices and impactful consequences. In this final installment of the series, you play as Geralt of Rivia, a monster hunter searching for his adopted daughter, Ciri, who is on the run from the mysterious Wild Hunt.','rpg','playstation','CD Projekt Red','2015-05-19',39.99,33,'https://upload.wikimedia.org/wikipedia/en/thumb/0/0c/Witcher_3_cover_art.jpg/220px-Witcher_3_cover_art.jpg','2024-12-19 14:00:25','2024-12-20 12:04:31'),(2,'Elden Ring','Elden Ring is a vast world where open fields with a variety of situations and huge dungeons with complex and three-dimensional designs are seamlessly connected.','rpg','xbox','FromSoftware','2022-02-25',59.99,77,'https://upload.wikimedia.org/wikipedia/en/thumb/b/b9/Elden_Ring_Box_art.jpg/220px-Elden_Ring_Box_art.jpg','2024-12-19 14:00:25','2024-12-20 12:04:32'),(3,'Call of Duty: Modern Warfare II','Call of Duty: Modern Warfare II is the sequel to the 2019 reboot, featuring a new campaign and multiplayer experience.','shooter','playstation','Activision','2022-10-28',69.99,37,'https://upload.wikimedia.org/wikipedia/en/thumb/4/4a/Call_of_Duty_Modern_Warfare_II_Key_Art.jpg/220px-Call_of_Duty_Modern_Warfare_II_Key_Art.jpg','2024-12-19 14:00:25','2024-12-20 11:02:45'),(4,'God of War: Ragnarok','God of War: Ragnarok is an action-adventure game where players join Kratos and Atreus on a mythic journey for answers before Ragnarök arrives.','adventure','playstation','Sony Interactive Entertainment','2022-11-09',69.99,146,'https://upload.wikimedia.org/wikipedia/en/thumb/e/ee/God_of_War_Ragnar%C3%B6k_cover.jpg/220px-God_of_War_Ragnar%C3%B6k_cover.jpg','2024-12-19 14:00:25','2024-12-20 08:41:54'),(5,'Starfield','Starfield is an epic role-playing game set amongst the stars, where you can create any character you want and explore with unparalleled freedom.','rpg','xbox','Bethesda Softworks','2023-09-06',79.99,120,'https://upload.wikimedia.org/wikipedia/en/thumb/6/6d/Bethesda_Starfield.jpg/220px-Bethesda_Starfield.jpg','2024-12-19 14:00:25','2024-12-19 14:00:25'),(6,'Halo Infinite','Halo Infinite is the next chapter of the legendary franchise, where the Master Chief returns to confront the most ruthless foe he\'s ever faced.','shooter','xbox','Xbox Game Studios','2021-12-08',59.99,80,'https://upload.wikimedia.org/wikipedia/en/thumb/1/14/Halo_Infinite.png/220px-Halo_Infinite.png','2024-12-19 14:00:25','2024-12-20 12:04:48'),(7,'Animal Crossing: New Horizons','Animal Crossing: New Horizons allows players to create their personal island paradise on a deserted island brimming with possibility.','adventure','nintendo','Nintendo','2020-03-20',49.99,250,'https://upload.wikimedia.org/wikipedia/en/thumb/1/1f/Animal_Crossing_New_Horizons.jpg/220px-Animal_Crossing_New_Horizons.jpg','2024-12-19 14:00:25','2024-12-19 14:00:25'),(8,'Super Mario Odyssey','Super Mario Odyssey is a 3D platformer where Mario embarks on a globe-trotting adventure to rescue Princess Peach from Bowser.','adventure','nintendo','Nintendo','2017-10-27',59.99,90,'https://upload.wikimedia.org/wikipedia/en/thumb/8/8d/Super_Mario_Odyssey.jpg/220px-Super_Mario_Odyssey.jpg','2024-12-19 14:00:25','2024-12-19 14:00:25'),(9,'Red Dead Redemption 2','Red Dead Redemption 2 is an epic Western action-adventure game set in a vast open world, following outlaw Arthur Morgan.','action','playstation','Rockstar Games','2018-10-26',59.99,70,'https://upload.wikimedia.org/wikipedia/en/thumb/4/44/Red_Dead_Redemption_II.jpg/220px-Red_Dead_Redemption_II.jpg','2024-12-19 14:00:25','2024-12-19 14:00:25'),(10,'Cyberpunk 2077','Cyberpunk 2077 is a futuristic open-world RPG set in Night City, where players assume the role of V, a mercenary outlaw.','rpg','pc','CD Projekt','2020-12-10',49.99,60,'https://upload.wikimedia.org/wikipedia/en/thumb/9/9f/Cyberpunk_2077_box_art.jpg/220px-Cyberpunk_2077_box_art.jpg','2024-12-19 14:00:25','2024-12-19 14:00:25'),(11,'Fire Emblem: Three Houses','Fire Emblem: Three Houses is a tactical role-playing game where players take on the role of a professor at a prestigious military academy.','strategy','nintendo','Intelligent Systems','2019-07-26',59.99,80,'https://upload.wikimedia.org/wikipedia/en/thumb/1/16/Fire_Emblem_Three_Houses.jpg/220px-Fire_Emblem_Three_Houses.jpg','2024-12-19 14:06:44','2024-12-19 14:06:44'),(12,'Far Cry 6','Far Cry 6 is a first-person shooter set on the fictional Caribbean island of Yara, ruled by dictator Antón Castillo.','action','xbox','Ubisoft','2021-10-07',49.99,100,'https://upload.wikimedia.org/wikipedia/en/thumb/3/35/Far_cry_6_cover.jpg/220px-Far_cry_6_cover.jpg','2024-12-19 14:06:44','2024-12-19 14:06:44'),(13,'Horizon Forbidden West','Horizon Forbidden West follows Aloy as she ventures into uncharted lands to stop a mysterious threat.','adventure','playstation','Guerrilla Games','2022-02-18',69.99,90,'https://upload.wikimedia.org/wikipedia/en/thumb/6/69/Horizon_Forbidden_West_cover_art.jpg/220px-Horizon_Forbidden_West_cover_art.jpg','2024-12-19 14:06:44','2024-12-19 14:06:44'),(14,'Overwatch 2','Overwatch 2 is a team-based first-person shooter that introduces new heroes, maps, and a fresh 5v5 gameplay mode.','shooter','pc','Blizzard Entertainment','2022-10-04',59.99,150,'https://upload.wikimedia.org/wikipedia/en/thumb/8/89/Overwatch_2_Steam_artwork.jpg/220px-Overwatch_2_Steam_artwork.jpg','2024-12-19 14:06:44','2024-12-19 14:06:44'),(15,'Splinter Cell: Blacklist','Splinter Cell: Blacklist is a stealth-action game where players control Sam Fisher as he battles global terrorist organizations.','action','xbox','Ubisoft Toronto','2013-08-20',19.99,50,'https://upload.wikimedia.org/wikipedia/en/thumb/4/47/Tom_Clancy%27s_Splinter_Cell_Blacklist_box_art.png/220px-Tom_Clancy%27s_Splinter_Cell_Blacklist_box_art.png','2024-12-19 14:06:44','2024-12-19 14:06:44'),(16,'Metroid Dread','Metroid Dread follows bounty hunter Samus Aran as she investigates a mysterious signal from the planet ZDR.','adventure','nintendo','MercurySteam','2021-10-08',59.99,60,'https://upload.wikimedia.org/wikipedia/en/thumb/f/f7/Metroid_Dread_Banner.png/220px-Metroid_Dread_Banner.png','2024-12-19 14:06:44','2024-12-19 14:06:44'),(17,'Gears 5','Gears 5 is a third-person shooter that follows Kait Diaz as she uncovers the origins of the Locust Horde.','shooter','xbox','The Coalition','2019-09-10',39.99,90,'https://upload.wikimedia.org/wikipedia/en/thumb/e/e4/Gears_5_cover_art.png/220px-Gears_5_cover_art.png','2024-12-19 14:06:44','2024-12-19 14:06:44'),(18,'Divinity: Original Sin 2','Divinity: Original Sin 2 is an award-winning role-playing game featuring a rich story, tactical turn-based combat, and multiplayer co-op.','rpg','pc','Larian Studios','2017-09-14',44.99,100,'https://upload.wikimedia.org/wikipedia/en/thumb/4/48/Divinity_Original_Sin_2_cover_art.jpg/220px-Divinity_Original_Sin_2_cover_art.jpg','2024-12-19 14:06:44','2024-12-19 14:06:44'),(19,'Rainbow Six Siege','Rainbow Six Siege is a tactical first-person shooter focused on close-quarters combat, teamwork, and destruction.','shooter','pc','Ubisoft Montreal','2015-12-01',19.99,200,'https://upload.wikimedia.org/wikipedia/en/thumb/4/47/Tom_Clancy%27s_Rainbow_Six_Siege_cover_art.jpg/220px-Tom_Clancy%27s_Rainbow_Six_Siege_cover_art.jpg','2024-12-19 14:06:44','2024-12-19 14:06:44'),(20,'Age of Empires IV','Age of Empires IV is a real-time strategy game where players command ancient civilizations in epic battles.','strategy','pc','Relic Entertainment','2021-10-28',59.99,70,'https://upload.wikimedia.org/wikipedia/en/thumb/0/08/Age_of_Empires_IV_Cover_Art.png/220px-Age_of_Empires_IV_Cover_Art.png','2024-12-19 14:06:44','2024-12-19 14:06:44'),(21,'Dragon Quest XI','Dragon Quest XI is a turn-based role-playing game where players follow the Luminary on his journey to uncover his destiny.','rpg','playstation','Square Enix','2017-07-29',39.99,120,'https://upload.wikimedia.org/wikipedia/en/thumb/4/4c/Dragon_Quest_XI_cover_art.jpg/220px-Dragon_Quest_XI_cover_art.jpg','2024-12-19 14:06:44','2024-12-19 14:06:44'),(22,'Xenoblade Chronicles 3','Xenoblade Chronicles 3 is an epic RPG set in the world of Aionios, where two nations are locked in eternal conflict.','rpg','nintendo','Monolith Soft','2022-07-29',59.99,90,'https://upload.wikimedia.org/wikipedia/en/thumb/7/76/Xenoblade_3.png/220px-Xenoblade_3.png','2024-12-19 14:06:44','2024-12-19 14:06:44'),(23,'Sekiro: Shadows Die Twice','Sekiro: Shadows Die Twice is an action-adventure game set in Sengoku-era Japan, where players control the \"One-Armed Wolf\" seeking revenge.','adventure','pc','FromSoftware','2019-03-22',49.99,70,'https://upload.wikimedia.org/wikipedia/en/thumb/6/6e/Sekiro_art.jpg/220px-Sekiro_art.jpg','2024-12-19 14:06:44','2024-12-19 14:06:44'),(24,'Control','Control is a third-person action-adventure game where players assume the role of Jesse Faden, the new director of the Federal Bureau of Control.','adventure','playstation','Remedy Entertainment','2019-08-27',29.99,100,'https://upload.wikimedia.org/wikipedia/en/thumb/e/e5/Control_game_cover_art.jpg/220px-Control_game_cover_art.jpg','2024-12-19 14:06:44','2024-12-19 14:06:44'),(25,'Outer Worlds','The Outer Worlds is a first-person RPG set in an alternate future where megacorporations colonize space.','rpg','xbox','Obsidian Entertainment','2019-10-25',39.99,80,'https://upload.wikimedia.org/wikipedia/en/thumb/e/e7/The_Outer_Worlds_cover_art.png/220px-The_Outer_Worlds_cover_art.png','2024-12-19 14:06:44','2024-12-19 14:06:44'),(26,'Mario Kart 8 Deluxe','Mario Kart 8 Deluxe is a fast-paced kart racing game featuring iconic Nintendo characters and wild tracks.','strategy','nintendo','Nintendo','2017-04-28',59.99,200,'https://upload.wikimedia.org/wikipedia/en/b/b5/MarioKart8Boxart.jpg','2024-12-19 14:13:19','2024-12-19 14:13:19'),(27,'The Legend of Zelda: Tears of the Kingdom','Tears of the Kingdom is a breathtaking open-world adventure in the iconic Legend of Zelda series.','adventure','nintendo','Nintendo','2023-05-12',69.99,110,'https://upload.wikimedia.org/wikipedia/en/f/fb/The_Legend_of_Zelda_Tears_of_the_Kingdom_cover.jpg','2024-12-19 14:13:19','2024-12-19 14:13:19'),(28,'Resident Evil Village','Resident Evil Village follows Ethan Winters as he searches for his kidnapped daughter in a mysterious, snow-covered European village.','action','playstation','Capcom','2021-05-07',59.99,65,'https://upload.wikimedia.org/wikipedia/en/2/2c/Resident_Evil_Village.png','2024-12-19 14:13:19','2024-12-19 14:13:19'),(29,'DOOM Eternal','DOOM Eternal is a high-speed, fast-paced first-person shooter where players take on the role of the DOOM Slayer, battling demonic forces.','shooter','pc','id Software','2020-03-20',59.99,75,'https://upload.wikimedia.org/wikipedia/en/9/9d/Cover_Art_of_Doom_Eternal.png','2024-12-19 14:13:19','2024-12-19 14:13:19'),(30,'Assassin\'s Creed Valhalla','In Assassin\'s Creed Valhalla, players control Eivor, a Viking warrior, as they raid and conquer England during the Dark Ages.','action','xbox','Ubisoft','2020-11-10',59.99,90,'https://upload.wikimedia.org/wikipedia/en/f/ff/Assassin%27s_Creed_Valhalla_cover.jpg','2024-12-19 14:13:19','2024-12-19 14:13:19'),(31,'Bloodborne','Bloodborne is an action RPG set in the dark, Gothic city of Yharnam.','adventure','playstation','FromSoftware','2015-03-24',39.99,70,'https://upload.wikimedia.org/wikipedia/en/6/68/Bloodborne_Cover_Wallpaper.jpg','2024-12-19 14:13:19','2024-12-19 14:13:19'),(32,'Hollow Knight','Hollow Knight is a 2D action-adventure game where players explore the underground world of Hallownest.','adventure','pc','Team Cherry','2017-02-24',14.99,150,'https://upload.wikimedia.org/wikipedia/en/0/04/Hollow_Knight_first_cover_art.webp','2024-12-19 14:13:19','2024-12-19 14:13:19'),(33,'Persona 5 Royal','Persona 5 Royal is a turn-based RPG where players assume the role of the Phantom Thieves.','rpg','playstation','Atlus','2019-10-31',49.99,120,'https://upload.wikimedia.org/wikipedia/en/b/b0/Persona_5_cover_art.jpg','2024-12-19 14:13:19','2024-12-19 14:13:19'),(34,'Nioh 2','Nioh 2 is a fast-paced action RPG set in Sengoku-era Japan.','rpg','playstation','Team Ninja','2020-03-12',49.99,90,'https://upload.wikimedia.org/wikipedia/en/9/91/Nioh_2_cover_art.jpg','2024-12-19 14:13:19','2024-12-19 14:13:19'),(35,'The Quarry','The Quarry is an interactive horror game where players make choices that determine the fate of a group of camp counselors.','adventure','playstation','Supermassive Games','2022-06-10',69.99,60,'https://upload.wikimedia.org/wikipedia/en/a/a1/The_Quarry_cover_art.png','2024-12-19 14:13:19','2024-12-19 14:13:19'),(36,'Apex Legends','Apex Legends is a free-to-play battle royale shooter.','shooter','pc','Respawn Entertainment','2019-02-04',0.00,500,'https://upload.wikimedia.org/wikipedia/en/d/db/Apex_legends_cover.jpg','2024-12-19 14:13:19','2024-12-19 14:13:19'),(37,'XCOM 2','XCOM 2 is a turn-based tactical strategy game where players lead the XCOM resistance.','strategy','pc','Firaxis Games','2016-02-05',24.99,100,'https://upload.wikimedia.org/wikipedia/en/c/c3/XCOM_2_cover_art.jpg','2024-12-19 14:13:19','2024-12-19 14:13:19'),(38,'Bayonetta 3','Bayonetta 3 is an action-packed hack-and-slash adventure where players control the powerful witch, Bayonetta, as she faces interdimensional threats.','action','nintendo','PlatinumGames','2022-10-28',59.99,80,'https://upload.wikimedia.org/wikipedia/en/f/fe/Bayonetta_3_cover.webp','2024-12-19 14:13:19','2024-12-19 14:13:19'),(39,'Dying Light 2 Stay Human','Dying Light 2 Stay Human is a first-person action-survival game set in a post-apocalyptic open world.','action','pc','Techland','2022-02-04',59.99,90,'https://upload.wikimedia.org/wikipedia/en/6/6d/Dying_Light_2_cover_art.jpg','2024-12-19 14:13:19','2024-12-19 14:13:19'),(40,'The Evil Within 2','The Evil Within 2 is a survival horror game where players control Sebastian Castellanos as he searches for his daughter in a nightmarish alternate dimension.','adventure','xbox','Tango Gameworks','2017-10-13',29.99,70,'https://upload.wikimedia.org/wikipedia/en/b/bf/The_Evil_Within_2_cover_art.jpg','2024-12-19 14:13:19','2024-12-19 14:13:19');
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_profile`
--

DROP TABLE IF EXISTS `user_profile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_profile` (
  `profile_id` int NOT NULL AUTO_INCREMENT,
  `user_uuid` varchar(36) NOT NULL,
  `first_name` varchar(50) DEFAULT NULL,
  `last_name` varchar(50) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `dob` date DEFAULT NULL,
  `gender` enum('male','female','other') DEFAULT 'other',
  `address` text,
  `credit_card` varchar(16) DEFAULT NULL,
  PRIMARY KEY (`profile_id`),
  KEY `user_uuid` (`user_uuid`),
  CONSTRAINT `user_profile_ibfk_1` FOREIGN KEY (`user_uuid`) REFERENCES `users` (`user_uuid`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_profile`
--

LOCK TABLES `user_profile` WRITE;
/*!40000 ALTER TABLE `user_profile` DISABLE KEYS */;
INSERT INTO `user_profile` VALUES (1,'fd3ccab1-5fe1-469e-b3dd-e68d3b4fdc0e','a','','',NULL,'male','',''),(2,'d5f03c97-aade-4fc6-85ea-b7fc5c5035f7',NULL,NULL,NULL,NULL,'other',NULL,'');
/*!40000 ALTER TABLE `user_profile` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_uuid` varchar(36) NOT NULL,
  `username` varchar(50) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `role` varchar(20) DEFAULT 'customer',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_uuid` (`user_uuid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'fd3ccab1-5fe1-469e-b3dd-e68d3b4fdc0e','a','a@gmail.com','a','2024-12-19 19:00:03','admin'),(2,'d5f03c97-aade-4fc6-85ea-b7fc5c5035f7','t','tonypt@my.yorku.ca','t','2024-12-19 19:40:38','customer');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-12-20  7:19:52
