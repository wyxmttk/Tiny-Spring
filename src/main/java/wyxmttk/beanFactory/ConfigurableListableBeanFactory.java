package wyxmttk.beanFactory;

import wyxmttk.beanDefinition.BeanDefinition;
import wyxmttk.processor.BeanPostProcessor;

public interface ConfigurableListableBeanFactory extends ConfigurableBeanFactory,ListableBeanFactory,AutowireCapableBeanFactory {
    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

    void preInstantiateSingletons();

    BeanDefinition getBeanDefinition(String beanName);

}
