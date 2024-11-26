package com.atcode.watermall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atcode.common.utils.PageUtils;
import com.atcode.watermall.coupon.entity.SeckillSkuRelationEntity;

import java.util.Map;

/**
 * 秒杀活动商品关联
 *
 * @author JiangCheng
 * @email JiangCheng@watermail.com
 * @date 2024-11-26 10:30:57
 */
public interface SeckillSkuRelationService extends IService<SeckillSkuRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

