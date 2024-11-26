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
