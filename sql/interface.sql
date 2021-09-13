-- auto-generated definition
create table interface_info
(
    api_id          varchar(64)  not null comment 'ID'
        primary key,
    api_method      varchar(12)  not null comment 'HttpMethod：GET、PUT、POST',
    api_path        varchar(512) not null comment '拦截路径',
    api_status      varchar(4)   not null comment '状态：-1-删除, 0-草稿，1-发布，2-有变更，3-禁用',
    api_comment     varchar(255) not null comment '注释',
    api_type        varchar(24)  not null comment '脚本类型：SQL、DataQL',
    api_script      mediumtext   not null comment '查询脚本：xxxxxxx',
    api_schema      mediumtext   not null comment '接口的请求/响应数据结构',
    api_sample      mediumtext   not null comment '请求/响应/请求头样本数据',
    api_option      mediumtext   not null comment '扩展配置信息',
    api_create_time varchar(32)  not null comment '创建时间',
    api_gmt_time    varchar(32)  not null comment '修改时间',
    constraint uk_interface_info
        unique (api_path)
)
    comment 'Dataway 中的API';

-- auto-generated definition
create table interface_release
(
    pub_id           varchar(64)  not null comment 'Publish ID'
        primary key,
    pub_api_id       varchar(64)  not null comment '所属API ID',
    pub_method       varchar(12)  not null comment 'HttpMethod：GET、PUT、POST',
    pub_path         varchar(512) not null comment '拦截路径',
    pub_status       varchar(4)   not null comment '状态：-1-删除, 0-草稿，1-发布，2-有变更，3-禁用',
    pub_comment      varchar(255) not null comment '注释',
    pub_type         varchar(24)  not null comment '脚本类型：SQL、DataQL',
    pub_script       mediumtext   not null comment '查询脚本：xxxxxxx',
    pub_script_ori   mediumtext   not null comment '原始查询脚本，仅当类型为SQL时不同',
    pub_schema       mediumtext   not null comment '接口的请求/响应数据结构',
    pub_sample       mediumtext   not null comment '请求/响应/请求头样本数据',
    pub_option       mediumtext   not null comment '扩展配置信息',
    pub_release_time varchar(32)  not null comment '发布时间（下线不更新）'
)
    comment 'Dataway API 发布历史。';

create index idx_interface_release_api
    on interface_release (pub_api_id);

create index idx_interface_release_path
    on interface_release (pub_path);

