-- phpMyAdmin SQL Dump
-- version 4.2.7.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Nov 24, 2014 at 03:57 PM
-- Server version: 5.6.20
-- PHP Version: 5.5.15

SET FOREIGN_KEY_CHECKS=0;
SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `VolunteerManagementApp`
--

--
-- Truncate table before insert `acl_class`
--

TRUNCATE TABLE `acl_class`;
--
-- Dumping data for table `acl_class`
--

INSERT INTO `acl_class` (`id`, `class`) VALUES
(7, 'dash.pojo.Group'),
(9, 'dash.pojo.Post'),
(8, 'dash.pojo.Task'),
(4, 'dash.pojo.User');

--
-- Truncate table before insert `acl_entry`
--

TRUNCATE TABLE `acl_entry`;
--
-- Dumping data for table `acl_entry`
--

INSERT INTO `acl_entry` (`id`, `acl_object_identity`, `ace_order`, `sid`, `mask`, `granting`, `audit_success`, `audit_failure`) VALUES
(1, 10, 0, 4, 1, 1, 1, 1),
(2, 10, 1, 4, 2, 1, 0, 0),
(127, 46, 0, 35, 1, 1, 0, 0),
(128, 46, 1, 35, 2, 1, 0, 0),
(129, 46, 2, 35, 8, 1, 0, 0),
(130, 45, 0, 34, 1, 1, 0, 0),
(134, 57, 0, 39, 1, 1, 0, 0),
(135, 57, 1, 39, 2, 1, 0, 0),
(136, 57, 2, 39, 8, 1, 0, 0),
(147, 60, 0, 41, 1, 1, 0, 0),
(148, 60, 1, 41, 2, 1, 0, 0),
(149, 60, 2, 41, 8, 1, 0, 0),
(195, 64, 0, 42, 1, 1, 0, 0),
(196, 64, 1, 42, 2, 1, 0, 0),
(197, 64, 2, 42, 8, 1, 0, 0),
(198, 59, 0, 35, 128, 1, 0, 0),
(199, 59, 1, 41, 64, 1, 0, 0),
(200, 59, 2, 42, 64, 1, 0, 0),
(207, 63, 0, 41, 128, 1, 0, 0),
(208, 63, 1, 42, 64, 1, 0, 0),
(212, 65, 0, 43, 1, 1, 0, 0),
(213, 65, 1, 43, 2, 1, 0, 0),
(214, 65, 2, 43, 8, 1, 0, 0);

--
-- Truncate table before insert `acl_object_identity`
--

TRUNCATE TABLE `acl_object_identity`;
--
-- Dumping data for table `acl_object_identity`
--

INSERT INTO `acl_object_identity` (`id`, `object_id_class`, `object_id_identity`, `parent_object`, `owner_sid`, `entries_inheriting`) VALUES
(10, 4, 1, NULL, 4, 0),
(45, 4, 7, NULL, 4, 1),
(46, 4, 8, NULL, 4, 1),
(57, 4, 12, NULL, 4, 1),
(59, 7, 16, NULL, 4, 1),
(60, 4, 14, NULL, 4, 1),
(63, 8, 5, NULL, 4, 1),
(64, 4, 15, NULL, 4, 1),
(65, 4, 18, NULL, 4, 1);

--
-- Truncate table before insert `acl_sid`
--

TRUNCATE TABLE `acl_sid`;
--
-- Dumping data for table `acl_sid`
--

INSERT INTO `acl_sid` (`id`, `principal`, `sid`) VALUES
(39, 1, 'Admin'),
(43, 1, 'BrokenTest'),
(4, 1, 'Root'),
(41, 1, 'TaskManagerDemo'),
(42, 1, 'taskUser'),
(35, 1, 'User'),
(34, 1, 'Visitor');

--
-- Truncate table before insert `authorities`
--

TRUNCATE TABLE `authorities`;
--
-- Dumping data for table `authorities`
--

INSERT INTO `authorities` (`id`, `username`, `authority`) VALUES
(1, 'Admin', 'ROLE_ADMIN'),
(7, 'BrokenTest', 'ROLE_USER'),
(2, 'Root', 'ROLE_ROOT'),
(3, 'TaskManagerDemo', 'ROLE_USER'),
(4, 'taskUser', 'ROLE_USER'),
(5, 'User', 'ROLE_USER'),
(6, 'Visitor', 'ROLE_VISITOR');

--
-- Truncate table before insert `comment`
--

TRUNCATE TABLE `comment`;
--
-- Truncate table before insert `entered_hours`
--

TRUNCATE TABLE `entered_hours`;
--
-- Truncate table before insert `group_data`
--

TRUNCATE TABLE `group_data`;
--
-- Dumping data for table `group_data`
--

INSERT INTO `group_data` (`id`, `name`, `description`, `creation_timestamp`) VALUES
(16, 'TestGroup', 'This is the description, ''User'' is the group manager', '2014-07-29 16:14:53'),
(17, 'Breaking', NULL, '2014-08-12 15:34:08');

--
-- Truncate table before insert `login`
--

TRUNCATE TABLE `login`;
--
-- Dumping data for table `login`
--

INSERT INTO `login` (`username`, `password`, `enabled`, `id`) VALUES
('Root', 'test', 1, 1),
('Visitor', 'test', 1, 7),
('User', 'test', 1, 8),
('Admin', 'test', 1, 12),
('TaskManagerDemo', 'test', 1, 14),
('taskUser', 'test', 1, 15),
('BrokenTest', 'test', 1, 18);

--
-- Truncate table before insert `message`
--

TRUNCATE TABLE `message`;
--
-- Truncate table before insert `post`
--

TRUNCATE TABLE `post`;
--
-- Dumping data for table `post`
--

INSERT INTO `post` (`id`, `user_id`, `group_id`, `content`, `image`, `creation_timestamp`, `latest_activity_timestamp`, `like_count`, `task_link_id`) VALUES
(2, 15, 16, 'This is test my post', NULL, '2014-08-07 12:53:49', '2014-08-07 12:53:49', 0, NULL);

--
-- Truncate table before insert `tasks`
--

TRUNCATE TABLE `tasks`;
--
-- Dumping data for table `tasks`
--

INSERT INTO `tasks` (`id`, `group_id`, `name`, `description`, `time`, `duration`, `location`, `creation_timestamp`, `finished`, `badge_id`) VALUES
(5, 16, 'TestTask', NULL, NULL, 0, NULL, '2014-07-30 14:13:42', 0, NULL);

--
-- Truncate table before insert `user_data`
--

TRUNCATE TABLE `user_data`;
--
-- Dumping data for table `user_data`
--

INSERT INTO `user_data` (`username`, `id`, `firstName`, `lastName`, `city`, `homePhone`, `cellPhone`, `email`, `picture`, `insertion_date`) VALUES
('Root', 1, '', '', NULL, NULL, NULL, NULL, NULL, '2014-07-09 16:41:51'),
('Visitor', 7, 'Client', 'Device', NULL, NULL, NULL, NULL, NULL, '2014-07-18 12:12:54'),
('User', 8, 'Demo', 'ofUser', NULL, NULL, NULL, NULL, NULL, '2014-07-18 12:14:26'),
('Admin', 12, 'Demo', 'ofAdmin', NULL, NULL, NULL, NULL, NULL, '2014-07-24 10:38:34'),
('TaskManagerDemo', 14, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2014-07-29 16:17:31'),
('taskUser', 15, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2014-08-01 12:23:02'),
('BrokenTest', 18, 'first', 'last', 'houston', '2818888208', '1234567890', 'test@gmail.com', NULL, '2014-10-22 21:26:16');
SET FOREIGN_KEY_CHECKS=1;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
