package com.yxifu.datainspection.quartz.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author yxifu
 * @date 2020/06/14
 **/
@Slf4j
public class MyJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("测试作业执行：" + jobExecutionContext.getTrigger().getKey().getName());
    }
}
