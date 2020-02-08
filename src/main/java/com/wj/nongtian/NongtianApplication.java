package com.wj.nongtian;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.wj.nongtian.mapper")
public class NongtianApplication {

    public static void main(String[] args) {
        SpringApplication.run(NongtianApplication.class, args);
    }

}
