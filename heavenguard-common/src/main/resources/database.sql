SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

CREATE TABLE `regions` (
  `name` varchar(64) NOT NULL,
  `parent_name` varchar(64) DEFAULT NULL,
  `world` varchar(16) NOT NULL,
  `min_x` int(11) NOT NULL,
  `min_y` int(11) NOT NULL,
  `min_z` int(11) NOT NULL,
  `max_x` int(11) NOT NULL,
  `max_y` int(11) NOT NULL,
  `max_z` int(11) NOT NULL,
  `flag_pvp` tinyint(1) DEFAULT NULL,
  `flag_public` tinyint(1) DEFAULT NULL,
  `flag_remove_timestamp` timestamp NULL DEFAULT NULL,
  `flag_state` mediumblob
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `regions_members` (
  `region_name` varchar(64) NOT NULL,
  `uuid` char(36) NOT NULL,
  `owner` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `worlds` (
  `name` varchar(32) NOT NULL,
  `flag_pvp` tinyint(1) NOT NULL DEFAULT '0',
  `flag_public` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


ALTER TABLE `regions`
 ADD PRIMARY KEY (`name`), ADD KEY `parent_name` (`parent_name`), ADD KEY `world` (`world`,`min_x`,`min_y`,`min_z`,`max_x`,`max_y`,`max_z`);

ALTER TABLE `regions_members`
 ADD PRIMARY KEY (`region_name`,`uuid`), ADD KEY `region_name` (`region_name`);

ALTER TABLE `worlds`
 ADD PRIMARY KEY (`name`);


ALTER TABLE `regions`
ADD CONSTRAINT `regions_ibfk_3` FOREIGN KEY (`parent_name`) REFERENCES `regions` (`name`) ON DELETE SET NULL ON UPDATE NO ACTION;

ALTER TABLE `regions_members`
ADD CONSTRAINT `regions_members_ibfk_2` FOREIGN KEY (`region_name`) REFERENCES `regions` (`name`) ON DELETE CASCADE ON UPDATE CASCADE;
