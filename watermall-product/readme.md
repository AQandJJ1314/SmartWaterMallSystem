整合Mybatis-plus

  1.导入依赖(已在common中做过)
  <!-- mybatis-plus公共依赖-->
  <dependency>
  <groupId>com.baomidou</groupId>
  <artifactId>mybatis-plus-boot-starter</artifactId>
  <version>3.2.0</version>
  </dependency>

  2.配置
   配置数据库
    1）配置数据源  导入数据库的驱动
    2）在application.yml配置数据源相关信息
   配置mybatis-plus
    1）使用MapperScan
    2）告诉mybatis-plus,和dao对应的映射文件(xml)的位置




注意：除openfein之外其余的和springboot相关的版本都要用2.4.0以上的
  
    

