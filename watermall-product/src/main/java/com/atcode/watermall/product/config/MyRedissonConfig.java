package com.atcode.watermall.product.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyRedissonConfig {
    /**
     * 所有对Redisson的操作都是通过RedissonClient对象
     * @return
     */
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient(){
        //1.创建配置
        Config config = new Config();
        //设置单节点模式，设置redis地址。ssl安全连接redission://127.0.0.1:6379
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        //2.根据Config创建出RedissonClient实例
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }

}
