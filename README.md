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
位置:com.atcode.watermall.product.controller.CategoryBrandRelationController
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



