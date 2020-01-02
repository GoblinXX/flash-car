create table t_area
(
  id      bigint auto_increment
    primary key,
  name    varchar(45) null,
  city_id bigint      null
)
  comment '区表';

create table t_audit_store
(
  id                    bigint auto_increment
    primary key,
  archive               tinyint(1) default '0'          null,
  version               int default '1'                 null,
  created_at            datetime                        null,
  updated_at            datetime                        null,
  name                  varchar(45)                     null,
  phone                 varchar(45)                     null
  comment '联系电话',
  business_license_up   varchar(255)                    null
  comment '营业执照正面',
  business_license_down varchar(255)                    null
  comment '营业执照背面',
  id_card_up            varchar(255)                    null
  comment '身份证正面',
  id_card_down          varchar(255)                    null
  comment '身份证背面',
  area_id               bigint                          null
  comment '区域id',
  address               varchar(127)                    null
  comment '详细地址',
  status                varchar(127) default 'AUDITING' null
  comment '申请状态',
  user_id               bigint                          null
  comment '用户id'
)
  comment '审核门店表';

create table t_carousel_img
(
  id              bigint auto_increment
    primary key,
  archive         tinyint(1) default '0' null,
  version         int default '1'        null,
  created_at      datetime               null,
  updated_at      datetime               null,
  img             varchar(255)           null
  comment '展示图片地址',
  title           varchar(127)           null
  comment '标题',
  jump_type       varchar(45)            null
  comment '跳转类型',
  jump_img        varchar(255)           null
  comment '跳转图片',
  product_id      bigint                 null
  comment '常规商品id',
  rent_product_id bigint                 null
  comment '租赁商品id'
)
  comment '轮播图表';

create table t_cart_item
(
  id         bigint auto_increment
    primary key,
  archive    tinyint(1) default '0' null,
  version    int default '1'        null,
  created_at datetime               null,
  updated_at datetime               null,
  user_id    bigint                 null,
  sku_id     bigint                 null,
  quantity   int                    null
  comment '购买数量'
)
  comment '用户购物车表';

create table t_cash
(
  id                 bigint auto_increment
    primary key,
  archive            tinyint(1) default '0'        null,
  version            int default '1'               null,
  created_at         datetime                      null,
  updated_at         datetime                      null,
  user_id            bigint                        null,
  cash_amount        decimal(10, 2) default '0.00' null
  comment '提现金额',
  cash_amount_number varchar(127)                  null
  comment '提现账户',
  bank               varchar(127)                  null
  comment '开户银行',
  cash_name          varchar(45)                   null
  comment '提现人',
  status             varchar(45) default 'PENDING' null
  comment '提现状态'
)
  comment '用户提现表';

create table t_category
(
  id         bigint auto_increment
    primary key,
  archive    tinyint(1) default '0' null,
  version    int default '1'        null,
  created_at datetime               null,
  updated_at datetime               null,
  name       varchar(45)            null
  comment '分类名称'
)
  comment '商品分类';

create table t_city
(
  id          bigint auto_increment
    primary key,
  name        varchar(45) null,
  province_id bigint      null
)
  comment '市表';

create table t_commission_records
(
  id                bigint auto_increment
    primary key,
  archive           tinyint(1) default '0' null,
  version           int default '1'        null,
  created_at        datetime               null,
  updated_at        datetime               null,
  num               decimal(10, 2)         null
  comment '佣金变化量',
  order_no          varchar(127)           null
  comment '订单编号',
  type              varchar(45)            null
  comment '类型
',
  before_commission decimal(10, 2)         null
  comment '历史佣金--操作前的佣金',
  after_commission  decimal(10, 2)         null
  comment '当前佣金--操作后的佣金',
  user_id           bigint                 null
)
  comment '用户佣金明细表';

create table t_coupon
(
  id            bigint auto_increment
    primary key,
  archive       tinyint(1) default '0'        null,
  version       int default '1'               null,
  created_at    datetime                      null,
  updated_at    datetime                      null,
  name          varchar(45)                   null,
  face_value    decimal(10, 2) default '0.00' null
  comment '面值',
  amount        int default '0'               null
  comment '数量',
  rest_amount   int default '0'               null
  comment '剩余数量',
  valid_from    datetime                      null
  comment '开始时间',
  valid_to      datetime                      null
  comment '截止时间',
  condition_use decimal(10, 2)                null
  comment '使用条件'
)
  comment '优惠券表';

create table t_distribution_center
(
  id                     bigint auto_increment
    primary key,
  archive                tinyint(1) default '0'        null,
  version                int default '1'               null,
  created_at             datetime                      null,
  updated_at             datetime                      null,
  user_id                bigint                        null
  comment '用户id
',
  superior_id            bigint                        null
  comment '关联上级用户id,没有则为null，表示系统',
  accumulated_commission decimal(10, 2) default '0.00' null
  comment '累计佣金',
  current_balance        decimal(10, 2) default '0.00' null
  comment '当前佣金'
)
  comment '分销中心表';

create table t_home_order
(
  id              bigint auto_increment
    primary key,
  archive         tinyint(1) default '0'        null,
  version         int default '1'               null,
  created_at      datetime                      null,
  updated_at      datetime                      null,
  ordered_at      datetime                      null
  comment '预约时间',
  paid_at         datetime                      null
  comment '支付时间',
  buyer_phone     varchar(45)                   null
  comment '买家电话',
  order_no        varchar(127)                  null
  comment '订单编号',
  user_id         bigint                        null,
  store_id        bigint                        null,
  coupon_id       bigint                        null,
  coupon_discount decimal(10, 2) default '0.00' null
  comment '优惠券抵扣',
  point_discount  decimal(10, 2) default '0.00' null
  comment '积分抵扣',
  sku_id          bigint                        null
  comment 'sku id',
  order_type      varchar(45)                   null
  comment '订单类型',
  address         varchar(127)                  null
  comment '上门地址--需要拼接',
  status          varchar(45)                   null
  comment '订单状态',
  total_fee       decimal(10, 2) default '0.00' null
  comment '实付金额',
  buyer_name      varchar(45)                   null
  comment '买家姓名',
  goods_fee       decimal(10, 2) default '0.00' null
  comment '订单总价',
  install_fee     decimal(10, 2) default '0.00' null
  comment '服务费'
)
  comment '上门订单表';

create table t_invitation_detail
(
  id              bigint auto_increment
    primary key,
  archive         tinyint(1) default '0'           null,
  version         int default '1'                  null,
  created_at      datetime                         null,
  updated_at      datetime                         null,
  user_id         bigint                           null
  comment '发帖人id',
  name            varchar(127)                     null
  comment '姓名',
  phone           varchar(45)                      null
  comment '电话',
  address         varchar(127)                     null
  comment '地址',
  invitation_type varchar(45)                      null
  comment '帖子分类枚举',
  supply_type     varchar(45)                      null
  comment '供求类型枚举',
  release_type    varchar(45)                      null
  comment '发布类别枚举',
  title           varchar(45)                      null
  comment '标题',
  content         text                             null
  comment '内容',
  hidden_status   tinyint(1) default '0'           null
  comment '隐藏状态(默认0,不隐藏)',
  approval_status varchar(45) default 'UNREVIEWED' null
  comment '审核状态'
)
  comment '帖子详情表';

create table t_invitation_pic
(
  id            bigint auto_increment
    primary key,
  archive       tinyint(1) default '0' null,
  version       int default '1'        null,
  created_at    datetime               null,
  updated_at    datetime               null,
  invitation_id bigint                 null
  comment '帖子详情id',
  img_url       varchar(255)           null
  comment '图片地址',
  img_type      varchar(45)            null
  comment '图片类型枚举'
)
  comment '帖子图片表';

create table t_invitation_reply
(
  id             bigint auto_increment
    primary key,
  archive        tinyint(1) default '0' null,
  version        int default '1'        null,
  created_at     datetime               null,
  updated_at     datetime               null,
  invitation_id  bigint                 null
  comment '帖子详情id',
  user_id        bigint                 null
  comment '回复人id',
  content        varchar(255)           null
  comment '回复内容',
  super_reply_id bigint default '0'     null
  comment '上级回复id(一级回复填0,二级回复填一级回复id)'
)
  comment '帖子回复表';

create table t_main_order
(
  id              bigint auto_increment
    primary key,
  archive         tinyint(1) default '0'        null,
  version         int default '1'               null,
  created_at      datetime                      null,
  updated_at      datetime                      null,
  main_order_no   varchar(127)                  null
  comment '主订单编号',
  buyer_name      varchar(127)                  null
  comment '买家姓名',
  buyer_phone     varchar(127)                  null
  comment '买家电话',
  user_id         bigint                        null
  comment '买家id',
  message         varchar(127)                  null
  comment '买家留言',
  coupon_discount decimal(10, 2) default '0.00' null
  comment '优惠券抵扣',
  coupon_id       bigint                        null,
  point_discount  decimal(10, 2) default '0.00' null
  comment '积分抵扣',
  use_point       decimal(10, 2) default '0.00' null
  comment '使用积分',
  status          varchar(45)                   null
  comment '订单状态:待付款-SUBMITTED,已付款-PAID,已完成-SUCCESS,取消-CANCELLED
​退款中-REFUNDING(只有等待接单状态才能退款),已关闭-CLOSED',
  store_id        bigint                        null,
  total_fee       decimal(10, 2) default '0.00' null
  comment '订单支付金额',
  goods_fee       decimal(10, 2) default '0.00' null
  comment '订单总金额',
  order_type      varchar(45)                   null
  comment '订单类型（到店或者上门）',
  address         varchar(255)                  null
  comment '上门地址（只针对上门订单）',
  install_fee     decimal(10, 2) default '0.00' null
  comment '安装费',
  service_fee     decimal(10, 2) default '0.00' null
  comment '服务费',
  deposit_fee     decimal(10, 2) default '0.00' null
  comment '押金',
  paid_at         datetime                      null
  comment '支付时间',
  order_time      datetime                      null
  comment '预约时间(针对上门订单)'
)
  comment '主订单表';

create table t_point_config
(
  id         bigint auto_increment
    primary key,
  archive    tinyint(1) default '0'        null,
  version    int default '1'               null,
  created_at datetime                      null,
  updated_at datetime                      null,
  scene      varchar(45)                   null
  comment '积分获得场景',
  amount     decimal(10, 2) default '0.00' null
  comment '可获得积分积分数'
)
  comment '积分配置表';

create table t_product
(
  id                       bigint auto_increment
    primary key,
  archive                  tinyint(1) default '0'        null,
  version                  int default '1'               null,
  created_at               datetime                      null,
  updated_at               datetime                      null,
  name                     varchar(127)                  null
  comment '商品名称',
  image                    varchar(255)                  null
  comment '商品主图',
  install_fee              decimal(10, 2) default '0.00' null
  comment '安装费',
  service_fee              decimal(10, 2) default '0.00' null
  comment '服务费',
  category_id              bigint                        null
  comment '分类id',
  parent_commission_ratio  decimal(10, 2)                null
  comment '上级返佣比例(计算时除以100)',
  grandpa_commission_ratio decimal(10, 2)                null
  comment '上上级返佣比例(计算时除以100)',
  content                  text                          null
  comment '商品详情',
  on_sale                  tinyint(1) default '1'        null
  comment '是否上架(1上架 0下架 默认上架)',
  on_hot                   tinyint(1) default '0'        null
  comment '是否热门(1热门 0不热门 默认不热门)'
)
  comment '商品(包括租赁商品，常规商品)';

create table t_product_pic
(
  id         bigint auto_increment
    primary key,
  archive    tinyint(1) default '0' null,
  version    int default '1'        null,
  created_at datetime               null,
  updated_at datetime               null,
  picture    varchar(255)           null
  comment '图片地址',
  product_id bigint                 null
  comment '常规商品id'
)
  comment '商品详情图表';

create table t_province
(
  id   bigint auto_increment
    primary key,
  name varchar(45) null
)
  comment '省表';

create table t_rent_order
(
  id              bigint auto_increment
    primary key,
  archive         tinyint(1) default '0'        null,
  version         int default '1'               null,
  created_at      datetime                      null,
  updated_at      datetime                      null,
  user_id         bigint                        null,
  order_no        varchar(127)                  null
  comment '订单编号',
  buyer_phone     varchar(45)                   null
  comment '买家电话',
  store_id        bigint                        null
  comment '门店id',
  rent_time_id    bigint                        null
  comment '租期id',
  status          varchar(45)                   null
  comment '订单状态',
  message         varchar(127)                  null
  comment '买家留言',
  coupon_discount decimal(10, 2) default '0.00' null
  comment '优惠券抵扣',
  coupon_id       bigint                        null,
  point_discount  decimal(10, 2) default '0.00' null
  comment '积分抵扣',
  goods_fee       decimal(10, 2) default '0.00' null
  comment '订单总价',
  total_fee       decimal(10, 2) default '0.00' null
  comment '支付总价',
  order_type      varchar(45)                   null
  comment '订单类型--租赁订单',
  deposit         decimal(10, 2) default '0.00' null
  comment '押金',
  buyer_name      varchar(45)                   null
  comment '买家姓名'
)
  comment '租赁订单表';

create table t_rent_product
(
  id                       bigint auto_increment
    primary key,
  archive                  tinyint(1) default '0'        null,
  version                  int default '1'               null,
  created_at               datetime                      null,
  updated_at               datetime                      null,
  name                     varchar(127)                  null,
  image                    varchar(127)                  null
  comment '主图',
  amount                   int                           null
  comment '库存',
  cost_price               decimal(10, 2) default '0.00' null
  comment '成本价(元/天)',
  deposit                  decimal(10, 2) default '0.00' null
  comment '押金',
  on_sale                  tinyint(1) default '1'        null
  comment '是否上架(1上架 0下架 默认上架)',
  content                  text                          null
  comment '租赁商品详情',
  parent_commission_ratio  decimal(10, 2)                null
  comment '上级返佣比例(计算时需除以100)',
  grandpa_commission_ratio decimal(10, 2)                null
  comment '上上级返佣比例(计算时需除以100)'
)
  comment '租赁商品表';

create table t_rent_product_pic
(
  id              bigint auto_increment
    primary key,
  archive         tinyint(1) default '0' null,
  version         int default '1'        null,
  created_at      datetime               null,
  updated_at      datetime               null,
  picture         varchar(127)           null
  comment '图片地址',
  rent_product_id bigint                 null
  comment '租赁商品id'
)
  comment '租赁商品详情图片';

create table t_rent_product_time
(
  id              bigint auto_increment
    primary key,
  archive         tinyint(1) default '0'        null,
  version         int default '1'               null,
  created_at      datetime                      null,
  updated_at      datetime                      null,
  tenancy         int                           null
  comment '租期',
  price           decimal(10, 2) default '0.00' null
  comment '租金/天',
  rent_product_id bigint                        null
  comment '租赁商品id'
)
  comment '租赁商品租期';

create table t_road_rescue
(
  id         bigint auto_increment
    primary key,
  archive    tinyint(1) default '0'        null,
  version    int default '1'               null,
  created_at datetime                      null,
  updated_at datetime                      null,
  name       varchar(127)                  null
  comment '名称',
  price      decimal(10, 2) default '0.00' null,
  picture    varchar(127)                  null
)
  comment '道路救援表';

create table t_road_rescue_order
(
  id               bigint auto_increment
    primary key,
  archive          tinyint(1) default '0'            null,
  version          int default '1'                   null,
  created_at       datetime                          null,
  updated_at       datetime                          null,
  paid_at          datetime                          null
  comment '支付时间',
  goods_fee        decimal(10, 2) default '0.00'     null
  comment '订单总金额',
  total_fee        decimal(10, 2) default '0.00'     null
  comment '订单支付金额',
  user_id          bigint                            null
  comment '买家id',
  buyer_name       varchar(45)                       null
  comment '买家姓名',
  buyer_phone      varchar(45)                       null
  comment '买家电话',
  status           varchar(45)                       null
  comment '待付款-SUBMITTED 待接单-PAID 待处理-PROCESSING 已完成-SUCCESS,取消-CANCELLED,退款中-REFUNDING,已关闭-CLOSED',
  order_no         varchar(127)                      null
  comment '订单号',
  store_id         bigint                            null
  comment '门店id',
  address          varchar(127)                      null
  comment '选择地址',
  detailed_address varchar(127)                      null
  comment '详细地址',
  order_type       varchar(45) default 'ROAD_RESCUE' null
  comment '服务类型，默认道路救援',
  road_rescue_id   bigint default '1'                null
  comment '道路救援商品id
'
)
  comment '道路救援订单表';

create table t_road_rescue_pic
(
  id             bigint auto_increment
    primary key,
  archive        tinyint(1) default '0' null,
  version        int default '1'        null,
  created_at     datetime               null,
  updated_at     datetime               null,
  road_rescue_id bigint                 null
  comment '道路救援订单id',
  image          varchar(255)           null
  comment '故障图片'
)
  comment '道路救援故障表';

create table t_sku
(
  id             bigint auto_increment
    primary key,
  archive        tinyint(1) default '0'        null,
  version        int default '1'               null,
  created_at     datetime                      null,
  updated_at     datetime                      null,
  name           varchar(127)                  null
  comment '规格名称',
  sale_price     decimal(10, 2) default '0.00' null
  comment '售价',
  original_price decimal(10, 2) default '0.00' null
  comment '原价',
  cost_price     decimal(10, 2) default '0.00' null
  comment '成本价',
  amount         int                           null
  comment '库存',
  sale_amount    int default '0'               null
  comment '销量',
  product_id     bigint                        null
  comment '关联常规商品id'
)
  comment '常规商品规格';

create table t_store
(
  id             bigint auto_increment
    primary key,
  archive        tinyint(1) default '0' null,
  version        int default '1'        null,
  created_at     datetime               null,
  updated_at     datetime               null,
  name           varchar(127)           null
  comment '店铺名称',
  image          varchar(127)           null
  comment '门店主图',
  address        varchar(127)           null
  comment '门店地址',
  longitude      varchar(127)           null
  comment '经度',
  latitude       varchar(127)           null
  comment '纬度',
  phone          varchar(127)           null
  comment '门店电话',
  content        text                   null
  comment '门店详情',
  audit_store_id bigint                 null,
  on_sale        tinyint(1) default '0' null
  comment '门店上下架状态，默认下架0'
)
  comment '门店表';

create table t_store_order
(
  id              bigint auto_increment
    primary key,
  archive         tinyint(1) default '0'        null,
  version         int default '1'               null,
  created_at      datetime                      null,
  updated_at      datetime                      null,
  main_order_id   bigint                        null
  comment '主订单id',
  order_no        varchar(127)                  null
  comment '订单编号',
  buyer_name      varchar(45)                   null
  comment '买家姓名',
  buyer_phone     varchar(45)                   null
  comment '买家电话',
  user_id         bigint                        null,
  sku_id          bigint                        null
  comment 'sku id',
  amount          int default '0'               null
  comment 'sku下单数量',
  store_id        bigint                        null,
  status          varchar(45)                   null
  comment '订单状态:待付款-SUBMITTED,已付款-PAID,已完成-SUCCESS,取消-CANCELLED
​退款中-REFUNDING(只有等待接单状态才能退款),已关闭-CLOSED',
  total_fee       decimal(10, 2) default '0.00' null
  comment '实付金额',
  goods_fee       decimal(10, 2) default '0.00' null
  comment '商品总价',
  coupon_discount decimal(10, 2) default '0.00' null
  comment '优惠券抵扣',
  point_discount  decimal(10, 2) default '0.00' null
  comment '积分抵扣',
  use_point       decimal(10, 2) default '0.00' null
  comment '使用积分',
  service_fee     decimal(10, 2) default '0.00' null
  comment '安装费',
  paid_at         datetime                      null
  comment '支付时间'
)
  comment '到店订单表';

create table t_store_pic
(
  id         bigint auto_increment
    primary key,
  archive    tinyint(1) default '0' null,
  version    int default '1'        null,
  created_at datetime               null,
  updated_at datetime               null,
  store_id   bigint                 null,
  image      varchar(255)           null
  comment '门店详情图'
)
  comment '门店详情图表';

create table t_store_user
(
  id         bigint auto_increment
    primary key,
  archive    tinyint(1) default '0' null,
  version    int default '1'        null,
  created_at datetime               null,
  updated_at datetime               null,
  username   varchar(127)           null
  comment '门店后台账户',
  password   varchar(127)           null
  comment '门店后台密码',
  store_id   bigint                 null
)
  comment '门店后台用户管理表';

create table t_sys_authority
(
  id         bigint auto_increment
    primary key,
  archive    tinyint(1) default '0' null,
  version    int default '1'        null,
  created_at datetime               null,
  updated_at datetime               null,
  user_id    bigint                 null,
  menu_name  varchar(127)           null
  comment '权限名称'
)
  comment '门店总后台权限表';

create table t_sys_user
(
  id         bigint auto_increment
    primary key,
  archive    tinyint(1) default '0' null,
  version    int default '1'        null,
  created_at datetime               null,
  updated_at datetime               null,
  username   varchar(127)           null,
  password   varchar(127)           null,
  store_id   bigint default '0'     null
  comment '门店id，默认为0 表示总后台'
)
  comment '门店总后台表';

create table t_user_address
(
  id         bigint auto_increment
    primary key,
  archive    tinyint(1) default '0' null,
  version    int default '1'        null,
  created_at datetime               null,
  updated_at datetime               null,
  user_id    bigint                 null
  comment '用户id',
  contact    varchar(45)            null
  comment '联系人',
  phone      varchar(45)            null
  comment '电话',
  area_id    bigint                 null
  comment '区id',
  street     varchar(255)           null
  comment '详细地址',
  on_default tinyint(1) default '0' null
  comment '是否设置为默认地址,默认为0'
)
  comment '用户地址表';

create table t_user_coupon
(
  id         bigint auto_increment
    primary key,
  archive    tinyint(1) default '0' null,
  version    int default '1'        null,
  created_at datetime               null,
  updated_at datetime               null,
  user_id    bigint                 null,
  coupon_id  bigint                 null,
  usable     tinyint(1) default '1' null
  comment '是否可用'
)
  comment '用户优惠券表';

create index t_user_coupon_user_id_index
  on t_user_coupon (user_id);

create table t_user_point
(
  id               bigint auto_increment
    primary key,
  archive          tinyint(1) default '0'        null,
  version          int default '1'               null,
  created_at       datetime                      null,
  updated_at       datetime                      null,
  user_id          bigint                        null,
  available_point  decimal(10, 2) default '0.00' null
  comment '可用积分',
  cumulative_point decimal(10, 2) default '0.00' null
  comment '累计积分（只加不减）'
)
  comment '用户积分表';

create table t_user_point_record
(
  id             bigint auto_increment
    primary key,
  archive        tinyint(1) default '0'        null,
  version        int default '1'               null,
  created_at     datetime                      null,
  updated_at     datetime                      null,
  user_id        bigint                        null,
  scene          varchar(45)                   null
  comment '场景：评论成功后、签到、邀请新用户首次下单获得、​订单取消、订单退款、订单抵扣',
  record         decimal(10, 2) default '0.00' null
  comment '积分记录 正或负',
  previous_point decimal(10, 2) default '0.00' null
  comment '历史积分（计算之前的积分）',
  current_point  decimal(10, 2) default '0.00' null
  comment '当前积分（计算之后的积分）',
  order_no       varchar(127)                  null
  comment '订单编号'
)
  comment '用户积分记录表';

create table t_user_punch
(
  id               bigint auto_increment
    primary key,
  archive          tinyint(1) default '0'             null,
  version          int default '1'                    null,
  created_at       datetime                           null,
  updated_at       datetime                           null,
  user_id          bigint                             null,
  accumulated_days int default '0'                    null
  comment '累计签到天数',
  punch_time       datetime default CURRENT_TIMESTAMP null
  comment '签到时间'
)
  comment '用户签到表';

create table t_we_chat_config
(
  id           bigint auto_increment
    primary key,
  archive      tinyint(1) default '0' null,
  version      int default '1'        null,
  created_at   datetime               null,
  updated_at   datetime               null,
  app_id       varchar(127)           null,
  app_secret   varchar(127)           null,
  access_token varchar(127)           null,
  expired_at   datetime               null
)
  comment '微信配置表';

create table t_we_chat_user
(
  id          bigint auto_increment
    primary key,
  archive     tinyint(1) default '0' null,
  version     int default '1'        null,
  created_at  datetime               null,
  updated_at  datetime               null,
  open_id     varchar(255)           null,
  union_id    varchar(255)           null,
  session_key varchar(255)           null
  comment '登录态',
  phone       varchar(45)            null
  comment '手机号',
  gender      tinyint(1) default '0' null
  comment '性别0未知 1男 2女',
  nickname    varchar(127)           null
  comment '昵称',
  avatar      varchar(255)           null
  comment '头像',
  username    varchar(127)           null
  comment '用户名',
  password    varchar(255)           null
  comment '密码'
)
  comment '微信用户表';

