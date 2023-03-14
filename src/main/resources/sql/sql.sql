CREATE TABLE `test` (
                          `id` bigint(20) NOT NULL AUTO_INCREMENT,
                          `content` varchar(255) DEFAULT NULL COMMENT '内容',
                          `create_time` datetime DEFAULT NULL,
                          `last_update_time` datetime DEFAULT NULL,
                          `version_id` bigint(20) DEFAULT '0',
                          PRIMARY KEY (`id`)
);

create table test_0 like test;
create table test_1 like test;