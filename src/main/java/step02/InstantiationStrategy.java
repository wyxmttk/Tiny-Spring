package step02;

import java.lang.reflect.Constructor;

public interface InstantiationStrategy {
    Object instantiate(String beanName, BeanDefinition beanDefinition, Constructor<?> constructor,Object[] args);
}
