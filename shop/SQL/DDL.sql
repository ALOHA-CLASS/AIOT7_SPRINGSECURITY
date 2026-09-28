SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE `users`;
TRUNCATE TABLE `user_auth`;


CREATE TABLE `product` (
  `no` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `id` varchar(36) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `category` varchar(50) DEFAULT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `image_url` varchar(500) DEFAULT NULL,
  `name` varchar(100) NOT NULL,
  `price` int NOT NULL,
  `stock` int NOT NULL,
  PRIMARY KEY (`no`),
  UNIQUE KEY `UK2gi03oht571gsnsvllxu1q19x` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci



CREATE TABLE `cart_item` (
  `no` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `id` varchar(36) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `quantity` int NOT NULL,
  `product_no` bigint NOT NULL,
  `user_no` bigint NOT NULL,
  PRIMARY KEY (`no`),
  UNIQUE KEY `UKfxoweir15bwjahvditmo13nfj` (`id`),
  KEY `FKs7n4m1ul7lnp5f9x1mr8gm90k` (`product_no`),
  KEY `FKt8sxifqdxbkcgxdyde4dlrh67` (`user_no`),
  CONSTRAINT `FKs7n4m1ul7lnp5f9x1mr8gm90k` FOREIGN KEY (`product_no`) REFERENCES `product` (`no`),
  CONSTRAINT `FKt8sxifqdxbkcgxdyde4dlrh67` FOREIGN KEY (`user_no`) REFERENCES `users` (`no`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci



CREATE TABLE `users` (
  `no` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `id` varchar(36) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `enabled` bit(1) NOT NULL,
  `name` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `phone` varchar(30) DEFAULT NULL,
  `username` varchar(50) NOT NULL,
  PRIMARY KEY (`no`),
  UNIQUE KEY `UKr43af9ap4edm43mmtq01oddj6` (`username`),
  UNIQUE KEY `UK6jvqtxgs6xvh0h0t261hurgqo` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci