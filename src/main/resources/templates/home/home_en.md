[中文 READEME_zh.md](https://github.com/yxifu/DataInspection/tree/master/src/main/resources/templates/home/home_zh.md)

# Data Inspection
- Only the Chinese version has been developed for the time being, and the bilingual version will be released later
## Introduce
### BriefIntroduction
Find the wrong data, email to remind relevant personnel.

### Background
- In the work, there are inevitably errors in the data, which are always handled after receiving feedback from users, which is very passive.
- In order to solve this problem, the function of regularly checking SQL email is written to discover the problem data in advance.
- This project is developed to realize simple and flexible management of data patrol.

### Function description
This project uses SQL to check whether there is wrong data in the data, and then send email reminder.
Main functions:
- Configure Inspection group
- Configure patrol items (SQL, support database sqllite / MySQL / Oracle / SQL Server /)
- configure triggers (execute regularly, and execute at regular intervals)
- send e-mail (can be set, send wrong data or send fixed data) 
- support Chinese and English (continue to improve English in the background)
### Technical description
- Use Spring boot 2.3.1 + thymeleaf + sqlite + layui
- Usesqllite，In order to reduce database dependence and facilitate value shifting installation.


## Install
### JAR Installation instructions

### Docker Installation instructions

## Use
### Instructions

### Instructions
