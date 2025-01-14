package com.atcode.watermall.watermallsearch.service.impl;

import com.alibaba.fastjson.JSON;
import com.atcode.common.to.SkuEsModel;
import com.atcode.watermall.watermallsearch.config.WatermallElasticSearchConfig;
import com.atcode.watermall.watermallsearch.constant.EsConstant;
import com.atcode.watermall.watermallsearch.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductSaveServiceImpl implements ProductSaveService {

    @Autowired
    RestHighLevelClient restHighLevelClient;
    @Override
    public boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException {
        /**
         * 数据保存到es中
         * 1.给es中建立索引，product，建立好映射关系
         * 在根目录的mapping.txt文件中
         * 2.给es中保存数据
         */
        //BulkRequest bulkRequest, RequestOptions options
        BulkRequest bulkRequest = new BulkRequest();
        for (SkuEsModel model : skuEsModels){
            //构造保存请求
            IndexRequest indexRequest = new IndexRequest(EsConstant.PRODUCT_INDEX);
            indexRequest.id(model.getSkuId().toString());
            String s = JSON.toJSONString(model);
            indexRequest.source(s, XContentType.JSON);

            bulkRequest.add(indexRequest);
        }

        //TODO 后续测试，到目前，上面代码的数据封装已经没有问题  20250114
        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, WatermallElasticSearchConfig.COMMON_OPTIONS);

        //TODO 如果批量错误
        boolean b = bulk.hasFailures();
        List<String> collect = Arrays.stream(bulk.getItems()).map(item -> {
            return item.getId();
        }).collect(Collectors.toList());
        log.error("商品上架成功:{}",collect);

        return b;

    }
}
