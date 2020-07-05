package com.yxifu.datainspection.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

/**
 * @author yxifu
 * @date 2020/04/18
 **/
@Configuration
public class LocaleResolverConfig {
    @Bean(name="localeResolver")
    public LocaleResolver localeResolver() {
        //return new SessionLocaleResolver();
        return new CookieLocaleResolver();
    }
}