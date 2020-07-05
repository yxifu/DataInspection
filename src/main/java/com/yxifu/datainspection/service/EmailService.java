package com.yxifu.datainspection.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.yxifu.datainspection.bean.ApiInterface;
import com.yxifu.datainspection.bean.EmailConfigBean;
import com.yxifu.datainspection.bean.GroupResultBean;
import com.yxifu.datainspection.entity.Group;
import com.yxifu.datainspection.entity.Setting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * @author yxifu
 * @date 2020/06/25
 **/
@Slf4j
@Service
public class EmailService {

    public static EmailConfigBean emailConfigBean = null;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    ISettingService iSettingService;

    public EmailConfigBean getOne(){
        if(emailConfigBean==null){
            Setting setting= new Setting();

            QueryWrapper<Setting> queryWapper = new QueryWrapper<>();
            queryWapper.lambda().eq(Setting::getCode,"email_config")
                    .eq(Setting::getStatus,1);
            setting = this.iSettingService.getOne(queryWapper);
            if(setting==null){
                log.error("获取邮件配置异常,未找到配置");
                return null;
            }
            try{
                Gson gson = new Gson();
                emailConfigBean = gson.fromJson(setting.getValue(),EmailConfigBean.class);

            }catch (Exception ex){
                log.error("获取邮件配置异常",ex);
            }
        }
        return emailConfigBean;
    }


    public void SendByExecuteResultBean(Group group, GroupResultBean groupResultBean){
        log.info("发送邮件");
        Context context = new Context();
        context.setVariable("group",group);
        context.setVariable("groupResultBean",groupResultBean);
        String html = templateEngine.process("diTemplate/email",context);
        log.info(html);
        sendMail("[DI]"+group.getName()
                ,group.getEmails().split(",|;")
                ,html);
    }


    public void sendMail(String title, String[] emails, String content) {
        try {
            EmailConfigBean emailConfigBean = getOne();
            if(emailConfigBean==null){
                return;
            }
            Properties prop = new Properties();
            //String EMAIL_OWNER_ADDR = "yxifu2@yxifu.com";
            //prop.put("mail.host", "smtp.exmail.qq.com");

            // SMTP 服务器端口号，可以从 QQ 邮箱的帮助文档查到端口为 465 或 587
            //prop.put("mail.smtp.port", 465);
            //prop.put("mail.transport.protocol", "smtp");
            String EMAIL_OWNER_ADDR = emailConfigBean.getEmail();
            prop.put("mail.host", emailConfigBean.getMailHost());

            // SMTP 服务器端口号，可以从 QQ 邮箱的帮助文档查到端口为 465 或 587
            prop.put("mail.smtp.port", Integer.valueOf(emailConfigBean.getMailSmtpPort()));
            prop.put("mail.transport.protocol", emailConfigBean.getMailTransportProtocol());
            prop.put("mail.smtp.auth", "true");
            //如果不加下面的这行代码 windows下正常，linux环境下发送失败，解决：http://www.cnblogs.com/Harold-Hua/p/7029117.html
            prop.put("mail.smtp.ssl.enable", "true");
            // 是否输出控制台信息
            prop.put("mail.debug", "true");
            //使用java发送邮件5步骤
            //1.创建sesssion
            Session session = Session.getInstance(prop);
            //开启session的调试模式，可以查看当前邮件发送状态
            //session.setDebug(true);

            //2.通过session获取Transport对象（发送邮件的核心API）
            Transport ts = session.getTransport();
            //3.通过邮件用户名密码链接，阿里云默认是开启个人邮箱pop3、smtp协议的，所以无需在阿里云邮箱里设置
            //ts.connect(EMAIL_OWNER_ADDR, "geqhttg@467");
            ts.connect(EMAIL_OWNER_ADDR, emailConfigBean.getPassword());

            //4.创建邮件
            //创建邮件对象
            MimeMessage mm = new MimeMessage(session);
            //设置发件人
            mm.setFrom(new InternetAddress(EMAIL_OWNER_ADDR));
            //设置收件人
            for (String toEmail : emails) {
                mm.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            }
            //设置抄送人
            //mm.setRecipient(Message.RecipientType.CC, new InternetAddress("XXXX@qq.com"));

            //mm.setSubject("吸引力注册邮件");
            mm.setSubject(title);

            //mm.setContent("您的注册验证码为:<b style=\"color:blue;\">0123</b>", "text/html;charset=utf-8");
            mm.setContent(content, "text/html;charset=utf-8");

            // true表示开始附件模式 -----------------------------------------------------------------------
        /*MimeMessageHelper messageHelper = new MimeMessageHelper(mm, true, "utf-8");
        // 设置收件人，寄件人
        messageHelper.setTo(email);
        messageHelper.setFrom(EMAIL_OWNER_ADDR);
        messageHelper.setSubject(title);
        // true 表示启动HTML格式的邮件
        messageHelper.setText(content, true);

        FileSystemResource file1 = new FileSystemResource(new File("d:/rongke.log"));
        FileSystemResource file2 = new FileSystemResource(new File("d:/新建文本文档.txt"));
        // 添加2个附件
        messageHelper.addAttachment("rongke.log", file1);
        try {
            //附件名有中文可能出现乱码
            messageHelper.addAttachment(MimeUtility.encodeWord("新建文本文档.txt"), file2);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new MessagingException();
        }*/
            //-------------------------------------------------------------------------------------------
            //5.发送电子邮件

            ts.sendMessage(mm, mm.getAllRecipients());
        }catch (Exception ex)
        {

        }
    }


    public ApiInterface<Boolean> emailTest(EmailConfigBean emailConfigBean) {
        ApiInterface<Boolean> result = new ApiInterface<Boolean>();
        try {
            Properties prop = new Properties();
            String EMAIL_OWNER_ADDR = emailConfigBean.getEmail();
            prop.put("mail.host", emailConfigBean.getMailHost());

            // SMTP 服务器端口号，可以从 QQ 邮箱的帮助文档查到端口为 465 或 587
            prop.put("mail.smtp.port", Integer.valueOf(emailConfigBean.getMailSmtpPort()));
            prop.put("mail.transport.protocol", emailConfigBean.getMailTransportProtocol());
            prop.put("mail.smtp.auth", "true");
            //如果不加下面的这行代码 windows下正常，linux环境下发送失败，解决：http://www.cnblogs.com/Harold-Hua/p/7029117.html
            prop.put("mail.smtp.ssl.enable", "true");
            // 是否输出控制台信息
            prop.put("mail.debug", "true");
            //使用java发送邮件5步骤
            //1.创建sesssion
            Session session = Session.getInstance(prop);
            Transport ts = session.getTransport();
            //3.通过邮件用户名密码链接，阿里云默认是开启个人邮箱pop3、smtp协议的，所以无需在阿里云邮箱里设置
            ts.connect(EMAIL_OWNER_ADDR, emailConfigBean.getPassword());
            result.setData(true);
        } catch (Exception ex) {
            result.setError_code(500);
            result.setData(false);
            result.setMessage(ex.getMessage());
        }
        return result;
    }
}
