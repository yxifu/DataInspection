package com.yxifu.datainspection.controller;

import com.yxifu.datainspection.bean.ApiInterface;
import com.yxifu.datainspection.bean.UserBean;
import com.yxifu.datainspection.entity.Setting;
import com.yxifu.datainspection.util.ConstantUtils;
import com.yxifu.datainspection.util.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.LocaleResolver;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * @author yxifu
 * @date 2020/04/11
 **/
@Controller
public class HomeController {


    @Autowired
    private LocaleResolver localeResolver;

    @RequestMapping("/")
    public String index(Model model, HttpServletRequest request, HttpServletResponse response){

        model.addAttribute("name","123456");
        model.addAttribute("topNavBar","home");
        System.out.println(request.getLocale());
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

    @RequestMapping(value = "/login",method ={  RequestMethod.GET,RequestMethod.POST})
    public String login(Model model, UserBean userBean
            , HttpServletRequest request, HttpServletResponse response){

        // 获取请求是从哪里来的
        String referer = request.getHeader("referer");
        ApiInterface<UserBean> apiInterface = new ApiInterface<>();
        if(userBean.getUserName()==null && userBean.getPassword()==null){
            userBean.setUserName("admin");
        } else {
            if("admin".equals(userBean.getUserName()) && "admin".equals(userBean.getPassword())){
                userBean.setPassword("");
                request.getSession().setAttribute(ConstantUtils.USER_SESSION_KEY,userBean);
                return "redirect:/";
            } else {
                apiInterface.setError_code(300);
                apiInterface.setMessage("账号和密码不正确！");
            }
        }
        apiInterface.setData(userBean);

        model.addAttribute("userBean",userBean);
        model.addAttribute("apiInterface",apiInterface);
        return "home/login";
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
        System.out.println(request.getLocale());
        //return "切换成中文";
        return "redirect:/";
    }


    @RequestMapping("/en")
    public String en(Model model, HttpServletRequest request, HttpServletResponse response){
        localeResolver.setLocale(request,response, Locale.ENGLISH);
        System.out.println(request.getLocale());
        //return "index";
        return "redirect:/";
    }
}
