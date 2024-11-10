DROP TABLE IF EXISTS `concert`;
DROP TABLE IF EXISTS `concert_session`;
DROP TABLE IF EXISTS `concert_seat`;
DROP TABLE IF EXISTS `concert_reservation`;
DROP TABLE IF EXISTS `payment`;
DROP TABLE IF EXISTS `users`;
DROP TABLE IF EXISTS `user_point`;

CREATE TABLE `concert`
(
    `id`                   bigint NOT NULL AUTO_INCREMENT,
    `created_at`           datetime(6) DEFAULT NULL,
    `updated_at`           datetime(6) DEFAULT NULL,
    `name`                 varchar(255) DEFAULT NULL,
    `place`                varchar(255) DEFAULT NULL,
    `reservation_close_at` datetime(6) DEFAULT NULL,
    `reservation_open_at`  datetime(6) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `concert_session`
(
    `id`               bigint NOT NULL AUTO_INCREMENT,
    `created_at`       datetime(6) DEFAULT NULL,
    `updated_at`       datetime(6) DEFAULT NULL,
    `concert_at`       datetime(6) DEFAULT NULL,
    `concert_id`       bigint DEFAULT NULL,
    `total_seat_count` int    DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `concert_seat`
(
    `id`                 bigint NOT NULL AUTO_INCREMENT,
    `created_at`         datetime(6) DEFAULT NULL,
    `updated_at`         datetime(6) DEFAULT NULL,
    `concert_session_id` bigint DEFAULT NULL,
    `price`              bigint NOT NULL,
    `seat_number`        int    DEFAULT NULL,
    `status`             varchar(255) DEFAULT NULL,
    `version`            bigint DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `concert_reservation`
(
    `id`               bigint NOT NULL AUTO_INCREMENT,
    `created_at`       datetime(6) DEFAULT NULL,
    `updated_at`       datetime(6) DEFAULT NULL,
    `concertId`        bigint       DEFAULT NULL,
    `concertSessionId` bigint       DEFAULT NULL,
    `reservation_at`   datetime(6) DEFAULT NULL,
    `seat_ids`         varchar(255) DEFAULT NULL,
    `status`           varchar(255) DEFAULT NULL,
    `total_price`      bigint       DEFAULT NULL,
    `user_id`          bigint       DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



CREATE TABLE `payment`
(
    `id`             bigint NOT NULL AUTO_INCREMENT,
    `created_at`     datetime(6) DEFAULT NULL,
    `updated_at`     datetime(6) DEFAULT NULL,
    `payment_at`     datetime(6) DEFAULT NULL,
    `reservation_id` bigint DEFAULT NULL,
    `total_price`    bigint DEFAULT NULL,
    `user_id`        bigint DEFAULT NULL,
    `status`         varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



CREATE TABLE `users`
(
    `id`         bigint NOT NULL AUTO_INCREMENT,
    `created_at` datetime(6) DEFAULT NULL,
    `updated_at` datetime(6) DEFAULT NULL,
    `email`      varchar(255) DEFAULT NULL,
    `name`       varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



CREATE TABLE `user_point`
(
    `id`         bigint NOT NULL AUTO_INCREMENT,
    `created_at` datetime(6) DEFAULT NULL,
    `updated_at` datetime(6) DEFAULT NULL,
    `amount`     bigint NOT NULL,
    `user_id`    bigint DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;