package com.yxifu.datainspection;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author yxifu
 * @date 2020/04/11
 **/
@SpringBootApplication
@MapperScan("com.yxifu.dataInspection.mapper")
public class DataInspectionAPP {
    public static void main(String[] args) {
        String ud = System.getProperty("user.dir");
        System.out.println("user.dir:"+ud);
        System.out.println("如果数据库文件不存在，复制到:"+ud);
        System.out.println("========================");
        SpringApplication.run(DataInspectionAPP.class,args);

        //String ud = System.getProperty("user.dir");
        System.out.println("user.dir:"+ud);
        /*System.out.println("========================");
        System.out.println("启动巡检作业");
        try {
            QuartzService.();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }*/
    }
}
