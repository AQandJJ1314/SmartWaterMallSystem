package com.atcode.watermall.member;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


/**
 * 想要远程调用别的服务
 * 1.引入open-feign
 * 2.编写一个接口告诉springcloud这个接口需要调用远程服务 统一放在fegin包下
 *    声明接口的每一个方法都是调用哪个远程服务的哪个请求
 * 3.开启远程调用的功能  @EnableFeignClients
 */
@MapperScan("com.atcode.watermall.member.dao")
@EnableFeignClients(basePackages = "com.atcode.watermall.member.fegin")
@EnableDiscoveryClient
@SpringBootApplication
public class WatermallMemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(WatermallMemberApplication.class, args);
    }

}
