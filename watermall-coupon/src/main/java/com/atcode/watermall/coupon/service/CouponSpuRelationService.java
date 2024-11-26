package com.atcode.watermall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atcode.common.utils.PageUtils;
import com.atcode.watermall.coupon.entity.CouponSpuRelationEntity;

import java.util.Map;

/**
 * 优惠券与产品关联
 *
 * @author JiangCheng
 * @email JiangCheng@watermail.com
 * @date 2024-11-26 10:30:57
 */
public interface CouponSpuRelationService extends IService<CouponSpuRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

