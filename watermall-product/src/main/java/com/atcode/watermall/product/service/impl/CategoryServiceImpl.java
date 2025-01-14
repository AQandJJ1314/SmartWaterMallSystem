package com.atcode.watermall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.atcode.watermall.product.service.CategoryBrandRelationService;
import com.atcode.watermall.product.vo.Catalog2Vo;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
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
import org.springframework.util.StringUtils;


@EnableTransactionManagement
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    //注入DAO或者使用BaseMapper
//    @Autowired
//    CategoryDao categoryDao;

    //连接redis所用
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    @Autowired
    private RedissonClient redissonClient;

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
                .map((menu) -> {
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
        List<Long> parentPath = findParentPath(paths, catelogId);
        return parentPath.toArray(new Long[parentPath.size()]);
    }


    //调用该方法会删除缓存category下的所有cache，如果要删除某个具体，用key="''"
    //allEntries = true，value中分区删除里的所有数据
    //更新操作
    @Override
    @CacheEvict(value = {"category"},allEntries = true)   //调用该方法(updateCascade)会删除缓存category下的所有cache
    public void updateCascade(CategoryEntity category) {
        this.updateById(category);
        if (!StringUtils.isEmpty(category.getName())) {
            categoryBrandRelationService.updateCategory(category.getCatId(), category.getName());
        }
    }

    //别忘了加业务注解，引导类开启了业务@EnableTransactionManagement
//    @Transactional
//    @Override
//    public void updateCascade(CategoryEntity category) {
//        this.updateById(category);
//        categoryBrandRelationService.updateCategory(category.getCatId(), category.getName());
//
//
//        //同时修改缓存中的数据
//        //redis.del("catalogJSON");等待下次主动查询进行更新
//    }

    // 调用该方法时会将结果缓存，缓存名为category，key为方法名
    // sync表示该方法的缓存被读取时会加锁
    // value等同于cacheNames 【缓存分区名】
    // key如果是字符串"''"，【请加上单引号】
    //每一个需要缓存的数据都需要指定放到哪个名字的缓存(缓存的分区，按照业务类型分)
    @Cacheable (value = {"category"},key = "#root.method.name")   //代表当前方法的结果需要缓存  如果缓存中有，那该方法就不会被调用，如果缓存中没有就调用该方法，最后将方法的结果放入缓存
//    @Cacheable (value = {"category"},key = "'Level1Categorys'")
    @Override
    public List<CategoryEntity> getLevel1Categorys() {
        System.out.println("=======数据库查询进入一级分类=========");
        List<CategoryEntity> categoryEntities = this.baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));
        return categoryEntities;
//        return null;
    }


    //TODO OutOfMemoryError  产生堆外内存异常  此性能未做压测，不清楚是否高版本已修复，后续做压测验证

    /**
     * //springboot2.0以后默认使用lettuce作为redis的客户端，它使用netty进行网络通信
     * //lettuce的bug导致netty的堆外内存溢出 -Xmx300 netty如果没有指定堆外最大内存，默认使用-Xmx300
     * 不能使用-Dio.netty.maxDirectMemory调大堆外内存，迟早会出问题。
     * 解决方案
     * 升级lettuce客户端（推荐）；【2.3.2已解决】【lettuce使用netty吞吐量很大】
     * 切换使用jedis客户端
     */

    @Override
    public Map<String, List<Catalog2Vo>> getCatalogJson() {
        //给缓存中放json字符串，拿出的json字符串还要逆转成能用的对象类型 ‘序列化与反序列化’

        /**
         * 1、空结果缓存，解决缓存穿透问题
         * 2、设置过期时间（加随机值），解决缓存雪崩问题
         * 3、加锁，解决缓存击穿问题
         */

        //1.加入缓存逻辑   缓存中放的数据是json字符串
        //json跨语言跨平台兼容
        String catalogJson = stringRedisTemplate.opsForValue().get("catalogJson");
        if (StringUtils.isEmpty(catalogJson)) {
            System.out.println("缓存不命中....查询数据库...");
            //2.缓存中没有，查询数据库
            Map<String, List<Catalog2Vo>> catalogJsonFromDB = getCatalogJsonFromDBWithRedissonLock();
            /**
             *         //3.查到的数据再放入缓存 将对象转为json放在缓存中
             *             // String s = JSON.toJSONString(catalogJsonFromDB);
             *             //   stringRedisTemplate.opsForValue().set("catalogJson",s,1, TimeUnit.DAYS);
             */
        }
        System.out.println("缓存命中....直接返回结果.....");
        //转为指定的对象
        Map<String, List<Catalog2Vo>> result = JSON.parseObject(catalogJson, new TypeReference<Map<String, List<Catalog2Vo>>>() {
        });
        return result;
    }


    /**
     * 缓存中的数据如何和数据库中的数据保持一致
     * 缓存数据的一致性
     * 解决方案： 双写模式 失效模式
     * 双写模式： 数据库改完之后改缓存中的数据
     * 失效模式： 数据库改完之后直接删除缓存中的数据 等待下次主动更新
     * @return
     */
    public Map<String, List<Catalog2Vo>> getCatalogJsonFromDBWithRedissonLock() {

        //1.占用分布式锁 redis 同时设置过期时间，避免因程序异常或者服务器宕机导致的死锁问题
        /**
         * 锁的名字，锁的粒度，越细越快
         * 锁的粒度：具体缓存的是某个数据，比如说11号商品 可以用product-11-lock,12号用product-12-lock
         * 如果上述两个商品用同一把锁，product-lock，那么如果11号商品是一千以内的并发，12号商品是百万级别的并发
         * 就会出现11号锁等待12号锁释放了才拿到的尴尬情况
         */



        RLock lock = redissonClient.getLock("CatalogJson-lock");
        lock.lock(30, TimeUnit.SECONDS);
        Map<String, List<Catalog2Vo>> dataFromDb;
        try {
            dataFromDb = getDataFromDb();
        } finally {
            lock.unlock();
        }
        return dataFromDb;
    }

    public Map<String, List<Catalog2Vo>> getCatalogJsonFromDBWithRedisLock() {

        //1.占用分布式锁 redis 同时设置过期时间，避免因程序异常或者服务器宕机导致的死锁问题
        String uuid = UUID.randomUUID().toString();
        Boolean lock = stringRedisTemplate.opsForValue().setIfAbsent("lock", uuid, 300, TimeUnit.SECONDS);
        if (lock) {
            System.out.println("获取分布式锁成功...");
            //加锁成功....执行业务
            //设置过期时间
//            stringRedisTemplate.expire("lock",30,TimeUnit.SECONDS);
            Map<String, List<Catalog2Vo>> dataFromDb = null;
            try {
                dataFromDb = getDataFromDb();
            } finally {


                //释放锁
//            stringRedisTemplate.delete("lock");
                //获取值对比+对比成功删除  需要成为原子操作 使用Lua脚本解锁
//            String lockValue = stringRedisTemplate.opsForValue().get("lock");
//            //判断是自己的锁才可以删除
//            if(uuid.equals(lockValue)){stringRedisTemplate.delete("lock");}
                // 2、查询UUID是否是自己，是自己的lock就删除
                // 查询+删除 必须是原子操作：lua脚本解锁
                String luaScript = "if redis.call('get',KEYS[1]) == ARGV[1]\n" +
                        "then\n" +
                        "    return redis.call('del',KEYS[1])\n" +
                        "else\n" +
                        "    return 0\n" +
                        "end";
                // 删除锁
                Long lock1 = stringRedisTemplate.execute(
                        new DefaultRedisScript<Long>(luaScript, Long.class),
                        Arrays.asList("lock"), uuid);    //把key和value传给lua脚本
            }

            return dataFromDb;
        } else {
            System.out.println("获取分布式锁失败，等待重试....");
            //加锁失败....重试
            //休眠100mm重试
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return getCatalogJsonFromDBWithRedisLock();//自旋的方式等待
        }

    }

    private Map<String, List<Catalog2Vo>> getDataFromDb() {
        String catalogJson = stringRedisTemplate.opsForValue().get("catalogJson");
        if (!StringUtils.isEmpty(catalogJson)) {
            //缓存不为null，直接返回
            Map<String, List<Catalog2Vo>> result = JSON.parseObject(catalogJson, new TypeReference<Map<String, List<Catalog2Vo>>>() {
            });
            return result;
        }

        System.out.println("查询了数据库.....");
        /**
         * 1.将数据库的多次查询变成一次
         */
        // 一次性获取所有 数据
        List<CategoryEntity> selectList = baseMapper.selectList(null);
        // 1）、所有1级分类
        List<CategoryEntity> level1Categorys = getParent_cid(selectList, 0L);

        // 2）、封装数据
        Map<String, List<Catalog2Vo>> collect = level1Categorys.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), level1 -> {
            // 查到当前1级分类的2级分类
            List<CategoryEntity> category2level = getParent_cid(selectList, level1.getCatId());
            List<Catalog2Vo> catalog2Vos = null;
            if (category2level != null) {
                catalog2Vos = category2level.stream().map(level12 -> {
                    // 查询当前2级分类的3级分类
                    List<CategoryEntity> category3level = getParent_cid(selectList, level12.getCatId());
                    List<Catalog2Vo.Catalog3Vo> catalog3Vos = null;
                    if (category3level != null) {
                        catalog3Vos = category3level.stream().map(level13 -> {
                            return new Catalog2Vo.Catalog3Vo(level12.getCatId().toString(), level13.getCatId().toString(), level13.getName());
                        }).collect(Collectors.toList());
                    }
                    return new Catalog2Vo(level1.getCatId().toString(), catalog3Vos, level12.getCatId().toString(), level12.getName());
                }).collect(Collectors.toList());
            }
            return catalog2Vos;
        }));


        //3.查到的数据再放入缓存 将对象转为json放在缓存中
        String s = JSON.toJSONString(collect);
        stringRedisTemplate.opsForValue().set("catalogJson", s, 1, TimeUnit.DAYS);

        return collect;
    }

    //从数据库查询并封装分类数据
    public Map<String, List<Catalog2Vo>> getCatalogJsonFromDBWithLocalLock() {
        //只要是同一把锁，就能锁住需要这个锁的所有线程
        //1、 synchronized (this) SpringBoot所有组件在容器中都是单例的
        //TODO 本地（进程）锁，synchronized,JUC(Lock)只锁当前的进程的资源（实例对象/方法） ，分布式情况下想要锁住所有就得用分布式锁

        synchronized (this) {
            //得到锁以后再去缓存中确定一次，如果没有，才需要继续查询
            return getDataFromDb();
        }
    }

    /**
     * 查询出父ID为 parent_cid的List集合
     */
    private List<CategoryEntity> getParent_cid(List<CategoryEntity> selectList, Long parent_cid) {
        return selectList.stream().filter(item -> item.getParentCid() == parent_cid).collect(Collectors.toList());
        //return baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", level.getCatId()));
    }

    /**
     * 递归查询父节点id
     *
     * @param paths
     * @param catelogId
     * @return
     */
    private List<Long> findParentPath(List<Long> paths, Long catelogId) {
        CategoryEntity categoryEntity = this.getById(catelogId);

        if (categoryEntity == null) return paths;

        if (categoryEntity.getParentCid() != 0) {
            findParentPath(paths, categoryEntity.getParentCid());
        }
        paths.add(catelogId);
        return paths;
    }

    //递归查找所有菜单的子菜单


    /**
     * 获取一个菜单的子菜单,递归查找
     *
     * @param root 当前菜单
     * @param all  从哪里获取子菜单(所有菜单数据)
     * @return
     */
    private List<CategoryEntity> getChildren(CategoryEntity root, List<CategoryEntity> all) {
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

    /**
     * 整合springCache简化缓存开发
     * 1.引入依赖
     *   写配置 CacheAutoConfiguration会导入RedisConfiguration
     *   自动配置了缓存管理器RedisCacheManager
     * 2.配置使用redis作为缓存
     * 3.测试使用缓存
     *   @Cacheable 主要针对方法配置, 能够根据方法的请求参数对其结果进行缓存  ：触发将数据保存到缓存的操作
     *   @CacheEvict 清空缓存                                        ：  触发将数据从缓存删除的操作
     *   @CachePut 保证方法被调用, 又希望结果被缓存                      ：   不影响方法执行的方式更新缓存
     *   @Caching 组合上面三个注解多个操作                             ： 组合以上多个操作
     *   @EnableCaching 开启基于注解的缓存
     *   @CacheConfig 在类级别分享缓存的相同配置                      ：  在类级别分享缓存的相同配置
     *   @Cacheable：标注方法上：当前方法的结果存入缓存，如果缓存中有，方法不调用
     *   @CacheEvict：触发将数据从缓存删除的操作
     *   @CachePut：不影响方法执行更新缓存
     *   @Caching：组合以上多个操作
     *   @CacheConfig：在类级别共享缓存的相同配置
     *
     * 4.原理：CacheAutoConfiguration->RedisCacheConfiguration->自动配置了RedisCacheManager->初始化所有的缓存->每个缓存决定使用什么配置
     *        ->如果RedisCacheConfiguration有就用已有的，没有就用默认配置->如果要改配置，只需要给容器中增加一个RedisCacheConfiguration即可
     *        ->就会应用到当前RedisCacheManager管理的所有缓存分区中
     *
     *    1.开启缓存功能     配置spring.cache.type: redis  启动类加@EnableCaching
     *    2.只需要使用注解就能完成缓存操作
     *    使用springcache对获取一级菜单加缓存
     *        //每一个需要缓存的数据都需要指定放到哪个名字的缓存(缓存的分区，按照业务类型分)
     *     @Cacheable ({ " category " })   //代表当前方法的结果需要缓存  如果缓存中有，那该方法就不会被调用，如果缓存中没有就调用该方法，最后将方法的结果放入缓存
     *     位置：com.atcode.watermall.product.service.impl.CategoryServiceImpl
     *    3.默认行为：
     *       1.缓存中有，那么不调用这个被注解的方法；缓存中没有，调用该方法把数据存到缓存中
     *       2.key自动生成： 缓存的名字 :: SimpleKey[](自主生成的key)
     *       3.缓存的value的值：默认使用jdk序列化机制，将序列化后的结果存到redis
     *       4.默认ttl时间：-1(永不过期)
     *    4.自定义：
     *            1. 指定生成的缓存使用的key  key属性指定，接收一个SpEl表达式
     *                 @Cacheable (value = {"category"},key = "#root.method.name") value用数组是因为可以放入多个缓存区，比如分类区和product区
     *                 @Cacheable (value = {"category"},key = "'Level1Categorys'")  key直接传字符串
     *            2. 指定缓存的数据的存活时间
     *            3. 将数据保存为json格式  位置：com.atcode.watermall.product.config.MyCacheConfig
     *            配置信息：spring:
     *                           cache:
     *   	                    #指定缓存类型为redis
     *                           type: redis
     *                          redis:
     *                          # 指定redis中的过期时间为1h
     *                           time-to-live: 3600000
     *                           key-prefix: CACHE_   #缓存key前缀
     *                           use-key-prefix: true #是否开启缓存key前缀
     *                           cache-null-values: true #缓存空值，解决缓存穿透问题
     *
     */




    /**
     * SpringCache原理与不足
     * 1、读模式
     * 缓存穿透：
     *
     * 查询一个null数据。解决方案：缓存空数据，可通过spring.cache.redis.cache-null-values=true
     * 缓存击穿：
     *
     * 大量并发进来同时查询一个正好过期的数据。解决方案：加锁 ? 默认是无加锁的;
     * 使用sync = true来解决击穿问题
     * 缓存雪崩：
     *
     * 大量的key同时过期。解决：加随机时间。
     * 2、写模式：（缓存与数据库一致）
     * 读写加锁。
     *
     * 引入Canal，感知到MySQL的更新去更新Redis
     *
     * 读多写多，直接去数据库查询就行
     *
     * 3、总结
     * 常规数据（读多写少，即时性，一致性要求不高的数据，完全可以使用Spring-Cache）：
     * 写模式(只要缓存的数据有过期时间就足够了)
     * 特殊数据：
     * 特殊设计（读写锁等）
     */


}
