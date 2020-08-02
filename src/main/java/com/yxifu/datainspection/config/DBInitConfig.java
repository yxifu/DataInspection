package com.yxifu.datainspection.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * @author yxifu
 * @date 2020/07/12
 **/
@Slf4j
@Configuration
public class DBInitConfig {

    @PostConstruct
    public void initDB(){
        String projectHome = System.getProperty("project.home");
        String dbPath = projectHome+ "\\DataInspection.sqlite3";

        File dbFile = new File(dbPath);
        if(dbFile.exists()){
            log.info("The database file already exists："+dbPath);
        } else {
            log.info("The database file does not exist：" + dbPath);
            log.info("Start copying database files from template.");

            try {
                File file1 = ResourceUtils.getFile("classpath:templates/DataInspection_template.sqlite3");
                if (file1.exists()) {
                    Files.copy(file1.toPath(), dbFile.toPath());
                }
/*
//获取文件的相对路径  可在控制台打印查看输出结果
            String filePath = ResourceUtils.getFile("classpath:templates/DataInspection_template.sqlite3").getPath();

//直接将目标文件读成inputstream  this指当前类的实例对象
            InputStream ins = this.getClass().getClassLoader().getResourceAsStream("templates/DataInspection_template.sqlite3");
            File file2 = new File(String.valueOf(ins));
 */
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                log.error("check the database file error",e);
            } catch (IOException e) {
                e.printStackTrace();
                log.error("copying the database file error",e);
            }
            log.info("End copying the database file from the template.");
        }
    }
}
