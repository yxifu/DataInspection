package com.yxifu.datainspection.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.yxifu.datainspection.bean.ApiInterface;
import com.yxifu.datainspection.bean.UserBean;
import com.yxifu.datainspection.entity.Setting;
import com.yxifu.datainspection.entity.Trigger;
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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Locale;

/**
 * @author yxifu
 * @date 2020/04/11
 **/
@Controller
public class HomeController {


    @Autowired
    private LocaleResolver localeResolver;

    @Autowired
    ISettingService iSettingService;

    @RequestMapping("/")
    public String index(Model model, HttpServletRequest request, HttpServletResponse response){

        model.addAttribute("name","123456");
        model.addAttribute("topNavBar","home");
        model.addAttribute("locale",request.getLocale());
        System.out.println(request.getLocale());

        String mdPath ="";
        String mdContent ="";

        if("zh".equals(localeResolver.resolveLocale(request).toString().substring(0,2))) {
            mdPath = "classpath:templates/home/home_zh.md";
        } else// if(request.getLocale().toString().substring(0,1)=="en") {
        {
            mdPath = "classpath:templates/home/home_en.md";
        }
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
        model.addAttribute("mdContent",mdContent);
        return "home/index";
    }
/*
    @RequestMapping("/login")
    public String login(Model model, HttpServletRequest request, HttpServletResponse response){

        UserBean userBean = new UserBean();
        model.addAttribute("userBean",userBean);
        System.out.println(request.getLocale());
        return "home/login";
    }*/

    @RequestMapping(value = "/login",method =RequestMethod.GET)
    public String login(Model model, HttpServletRequest request, HttpServletResponse response){
        // 获取请求是从哪里来的
        String referer = request.getHeader("referer");
        UserBean userBean = new UserBean();
        userBean.setUserName("admin");
        model.addAttribute("userBean",userBean);
        model.addAttribute("referer",referer);
        return "home/login";
    }


    @ResponseBody
    @RequestMapping(value = "/login",method =RequestMethod.POST)
    public ApiInterface<String> login(UserBean userBean
            , HttpServletRequest request, HttpServletResponse response){

        // 获取请求是从哪里来的
        String referer = request.getHeader("referer");
        ApiInterface<String> apiInterface = new ApiInterface<>();
        apiInterface.setData("");

        QueryWrapper<Setting> queryWapperSetting = new QueryWrapper<>();
        queryWapperSetting.lambda().eq(Setting::getCode,"admin_password").eq(Setting::getStatus,1);
        Setting setting = this.iSettingService.getOne(queryWapperSetting);
        boolean checkDefaultPassword =true;
        if(setting!=null && setting.getValue()!=null && !"".equals(setting.getValue())  ){
            checkDefaultPassword=false;
        }

        if(!checkDefaultPassword){
            if("admin".equals(userBean.getUserName()) &&
                    MD5Util.verify(userBean.getPassword(),setting.getValue())
            ){
                userBean.setPassword("");
                request.getSession().setAttribute(ConstantUtils.USER_SESSION_KEY,userBean);
                //return "redirect:/";
                //apiInterface.setData(Tools.notNullOrEmpty(referer)?referer: "/");
            } else {
                apiInterface.setError_code(300);
                apiInterface.setMessage("账号和密码不正确！");
            }
        } else  if("admin".equals(userBean.getUserName()) && "admin".equals(userBean.getPassword())){
            userBean.setPassword("");
            request.getSession().setAttribute(ConstantUtils.USER_SESSION_KEY,userBean);
            apiInterface.setData("/setting/modifyPassword");
        } else {
            apiInterface.setError_code(300);
            apiInterface.setMessage("账号和密码不正确！");
        }
        return apiInterface;
    }



    @RequestMapping("/logout")
    public String logout(Model model, HttpServletRequest request, HttpServletResponse response){

        try {
            request.getSession().removeAttribute(ConstantUtils.USER_SESSION_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/login";
    }
    @RequestMapping("/zh")
    public String zh(Model model, HttpServletRequest request, HttpServletResponse response){
        localeResolver.setLocale(request,response, Locale.CHINA);
        if(request.getHeader("referer")!=null && !"".equals(request.getHeader("referer")) ){
            return "redirect:"+request.getHeader("referer");
        } else {
            //return "index";
            return "redirect:/";
        }
    }


    @RequestMapping("/en")
    public String en(Model model, HttpServletRequest request, HttpServletResponse response){
        localeResolver.setLocale(request,response, Locale.ENGLISH);
        //System.out.println(request.getLocale());

        if(request.getHeader("referer")!=null && !"".equals(request.getHeader("referer")) ){
            return "redirect:"+request.getHeader("referer");
        } else {
            //return "index";
            return "redirect:/";
        }
    }
}
