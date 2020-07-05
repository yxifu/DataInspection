package com.yxifu.datainspection.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yxifu.datainspection.bean.ApiInterface;
import com.yxifu.datainspection.bean.GroupResultBean;
import com.yxifu.datainspection.entity.Group;
import com.yxifu.datainspection.entity.Item;
import com.yxifu.datainspection.entity.Trigger;
import com.yxifu.datainspection.quartz.service.QuartzService;
import com.yxifu.datainspection.service.GroupResultService;
import com.yxifu.datainspection.service.IGroupService;
import com.yxifu.datainspection.service.IItemService;
import com.yxifu.datainspection.service.ITriggerService;
import com.yxifu.datainspection.util.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.util.List;
import java.util.UUID;

/**
 * @author yxifu
 * @date 2020/05/24
 **/

@RequestMapping("group")
@Controller
public class GroupController {


    @Autowired
    IGroupService iGroupService;

    @Autowired
    IItemService iItemService;

    @Autowired
    ITriggerService iTriggerService;

    @Autowired
    GroupResultService groupResultService;

    @Autowired
    QuartzService quartzService;

    @RequestMapping({"","/"})
    public String index(Model model, HttpServletRequest request, HttpServletResponse response) {
        return "redirect:/group/list";
    }

    @RequestMapping("/list")
    public String list(Model model, HttpServletRequest request, HttpServletResponse response){

        List<Group> list = this.iGroupService.list();
        System.out.println(list);
        model.addAttribute("groupList",list);
        model.addAttribute("topNavBar","group");
        System.out.println(request.getLocale());
        return "group/list";
    }


    @RequestMapping(value = "/edit",method = RequestMethod.GET)
    public String index(Model model, @PathParam("id") int id, HttpServletRequest request, HttpServletResponse response){

        Group group = new Group();
        if(id==0){
            group.setGroupId(0);
            group.setGroupCode(UUID.randomUUID().toString());
        } else {
            group = this.iGroupService.getById(id);
        }

        model.addAttribute("group",group);
        model.addAttribute("apiInterface",null);
        model.addAttribute("topNavBar","group");
        return "group/edit";
    }


    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    public String index(Model model, Group group
            , HttpServletRequest request, HttpServletResponse response){
        int id= group.getGroupId();
        ApiInterface<Group> apiInterface = new ApiInterface<>();
        if(id>0) {
            group.setLastUpdateTime(Tools.formatDate(Tools.DateFormat_yyyyMMDDHHmmss));
            boolean b = this.iGroupService.updateById(group);
            this.quartzService.initByGroup();
            //model.addAttribute("ispost",true);
            apiInterface.setData(group);
            if(!b){
                apiInterface.setError_code(500);
                apiInterface.setMessage("更新出错");
            }
        }
        else
        {
            //conn.setStatus(1);
            //conn.setCreateTime(new Date());
            int count = this.iGroupService.getBaseMapper().insert(group);
            apiInterface.setData(group);
            if(count==0){
                apiInterface.setError_code(500);
                apiInterface.setMessage("添加出错");
            }
        }
        model.addAttribute("conn",group);
        model.addAttribute("apiInterface",apiInterface);
        model.addAttribute("topNavBar","group");
        //System.out.println(request.getLocale());
        return "group/edit";
    }

    @RequestMapping("/view")
    public String view(Model model,@PathParam("id") int id, HttpServletRequest request, HttpServletResponse response){

        Group group = this.iGroupService.getById(id);

        QueryWrapper<Item> queryWapper = new QueryWrapper<>();
        queryWapper.lambda().eq(Item::getGroupId,id).ne(Item::getStatus,-1);
        List<Item> itemList = this.iItemService.list(queryWapper);


        QueryWrapper<Trigger> queryWapperTrigger = new QueryWrapper<>();
        queryWapperTrigger.lambda().eq(Trigger::getGroupId,id).ne(Trigger::getStatus,-1);
        List<Trigger> triggerList = this.iTriggerService.list(queryWapperTrigger);

        model.addAttribute("group",group);
        model.addAttribute("itemList",itemList);
        model.addAttribute("triggerList",triggerList);
        return "group/view";
    }


    @RequestMapping("/result")
    public String result(Model model,@PathParam("id") int id,@PathParam("email") String email, HttpServletRequest request, HttpServletResponse response){

        Group group = this.iGroupService.getById(id);

        //QueryWrapper<Item> queryWapper = new QueryWrapper<>();
        //queryWapper.lambda().eq(Item::getGroupId,id);
        //List<Item> itemList = this.iItemService.list(queryWapper);


        //QueryWrapper<Trigger> queryWapperTrigger = new QueryWrapper<>();
        //queryWapperTrigger.lambda().eq(Trigger::getGroupId,id);
        //List<Trigger> triggerList = this.iTriggerService.list(queryWapperTrigger);
        GroupResultBean groupResultBean = this.groupResultService.getGroupResultBean(group);
        model.addAttribute("group",group);
        //model.addAttribute("itemList",itemList);
        //model.addAttribute("triggerList",triggerList);
        model.addAttribute("groupResultBean",groupResultBean);
        if("1".equals(email)) {
            return "diTemplate/email";
        }
        return "group/result";
    }

}
