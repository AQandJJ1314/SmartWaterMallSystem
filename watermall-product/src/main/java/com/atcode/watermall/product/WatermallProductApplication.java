package com.atcode.watermall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.atcode.watermall.product.dao")
@SpringBootApplication
public class WatermallProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(WatermallProductApplication.class, args);
    }

}
