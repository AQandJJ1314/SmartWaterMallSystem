package com.atcode.watermall.ware.service;

import com.atcode.watermall.ware.VO.MergerVo;
import com.atcode.watermall.ware.VO.PurchaseDoneVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atcode.common.utils.PageUtils;
import com.atcode.watermall.ware.entity.PurchaseEntity;

import java.util.List;
import java.util.Map;

/**
 * 采购信息
 *
 * @author JiangCheng
 * @email JiangCheng@watermail.com
 * @date 2024-11-26 13:39:03
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPageUnreceive(Map<String, Object> params);

    void mergePurchase(MergerVo mergerVo);

    void received(List<Long> ids);

    void done(PurchaseDoneVo vo);

}

