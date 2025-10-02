package com.ksanaDock;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@MapperScan("com.ksanaDock.dao")
@EnableAspectJAutoProxy
public class KsanaDockApplication {
    public static void main(String[] args) {
        SpringApplication.run(KsanaDockApplication.class, args);
    }
} 