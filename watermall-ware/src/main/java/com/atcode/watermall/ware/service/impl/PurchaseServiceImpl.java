package com.atcode.watermall.ware.service.impl;

import com.atcode.common.constant.WareConstant;
import com.atcode.common.utils.R;
import com.atcode.watermall.ware.VO.MergerVo;
import com.atcode.watermall.ware.VO.PurchaseDoneVo;
import com.atcode.watermall.ware.VO.PurchaseItemDoneVo;
import com.atcode.watermall.ware.entity.PurchaseDetailEntity;
import com.atcode.watermall.ware.feign.ProductFeignService;
import com.atcode.watermall.ware.service.PurchaseDetailService;
import com.atcode.watermall.ware.service.WareSkuService;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atcode.common.utils.PageUtils;
import com.atcode.common.utils.Query;

import com.atcode.watermall.ware.dao.PurchaseDao;
import com.atcode.watermall.ware.entity.PurchaseEntity;
import com.atcode.watermall.ware.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {


    @Autowired
    private PurchaseDetailService purchaseDetailService;

    @Autowired
    private WareSkuService wareSkuService;

    @Autowired
    private ProductFeignService productFeignService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageUnreceive(Map<String, Object> params) {
        QueryWrapper<PurchaseEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("status", WareConstant.PurchaseStatusEnum.CREATED.getCode()).or().eq("status",WareConstant.PurchaseStatusEnum.ASSIGNED.getCode());

        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void mergePurchase(MergerVo mergerVo) {
        Long purchaseId = mergerVo.getPurchaseId();
        // 如果采购id为null 说明没选采购单
        if (purchaseId == null){
            //新建采购单
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.CREATED.getCode());
            this.save(purchaseEntity);
            purchaseId = purchaseEntity.getId();
        }
        //合并采购需求
        List<Long> items = mergerVo.getItems();

        Long finalPurchaseId = purchaseId;
        List<PurchaseDetailEntity> list = purchaseDetailService.getBaseMapper().selectBatchIds(items).stream().filter(entity -> {
            //如果还没去采购，或者采购失败，就可以修改
            return entity.getStatus() < WareConstant.PurchaseDetailStatusEnum.BUYING.getCode()
                    || entity.getStatus() == WareConstant.PurchaseDetailStatusEnum.HASERROR.getCode();
        }).map(entity -> {
            //修改状态，以及采购单id
            entity.setStatus(WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode());
            entity.setPurchaseId(finalPurchaseId);
            return entity;
        }).collect(Collectors.toList());

        purchaseDetailService.updateBatchById(list);

    }

    @Transactional
    @Override
    public void received(List<Long> ids) {
        // 没有采购需求直接返回，否则会破坏采购单
        if (ids == null || ids.size() == 0) {
            return;
        }

        List<PurchaseEntity> list = this.getBaseMapper().selectBatchIds(ids).stream().filter(entity -> {
            //确保采购单的状态是新建或者已分配
            return entity.getStatus() <= WareConstant.PurchaseStatusEnum.ASSIGNED.getCode();
        }).map(entity -> {
            //修改采购单的状态为已领取
            entity.setStatus(WareConstant.PurchaseStatusEnum.RECEIVE.getCode());
            return entity;
        }).collect(Collectors.toList());
        this.updateBatchById(list);

        //修改该采购单下的所有采购需求的状态为正在采购
        UpdateWrapper<PurchaseDetailEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("purchase_id", ids);
        PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
        purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.BUYING.getCode());
        purchaseDetailService.update(purchaseDetailEntity, updateWrapper);
    }


    @Override
    public void done(PurchaseDoneVo vo) {
        //1、根据前端发过来的信息，更新采购需求的状态
        List<PurchaseItemDoneVo> items = vo.getItems();
        List<PurchaseDetailEntity> updateList = new ArrayList<>();
        boolean flag = true;
        for (PurchaseItemDoneVo item : items) {
            Long detailId = item.getItemId();
            PurchaseDetailEntity detailEntity = purchaseDetailService.getById(detailId);
            if (detailEntity != null) {
            detailEntity.setStatus(item.getStatus());
            //采购需求失败
            if (item.getStatus() == WareConstant.PurchaseDetailStatusEnum.HASERROR.getCode()) {
                flag = false;
            } else {
                //3、根据采购需求的状态，更新库存
                // sku_id, sku_num, ware_id
                // sku_id, ware_id, stock sku_name(调用远程服务获取), stock_locked(先获取已经有的库存，再加上新购买的数量)
                String skuName = "";
                try {
                    R info = productFeignService.info(detailEntity.getSkuId());
                    if(info.getCode() == 0){
                        Map<String,Object> data=(Map<String,Object>)info.get("skuInfo");
                        skuName = (String) data.get("skuName");
                    }
                } catch (Exception e) {

                }
                //更新库存
                wareSkuService.addStock(detailEntity.getSkuId(), detailEntity.getWareId(), skuName, detailEntity.getSkuNum());
            }
            updateList.add(detailEntity);
        }
            }
            //保存采购需求
            purchaseDetailService.updateBatchById(updateList);
            //2、根据采购需求的状态，更新采购单的状态
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setId(vo.getId());
            purchaseEntity.setStatus(flag ? WareConstant.PurchaseStatusEnum.FINISH.getCode() : WareConstant.PurchaseStatusEnum.HASERROR.getCode());
            this.updateById(purchaseEntity);

    }


}
