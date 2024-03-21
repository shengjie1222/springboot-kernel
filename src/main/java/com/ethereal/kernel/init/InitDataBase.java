package com.ethereal.kernel.init;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Configuration;
import java.sql.*;

/**
 * 初始化java数据库
 *
 * @author mind
 * @date 2020/8/3 13:45
 */
@Configuration
@Slf4j
public class InitDataBase  implements ApplicationRunner {

    @Autowired
    private DataSourceProperties dataSourceProperties;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String url = dataSourceProperties.getUrl();
        String username = dataSourceProperties.getUsername();
        String password = dataSourceProperties.getPassword();
        // 如果想要初始化后创建数据库，那么一开始在此处初始化的时候需要连接一个已经存在的数据,这儿设置了默认的肯定存在的数据库。
        try(Connection conn = DriverManager.getConnection(url, username, password);){
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery("show tables");
            //8张表
            result.last();
            if(result.getRow() == 4){
                log.info("数据库完整，跳过初始化sql脚本........");
                return;
            }
            log.info("正在初始化sql脚本........");
            ScriptRunner runner = new ScriptRunner(conn);
            runner.setErrorLogWriter(null);
            runner.setLogWriter(null);
            // 执行SQL脚本
            runner.runScript(Resources.getResourceAsReader("database/schema.sql"));
            runner.runScript(Resources.getResourceAsReader("database/data.sql"));
            log.info("数据库初始化完成!");
        }catch (Exception e){
            log.error("init sql failed!",e);
        }
    }
}
