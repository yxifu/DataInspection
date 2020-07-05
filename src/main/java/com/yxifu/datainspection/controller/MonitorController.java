package com.yxifu.datainspection.controller;

import com.yxifu.datainspection.bean.MonitorBean;
import com.yxifu.datainspection.quartz.service.QuartzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.util.List;

/**
 * @author yxifu
 * @date 2020/06/28
 **/
@RequestMapping("/monitor")
@Controller
public class MonitorController {

    @Autowired
    QuartzService quartzService;

    @RequestMapping({"","/"})
    public String index(Model model, HttpServletRequest request, HttpServletResponse response) {
        model.addAttribute("topNavBar","monitor");
        List<MonitorBean> monitorBeanList = quartzService.getMonitorBeanList();
        model.addAttribute("monitorBeanList",monitorBeanList);
        return "monitor/index";
    }

    @RequestMapping("/addTrigger")
    @ResponseBody
    public boolean addTrigger(@PathParam("jobName") String jobName){
        return this.quartzService.addTrigger(jobName);
    }
}
