package com.atcode.watermall.product.service.impl;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.atcode.common.constant.ProductConstant;
import com.atcode.watermall.product.dao.AttrAttrgroupRelationDao;
import com.atcode.watermall.product.dao.AttrGroupDao;
import com.atcode.watermall.product.dao.CategoryDao;
import com.atcode.watermall.product.entity.AttrAttrgroupRelationEntity;
import com.atcode.watermall.product.entity.AttrGroupEntity;
import com.atcode.watermall.product.entity.CategoryEntity;
import com.atcode.watermall.product.service.CategoryService;
import com.atcode.watermall.product.vo.AttrGroupRelationVo;
import com.atcode.watermall.product.vo.AttrRespVo;
import com.atcode.watermall.product.vo.AttrVo;
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
    private AttrAttrgroupRelationDao attrAttrgroupRelationDao;

    @Autowired
    private AttrGroupDao attrGroupDao;

    @Autowired
    private CategoryDao categoryDao;
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
//        wrapper.eq(AttrEntity::getAttrType,"base".equalsIgnoreCase(attrType)? ProductConstant.AttrEnum.ATTR_TYPE_BASE : ProductConstant.AttrEnum.ATTR_TYPE_SALE);
        if(StringUtils.isNotEmpty(key)){
            wrapper.and(obj->obj.eq(AttrEntity::getAttrId,key).or().like(AttrEntity::getAttrName,key));
        }
        //catelogId查询
        if(catelogId!=0) {
            wrapper.eq(AttrEntity::getCatelogId,catelogId);
//            wrapper.eq("catelog_id",catelogId);
        }

        //查询
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                wrapper
        );
        PageUtils pageUtils = new PageUtils(page);

        List<AttrEntity> allData = this.list();  // 查询所有数据
        System.out.println("Total records in table: " + allData.size());
        System.out.println("Total records in Ipages: " + page.getRecords().size());

        /**
         *分页数据中加入当前属性的“所属分类”和“所属分组”
         * 报错原因，selectone可能会出现空指针异常  优化方法是使用!null判断
         * sql优化代码
         */

        //TODO 下面的selectOne位置有bug，需要接收的是一条，但是查到的不是一条   //bug原因，属性分组关系表中只能是一对一的关系  也有可能是下面的代码原因，使用1/0即使是一对多也不会报错
        /**
         * wrapper.eq(AttrEntity::getAttrType,"base".equalsIgnoreCase(attrType)? ProductConstant.AttrEnum.ATTR_TYPE_BASE : ProductConstant.AttrEnum.ATTR_TYPE_SALE);
         */
        List<AttrEntity> attrEntities = page.getRecords();
        List<AttrRespVo> attrRespVos = attrEntities.stream().map(attrEntity->{
            AttrRespVo attrRespVo = new AttrRespVo();
            BeanUtils.copyProperties(attrEntity,attrRespVo);
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

            if(attrGroupId!=null){
            AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrGroupId);
            if(attrGroupEntity!=null) attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
            }
            return attrRespVo;

        }).collect(Collectors.toList());

        /**
         * sql性能优化
         * 使用批量查询来减少数据库访问次数。
         * 可以先通过 attrRespVo.getAttrId()
         * 批量查询所有 AttrAttrgroupRelationEntity 记录，
         * 然后通过 attrGroupId 批量查询 AttrGroupEntity，
         * 从而减少每次都要进行数据库查询的开销。
         */
//        List<AttrEntity> attrEntities = page.getRecords();
//        // 批量查询 AttrAttrgroupRelationEntity
//        List<AttrAttrgroupRelationEntity> attrGroupRelations = attrAttrgroupRelationDao.selectList(
//                new LambdaQueryWrapper<AttrAttrgroupRelationEntity>().in(AttrAttrgroupRelationEntity::getAttrId,
//                        attrEntities.stream().map(AttrEntity::getAttrId).collect(Collectors.toList()))
//        );
//
//// 生成 ID 到 AttrGroupId 的映射
//        Map<Long, Long> attrGroupIdMap = attrGroupRelations.stream().collect(Collectors.toMap(AttrAttrgroupRelationEntity::getAttrId,
//                AttrAttrgroupRelationEntity::getAttrGroupId));
//
//// 批量查询 CategoryEntity 和 AttrGroupEntity
//        List<CategoryEntity> categories = categoryDao.selectBatchIds(attrEntities.stream().map(AttrEntity::getCatelogId).collect(Collectors.toList()));
//        List<AttrGroupEntity> attrGroups = attrGroupDao.selectBatchIds(new ArrayList<>(attrGroupIdMap.values()));
//
//// 对每个 `attrRespVo` 进行赋值操作
//        List<AttrRespVo> attrRespVos = attrEntities.stream().map(attrEntity -> {
//            AttrRespVo attrRespVo = new AttrRespVo();
//            BeanUtils.copyProperties(attrEntity, attrRespVo);
//
//            // 设置分类名称
//            CategoryEntity categoryEntity = categories.stream().filter(c -> c.getCatId().equals(attrRespVo.getCatelogId())).findFirst().orElse(null);
//            if (categoryEntity != null) {
//                attrRespVo.setCatelogName(categoryEntity.getName());
//            } else {
//                attrRespVo.setCatelogName("未知分类");
//            }
//
//            // 设置分组名称
//            Long groupId = attrGroupIdMap.get(attrRespVo.getAttrId());
//            if (groupId != null) {
//                AttrGroupEntity attrGroupEntity = attrGroups.stream().filter(g -> g.getAttrGroupId().equals(groupId)).findFirst().orElse(null);
//                if (attrGroupEntity != null) {
//                    attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
//                } else {
//                    attrRespVo.setGroupName("未知分组");
//                }
//            }
//
//            return attrRespVo;
//        }).collect(Collectors.toList());


        pageUtils.setList(attrRespVos);
        //返回分页工具对象
        return pageUtils;
    }

//    @Override
//    public void saveAttr(AttrVo attr) {
//        //AttrEntity attrEntity = new AttrEntity();
//        //保存attrEntity
//        //利用attr的属性给attrEntity的属性赋值，前提是他们俩的属性名一致
//        //BeanUtils.copyProperties(attr, attrEntity);
//
//        //1、保存基本数据
//        this.save(attr);
//        //2、保存关联关系
//        AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
//        relationEntity.setAttrGroupId(attr.getAttrGroupId());
//        relationEntity.setAttrId(attr.getAttrId());
//        attrAttrgroupRelationDao.insert(relationEntity);
//    }

    /**
     * 保存基本规格参数和关联关系
     * @param attrVo
     */
    @Override
    @Transactional
    public void saveAttr(AttrVo attrVo) {
        this.save(attrVo);
        //仅在规格属性的分组有值时，保存属性和属性分组关联关系
        if(attrVo.getAttrType()==ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()&&attrVo.getAttrGroupId()!=null){
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            attrAttrgroupRelationEntity.setAttrId(attrVo.getAttrId());
            attrAttrgroupRelationEntity.setAttrGroupId(attrVo.getAttrGroupId());
            attrAttrgroupRelationDao.insert(attrAttrgroupRelationEntity);
        }


    }

    /**
     * 修改属性时，同步修改与属性分组关联关系
     * @param attrVo
     */

    @Transactional
    @Override
    public void updateAttr(AttrVo attrVo) {
        //先修改属性
        this.updateById(attrVo);
        Long attrGroupId = attrVo.getAttrGroupId();
        Long attrId = attrVo.getAttrId();
        //再判断修改关联关系。如果是销售属性或者分组id是空时，不用修改
        if(attrVo.getAttrType()==ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode()||attrGroupId==null){
            //TODO 待优化，如果是将规格属性改为销售属性，查询删除原关联关系
            return;
        }
        //查询关联表中是否已有此属性
        AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = attrAttrgroupRelationDao.selectOne(
                new LambdaQueryWrapper<AttrAttrgroupRelationEntity>().eq(AttrAttrgroupRelationEntity::getAttrId, attrId)
        );
        if(attrAttrgroupRelationEntity!=null){
//            已存在，修改
            attrAttrgroupRelationEntity.setAttrGroupId(attrGroupId);
            attrAttrgroupRelationDao.updateById(attrAttrgroupRelationEntity);
        }else {
            //不存在，新增关联关系
            attrAttrgroupRelationEntity=new AttrAttrgroupRelationEntity();
            attrAttrgroupRelationEntity.setAttrGroupId(attrGroupId);
            attrAttrgroupRelationEntity.setAttrId(attrId);
            attrAttrgroupRelationDao.insert(attrAttrgroupRelationEntity);
        }
    }

    @Override
    public List<AttrEntity> getRelationAttr(Long attrgroupId) {
        List<AttrAttrgroupRelationEntity> relationEntities = attrAttrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", attrgroupId));
        List<Long> attrIds = relationEntities.stream().map((entity) -> {
            return entity.getAttrId();
        }).collect(Collectors.toList());

//        List<AttrEntity> attrEntities = this.baseMapper.selectList(new QueryWrapper<AttrEntity>().eq("attr_id",attrIds));
//        List<AttrEntity> attrEntities = this.baseMapper.selectBatchIds(attrIds);
        /**
         * 每个id查一次表
         */
        //TODO 可以修改为查到关系表的所有内容，然后再和group_id进行匹配
        List<AttrEntity> attrEntities = attrIds.stream().map((attrId) -> {
            AttrEntity attrEntity = new AttrEntity();
            attrEntity = this.baseMapper.selectById(attrId);
            return attrEntity;
        }).collect(Collectors.toList());

        return attrEntities;
    }

    @Override
    public void deleteRelation(AttrGroupRelationVo[] vos) {
        List<AttrGroupRelationVo> relationVos = Arrays.asList(vos);
        List<AttrAttrgroupRelationEntity> entities = relationVos.stream().map((relationVo) -> {
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(relationVo, relationEntity);
            return relationEntity;
        }).collect(Collectors.toList());
        //根据attrId，attrGroupId批量删除关联关系
        attrAttrgroupRelationDao.deleteBatchRelation(entities);
    }

    /**
     *获取当前分组没有关联的所有属性
     * 当前分组展示的未关联属性要求：
     * 1.查询当前分组所在分类。当前分组只能关联自己所属分类里面的所有属性。例如“基本属性”分组里展示未关联的“入网型号”等手机分类里的属性。
     * 2.查询此分类下的。当前分组只能关联同分类下，别的分组没有引用的属性。
     * 2.1.查询当前分类下的其他属性分组ids
     * 2.2.查询这些分组中已关联的属性ids
     * 2.3.从当前分类的所有属性中排除查询这些分组中已关联的属性list
     * 每个属性和分组都属于某个分类。

     * @param params
     * @param attrgroupId
     * @return
     */
    @Override
    public PageUtils getNoRelationAttr(Map<String, Object> params, Long attrgroupId) {
        //1、当前分组只能关联自己所属分类里面的所有属性
        //先查询出当前分组所属的分类
        AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrgroupId);
        Long catelogId = attrGroupEntity.getCatelogId();
        //2、当前分组只能关联别的分组没有引用的属性
        //2.1当前分类下的所有分组
        List<AttrGroupEntity> attrGroupEntities = attrGroupDao.selectList(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));
        List<Long> attrGroupIds = attrGroupEntities.stream().map(attrGroupEntity1 -> {
            return attrGroupEntity1.getAttrGroupId();
        }).collect(Collectors.toList());
        //2.2这些分组关联的属性
        List<AttrAttrgroupRelationEntity> relationEntities = attrAttrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().in("attr_group_id", attrGroupIds));
        List<Long> attrIds = relationEntities.stream().map((relationEntity) -> {
            return relationEntity.getAttrId();
        }).collect(Collectors.toList());
        // 2.3从当前分类的所有属性中移除这些属性
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<AttrEntity>().eq("catelog_id", catelogId).eq("attr_type", ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode());
        if (attrIds != null && attrIds.size() > 0){
            wrapper.notIn("attr_id", attrIds);
        }
        //模糊查询
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)){
            wrapper.and((w)->{
                w.eq("attr_id", key).or().like("attr_name", key);
            });
        }
        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), wrapper);

        return new PageUtils(page);
    }

    /**
     * 在指定的所有属性集合中，挑出所有的检索属性
     * @param attrIds
     * @return
     */
    @Override
    public List<Long> selectSearchAttrIds(List<Long> attrIds) {
        /**
         * select attr_id from pms_attr where attr_id in(?) and search_type = 1
         */
        return baseMapper.selectSearchAttrIds(attrIds);
    }


    /**
     * 也可以不用xml，用wrapper删除，但时间复杂度和编写复杂度会比动态sql低一些：
     * @param attrId
     * @return
     */
//    @Override
//    public void deleteRelation(List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntities) {
//        //遍历查删
//        attrAttrgroupRelationEntities.stream().forEach(item->{
//            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = attrAttrgroupRelationDao.selectOne(
//                    new LambdaQueryWrapper<AttrAttrgroupRelationEntity>().eq(AttrAttrgroupRelationEntity::getAttrId, item.getAttrId()).eq(AttrAttrgroupRelationEntity::getAttrGroupId, item.getAttrGroupId())
//            );
//            if(attrAttrgroupRelationEntity!=null) attrAttrgroupRelationDao.deleteById(attrAttrgroupRelationEntity.getId());
//        });
//    }

    @Override
    public AttrRespVo getAttrInfo(Long attrId) {
        AttrRespVo attrRespVo = new AttrRespVo();
        AttrEntity attrEntity = this.getById(attrId);
        BeanUtils.copyProperties(attrEntity, attrRespVo);

        /**
         * 根据属性id拿到对应的分组id  attrAttrgroupRelationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrId)
         *                         attrAttrgroupRelation.getAttrGroupId()
         * 有分组Id之后可以拿到对应的分组名称和分类路径
         */

        //设置所属分组
        AttrAttrgroupRelationEntity attrAttrgroupRelation = attrAttrgroupRelationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrId));
//        if (attrAttrgroupRelation != null){
//            attrRespVo.setAttrGroupId(attrAttrgroupRelation.getAttrGroupId());
//        }
//        //设置分组名称
//        AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrAttrgroupRelation.getAttrGroupId());
//
//        if(attrGroupEntity != null){
//            attrRespVo.setAttrGroupId(attrGroupEntity.getAttrGroupId());
//            attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
//        }

        if (attrAttrgroupRelation != null){
            attrRespVo.setAttrGroupId(attrAttrgroupRelation.getAttrGroupId());
            //设置分组名称
            AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrAttrgroupRelation.getAttrGroupId());

            if(attrGroupEntity != null){
                attrRespVo.setAttrGroupId(attrGroupEntity.getAttrGroupId());
                attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
            }
        }



        //设置所属分类路径
        Long[] catelogPath = categoryService.findCatelogPath(attrEntity.getCatelogId());
        attrRespVo.setCatelogPath(catelogPath);

        CategoryEntity categoryEntity = categoryDao.selectById(attrEntity.getCatelogId());
        if(categoryEntity != null){
        attrRespVo.setCatelogName(categoryEntity.getName());}

        return attrRespVo;
    }

    //保存时，要修改两张表，要加业务注解，引导类也开启了业务
//    @Transactional
//    @Override
//    public void updateAttr(AttrVo attr) {
//        AttrEntity attrEntity = new AttrEntity();
//        BeanUtils.copyProperties(attr, attrEntity);
//        this.updateById(attrEntity);
//
//        AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
//        relationEntity.setAttrGroupId(attr.getAttrGroupId());
//        relationEntity.setAttrId(attr.getAttrId());
//
//        //判断是新增还是删除。属性分组和属性是一对多，也可以用selectOne查
//        //TODO
//        Integer count = Math.toIntExact(attrAttrgroupRelationDao.selectCount(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attr.getAttrId())));
//        if (count > 0){
//            attrAttrgroupRelationDao.update(relationEntity, new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attr.getAttrId()));
//        }else{
//            attrAttrgroupRelationDao.insert(relationEntity);
//        }
//
//    }

}
