/*==============================================================*/
/* Table: user                                                  */
/*==============================================================*/
create table if not exists user
(
    id                   int not null auto_increment comment '用户表ID',
    role_id              int comment '角色表ID',
    `name`                 varchar(64) comment '用户名称',
    username             varchar(64) comment '用户名',
    password             varchar(64) comment '密码',
    remark               varchar(4096) comment '备注',
    create_time          datetime comment '创建时间',
    update_time          datetime comment '修改时间',
    primary key (id)
);

/*==============================================================*/
/* Table: role                                                  */
/*==============================================================*/
create table if not exists role
(
    id                   int not null auto_increment comment '角色表ID',
    `name`                 varchar(255) comment '名称',
    parent_id            int comment '上级角色，顶级为0且唯一',
    remark               varchar(2048) comment '说明',
    primary key (id)
);


/*==============================================================*/
/* Table: permission                                            */
/*==============================================================*/
create table if not exists permission
(
    id                   int not null auto_increment comment '权限表ID',
    code                 varchar(32) comment '权限代码',
    `name`                 varchar(64) comment '权限名称',
    url_pattern          varchar(255) comment '权限路径规则',
    parent_id            int comment '上级权限ID',
    remark               varchar(512) comment '说明',
    primary key (id)
);
/*==============================================================*/
/* Table: role_permission_mid                                   */
/*==============================================================*/
create table if not exists role_permission_mid
(
    rol_id               int not null comment '角色表ID',
    per_id               int not null comment '权限表ID',
    primary key (rol_id, per_id)
);