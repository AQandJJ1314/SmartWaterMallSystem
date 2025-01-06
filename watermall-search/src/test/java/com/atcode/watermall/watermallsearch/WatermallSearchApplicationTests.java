package com.atcode.watermall.watermallsearch;

import com.alibaba.fastjson.JSON;
import com.atcode.watermall.watermallsearch.config.WatermallElasticSearchConfig;
import lombok.Data;
import lombok.ToString;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Avg;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class WatermallSearchApplicationTests {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Data
    class User{
        private String userName;
        private String gender;
        private Integer age;
    }

    //TODO 未测试，内存不足，后续测试
    /**
     * 测试储存数据es
     */
    @Test
    public void indexData() throws IOException {
        IndexRequest indexRequest = new IndexRequest("users");
        indexRequest.id("1");
//        indexRequest.source("userName","zhangsan","age",18,"gender","男");
        User user = new User();
        user.setUserName("张三");
        user.setAge(13);
        user.setGender("男");
        String jsonString = JSON.toJSONString(user);
        indexRequest.source(jsonString, XContentType.JSON);

        //执行操作
        IndexResponse index = restHighLevelClient.index(indexRequest, WatermallElasticSearchConfig.COMMON_OPTIONS);

        //提取有用地响应数据
        System.out.println(index);
    }

    @Data
    @ToString
    static class Account {
        private int account_number;
        private int balance;
        private String firstname;
        private String lastname;
        private int age;
        private String gender;
        private String address;
        private String employer;
        private String email;
        private String city;
        private String state;
    }

    //TODO 未测试，内存不足，后续测试
    /**
     * 测试检索请求
     * 复杂检索:在bank中搜索address中包含mill的所有人的年龄分布以及平均年龄，平均薪资
     */
    @Test
    public void searchData() throws IOException {
        // 1、创建检索请求
        SearchRequest searchRequest = new SearchRequest();
        // 1.1、指定索引
        searchRequest.indices("bank");

        // 1.2、指定DSL,检索条件
        //SearchSourceBuilder sourceBuilder  封装的条件
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        searchRequest.source(sourceBuilder);
        // 1.2.1、检索条件
//        sourceBuilder.query();
//        sourceBuilder.from();
//        sourceBuilder.size();
//        sourceBuilder.aggregation();
        sourceBuilder.query(QueryBuilders.matchQuery("address", "mill"));
        // 1.2.2、按照年龄的值分布进行聚合
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("ageAgg").field("age").size(10);
        sourceBuilder.aggregation(termsAggregationBuilder);
        // 1.2.3、计算平均薪资
        AvgAggregationBuilder avgAggregationBuilder = AggregationBuilders.avg("balanceAvg").field("balance");
        sourceBuilder.aggregation(avgAggregationBuilder);
        System.out.println("检索条件" + sourceBuilder.toString());

        // 2、执行检索
        SearchResponse search = restHighLevelClient.search(searchRequest, WatermallElasticSearchConfig.COMMON_OPTIONS);

        // 3、分析结果 search
        System.out.println(search.toString());
        // 3.1、获取所有查到的数据
        SearchHits hits = search.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            String sourceAsString = hit.getSourceAsString();
            Account account = JSON.parseObject(sourceAsString, Account.class);
            System.out.println(account);
        }
        // 3.2、获取这次检索到的分析信息
        Aggregations aggregations = search.getAggregations();
        for (Aggregation aggregation : aggregations.asList()) {
            String name = aggregation.getName();
            System.out.println("当前聚合的名字" + name);
        }
        Terms ageAgg = aggregations.get("ageAgg");
        for (Terms.Bucket bucket : ageAgg.getBuckets()) {
            String keyAsString = bucket.getKeyAsString();
            System.out.println("年龄" + keyAsString);
        }

        Avg balanceAvg = aggregations.get("balanceAvg");
        System.out.println("平均薪资" + balanceAvg.getValue());
    }



    @Test
    public void contextLoads() {
        System.out.println(restHighLevelClient);

    }

}
