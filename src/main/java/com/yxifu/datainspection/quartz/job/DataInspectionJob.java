package com.yxifu.datainspection.quartz.job;

import com.yxifu.datainspection.entity.Group;
import com.yxifu.datainspection.service.GroupResultService;
import com.yxifu.datainspection.util.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author yxifu
 * @date 2020/06/14
 **/
@Slf4j
public class DataInspectionJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("=================");
        log.info("触发器：" + jobExecutionContext.getTrigger().getKey().getName());
        Group group = (Group)(jobExecutionContext.getJobDetail().getJobDataMap().get("group"));
        if(group.getIsSend()>0 && group.getIsSendEmail()>0) {
            GroupResultService groupResultService= SpringUtils.getBean("groupResultService");
            groupResultService.exec(group);
        }
        log.info(jobExecutionContext.getTrigger().getKey().getName());
    }
}
