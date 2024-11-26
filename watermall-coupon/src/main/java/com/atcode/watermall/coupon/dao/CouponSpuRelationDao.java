package com.atcode.watermall.coupon.dao;

import com.atcode.watermall.coupon.entity.CouponSpuRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券与产品关联
 * 
 * @author JiangCheng
 * @email JiangCheng@watermail.com
 * @date 2024-11-26 10:30:57
 */
@Mapper
public interface CouponSpuRelationDao extends BaseMapper<CouponSpuRelationEntity> {
	
}
