package step03;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;

import java.lang.reflect.Constructor;

public class CglibSubclassingInstantiationStrategy implements InstantiationStrategy {

    @Override
    public Object instantiate(String beanName, BeanDefinition beanDefinition, Constructor<?> constructor, Object[] args) {
        //创建一个类
        Enhancer enhancer = new Enhancer();
        Class<?> beanClass = beanDefinition.getBeanClass();
        if (beanClass==null){
            throw new RuntimeException("beanClass is null");
        }
        enhancer.setSuperclass(beanClass);
        enhancer.setCallback(new NoOp() {
            @Override
            public int hashCode() {
                return super.hashCode();
            }
        });
        if(constructor == null){
            return enhancer.create();
        }
        return enhancer.create(constructor.getParameterTypes(), args);
    }
}
