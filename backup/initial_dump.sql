-- MySQL dump 10.13  Distrib 8.0.40, for Linux (x86_64)
--
-- Host: localhost    Database: movierentalsystemdb
-- ------------------------------------------------------
-- Server version	8.0.40

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
-- Table structure for table `Account_Discount`
--

DROP TABLE IF EXISTS `Account_Discount`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Account_Discount` (
  `account_id` char(8) COLLATE utf8mb4_unicode_ci NOT NULL,
  `discount_code` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`account_id`,`discount_code`),
  KEY `discount_code` (`discount_code`),
  CONSTRAINT `Account_Discount_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `Accounts` (`account_id`) ON DELETE CASCADE,
  CONSTRAINT `Account_Discount_ibfk_2` FOREIGN KEY (`discount_code`) REFERENCES `Discounts` (`discount_code`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Account_Discount`
--

LOCK TABLES `Account_Discount` WRITE;
/*!40000 ALTER TABLE `Account_Discount` DISABLE KEYS */;
/*!40000 ALTER TABLE `Account_Discount` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Accounts`
--

DROP TABLE IF EXISTS `Accounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Accounts` (
  `account_id` char(8) COLLATE utf8mb4_unicode_ci NOT NULL,
  `username` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `role` enum('ADMIN','CUSTOMER','STAFF','PREMIUM') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'CUSTOMER',
  `email` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` enum('ONLINE','BANNED','OFFLINE') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'OFFLINE',
  `online_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`account_id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Accounts`
--

LOCK TABLES `Accounts` WRITE;
/*!40000 ALTER TABLE `Accounts` DISABLE KEYS */;
INSERT INTO `Accounts` VALUES ('AD000001','admin','1','ADMIN','admin@gmail.com','OFFLINE',NULL,NULL,NULL),('AT000002','kiet','123456','ADMIN','1@gmail.com','OFFLINE','2024-12-03 00:00:00','2024-12-03 00:00:00',NULL);
/*!40000 ALTER TABLE `Accounts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Actors`
--

DROP TABLE IF EXISTS `Actors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Actors` (
  `actor_id` char(8) COLLATE utf8mb4_unicode_ci NOT NULL,
  `actor_name` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `actor_rank` enum('A','B','C','D') COLLATE utf8mb4_unicode_ci DEFAULT 'C',
  `actor_description` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  PRIMARY KEY (`actor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Actors`
--

LOCK TABLES `Actors` WRITE;
/*!40000 ALTER TABLE `Actors` DISABLE KEYS */;
/*!40000 ALTER TABLE `Actors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Discounts`
--

DROP TABLE IF EXISTS `Discounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Discounts` (
  `discount_code` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `customer_id` char(8) COLLATE utf8mb4_unicode_ci NOT NULL,
  `discount_type` enum('PERCENT','FIXED_AMOUNT','BUY_X_GET_Y_FREE') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'PERCENT',
  `discount_value` decimal(10,2) NOT NULL,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `quantity` int DEFAULT '1',
  `is_active` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`discount_code`),
  KEY `customer_id` (`customer_id`),
  CONSTRAINT `Discounts_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `Accounts` (`account_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Discounts`
--

LOCK TABLES `Discounts` WRITE;
/*!40000 ALTER TABLE `Discounts` DISABLE KEYS */;
/*!40000 ALTER TABLE `Discounts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Genres`
--

DROP TABLE IF EXISTS `Genres`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Genres` (
  `genre_name` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`genre_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Genres`
--

LOCK TABLES `Genres` WRITE;
/*!40000 ALTER TABLE `Genres` DISABLE KEYS */;
/*!40000 ALTER TABLE `Genres` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Languages`
--

DROP TABLE IF EXISTS `Languages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Languages` (
  `language_code` char(2) COLLATE utf8mb4_unicode_ci NOT NULL,
  `language_name` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  PRIMARY KEY (`language_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Languages`
--

LOCK TABLES `Languages` WRITE;
/*!40000 ALTER TABLE `Languages` DISABLE KEYS */;
/*!40000 ALTER TABLE `Languages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Movie_Actor`
--

DROP TABLE IF EXISTS `Movie_Actor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Movie_Actor` (
  `movie_id` char(8) COLLATE utf8mb4_unicode_ci NOT NULL,
  `actor_id` char(8) COLLATE utf8mb4_unicode_ci NOT NULL,
  `role` enum('MAIN','VILLAIN','SUPPORT','CAMEO') COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`movie_id`,`actor_id`),
  KEY `actor_id` (`actor_id`),
  CONSTRAINT `Movie_Actor_ibfk_1` FOREIGN KEY (`movie_id`) REFERENCES `Movies` (`movie_id`) ON DELETE CASCADE,
  CONSTRAINT `Movie_Actor_ibfk_2` FOREIGN KEY (`actor_id`) REFERENCES `Actors` (`actor_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Movie_Actor`
--

LOCK TABLES `Movie_Actor` WRITE;
/*!40000 ALTER TABLE `Movie_Actor` DISABLE KEYS */;
/*!40000 ALTER TABLE `Movie_Actor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Movie_Genre`
--

DROP TABLE IF EXISTS `Movie_Genre`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Movie_Genre` (
  `movie_id` char(8) COLLATE utf8mb4_unicode_ci NOT NULL,
  `genre_name` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  PRIMARY KEY (`movie_id`,`genre_name`),
  KEY `genre_name` (`genre_name`),
  CONSTRAINT `Movie_Genre_ibfk_1` FOREIGN KEY (`movie_id`) REFERENCES `Movies` (`movie_id`) ON DELETE CASCADE,
  CONSTRAINT `Movie_Genre_ibfk_2` FOREIGN KEY (`genre_name`) REFERENCES `Genres` (`genre_name`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Movie_Genre`
--

LOCK TABLES `Movie_Genre` WRITE;
/*!40000 ALTER TABLE `Movie_Genre` DISABLE KEYS */;
/*!40000 ALTER TABLE `Movie_Genre` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Movie_Language`
--

DROP TABLE IF EXISTS `Movie_Language`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Movie_Language` (
  `movie_id` char(8) COLLATE utf8mb4_unicode_ci NOT NULL,
  `language_code` char(2) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`movie_id`,`language_code`),
  KEY `language_code` (`language_code`),
  CONSTRAINT `Movie_Language_ibfk_1` FOREIGN KEY (`movie_id`) REFERENCES `Movies` (`movie_id`) ON DELETE CASCADE,
  CONSTRAINT `Movie_Language_ibfk_2` FOREIGN KEY (`language_code`) REFERENCES `Languages` (`language_code`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Movie_Language`
--

LOCK TABLES `Movie_Language` WRITE;
/*!40000 ALTER TABLE `Movie_Language` DISABLE KEYS */;
/*!40000 ALTER TABLE `Movie_Language` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Movies`
--

DROP TABLE IF EXISTS `Movies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Movies` (
  `movie_id` char(8) COLLATE utf8mb4_unicode_ci NOT NULL,
  `title` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  `avg_rating` double(3,1) DEFAULT '0.0',
  `release_year` date DEFAULT NULL,
  `rental_price` decimal(10,2) NOT NULL,
  `available_copies` int DEFAULT '1',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`movie_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Movies`
--

LOCK TABLES `Movies` WRITE;
/*!40000 ALTER TABLE `Movies` DISABLE KEYS */;
/*!40000 ALTER TABLE `Movies` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Payments`
--

DROP TABLE IF EXISTS `Payments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Payments` (
  `rental_id` char(8) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `payment_method` enum('CARD','ONLINE','BANKING') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'CARD',
  `payment_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  KEY `rental_id` (`rental_id`),
  CONSTRAINT `Payments_ibfk_1` FOREIGN KEY (`rental_id`) REFERENCES `Rentals` (`rental_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Payments`
--

LOCK TABLES `Payments` WRITE;
/*!40000 ALTER TABLE `Payments` DISABLE KEYS */;
/*!40000 ALTER TABLE `Payments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Profiles`
--

DROP TABLE IF EXISTS `Profiles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Profiles` (
  `account_id` char(8) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `full_name` varchar(60) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `birthday` date NOT NULL,
  `address` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `phone_number` char(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `credit` decimal(10,2) DEFAULT '0.00',
  KEY `account_id` (`account_id`),
  CONSTRAINT `Profiles_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `Accounts` (`account_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Profiles`
--

LOCK TABLES `Profiles` WRITE;
/*!40000 ALTER TABLE `Profiles` DISABLE KEYS */;
/*!40000 ALTER TABLE `Profiles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Rentals`
--

DROP TABLE IF EXISTS `Rentals`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Rentals` (
  `rental_id` char(8) COLLATE utf8mb4_unicode_ci NOT NULL,
  `movie_id` char(8) COLLATE utf8mb4_unicode_ci NOT NULL,
  `staff_id` char(8) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `customer_id` char(8) COLLATE utf8mb4_unicode_ci NOT NULL,
  `due_date` date NOT NULL,
  `rental_date` date NOT NULL,
  `return_date` date DEFAULT NULL,
  `status` enum('PENDING','APPROVED','DENIED') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'PENDING',
  `total_amount` decimal(10,2) DEFAULT '0.00',
  `late_fee` decimal(10,2) DEFAULT '0.00',
  PRIMARY KEY (`rental_id`),
  KEY `movie_id` (`movie_id`),
  KEY `staff_id` (`staff_id`),
  KEY `customer_id` (`customer_id`),
  CONSTRAINT `Rentals_ibfk_1` FOREIGN KEY (`movie_id`) REFERENCES `Movies` (`movie_id`) ON DELETE CASCADE,
  CONSTRAINT `Rentals_ibfk_2` FOREIGN KEY (`staff_id`) REFERENCES `Accounts` (`account_id`) ON DELETE SET NULL,
  CONSTRAINT `Rentals_ibfk_3` FOREIGN KEY (`customer_id`) REFERENCES `Accounts` (`account_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Rentals`
--

LOCK TABLES `Rentals` WRITE;
/*!40000 ALTER TABLE `Rentals` DISABLE KEYS */;
/*!40000 ALTER TABLE `Rentals` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Reviews`
--

DROP TABLE IF EXISTS `Reviews`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Reviews` (
  `review_id` char(8) COLLATE utf8mb4_unicode_ci NOT NULL,
  `movie_id` char(8) COLLATE utf8mb4_unicode_ci NOT NULL,
  `customer_id` char(8) COLLATE utf8mb4_unicode_ci NOT NULL,
  `review_text` text COLLATE utf8mb4_unicode_ci,
  `rating` int DEFAULT NULL,
  `review_date` date NOT NULL,
  PRIMARY KEY (`review_id`),
  KEY `movie_id` (`movie_id`),
  KEY `customer_id` (`customer_id`),
  CONSTRAINT `Reviews_ibfk_1` FOREIGN KEY (`movie_id`) REFERENCES `Movies` (`movie_id`) ON DELETE CASCADE,
  CONSTRAINT `Reviews_ibfk_2` FOREIGN KEY (`customer_id`) REFERENCES `Accounts` (`account_id`) ON DELETE CASCADE,
  CONSTRAINT `Reviews_chk_1` CHECK ((`rating` between 1 and 5))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Reviews`
--

LOCK TABLES `Reviews` WRITE;
/*!40000 ALTER TABLE `Reviews` DISABLE KEYS */;
/*!40000 ALTER TABLE `Reviews` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Wishlists`
--

DROP TABLE IF EXISTS `Wishlists`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Wishlists` (
  `wishlist_id` char(8) COLLATE utf8mb4_unicode_ci NOT NULL,
  `customer_id` char(8) COLLATE utf8mb4_unicode_ci NOT NULL,
  `movie_id` char(8) COLLATE utf8mb4_unicode_ci NOT NULL,
  `added_date` date NOT NULL,
  `priority` enum('HIGH','MEDIUM','LOW') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'MEDIUM',
  PRIMARY KEY (`wishlist_id`),
  KEY `movie_id` (`movie_id`),
  KEY `customer_id` (`customer_id`),
  CONSTRAINT `Wishlists_ibfk_1` FOREIGN KEY (`movie_id`) REFERENCES `Movies` (`movie_id`) ON DELETE CASCADE,
  CONSTRAINT `Wishlists_ibfk_2` FOREIGN KEY (`customer_id`) REFERENCES `Accounts` (`account_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Wishlists`
--

LOCK TABLES `Wishlists` WRITE;
/*!40000 ALTER TABLE `Wishlists` DISABLE KEYS */;
/*!40000 ALTER TABLE `Wishlists` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-12-02 19:19:57
