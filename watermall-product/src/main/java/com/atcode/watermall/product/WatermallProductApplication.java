package com.atcode.watermall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * JSR303
 * 1.给bean添加javax.validation.constraints.* ,并定义自己的message提示
 * 2.开启校验功能@Valid
 * 3.校验的Bean后紧跟一个BindingResult,就可以获取到校验的结果
 */

@MapperScan("com.atcode.watermall.product.dao")

@EnableFeignClients(basePackages = "com.atcode.watermall.product.feign")
@EnableCaching
@EnableDiscoveryClient
@SpringBootApplication
public class WatermallProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(WatermallProductApplication.class, args);
        System.err.println("商品模块已启动");

    }

}
