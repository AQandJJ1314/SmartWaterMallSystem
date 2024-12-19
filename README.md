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

