package com.atcode.watermall.fegin;

import com.atcode.common.utils.R;
import com.atcode.watermall.vo.UserLoginVo;
import com.atcode.watermall.vo.UserRegistVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("watermall-member")
public interface MemberFeignService {
    /**
     * 通过watermall-member会员服务注册逻辑
     *
     * 通过异常机制判断当前注册会员名和电话号码是否已经注册，如果已经注册，则抛出对应的自定义异常，并在返回时封装对应的错误信息
     * 如果没有注册，则封装传递过来的会员信息，并设置默认的会员等级、创建时间
     * 修改“com.atguigu.watermall.member.controller.MemberController” 类，代码如下
     */
    @PostMapping("/member/member/regist")
    public R regist(@RequestBody UserRegistVo vo);

    @PostMapping("/member/member/login")
    public R login(@RequestBody UserLoginVo vo);

}
