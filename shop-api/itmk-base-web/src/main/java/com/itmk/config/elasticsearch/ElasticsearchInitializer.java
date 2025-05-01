package com.itmk.config.elasticsearch;

import com.itmk.web.elasticsearch.service.ElasticsearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ElasticsearchInitializer implements CommandLineRunner {

    @Autowired
    private ElasticsearchService elasticsearchService;

    @Override
    public void run(String... args) throws Exception {
        // 应用启动时同步所有数据
        // 如果数据量很大，建议通过接口手动触发，而不是在启动时自动同步
        // elasticsearchService.syncAllGoodsToEs();
    }
}