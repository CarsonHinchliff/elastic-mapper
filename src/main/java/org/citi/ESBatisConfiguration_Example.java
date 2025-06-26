//package org.citi;
//
//import org.citi.bean.ESMapperScan;
//import org.citi.es.ESRestClientBuilder;
//import org.citi.es.ESRestClientProperties;
//import org.citi.factory.ESMapperFactory;
//import org.elasticsearch.client.RestClient;
//import org.springframework.beans.BeansException;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationContextAware;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * @author Carson
// * @created 2025/6/12 星期四 下午 06:45
// */
//@ESMapperScan(basePackages = "org.citi.**.*.dsl", dslPath = "classpath*:dsl/*.dsl")
//@Configuration
//public class ESBatisConfiguration_Example  {
//    @Bean
//    public ESBatisConfiguration esBatisConfiguration(ESRestClientProperties properties){
//        return new ESBatisConfiguration(properties);
//    }
//}
