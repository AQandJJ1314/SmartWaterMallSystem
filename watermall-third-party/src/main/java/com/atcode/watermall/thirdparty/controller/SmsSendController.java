package com.atcode.watermall.thirdparty.controller;

import com.atcode.common.utils.R;
import com.atcode.watermall.thirdparty.component.SmsComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 短信验证controller
 */
@RestController
@RequestMapping("/sms")
public class SmsSendController {
    @Autowired
    SmsComponent smsComponent;

    /**
     * 提供给别的服务进行调用的
     * @param phone
     * @param code
     * @return
     */
    @GetMapping("/sendCode")
    public R sendCode(@RequestParam("phone") String phone , @RequestParam("code") String code){
        smsComponent.sendSmsCode(phone,code);
        return R.ok();

    }
}
