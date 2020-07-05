package com.yxifu.datainspection.quartz.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yxifu.datainspection.bean.MonitorBean;
import com.yxifu.datainspection.bean.MonitorTriggerBean;
import com.yxifu.datainspection.entity.Group;
import com.yxifu.datainspection.quartz.job.DataInspectionJob;
import com.yxifu.datainspection.service.IGroupService;
import com.yxifu.datainspection.service.ITriggerService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * @author yxifu
 * @date 2020/06/14
 **/
@Slf4j
@Service
public class QuartzService {
    static  SchedulerFactory schedulerFactory;
    public static Scheduler scheduler=getOne();

    @Autowired
    IGroupService iGroupService;

    @Autowired
    ITriggerService iTriggerService;

    private static Scheduler getOne()
    {
        if(scheduler==null) {
            schedulerFactory = new StdSchedulerFactory();
            try {
                scheduler = schedulerFactory.getScheduler();
                scheduler.start();
            } catch (SchedulerException e) {
                e.printStackTrace();
                log.error("getOne",e);
            }
        }
        return scheduler;
    }
    public static boolean isStarted()
    {
        try {
            return scheduler.isStarted();
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.error("isStarted",e);
            return false;
        }
    }

    public void initByGroup(){
        //test1();
        log.info("初始化触发器=====================");
        try {
            scheduler.clear();
            QueryWrapper<Group> queryWrapperGroup = new QueryWrapper<>();
            queryWrapperGroup.lambda().eq(Group::getStatus,1).eq(Group::getIsSend,1);

            List<Group> groupList = iGroupService.list(queryWrapperGroup);
            for (Group group: groupList) {
                JobDetail jobDetail = newJob(DataInspectionJob.class)
                        .withIdentity(group.getGroupId().toString()+"_"+group.getName(),"DI")
                        .build();
                jobDetail.getJobDataMap().put("group",group);

                QueryWrapper<com.yxifu.datainspection.entity.Trigger> queryWapper = new QueryWrapper<>();
                queryWapper.lambda().eq(com.yxifu.datainspection.entity.Trigger::getGroupId,group.getGroupId())
                .eq(com.yxifu.datainspection.entity.Trigger::getStatus,1);
                List<com.yxifu.datainspection.entity.Trigger> triggerList = iTriggerService.list(queryWapper);

                int index=0;
                for (com.yxifu.datainspection.entity.Trigger trigger: triggerList) {
                    Trigger trigger1 = getDataInspectionJobTrigger(jobDetail, trigger);
                    if(index==0) {
                        scheduler.scheduleJob(jobDetail, trigger1);
                    }
                    else {
                        scheduler.scheduleJob( trigger1);
                    }
                    index++;
                }
            }
            scheduler.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.error("初始化触发器出错",e);
        }
        log.info("END 初始化触发器=====================");
    }
/*
    public void test1() throws SchedulerException {

        JobDetail jobDetail = getJobDetail("default","job1");
        Trigger trigger1 = getJobTrigger(jobDetail,"trigger1");
        Trigger trigger2 = getJobTrigger(jobDetail,"trigger2");
        scheduler.scheduleJob(jobDetail,trigger1);
        //scheduler.scheduleJob(jobDetail,trigger2);

        //开始scheduler对任务进行调度
        scheduler.start();
        scheduler.scheduleJob(trigger2);
//            scheduler.shutdown();停止scheduler
    }

    private static  JobDetail getJobDetail(String jobName,String group){
        return newJob(MyJob.class)
                .withIdentity(jobName,group)
                .build();
    }*/

/*    private static  Trigger getJobTrigger(JobDetail jobDetail,String triggerName){
        return newTrigger()
                .withIdentity(triggerName, jobDetail.getKey().getGroup())
                .startNow() // start the job by now
//                .withSchedule(dailyAtHourAndMinute(0, 0))
                .withSchedule(simpleSchedule()
                        .withIntervalInSeconds(5)
                        .repeatForever())
                .forJob(jobDetail)
                .build();
    }*/
    private Trigger getDataInspectionJobTrigger(JobDetail jobDetail,com.yxifu.datainspection.entity.Trigger trigger){

        TriggerBuilder<Trigger> tb = newTrigger();
        tb.withIdentity(trigger.getId().toString()+"_"+trigger.getTitle(), jobDetail.getKey().getGroup())
                .startNow();
        if(trigger.getTriggerType()==0){
            tb.withSchedule(simpleSchedule()
                    //.withIntervalInSeconds(5)
                    .withIntervalInMinutes(trigger.getInterval())
                    .repeatForever());
        } else if(trigger.getTriggerType()==1){
            tb.withSchedule(CronScheduleBuilder.cronSchedule(trigger.getCorn()));
        }
        return tb.forJob(jobDetail)
                .build();
    }
    public List<MonitorBean> getMonitorBeanList(){
        //scheduler.getJobKeys();
        List<MonitorBean> monitorBeanList = new ArrayList<>();
        try {
            //Set<JobKey> jobKeySet = scheduler.getJobKeys(GroupMatcher.anyGroup());
            Set<JobKey> jobKeySet = scheduler.getJobKeys(GroupMatcher.jobGroupEquals("DI"));
            for (JobKey jobKey:jobKeySet) {
                MonitorBean monitorBean = new MonitorBean();

                JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                Group group = (Group) jobDetail.getJobDataMap().get("group");
                monitorBean.setGroup(group);
                monitorBean.setJobFullName(jobKey.getName());
                List<MonitorTriggerBean> monitorTriggerBeanList = new ArrayList<>();
                List<Trigger> triggerList = (List<Trigger>)scheduler.getTriggersOfJob(jobKey);
                for (Trigger trigger:triggerList) {
                    MonitorTriggerBean monitorTriggerBean = new MonitorTriggerBean();

                    monitorTriggerBean.setTriggerFullName(trigger.getKey().getName());
                    monitorTriggerBean.setPreviousFireTime(trigger.getPreviousFireTime());
                    monitorTriggerBean.setNextFireTime(trigger.getNextFireTime());

                    if(trigger instanceof SimpleTrigger){
                        SimpleTrigger simpleTrigger = (SimpleTrigger)trigger;
                        //System.out.println(simpleTrigger.getRepeatInterval());
                        monitorTriggerBean.setType(0);
                        System.out.println(simpleTrigger.getRepeatCount());
                        monitorTriggerBean.setInterval(simpleTrigger.getRepeatInterval());
                    } else if(trigger instanceof  CronTrigger)
                    {
                        CronTrigger cronTrigger= (CronTrigger)trigger;
                        monitorTriggerBean.setType(1);
                        monitorTriggerBean.setCronExpression(cronTrigger.getCronExpression());
                    }
                    monitorTriggerBeanList.add(monitorTriggerBean);
                    //System.out.println(trigger.getPreviousFireTime());
                    //System.out.println(trigger.getNextFireTime());
                    //System.out.println(trigger.getCalendarName());
                    //System.out.println(trigger);
                }
                monitorBean.setMonitorTriggerBeanList(monitorTriggerBeanList);
                monitorBeanList.add(monitorBean);
                //Trigger trigger=null;

            }
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.error("getAll",e);
        }
        return monitorBeanList;
    }
    public boolean addTrigger(String jobName){

        JobKey jobKey = new JobKey(jobName,"DI");
        try {
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            if(jobDetail==null){
                return false;
            }

            Date date  = new Date(System.currentTimeMillis()+5*1000);
            SimpleTrigger trigger = (SimpleTrigger) newTrigger()
                .withIdentity("temp_"+System.currentTimeMillis(), jobDetail.getKey().getGroup())
                .startAt(date)
                .forJob(jobDetail)
                .build();
            scheduler.scheduleJob(trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
