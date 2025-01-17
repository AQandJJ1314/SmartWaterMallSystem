package com.atcode.watermall.product.web;


import com.atcode.watermall.product.entity.CategoryEntity;
import com.atcode.watermall.product.service.CategoryService;
import com.atcode.watermall.product.vo.Catalog2Vo;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
public class IndexController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @GetMapping({"/","index.html"})
//参数是springMvc提供的接口，Model类，给这个类的对象里放的数据就会存到页面请求域中
    public String indexPage(Model model){    //传参Model类
        System.out.println("===================进入了productindex方法===============");
        // TODO 1、查出所有1级分类
        List<CategoryEntity> categoryEntities = categoryService.getLevel1Categorys();
        model.addAttribute("categories",categoryEntities);
//视图解析器进行拼串，前缀classpath:/templates/返回值.html
        return "index";    //相当于return "classpath:/templates/index.html"; 拦截GetMapping路径后转到首页
    }

    /**
     * 查出三级分类
     * 1级分类作为key，2级引用List
     */
    @ResponseBody
    @GetMapping("/index/json/catalog.json")
    public Map<String, List<Catalog2Vo>> getCatalogJson() {
        Map<String, List<Catalog2Vo>> map = categoryService.getCatalogJson();
        return map;
    }
    @ResponseBody
    @GetMapping("/hello")
    public String hello() {
        //1.调用getLock获取一把锁，只要锁的名字一样就是同一把锁
        RLock lock = redissonClient.getLock("my-lock");

        //2.加锁   阻塞式等待  默认加的锁都是30s
        // 锁会自动续期，如果业务超长，运行期间自动给锁续上新的30s，不用担心业务时间长，锁过期被删掉
        // 加锁的业务只要运行完成，锁就不会自动续期，锁也会默认在30s以后自动删除
//        lock.lock();
        lock.lock(30, TimeUnit.SECONDS);
        //给锁指定了过期时间，那么就不会有看门狗机制，但是要保证锁的过期时间必须完全大于业务时间
        //如果给锁指定了过期时间，就发送Lua脚本给Redis，过期时间就是指定的时间
        //如果未指定锁的超时时间，就使用30 * 1000(看门狗默认时间LockWatchdogTimeout())
        //只要占锁成功就会启动一个定时任务，给redis发Lua脚本重新指定过期时间，新的过期时间就是看门狗的默认时间
        //internalLockLeaseTime / 3,  1/3的看门狗时间之后续期
        /**
         *         1.给锁指定了过期时间，那么就不会有看门狗机制，但是要保证锁的过期时间必须完全大于业务时间
         *         2.如果给锁指定了过期时间，就发送Lua脚本给Redis，过期时间就是指定的时间
         *         3.如果未指定锁的超时时间，就使用30 * 1000(看门狗默认时间LockWatchdogTimeout())
         *         4.只要占锁成功就会启动一个定时任务，给redis发Lua脚本重新指定过期时间，新的过期时间就是看门狗的默认时间
         *         5.internalLockLeaseTime / 3,  1/3的看门狗时间之后续期
         */
        try{
            System.out.println("加锁成功，执行业务....."+"当前线程："+Thread.currentThread().getId());
            Thread.sleep(30000);
        }catch (Exception e){

        }
        finally {
            //3.解锁   假设解锁代码没有运行，redisson是否会出现死锁
            System.out.println("释放锁....."+"当前线程："+Thread.currentThread().getId());
            lock.unlock();
        }

        return "hello";
    }


    /**
     * 读写锁，读和写操作为互斥操作，这样做可以避免读到脏数据，修改期间写锁是排他锁(互斥锁)，读锁是一个共享锁
     * 在有一个线程写的时候其余线程均不能读，必须等写操作进行完成之后才可以读
     * 读 + 读：相当于无锁，并发读，只会在redis中记录好当前的读锁，他们都会同时加锁成功
     * 写 + 读： 等待写锁释放
     * 写 + 写：  阻塞
     * 读 + 写： 等待读锁释放  Thread.sleep(30000);
     * @return
     */
    @GetMapping("/write")
    @ResponseBody
    public String writeValue() {
        String s = "";
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock("rw-lock");
        RLock lock = readWriteLock.writeLock();
        try {
            //1.写数据加写锁
            lock.lock();
            System.out.println("写锁加锁成功......+当前线程id:   "+Thread.currentThread().getId());
            s = UUID.randomUUID().toString();
            stringRedisTemplate.opsForValue().set("writeValue",s);
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
            System.out.println("写锁解锁锁成功......+当前线程id:   "+Thread.currentThread().getId());
        }
        return s;
    }


    @ResponseBody
    @GetMapping("/read")
    public String readValue() {
        String s = "";
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock("rw-lock");
        RLock lock = readWriteLock.readLock();
        try {
            lock.lock();
            System.out.println("读锁加锁成功......+当前线程id:   "+Thread.currentThread().getId());
//            Thread.sleep(30000);
            //读数据加读锁
           s = stringRedisTemplate.opsForValue().get("writeValue");
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
            System.out.println("读锁解锁成功......+当前线程id:   "+Thread.currentThread().getId());
        }
        return s;
    }


    /**
     * 信号量测试，车库停车
     * 获取
     *  RSemaphore park = redissonClient.getSemaphore("park");
     *  park.acquire();//获取一个信号量，占用一个车位   阻塞式方法，必须停
     *  //boolean b = park.tryAcquire();//非阻塞式方法，可以不停，也就是可以没有信号量
     *  释放
     *   RSemaphore park = redissonClient.getSemaphore("park");
     *   park.release();//释放一个信号量，释放一个车位
     *
     */
    @GetMapping("/park")
    @ResponseBody
    public String park() throws InterruptedException {
        RSemaphore park = redissonClient.getSemaphore("park");
            park.acquire();//获取一个信号量，占用一个车位   阻塞式方法，必须停
//        boolean b = park.tryAcquire();//非阻塞式方法，可以不停，也就是可以没有信号量

//        return "ok=>"+ b;
        return "ok";
    }

    @GetMapping("/go")
    @ResponseBody
    public String go() throws InterruptedException{
        RSemaphore park = redissonClient.getSemaphore("park");

            park.release();//释放一个信号量，释放一个车位

        return "ok";
    }


    /**
     * 闭锁示例(CountDownLatch)
     * 学校放假锁门
     * 比如有12345五个班，只有五个班的同学全部走完才能锁门
     *
     *   RCountDownLatch door = redissonClient.getCountDownLatch("door");
     *         door.trySetCount(5);
     *         door.await();  //等待闭锁都完成
     *
     *         return "放假了...";
     *
     *  RCountDownLatch door = redissonClient.getCountDownLatch("door");
     *         door.countDown();  //计数-1
     *
     *         return id+"班的人都走了...";
     *
     */
    @GetMapping("/lockDoor")
    @ResponseBody
    public String lockDoor() throws InterruptedException {

        RCountDownLatch door = redissonClient.getCountDownLatch("door");
        door.trySetCount(5);
        door.await();  //等待闭锁都完成

        return "放假了...";
    }

    @GetMapping("/gogogo/{id}")
    @ResponseBody
    public String gogogo(@PathVariable("id") Long id){
        RCountDownLatch door = redissonClient.getCountDownLatch("door");
        door.countDown();  //计数-1

        return id+"班的人都走了...";

    }
}
