package com.browser;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.TimeZone;

@EnableAsync
@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
@MapperScan("com.browser.dao.mapper")
public class WhiteCoinBrowserApplication {

    public static void main(String[] args) {
//        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
        SpringApplication.run(WhiteCoinBrowserApplication.class, args);
    }
}
