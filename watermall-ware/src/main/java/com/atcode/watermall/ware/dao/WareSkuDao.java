package com.atcode.watermall.ware.dao;

import com.atcode.watermall.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品库存
 * 
 * @author JiangCheng
 * @email JiangCheng@watermail.com
 * @date 2024-11-26 13:39:03
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {
	
}
