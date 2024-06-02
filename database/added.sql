USE catering;

DROP TABLE IF EXISTS `Tasks`;

CREATE TABLE `Tasks` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `recipe_id` INT(11) NOT NULL,
  `expiration` date DEFAULT NULL,
  `quantity` INT(11) DEFAULT NULL,
  `duration_min` INT(11) DEFAULT NULL,
  `completed` BOOLEAN DEFAULT FALSE,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `TaskService`;

CREATE TABLE `TaskService` (
  `task_id` int(11) NOT NULL,
  `service_id` int(11) NOT NULL
  -- TODO add primary key
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `TaskTurn`;

CREATE TABLE `TaskTurn` (
  `task_id` int(11) NOT NULL,
  `turn_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;