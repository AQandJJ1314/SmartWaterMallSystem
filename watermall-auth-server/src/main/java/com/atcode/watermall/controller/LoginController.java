package com.atcode.watermall.controller;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.atcode.common.constant.AuthServerConstant;
import com.atcode.common.exception.BizCodeEnum;
import com.atcode.common.utils.R;
import com.atcode.common.vo.MemberResponseVo;
import com.atcode.watermall.fegin.MemberFeignService;
import com.atcode.watermall.fegin.ThirdPartFeignService;
import com.atcode.watermall.vo.UserLoginVo;
import com.atcode.watermall.vo.UserRegistVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Controller
public class LoginController {

    @Autowired
    ThirdPartFeignService thirdPartFeignService;

    @Autowired
    MemberFeignService memberFeignService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;
    /**
     * 用户名密码登录
     * 在gulimall-auth-server模块中的主体逻辑
     *
     * 通过会员服务远程调用登录接口
     * 如果调用成功，重定向至首页
     * 如果调用失败，则封装错误信息并携带错误信息重定向至登录页
     */
//    @PostMapping("/login")
//    public String login(UserLoginVo vo, RedirectAttributes redirectAttributes){
//        // 远程登录
//        R login = memberFeignService.login(vo);
//        if (login.getCode() == 0){
//            // 成功
//            return "redirect:http://watermall.com";
//        }else{
//            Map<String,String> errors = new HashMap<>();
//            errors.put("msg",login.getData("msg",new TypeReference<String>(){}));
//            redirectAttributes.addFlashAttribute("errors",errors);
//            return "redirect:http://auth.watermall.com/login.html";
//        }
//    }

    @RequestMapping("/login")
    public String login(UserLoginVo vo, RedirectAttributes attributes, HttpSession session){
        R r = memberFeignService.login(vo);
        if (r.getCode() == 0) {
            String jsonString = JSON.toJSONString(r.get("memberEntity"));
            MemberResponseVo memberResponseVo = JSON.parseObject(jsonString, new TypeReference<MemberResponseVo>() {
            });
            session.setAttribute(AuthServerConstant.LOGIN_USER, memberResponseVo);
            return "redirect:http://watermall.com/";
        }else {
            String msg = (String) r.get("msg");
            Map<String, String> errors = new HashMap<>();
            errors.put("msg", msg);
            attributes.addFlashAttribute("errors", errors);
            //TODO 后续加一个提示框，提示密码错误
            return "redirect:http://auth.watermall.com/login.html";
        }
    }

    @ResponseBody
    @GetMapping("/sms/sendcode")
    public R sendCode(@RequestParam("phone") String phone){
        // 1、接口防刷
        String prefixPhone = AuthServerConstant.SMS_CODE_CACHE_PREFIX + phone;
        String redisCode = stringRedisTemplate.opsForValue().get(prefixPhone);
        if (!StringUtils.isEmpty(redisCode)){
            long l = Long.parseLong(redisCode.split("_")[1]);
            if (System.currentTimeMillis() -l < 60000){
                // 60秒内不能再发
                return R.error(BizCodeEnum.SMS_CODE_EXCEPTION.getCode(),BizCodeEnum.SMS_CODE_EXCEPTION.getMsg());
            }
        }

        // 2、验证码的再次校验。redis 存key-phone, value-code   sms:code:18896736055 ->12345
        String code = String.valueOf((int)((Math.random() + 1) * 100000));
        // redis缓存验证码   防止同一个phone在60s内再次发送验证码  set(K var1, V var2, long var3, TimeUnit var5)
        stringRedisTemplate.opsForValue().set(prefixPhone,code + "_" + System.currentTimeMillis(),10, TimeUnit.MINUTES);
        thirdPartFeignService.sendCode(phone,code);
        return R.ok();
    }

    /**
     * http://watermall.com/
     * http://auth.watermall.com/regist
     * 重定向携带数据，利用session原理。将数据放在session中。只要跳到下一个页面，取出数据以后，session里面的数据就会删掉
     * RedirectAttributes redirectAttributes 模拟重定向携带数据
     * @param vo
     * @param result
     * @param redirectAttributes
     * @return
     */

    //TODO 这里加了一个 @RequestBody 注解之后可以使用postman测试，发送的数据可以被封装 被后端接收到  后续按需修改
    @PostMapping("/regist")
    public String regist(@Valid @RequestBody UserRegistVo vo, BindingResult result,
                         RedirectAttributes redirectAttributes){
        if (result.hasErrors()){
            /**
             * 方法一
             * Map<String, String> errors = result.getFieldErrors().stream().map(fieldError ->{
             *                 String field = fieldError.getField();
             *                 String defaultMessage = fieldError.getDefaultMessage();
             *                 errors.put(field,defaultMessage);
             *                 return errors;
             *             }).collect(Collector.asList());
             */
            // 方法二：
            // 1、如果校验不通过，则封装校验结果
            Map<String, String> errors = result.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            // 将错误信息封装到session中
            redirectAttributes.addFlashAttribute("errors",errors);
            /**
             * 使用 return "forward:/reg.html"; 会出现
             * 问题：Request method 'POST' not supported的问题
             * 原因：用户注册-> /regist[post] ------>转发/reg.html (路径映射默认都是get方式访问的)
             * 校验出错转发到注册页
             */
            //return "reg";    //转发会出现重复提交的问题，不要以转发的方式
            //使用重定向  解决重复提交的问题。但面临着数据不能携带的问题，就用RedirectAttributes
            return "redirect:http://auth.watermall.com/reg.html";
        }

        // 2、校验验证码
        String code = vo.getCode();
        String s = stringRedisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + vo.getPhone());
        if (!StringUtils.isEmpty(s)) {
            if (code.equals(s.split("_")[0])) {
                // 验证码通过,删除缓存中的验证码；令牌机制
                stringRedisTemplate.delete(AuthServerConstant.SMS_CODE_CACHE_PREFIX + vo.getPhone());
                // 真正注册调用远程服务注册
                R r = memberFeignService.regist(vo);
                if (r.getCode() == 0) {
                    //成功
                    return "redirect:http://auth.watermall.com/login.html";
                } else {
                    Map<String, String> errors = new HashMap<>();
                    errors.put("msg", r.getData(new TypeReference<String>() {
                    }));
                    redirectAttributes.addFlashAttribute("errors", errors);
                }
            } else {
                Map<String, String> errors = new HashMap<>();
                errors.put("code", "验证码错误");
                redirectAttributes.addFlashAttribute("errors", errors);
                return "redirect:http://auth.watermall.com/reg.html";
            }
        } else {
            Map<String, String> errors = new HashMap<>();
            errors.put("code", "验证码错误");
            redirectAttributes.addFlashAttribute("errors", errors);
            // 校验出错转发到注册页
            return "redirect:http://auth.watermall.com/reg.html";
        }

        // 注册成功回到登录页
        return "redirect:http://auth.watermall.com/login.html";
    }
}
