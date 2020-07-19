package com.yxifu.datainspection.controller;


import com.yxifu.datainspection.bean.ApiInterface;
import com.yxifu.datainspection.entity.Trigger;
import com.yxifu.datainspection.quartz.service.QuartzService;
import com.yxifu.datainspection.service.ITriggerService;
import com.yxifu.datainspection.util.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yxifu
 * @since 2020-06-14
 */
@Controller
@RequestMapping("/trigger")
public class TriggerController {

    @Autowired
    ITriggerService iTriggerService;

    @Autowired
    QuartzService quartzService;

    @RequestMapping(value = "/edit",method = RequestMethod.GET)
    public String index(Model model, @PathParam("groupId") int groupId, @PathParam("id") int id, HttpServletRequest request, HttpServletResponse response){
        Trigger trigger = new Trigger();
        if(id==0){
            trigger.setGroupId(groupId);
            trigger.setId(id);
        } else {
            trigger = this.iTriggerService.getById(id);
        }

        model.addAttribute("trigger",trigger);
        return "trigger/edit";
    }


    @ResponseBody
    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    public ApiInterface<Trigger> index(Model model, Trigger trigger
            , HttpServletRequest request, HttpServletResponse response){
        int id= trigger.getId();
        ApiInterface<Trigger> apiInterface = new ApiInterface<>();
        if(id>0) {
            trigger.setLastUpdateTime(Tools.formatDate(Tools.DateFormat_yyyyMMDDHHmmss));
            boolean b = this.iTriggerService.updateById(trigger);
            apiInterface.setData(trigger);
            if(!b){
                apiInterface.setError_code(500);
                apiInterface.setMessage("更新出错");
            }
        }
        else
        {
            int count = this.iTriggerService.getBaseMapper().insert(trigger);
            apiInterface.setData(trigger);
            if(count==0){
                apiInterface.setError_code(500);
                apiInterface.setMessage("添加出错");
            }
        }
        this.quartzService.initByGroup();
        return apiInterface;
    }
}
