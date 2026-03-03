# Database initialization

-- Create database
create database if not exists ai_code;

-- Switch database
use ai_code;

-- User table
-- DDL statements below

-- User table
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userAccount  varchar(256)                           not null comment 'Account',
    userPassword varchar(512)                           not null comment 'Password',
    userName     varchar(256)                           null comment 'Display name',
    userAvatar   varchar(1024)                          null comment 'Avatar URL',
    userProfile  varchar(512)                           null comment 'User profile',
    userRole     varchar(256) default 'user'            not null comment 'User role: user/admin',
    editTime     datetime     default CURRENT_TIMESTAMP not null comment 'Edit time',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment 'Creation time',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment 'Update time',
    isDelete     tinyint      default 0                 not null comment 'Logical delete flag',
    UNIQUE KEY uk_userAccount (userAccount),
    INDEX idx_userName (userName)
) comment 'User' collate = utf8mb4_unicode_ci;

-- Application table
create table app
(
    id           bigint auto_increment comment 'id' primary key,
    appName      varchar(256)                       null comment 'Application name',
    cover        varchar(512)                       null comment 'Application cover',
    initPrompt   text                               null comment 'Initial prompt for the application',
    codeGenType  varchar(64)                        null comment 'Code generation type (enum)',
    deployKey    varchar(64)                        null comment 'Deployment key',
    deployedTime datetime                           null comment 'Deployment time',
    priority     int      default 0                 not null comment 'Priority',
    userId       bigint                             not null comment 'Creator user id',
    editTime     datetime default CURRENT_TIMESTAMP not null comment 'Edit time',
    createTime   datetime default CURRENT_TIMESTAMP not null comment 'Creation time',
    updateTime   datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment 'Update time',
    isDelete     tinyint  default 0                 not null comment 'Logical delete flag',
    UNIQUE KEY uk_deployKey (deployKey), -- Ensure deploy key uniqueness
    INDEX idx_appName (appName),         -- Speed up name-based queries
    INDEX idx_userId (userId)            -- Speed up user id queries
) comment 'Application' collate = utf8mb4_unicode_ci;

-- Chat history table
create table chat_history
(
    id          bigint auto_increment comment 'id' primary key,
    message     text                               not null comment 'Message body',
    messageType varchar(32)                        not null comment 'user/ai',
    appId       bigint                             not null comment 'Application id',
    userId      bigint                             not null comment 'Creator user id',
    createTime  datetime default CURRENT_TIMESTAMP not null comment 'Creation time',
    updateTime  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment 'Update time',
    isDelete    tinyint  default 0                 not null comment 'Logical delete flag',
    INDEX idx_appId (appId),                       -- Speed up application-based queries
    INDEX idx_createTime (createTime),             -- Speed up time-based queries
    INDEX idx_appId_createTime (appId, createTime) -- Core index for cursor pagination
) comment 'Chat history' collate = utf8mb4_unicode_ci;
