package com.atcode.watermall.thirdparty;

import com.aliyun.oss.OSSClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@SpringBootTest
class WatermallThirdPartyApplicationTests {

    @Resource
    OSSClient ossClient;

    @Test
    void contextLoads() throws FileNotFoundException {

        FileInputStream inputStream = new FileInputStream("C:\\work\\开发\\逆向项目\\gulimall-learning-master\\gulimall-product\\src\\main\\resources\\static\\index\\img\\5a0bb29bN598fefa4.jpg!q90.webp.jpg");
        ossClient.putObject("watermall-png","903d28ab0e2f4bb59bdf8f7c6fd22fe4.jpg",inputStream);
        ossClient.shutdown();
        System.out.println("上传完成");
    }

}
