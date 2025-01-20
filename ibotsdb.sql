-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               8.0.40 - MySQL Community Server - GPL
-- Server OS:                    Win64
-- HeidiSQL Version:             12.8.0.6908
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for ibots
CREATE DATABASE IF NOT EXISTS `ibots` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `ibots`;

-- Dumping structure for table ibots.address
CREATE TABLE IF NOT EXISTS `address` (
  `id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(45) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `line1` text NOT NULL,
  `line2` text NOT NULL,
  `postal_code` varchar(10) NOT NULL,
  `mobile` varchar(10) NOT NULL,
  `user_id` int NOT NULL,
  `city_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_address_city1_idx` (`city_id`),
  KEY `fk_address_user1_idx` (`user_id`),
  CONSTRAINT `fk_address_city1` FOREIGN KEY (`city_id`) REFERENCES `city` (`id`),
  CONSTRAINT `fk_address_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ibots.address: ~0 rows (approximately)
INSERT INTO `address` (`id`, `first_name`, `last_name`, `line1`, `line2`, `postal_code`, `mobile`, `user_id`, `city_id`) VALUES
	(38, 'Ravindu', 'lakmina', 'no', 'gampha', '11108', '0773055098', 14, 1);

-- Dumping structure for table ibots.brand
CREATE TABLE IF NOT EXISTS `brand` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `category_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_brand_category1_idx` (`category_id`),
  CONSTRAINT `fk_brand_category1` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ibots.brand: ~21 rows (approximately)
INSERT INTO `brand` (`id`, `name`, `category_id`) VALUES
	(1, 'Apple', 1),
	(2, 'Hp', 1),
	(4, 'Hp', 4),
	(5, 'Hp', 2),
	(6, 'Hp FHD', 3),
	(7, 'Adata', 5),
	(8, 'MSI', 1),
	(9, 'Acer', 1),
	(10, 'Dell', 1),
	(11, 'Adata', 6),
	(12, 'Adata', 9),
	(13, 'Adata', 7),
	(14, 'MSI', 7),
	(15, 'Aser', 2),
	(16, 'MSI', 4),
	(17, 'Aser', 4),
	(18, 'logitech', 8),
	(19, 'Xbox', 8),
	(20, 'Sony', 8),
	(21, 'DELL', 3),
	(22, 'logitech', 6),
	(23, 'Sony', 6),
	(24, 'Samsung', 5),
	(25, 'WD', 5);

-- Dumping structure for table ibots.cart
CREATE TABLE IF NOT EXISTS `cart` (
  `id` int NOT NULL AUTO_INCREMENT,
  `qty` int NOT NULL,
  `product_id` int NOT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_cart_product1_idx` (`product_id`),
  KEY `fk_cart_user1_idx` (`user_id`),
  CONSTRAINT `fk_cart_product1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `fk_cart_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ibots.cart: ~4 rows (approximately)
INSERT INTO `cart` (`id`, `qty`, `product_id`, `user_id`) VALUES
	(4, 1, 21, 14),
	(5, 1, 31, 14),
	(6, 1, 33, 14);

-- Dumping structure for table ibots.category
CREATE TABLE IF NOT EXISTS `category` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ibots.category: ~8 rows (approximately)
INSERT INTO `category` (`id`, `name`) VALUES
	(1, 'Laptop'),
	(2, 'Desktop'),
	(3, 'Printers'),
	(4, 'Monitors'),
	(5, 'Storage'),
	(6, 'Speakers'),
	(7, 'Computer-case'),
	(8, 'Play-station'),
	(9, 'cable');

-- Dumping structure for table ibots.city
CREATE TABLE IF NOT EXISTS `city` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ibots.city: ~3 rows (approximately)
INSERT INTO `city` (`id`, `name`) VALUES
	(1, 'Colombo'),
	(2, 'Gampaha'),
	(3, 'Kandy');

-- Dumping structure for table ibots.color
CREATE TABLE IF NOT EXISTS `color` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ibots.color: ~1 rows (approximately)
INSERT INTO `color` (`id`, `name`) VALUES
	(1, 'Black'),
	(2, 'White');

-- Dumping structure for table ibots.orders
CREATE TABLE IF NOT EXISTS `orders` (
  `id` int NOT NULL AUTO_INCREMENT,
  `date_time` datetime DEFAULT NULL,
  `user_id` int NOT NULL,
  `address_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_order_user1_idx` (`user_id`),
  KEY `fk_order_address1_idx` (`address_id`),
  CONSTRAINT `fk_order_address1` FOREIGN KEY (`address_id`) REFERENCES `address` (`id`),
  CONSTRAINT `fk_order_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ibots.orders: ~0 rows (approximately)
INSERT INTO `orders` (`id`, `date_time`, `user_id`, `address_id`) VALUES
	(99, '2025-01-14 13:57:57', 14, 38);

-- Dumping structure for table ibots.order_item
CREATE TABLE IF NOT EXISTS `order_item` (
  `id` int NOT NULL AUTO_INCREMENT,
  `product_id` int NOT NULL,
  `qty` int NOT NULL,
  `order_status_id` int NOT NULL,
  `orders_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_order_item_product1_idx` (`product_id`),
  KEY `fk_order_item_order_status1_idx` (`order_status_id`),
  KEY `fk_order_item_orders1_idx` (`orders_id`),
  CONSTRAINT `fk_order_item_order_status1` FOREIGN KEY (`order_status_id`) REFERENCES `order_status` (`id`),
  CONSTRAINT `fk_order_item_orders1` FOREIGN KEY (`orders_id`) REFERENCES `orders` (`id`),
  CONSTRAINT `fk_order_item_product1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ibots.order_item: ~0 rows (approximately)
INSERT INTO `order_item` (`id`, `product_id`, `qty`, `order_status_id`, `orders_id`) VALUES
	(1, 31, 1, 4, 99),
	(2, 25, 1, 4, 99),
	(3, 21, 1, 4, 99);

-- Dumping structure for table ibots.order_status
CREATE TABLE IF NOT EXISTS `order_status` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ibots.order_status: ~4 rows (approximately)
INSERT INTO `order_status` (`id`, `name`) VALUES
	(1, 'Pending'),
	(2, 'Processing'),
	(3, 'Shipped'),
	(4, 'Delivered');

-- Dumping structure for table ibots.product
CREATE TABLE IF NOT EXISTS `product` (
  `id` int NOT NULL AUTO_INCREMENT,
  `tital` varchar(45) NOT NULL,
  `description` varchar(45) NOT NULL,
  `price` double NOT NULL,
  `qty` int NOT NULL,
  `date_time` datetime NOT NULL,
  `user_id` int NOT NULL,
  `brand_id` int NOT NULL,
  `color_id` int NOT NULL,
  `size_id` int NOT NULL,
  `status_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_product_user_idx` (`user_id`),
  KEY `fk_product_color1_idx` (`color_id`),
  KEY `fk_product_size1_idx` (`size_id`),
  KEY `fk_product_status1_idx` (`status_id`),
  KEY `fk_product_brand1_idx` (`brand_id`),
  CONSTRAINT `fk_product_brand1` FOREIGN KEY (`brand_id`) REFERENCES `brand` (`id`),
  CONSTRAINT `fk_product_color1` FOREIGN KEY (`color_id`) REFERENCES `color` (`id`),
  CONSTRAINT `fk_product_size1` FOREIGN KEY (`size_id`) REFERENCES `size` (`id`),
  CONSTRAINT `fk_product_status1` FOREIGN KEY (`status_id`) REFERENCES `status` (`id`),
  CONSTRAINT `fk_product_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ibots.product: ~11 rows (approximately)
INSERT INTO `product` (`id`, `tital`, `description`, `price`, `qty`, `date_time`, `user_id`, `brand_id`, `color_id`, `size_id`, `status_id`) VALUES
	(21, 'Apple MacBook Air', ' MacBook Air 13- m3', 200000, 19, '2025-01-10 19:12:10', 14, 1, 2, 1, 1),
	(22, 'Apple MacBook Air M1', 'Apple M1 chip with 8âcore CPU', 250000, 25, '2025-01-10 19:22:03', 14, 1, 2, 1, 1),
	(23, 'Dell Inspiron 5620 â i5', 'Intel Core i5 â 1235U Processor', 350000, 14, '2025-01-10 19:26:20', 14, 10, 1, 1, 1),
	(24, 'Dell 5525 G15 Gaming', 'AMD Ryzen 5 â 6600H Processor', 370000, 22, '2025-01-10 19:29:23', 14, 10, 1, 1, 1),
	(25, 'Dell Inspiron G15 5511 Gaming', 'Intel Core i7 11th Gen Processor', 245000, 16, '2025-01-10 19:33:38', 14, 10, 1, 1, 1),
	(26, 'MSI Cyborg 15 Gaming A13UCX', 'Intel Core i5â13420H Processor', 340000, 27, '2025-01-10 19:36:12', 14, 8, 1, 1, 1),
	(27, 'MSI Modern MD271UL 27', '3840 x 2160 (4K UHD) Resolution', 84000, 30, '2025-01-10 19:39:28', 14, 16, 1, 1, 1),
	(28, 'MSI G2712 27', '170 Hz Refresh Rate', 70000, 42, '2025-01-10 19:41:14', 14, 16, 1, 1, 1),
	(29, 'HP Series 5', 'FHD (1920 x 1080) Resolution', 55000, 18, '2025-01-10 19:44:40', 14, 4, 1, 1, 1),
	(30, 'Logitech Z625', 'Logitech Z625 Powerful THX Speaker', 55000, 8, '2025-01-10 19:46:55', 14, 22, 1, 1, 1),
	(31, 'HP Smart Tank 210 Printer', 'HP Smart Tank 210 Printer series', 67000, 17, '2025-01-10 22:36:38', 14, 6, 2, 2, 1),
	(32, 'Adata 120GB SSD', 'Hard Disk Interface Solid State', 6500, 50, '2025-01-10 22:40:18', 14, 7, 1, 1, 1),
	(33, 'Dell test', '13i ', 200000, 20, '2025-01-14 14:01:53', 14, 10, 1, 1, 1);

-- Dumping structure for table ibots.size
CREATE TABLE IF NOT EXISTS `size` (
  `id` int NOT NULL AUTO_INCREMENT,
  `size` varchar(10) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ibots.size: ~5 rows (approximately)
INSERT INTO `size` (`id`, `size`) VALUES
	(1, 'no'),
	(2, '50cm'),
	(3, '1m'),
	(4, '1.5m'),
	(5, '2m');

-- Dumping structure for table ibots.status
CREATE TABLE IF NOT EXISTS `status` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ibots.status: ~2 rows (approximately)
INSERT INTO `status` (`id`, `name`) VALUES
	(1, 'Active'),
	(2, 'Inactive');

-- Dumping structure for table ibots.user
CREATE TABLE IF NOT EXISTS `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `fname` varchar(45) NOT NULL,
  `lname` varchar(45) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(45) NOT NULL,
  `verification` varchar(10) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ibots.user: ~0 rows (approximately)
INSERT INTO `user` (`id`, `fname`, `lname`, `email`, `password`, `verification`) VALUES
	(14, 'Ravindu', 'lakmina', 'ravindulakmina008@gmail.com', 'Ravindu@88', 'Verified'),
	(17, 'kasul', 'kalhara', 'ksadakalum40@gmail.com', 'Ravindu@88', 'Verified');

-- Dumping structure for table ibots.watchlist
CREATE TABLE IF NOT EXISTS `watchlist` (
  `id` int NOT NULL AUTO_INCREMENT,
  `product_id` int NOT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_watchlist_product1_idx` (`product_id`),
  KEY `fk_watchlist_user1_idx` (`user_id`),
  CONSTRAINT `fk_watchlist_product1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `fk_watchlist_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ibots.watchlist: ~0 rows (approximately)

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
