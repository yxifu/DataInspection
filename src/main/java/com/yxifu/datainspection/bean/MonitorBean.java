package com.yxifu.datainspection.bean;

import com.yxifu.datainspection.entity.Group;
import lombok.Data;

import java.util.List;

/**
 * @author yxifu
 * @date 2020/06/28
 **/
@Data
public class MonitorBean {
    Group group;
    String jobFullName;
    List<MonitorTriggerBean> monitorTriggerBeanList;


}
