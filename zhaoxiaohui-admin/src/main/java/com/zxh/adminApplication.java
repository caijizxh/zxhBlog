package com.zxh;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zxh.mapper")
public class adminApplication {
    public static void main(String[] args) {
        SpringApplication.run(adminApplication.class,args);
    }
}
