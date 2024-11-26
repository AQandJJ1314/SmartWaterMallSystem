package com.atcode.watermall.member.fegin;

import com.atcode.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 这是一个声明式的远程调用
 * 注解作用 : 告诉spring cloud这个接口是一个远程客户端，要调用coupon服务，再去调用coupon服务/coupon/coupon/member/list对应的方法
 */
@FeignClient("watermall-coupon")
public interface CouponFeginService {

    @RequestMapping("coupon/coupon/member/list")
    public R memberCoupns();
}
