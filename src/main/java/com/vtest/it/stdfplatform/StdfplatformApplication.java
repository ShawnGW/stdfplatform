package com.vtest.it.stdfplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@EnableCaching
@EnableAspectJAutoProxy
public class StdfplatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(StdfplatformApplication.class, args);
    }
}
