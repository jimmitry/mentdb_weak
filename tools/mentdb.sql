CREATE TABLE IF NOT EXISTS `cb_botname` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `botname` varchar(120) NOT NULL,
  `dt_last_select` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `is_male` char(1) NOT NULL DEFAULT '0',
  `lang` char(2) NOT NULL DEFAULT 'fr',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `cb_context` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `id_botname` bigint(20) NOT NULL,
  `desc` varchar(512) DEFAULT NULL,
  `dt_last_select` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_bot_context_botname_idx` (`id_botname`),
  CONSTRAINT `fk_context_botname` FOREIGN KEY (`id_botname`) REFERENCES `cb_botname` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `cb_subject` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `id_context` bigint(20) NOT NULL,
  `desc` varchar(512) DEFAULT NULL,
  `dt_last_select` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_bot_subject_context_idx` (`id_context`),
  CONSTRAINT `fk_subject_context` FOREIGN KEY (`id_context`) REFERENCES `cb_context` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `cb_intent` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `id_subject` bigint(20) NOT NULL,
  `pattern` varchar(1024) NOT NULL,
  `dt_last_select` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `topic` varchar(512) DEFAULT NULL,
  `that` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_bot_intent_subject_idx` (`id_subject`),
  CONSTRAINT `fk_intent_subject` FOREIGN KEY (`id_subject`) REFERENCES `cb_subject` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `cb_response` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `id_intent` bigint(20) NOT NULL,
  `response` varchar(2048) NOT NULL,
  `dt_last_select` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_bot_response_intent_idx` (`id_intent`),
  CONSTRAINT `fk_response_intent` FOREIGN KEY (`id_intent`) REFERENCES `cb_intent` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `logs` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dtInsert` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `script` varchar(1024) DEFAULT NULL,
  `parent_pid` varchar(20) DEFAULT NULL,
  `pid` varchar(20) DEFAULT NULL,
  `msg` longtext CHARACTER SET utf8mb4,
  `status` enum('OK', 'KO') NOT NULL,
  `c_key` varchar(45) DEFAULT NULL,
  `c_val` varchar(255) DEFAULT NULL,
  `nodename` varchar(100) NOT NULL,
  `centralization_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_log_dtInsert_index` (`dtInsert`),
  KEY `fk_log_script` (`script`),
  KEY `fk_log_parent_pid_index` (`parent_pid`),
  KEY `fk_log_pid_index` (`pid`),
  KEY `fk_log_status` (`status`),
  KEY `fk_log_c_key` (`c_key`),
  KEY `fk_log_c_val` (`c_val`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `mails` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `state` enum('W','S','E') NOT NULL DEFAULT 'W',
  `dtcreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastattempt` datetime DEFAULT NULL,
  `nbattempt` int(11) NOT NULL DEFAULT '0',
  `subject` varchar(512) CHARACTER SET utf8mb4 NOT NULL,
  `body` longtext CHARACTER SET utf8mb4 NOT NULL,
  `maillist` varchar(2048) NOT NULL,
  `mailcc` varchar(2048) DEFAULT NULL,
  `mailbcc` varchar(2048) DEFAULT NULL,
  `errmsg` longtext CHARACTER SET utf8mb4,
  `cm` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_mail_idx_state` (`state`),
  KEY `fk_mail_idx_dtcreate` (`dtcreate`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `mail_files` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `id_mail` bigint(20) NOT NULL,
  `filename` varchar(255) NOT NULL,
  `b64` longtext NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_mail_files_idx` (`id_mail`),
  CONSTRAINT `fk_mail_files` FOREIGN KEY (`id_mail`) REFERENCES `mails` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `stack_var` (
  `pid` varchar(20) NOT NULL,
  `param` varchar(512) NOT NULL,
  `value` longtext CHARACTER SET utf8mb4 DEFAULT NULL,
  PRIMARY KEY (`pid`, `param`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `stack_wait` (
  `pid` varchar(20) NOT NULL,
  `parent_id` varchar(20) NULL DEFAULT NULL,
  `priority` int(11) NOT NULL,
  `nb_in_thread` int(11) NOT NULL,
  `state` enum('W','E') NOT NULL DEFAULT 'W',
  `script` varchar(1024) NOT NULL,
  `dtcreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastattempt` datetime DEFAULT NULL,
  `nbattempt` int(11) NOT NULL,
  `nbmaxattempt` int(11) NOT NULL,
  `dtexe` datetime NOT NULL,
  `login` varchar(120) NOT NULL,
  `lasterrormsg` longtext CHARACTER SET utf8mb4 DEFAULT NULL,
  `dtclosed` datetime DEFAULT NULL,
  `dterror` datetime DEFAULT NULL,
  `flowname` varchar(1024) DEFAULT NULL,
  `json` JSON DEFAULT NULL,
  `pos` int DEFAULT 0,
  `posname` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`pid`),
  KEY `fk_stack_wait_idx_dtexe` (`dtexe`),
  KEY `fk_stack_wait_idx_flowname` (`flowname`),
  KEY `fk_stack_wait_idx_pos` (`pos`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `stack_closed` (
  `pid` varchar(20) NOT NULL,
  `parent_id` varchar(20) NULL DEFAULT NULL,
  `priority` int(11) NOT NULL,
  `nb_in_thread` int(11) NOT NULL,
  `script` varchar(1024) NOT NULL,
  `dtcreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastattempt` datetime DEFAULT NULL,
  `nbattempt` int(11) NOT NULL,
  `nbmaxattempt` int(11) NOT NULL,
  `dtexe` datetime NOT NULL,
  `login` varchar(120) NOT NULL,
  `lasterrormsg` longtext CHARACTER SET utf8mb4 DEFAULT NULL,
  `dtclosed` datetime DEFAULT NULL,
  `dterror` datetime DEFAULT NULL,
  `nodename` varchar(100) NOT NULL,
  `centralization_date` datetime DEFAULT NULL,
  `flowname` varchar(1024) DEFAULT NULL,
  `json` JSON DEFAULT NULL,
  `pos` int DEFAULT 0,
  `posname` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`nodename`, `pid`),
  KEY `fk_stack_closed_idx_dtexe` (`dtexe`),
  KEY `fk_stack_closed_idx_flowname` (`flowname`),
  KEY `fk_stack_closed_idx_pos` (`pos`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `stack_error` (
  `pid` varchar(20) NOT NULL,
  `parent_id` varchar(20) NULL DEFAULT NULL,
  `priority` int(11) NOT NULL,
  `nb_in_thread` int(11) NOT NULL,
  `script` varchar(1024) NOT NULL,
  `dtcreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastattempt` datetime DEFAULT NULL,
  `nbattempt` int(11) NOT NULL,
  `nbmaxattempt` int(11) NOT NULL,
  `dtexe` datetime NOT NULL,
  `login` varchar(120) NOT NULL,
  `lasterrormsg` longtext CHARACTER SET utf8mb4 DEFAULT NULL,
  `dtclosed` datetime DEFAULT NULL,
  `dterror` datetime DEFAULT NULL,
  `nodename` varchar(100) NOT NULL,
  `centralization_date` datetime DEFAULT NULL,
  `flowname` varchar(1024) DEFAULT NULL,
  `json` JSON DEFAULT NULL,
  `pos` int DEFAULT 0,
  `posname` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`nodename`, `pid`),
  KEY `fk_stack_error_idx_dtexe` (`dtexe`),
  KEY `fk_stack_error_idx_flowname` (`flowname`),
  KEY `fk_stack_error_idx_pos` (`pos`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `history` (
	`dt_insert` datetime NOT NULL ,
	`client_host_name` varchar(512) NULL , 
	`client_host_ip` varchar(255) NULL , 
	`client_port` int NULL , 
	`mql_user` varchar(50) NOT NULL , 
	`mql_script` varchar(600) NOT NULL ,
	`mql` longtext NOT NULL , 
  	KEY `fk_history_idx_dt_insert` (`dt_insert`),
  	KEY `fk_history_idx_mql_user` (`mql_user`),
  	KEY `fk_history_idx_mql_script` (`mql_script`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `users` (
  `login` VARCHAR(150) NOT NULL,
  `password` VARCHAR(32) NOT NULL,
  `firstname` VARCHAR(60) NULL,
  `lastname` VARCHAR(45) NULL,
  `activate` char(1) NOT NULL DEFAULT 'Y', 
  `jsonconf` longtext DEFAULT NULL,
  PRIMARY KEY (`login`));

CREATE TABLE IF NOT EXISTS `groups` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(120) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `user_group` (
  `login` varchar(150) NOT NULL,
  `group_id` bigint(20) NOT NULL,
  PRIMARY KEY (`login`,`group_id`),
  KEY `fk_user_group_groups_idx` (`group_id`),
  CONSTRAINT `fk_user_group_groups` FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_group_users` FOREIGN KEY (`login`) REFERENCES `users` (`login`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `tags` (
  `id` VARCHAR(256) NOT NULL,
  PRIMARY KEY (`id`));

CREATE TABLE IF NOT EXISTS `group_tag` (
  `group_id` bigint(20) NOT NULL,
  `tag` varchar(256) NOT NULL,
  PRIMARY KEY (`group_id`,`tag`),
  KEY `fk_group_tag_tags_idx` (`tag`),
  CONSTRAINT `fk_group_tag_groups` FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_group_tag_tags` FOREIGN KEY (`tag`) REFERENCES `tags` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `apps` (
  `app_id` VARCHAR(45) NOT NULL,
  `title` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`app_id`));
  
CREATE TABLE IF NOT EXISTS `app_user` (
  `app_id` varchar(45) NOT NULL,
  `login` varchar(150) NOT NULL,
  PRIMARY KEY (`app_id`,`login`),
  KEY `fk_app_user_idx` (`login`),
  CONSTRAINT `fk_app_app` FOREIGN KEY (`app_id`) REFERENCES `apps` (`app_id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_app_user` FOREIGN KEY (`login`) REFERENCES `users` (`login`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
  
CREATE TABLE IF NOT EXISTS `record` (
  `key` varchar(600) COLLATE utf8_bin NOT NULL,
  `val` longtext CHARACTER SET utf8mb4 NOT NULL,
  `type` varchar(50) NOT NULL,
  PRIMARY KEY (`key`),
  KEY `fk_record_type_index` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
  
CREATE TABLE IF NOT EXISTS `mona_bot` (
  `bot` varchar(250) NOT NULL,
  `lang` varchar(250) NOT NULL,
  `firstname` varchar(250) NOT NULL,
  `lastname` varchar(250) NOT NULL,
  `is_male` char(1) NOT NULL,
  `cancel_key` varchar(250) NULL DEFAULT NULL,
  `not_found_response` varchar(512) NOT NULL DEFAULT '',
  PRIMARY KEY (`bot`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
  
CREATE TABLE IF NOT EXISTS `mona_user` (
  `bot` varchar(250) NOT NULL,
  `login` varchar(250) NOT NULL,
  `password` varchar(40) NOT NULL,
  `vars` longtext NOT NULL,
  `rights` varchar(2048) NOT NULL,
  `key` varchar(250) NULL DEFAULT NULL,
  PRIMARY KEY (`bot`, `login`),
  CONSTRAINT `fk_mona_user_mona_bot` FOREIGN KEY (`bot`) REFERENCES `mona_bot` (`bot`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
  
CREATE TABLE IF NOT EXISTS `mona_training` (
  `bot` varchar(250) NOT NULL,
  `key` varchar(250) NOT NULL,
  `context` varchar(250) NOT NULL,
  `rights` varchar(2048) NOT NULL,
  `desc` varchar(1024) NOT NULL,
  `in_trigger_array` longtext NOT NULL,
  `out_mql_output` longtext NOT NULL,
  `consciousness_obj` longtext NOT NULL,
  PRIMARY KEY (`bot`, `key`),
  CONSTRAINT `fk_mona_training_mona_bot` FOREIGN KEY (`bot`) REFERENCES `mona_bot` (`bot`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
  
CREATE TABLE IF NOT EXISTS `mona_task` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bot` varchar(250) NOT NULL,
  `login` varchar(250) NOT NULL,
  `key` varchar(250) NOT NULL,
  `vars_copy` longtext NOT NULL,
  `dtcreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_mona_training` FOREIGN KEY (`bot`, `key`) REFERENCES `mona_training` (`bot`, `key`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
  
CREATE TABLE IF NOT EXISTS `mona_not_found` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bot` varchar(250) NOT NULL,
  `login` varchar(250) NOT NULL,
  `input` varchar(2048) NOT NULL,
  `dtcreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_mona_not_found` FOREIGN KEY (`bot`, `login`) REFERENCES `mona_user` (`bot`, `login`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DELETE FROM `mona_bot`;
DELETE FROM `mona_user`;
DELETE FROM `mona_training`;
DELETE FROM `mona_task`;
DELETE FROM `mona_not_found`;
DELETE FROM `stack_closed`;
DELETE FROM `stack_error`;
DELETE FROM `stack_var`;
DELETE FROM `stack_wait`;
DELETE FROM `logs`;
DELETE FROM `mails`;
DELETE FROM `mail_files`;
DELETE FROM `users`;
DELETE FROM `groups`;
DELETE FROM `user_group`;
DELETE FROM `tags`;
DELETE FROM `group_tag`;
DELETE FROM `apps`;
DELETE FROM `app_user`;
DELETE FROM `record`;
DELETE FROM `history`;
ALTER TABLE `mona_task` AUTO_INCREMENT = 1;
ALTER TABLE `logs` AUTO_INCREMENT = 1;
ALTER TABLE `mails` AUTO_INCREMENT = 1;
ALTER TABLE `mail_files` AUTO_INCREMENT = 1;
ALTER TABLE `groups` AUTO_INCREMENT = 1;

INSERT INTO `users`
(`login`,
`password`,
`firstname`,
`lastname`)
VALUES
('admin',
'9003d1df22eb4d3820015070385194c8',
'Jimmitry',
'PAYET');

INSERT INTO `users`
(`login`,
`password`,
`firstname`,
`lastname`)
VALUES
('system',
'9003d1df22eb4d3820015070385194c8',
'Lisa',
'PAYET');

INSERT INTO `groups`
(`id`, `name`)
VALUES
(1, 'Super administrateur');

INSERT INTO `groups`
(`id`, `name`)
VALUES
(2, 'Administrateur');

INSERT INTO `groups`
(`id`, `name`)
VALUES
(3, 'Utilisateur');

INSERT INTO `user_group`
(`login`,
`group_id`)
VALUES
('system',
1);

INSERT INTO `user_group`
(`login`,
`group_id`)
VALUES
('admin',
2);

INSERT INTO `tags` (`id`) VALUES ('*+');
INSERT INTO `tags` (`id`) VALUES ('*-');
INSERT INTO `tags` (`id`) VALUES ('menu_demo_home');
INSERT INTO `tags` (`id`) VALUES ('menu_demo_id');
INSERT INTO `tags` (`id`) VALUES ('menu_demo_users');
INSERT INTO `tags` (`id`) VALUES ('menu_demo_help');
INSERT INTO `tags` (`id`) VALUES ('menu_user_manager_home');
INSERT INTO `tags` (`id`) VALUES ('menu_user_manager_users');
INSERT INTO `group_tag` (`group_id`, `tag`) VALUES (1, '*+');
INSERT INTO `group_tag` (`group_id`, `tag`) VALUES (2, '*-');
INSERT INTO `group_tag` (`group_id`, `tag`) VALUES (3, 'menu_demo_home');
INSERT INTO `group_tag` (`group_id`, `tag`) VALUES (3, 'menu_demo_id');
INSERT INTO `group_tag` (`group_id`, `tag`) VALUES (3, 'menu_demo_users');
INSERT INTO `group_tag` (`group_id`, `tag`) VALUES (3, 'menu_demo_help');
INSERT INTO `group_tag` (`group_id`, `tag`) VALUES (3, 'menu_user_manager_home');
INSERT INTO `group_tag` (`group_id`, `tag`) VALUES (3, 'menu_user_manager_users');
