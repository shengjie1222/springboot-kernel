package com.ethereal.kernel;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@MapperScan(basePackages = "com.ethereal.kernel.mapper")
@EnableTransactionManagement
@EnableCaching
public class KernelApplication {

    public static void main(String[] args) {
        SpringApplication.run(KernelApplication.class, args);
    }

    @Bean
    public ScheduledThreadPoolExecutor scheduledThreadPoolExecutor() {
        int cpuCore = Runtime.getRuntime().availableProcessors();
        ScheduledThreadPoolExecutor fileSearcherThread = new ScheduledThreadPoolExecutor(6 + cpuCore);
        fileSearcherThread.setMaximumPoolSize(120);
        fileSearcherThread.setKeepAliveTime(200, TimeUnit.SECONDS);
        return fileSearcherThread;
    }

    /**
     * 登录错误提示源文件
     * @return
     */
    @Bean
    public ReloadableResourceBundleMessageSource messageSource(){
        ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource = new ReloadableResourceBundleMessageSource();
        reloadableResourceBundleMessageSource.setBasename("classpath:messages");
        reloadableResourceBundleMessageSource.setDefaultEncoding("utf-8");
        return reloadableResourceBundleMessageSource;

    }

    /**
     * Spring Security 感知session销毁时需要的bean
     * @return
     */
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher(){
        return new HttpSessionEventPublisher();
    }
}
