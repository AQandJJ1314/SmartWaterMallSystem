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

  逻辑删除
  mybatisplus逻辑删除步骤：

逻辑删除   
| MyBatis-Plus

方法一（mp3.3.0版本后，不推荐怕忘）：配置yml，全局   配置全局的逻辑删除规则

mybatis-plus:
global-config:
db-config:
logic-delete-field: isDelete # 全局逻辑删除的实体字段名(since 3.3.0,配置本项后可以忽略不配置实体类注解@TableLogic)
logic-delete-value: 1 # 逻辑已删除值(默认为 1)。
logic-not-delete-value: 0 # 逻辑未删除值(默认为 0)
方法二（mp所有版本，推荐）：

实体类成员变量加上@TableLogic(value = "0", delval = "1")注解

注意：本项目使用的是category表的show_status字段，逻辑删除值正好相反，状态为1表示未删除，状态为0表示删除。

CategoryEntity注解逻辑删除：
/**
* 是否显示[0-不显示，1显示]
*/
@TableLogic(value = "1",delval = "0")
private Integer showStatus;
service和controller不用改，使用mybatisplus生成的即可





注意：除openfein之外其余的和springboot相关的版本都要用2.4.0以上的
  
    

