package org.citi.elasticmapper.bean;

import org.citi.elasticmapper.ESMapperDSLParser;
import org.citi.elasticmapper.annotation.ESMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.DefaultBeanNameGenerator;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Carson
 * @created 2025/6/12 10:02
 */
public class ESMapperScannerRegister implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {
    private ResourceLoader resourceLoader;
    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader =resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry, BeanNameGenerator importBeanNameGenerator) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(ESMapperScan.class.getName()));
        if (attributes != null) {
            this.registerBeanDefinitions(attributes, registry, generateBeanName(importingClassMetadata, 0));
        }
    }

    private static String generateBeanName(AnnotationMetadata importingClassMetadata, int index) {
        return importingClassMetadata.getClassName() + "#" + ESMapperScannerRegister.class.getSimpleName() + "#" + index;
    }

    void registerBeanDefinitions(AnnotationAttributes attributes, BeanDefinitionRegistry registry, String beanName) {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(ESMapperScannerConfigure.class);
        beanDefinitionBuilder.addPropertyValue("annotationClass", ESMapper.class);
        beanDefinitionBuilder.addPropertyValue("lazyInit", false);
        beanDefinitionBuilder.addPropertyValue("factoryBeanClass", ESMapperFactoryBean.class);
        beanDefinitionBuilder.addPropertyValue("beanNameGenerator", BeanUtils.instantiateClass(DefaultBeanNameGenerator.class));
        List<String> basePackages = new ArrayList<>();
        basePackages.addAll(Arrays.stream(attributes.getStringArray("value")).filter(StringUtils::hasText).collect(Collectors.toList()));
        basePackages.addAll(Arrays.stream(attributes.getStringArray("basePackages")).filter(StringUtils::hasText).collect(Collectors.toList()));
        beanDefinitionBuilder.addPropertyValue("basePackage", StringUtils.collectionToCommaDelimitedString(basePackages));
        registry.registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());
        ESMapperDSLParser.getInstance().loadDSLFile(attributes.getString("dslPath"), this.resourceLoader);
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(ESMapperScan.class.getName()));
        if (null != attributes){
            this.registerBeanDefinitions(attributes, registry, generateBeanName(importingClassMetadata, 0));
        }
    }

    static class RepeatingRegistrar extends ESMapperScannerRegister{
        @Override
        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
            AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(ESMapperScan.class.getName()));
            if (attributes != null) {
                AnnotationAttributes[] annotationAttributes = attributes.getAnnotationArray("value");
                for (int i = 0; i < annotationAttributes.length; i++) {
                    AnnotationAttributes annotationAttribute = annotationAttributes[i];
                    this.registerBeanDefinitions(annotationAttribute, registry, ESMapperScannerRegister.generateBeanName(importingClassMetadata, i));
                }
            }
        }
    }
}
