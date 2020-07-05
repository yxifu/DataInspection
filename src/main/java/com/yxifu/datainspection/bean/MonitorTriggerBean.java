package com.yxifu.datainspection.bean;

import lombok.Data;

import java.util.Date;

/**
 * @author yxifu
 * @date 2020/06/28
 **/
@Data
public class MonitorTriggerBean {

    String triggerFullName;
    Date previousFireTime;
    Date nextFireTime;
    int type;
    long Interval;
    String CronExpression;

    public String toExplain(){
        if(type==0){
            if(Interval>0) {
                return "每" + (Interval / 1000) + "分钟执行一次";
            } else {
                return  "仅执行一次";
            }

        } else if(type==1){
            return this.CronExpression;
        }
        return null;
    }
}
