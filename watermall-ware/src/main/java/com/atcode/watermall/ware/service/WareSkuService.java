package com.atcode.watermall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atcode.common.utils.PageUtils;
import com.atcode.watermall.ware.entity.WareSkuEntity;

import java.util.Map;

/**
 * 商品库存
 *
 * @author JiangCheng
 * @email JiangCheng@watermail.com
 * @date 2024-11-26 13:39:03
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

