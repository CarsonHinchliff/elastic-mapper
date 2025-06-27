package org.citi.elasticmapper.bean;

import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;

/**
 * @author Carson
 * @created 2025/6/12 星期四 上午 10:17
 */
@Slf4j
@EqualsAndHashCode(callSuper=true)
public class ESMapperClassPathScanner extends ClassPathBeanDefinitionScanner {
    private boolean lazyInit = false;
    private Class<? extends Annotation> annotationClass;
    private Class<? extends ESMapperFactoryBean> factoryBeanClass;

    public boolean isLazyInit() {
        return lazyInit;
    }

    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    public Class<? extends Annotation> getAnnotationClass() {
        return annotationClass;
    }

    public void setAnnotationClass(Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }

    public Class<? extends ESMapperFactoryBean> getFactoryBeanClass() {
        return factoryBeanClass;
    }

    public void setFactoryBeanClass(Class<? extends ESMapperFactoryBean> factoryBeanClass) {
        this.factoryBeanClass = factoryBeanClass;
    }

    public ESMapperClassPathScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitionHolders =  super.doScan(basePackages);
        if (beanDefinitionHolders.isEmpty()) {
            log.warn("No ES Mapper be found in {}", Arrays.toString(basePackages));
            return beanDefinitionHolders;
        }
        for(BeanDefinitionHolder beanDefinitionHolder : beanDefinitionHolders) {
            GenericBeanDefinition beanDefinition = (GenericBeanDefinition) beanDefinitionHolder.getBeanDefinition();
            beanDefinition.getPropertyValues().add("mapperClazz", beanDefinition.getBeanClassName());
            beanDefinition.setBeanClass(factoryBeanClass);
            beanDefinition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
            beanDefinition.setLazyInit(lazyInit);
        }
        return beanDefinitionHolders;
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().hasAnnotation(this.annotationClass.getName());
    }
}
