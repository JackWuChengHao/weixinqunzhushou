create database wechatass;
use wechatass;

create table User
(
   id                   int not null AUTO_INCREMENT,
   username             varchar(20) not null,
  PRIMARY KEY (`id`)
);