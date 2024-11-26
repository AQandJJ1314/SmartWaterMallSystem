package com.atcode.watermall.coupon.dao;

import com.atcode.watermall.coupon.entity.SeckillSessionEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 秒杀活动场次
 * 
 * @author JiangCheng
 * @email JiangCheng@watermail.com
 * @date 2024-11-26 10:30:57
 */
@Mapper
public interface SeckillSessionDao extends BaseMapper<SeckillSessionEntity> {
	
}
