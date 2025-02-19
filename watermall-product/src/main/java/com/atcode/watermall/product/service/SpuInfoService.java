package com.atcode.watermall.product.service;

import com.atcode.watermall.product.vo.SpuSaveVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atcode.common.utils.PageUtils;
import com.atcode.watermall.product.entity.SpuInfoEntity;

import java.util.Map;

/**
 * spu信息
 *
 * @author JiangCheng
 * @email JiangCheng@watermail.com
 * @date 2024-11-25 19:53:42
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSpuInfo(SpuSaveVo spuSaveVo);

    /**
     * 商品上架
     * @param spuId
     */
    void up(Long spuId);
}

