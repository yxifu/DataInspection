package com.yxifu.datainspection.config;

import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author yxifu
 * @date 2020/07/12
 **/
@Configuration
public class DBInitConfig {

    @PostConstruct
    public void initDB(){
        System.out.println("初始化数据库222222222222");
    }
}
