CREATE TABLE `products` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `image` varchar(255) NOT NULL,
  `clasification` enum('CuidadoPersonal','Limpieza','Bebidas','Almacen') DEFAULT NULL,
  `description` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
);


CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `role` varchar(45) NOT NULL DEFAULT 'user',
  PRIMARY KEY (`id`),
  UNIQUE KEY `email_UNIQUE` (`email`)
);


CREATE TABLE `branches` (
  `id` int NOT NULL AUTO_INCREMENT,
  `address` varchar(255) NOT NULL,
  `city` varchar(255) NOT NULL,
  `latitude` varchar(45) DEFAULT NULL,
  `longitude` varchar(45) DEFAULT NULL,
  `user_id` int NOT NULL,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_branches_user_id` (`user_id`),
  CONSTRAINT `fk_branches_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
);


CREATE TABLE `offers` (
  `id` int NOT NULL AUTO_INCREMENT,
  `product_id` int NOT NULL,
  `price` float NOT NULL,
  `offer_type` enum('discount','promotion','quantity') NOT NULL,
  `from_date` datetime DEFAULT NULL,
  `to_date` datetime DEFAULT NULL,
  `available` tinyint(1) NOT NULL DEFAULT '1',
  `old_price` float NOT NULL,
  `offer_description` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_offers_product_id` (`product_id`),
  CONSTRAINT `fk_offers_product_id` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
);

CREATE TABLE `branches_x_offers` (
  `branch_id` int NOT NULL,
  `offer_id` int NOT NULL,
  KEY `branches_x_offers_FK` (`branch_id`),
  KEY `branches_x_offers_FK_1` (`offer_id`),
  CONSTRAINT `branches_x_offers_FK` FOREIGN KEY (`branch_id`) REFERENCES `branches` (`id`),
  CONSTRAINT `branches_x_offers_FK_1` FOREIGN KEY (`offer_id`) REFERENCES `offers` (`id`)
);

CREATE TABLE `partners` (
  `id` int NOT NULL AUTO_INCREMENT,
  `partners` enum('PedidosYa','MercadoLibre','Rappi') DEFAULT NULL,
  `link` varchar(255) DEFAULT NULL,
  `offer_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_partners_offer_id` (`offer_id`),
  CONSTRAINT `fk_partners_offer_id` FOREIGN KEY (`offer_id`) REFERENCES `offers` (`id`)
);