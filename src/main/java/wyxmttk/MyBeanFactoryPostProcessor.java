package wyxmttk;

import wyxmttk.beanDefinition.BeanDefinition;
import wyxmttk.beanDefinition.PropertyValue;
import wyxmttk.beanFactory.ConfigurableListableBeanFactory;
import wyxmttk.processor.BeanFactoryPostProcessor;

public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws Exception {
        BeanDefinition service = beanFactory.getBeanDefinition("service");
        service.getPropertyValues().addPropertyValue(new PropertyValue("age",18));
    }
}
