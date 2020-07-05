package com.yxifu.datainspection.system;

import com.yxifu.datainspection.quartz.service.QuartzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author yxifu
 * @date 2020/06/25
 **/
@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    @Autowired
    QuartzService quartzService;

    @Override
    public void run(String... args) throws Exception {
        /*System.out.println("通过实现CommandLineRunner接口，在spring boot项目启动后打印参数");
        for (String arg : args) {
            System.out.print(arg + " ");
        }
        System.out.println();*/

        quartzService.initByGroup();
    }
}