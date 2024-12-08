package com.atcode.watermall.product.service.impl;

import com.atcode.watermall.product.service.CategoryBrandRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atcode.common.utils.PageUtils;
import com.atcode.common.utils.Query;

import com.atcode.watermall.product.dao.CategoryDao;
import com.atcode.watermall.product.entity.CategoryEntity;
import com.atcode.watermall.product.service.CategoryService;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;


@EnableTransactionManagement
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    //注入DAO或者使用BaseMapper
//    @Autowired
//    CategoryDao categoryDao;


    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {

        //1、查出所有分类。baseMapper来自于继承的ServiceImpl<>类，跟CategoryDao一样用法
        List<CategoryEntity> entities = baseMapper.selectList(null);

        //2、递归组装多级分类的树形结构。先过滤得到一级分类，再加工递归设置一级分类的子孙分类，再排序，再收集
        List<CategoryEntity> level1Menus = entities.stream()
                .filter(categoryEntity -> categoryEntity.getParentCid() == 0)
                .map((menu)->{
                    // 设置一级分类的子分类
                    menu.setChildren(getChildren(menu, entities));
                    return menu;
                }).sorted((menu1, menu2) -> {
                    //排序，sort是实体类的排序属性，值越小优先级越高，要判断非空防止空指针异常
                    return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
                })
                .collect(Collectors.toList());


        return level1Menus;
    }

    @Override
    public void removeMenuByIds(List<Long> asList) {

        //TODO 1.检查当前删除的菜单是否被别的地方引用



        //逻辑删除
        baseMapper.deleteBatchIds(asList);
    }

    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> paths = new ArrayList<>();
        List<Long> parentPath = findParentPath(paths,catelogId);
        return parentPath.toArray(new Long[parentPath.size()]);
    }

    //别忘了加业务注解，引导类开启了业务@EnableTransactionManagement
    @Transactional
    @Override
    public void updateCascade(CategoryEntity category) {
        this.updateById(category);
        categoryBrandRelationService.updateCategory(category.getCatId(), category.getName());
    }
    /**
     * 递归查询父节点id
     * @param paths
     * @param catelogId
     * @return
     */
    private List<Long> findParentPath(List<Long> paths, Long catelogId) {
        CategoryEntity categoryEntity = this.getById(catelogId);

        if(categoryEntity == null) return paths;

        if (categoryEntity.getParentCid() != 0){
            findParentPath(paths ,categoryEntity.getParentCid());
        }
        paths.add(catelogId);
        return paths;
    }

    //递归查找所有菜单的子菜单


    /**
     * 获取一个菜单的子菜单,递归查找
     * @param root  当前菜单
     * @param all   从哪里获取子菜单(所有菜单数据)
     * @return
     */
    private List<CategoryEntity> getChildren(CategoryEntity root, List<CategoryEntity> all){
        List<CategoryEntity> children = all.stream()
                .filter(CategoryEntity -> CategoryEntity.getParentCid().equals(root.getCatId()))
                .map(categoryEntity -> {
                    //递归查找
                    categoryEntity.setChildren(getChildren(categoryEntity, all));
                    return categoryEntity;
                })
                .sorted((menu1, menu2) -> {
                    return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
                })
                .collect(Collectors.toList());
        return children;
    }



}
