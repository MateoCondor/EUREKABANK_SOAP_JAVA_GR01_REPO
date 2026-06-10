CREATE DATABASE  IF NOT EXISTS `eurekabank` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `eurekabank`;
-- MySQL dump 10.13  Distrib 8.0.45, for Win64 (x86_64)
--
-- Host: localhost    Database: eurekabank
-- ------------------------------------------------------
-- Server version	8.0.45

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
-- Table structure for table `accounts`
--

DROP TABLE IF EXISTS `accounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `accounts` (
  `ID` bigint NOT NULL AUTO_INCREMENT,
  `ACCOUNTNUMBER` varchar(20) NOT NULL,
  `BALANCE` decimal(15,2) NOT NULL,
  `STATUS` varchar(10) NOT NULL,
  `TYPE` varchar(10) NOT NULL,
  `client_id` bigint NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ACCOUNTNUMBER` (`ACCOUNTNUMBER`),
  KEY `FK_accounts_client_id` (`client_id`),
  CONSTRAINT `FK_accounts_client_id` FOREIGN KEY (`client_id`) REFERENCES `clients` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `clients`
--

DROP TABLE IF EXISTS `clients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `clients` (
  `ID` bigint NOT NULL AUTO_INCREMENT,
  `DNI` varchar(20) NOT NULL,
  `EMAIL` varchar(150) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  `PHONE` varchar(20) DEFAULT NULL,
  `STATUS` varchar(10) NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `DNI` (`DNI`),
  UNIQUE KEY `user_id` (`user_id`),
  CONSTRAINT `FK_clients_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `parameters`
--

DROP TABLE IF EXISTS `parameters`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `parameters` (
  `ID` bigint NOT NULL AUTO_INCREMENT,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `param_key` varchar(100) NOT NULL,
  `param_value` varchar(500) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `param_key` (`param_key`),
  UNIQUE KEY `UNQ_parameters_0` (`param_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `transactions`
--

DROP TABLE IF EXISTS `transactions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transactions` (
  `ID` bigint NOT NULL AUTO_INCREMENT,
  `AMOUNT` decimal(15,2) NOT NULL,
  `DATE` datetime(6) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `FEE` decimal(15,2) DEFAULT NULL,
  `transfer_type` varchar(10) DEFAULT NULL,
  `TYPE` varchar(10) NOT NULL,
  `source_account_id` bigint NOT NULL,
  `target_account_id` bigint DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_transactions_target_account_id` (`target_account_id`),
  KEY `FK_transactions_source_account_id` (`source_account_id`),
  CONSTRAINT `FK_transactions_source_account_id` FOREIGN KEY (`source_account_id`) REFERENCES `accounts` (`ID`),
  CONSTRAINT `FK_transactions_target_account_id` FOREIGN KEY (`target_account_id`) REFERENCES `accounts` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `ID` bigint NOT NULL AUTO_INCREMENT,
  `PASSWORD` varchar(255) NOT NULL,
  `ROLE` varchar(10) NOT NULL,
  `STATUS` varchar(10) NOT NULL,
  `USERNAME` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `USERNAME` (`USERNAME`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-18 14:45:24
