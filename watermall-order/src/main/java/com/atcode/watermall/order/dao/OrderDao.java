package com.atcode.watermall.order.dao;

import com.atcode.watermall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author JiangCheng
 * @email JiangCheng@watermail.com
 * @date 2024-11-26 13:36:56
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
