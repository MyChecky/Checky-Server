/*
SQLyog Ultimate v8.32 
MySQL - 5.7.31-log : Database - checky_test
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`checky_test` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `checky_test`;

/*Table structure for table `admin_menu` */

DROP TABLE IF EXISTS `admin_menu`;

CREATE TABLE `admin_menu` (
  `USER_ID` varchar(36) NOT NULL,
  `MENU_ID` varchar(12) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `admin_menu` */

insert  into `admin_menu`(`USER_ID`,`MENU_ID`) values ('4','1'),('4','0'),('4','2'),('4','3'),('4','4'),('4','5'),('4','6'),('4','7'),('3','7'),('3','6'),('3','5'),('3','4'),('5','5'),('5','6'),('5','3'),('5','4'),('2','0'),('2','1'),('2','2'),('2','3'),('2','4'),('2','5'),('2','6'),('2','7'),('6','0'),('6','2'),('6','5'),('6','6');

/*Table structure for table `administrator` */

DROP TABLE IF EXISTS `administrator`;

CREATE TABLE `administrator` (
  `user_id` varchar(12) NOT NULL,
  `user_name` varchar(20) NOT NULL,
  `user_password` varchar(20) NOT NULL,
  `session_id` varchar(40) DEFAULT NULL,
  `USER_TEL` varchar(11) DEFAULT NULL,
  `USER_EMAIL` varchar(40) DEFAULT NULL,
  `DEPARTMENT` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `administrator` */

insert  into `administrator`(`user_id`,`user_name`,`user_password`,`session_id`,`USER_TEL`,`USER_EMAIL`,`DEPARTMENT`) values ('2','二代','123456','ac572ccd-a6f2-4c01-b6ca-9d350ceb2149','222244','123@gmail.com','task'),('3','三代','sehse','049c6312-bfbf-49e5-90a1-76f06f3e4c66','3333','tom@163.com','money'),('4','root','root@@@','c7483c11-fdff-4e18-b6e2-f9ecb7c5964f','4444','Mary@outlook.com','super'),('5','四代','aerahes','049c6312-bfbf-49e5-90a1-76f06f3e4c62','5555','Rose@gamil.com','task'),('6','5dai','123456','adda3525-c9e5-4f56-838e-7d7e999067db','137****5641','5dai@163.com',NULL);

/*Table structure for table `appeal` */

DROP TABLE IF EXISTS `appeal`;

CREATE TABLE `appeal` (
  `appeal_id` varchar(36) NOT NULL,
  `user_id` varchar(36) NOT NULL,
  `task_id` varchar(36) NOT NULL,
  `check_id` varchar(36) NOT NULL,
  `appeal_content` varchar(255) NOT NULL DEFAULT '暂时没有具体内容',
  `appeal_time` varchar(19) NOT NULL,
  `process_result` varchar(20) DEFAULT 'toProcess',
  `process_time` varchar(19) DEFAULT NULL,
  PRIMARY KEY (`appeal_id`),
  UNIQUE KEY `appeal_id` (`appeal_id`),
  KEY `fk_appeal_to_user` (`user_id`),
  KEY `fk_appeal_to_task` (`task_id`),
  KEY `fk_appeal_to_check` (`check_id`),
  CONSTRAINT `fk_appeal_to_check` FOREIGN KEY (`check_id`) REFERENCES `check` (`check_id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_appeal_to_task` FOREIGN KEY (`task_id`) REFERENCES `task` (`task_id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_appeal_to_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `appeal` */

/*Table structure for table `check` */

DROP TABLE IF EXISTS `check`;

CREATE TABLE `check` (
  `check_id` varchar(36) NOT NULL,
  `user_id` varchar(36) NOT NULL,
  `task_id` varchar(36) NOT NULL,
  `check_time` varchar(19) NOT NULL,
  `check_state` varchar(10) DEFAULT 'unknown',
  `supervise_num` int(11) NOT NULL DEFAULT '0',
  `pass_num` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`check_id`),
  UNIQUE KEY `check_id` (`check_id`),
  KEY `fk_check_to_user` (`user_id`),
  KEY `fk_check_to_task` (`task_id`),
  CONSTRAINT `fk_check_to_task` FOREIGN KEY (`task_id`) REFERENCES `task` (`task_id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_check_to_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `check` */

/*Table structure for table `comment` */

DROP TABLE IF EXISTS `comment`;

CREATE TABLE `comment` (
  `comment_id` varchar(36) NOT NULL,
  `essay_id` varchar(36) NOT NULL,
  `user_id` varchar(36) NOT NULL,
  `comment_content` varchar(255) NOT NULL DEFAULT '暂时没有具体内容',
  `comment_time` varchar(19) NOT NULL,
  `IF_DELETE` int(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`comment_id`),
  UNIQUE KEY `comment_id` (`comment_id`),
  KEY `fk_comment_to_essay` (`essay_id`),
  KEY `fk_comment_to_user` (`user_id`),
  CONSTRAINT `fk_comment_to_essay` FOREIGN KEY (`essay_id`) REFERENCES `essay` (`essay_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_comment_to_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `comment` */

/*Table structure for table `essay` */

DROP TABLE IF EXISTS `essay`;

CREATE TABLE `essay` (
  `essay_id` varchar(36) NOT NULL,
  `user_id` varchar(36) NOT NULL,
  `essay_content` varchar(1024) NOT NULL DEFAULT '暂时没有具体内容',
  `essay_time` varchar(30) NOT NULL,
  `like_num` int(11) NOT NULL DEFAULT '0',
  `version` int(11) NOT NULL DEFAULT '0',
  `longtitude` decimal(10,7) DEFAULT NULL,
  `latitude` decimal(10,7) DEFAULT NULL,
  `comment_num` int(11) NOT NULL DEFAULT '0',
  `if_delete` int(1) NOT NULL DEFAULT '0',
  `topic_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`essay_id`),
  UNIQUE KEY `essay_id` (`essay_id`),
  KEY `fk_essay_to_user` (`user_id`),
  CONSTRAINT `fk_essay_to_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `essay` */

/*Table structure for table `essaylike` */

DROP TABLE IF EXISTS `essaylike`;

CREATE TABLE `essaylike` (
  `user_id` varchar(36) NOT NULL,
  `essay_id` varchar(36) NOT NULL,
  `add_time` varchar(30) NOT NULL,
  PRIMARY KEY (`user_id`,`essay_id`),
  KEY `fk_like_to_essay` (`essay_id`),
  CONSTRAINT `fk_like_to_essay` FOREIGN KEY (`essay_id`) REFERENCES `essay` (`essay_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_like_to_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `essaylike` */

/*Table structure for table `friend_chat` */

DROP TABLE IF EXISTS `friend_chat`;

CREATE TABLE `friend_chat` (
  `SEND_ID` varchar(36) NOT NULL,
  `RECEIVER_ID` varchar(36) NOT NULL,
  `CHAT_TIME` varchar(20) NOT NULL,
  `CHAT_CONTENT` varchar(140) NOT NULL,
  `IF_READ` int(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `friend_chat` */

/*Table structure for table `hobby` */

DROP TABLE IF EXISTS `hobby`;

CREATE TABLE `hobby` (
  `HOBBY_ID` int(5) NOT NULL AUTO_INCREMENT,
  `HOBBY_VALUE` varchar(5) NOT NULL,
  `ADD_TIME` varchar(20) DEFAULT '2020-03-05 00:00:00',
  PRIMARY KEY (`HOBBY_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8;

/*Data for the table `hobby` */

insert  into `hobby`(`HOBBY_ID`,`HOBBY_VALUE`,`ADD_TIME`) values (0,'计算机','2020-03-05 00:00:00'),(1,'篮球','2020-03-05 00:00:00'),(2,'足球','2020-03-05 00:00:00'),(3,'桌球','2020-03-05 00:00:00'),(4,'羽毛球','2020-03-05 00:00:00'),(5,'乒乓球','2020-03-05 00:00:00'),(6,'网球','2020-03-05 00:00:00'),(7,'棒球','2020-03-05 00:00:00'),(8,'五子棋','2020-03-05 00:00:00'),(9,'飞行棋','2020-03-05 00:00:00'),(10,'象棋','2020-03-05 00:00:00'),(11,'围棋','2020-03-05 00:00:00'),(12,'军旗','2020-03-05 00:00:00'),(13,'黑白棋','2020-03-05 00:00:00'),(14,'国际象棋','2020-03-05 00:00:00'),(15,'自走棋','2020-03-05 00:00:00'),(16,'英雄联盟','2020-03-05 00:00:00'),(17,'王者荣耀','2020-03-05 00:00:00'),(18,'刺激战场','2020-03-05 00:00:00'),(19,'和平经营','2020-03-05 00:00:00'),(20,'魔兽争霸','2020-03-05 00:00:00'),(21,'钢琴','2020-03-05 00:00:00'),(22,'电子琴','2020-03-05 00:00:00'),(23,'古筝','2020-03-05 00:00:00'),(24,'读书','2020-03-05 00:00:00'),(25,'书法','2020-03-05 00:00:00'),(26,'画画','2020-03-05 00:00:00'),(27,'漫画','2020-03-05 00:00:00'),(28,'国画','2020-03-05 00:00:00'),(29,'油画','2020-03-05 00:00:00'),(30,'水彩','2020-03-05 00:00:00'),(31,'爵士','2020-03-05 00:00:00'),(32,'街舞','2020-03-05 00:00:00'),(33,'嘻哈','2020-03-05 00:00:00'),(34,'说唱','2020-03-05 00:00:00'),(35,'拉丁','2020-03-05 00:00:00'),(36,'探戈','2020-03-05 00:00:00'),(37,'唢呐','2020-03-05 00:00:00'),(38,'编程','2020-03-05 00:00:00'),(39,'设计','2020-03-05 00:00:00');

/*Table structure for table `medal` */

DROP TABLE IF EXISTS `medal`;

CREATE TABLE `medal` (
  `medal_id` varchar(36) NOT NULL,
  `medal_url` varchar(255) NOT NULL,
  `medal_type` varchar(36) NOT NULL,
  `medal_name` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`medal_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `medal` */

insert  into `medal`(`medal_id`,`medal_url`,`medal_type`,`medal_name`) values ('001','l1.png','等级勋章','白银等级'),('002','l2.png','等级勋章','黄金等级'),('003','l3.png','等级勋章','铂金等级'),('004','l4.png','等级勋章','黑金等级'),('005','l5.png','等级勋章','砖石等级'),('011','concentrate.png','专注勋章','专注之星'),('021','type.png','达人勋章','运动达人'),('022','type.png','达人勋章','游戏达人'),('023','type.png','达人勋章','读书达人'),('024','type.png','达人勋章','学习达人'),('025','type.png','达人勋章','考研达人'),('026','type.png','达人勋章','娱乐达人');

/*Table structure for table `menu` */

DROP TABLE IF EXISTS `menu`;

CREATE TABLE `menu` (
  `MENU_ID` varchar(36) NOT NULL,
  `MENU_NAME` varchar(20) NOT NULL,
  `MENU_URL` varchar(50) DEFAULT NULL,
  `FLAG` int(1) NOT NULL,
  PRIMARY KEY (`MENU_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `menu` */

insert  into `menu`(`MENU_ID`,`MENU_NAME`,`MENU_URL`,`FLAG`) values ('0','admin','menu_url',1),('1','appeal','menu_url',1),('2','essays','menu_url',1),('3','money','menu_url',1),('4','report','menu_url',1),('5','tasks','menu_url',1),('6','users','menu_url',1),('7','parameter','menu_url',1);

/*Table structure for table `moneyflow` */

DROP TABLE IF EXISTS `moneyflow`;

CREATE TABLE `moneyflow` (
  `flow_id` varchar(36) NOT NULL,
  `user_id` varchar(36) NOT NULL,
  `flow_money` double NOT NULL,
  `flow_time` varchar(19) NOT NULL,
  `task_id` varchar(36) NOT NULL,
  `if_test` int(1) NOT NULL DEFAULT '1',
  `flow_io` varchar(1) NOT NULL,
  `flow_type` varchar(8) DEFAULT NULL,
  `remark` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`flow_id`),
  UNIQUE KEY `flow_id` (`flow_id`),
  KEY `fk_flow_from_user` (`user_id`),
  KEY `fk_flow_to_task_idx` (`task_id`),
  CONSTRAINT `FK_moneyflow` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
  CONSTRAINT `fk_flow_from_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `moneyflow` */

/*Table structure for table `parameter` */

DROP TABLE IF EXISTS `parameter`;

CREATE TABLE `parameter` (
  `PARAM_ID` varchar(36) NOT NULL,
  `PARAM_NAME` varchar(20) NOT NULL,
  `PARAM_VALUE` varchar(40) NOT NULL,
  PRIMARY KEY (`PARAM_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `parameter` */

insert  into `parameter`(`PARAM_ID`,`PARAM_NAME`,`PARAM_VALUE`) values ('0','test_money','100'),('1','deposit_with_sup','10'),('10','hobbiesTotal','40'),('11','baseIp','http://127.0.0.1:8080/Checky'),('2','deposit_without_sup','10'),('3','check_lowest_pass','0.5'),('4','task_lowest_pass','0.5'),('5','time_out_day','2'),('6','if_true_money_access','0'),('7','if_new_task_high_set','0'),('8','wxspAppid','wx5f1aa0197013dad6'),('9','wxspSecret','0b82e68c443bcc8ba76b3c9eeb327cf5');

/*Table structure for table `pay` */

DROP TABLE IF EXISTS `pay`;

CREATE TABLE `pay` (
  `PAY_ID` varchar(36) NOT NULL,
  `PAY_ORDERINFO` varchar(36) NOT NULL,
  `PAY_USERID` varchar(36) NOT NULL,
  `PAY_MONEY` double NOT NULL,
  `PAY_TYPE` varchar(10) NOT NULL,
  `PAY_TIME` varchar(20) NOT NULL,
  `PAY_STATE` varchar(10) NOT NULL DEFAULT 'submit',
  PRIMARY KEY (`PAY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `pay` */

/*Table structure for table `record` */

DROP TABLE IF EXISTS `record`;

CREATE TABLE `record` (
  `record_id` varchar(36) NOT NULL,
  `check_id` varchar(36) DEFAULT NULL,
  `record_type` varchar(10) NOT NULL,
  `file_addr` varchar(255) DEFAULT NULL,
  `record_time` varchar(19) NOT NULL,
  `record_content` varchar(1024) DEFAULT NULL,
  `essay_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`record_id`),
  UNIQUE KEY `record_id` (`record_id`),
  KEY `fk_record_to_check` (`check_id`),
  KEY `fk_record_to_essay` (`essay_id`),
  CONSTRAINT `fk_record_to_check` FOREIGN KEY (`check_id`) REFERENCES `check` (`check_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_record_to_essay` FOREIGN KEY (`essay_id`) REFERENCES `essay` (`essay_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `record` */

/*Table structure for table `report` */

DROP TABLE IF EXISTS `report`;

CREATE TABLE `report` (
  `report_id` varchar(36) NOT NULL,
  `user_id` varchar(36) DEFAULT NULL,
  `supervisor_id` varchar(36) DEFAULT NULL,
  `task_id` varchar(36) DEFAULT NULL,
  `check_id` varchar(36) DEFAULT NULL,
  `essay_id` varchar(36) DEFAULT NULL,
  `report_time` varchar(30) NOT NULL,
  `report_content` varchar(255) NOT NULL DEFAULT '暂时没有具体内容',
  `report_type` varchar(1) NOT NULL,
  `process_result` varchar(20) DEFAULT 'toProcess',
  `process_time` varchar(30) DEFAULT NULL,
  `user_reported_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`report_id`),
  UNIQUE KEY `report_id` (`report_id`),
  KEY `fk_report_to_user` (`user_id`),
  KEY `fk_report_to_supervisor` (`supervisor_id`),
  KEY `fk_report_to_task` (`task_id`),
  KEY `fk_report_to_check` (`check_id`),
  KEY `fk_report_to_essay` (`essay_id`),
  CONSTRAINT `fk_report_to_check` FOREIGN KEY (`check_id`) REFERENCES `check` (`check_id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_report_to_essay` FOREIGN KEY (`essay_id`) REFERENCES `essay` (`essay_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_report_to_supervisor` FOREIGN KEY (`supervisor_id`) REFERENCES `user` (`user_id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_report_to_task` FOREIGN KEY (`task_id`) REFERENCES `task` (`task_id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_report_to_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `report` */

/*Table structure for table `service` */

DROP TABLE IF EXISTS `service`;

CREATE TABLE `service` (
  `SERVICE_ID` int(5) NOT NULL AUTO_INCREMENT,
  `SERVICE_CONTENT` varchar(8000) NOT NULL,
  `SERVICE_TIME` varchar(20) NOT NULL,
  PRIMARY KEY (`SERVICE_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Data for the table `service` */

insert  into `service`(`SERVICE_ID`,`SERVICE_CONTENT`,`SERVICE_TIME`) values (1,'您好！欢迎您使用XXXX（名字未定）。\r\n1. 特别提示\r\n1.1 为了更好地为您提供服务，请您仔细阅读这份协议。本协议是您与本应用就您登录本应用平台进行注册及使用等所涉及的全部行为所订立的权利义务规范。您在注册过程中点击“登录”等按钮和使用时，均表明您已完全同意并接受本协议，愿意遵守本协议的各项规则、规范的全部内容，若不同意则可停止注册或使用本应用平台。如您是未成年人，您还应要求您的监护人仔细阅读本协议，并取得他/他们的同意。\r\n1.2 为提高用户的使用感受和满意度，用户同意本应用将基于用户的操作行为对用户数据进行调查研究和分析，从而进一步优化服务。\r\n2. 服务内容\r\n2.1 XXXX（名字未定）是一个打卡监督应用平台，平台内严禁一切非法、涉黄、反动等国家相关部分规定的信息，违反社区运营规范者，一律封号处理。\r\n2.2 本应用服务的具体内容由本应用制作者根据实际情况提供。\r\n2.3 除非本注册及服务协议另有其它明示规定，本应用所推出的新产品、新功能、新服务，均受到本注册及注册协议规范。\r\n2.4 本应用仅提供相关的网络服务，除此之外与相关网络服务有关的设备(如个人电脑、手机、及其他与接入互联网或移动网有关的装置)及所需的费用(如为接入互联网而支付的电话费及上网费、为使用移动网而支付的手机费)均应由用户自行负担。\r\n3. 使用规则\r\n3.1 若您想要使用本应用，需要与微信进行绑定登录，且昵称只能使用汉字、英文字母、数字、下划线及它们的组合，禁止使用空格、各种符号和特殊字符，且最多不超过14个字符(7个汉字)注册，否则本社区有权只截取前14个字符（7个汉字）予以显示用户帐号（若该用户帐号与应用现有用户帐号重名，系统将随机添加一个字符以示区别），否则将不予注册。\r\n3.2 如发现用户帐号中含有不雅文字或不恰当名称的，XXXX（名字未定）保留取消其用户资格的权利。\r\n3.2.1 请勿以党和国家领导人或其他社会名人的真实姓名、字号、艺名、笔名注册为昵称；\r\n3.2.2 请勿以国家机构或其他机构的名称注册为昵称；\r\n3.2.3 请勿注册不文明、不健康名字，或包含歧视、侮辱、猥亵类词语的帐号；\r\n3.2.4 请勿注册易产生歧义、引起他人误解或其它不符合法律规定的帐号。\r\n3.3 用户帐号的所有权归本应用，用户仅享有使用权。\r\n3.4 用户有义务保证帐号的安全，用户利用所绑定的微信帐号所进行的一切活动引起的任何损失或损害，由用户自行承担全部责任，本应用不承担任何责任。如用户发现帐号遭到未授权的使用或发生其他任何安全问题，应立即妥善保管绑定的微信账户，如有必要，请反馈通知本应用管理人员。因黑客行为或用户的保管疏忽导致帐号非法使用，本应用不承担任何责任。\r\n3.5 用户承诺对其发表或者上传于本应用的所有信息(即属于《中华人民共和国著作权法》规定的作品，包括但不限于文字、图片、音乐、电影、表演和录音录像制品和电脑程序等)均享有完整的知识产权，或者已经得到相关权利人的合法授权；如用户违反本条规定造成本应用被第三人索赔的，用户应全额补偿本应用的一切费用(包括但不限于各种赔偿费、诉讼代理费及为此支出的其它合理费用)；\r\n3.6 当第三方认为用户发表或者上传于本应用的信息侵犯其权利，并根据《信息网络传播权保护条例》或者相关法律规定向本应用发送权利通知书时，用户同意本应用可以自行判断决定删除涉嫌侵权信息，除非用户提交书面证据材料排除侵权的可能性，本应用将不会自动恢复上述删除的信息；\r\n(1) 不得为任何非法目的而使用网络服务系统；\r\n(2) 遵守所有与网络服务有关的网络协议、规定和程序；\r\n(3) 不得利用本应用的服务进行任何可能对互联网的正常运转造成不利影响的行为；\r\n(4) 不得利用本应用服务进行任何不利于本应用的行为。\r\n3.7 如用户在使用网络服务时违反上述任何规定，本应用有权要求用户改正或直接采取一切必要的措施(包括但不限于删除用户上传的内容、暂停或终止用户使用网络服务的权利)以减轻用户不当行为而造成的影响。\r\n4. 责任声明\r\n4.1 任何网站、单位或者个人如认为本应用或者本应用提供的相关内容涉嫌侵犯其合法权益，应及时向本应用提供书面权力通知，并提供身份证明、权属证明及详细侵权情况证明。本应用在收到上述法律文件后，将会尽快切断相关内容以保证相关网站、单位或者个人的合法权益得到保障。\r\n4.2 用户明确同意其使用本应用网络服务所存在的风险及一切后果将完全由用户本人承担，本应用对此不承担任何责任。\r\n4.3 本应用无法保证网络服务一定能满足用户的要求，也不保证网络服务的及时性、安全性、准确性。\r\n4.4 本应用不保证为方便用户而设置的外部链接的准确性和完整性，同时，对于该等外部链接指向的不由本应用实际控制的任何网页上的内容，本应用不承担任何责任。\r\n5. 知识产权\r\n5.1 本应用特有的标识、版面设计、编排方式等版权均属本应用享有，未经本应用许可授权，不得任意复制或转载。\r\n5.2 用户从本应用的服务中获得的信息，未经本应用的许可，不得任意复制或转载。\r\n5.3 本应用的所有内容，包括图片、视频、音频等内容所有权归属于XXXX（名字未定）的用户，任何人不得转载。\r\n5.4 本应用所有用户上传内容仅代表用户自己的立场和观点，与本应用无关，由作者本人承担一切法律责任。\r\n5.5 上述及其他任何本服务包含的内容的知识产权均受到法律保护，未经本应用、用户或相关权利人书面许可，任何人不得以任何形式进行使用或创造相关衍生作品。\r\n6. 隐私保护\r\n6.1 本应用不对外公开或向第三方提供单个用户的注册资料及用户在使用网络服务时存储在本社区的非公开内容，但下列情况除外：\r\n(1) 事先获得用户的明确授权；\r\n(2) 根据有关的法律法规要求；\r\n(3) 按照相关政府主管部门的要求；\r\n(4) 为维护社会公众的利益。\r\n6.2 本应用可能会与第三方合作向用户提供相关的网络服务，在此情况下，如该第三方同意承担与本社区同等的保护用户隐私的责任，则本社区有权将用户的注册资料等信息提供给该第三方，并无须另行告知用户。\r\n6.3 在不透露单个用户隐私资料的前提下，本应用有权对整个用户数据库进行分析并对用户数据库进行商业上的利用。\r\n7. 协议修改\r\n7.1 本应用有权随时修改本协议的任何条款，一旦本协议的内容发生变动，本应用将会在本应用上公布修改之后的协议内容，若用户不同意上述修改，则可以选择停止使用本应用，并可申请注销账户。本应用也可选择通过其他适当方式（比如系统通知）向用户通知修改内容。\r\n7.2 如果不同意本应用对本协议相关条款所做的修改，用户有权停止使用本应用，并可申请注销账户。如果用户继续使用本应用，则视为用户接受本应用对本协议相关条款所做的修改。\r\n8. 通知送达\r\n8.1 本协议项下本应用对于用户所有的通知均可通过网页公告、电子邮件、系统通知等主动联系、私信、手机短信或常规的信件传送等方式进行；该等通知于发送之日视为已送达收件人。\r\n8.2 用户对于本应用的通知应当通过本应用对外正式公布的通信地址、电子邮件地址、或应用内建议反馈等联系信息进行送达。\r\n','2020-03-15 00:00:00');

/*Table structure for table `suggestion` */

DROP TABLE IF EXISTS `suggestion`;

CREATE TABLE `suggestion` (
  `suggestion_id` varchar(36) NOT NULL,
  `user_id` varchar(36) NOT NULL,
  `suggestion_content` varchar(500) NOT NULL,
  `suggestion_time` varchar(19) NOT NULL,
  `suggestion_state` varchar(10) DEFAULT 'waiting',
  PRIMARY KEY (`suggestion_id`),
  UNIQUE KEY `suggestion_id` (`suggestion_id`),
  KEY `fk_suggestion_to_user` (`user_id`),
  CONSTRAINT `fk_suggestion_to_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `suggestion` */

/*Table structure for table `supervise` */

DROP TABLE IF EXISTS `supervise`;

CREATE TABLE `supervise` (
  `supervise_id` varchar(36) NOT NULL,
  `check_id` varchar(36) NOT NULL,
  `supervisor_id` varchar(36) NOT NULL,
  `supervise_time` varchar(19) NOT NULL,
  `supervise_content` varchar(255) NOT NULL DEFAULT '暂时没有具体内容',
  `supervise_state` varchar(10) NOT NULL,
  PRIMARY KEY (`supervise_id`),
  UNIQUE KEY `supervise_id` (`supervise_id`),
  KEY `fk_supervise_to_check` (`check_id`),
  KEY `fk_supervise_to_supervisor` (`supervisor_id`),
  CONSTRAINT `fk_supervise_to_check` FOREIGN KEY (`check_id`) REFERENCES `check` (`check_id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_supervise_to_supervisor` FOREIGN KEY (`supervisor_id`) REFERENCES `task_supervisor` (`supervisor_id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `supervise` */

/*Table structure for table `tag` */

DROP TABLE IF EXISTS `tag`;

CREATE TABLE `tag` (
  `tag_id` varchar(36) NOT NULL,
  `tag_content` varchar(36) NOT NULL,
  `tag_count` int(10) NOT NULL,
  `pass_count` int(10) NOT NULL,
  PRIMARY KEY (`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `tag` */

insert  into `tag`(`tag_id`,`tag_content`,`tag_count`,`pass_count`) values ('11','1984',0,0),('12','英语',0,0),('13','帕梅拉',0,0),('14','javaSpring',0,0),('15','射箭',0,0),('16','神雕侠女',0,0),('17','机器学习',0,0),('18','哈哈哈哈',0,0),('19','篮球',0,0),('20','网课',0,0),('21','Web开发',0,0),('22','明朝那些事儿',0,0),('23','篮球',0,0),('24','跑步',0,0),('25','卧推',0,0),('26','大作业',0,0),('27','足球',0,0),('28','日语',0,0),('29','汉姆运动',0,0),('30','天龙八部',0,0),('31','四六级',0,0),('32','羽毛球',0,0),('33','乒乓球',0,0),('34','篮球',0,0);

/*Table structure for table `task` */

DROP TABLE IF EXISTS `task`;

CREATE TABLE `task` (
  `task_id` varchar(36) NOT NULL,
  `user_id` varchar(36) NOT NULL,
  `type_id` varchar(36) NOT NULL,
  `task_title` varchar(50) NOT NULL,
  `task_content` varchar(255) NOT NULL DEFAULT '暂时没有具体内容',
  `task_start_time` varchar(19) NOT NULL,
  `task_end_time` varchar(19) NOT NULL,
  `task_state` varchar(10) NOT NULL DEFAULT 'save',
  `task_money` double NOT NULL DEFAULT '0',
  `supervisor_num` int(11) NOT NULL DEFAULT '0',
  `refund_money` double DEFAULT NULL,
  `check_times` int(11) NOT NULL DEFAULT '0',
  `check_frec` varchar(7) NOT NULL DEFAULT '0000000',
  `MATCH_NUM` int(11) DEFAULT '0',
  `check_num` int(11) NOT NULL,
  `if_test` int(1) NOT NULL,
  `SYSTEM_BENIFIT` double DEFAULT NULL,
  `CHECK_PASS` int(11) DEFAULT NULL,
  `min_pass` double DEFAULT NULL,
  `REAL_PASS` double DEFAULT NULL,
  `MIN_CHECK` double NOT NULL,
  `MIN_CHECK_TYPE` varchar(11) NOT NULL,
  `SUPERVISOR_TYPE` int(1) NOT NULL DEFAULT '0',
  `IF_AREA` int(1) NOT NULL DEFAULT '0',
  `IF_HOBBY` int(1) NOT NULL DEFAULT '0',
  `add_time` varchar(20) DEFAULT NULL,
  `task_Announce_Time` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`task_id`),
  UNIQUE KEY `task_id` (`task_id`),
  KEY `fk_task_to_user` (`user_id`),
  KEY `fk_task_to_type` (`type_id`),
  CONSTRAINT `fk_task_to_type` FOREIGN KEY (`type_id`) REFERENCES `task_type` (`type_id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_task_to_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `task` */

/*Table structure for table `task_supervisor` */

DROP TABLE IF EXISTS `task_supervisor`;

CREATE TABLE `task_supervisor` (
  `task_id` varchar(36) NOT NULL,
  `supervisor_id` varchar(36) NOT NULL,
  `benefit` double NOT NULL DEFAULT '0',
  `add_time` varchar(19) NOT NULL,
  `remove_time` varchar(19) DEFAULT NULL,
  `supervise_num` int(11) NOT NULL DEFAULT '0',
  `REMOVE_REASON` varchar(255) DEFAULT NULL,
  `REPORT_NUM` int(11) DEFAULT '0',
  `BAD_NUM` int(11) DEFAULT '0',
  KEY `fk_task_has_supervisor` (`task_id`),
  KEY `fk_supervisor_has_task` (`supervisor_id`),
  CONSTRAINT `fk_supervisor_has_task` FOREIGN KEY (`supervisor_id`) REFERENCES `user` (`user_id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_task_has_supervisor` FOREIGN KEY (`task_id`) REFERENCES `task` (`task_id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `task_supervisor` */

/*Table structure for table `task_tag` */

DROP TABLE IF EXISTS `task_tag`;

CREATE TABLE `task_tag` (
  `TASK_ID` varchar(36) DEFAULT NULL,
  `TAG_ID` varchar(36) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `task_tag` */

/*Table structure for table `task_type` */

DROP TABLE IF EXISTS `task_type`;

CREATE TABLE `task_type` (
  `type_id` varchar(36) NOT NULL,
  `type_content` varchar(50) NOT NULL,
  `total_num` int(11) NOT NULL DEFAULT '0',
  `pass_num` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`type_id`),
  UNIQUE KEY `type_id` (`type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `task_type` */

insert  into `task_type`(`type_id`,`type_content`,`total_num`,`pass_num`) values ('1747492e-1dd8-4975-82f8-32b204e49891','运动',0,0),('6c3b9c47-6f0b-4061-af3b-f89b153db668','游戏',0,0),('b72fb969-f164-4b4c-9e66-0722dadefb3c','读书',0,0),('c007e6b3-6c7d-487f-b7ff-20181927e140','学习',0,0),('c2378983-8cb3-49cb-a302-960f4d12f7a3','娱乐',0,0),('C8CB4418-BA95-40DC-A34E-613A7393E59D','考研',0,0);

/*Table structure for table `topic` */

DROP TABLE IF EXISTS `topic`;

CREATE TABLE `topic` (
  `topic_content` varchar(50) NOT NULL,
  `topic_id` varchar(36) NOT NULL,
  `topic_count` int(10) NOT NULL,
  `user_id` char(36) DEFAULT NULL,
  `launch_time` char(36) DEFAULT NULL,
  PRIMARY KEY (`topic_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `topic` */

insert  into `topic`(`topic_content`,`topic_id`,`topic_count`,`user_id`,`launch_time`) values ('Checky好用吗','1e7fd82b-521b-460a-9344-3d5661502774',0,'admin','2020-10-16 15:27:20'),('中秋节','49aa1a8d-6ee1-40ad-9146-0e540f872ff4',0,'ow-ZO5Tvnw7846mMNq9m7MnY0hU0','2020-10-16 15:06:09'),('2020保研','cfc87835-f684-4f88-ae25-894b04e2eb0f',0,'ow-ZO5Tvnw7846mMNq9m7MnY0hU0','2020-10-16 15:06:42'),('今天你健身了吗','d8b6ade1-d44e-4e0e-aa9b-6111fd953d74',0,'admin','2020-10-19 00:22:40'),('国庆七天游','fdbfb75a-406f-48c6-861e-730148bee18b',0,'ow-ZO5Tvnw7846mMNq9m7MnY0hU0','2020-10-16 15:06:31');

/*Table structure for table `topic_count` */

DROP TABLE IF EXISTS `topic_count`;

CREATE TABLE `topic_count` (
  `TOPIC_ID` varchar(36) DEFAULT NULL,
  `COUNT_DATE` varchar(20) DEFAULT NULL,
  `COUNT_NUMBER` int(11) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `topic_count` */

/*Table structure for table `type_tag` */

DROP TABLE IF EXISTS `type_tag`;

CREATE TABLE `type_tag` (
  `type_id` varchar(36) NOT NULL,
  `tag_id` varchar(36) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `type_tag` */

insert  into `type_tag`(`type_id`,`tag_id`) values ('1747492e-1dd8-4975-82f8-32b204e49891','11'),('6c3b9c47-6f0b-4061-af3b-f89b153db668','21'),('1747492e-1dd8-4975-82f8-32b204e49891','21'),('1747492e-1dd8-4975-82f8-32b204e49891','12'),('1747492e-1dd8-4975-82f8-32b204e49891','13'),('1747492e-1dd8-4975-82f8-32b204e49891','14'),('1747492e-1dd8-4975-82f8-32b204e49891','15'),('1747492e-1dd8-4975-82f8-32b204e49891','16'),('1747492e-1dd8-4975-82f8-32b204e49891','17'),('1747492e-1dd8-4975-82f8-32b204e49891','18');

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `user_id` varchar(36) NOT NULL,
  `user_name` varchar(20) NOT NULL,
  `user_gender` varchar(10) NOT NULL,
  `user_avatar` varchar(255) NOT NULL,
  `user_time` varchar(19) NOT NULL,
  `user_credit` int(11) NOT NULL DEFAULT '100',
  `user_money` double DEFAULT '0',
  `task_num` int(11) NOT NULL DEFAULT '0',
  `task_num_suc` int(11) NOT NULL DEFAULT '0',
  `supervise_num` int(11) NOT NULL DEFAULT '0',
  `supervise_num_min` int(11) NOT NULL DEFAULT '0',
  `wantpush` int(11) NOT NULL DEFAULT '0',
  `longtitude` decimal(10,7) DEFAULT NULL,
  `latitude` decimal(10,7) DEFAULT NULL,
  `session_id` varchar(40) DEFAULT NULL,
  `TEST_MONEY` double DEFAULT '100',
  `REPORTED_TOTAL` int(11) DEFAULT '0',
  `REPORTED_PASSED` int(11) DEFAULT '0',
  `REPORT_TOTAL` int(11) DEFAULT '0',
  `REPORT_PASSED` int(11) DEFAULT '0',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `user` */

insert  into `user`(`user_id`,`user_name`,`user_gender`,`user_avatar`,`user_time`,`user_credit`,`user_money`,`task_num`,`task_num_suc`,`supervise_num`,`supervise_num_min`,`wantpush`,`longtitude`,`latitude`,`session_id`,`TEST_MONEY`,`REPORTED_TOTAL`,`REPORTED_PASSED`,`REPORT_TOTAL`,`REPORT_PASSED`) values ('oM2yQ4jR0La_jZ8hyxkERsqNTh_8','华1','1','https://wx.qlogo.cn/mmopen/vi_32/cj58hCzRCpHf5YbbrFEcuCwHTNxuUJWAFq15thM9aoCuyeM08YIaGuhgJfaZvL1UBtaXDlocbIfDDvc5NToQXg/132','2019-07-19 12:58:14',100,0,0,0,0,0,0,'0.0000000','0.0000000','a199bc39-2bcb-4923-aac4-cf9bda8baff9',100,0,0,0,0),('oM2yQ4mpOuCfEIeN17S-rdteug28','华2','1','https://wx.qlogo.cn/mmopen/vi_32/DYAIOgq83ep5icmzTicibMZQJ5qYy3BQOiaC00iaHqiaz3h9DmVkQ2XgkroEaN0wibH1nxiaErY4OxaJmVP0A22BE2dCYg/132','2019-07-19 12:53:09',100,0,0,0,0,0,0,'0.0000000','0.0000000','49a52f34-936d-49d7-87a6-ef69cec1a5d4',100,0,0,0,0),('oM2yQ4nodPiKcVYDmc6VRrFUCtxs','华3','1','https://wx.qlogo.cn/mmopen/vi_32/197ftmjibGJlYCNliaN64SOpicbicy6icIupAOibNG7FENc9VMiasPaSI13r81t4Q028NtQTviaYCxsgurX4an6TXDPrww/132','2019-07-18 18:23:46',100,0,0,0,0,0,0,'0.0000000','0.0000000','4243fb7f-adf4-4dd9-beb7-f70621e49cad',100,0,0,0,0),('oM2yQ4okyTyY_WQu2lOsbbjZWN6w','华4','1','https://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTIV9yfrqsXyEV3Xic3jFgxdRS2tFw9iao7OWhO8qJdUeJQHK77YuzL7H1OoX4a0CMLicPbnjxRUrBrGA/132','2019-07-19 13:00:54',100,0,0,0,0,0,0,'114.3425300','30.4998400','28259b59-ae9e-4891-988f-5acbbf40290a',100,0,0,0,0),('oM2yQ4qlD4Qhy251CTO3FZOxqtwc','华5','1','https://wx.qlogo.cn/mmopen/vi_32/wkUVtdP5sbZAlmwDXZhKqocpMyPW3ylP8oZmbEsjefibe2c2Eyb2iclmzX5BclWKmz4yXAe8z8AuXKRAVUbZ6Gvw/132','2019-07-19 15:46:12',100,0,0,0,0,0,0,'0.0000000','0.0000000','c8b76634-fe13-4652-8f14-8d42b4d272d1',100,0,0,0,0),('ow-ZO5c963ph821UvRNkM51Z_89M','本来','0','https://thirdwx.qlogo.cn/mmopen/vi_32/TJMXfwW6JWmXMS3nfVRumqL3vXwiblkkbW3LDmEDAUReRCQvUqiakefeoa4rnMBh9NKAibIMyxrCTLdHpb2wW2oTQ/132','2020-08-13 00:37:19',100,0,0,0,0,0,0,'114.3052500','30.5927600','42a56f5b-3347-4713-bcc0-0e584be57e80',100,0,0,0,0),('ow-ZO5cN3ghvNJf3fX7_6IF6w_JI','林政群','0','https://thirdwx.qlogo.cn/mmhead/Vh9jibqXngf8RLQzCuJX7yeFS1y9uxG1XUDCru8ibmp50/132','2020-09-23 17:11:24',100,0,0,0,0,0,0,'113.3000000','23.1000000','a799b5a6-c133-42b0-97e5-e2020be60fe3',100,0,0,0,0),('ow-ZO5eIGdrjEf0Cf5g11RsvAkOQ','华6','1','https://wx.qlogo.cn/mmopen/vi_32/8Y3EW43KAFgiaDG99CJVeNjqSg8VmnLlJDGwgLVDQ477HJ1EE9u0C13almiaL3FWJ7MVK2qCgwFTw8C7qbJktn6g/132','2019-12-31 16:04:00',100,0,0,0,0,0,0,'0.0000000','0.0000000','20d59686-14e6-4748-bc84-a85c247be204',100,0,0,0,0),('ow-ZO5S3yk22gBmaQI0cJuGsdSfI','华仞','0','https://wx.qlogo.cn/mmopen/vi_32/mg86W2NaRPUPJ4ZJiau8RuMAb6WAkRYoS78cPkGMrsbaUAiaiajOPC3MTAAkZ6sXkMcdlJWlymXibTco8VicsEgvlRA/132','2020-01-14 17:16:08',100,0,0,0,0,0,0,'117.0653300','36.6801300','b6748faf-32e7-454e-8a39-15b83b92faa4',100,0,0,0,0),('ow-ZO5TlM1wFcAqQLbvFN9SWJYtc','hwenliu','1','https://wx.qlogo.cn/mmopen/vi_32/QkW4xtuNT5BXAibnibvgIsticlj9Kr6sicickIS76cbmOeAHezKgkYRqLoaWoMEV98vra7puzAs3dNJ5vWRmCP7P7Ug/132','2019-12-31 15:43:00',100,0,0,0,0,0,0,'114.3159900','30.5538600','094258b0-4ac6-4ecd-956a-3655c0b2de86',100,0,0,0,0),('ow-ZO5Tvnw7846mMNq9m7MnY0hU0','李博文','1','https://wx.qlogo.cn/mmopen/vi_32/7D2T05VPhwPRaf1TfJwnqO0MTTaHwFbRqHZ4RROL1V9VYRIU7F4ZP9UW8pVeOUtreibvuumjgseyINQ3U3PWtnQ/132','2020-08-20 11:06:55',100,0,0,0,0,0,0,'114.3052500','30.5927600','2e2d9d3f-f574-4b6c-88df-cc836c05bd41',100,0,0,0,0),('ow-ZO5VYaELkUFeD_IC0qN02NsCw','华9','1','https://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTLicBeounDPewEIs6r9zeWrcYsbria1wYQtrm0jibmlC2WLSnwGKU5SBYSNbPzs96ak4bkNibiaoicEaJpQ/132','2019-12-31 16:05:36',100,0,0,0,0,0,0,'115.9443200','30.0703700','bd822cbf-3cfb-4978-9be9-8edad4861848',100,0,0,0,0),('System','System','1','https://wx.qlogo.cn/mmopen/vi_32/197ftmjibGJlYCNliaN64SOpicbicy6icIupAOibNG7FENc9VMiasPaSI13r81t4Q028NtQPSpk5ibhwKQbNPr5dxEe6Sg/132','2019-07-18 18:00:00',100,0,0,0,0,0,0,'0.0000000','0.0000000',NULL,100,0,0,0,0);

/*Table structure for table `user_friend` */

DROP TABLE IF EXISTS `user_friend`;

CREATE TABLE `user_friend` (
  `to_user_id` varchar(36) NOT NULL,
  `from_user_id` varchar(36) NOT NULL,
  `add_time` varchar(19) NOT NULL,
  `coo_num` int(11) NOT NULL DEFAULT '0',
  `add_state` int(1) NOT NULL DEFAULT '0',
  `add_content` varchar(25) NOT NULL DEFAULT '一起加个好友吧！',
  KEY `fk_to_user` (`to_user_id`),
  KEY `fk_from_user` (`from_user_id`),
  CONSTRAINT `fk_from_user` FOREIGN KEY (`from_user_id`) REFERENCES `user` (`user_id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_to_user` FOREIGN KEY (`to_user_id`) REFERENCES `user` (`user_id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `user_friend` */

/*Table structure for table `user_hobby` */

DROP TABLE IF EXISTS `user_hobby`;

CREATE TABLE `user_hobby` (
  `USER_ID` varchar(36) NOT NULL,
  `HOBBY_ID` varchar(5) NOT NULL,
  `ADD_TIME` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `user_hobby` */

/*Table structure for table `user_medal` */

DROP TABLE IF EXISTS `user_medal`;

CREATE TABLE `user_medal` (
  `MEDAL_ID` varchar(36) NOT NULL,
  `USER_ID` varchar(36) NOT NULL,
  `TIME` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `user_medal` */

/* Trigger structure for table `administrator` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `delete_administrator` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'localhost' */ /*!50003 TRIGGER `delete_administrator` BEFORE DELETE ON `administrator` FOR EACH ROW BEGIN
	DELETE FROM admin_menu WHERE user_id = OLD.user_id;
END */$$


DELIMITER ;

/* Trigger structure for table `check` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `delete_check` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'localhost' */ /*!50003 TRIGGER `delete_check` BEFORE DELETE ON `check` FOR EACH ROW BEGIN
	DELETE FROM `checky`.`supervise` WHERE `checky`.`supervise`.`check_id` = OLD.`check_id`;
	DELETE FROM `checky`.`record` WHERE `checky`.`record`.`check_id` = OLD.`check_id`;
	DELETE FROM `checky`.`appeal` WHERE `checky`.`appeal`.`check_id` = OLD.`check_id`;
	DELETE FROM `checky`.`report` WHERE `checky`.`report`.`check_id` = OLD.`check_id`;
    END */$$


DELIMITER ;

/* Trigger structure for table `comment` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `comment_AFTER_INSERT` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'localhost' */ /*!50003 TRIGGER `comment_AFTER_INSERT` AFTER INSERT ON `comment` FOR EACH ROW BEGIN
	update essay set comment_num = comment_num + 1 where essay_id = new.essay_id;
END */$$


DELIMITER ;

/* Trigger structure for table `comment` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `comment_BEFORE_DELETE` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'localhost' */ /*!50003 TRIGGER `comment_BEFORE_DELETE` BEFORE DELETE ON `comment` FOR EACH ROW BEGIN
	UPDATE essay SET comment_num = comment_num - 1 WHERE essay_id = old.essay_id;
END */$$


DELIMITER ;

/* Trigger structure for table `essaylike` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `like_AFTER_INSERT` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'localhost' */ /*!50003 TRIGGER `like_AFTER_INSERT` AFTER INSERT ON `essaylike` FOR EACH ROW BEGIN
	UPDATE essay SET like_num = like_num + 1 WHERE essay_id = new.essay_id;
END */$$


DELIMITER ;

/* Trigger structure for table `essaylike` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `like_BEFORE_DELETE` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'localhost' */ /*!50003 TRIGGER `like_BEFORE_DELETE` BEFORE DELETE ON `essaylike` FOR EACH ROW BEGIN
	UPDATE essay SET like_num = like_num - 1 WHERE essay_id = old.essay_id;
END */$$


DELIMITER ;

/* Trigger structure for table `hobby` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `delete_hobby` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'localhost' */ /*!50003 TRIGGER `delete_hobby` BEFORE DELETE ON `hobby` FOR EACH ROW BEGIN
	DELETE FROM `checky`.`user_hobby` WHERE `checky`.`user_hobby`.`hobby_id` = OLD.`hobby_id`;
    END */$$


DELIMITER ;

/* Trigger structure for table `menu` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `delete_menu` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'localhost' */ /*!50003 TRIGGER `delete_menu` BEFORE DELETE ON `menu` FOR EACH ROW BEGIN
	DELETE FROM `checky`.`admin_menu` WHERE `checky`.`admin_menu`.`menu_id` = OLD.`menu_id`;
    END */$$


DELIMITER ;

/* Trigger structure for table `supervise` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `SUPERVISE_AFTER_INSERT` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'localhost' */ /*!50003 TRIGGER `SUPERVISE_AFTER_INSERT` AFTER INSERT ON `supervise` FOR EACH ROW BEGIN
	UPDATE `check` SET SUPERVISE_NUM = SUPERVISE_NUM + 1 WHERE check_id = new.check_id;
	UPDATE `check` SET PASS_NUM = PASS_NUM + 1 WHERE (new.SUPERVISE_STATE = 'pass' AND check_id = new.check_id);
END */$$


DELIMITER ;

/* Trigger structure for table `task` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `delete_task` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'localhost' */ /*!50003 TRIGGER `delete_task` BEFORE DELETE ON `task` FOR EACH ROW BEGIN
	DELETE FROM `checky`.`task_supervisor` WHERE `checky`.`task_supervisor`.`task_id` = OLD.`task_id`;
	DELETE FROM `checky`.`check` WHERE `checky`.`check`.`task_id` = OLD.`task_id`;
	DELETE FROM `checky`.`moneyflow` WHERE `checky`.`moneyflow`.`task_id` = OLD.`task_id`;
    END */$$


DELIMITER ;

/* Trigger structure for table `task_supervisor` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TASK_SUPERVISOR_AFTER_INSERT` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'localhost' */ /*!50003 TRIGGER `TASK_SUPERVISOR_AFTER_INSERT` AFTER INSERT ON `task_supervisor` FOR EACH ROW BEGIN
	UPDATE task SET SUPERVISOR_NUM = SUPERVISOR_NUM + 1 WHERE task_id = new.task_id;
END */$$


DELIMITER ;

/* Trigger structure for table `task_supervisor` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `delete_task_supervisor` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'localhost' */ /*!50003 TRIGGER `delete_task_supervisor` BEFORE DELETE ON `task_supervisor` FOR EACH ROW BEGIN
	DELETE FROM `checky`.`supervise` WHERE `checky`.`supervise`.`supervisor_id` = OLD.`supervisor_id`;
    END */$$


DELIMITER ;

/* Trigger structure for table `user` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `delete_user` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'localhost' */ /*!50003 TRIGGER `delete_user` BEFORE DELETE ON `user` FOR EACH ROW BEGIN
	DELETE FROM `checky`.`pay` WHERE `checky`.`pay`.`pay_userid` = OLD.`user_id`;
	DELETE FROM `checky`.`moneyflow` WHERE `checky`.`moneyflow`.`user_id` = OLD.`user_id`;
	DELETE FROM `checky`.`comment` WHERE `checky`.`comment`.`user_id` = OLD.`user_id`;
	DELETE FROM `checky`.`essaylike` WHERE `checky`.`essaylike`.`user_id` = OLD.`user_id`;
	DELETE FROM `checky`.`essay` WHERE `checky`.`essay`.`user_id` = OLD.`user_id`;
	DELETE FROM `checky`.`user_friend` WHERE `checky`.`user_friend`.`to_user_id` = OLD.`user_id`;
	DELETE FROM `checky`.`user_friend` WHERE `checky`.`user_friend`.`from_user_id` = OLD.`user_id`;
	DELETE FROM `checky`.`friend_chat` WHERE `checky`.`friend_chat`.`send_id` = OLD.`user_id`;
	DELETE FROM `checky`.`friend_chat` WHERE `checky`.`friend_chat`.`receiver_id` = OLD.`user_id`;
	DELETE FROM `checky`.`user_hobby` WHERE `checky`.`user_hobby`.`user_id` = OLD.`user_id`;
	DELETE FROM `checky`.`report` WHERE `checky`.`report`.`user_id` = OLD.`user_id`;
	DELETE FROM `checky`.`appeal` WHERE `checky`.`appeal`.`user_id` = OLD.`user_id`;
	DELETE FROM `checky`.`suggestion` WHERE `checky`.`suggestion`.`user_id` = OLD.`user_id`;
	DELETE FROM `checky`.`task_supervisor` WHERE `checky`.`task_supervisor`.`supervisor_id` = OLD.`user_id`;
	DELETE FROM `checky`.`task` WHERE `checky`.`task`.`user_id` = OLD.`user_id`;
    END */$$


DELIMITER ;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
