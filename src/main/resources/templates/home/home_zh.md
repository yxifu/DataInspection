[English READEME_en.md](https://github.com/yxifu/DataInspection/tree/master/src/main/resources/templates/home/home_en.md)

# Data Inspection(数据巡检)
- 暂时只开发了中文版，稍后会推中英双语版
## 介绍
### 简介
查找错误数据，邮件提醒相关人员。

### 背景介绍
- 工作中，数据难免有错误，总是收到用户反馈后才去处理，很被动。
- 为解决此问题，就写了定时查SQL发邮件的功能，提前发现问题数据。
- 此项目是为了实现简单灵活管理数据巡检而开发。

### 功能说明
本项目利用SQL，检查数据是否有错误数据，然后发送邮件提醒。
主要功能：
- 配置巡检组
- 配置巡检项（SQL,支持数据库sqllite/mysql/oracle/sql server/）
- 配置触发器（定期执行，还每隔一段时间执行）
- 发送邮件（可以设置，有错误数据发送或固定发送）
- 支持中英双语（后台续完善英文）
### 技术说明
- 使用 Spring boot 2.3.1 + thymeleaf + sqlite + layui
- 使用sqllite，是为了减少数据库依赖，方便移值安装。


## 安装
### JAR安装说明

### Docker安装说明

## 使用
### 使用说明

### 找回密码
