package com.atcode.watermall.member.fegin;

import com.atcode.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 这是一个声明式的远程调用
 */
@FeignClient("watermall-coupon")
public interface CouponFeginService {

    @RequestMapping("coupon/coupon/member/list")
    public R memberCoupns();
}
