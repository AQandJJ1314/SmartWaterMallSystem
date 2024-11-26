package com.atcode.watermall.order.dao;

import com.atcode.watermall.order.entity.OrderItemEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单项信息
 * 
 * @author JiangCheng
 * @email JiangCheng@watermail.com
 * @date 2024-11-26 13:36:56
 */
@Mapper
public interface OrderItemDao extends BaseMapper<OrderItemEntity> {
	
}
