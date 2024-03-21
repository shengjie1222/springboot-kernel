package com.ethereal.kernel.init;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.List;

/**
 * @author Jie Jie
 * date 2024-03-21
 */
@Configuration
public class AutoOpenBrowser implements ApplicationRunner {

    @Value("${server.port}")
    private int port;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        //输入启动命令 --browser=enable
        if(args.containsOption("browser")){
            List<String> browserValues = args.getOptionValues("browser");
            if(browserValues.contains("disable")){
                return;
            }
        }
        try {
            Runtime.getRuntime().exec(String.format("cmd  /c  start http://127.0.0.1:%d",port));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
