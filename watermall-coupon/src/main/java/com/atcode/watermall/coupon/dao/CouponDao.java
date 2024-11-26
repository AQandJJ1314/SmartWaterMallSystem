package com.atcode.watermall.coupon.dao;

import com.atcode.watermall.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author JiangCheng
 * @email JiangCheng@watermail.com
 * @date 2024-11-26 10:30:57
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
