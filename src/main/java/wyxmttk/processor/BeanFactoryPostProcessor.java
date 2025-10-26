package wyxmttk.processor;

import wyxmttk.beanFactory.ConfigurableListableBeanFactory;

public interface BeanFactoryPostProcessor {
    //提供修改beanDefinition的机制
    void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws Exception;
}
