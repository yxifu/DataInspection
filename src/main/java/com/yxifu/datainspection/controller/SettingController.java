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
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

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

    @Autowired
    private LocaleResolver localeResolver;

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
        model.addAttribute("topNavBar","setting");
        model.addAttribute("setting",setting);
        model.addAttribute("apiInterface",null);
        return "setting/email";
    }


    @ResponseBody
    @RequestMapping(value = "/email",method = RequestMethod.POST)
    public ApiInterface<Setting> email(Model model, Setting setting
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
        return apiInterface;
    }


    @ResponseBody
    @RequestMapping(value = "/testEmail",method = RequestMethod.POST)
    public ApiInterface<Boolean> testEmail(Model model, EmailConfigBean emailConfigBean
            , HttpServletRequest request, HttpServletResponse response){
        return emailService.emailTest(emailConfigBean);
    }



    @RequestMapping(value = "/modifyPassword",method =RequestMethod.GET)
    public String modifyPassword(Model model, HttpServletRequest request, HttpServletResponse response){
        // 获取请求是从哪里来的
        String referer = request.getHeader("referer");
        ApiInterface<UserBean> apiInterface = new ApiInterface<>();
        UserBean userBean = new UserBean();
        userBean.setUserName("admin");
        apiInterface.setData(userBean);

        model.addAttribute("topNavBar","setting");
        model.addAttribute("userBean",userBean);
        model.addAttribute("referer",referer);
        return "setting/modifyPassword";
    }


    @ResponseBody
    @RequestMapping(value = "/modifyPassword",method =RequestMethod.POST)
    public ApiInterface<String> modifyPassword(UserBean userBean
            , HttpServletRequest request, HttpServletResponse response) {
        // 获取请求是从哪里来的
        String referer = request.getHeader("referer");
        ApiInterface<String> apiInterface = new ApiInterface<>();

        QueryWrapper<Setting> queryWapperSetting = new QueryWrapper<>();
        queryWapperSetting.lambda().eq(Setting::getCode, "admin_password").eq(Setting::getStatus, 1);
        Setting setting = this.iSettingService.getOne(queryWapperSetting);
        boolean checkDefaultPassword = true;
        if (setting != null && setting.getValue() != null && !"".equals(setting.getValue())) {
            checkDefaultPassword = false;
        }

        boolean loginSuccess = false;
        if (!checkDefaultPassword) {
            if ("admin".equals(userBean.getUserName()) &&
                    MD5Util.verify(userBean.getOldPassword(), setting.getValue())
            ) {
                loginSuccess = true;
            } else {
                apiInterface.setError_code(300);
                apiInterface.setMessage("账号和密码不正确！");
            }
        } else if ("admin".equals(userBean.getUserName()) && "admin".equals(userBean.getOldPassword())) {
            loginSuccess = true;
        } else {
            apiInterface.setError_code(300);
            apiInterface.setMessage("账号和密码不正确！");
        }
        if (apiInterface.getError_code() != 1) {
            if (loginSuccess) {
                if (setting == null) {
                    setting = new Setting();
                    setting.setCode("admin_password");

                    boolean getPW =false;
                    while (!getPW){
                        String  s1 = MD5Util.generate(userBean.getPassword());
                        if(MD5Util.verify(userBean.getPassword(),s1))
                        {
                            setting.setValue(s1);
                            getPW =true;
                        }
                    }
                    setting.setLastUpdateTime(Tools.formatDate(Tools.DateFormat_yyyyMMDDHHmmss));
                    setting.setStatus(1);
                    this.iSettingService.save(setting);
                } else {
                    setting.setValue(MD5Util.generate(userBean.getPassword()));
                    setting.setLastUpdateTime(Tools.formatDate(Tools.DateFormat_yyyyMMDDHHmmss));
                    this.iSettingService.updateById(setting);
                }
            }
        }
        return apiInterface;
    }

    @RequestMapping("/about")
    public String about(Model model, HttpServletRequest request, HttpServletResponse response){
        String projectHome = System.getProperty("project.home");
        model.addAttribute("topNavBar","setting");
        model.addAttribute("projectHome",projectHome);


        String mdPath = "classpath:templates/setting/about_en.md";
        if("zh".equals(localeResolver.resolveLocale(request).toString().substring(0,2))) {
            mdPath = "classpath:templates/setting/about_zh.md";
        }
        String mdContent ="";
        try {
            File file = ResourceUtils.getFile(mdPath);
            StringBuilder result = new StringBuilder();
            try{
                BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
                String s = null;
                while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                    result.append(System.lineSeparator()+s);
                }
                br.close();
            }catch(Exception e){
                e.printStackTrace();
            }
            mdContent = result.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mdContent = mdContent.replace("${project.home}",projectHome);
        model.addAttribute("mdContent",mdContent);
        return "setting/about";
    }
}
