package com.atcode.watermall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * JSR303
 * 1.给bean添加javax.validation.constraints.* ,并定义自己的message提示
 * 2.开启校验功能@Valid
 * 3.校验的Bean后紧跟一个BindingResult,就可以获取到校验的结果
 */

@MapperScan("com.atcode.watermall.product.dao")
@EnableDiscoveryClient
@SpringBootApplication
public class WatermallProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(WatermallProductApplication.class, args);
    }

}
