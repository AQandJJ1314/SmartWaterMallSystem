package com.atcode.watermall.product.dao;

import com.atcode.watermall.product.entity.AttrEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品属性
 * 
 * @author JiangCheng
 * @email JiangCheng@watermail.com
 * @date 2024-11-25 19:53:42
 */
@Mapper
public interface AttrDao extends BaseMapper<AttrEntity> {
	
}
