package org.citi.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;

import static com.jfinal.kit.StrKit.notNull;

/**
 * @author Carson
 * @created 2025/6/12 星期四 上午 10:10
 */
public class ESMapperScannerConfigure implements BeanDefinitionRegistryPostProcessor, InitializingBean, ApplicationContextAware, BeanNameAware {
    private ApplicationContext applicationContext;

    private String basePackage;
    private String beanName;
    private boolean lazyInit;
    private BeanNameGenerator beanNameGenerator;
    private Class<? extends Annotation> annotationClass;
    private Class<? extends ESMapperFactoryBean> factoryBeanClass;

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    public void setBeanNameGenerator(BeanNameGenerator beanNameGenerator) {
        this.beanNameGenerator = beanNameGenerator;
    }

    public void setAnnotationClass(Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }

    public void setFactoryBeanClass(Class<? extends ESMapperFactoryBean> factoryBeanClass) {
        this.factoryBeanClass = factoryBeanClass;
    }

    @Override
    public void setBeanName(String s) {
        this.beanName =s;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        notNull(this.basePackage, "Property 'basePackage' is required");
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        ESMapperClassPathScanner scanner = new ESMapperClassPathScanner(beanDefinitionRegistry);
        scanner.setLazyInit(this.lazyInit);
        scanner.setBeanNameGenerator(this.beanNameGenerator);
        scanner.setAnnotationClass(this.annotationClass);
        scanner.setFactoryBeanClass(this.factoryBeanClass);
        scanner.addIncludeFilter(new AnnotationTypeFilter(this.annotationClass));
        scanner.setResourceLoader(this.applicationContext);
        scanner.scan(StringUtils.tokenizeToStringArray(this.basePackage, ",; \t\n"));
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
