CREATE TABLE `articles` (
  `id` int(8) NOT NULL auto_increment,
  `feed_id` int(8) default NULL,
  `title` varchar(300) NOT NULL,
  `real_title` varchar(300) default NULL,
  `sub_title` varchar(300) default NULL,
  `lead_in` text,
  `content` text NOT NULL,
  `url` varchar(250) default NULL,
  `uri` varchar(250) default NULL,
  `published` datetime NOT NULL,
  `updated` datetime default NULL,
  `category_id` int(8) default NULL,
  `signature` varchar(100) default NULL,
  `user_id` int(8) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=57271 ;

-- --------------------------------------------------------

--
-- Table structure for table `ARTICLE_TO_TAG`
--

CREATE TABLE `article_to_tag` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `article_id` int(10) unsigned NOT NULL,
  `tag_id` int(10) unsigned NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3096 ;

-- --------------------------------------------------------

--
-- Table structure for table `CATEGORIES`
--

CREATE TABLE `categories` (
  `id` int(8) NOT NULL auto_increment,
  `parent_id` int(8) default NULL,
  `priority` int(8) default NULL,
  `name` varchar(50) default NULL,
  `display_name` varchar(100) default NULL,
  `enabled` int(1) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=69 ;

-- --------------------------------------------------------

--
-- Table structure for table `COMMENTS`
--

CREATE TABLE `comments` (
  `id` int(8) NOT NULL auto_increment,
  `url` varchar(150) NOT NULL,
  `title` varchar(150) default NULL,
  `content` varchar(20000) default NULL,
  `article_id` int(8) default NULL,
  `uri` varchar(150) NOT NULL,
  `published` datetime NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `COMMITTEES`
--

CREATE TABLE `committees` (
  `id` int(8) NOT NULL auto_increment,
  `name` varchar(200) default NULL,
  `type` tinyint(3) unsigned default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=30 ;

-- --------------------------------------------------------

--
-- Table structure for table `CONSTITUENCIES`
--

CREATE TABLE `constituencies` (
  `id` int(8) NOT NULL auto_increment,
  `name` varchar(150) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=59 ;

-- --------------------------------------------------------

--
-- Table structure for table `EMAIL_EVENT`
--

CREATE TABLE `email_event` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `sent_to` varchar(100) NOT NULL,
  `sent_from` varchar(100) NOT NULL,
  `sent_when` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `ip` varchar(20) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=691 ;

-- --------------------------------------------------------

--
-- Table structure for table `FEEDS`
--

CREATE TABLE `feeds` (
  `id` int(8) NOT NULL auto_increment,
  `url` varchar(250) default NULL,
  `title` varchar(250) default NULL,
  `description` varchar(255) default NULL,
  `category_id` int(8) default NULL,
  `reload_time` int(8) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=36 ;

-- --------------------------------------------------------

--
-- Table structure for table `FEEDS_DEV`
--

CREATE TABLE `feeds_dev` (
  `id` int(8) NOT NULL auto_increment,
  `url` varchar(250) default NULL,
  `title` varchar(250) default NULL,
  `description` varchar(255) default NULL,
  `category_id` int(8) default NULL,
  `reload_time` int(8) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `IMAGES`
--

CREATE TABLE `images` (
  `id` int(8) NOT NULL auto_increment,
  `article_id` int(8) default NULL,
  `priority` int(8) default NULL,
  `name` varchar(100) default NULL,
  `location` varchar(60) default NULL,
  `width` int(8) default NULL,
  `height` int(8) default NULL,
  `caption` varchar(400) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=62734 ;

-- --------------------------------------------------------

--
-- Table structure for table `images_backup`
--

CREATE TABLE `images_backup` (
  `id` int(8) NOT NULL default '0',
  `article_id` int(8) default NULL,
  `priority` int(8) default NULL,
  `name` varchar(100) default NULL,
  `location` varchar(60) default NULL,
  `width` int(8) default NULL,
  `height` int(8) default NULL,
  `caption` varchar(400) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `MOVIES`
--

CREATE TABLE `movies` (
  `id` int(8) NOT NULL auto_increment,
  `title_gr` varchar(150) default NULL,
  `title_en` varchar(150) default NULL,
  `type` varchar(150) default NULL,
  `country` varchar(100) default NULL,
  `language` varchar(100) default NULL,
  `duration` varchar(50) default NULL,
  `director` varchar(150) default NULL,
  `actors` varchar(500) default NULL,
  `trailer_url` varchar(300) default NULL,
  `distribution` varchar(100) default NULL,
  `url` varchar(300) default NULL,
  `description` varchar(10000) default NULL,
  `rating` int(8) default NULL,
  `premiere` varchar(100) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=416 ;

-- --------------------------------------------------------

--
-- Table structure for table `MOVIES_IMAGES`
--

CREATE TABLE `movies_images` (
  `id` int(8) NOT NULL auto_increment,
  `movie_id` int(8) NOT NULL,
  `image_id` int(8) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=419 ;

-- --------------------------------------------------------

--
-- Table structure for table `NEWSPAPERS`
--

CREATE TABLE `newspapers` (
  `id` int(8) NOT NULL auto_increment,
  `title` varchar(200) NOT NULL,
  `type_id` int(8) default NULL,
  `priority` int(8) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=63 ;

-- --------------------------------------------------------

--
-- Table structure for table `NEWSTYPE`
--

CREATE TABLE `newstype` (
  `id` int(8) NOT NULL auto_increment,
  `name` varchar(150) NOT NULL,
  `priority` int(8) default NULL,
  `display_name` varchar(150) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=7 ;

-- --------------------------------------------------------

--
-- Table structure for table `PAPER_IMAGE`
--

CREATE TABLE `paper_image` (
  `id` int(8) NOT NULL auto_increment,
  `paper_id` int(8) NOT NULL,
  `image_id` int(8) NOT NULL,
  `published` datetime NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=13009 ;

-- --------------------------------------------------------

--
-- Table structure for table `PARTIES`
--

CREATE TABLE `parties` (
  `id` int(8) NOT NULL auto_increment,
  `name` varchar(100) default NULL,
  `lookup` varchar(10) default NULL,
  `priority` int(8) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=7 ;

-- --------------------------------------------------------

--
-- Table structure for table `POLITICS`
--

CREATE TABLE `politics` (
  `id` int(8) NOT NULL auto_increment,
  `last_name` varchar(100) default NULL,
  `email` varchar(100) default NULL,
  `url` varchar(150) default NULL,
  `office_address` varchar(150) default NULL,
  `office_phone` varchar(250) default NULL,
  `office_fax` varchar(150) default NULL,
  `home_address` varchar(150) default NULL,
  `home_phone` varchar(50) default NULL,
  `home_fax` varchar(50) default NULL,
  `cv_short` varchar(3000) default NULL,
  `parliament_act` varchar(5000) default NULL,
  `political_act` varchar(5000) default NULL,
  `constituency_id` int(8) default NULL,
  `party_id` int(8) default NULL,
  `first_name` varchar(100) default NULL,
  `quotes` varchar(1000) default NULL,
  `birth_date` datetime default NULL,
  `birth_place` varchar(150) default NULL,
  `title` varchar(400) default NULL,
  `display` bit(1) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=307 ;

-- --------------------------------------------------------

--
-- Table structure for table `POLITICS_COMMITTEES`
--

CREATE TABLE `politics_committees` (
  `id` int(8) NOT NULL auto_increment,
  `politic_id` int(8) NOT NULL,
  `committee_id` int(8) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1061 ;

-- --------------------------------------------------------

--
-- Table structure for table `POLITICS_IMAGES`
--

CREATE TABLE `politics_images` (
  `id` int(8) NOT NULL auto_increment,
  `politic_id` int(8) NOT NULL,
  `image_id` int(8) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `ROLES`
--

CREATE TABLE `roles` (
  `id` int(8) NOT NULL auto_increment,
  `name` varchar(150) default NULL,
  `description` varchar(250) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=4 ;

-- --------------------------------------------------------

--
-- Table structure for table `SCHEDULE`
--

CREATE TABLE `schedule` (
  `id` int(8) NOT NULL auto_increment,
  `category_id` int(8) NOT NULL,
  `article_id` int(8) NOT NULL,
  `start_date` datetime default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=6543 ;

-- --------------------------------------------------------

--
-- Table structure for table `TAGS`
--

CREATE TABLE `tags` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `tag_name` varchar(45) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3 ;

-- --------------------------------------------------------

--
-- Table structure for table `USERS`
--

CREATE TABLE `users` (
  `id` int(8) NOT NULL auto_increment,
  `first_name` varchar(150) default NULL,
  `last_name` varchar(150) default NULL,
  `username` varchar(150) default NULL,
  `password` varchar(150) default NULL,
  `is_active` tinyint(1) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=15 ;

-- --------------------------------------------------------

--
-- Table structure for table `USERS_ROLES`
--

CREATE TABLE `users_roles` (
  `id` int(8) NOT NULL auto_increment,
  `user_id` int(8) NOT NULL,
  `role_id` int(8) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=39 ;

-- --------------------------------------------------------

--
-- Table structure for table `sections`
--

CREATE TABLE `sections` (
  `id` int(9) unsigned NOT NULL auto_increment,
  `name` varchar(50) NOT NULL,
  `parent_id` int(9) unsigned default NULL,
  `url_name` varchar(50) default NULL,
  `options` text,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COMMENT='site sections and configuration' AUTO_INCREMENT=5 ;

