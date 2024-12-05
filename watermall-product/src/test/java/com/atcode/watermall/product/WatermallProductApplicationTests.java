package com.atcode.watermall.product;

import com.atcode.watermall.product.entity.BrandEntity;
import com.atcode.watermall.product.service.BrandService;
import com.atcode.watermall.product.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
class WatermallProductApplicationTests {

    @Autowired
    BrandService brandService;


    @Autowired
    CategoryService categoryService;
    @Test
    public void testPathCatelog(){
        Long [] catelogPath= categoryService.findCatelogPath(165L);

        System.out.println(catelogPath.length);

    }
    @Test
    void contextLoads() {
        BrandEntity brandEntity = new BrandEntity();
        /**
         * 测试新增方法
         */
//        brandEntity.setDescript("这是用来测试的一个描述2");
//        brandEntity.setName("这是用来测试的品牌的名字2");
//        brandService.save(brandEntity);
//        System.out.println("保存成功");

        /**
         * 测试修改方法
         */
//        brandEntity.setBrandId(1L);
//        brandEntity.setDescript("这是被修改之后的描述");
//        brandService.updateById(brandEntity);
//        System.out.println("修改成功");

        /**
         * 测试查询方法
         */
        List<BrandEntity> list = brandService.list(new QueryWrapper<BrandEntity>().eq("brand_id", 1L));
        list.forEach((item)->
                System.out.println(item));
    }

}
