-- phpMyAdmin SQL Dump
-- version 2.10.1
-- http://www.phpmyadmin.net
-- 
-- Host: localhost
-- Generation Time: Jul 24, 2008 at 03:20 PM
-- Server version: 5.0.45
-- PHP Version: 5.2.5

CREATE DATABASE `todolist`;

USE todolist;

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

-- 
-- Database: `todo_list`
-- 

-- --------------------------------------------------------

-- 
-- Table structure for table `issues`
-- 

CREATE TABLE `issues` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `site_id` int(11) NOT NULL,
  `label` varchar(45) NOT NULL,
  `description` varchar(255) NOT NULL,
  `type` int(10) unsigned default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=7 ;

-- 
-- Dumping data for table `issues`
-- 

INSERT INTO `issues` (`id`, `site_id`, `label`, `description`, `type`) VALUES 
(1, 39, 'Introduction', '', NULL),
(2, 39, 'news247.gr', '', NULL),
(3, 39, 'Introduction', '<input type="hidden" value="<%redirect_to%>" name="redirect_to"></input>', NULL),
(4, 41, 'Introduction', ' Î£ÏÎ½ÎµÏÎ¯Î¶ÎµÏÎ±Î¹ Î· Î¼Î¬ÏÎ· Î³Î¹Î± Î½Î± ÏÎµÎ¸ÎµÎ¯ ÏÏÏ Î­Î»ÎµÎ³ÏÎ¿ Î· ÏÏÏÎ¹Î¬ ÏÏÎ¿ Î½Î·ÏÎ¯ ÏÎ·Ï Î¡ÏÎ´Î¿Ï. Î ÏÏÎºÎ±Î³Î¹Î¬ ÎµÎºÎ´Î·Î»ÏÎ¸Î·ÎºÎµ ÎºÎ±Î¹ ÏÏÎ¿ Î´Î·Î¼Î¿ÏÎ¹ÎºÏ Î´Î¹Î±Î¼Î­ÏÎ¹ÏÎ¼Î± ÎÎ±Î½Î¹Î¬ÎºÎ¹ ÏÏÎ· ÎÎµÏÏÎ·Î½Î¯Î', NULL),
(5, 40, 'Introduction', ' Î£ÏÎ½ÎµÏÎ¯Î¶ÎµÏÎ±Î¹ Î· Î¼Î¬ÏÎ· Î³Î¹Î± Î½Î± ÏÎµÎ¸ÎµÎ¯ ÏÏÏ Î­Î»ÎµÎ³ÏÎ¿ Î· ÏÏÏÎ¹Î¬ ÏÏÎ¿ Î½Î·ÏÎ¯ ÏÎ·Ï Î¡ÏÎ´Î¿Ï. Î ÏÏÎºÎ±Î³Î¹Î¬ ÎµÎºÎ´Î·Î»ÏÎ¸Î·ÎºÎµ ÎºÎ±Î¹ ÏÏÎ¿ Î´Î·Î¼Î¿ÏÎ¹ÎºÏ Î´Î¹Î±Î¼Î­ÏÎ¹ÏÎ¼Î± ÎÎ±Î½Î¹Î¬ÎºÎ¹ ÏÏÎ· ÎÎµÏÏÎ·Î½Î¯Î', NULL),
(6, 40, 'Introduction 2', ' Î£ÏÎ½ÎµÏÎ¯Î¶ÎµÏÎ±Î¹ Î· Î¼Î¬ÏÎ· Î³Î¹Î± Î½Î± ÏÎµÎ¸ÎµÎ¯ ÏÏÏ Î­Î»ÎµÎ³ÏÎ¿ Î· ÏÏÏÎ¹Î¬ ÏÏÎ¿ Î½Î·ÏÎ¯ ÏÎ·Ï Î¡ÏÎ´Î¿Ï. Î ÏÏÎºÎ±Î³Î¹Î¬ ÎµÎºÎ´Î·Î»ÏÎ¸Î·ÎºÎµ ÎºÎ±Î¹ ÏÏÎ¿ Î´Î·Î¼Î¿ÏÎ¹ÎºÏ Î´Î¹Î±Î¼Î­ÏÎ¹ÏÎ¼Î± ÎÎ±Î½Î¹Î¬ÎºÎ¹ ÏÏÎ· ÎÎµÏÏÎ·Î½Î¯Î', NULL);

-- --------------------------------------------------------

-- 
-- Table structure for table `issue_types`
-- 

CREATE TABLE `issue_types` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `label` varchar(45) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- 
-- Dumping data for table `issue_types`
-- 


-- --------------------------------------------------------

-- 
-- Table structure for table `lists`
-- 

CREATE TABLE `lists` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `label` varchar(255) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- 
-- Dumping data for table `lists`
-- 


-- --------------------------------------------------------

-- 
-- Table structure for table `sites`
-- 

CREATE TABLE `sites` (
  `id` int(11) NOT NULL auto_increment,
  `label` varchar(255) NOT NULL,
  `status` int(11) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=42 ;

-- 
-- Dumping data for table `sites`
-- 

INSERT INTO `sites` (`id`, `label`, `status`) VALUES 
(40, 'news247.gr', 1);

-- --------------------------------------------------------

-- 
-- Table structure for table `status_types`
-- 

CREATE TABLE `status_types` (
  `id` int(11) NOT NULL auto_increment,
  `label` varchar(255) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- 
-- Dumping data for table `status_types`
-- 

