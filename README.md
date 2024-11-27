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


