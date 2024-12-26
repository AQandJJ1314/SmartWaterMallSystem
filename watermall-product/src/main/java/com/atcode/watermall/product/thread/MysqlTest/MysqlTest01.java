package com.atcode.watermall.product.thread.MysqlTest;

public class MysqlTest01 {
    /**
     * 判断数据库是读密集型or写密集型or读写密集型
     * 可以看出很长一段时间内的数据库的整体性能
     * use gulimall_pms;
     * show global status like'Com_select';
     * show global status like'Com_insert';
     * show global status like'Com_update';
     * show global status like'Com_delete';
     * show global status like'Com_______';
     */

    /**
     * 慢查询日志
     * 慢查询日志可以将较慢的DQL语句记录下来，便于我们定位需要优化的select语句
     * 通过以下命令判断慢查询日志是否开启    该功能默认是关闭的
     * show variables like 'slow_query_log';
     * 慢查询日志默认是关闭的，可以通过修改my.ini文件来开启慢查询日志功能，可以在my.ini的[mysqld]后面添加如下配置：
     * '
     *  [mysqld]
     *  slow_query_log = 1
     *  long_query_time = 3
     *  '
     *  slow_query_log = 1 表示开启慢查询日志功能
     *  long_query_time = 3 表示只要select的语句耗时超过三秒就会记录到慢查询日志中
     *  日志位置和bin同级的data下的yourcomputername-slow.log文件下
     *  注意：Windows下my.ini文件需要自己新建，linux/mac下的后缀是.cnf
     */

    /**
     * profiling
     * 查看当前数据库是否支持profile操作
     * select @@have_profiling;
     *
     * 查看profiling开关是否开启(注意navicat for mysql是默认开启的,dos命令窗口则不一定是)
     * select @@profiling;
     *
     * 可以通过以下方式把profiling开关打开
     * set profiling = 1;
     *
     *
     */
}
