package com.atcode.watermall.product.app;

import java.util.Arrays;
import java.util.List;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.atcode.watermall.product.entity.CategoryEntity;
import com.atcode.watermall.product.service.CategoryService;
import com.atcode.common.utils.R;



/**
 * 商品三级分类
 *
 * @author JiangCheng
 * @email JiangCheng@watermail.com
 * @date 2024-11-25 19:53:42
 */
@RestController
@RequestMapping("product/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 查出所有分类以及子分类，以树形结构组装起来
     */
    @RequestMapping("/list/tree")
    public R list(){
        List<CategoryEntity> entities = categoryService.listWithTree();

        return R.ok().put("data", entities);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{catId}")
    //@RequiresPermissions("product:category:info")
    public R info(@PathVariable("catId") Long catId){
		CategoryEntity category = categoryService.getById(catId);

        return R.ok().put("data", category);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
   // @RequiresPermissions("product:category:save")
    public R save(@RequestBody CategoryEntity category){
		categoryService.save(category);

        return R.ok();
    }

//    /**
//     * 修改
//     */
//    @RequestMapping("/update")
//    //@RequiresPermissions("product:category:update")
//    public R update(@RequestBody CategoryEntity category){
//		categoryService.updateById(category);
//
//        return R.ok();
//    }
    /**
     * 修改分类名时，同步修改关联表的分类名。映射配置文件xml方法 对应brand
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:category:update")
    public R update(@RequestBody CategoryEntity category){
        categoryService.updateCascade(category);

        return R.ok();
    }

    /**
     * 批量修改，参数要传数组，不能传list
     * @param category
     * @return
     */

    @RequestMapping("/update/sort")
    public R updateSort(@RequestBody CategoryEntity[] category){
        categoryService.updateBatchById(Arrays.asList(category));
        return R.ok();
    }


    /**
     * 删除
     * @RequestBody 该注解是获取到请求体中的内容，必须发送POST请求，只有POST请求有请求体
     * SpringMVC会将请求体中的内容（json），转为对应的对象
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:category:delete")
    public R delete(@RequestBody Long[] catIds){

        //1.检查当前删除的菜单是否被别的地方引用
//		categoryService.removeByIds(Arrays.asList(catIds));

        categoryService.removeMenuByIds(Arrays.asList(catIds));
        return R.ok();
    }

}
