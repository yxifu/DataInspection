package com.yxifu.datainspection.controller;


import com.yxifu.datainspection.bean.ApiInterface;
import com.yxifu.datainspection.entity.Conn;
import com.yxifu.datainspection.service.DBService;
import com.yxifu.datainspection.service.IConnService;
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
 * @since 2020-04-11
 */
@Controller
@RequestMapping("conn")
public class ConnController {

    @Autowired
    IConnService iConnService;

    @Autowired
    DBService dbService;

    @RequestMapping("/list")
    public String index(Model model, HttpServletRequest request, HttpServletResponse response){

        List<Conn> connlist = this.iConnService.list();
        model.addAttribute("connlist",connlist);
        model.addAttribute("topNavBar","setting");
        System.out.println(request.getLocale());
        return "conn/list";
    }


    @RequestMapping(value = "/edit",method = RequestMethod.GET)
    public String index(Model model, @PathParam("id") int id, HttpServletRequest request, HttpServletResponse response){

        Conn conn= new Conn();
        if(id==0){
            conn.setConnId(0);
        } else {
            conn = this.iConnService.getById(id);
        }

        List<Conn> connslist = this.iConnService.list();
        model.addAttribute("conn",conn);
        model.addAttribute("apiInterface",null);
        //System.out.println(request.getLocale());
        return "conn/edit";
    }


    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    public String index(Model model, Conn conn
            , HttpServletRequest request, HttpServletResponse response){
        int id= conn.getConnId();
        ApiInterface<Conn> apiInterface = new ApiInterface<>();
        if(id>0) {
            conn.setLastUpdateTime(Tools.formatDate(Tools.DateFormat_yyyyMMDDHHmmss));
            boolean b = this.iConnService.updateById(conn);
            //model.addAttribute("ispost",true);
            apiInterface.setData(conn);
            if(!b){
                apiInterface.setError_code(500);
                apiInterface.setMessage("更新出错");
            }
        }
        else
        {
            //conn.setStatus(1);
            //conn.setCreateTime(new Date());
            int count = this.iConnService.getBaseMapper().insert(conn);
            apiInterface.setData(conn);
            if(count==0){
                apiInterface.setError_code(500);
                apiInterface.setMessage("添加出错");
            }
        }


        //List<Conn> connslist = this.iConnService.list();
        model.addAttribute("conn",conn);
        model.addAttribute("apiInterface",apiInterface);
        //System.out.println(request.getLocale());
        return "conn/edit";
    }


    @RequestMapping(value = "/testConn",method = RequestMethod.POST)
    @ResponseBody
    public ApiInterface<Boolean> testConn(Model model, Conn conn
            , HttpServletRequest request, HttpServletResponse response){
       return this.dbService.testConn(conn);
    }


}
