package com.yxifu.datainspection.bean;

import lombok.Data;

/**
 * @author yxifu
 * @date 2020/07/04
 **/
@Data
public class EmailConfigBean {
    private String mailTransportProtocol;
    private String mailHost;
    private String mailSmtpPort;
    private String email;
    private String password;
}
