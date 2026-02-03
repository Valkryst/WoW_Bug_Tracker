USE `characters`;

ALTER TABLE `bugreport` ADD COLUMN `status` TINYINT NOT NULL DEFAULT 0 AFTER `id`;