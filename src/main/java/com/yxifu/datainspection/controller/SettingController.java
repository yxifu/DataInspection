package com.yxifu.datainspection.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yxifu.datainspection.bean.EmailConfigBean;
import com.yxifu.datainspection.bean.UserBean;
import com.yxifu.datainspection.entity.Setting;
import com.yxifu.datainspection.bean.ApiInterface;
import com.yxifu.datainspection.service.EmailService;
import com.yxifu.datainspection.service.ISettingService;
import com.yxifu.datainspection.util.ConstantUtils;
import com.yxifu.datainspection.util.MD5Util;
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
 * @author yxifu
 * @date 2020/05/24
 **/
@RequestMapping("setting")
@Controller
public class SettingController {


    @Autowired
    ISettingService iSettingService;

    @Autowired
    EmailService emailService;

    @RequestMapping({"","/"})
    public String index(Model model, HttpServletRequest request, HttpServletResponse response){
        model.addAttribute("topNavBar","setting");
        return "setting/index";
    }


    @RequestMapping(value = "/edit",method = RequestMethod.GET)
    public String index(Model model, @PathParam("id") String id,@PathParam("id") String code, HttpServletRequest request, HttpServletResponse response){

        Setting setting= new Setting();
        if(!"".equals(code))
        {
            QueryWrapper<Setting> queryWapper = new QueryWrapper<>();
            queryWapper.lambda().eq(Setting::getCode,code)
                    .eq(Setting::getStatus,1);
            setting = this.iSettingService.getOne(queryWapper);
        }
        else if("0".equals(id)){
            setting.setId(0);
            setting.setStatus(1);
        } else {
            setting = this.iSettingService.getById( Integer.parseInt(id));
        }
        model.addAttribute("setting",setting);
        model.addAttribute("apiInterface",null);
        return "setting/edit";
    }


    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    public String index(Model model, Setting setting
            , HttpServletRequest request, HttpServletResponse response){
        int id= setting.getId();
        ApiInterface<Setting> apiInterface = new ApiInterface<>();
        if(id>0) {
            setting.setLastUpdateTime(Tools.formatDate(Tools.DateFormat_yyyyMMDDHHmmss));
            boolean b = this.iSettingService.updateById(setting);
            apiInterface.setData(setting);
            if(!b){
                apiInterface.setError_code(500);
                apiInterface.setMessage("更新出错");
            }
        }
        else
        {
            int count = this.iSettingService.getBaseMapper().insert(setting);
            apiInterface.setData(setting);
            if(count==0){
                apiInterface.setError_code(500);
                apiInterface.setMessage("添加出错");
            }
        }
        model.addAttribute("setting",setting);
        model.addAttribute("apiInterface",apiInterface);
        return "setting/edit";
    }



    @RequestMapping(value = "/email",method = RequestMethod.GET)
    public String email(Model model,HttpServletRequest request, HttpServletResponse response){

        Setting setting= new Setting();

        QueryWrapper<Setting> queryWapper = new QueryWrapper<>();
        queryWapper.lambda().eq(Setting::getCode,"email_config")
                .eq(Setting::getStatus,1);
        setting = this.iSettingService.getOne(queryWapper);
        if(setting==null){
            setting.setId(0);
            setting.setStatus(1);
            setting.setCode("setting");
        }
        model.addAttribute("setting",setting);
        model.addAttribute("apiInterface",null);
        return "setting/email";
    }


    @RequestMapping(value = "/email",method = RequestMethod.POST)
    public String email(Model model, Setting setting
            , HttpServletRequest request, HttpServletResponse response){
        int id= setting.getId();
        ApiInterface<Setting> apiInterface = new ApiInterface<>();
        if(id>0) {
            setting.setLastUpdateTime(Tools.formatDate(Tools.DateFormat_yyyyMMDDHHmmss));
            boolean b = this.iSettingService.updateById(setting);
            apiInterface.setData(setting);
            if(!b){
                apiInterface.setError_code(500);
                apiInterface.setMessage("更新出错");
            }
        }
        else
        {
            int count = this.iSettingService.getBaseMapper().insert(setting);
            apiInterface.setData(setting);
            if(count==0){
                apiInterface.setError_code(500);
                apiInterface.setMessage("添加出错");
            }
        }
        model.addAttribute("setting",setting);
        model.addAttribute("apiInterface",apiInterface);
        return "setting/email";
    }


    @ResponseBody
    @RequestMapping(value = "/testEmail",method = RequestMethod.POST)
    public ApiInterface<Boolean> testEmail(Model model, EmailConfigBean emailConfigBean
            , HttpServletRequest request, HttpServletResponse response){
        return emailService.emailTest(emailConfigBean);
    }



    @RequestMapping(value = "/modifyPassword",method ={  RequestMethod.GET,RequestMethod.POST})
    public String modifyPassword(Model model, UserBean userBean
            , HttpServletRequest request, HttpServletResponse response){
        model.addAttribute("topNavBar","setting");
        // 获取请求是从哪里来的
        String referer = request.getHeader("referer");
        ApiInterface<UserBean> apiInterface = new ApiInterface<>();
        if(userBean.getUserName()==null && userBean.getPassword()==null){
            userBean.setUserName("admin");
            apiInterface.setError_code(1);
        } else {

            QueryWrapper<Setting> queryWapperSetting = new QueryWrapper<>();
            queryWapperSetting.lambda().eq(Setting::getCode,"admin_password").eq(Setting::getStatus,1);
            Setting setting = this.iSettingService.getOne(queryWapperSetting);
            boolean checkDefaultPassword =true;
            if(setting!=null && setting.getValue()!=null && !"".equals(setting.getValue())  ){
                checkDefaultPassword=false;
            }

            boolean loginSuccess =false;
            if(!checkDefaultPassword){
                if("admin".equals(userBean.getUserName()) &&
                        MD5Util.verify(userBean.getOldPassword(),setting.getValue())
                ){
                    loginSuccess=true;
                } else {
                    apiInterface.setError_code(300);
                    apiInterface.setMessage("账号和密码不正确！");
                }
            } else  if("admin".equals(userBean.getUserName()) && "admin".equals(userBean.getOldPassword())){
                loginSuccess=true;
            } else {
                apiInterface.setError_code(300);
                apiInterface.setMessage("账号和密码不正确！");
            }
            if(apiInterface.getError_code()!=1){
                if(loginSuccess)
                {
                    if(setting==null){
                        setting = new Setting();
                        setting.setCode("admin_password");
                        setting.setValue(MD5Util.generate(userBean.getPassword()));
                        setting.setLastUpdateTime(Tools.formatDate(Tools.DateFormat_yyyyMMDDHHmmss));
                        setting.setStatus(1);
                        this.iSettingService.save(setting);
                    } else
                    {
                        setting.setValue(MD5Util.generate(userBean.getPassword()));
                        setting.setLastUpdateTime(Tools.formatDate(Tools.DateFormat_yyyyMMDDHHmmss));
                        this.iSettingService.updateById(setting);
                    }
                }
            }
        }
        apiInterface.setData(userBean);

        model.addAttribute("userBean",userBean);
        model.addAttribute("apiInterface",apiInterface);
        return "setting/modifyPassword";
    }
}
