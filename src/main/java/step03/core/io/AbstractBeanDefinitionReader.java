package step03.core.io;

import step03.BeanDefinitionRegistry;

public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader {
    //可自定义
    private ResourceLoader resourceLoader;
    //核心依赖，final
    private final BeanDefinitionRegistry beanDefinitionRegistry;

    public AbstractBeanDefinitionReader(BeanDefinitionRegistry beanDefinitionRegistry) {
        this.beanDefinitionRegistry = beanDefinitionRegistry;
        resourceLoader = new DefaultResourceLoader();
    }
    public AbstractBeanDefinitionReader(BeanDefinitionRegistry beanDefinitionRegistry,ResourceLoader resourceLoader) {
        this.beanDefinitionRegistry = beanDefinitionRegistry;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public BeanDefinitionRegistry getRegistry() {
        return beanDefinitionRegistry;
    }

    @Override
    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }
}
