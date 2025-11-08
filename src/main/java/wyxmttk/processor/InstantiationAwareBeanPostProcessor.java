package wyxmttk.processor;

import wyxmttk.beanDefinition.PropertyValues;

//见名知意:InstantiationAware表示能意识到要到bean将要实例化
public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor {

    Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName);

    PropertyValues postProcessPropertyValues(PropertyValues pvs,Object bean, String beanName);

}
