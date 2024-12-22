# SmartWaterMallSystem
智能水产品商城系统


nacos闪退解决方法：
startup -m standalone

/**
* 想要远程调用别的服务
* 1.引入open-feign
* 2.编写一个接口告诉springcloud这个接口需要调用远程服务 统一放在fegin包下
*    声明接口的每一个方法都是调用哪个远程服务的哪个请求
* 3.开启远程调用的功能  @EnableFeignClients
  */

20241126下午存在的问题： nacos配置中心内容修改之后只能在服务(coupon)重新启动之后才生效
nacos作为配置中心:以coupons服务为例
com.atcode.watermall.coupon.WatermallCouponApplication
/**
* 1、如何使用nacos作为配置中心统一管理配置
*     1)、引入依赖
*             <dependency>
*                 <groupId>com.alibaba.cloud</groupId>
*                 <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
*             </dependency>
*     2)、创建一个bootstrap.properties
*          #服务名称
*          spring.application.name=watermall-coupon
*          #配置中心的地址
*          spring.cloud.nacos.config.server-addr=127.0.0.1:8848
*     3)、需要给配置中心默认添加一个叫数据集（Data Id）watermall-coupon.默认规则，(应用名.properties)
*     4)、给 应用名.properties 添加任何配置
*     5）、动态获取配置
*          @RefreshScope:动态获取并刷新配置
*          @Value("${配置项的名}")：获取到配置
*          如果配置中心和当前应用的配置文件中都配置了相同的项，优先使用配置中心的配置
* 2、细节
*      1)、命名空间，配置隔离
*          默认：public（保留空间）；默认新增的所有配置都在public空间
*          1、开发、测试、生产;利用命名空间做环境隔离
*              注意：在bootstrap.properties配置上，需要使用哪个命名空间下的配置
*              spring.cloud.nacos.config.namespace=65875baf-002a-4bd1-a3b8-9547b7ba1fe2
*          2、每一个微服务之间互相隔离配置，每一个微服务都创建自己的命名空间，只加载自己命名和空间下的所有配置
*      2)、配置集：所有配置的集合
*      3)、配置集ID：类似文件名
*          DataID:类似文件名
*      4)、配置分组
*          默认所有的配置集都属于：DEFAULT_GROUP
*      项目中的使用：每个微服务创建自己的命名空间，使用配置分组区分的环境，dev,test,prop
*  3、同时加载多个配置集
*      1)、微服務任何配置信息，任何配置文件都可以放在配置中心中
*      2)、只需要在bootstrap.properties说明加载配置中心哪些配置文件即可
*      3)、@Value、@ConfigurationPropeties...以前SpringBoot任何方法从配置文件中获取，都能使用配置中心有的优先使用配置中心的
*/


配置网关：
  解决datasource冲突问题：
    @SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
  解决Web冲突问题：
     在properties文件加入 spring.main.web-application-type=reactive


JAVA8新特性内容，在创建查询三级目录时的serviceimpl层
  List<CategoryEntity> level1Menu = entities.stream().filter((categoryEntity) -> {
  return categoryEntity.getParentCid() == 0;
   }).collect(Collectors.toList());

解决lombok插件失效的问题：使用标准的javabean

# 路由id，自定义，只要唯一即可
        - id: admin_route
# uri路由的目标地址。lb就是负载均衡，后面跟服务名称。
          uri: lb://renren-fast
          #断言工厂的Path，请求路径必须符合指定规则
          predicates:
            - Path=/api/**    # 把所有api开头的请求都转发给renren-fast
          #局部过滤器。回顾默认过滤器default-filters是与routes同级
          filters:
#路径重写。逗号左边是原路径，右边是重写后的路径
- RewritePath=/api/(?<segment>.*),/renren-fast/$\{segment}
# 默认规则， 请求过来：http://localhost:88/api/captcha.jpg   转发-->  http://renren-fast:8080/renren-fast/captcha.jpg


配置跨域的关键代码：
public CorsWebFilter corsWebFilter(){
UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfiguration= new CorsConfiguration();
        //1、配置跨域
        // 允许跨域的请求头
        corsConfiguration.addAllowedHeader("*");
        // 允许跨域的请求方式
        corsConfiguration.addAllowedMethod("*");
        // 允许跨域的请求来源
        corsConfiguration.addAllowedOriginPattern("*");
//注释的这句会报错。因为当allowCredentials为真时，allowedorigin不能包含特殊值"*"，因为不能在"访问-控制-起源“响应头中设置该值。

//corsConfiguration.addAllowedOrigin("*");//这句会报错

// 是否允许携带cookie跨域

corsConfiguration.setAllowCredentials(true);


        // 任意url都要进行跨域配置，两个*号就是可以匹配包含0到多个/的路径
        source.registerCorsConfiguration("/**",corsConfiguration);
        return new CorsWebFilter(source);

    }



注意: application.yaml文件中配置的是服务的命名空间，bootstrap.properties文件中配置的是配置中心的命名空间
      两者不一样！！！！！

  统一封装错误状态码
正规开发过程中，错误状态码有着严格的定义规则
错误码和错误信息定义类：
1. 错误码定义规则为五位数字
2. 前两位表示业务场景，最后三位表示错误码。例如：100001。10:通用 001:系统未知异常
3. 维护错误码后需要维护错误描述，将他们定义为枚举形式
   //错误码和错误信息定义类：
  
   //        1. 错误码定义规则为五位数字

   //        2. 前两位表示业务场景，最后三位表示错误码。例如：100001。10:通用 001:系统未知异常
  
   //        3. 维护错误码后需要维护错误描述，将他们定义为枚举形式

   //        错误码列表（前两位业务场景）：

   //         10: 通用
   //             如10001：参数格式校验失败

   //         11: 商品

   //         12: 订单

   //         13: 购物车

   //         14: 物流


JSR303
/**
* JSR303
* 1.给bean添加javax.validation.constraints.* ,并定义自己的message提示
* 2.开启校验功能@Valid
* 3.校验的Bean后紧跟一个BindingResult,就可以获取到校验的结果  此参数能够得到异常的信息
  */
* 
  //@RequestMapping("/save")
  
* //    public R save(@Valid @RequestBody BrandEntity brand, BindingResult result){
  
* //        if (result.hasErrors()){
 
* //            Map<String, String> map = new HashMap<>();
  
* //            //1、获取校验的结果
 
* //            result.getFieldErrors().forEach((item)->{
  
* //                //获取到错误提示
 
* //                String message = item.getDefaultMessage();
  
* //                //获取到错误属性的名字(校验错误的字段)
 
* //                String field = item.getField();
  
* //                map.put(field, message);
  
* //            });
  
* //            return R.error().put("data", map);
  
* //        }else{
  
* //            brandService.save(brand);
 
* //        }
  
* //        return R.ok();
  
* //    }
 
* 参数校验异常
/** 位置 product/entity/BrandEneity.class
* 关于参数校验异常
* @NotEmpty和@NotBlank区别：
* @NotEmpty非空，不能是null和""
* @NotBlank 非空白，不能是null和""和"  "
* 自定义校验规则@Pattern(regexp = "^[a-zA-Z]$", message = "检索首字母必须是一个字母")
* 1.要给实体类对应的属性添加注解，在注解上也可以自定义message
* 2.在Controller层也要添加注解 @Valid
* 
  */
* {"name": "aa", "logo": "https://www.baidu.com","sort":"1","firstLetter":"A"}

  统一异常处理
/**  位置 com.atcode.watermall.product.exception.WatermallExceptionControllerAdvice
* 统一的异常处理  使用SpringMVC提供的 @ControllerAdvice
* 集中处理所有异常
* @ResponseBody 该注解用于返回json的数据
* @RestControllerAdvice =  @ControllerAdvice + @ResponseBody
* @ExceptionHandler(value = MethodArgumentNotValidException.class)  
* 上一个注解能让SpringMVC知道 此异常处理器类能处理什么异常 value 初始可以用Exception.class，测试获得异常类型
* 捕获到异常之后可以用更详细的  MethodArgumentNotValidException
* bindingResult.getFieldErrors() 获得数据校验的异常的结果
* handleVaildException方法相当于一个精确的异常处理，当这个方法不能捕获到异常时，采用下面的更大范围的异常处理方法handleException
  */


分组校验

位置  product/entity/BrandEneity.class
  分组校验 (JSR303提供的功能)，增改校验分开，@Validated (spring框架提供的规范)  可以完成多场景的复杂校验

    1.@NotBlank(message = "品牌名必须提交",groups = {AddGroup.class,UpdateGroup.class})
      给校验注解标注什么情况需要进行校验
    2.@Validated({AddGroup.class})
    3.默认没有指定分组的校验注解，在分组校验的情况下不生效

  场景：新增时id必须为空，修改时id必须非空，这样实体类注解就凌乱了，这时就必须用到分组校验。

  注意：在common模块中valid包里新建空接口AddGroup,UpdateGroup用来分组

  实体类变量注解分组，代表此变量支持这些分组的校验。
  controller参数对象注解分组，代表这个对象只校验有配置这个分组的成员变量。

自定义校验  

  1.编写一个自定义的校验注解   @ListValue(vals={0,1},groups = {AddGroup.class, UpdateStatusGroup.class})
 
  2.编写一个自定义的校验器

  3.关联自定义的校验器和自定义的校验注解 @Constraint(validatedBy = {ListValueConstraintValidator.class})

示例：

@Documented
//约束。同一个注解可以指定多个不同的校验器，适配不同类型的校验。这里ListValueConstraintValidator.class是数值校验器

@Constraint(validatedBy = {ListValueConstraintValidator.class})
//可以标注在哪些位置。方法、字段等。

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})

//注解的时机。这里是可以在运行时获取校验

@Retention(RetentionPolicy.RUNTIME)
public @interface ListValue {

//    校验出错后，错误信息去哪取。前缀一般是当前全类名，在ValidationMessages.properties配置文件里设置com.atcode.common.validator.ListValue.message=The specified value must be submitted

String message() default "{com.atcode.common.validator.ListValue.message}";
//    支持分组校验的功能

Class<?>[] groups() default {};

//    自定义负载信息

Class<? extends Payload>[] payload() default {};

//    自定义注解里的属性

int[] vals() default {};
}


    关于路径变量注解：@PathVariable
    /**
     * 列表
     */
    @RequestMapping("/list/{catlogId}")
    //@RequiresPermissions("product:attrgroup:list")
    public R list(@RequestParam Map<String, Object> params, @PathVariable("catlogId") Long catlogId){
//        PageUtils page = attrGroupService.queryPage(params);
PageUtils page = attrGroupService.queryPage(params,catlogId);

        return R.ok().put("page", page);
    }

解决前端级联菜单渲染问题： com.atcode.watermall.product.entity.CategoryEntity
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
com.atcode.watermall.product.service.impl.AttrServiceImpl
        /**
         *分页数据中加入当前属性的“所属分类”和“所属分组”
         * 报错原因，selectone可能会出现空指针异常  优化方法是使用!null判断
         * sql优化代码
         */

        //TODO 下面的selectOne位置有bug，需要接收的是一条，但是查到的不是一条   //bug原因，属性分组关系表中只能是一对一的关系  也有可能是下面的代码原因，使用1/0即使是一对多也不会报错
        /**
         * wrapper.eq(AttrEntity::getAttrType,"base".equalsIgnoreCase(attrType)? ProductConstant.AttrEnum.ATTR_TYPE_BASE : ProductConstant.AttrEnum.ATTR_TYPE_SALE);
         */
位置:com.atcode.watermall.product.app.CategoryBrandRelationController
    /**
     * 获取当前分类关联的所有品牌
     * 1、 Controller: 处理请求，接受和校验数据
     * 2、Service接受controller传来的数据，进行业务处理
     * 3、Controller接受Service处理完的数据，封装成页面指定的vo
     */
.common模块创建to，用于不同服务间传数据

TO:Transfer Object 数据传输对象
在应用程序不同关系之间传输的对象。



测试领取采购单的接口
POST http://localhost:88/api//ware/purchase/received
Content-Type: application/json

[3]

参数为采购单id
<> 2024-12-12T163748.200.json



测试采购完成的接口
POST http://localhost:88/api/ware/purchase/done
Content-Type: application/json

{
"id": 3,
"items": [
{"itemId":3,"status":3,"reason":"完成"},
{"itemId":4,"status":3,"reason":"完成"}
]
}


itemid为采购需求的id
<> 2024-12-13T152023.200.json


商城首页 
    模板引擎 thymeleaf-stater 关闭缓存，配置默认启动扫描路径
    静态资源在static文件夹下，可以通过路径直接访问到
    页面在templates文件夹下，也可以直接访问
    SpringBoot访问项目的时候默认会访问Index
  不重启服务器的情况下更新数据
    1.引入devtool
    <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
            <optional>true</optional>
        </dependency>
    2.重新编译当前页面ctrl+shift+F9

.JMeter压力测试
压力测试考察当前软硬件环境下系统所能承受的最大负荷并帮助找出系统瓶颈所在。 压测都是为了系统在线上的处理能力和稳定性维持在一个标准范围内， 做到心中有数。

使用压力测试， 我们有希望找到很多种用其他测试方法更难发现的错误。 有两种错误类型是:内存泄漏， 并发与同步问题。

内存泄漏：内存泄漏（Memory Leak）是指程序中已动态分配的堆内存由于某种原因程序未释放或无法释放，造成系统内存的浪费，导致程序运行速度减慢甚至系统崩溃等严重后果。

并发与同步：

有效的压力测试系统将应用以下这些关键条件:重复， 并发， 量级， 随机变化。

1.1 压力测试的性能指标
响应时间（Response Time:RT）
响应时间指用户从客户端发起一个请求开始，到客户端接收到从服务器端返回的响应结束，整个过程所耗费的时间。响应时间越少越好。

HPS（Hits Per Second）:每秒点击次数，单位是次/秒。【不是特别重要】

TPS(Transaction per Second）:系统每秒处理交易数，单位是笔/秒。

Qps(Query per Second）:系统每秒处理查询次数，单位是次/秒。
对于互联网业务中，如果某些业务有且仅有一个请求连接，那么TPS=QPS=HPS，一般情况下用 TPS来衡量整个业务流程，用QPS来衡量接口查询次数，用HPS来表示对服务器单击请求。

无论TPS、QPS、HPS,此指标是衡量系统处理能力非常重要的指标，越大越好，根据经
验，一般情况下:
金融行业:1000TPS~5000OTPS，不包括互联网化的活动
保险行业:100TPS~10000OTPS，不包括互联网化的活动
制造行业:10TPS~5000TPS
互联网电子商务:1000OTPS~1000000TPS
互联网中型网站:1000TPS~50000TPS
互联网小型网站:50OTPS~10000TPS

最大响应时间（MaxResponse Time）指用户发出请求或者指令到系统做出反应(响应)的最大时间。

最少响应时间(Mininum ResponseTime）指用户发出请求或者指令到系统做出反应(响应）的最少时间。

90%响应时间（90%Response Time）是指所有用户的响应时间进行排序，第90%的响应时间。

从外部看，性能测试主要关注如下三个指标
吞吐量:每秒钟系统能够处理的请求数、任务数。
响应时间:服务处理一个请求或一个任务的耗时。
错误率。一批请求中结果出错的请求所占比例。

结果分析 :

有错误率同开发确认， 确定是否允许错误的发生或者错误率允许在多大的范围内；

Throughput 吞吐量每秒请求的数大于并发数， 则可以慢慢的往上面增加； 若在压测的机器性能很好的情况下， 出现吞吐量小于并发数， 说明并发数不能再增加了， 可以慢慢的往下减， 找到最佳的并发数；

压测结束， 登陆相应的 web 服务器查看 CPU 等性能指标， 进行数据的分析;

最大的 tps：不断的增加并发数， 加到 tps 达到一定值开始出现下降， 那么那个值就是
最大的 tps。

最大的并发数： 最大的并发数和最大的 tps 是不同的概率， 一般不断增加并发数， 达到一个值后， 服务器出现请求超时， 则可认为该值为最大的并发数。

压测过程出现性能瓶颈， 若压力机任务管理器查看到的 cpu、 网络和 cpu 都正常， 未达 到 90%以上， 则可以说明服务器有问题， 压力机没有问题。

影响性能考虑点包括：数据库、 应用程序、 中间件（tomact、 Nginx） 、 网络和操作系统等方面

首先考虑自己的应用属于 CPU 密集型（以空间换时间）还是 IO 密集型（以时间换空间）

压测结果总结
中间件对性能的影响
SQL 耗时越小越好， 一般情况下微秒级别。
命中率越高越好， 一般情况下不能低于 95%。
锁等待次数越低越好， 等待时间越短越好。

中间件越多， 性能损失越大， 大多都损失在网络交互了；
业务期间需要考虑的问题：
数据库（MySQL 优化）
模板的渲染速度（开发时关闭缓存，上线后一定要缓存）
静态资源


绕过Nginx的静态资源，只让tomcat处理动态请求，模拟服务器因处理不了过多请求宕机
解决办法

调大堆内存

-Xmx1024m -Xms1024m -Xmn512m

-Xms :初始堆大小
-Xmx :最大堆大小
-Xmn :堆中新生代初始及最大大小

优化查询三级分类业务
位置：com.atcode.watermall.product.service.impl.CategoryServiceImpl


1 缓存与分布式锁
1.1 缓存
为了系统性能的提升， 我们一般都会将部分数据放入缓存中， 加速访问。 而 db 承担数据落盘工作。

1.1.1 哪些数据适合放入缓存
即时性、 数据一致性要求不高的
访问量大且更新频率不高的数据（读多， 写少）
举例： 电商类应用， 商品分类， 商品列表等适合缓存并加一个失效时间(根据数据更新频率来定)， 后台如果发布一个商品， 买家需要 5 分钟 才能看到新的商品一般还是可以接受的。

注意:在开发中， 凡是放入缓存中的数据我们都应该指定过期时间， 使其可以在系统即使没有主动更新数据也能自动触发数据加载进缓存的流程。 避免业务崩溃导致的数据永久不一致问题


com.atcode.watermall.product.service.impl.CategoryServiceImpl
压测内存泄露及解决
OutOfMemoryError
原因
springboot2.0以后默认使用lettuce作为操作redis的客户端。他使用netty进行网络通信
lettuce的bug导致netty堆外内存溢出 -Xmx300m；netty如果没有指定堆外内存，默认使用-Xmx300m，跟jvm设置的一样【迟早会出异常】
不能使用-Dio.netty.maxDirectMemory调大堆外内存，迟早会出问题。

    //TODO OutOfMemoryError  产生堆外内存异常  此性能未做压测，不清楚是否高版本已修复，后续做压测验证

    /**
     *    //springboot2.0以后默认使用lettuce作为redis的客户端，它使用netty进行网络通信
     *    //lettuce的bug导致netty的堆外内存溢出 -Xmx300 netty如果没有指定堆外最大内存，默认使用-Xmx300
     *    不能使用-Dio.netty.maxDirectMemory调大堆外内存，迟早会出问题。
     *    解决方案
     *      升级lettuce客户端（推荐）；【2.3.2已解决】【lettuce使用netty吞吐量很大】
     *      切换使用jedis客户端
     */

  高并发下缓存失效问题
   缓存穿透
缓存穿透：指查询一个数据库和缓存库都不存在的数据，每次查询都要查缓存库和数据库，一秒钟查一万次就要访问一万次数据库，
  这将导致数据库压力过大。如果我们在第一次查的时候就将查到的null加入缓存库并设置过期时间，这时一秒钟查一万次都不会再查数据库了，因为缓存库查到值了。

风险：利用不存在的数据进行攻击，数据库瞬时压力增大，最终导致崩溃缓存

解决：

采用：数据库查的空值放入缓存，并加入短暂过期时间
布隆过滤器（请求先查布隆过滤器、再查缓存库、数据库）

   缓存雪崩
缓存雪崩：缓存雪崩是指在我们设置缓存时key采用了相同的过期时间，导致缓存在某一时刻同时失效，请求全部转发到DB，DB瞬时压力过重雪崩。

解决：

原有的失效时间基础上增加一个随机值，比如1-5分钟随机，这样每一个缓存的过期时间的重复率就会降低，就很难引发集体失效的事件。
降级和熔断
采用哨兵或集群模式，从而构建高可用的Redis服务
如果已经发生缓存血崩：熔断、降级

  缓存击穿 【分布式锁】
一条数据过期了，还没来得及存null值解决缓存穿透，高并发情况下导致所有请求到达DB
解决：加分布式锁,获取到锁，先查缓存，其他人就有数据，不用去DB

  缓存击穿：区别于缓存穿透，穿透是数据库和缓存都无数据，击穿是缓存的热点key刚好失效
对于一些设置了过期时间的key，如果这些key可能会在某些时间点被超高并发地访问，是一种非常“热点”的数据。
如果这个key在大量请求同时进来前正好失效，那么所有对这个key的数据查询都落到db，我们称为缓存击穿
解决：

采用：加互斥锁。大量并发只让一个去查，其他人等待，查到以后释放锁，其他人获取到锁，先查缓存，就会有数据，不用去db
热点数据不设置过期时间



//只要是同一把锁，就能锁住需要这个锁的所有线程
//1、 synchronized (this) SpringBoot所有组件在容器中都是单例的   this是当前进程下的一个锁对象，因为springboot的对象是单例的，所以一个进程只有一个this锁对象
//TODO 本地（进程）锁，synchronized,JUC(Lock)只锁当前的进程的资源（实例对象/方法） ，分布式情况下想要锁住所有就得用分布式锁

  使用synchronized对查询数据库操作加锁，最终可能导致多次查询数据库，
  原因是在redis缓存保存数据的这个期间有其他的线程判断缓存为空拿到了锁，再次去查询数据库

解决方法：使用更大范围的锁，锁住存入缓存的这个操作

位置：com.atcode.watermall.product.service.impl.CategoryServiceImpl
  分布式锁的业务代码，但是仍然存在问题，如果执行过程中出现异常会导致无法删锁，其余线程全部等待 
  如果使用try catch finally处理，如果正常执行但是机器断电，也会出现锁无法释放问题
    解决方案，设置自动过期时间     stringRedisTemplate.expire("lock",30,TimeUnit.SECONDS);
   //1.占用分布式锁 redis
   Boolean lock = stringRedisTemplate.opsForValue().setIfAbsent("lock", "111");
    if(lock){
     //加锁成功....执行业务
      Map<String, List<Catalog2Vo>> dataFromDb = getDataFromDb();
      //释放锁
       stringRedisTemplate.delete("lock");
        return dataFromDb;
         }else {
        //加锁失败....重试
        //休眠100mm重试
         return getCatalogJsonFromDBWithRedisLock();//自旋的方式等待 
       }

   如果拿到锁之后刚好机器断电导致未设置上过期时间，同样会导致死锁  解决方案：使得加锁和设置过期时间作为一个原子命令，也就是同步的
   Boolean lock = stringRedisTemplate.opsForValue().setIfAbsent("lock", "111",300,TimeUnit.SECONDS);
   
   加锁问题解决之后删锁依然可能会存在问题
   如果由于业务时间很长导致设置的过期时间已经过期了，直接删除(按照key删)，可能会导致删除别人正在持有的锁
   解决方案：占锁的时候，值指定为uuid，每个线程匹配的是自己的锁才删除
   String uuid = UUID.randomUUID().toString();
   Boolean lock = stringRedisTemplate.opsForValue().setIfAbsent("lock", uuid,300,TimeUnit.SECONDS);
   //判断是自己的锁才可以删除
   if(uuid.equals(lockValue)){ stringRedisTemplate.delete("lock"); } 
   依然存在问题：如果在请求到redis判断lock的途中 锁过期了，假如是10s过期，9.5的时候请求，请求到了返回的途中，redis的lock过期了并且有其他线程的lock进入，此时删除的就不是自己的锁
   也就是说返回的值是当前线程的值没错，但是实际上redis中的值已经变了，所以此时删除就会出现问题
 
   解决办法：使得删锁的两步变成原子操作  获取值对比+对比成功删除  需要成为原子操作  使用redis+Lua脚本完成  代码：
     
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

总结：redis做分布式锁的两个核心是加锁和解锁都保证原子性

redission锁官方文档
https://github.com/redisson/redisson/wiki/1.-%E6%A6%82%E8%BF%B0

Redisson分布式锁
1.5.1 概念
在分布式情况下，之前学过的锁“syncronized” 、“lock”和JUC包下的类都用不成了。需要用Redission分布式锁。

Redisson是一个在Redis的基础上实现的Java驻内存数据网格（In-Memory Data Grid）。它不仅提供了一系列的分布式的Java常用对象，还提供了许多分布式服务。其中包括(BitSet, Set, Multimap, SortedSet, Map, List, Queue, BlockingQueue, Deque, BlockingDeque, Semaphore, Lock, AtomicLong, CountDownLatch, Publish / Subscribe, Bloom filter, Remote service, Spring cache, Executor service, Live Object service, Scheduler service)

Redisson提供了使用Redis的最简单和最便捷的方法。Redisson的宗旨是促进使用者对Redis的关注分离（Separation of Concern），从而让使用者能够将精力更集中地放在处理业务逻辑上。

本文我们仅关注分布式锁的实现，更多请参考Redisson官方文档

整合redisson作为分布式锁等功能的框架
 1.引入依赖
 2.配置redisson

位置：com.atcode.watermall.product.config.MyRedissonConfig
    /**
     * 所有对Redisson的操作都是通过RedissonClient对象
     * @return
     */3
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient(){
        //1.创建配置
        Config config = new Config();
        //设置单节点模式，设置redis地址。ssl安全连接redission://127.0.0.1:6379
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        //2.根据Config创建出RedissonClient实例
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }
关于对可重入锁和不可重入锁的理解：如果有两个方法(业务)A,B,B在A中,也就是要在A中去执行B
                            如果是可重入锁：那么对于A,A拿到一锁,此时B可以直接拿来A拿到的一锁来用，
                            如果是不可重入锁，那么就会出现B一直等待A释放锁之后拿到锁且A等B执行完才要释放的情况，导致死锁

测试redisson的分布式锁

    @ResponseBody
    @GetMapping("/hello")
    public String hello() {
        //1.调用getLock获取一把锁，只要锁的名字一样就是同一把锁
        RLock lock = redissonClient.getLock("my-lock");

        //2.加锁   阻塞式等待
        lock.lock();
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

lock()方法的两大特点：

1、会有一个看门狗机制，在我们业务运行期间，将我们的锁自动续期

2、为了防止死锁，加的锁设置成30秒的过期时间，不让看门狗自动续期，如果业务宕机，没有手动调用解锁代码，30s后redis也会对他自动解锁。

上述方法依然存在问题：不指定锁的过期时间，默认30s，每到20s看门狗会自动续期成30s，有死锁风险

指定锁的过期时间，看门狗不会自动续期   在自定义锁的存在时间时不会自动解锁
lock.lock(30, TimeUnit.SECONDS);

注意：
设置的自动解锁时间一定要稳稳地大于业务时间

        /**
         *         1.给锁指定了过期时间，那么就不会有看门狗机制，但是要保证锁的过期时间必须完全大于业务时间
         *         2.如果给锁指定了过期时间，就发送Lua脚本给Redis，过期时间就是指定的时间
         *         3.如果未指定锁的超时时间，就使用30 * 1000(看门狗默认时间LockWatchdogTimeout())
         *         4.只要占锁成功就会启动一个定时任务，给redis发Lua脚本重新指定过期时间，新的过期时间就是看门狗的默认时间
         *         5.internalLockLeaseTime / 3,  1/3的看门狗时间之后续期
         */

模拟读写锁
  一起读数据的请求互不影响，要是有一个在写，那其他的线程就不能读，得等写锁释放之后才能读，一般这两个锁都是成对出现的
位置：com.atcode.watermall.product.web.IndexController

         @GetMapping("/write")
    @ResponseBody
    public String writeValue() {
        String s = "";
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock("rw-lock");
        RLock lock = readWriteLock.writeLock();
        try {
            //1.写数据加写锁
            lock.lock();
            s = UUID.randomUUID().toString();
            stringRedisTemplate.opsForValue().set("writeValue",s);
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
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
            //读数据加读锁
           s = stringRedisTemplate.opsForValue().get("writeValue");
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
        return s;
    }


    /**
     * 读写锁，读和写操作为互斥操作，这样做可以避免读到脏数据，修改期间写锁是排他锁(互斥锁，独享锁)，读锁是一个共享锁
     * 在有一个线程写的时候其余线程均不能读，必须等写操作进行完成之后才可以读
     * 读 + 读：相当于无锁，并发读，只会在redis中记录好当前的读锁，他们都会同时加锁成功
     * 写 + 读： 等待写锁释放
     * 写 + 写：  阻塞
     * 读 + 写： 等待读锁释放  Thread.sleep(30000);
     * @return
     */
读写锁总结：只要有写的存在，必须等待

信号量 ： 可以用作限流，比如有10000个并发，信号量也有10000个，刚好被占满之后又有新的线程要来，
         规定拿到信号量才能进行下一步业务操作，那么此时可以使用tryAcquire方法返回一个false提示稍后再试

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

闭锁  必须把闭锁中的要求全部执行完才可以执行闭锁的代码

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

分布式锁的范围的名字：  需要根据场景去判断加锁的范围，并不是越细越好

        /**
         * 锁的名字，锁的粒度，越细越快
         * 锁的粒度：具体缓存的是某个数据，比如说11号商品 可以用product-11-lock,12号用product-12-lock
         * 如果上述两个商品用同一把锁，product-lock，那么如果11号商品是一千以内的并发，12号商品是百万级别的并发
         * 就会出现11号锁等待12号锁释放了才拿到的尴尬情况
         */

缓存数据一致性的解决方式以及会存在的问题

    /**
     * 缓存中的数据如何和数据库中的数据保持一致
     * 缓存数据的一致性
     * 解决方案： 双写模式 失效模式
     * 双写模式： 数据库改完之后改缓存中的数据
     * 失效模式： 数据库改完之后直接删除缓存中的数据 等待下次主动更新
     * @return
     */
双写模式
双写模式:在数据库进行写操作的同时对缓存也进行写操作,确保缓存数据与数据库数据的一致性

脏数据问题：在A修改数据库后，更新缓存时延迟高， 在延迟期间，B已经有修改数据并更新缓存，过了一会A才更新缓存完毕。此时数据库里是B修改的内容，缓存库里是A修改的内容。

两个线程同时进行写操作时由于缓存是存储在redis,写缓存时需要发送网络请求,导致虽然线程一先发送写缓存的网络请求但是比线程二发送的写缓存的网络请求后到达redis,造成数据被覆盖

是否满足最终一致性:满足,原因 缓存过期以后，又能得到最新的正确数据读到的最新数据有延迟：最终一致性

双写模式可以对写数据库和写缓存操作加锁来保证写入的一致性，但是会影响性能 如果满足最终的一致性可以接受

![img.png](img.png)

失效模式

失效模式:在数据库进行更新操作时,删除原来的缓存,再次查询数据库就可以更新最新数据

存在问题

脏数据问题：当两个请求同时修改数据库，A已经更新成功并删除缓存时又有读数据的请求进来，这时候发现缓存中无数据就去数据库中查询并放入缓存，在放入缓存前第二个更新数据库的请求B成功，这时候留在缓存中的数据依然是A更新的数据

解决方法

1、缓存的所有数据都有过期时间，数据过期下一次查询触发主动更新
2、读写数据的时候(并且写的不频繁)，加上分布式的读写锁。  这个属于强一致性的解决方案，对一致性要求没那么多的可以不考虑。


对于要经常读取且要保证正确的数据就可以直接去查询数据库，不放入缓存
![img_1.png](img_1.png)



缓存一致性解决方案，读写锁和过期时间                           
无论是双写模式还是失效模式，都会导致缓存的不一致问题。即多个实例同时更新会出事，解决方案：              
1、如果是用户纬度数据（订单数据、用户数据），这种并发几率非常小，不用考虑这个问题，缓存数据加上过期时间，每隔一段时间触发读的主动更新即可                
2、如果是菜单，商品介绍等基础数据，也可以去使用canal订阅binlog的方式。               
3、缓存数据+过期时间也足够解决大部分业务对于缓存的要求。           
4、通过加锁保证并发读写，写写的时候按顺序排好队。只有读读不会被阻塞。所以适合使用读写锁。（业务不关心脏数据，允许临时脏数据可忽略）；
总结：                   
• 我们能放入缓存的数据本就不应该是实时性、一致性要求超高的。所以缓存数据的时候加上过期时间，保证每天拿到当前最新数据即可。                       
• 我们不应该过度设计，增加系统的复杂性                     
• 遇到实时性、一致性要求高的数据，就应该查数据库，即使慢点。


缓存中间件Canal             
Canal是阿里的缓存中间件，Canal将自己伪装成数据库的从服务器，MySQL一有变化，它就会同步更新到redis。 
![img_2.png](img_2.png)


SpringCache
Spring 从 3.1 开始定义了 org.springframework.cache.Cache和 org.springframework.cache.CacheManager 接口来统一不同的缓存技术；并支持使用 JCache（JSR-107） 注解简化我们开发；
Cache 接口为缓存的组件规范定义， 包含缓存的各种操作集合；Cache 接 口 下 Spring 提 供 了 各 种 xxxCache 的 实 现 ； 如 RedisCache ， EhCacheCache ,ConcurrentMapCache 等；
每次调用需要缓存功能的方法时， Spring 会检查检查指定参数的指定的目标方法是否已经被调用过； 如果有就直接从缓存中获取方法调用后的结果， 如果没有就调用方法并缓存结果后返回给用户。 下次调用直接从缓存中获取。
使用 Spring 缓存抽象时我们需要关注以下两点；
1、 确定方法需要被缓存以及他们的缓存策略
2、 从缓存中读取之前缓存存储的数据

![img_3.png](img_3.png)

注解
Cache	缓存接口,定义缓存操作.实现有:RedisCache,RhCacheCache,ConcurrentMapCache等                                
CacheManager	缓存管理器,管理各种缓存组件                             
@Cacheable	主要针对方法配置,能够根据方法的请求参数对其结果进行缓存                          
@CacheEvict	清空缓存                             
@CachePut	保证方法被调用,又希望结果被缓存                   
@Caching	组合上面三个注解多个操作                  
@EnableCaching	开启基于注解的缓存                  
@CacheConfig	在类级别分享缓存的相同配置           
keyGenerator	缓存数据是key生成策略         
serialize	缓存数据是value序列化策略
 
![img_4.png](img_4.png)


SpEL表达式语法

![img_5.png](img_5.png)

整合SpringCache简化缓存开发
![img_6.png](img_6.png)

导入依赖：
<!--Spring Cache-->         
<dependency>            
<groupId>org.springframework.boot</groupId>         
<artifactId>spring-boot-starter-cache</artifactId>          
</dependency>           
<!--redis-->            
<dependency>            
<groupId>org.springframework.boot</groupId>         
<artifactId>spring-boot-starter-data-redis</artifactId>             
</dependency>           



配置使用redis作为缓存                           
spring:                         
cache:                                           
type: redis              
开启缓存功能 @EnableCaching           
使用缓存注解                      

注解	作用
@Cacheable	主要针对方法配置,能够根据方法的请求参数对其结果进行缓存            
@CacheEvict	清空缓存            
@CachePut	保证方法被调用,又希望结果被缓存            
@Caching	组合上面三个注解多个操作        
@EnableCaching	开启基于注解的缓存           
@CacheConfig	在类级别分享缓存的相同配置           
​ @Cacheable：标注方法上：当前方法的结果存入缓存，如果缓存中有，方法不调用     
​ @CacheEvict：触发将数据从缓存删除的操作     
​ @CachePut：不影响方法执行更新缓存     
​ @Caching：组合以上多个操作         
​ @CacheConfig：在类级别共享缓存的相同配置  




异步与线程：                    
位置：com.atcode.watermall.product.thread.ThreadTest.ThreadTest01
/**
* 初始化线程的4种方式
* 1.1 继承 Thread类，重写run()方法
* public static class ThreadTest001 extends Thread{
*         @Override
*         public void run() {
*             System.out.println("当前线程id:  "+Thread.currentThread().getId());
*             int i = 10/2;
*             System.out.println("当前线程的运行结果:  "+ i);
*
*         }
*
*   public static void main(String[] args) {
*
*         ThreadTest001 thread = new ThreadTest001();
*         thread.start();
*
*     }
* 1.2 实现 Runnable 接口，重写run()方法
*     public static class ThreadTest002 implements Runnable{
*
*         @Override
*         public void run() {
*             System.out.println("当前线程id:  "+Thread.currentThread().getId());
*             int i = 10/2;
*             System.out.println("当前线程的运行结果:  "+ i);
*
*         }
*     }
*
*      public static void main(String[] args) {
*
*         System.out.println("main...start...");
*         Runnable runnable = new ThreadTest002();
*
*         new Thread(runnable).start();
*         System.out.println("main...end...");
*
*     }
* 1.3 实现 Callable 接口 ， FutureTask （可以拿到返回结果， 可以处理异常）
*      public static class ThreadTest003 implements Callable<Integer> {
*
*         @Override
*         public Integer call() throws Exception {
*             System.out.println("当前线程id:  "+Thread.currentThread().getId());
*             int i = 10/2;
*             System.out.println("当前线程的运行结果:  "+ i);
*
*             return i;
*         }
*     }
*      public static void main(String[] args) throws ExecutionException, InterruptedException {
*
*         System.out.println("main...start...");
*         FutureTask<Integer> futureTask = new FutureTask<>(new ThreadTest003());
*         new Thread(futureTask).start();
*         Integer integer = futureTask.get();  //阻塞方法，等线程执行完之后再执行
*         System.out.println("main...end..."+integer);
*
*     }
* 1.4 创建线程池直接提交任务（推荐）
* 四种创建线程方法的区别
* 区别：
* 1、2不能得到返回值。3可以获取返回值
* 1、2、3都不能控制资源
* 4可以控制资源，性能稳定，不会一下子所有线程一起运行
* 总结：
* 1、实际开发中，只用线程池【高并发状态开启了n个线程，会耗尽资源】
* 2、当前系统中线程池只有一两个，每个异步任务提交给线程池让他自己去执行
  */





