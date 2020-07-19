package com.yxifu.datainspection.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yxifu.datainspection.bean.ApiInterface;
import com.yxifu.datainspection.entity.Conn;
import com.yxifu.datainspection.entity.Item;
import com.yxifu.datainspection.service.IConnService;
import com.yxifu.datainspection.service.IItemService;
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
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yxifu
 * @since 2020-06-14
 */
@Controller
@RequestMapping("/item")
public class ItemController {

    @Autowired
    IItemService iItemService;

    @Autowired
    IConnService iConnService;

    @RequestMapping(value = "/edit",method = RequestMethod.GET)
    public String index(Model model,@PathParam("groupId") int groupId, @PathParam("id") int id, HttpServletRequest request, HttpServletResponse response){

        Item item = new Item();
        if(id==0){
            item.setGroupId(groupId);
            item.setId(id);
        } else {
            item = this.iItemService.getById(id);
        }

        QueryWrapper<Conn> queryWapperTrigger = new QueryWrapper<>();
        queryWapperTrigger.lambda().eq(Conn::getStatus,1);
        List<Conn> connList = this.iConnService.list(queryWapperTrigger);

        model.addAttribute("item",item);
        model.addAttribute("connList",connList);
        model.addAttribute("apiInterface",null);
        return "item/edit";
    }


    @ResponseBody
    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    public ApiInterface<Item> index(Model model, Item item
            , HttpServletRequest request, HttpServletResponse response){
        int id= item.getId();
        ApiInterface<Item> apiInterface = new ApiInterface<>();
        if(id>0) {
            item.setLastUpdateTime(Tools.formatDate(Tools.DateFormat_yyyyMMDDHHmmss));
            boolean b = this.iItemService.updateById(item);
            apiInterface.setData(item);
            if(!b){
                apiInterface.setError_code(500);
                apiInterface.setMessage("更新出错");
            }
        }
        else
        {
            int count = this.iItemService.getBaseMapper().insert(item);
            apiInterface.setData(item);
            if(count==0){
                apiInterface.setError_code(500);
                apiInterface.setMessage("添加出错");
            }
        }


        return apiInterface;
    }
}
