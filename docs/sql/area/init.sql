create table t_area
(
  id          bigint auto_increment
    primary key,
  name        varchar(45) null,
  city_id bigint      null
)
  comment '区表';

create table t_city
(
  id          bigint auto_increment
    primary key,
  name        varchar(45) null,
  province_id bigint      null
)
  comment '市表';

create table t_province
(
  id          bigint auto_increment
    primary key,
  name        varchar(45) null
)
  comment '省表';

