package com.yxifu.datainspection.service;

import com.yxifu.datainspection.bean.ApiInterface;
import com.yxifu.datainspection.bean.ItemResultBean;
import com.yxifu.datainspection.entity.Conn;
import com.yxifu.datainspection.entity.Item;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 * @author yxifu
 * @date 2020/06/14
 **/
@Slf4j
@Service
public class DBService {

    public ApiInterface<Boolean> testConn(Conn conn)
    {
        ApiInterface<Boolean> api = new ApiInterface<>();
        boolean result =false;
        try {
            HikariConfig dataSourceConfig = new HikariConfig();
            dataSourceConfig.setDriverClassName(conn.getType());
            dataSourceConfig.setJdbcUrl(conn.getConnString());
            dataSourceConfig.setUsername(conn.getUsername());
            dataSourceConfig.setPassword(conn.getPassword());
            //dataSourceConfig.setConnectionTestQuery("SELECT name FROM sqlite_master WHERE type='table' ORDER BY name");
            if("com.mysql.jdbc.Driver".equals(conn.getType())
            || "org.postgresql.Driver".equals(conn.getType())){
                dataSourceConfig.setConnectionTestQuery("SELECT NOW() d");
            }else if("org.sqlite.JDBC".equals(conn.getType())) {
                dataSourceConfig.setConnectionTestQuery("SELECT DATE() d");
            }
            HikariDataSource hikariDataSource = new HikariDataSource(dataSourceConfig);
            if("org.sqlite.JDBC".equals(conn.getType())) {
                List<Map<String, Object>> list = new JdbcTemplate(hikariDataSource).queryForList("SELECT name FROM sqlite_master WHERE type='table' ORDER BY name");
                //System.out.println("==========");
                //System.out.println(hikariDataSource.getJdbcUrl());
                if (list.size() > 0) {
                    //System.out.println("查询返回" + list.size() + "行");
                    result = true;
                }
                else {
                    result = false;
                }
            }

            hikariDataSource.close();
            result = true;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            api.setData(false);
            api.setMessage(ex.toString());
        }
        api.setData(result);
        return api;
    }


    public ItemResultBean getItemResultBean(Conn conn, Item item)
    {
        ItemResultBean itemResultBean = new ItemResultBean();
        itemResultBean.setConn(conn);
        itemResultBean.setItem(item);
        try {
            HikariConfig dataSourceConfig = new HikariConfig();
            dataSourceConfig.setDriverClassName(conn.getType());
            dataSourceConfig.setJdbcUrl(conn.getConnString());
            dataSourceConfig.setUsername(conn.getUsername());
            dataSourceConfig.setPassword(conn.getPassword());
            //dataSourceConfig.setConnectionTestQuery("SELECT name FROM sqlite_master WHERE type='table' ORDER BY name");
            dataSourceConfig.setConnectionTestQuery(item.getSql());
            HikariDataSource hikariDataSource = new HikariDataSource(dataSourceConfig);
            try {
                List<Map<String, Object>> list = new JdbcTemplate(hikariDataSource).queryForList(item.getSql());
                itemResultBean.setData(list);
                // return list;
            }catch (Exception ex){
                log.error("getData失败",ex);
                itemResultBean.setException(ex);
            }
            finally {
                hikariDataSource.close();
            }

        }
        catch (Exception ex)
        {
            log.error("getData失败",ex);
            itemResultBean.setException(ex);
        }
        return itemResultBean;
    }
}
