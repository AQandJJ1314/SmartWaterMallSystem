package com.atcode.watermall.product.service.impl;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.atcode.watermall.product.dao.CategoryDao;
import com.atcode.watermall.product.entity.CategoryEntity;
import com.atcode.watermall.product.service.AttrService;
import com.atcode.watermall.product.vo.AttrGroupWithAttrsVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atcode.common.utils.PageUtils;
import com.atcode.common.utils.Query;

import com.atcode.watermall.product.dao.AttrGroupDao;
import com.atcode.watermall.product.entity.AttrGroupEntity;
import com.atcode.watermall.product.service.AttrGroupService;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {


    @Autowired
    AttrService attrService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long categoryId) {
        String key = (String) params.get("key");
        LambdaQueryWrapper<AttrGroupEntity> wrapper = new LambdaQueryWrapper<>();
        //先根据检索查
        //select * from pms_attr_group where catelog_id = ? and(attr_group_id=key or attr_group_name=key)
        //可以使用querywapper实现，要查的是哪张表，就用哪张表对应的实体类
        if(StringUtils.isNotEmpty(key)){
//wrapper.eq(AttrGroupEntity::getAttrGroupId,key).or().like(AttrGroupEntity::getAttrGroupName,key);//也可以，因为多条件查询默认是and，or要用.or()。
            wrapper.and(
                    obj->obj.eq(AttrGroupEntity::getAttrGroupId,key).or().like(AttrGroupEntity::getAttrGroupName,key)
            );
        }
        if(categoryId!=0) {
            wrapper.eq(AttrGroupEntity::getCatelogId,categoryId);
        }
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);

    }

    /**
     * 获取当前分类下所有分组以及属性
     * @param catelogId
     * @return
     */
    @Override
    public List<AttrGroupWithAttrsVo> getAttrGroupWithAttrsByCatelogId(Long catelogId) {
        //查询此分类下所有分组
        List<AttrGroupEntity> attrGroupEntities = this.list(new LambdaQueryWrapper<AttrGroupEntity>().eq(AttrGroupEntity::getCatelogId, catelogId));

        //每个分组查询规格属性
        List<AttrGroupWithAttrsVo> vos=attrGroupEntities.stream().map(item->{
            AttrGroupWithAttrsVo vo = new AttrGroupWithAttrsVo();
            BeanUtils.copyProperties(item,vo);
            //在关联表里查询此分组的规格属性
            Long groupId = item.getAttrGroupId();
            //有编写这个业务，获取分组关联的属性list
            vo.setAttrs(attrService.getRelationAttr(groupId));
            return vo;
        }).collect(Collectors.toList());
        return vos;
    }

}
