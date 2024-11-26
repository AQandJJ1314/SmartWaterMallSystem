package com.atcode.watermall.member;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.atcode.watermall.member.dao")
@SpringBootApplication
public class WatermallMemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(WatermallMemberApplication.class, args);
    }

}
