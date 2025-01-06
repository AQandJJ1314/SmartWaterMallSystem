package com.atcode.watermall.product.thread.ElasticSearchTest;

public class ElasticSearchTest01 {
    /**
     * 初步检索
     * 3.1、_CAT
     * 1)、GET  /_cat/nodes：查看所有节点
     *
     * 如：http://192.168.119.127:9200/_cat/nodes :
     *
     * 127.0.0.1 16 94 9 0.00 0.06 0.52 dilm * fec10300c5b8
     * 注：*表示集群中的主节点
     *
     * 2)、GET  /_cat/health：查看es健康状况
     *
     * 如： http://192.168.119.127:9200/_cat/health
     *
     * 1603632656 13:30:56 elasticsearch green 1 1 3 3 0 0 0 0 - 100.0%
     * 注：green表示健康值正常
     *
     * 3)、GET  /_cat/master：查看主节点
     *
     * 如： http://192.168.119.127:9200/_cat/master
     *
     * NI-h1wEIR9uRJ76dMdE-Cg 127.0.0.1 127.0.0.1 fec10300c5b8
     * 4)、GET  /_cat/indicies：查看所有索引 ，等价于mysql数据库的show databases;
     *
     * 如： http://192.168.119.127:9200/_cat/indices
     *
     * green open .kibana_task_manager_1   T87Lxcb5T22_HUNZ1Ak4QA 1 0 2 0 51.5kb 51.5kb
     * green open .apm-agent-configuration ps92glGfTkW6ID2ozvoofw 1 0 0 0   283b   283b
     * green open .kibana_1                CuxQb2nORlybswxvYfSQCA 1 0 5 0 22.6kb 22.6kb
     * 3.2、索引一个文档
     * 保存一个数据，保存在哪个索引的哪个类型下，指定用那个唯一标识
     * PUT customer/external/1;在customer索引下的external类型下保存1号数据为
     *
     * PUT customer/external/1
     * 192.168.119.127:9200/customer/extrnal/1
     *
     * {
     *  "name":"John Doe"
     * }
     * PUT和POST都可以
     * POST新增。如果不指定id，会自动生成id。指定id就会修改这个数据，并新增版本号；
     * PUT可以新增也可以修改。PUT必须指定id；由于PUT需要指定id，我们一般用来做修改操作，不指定id会报错。
     *
     * 下面是在postman中的测试数据：
     *
     *
     *
     * 创建数据成功后，显示201 created表示插入记录成功。
     *
     * {
     *     "_index": "customer",
     *     "_type": "extrnal",
     *     "_id": "1",
     *     "_version": 1,
     *     "result": "created",
     *     "_shards": {
     *         "total": 2,
     *         "successful": 1,
     *         "failed": 0
     *     },
     *     "_seq_no": 0,
     *     "_primary_term": 1
     * }
     * 这些返回的JSON串的含义；这些带有下划线开头的，称为元数据，反映了当前的基本信息。
     *
     * “_index”: “customer” 表明该数据在哪个数据库下；
     *
     * “_type”: “external” 表明该数据在哪个类型下；
     *
     * “_id”: “1” 表明被保存数据的id；
     *
     * “_version”: 1, 被保存数据的版本
     *
     * “result”: “created” 这里是创建了一条数据，如果重新put一条数据，则该状态会变为updated，并且版本号也会发生变化。
     *
     */
}
