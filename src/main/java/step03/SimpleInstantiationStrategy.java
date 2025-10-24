package step03;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class SimpleInstantiationStrategy implements InstantiationStrategy {
    @Override
    public Object instantiate(String beanName, BeanDefinition beanDefinition, Constructor<?> constructor, Object[] args){
        Class<?> beanClass = beanDefinition.getBeanClass();
        try {
            if (beanClass == null) {
                throw new InstantiationException("beanClass is null");
            }
            if (constructor == null) {
                return beanClass.getDeclaredConstructor().newInstance();
            }
            return beanClass.getDeclaredConstructor(constructor.getParameterTypes()).newInstance(args);
        } catch (InstantiationException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Failed to instantiate [" + beanClass.getName() + "]", e);
        }
    }
}
