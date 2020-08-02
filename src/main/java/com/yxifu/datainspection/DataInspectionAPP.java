package com.yxifu.datainspection;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author yxifu
 * @date 2020/04/11
 **/
@Slf4j
@SpringBootApplication
@MapperScan("com.yxifu.dataInspection.mapper")
public class DataInspectionAPP {
    public static void main(String[] args) {
        log.info("Start checking the environment");
        String projectHome = System.getProperty("project.home");
        log.info("project.home:"+projectHome);
        if(projectHome==null || "".equals(projectHome)) {
            projectHome = System.getProperty("user.dir");
            System.setProperty("project.home",projectHome);
            log.info("user.dir:"+projectHome);
            log.info("Variable not found:project.home");
            log.info("Use: user.dir replace project.home");
            log.info("project.home:"+projectHome);
        }
        log.info("END check the environment");
        System.out.println("======================");
        SpringApplication.run(DataInspectionAPP.class,args);
    }
}
