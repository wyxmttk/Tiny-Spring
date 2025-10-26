package wyxmttk.instantiate;

import wyxmttk.beanDefinition.BeanDefinition;

import java.lang.reflect.Constructor;

public interface InstantiationStrategy {
    Object instantiate(String beanName, BeanDefinition beanDefinition, Constructor<?> constructor, Object[] args);
}
