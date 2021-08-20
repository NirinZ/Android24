/*Not everyone supporting `COLLATE utf8mb4_0900_ai_ci`, maybe `COLLATE utf8mb4_unicode_ci` will be better*/

DROP TABLE IF EXISTS `inventory`;
DROP TABLE IF EXISTS `users_transformations`;
DROP TABLE IF EXISTS `users_attacks`;
DROP TABLE IF EXISTS `users_data`;
DROP TABLE IF EXISTS `scouters`;
DROP TABLE IF EXISTS `weapons`;
DROP TABLE IF EXISTS `transformations`;
DROP TABLE IF EXISTS `attacks`;
DROP TABLE IF EXISTS `shop`;
DROP TABLE IF EXISTS `races`;


CREATE TABLE `users_data` (
`UserID` BIGINT(1) UNSIGNED NOT NULL PRIMARY KEY,
`UserName` VARCHAR(100) NOT NULL,
`Race` VARCHAR(100),
`XP` BIGINT(1) UNSIGNED NOT NULL DEFAULT 0,
`Zeni` BIGINT(1) UNSIGNED NOT NULL DEFAULT 0,
`PowerPoints` INT UNSIGNED NOT NULL DEFAULT 0,
`Health` INT UNSIGNED NOT NULL DEFAULT 0,
`Ki` INT UNSIGNED NOT NULL DEFAULT 0,
`StrikeAttack` INT UNSIGNED NOT NULL DEFAULT 0,
`KiAttack` INT UNSIGNED NOT NULL DEFAULT 0,
`Defence` INT UNSIGNED NOT NULL DEFAULT 0,
`Speed` INT UNSIGNED NOT NULL DEFAULT 0)
DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci
COMMENT = 'Where we store most of the data of the users';

CREATE TABLE `inventory` (
`UserID` BIGINT(1) UNSIGNED NOT NULL PRIMARY KEY,
`Zenso` INT UNSIGNED NOT NULL DEFAULT 0,
`Scouter` VARCHAR(100),
`Weapon` VARCHAR(100))
DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci
COMMENT = 'Where we save the inventory of the users';

CREATE TABLE `shop` (
`Name` VARCHAR(100) NOT NULL PRIMARY KEY,
`LinkedTo` VARCHAR(100),
`Depended` VARCHAR(100),
`ForcedRace` VARCHAR(100),
`Cost` BIGINT(1) UNSIGNED NOT NULL DEFAULT 0,
`MinimalLevel` INT UNSIGNED NOT NULL DEFAULT 0)
DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci
COMMENT = 'The table that will describe all the items';

CREATE TABLE `users_transformations` (
`UserID` BIGINT(1) UNSIGNED NOT NULL,
`TransformationName` VARCHAR(100) NOT NULL,
PRIMARY KEY (`UserID`,`TransformationName`))
DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci
COMMENT = 'The table that is connecting between the users and their available transformations';

CREATE TABLE `users_attacks` (
`UserID` BIGINT(1) UNSIGNED NOT NULL,
`AttackName` VARCHAR(100) NOT NULL,
PRIMARY KEY (`UserID`,`AttackName`))
DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci
COMMENT = 'The table that is connecting the users to their attacks';

CREATE TABLE `scouters` (
`ScouterName` VARCHAR(100) NOT NULL PRIMARY KEY,
`PLLimit` BIGINT(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'The power level limit of the scouter')
DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci
COMMENT = 'The tables that describes the scouters';

CREATE TABLE `weapons` (
`WeaponName` VARCHAR(100) NOT NULL PRIMARY KEY)
DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci
COMMENT = 'The treble that describes the weapons';

CREATE TABLE `races` (
`RaceName` VARCHAR(100) NOT NULL PRIMARY KEY)
DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci
COMMENT = 'All the races in the game';

CREATE TABLE `transformations` (
`AttackPowerUp` INT UNSIGNED NOT NULL DEFAULT 1,
`DefencePowerUp` INT UNSIGNED NOT NULL DEFAULT 1,
`SpeedPowerUp` INT UNSIGNED NOT NULL DEFAULT 1,
`KiConsumption` DECIMAL UNSIGNED NOT NULL DEFAULT 1,
`TransformationName` VARCHAR(100) NOT NULL PRIMARY KEY,
`SoloTransformation` BOOLEAN NOT NULL DEFAULT true COMMENT 'Does this transformation have to be the only one used? or that you can add it to other?')
DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

CREATE TABLE `attacks` (
`AttackName` VARCHAR(100) NOT NULL PRIMARY KEY,
`AttackPowerUp` INT UNSIGNED NOT NULL DEFAULT 1,
`DefencePowerUp` INT UNSIGNED NOT NULL DEFAULT 1,
`SpeedPowerUp` INT UNSIGNED NOT NULL DEFAULT 1,
`KiConsumption` INT UNSIGNED NOT NULL DEFAULT 1,
`Counter` BOOLEAN NOT NULL DEFAULT false,
`AttackType` ENUM ('Strike', 'Ki') NOT NULL)
DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci
COMMENT = 'The table that will describe all the attacks';

ALTER TABLE `users_data` ADD CONSTRAINT `users_data_Race_races_RaceName` FOREIGN KEY (`Race`) REFERENCES `races`(`RaceName`) ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE `inventory` ADD CONSTRAINT `inventory_UserID_users_data_UserID` FOREIGN KEY (`UserID`) REFERENCES `users_data`(`UserID`) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE `inventory` ADD CONSTRAINT `inventory_Scouter_scouters_ScouterName` FOREIGN KEY (`Scouter`) REFERENCES `scouters`(`ScouterName`) ON DELETE SET NULL ON UPDATE CASCADE;
ALTER TABLE `inventory` ADD CONSTRAINT `inventory_Weapon_weapons_WeaponName` FOREIGN KEY (`Weapon`) REFERENCES `weapons`(`WeaponName`) ON DELETE SET NULL ON UPDATE CASCADE;
ALTER TABLE `shop` ADD CONSTRAINT `shop_LinkedTo_shop_Name` FOREIGN KEY (`LinkedTo`) REFERENCES `shop`(`Name`) ON DELETE SET NULL ON UPDATE CASCADE;
ALTER TABLE `shop` ADD CONSTRAINT `shop_Depended_shop_Name` FOREIGN KEY (`Depended`) REFERENCES `shop`(`Name`) ON DELETE SET NULL ON UPDATE CASCADE;
ALTER TABLE `shop` ADD CONSTRAINT `shop_ForcedRace_races_RaceName` FOREIGN KEY (`ForcedRace`) REFERENCES `races`(`RaceName`) ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE `users_transformations` ADD CONSTRAINT `users_transformations_UserID_users_data_UserID` FOREIGN KEY (`UserID`) REFERENCES `users_data`(`UserID`) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE `users_transformations` ADD CONSTRAINT `users_transformations-transformations: TransformationName` FOREIGN KEY (`TransformationName`) REFERENCES `transformations`(`TransformationName`) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE `users_attacks` ADD CONSTRAINT `users_attacks_UserID_users_data_UserID` FOREIGN KEY (`UserID`) REFERENCES `users_data`(`UserID`) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `users_attacks` ADD CONSTRAINT `users_attacks_AttackName_attacks_AttackName` FOREIGN KEY (`AttackName`) REFERENCES `attacks`(`AttackName`) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE `scouters` ADD CONSTRAINT `scouters_ScouterName_shop_Name` FOREIGN KEY (`ScouterName`) REFERENCES `shop`(`Name`) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE `weapons` ADD CONSTRAINT `weapons_WeaponName_shop_Name` FOREIGN KEY (`WeaponName`) REFERENCES `shop`(`Name`) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE `transformations` ADD CONSTRAINT `transformations_TransformationName_shop_Name` FOREIGN KEY (`TransformationName`) REFERENCES `shop`(`Name`) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE `attacks` ADD CONSTRAINT `attacks_AttackName_shop_Name` FOREIGN KEY (`AttackName`) REFERENCES `shop`(`Name`) ON DELETE CASCADE ON UPDATE CASCADE;
