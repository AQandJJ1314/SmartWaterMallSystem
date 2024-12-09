package com.atcode.watermall.product.service.impl;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.atcode.watermall.product.dao.AttrAttrgroupRelationDao;
import com.atcode.watermall.product.dao.AttrGroupDao;
import com.atcode.watermall.product.dao.CategoryDao;
import com.atcode.watermall.product.entity.AttrAttrgroupRelationEntity;
import com.atcode.watermall.product.entity.AttrGroupEntity;
import com.atcode.watermall.product.entity.CategoryEntity;
import com.atcode.watermall.product.service.CategoryService;
import com.atcode.watermall.product.vo.AttrRespVo;
import com.atcode.watermall.product.vo.AttrVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atcode.common.utils.PageUtils;
import com.atcode.common.utils.Query;

import com.atcode.watermall.product.dao.AttrDao;
import com.atcode.watermall.product.entity.AttrEntity;
import com.atcode.watermall.product.service.AttrService;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;


@EnableTransactionManagement
@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {


    @Autowired
    private CategoryService categoryService;

    @Autowired
    AttrAttrgroupRelationDao attrAttrgroupRelationDao;

    @Autowired
    AttrGroupDao attrGroupDao;

    @Autowired
    CategoryDao categoryDao;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }


    @Override
    public PageUtils queryBaseAttrPage(Map<String, Object> params,Long catelogId,String attrType) {
        //模糊查询

        String key = (String) params.get("key");
        LambdaQueryWrapper<AttrEntity> wrapper = new LambdaQueryWrapper<>();
        //值添加这一行。判断是规格参数还是销售属性,[0-销售属性，1-基本属性，2-既是销售属性又是基本属性]
        wrapper.eq(AttrEntity::getAttrType,"base".equalsIgnoreCase(attrType)?1:0);
        if(StringUtils.isNotEmpty(key)){
            wrapper.and(obj->obj.eq(AttrEntity::getAttrId,key).or().like(AttrEntity::getAttrName,key));
        }
        //catelogId查询
        if(catelogId!=0) wrapper.eq(AttrEntity::getCatelogId,catelogId);
        //查询
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                wrapper
        );
        PageUtils pageUtils = new PageUtils(page);
        //分页数据中加入当前属性的“所属分类”和“所属分组”
        List<AttrEntity> attrEntities = page.getRecords();
        List<AttrRespVo> attrRespVos = attrEntities.stream().map(item->{
            AttrRespVo attrRespVo = new AttrRespVo();
            BeanUtils.copyProperties(item,attrRespVo);
            //查询“所属分类”和“所属分组”的name
            Long catelogId2 = attrRespVo.getCatelogId();
            Long attrGroupId=null;
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = attrAttrgroupRelationDao.selectOne(
                    new LambdaQueryWrapper<AttrAttrgroupRelationEntity>().eq(AttrAttrgroupRelationEntity::getAttrId,attrRespVo.getAttrId())
            );
            if(attrAttrgroupRelationEntity!=null) attrGroupId = attrAttrgroupRelationEntity.getAttrGroupId();
            CategoryEntity categoryEntity = categoryDao.selectById(catelogId2);
            //没查到的对象就不能getName了，必须防止空指针异常，习惯习惯，坑点
            if(categoryEntity!=null) attrRespVo.setCatelogName(categoryEntity.getName());
            AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrGroupId);
            if(attrGroupEntity!=null) attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
            return attrRespVo;
        }).collect(Collectors.toList());
        pageUtils.setList(attrRespVos);
        //返回分页工具对象
        return pageUtils;
    }

    @Override
    public void saveAttr(AttrVo attr) {
        //AttrEntity attrEntity = new AttrEntity();
        //保存attrEntity
        //利用attr的属性给attrEntity的属性赋值，前提是他们俩的属性名一致
        //BeanUtils.copyProperties(attr, attrEntity);

        //1、保存基本数据
        this.save(attr);
        //2、保存关联关系
        AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
        relationEntity.setAttrGroupId(attr.getAttrGroupId());
        relationEntity.setAttrId(attr.getAttrId());
        attrAttrgroupRelationDao.insert(relationEntity);
    }



    @Override
    public AttrRespVo getAttrInfo(Long attrId) {
        AttrRespVo attrRespVo = new AttrRespVo();
        AttrEntity attrEntity = this.getById(attrId);
        BeanUtils.copyProperties(attrEntity, attrRespVo);

        //设置所属分组
        AttrAttrgroupRelationEntity attrAttrgroupRelation = attrAttrgroupRelationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrId));
        if (attrAttrgroupRelation != null){
            attrRespVo.setAttrGroupId(attrAttrgroupRelation.getAttrGroupId());
        }
        //设置所属分类路径
        Long[] catelogPath = categoryService.findCatelogPath(attrEntity.getCatelogId());
        attrRespVo.setCatelogPath(catelogPath);

        return attrRespVo;
    }

    //保存时，要修改两张表，要加业务注解，引导类也开启了业务
    @Transactional
    @Override
    public void updateAttr(AttrVo attr) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr, attrEntity);
        this.updateById(attrEntity);

        AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
        relationEntity.setAttrGroupId(attr.getAttrGroupId());
        relationEntity.setAttrId(attr.getAttrId());

        //判断是新增还是删除。属性分组和属性是一对多，也可以用selectOne查
        //TODO
        Integer count = Math.toIntExact(attrAttrgroupRelationDao.selectCount(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attr.getAttrId())));
        if (count > 0){
            attrAttrgroupRelationDao.update(relationEntity, new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attr.getAttrId()));
        }else{
            attrAttrgroupRelationDao.insert(relationEntity);
        }

    }

}
