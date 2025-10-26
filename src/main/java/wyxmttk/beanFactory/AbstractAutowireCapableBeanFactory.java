package wyxmttk.beanFactory;

import cn.hutool.core.bean.BeanUtil;
import wyxmttk.beanDefinition.BeanDefinition;
import wyxmttk.beanDefinition.BeanReference;
import wyxmttk.beanDefinition.PropertyValue;
import wyxmttk.beanDefinition.PropertyValues;
import wyxmttk.instantiate.CglibSubclassingInstantiationStrategy;
import wyxmttk.instantiate.InstantiationStrategy;
import wyxmttk.instantiate.SimpleInstantiationStrategy;
import wyxmttk.processor.BeanPostProcessor;

import java.lang.reflect.Constructor;
import java.util.List;

//AutowireCapable意味着能自动注入属性和处理依赖
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory {
    private InstantiationStrategy instantiationStrategy=new SimpleInstantiationStrategy();

    private InstantiationStrategy cglibSubclassingInstantiationStrategy = new CglibSubclassingInstantiationStrategy();

    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) {
        Object bean;
        try {
            bean = createBeanInstance(beanName, beanDefinition, args);
            applyPropertyValues(beanName,bean,beanDefinition);
            bean=initializeBean(beanName,bean,beanDefinition);
        } catch (Exception e) {
            throw new RuntimeException("createBeanInstance error", e);
        }
        registerSingleton(beanName, bean);
        return bean;
    }

    protected Object createBeanInstance(String beanName, BeanDefinition beanDefinition, Object[] args) {
        Class<?> beanClass = beanDefinition.getBeanClass();
        Constructor<?>[] declaredConstructors = beanClass.getDeclaredConstructors();
        Constructor<?> constructorToUse = null;
        for (Constructor<?> constructor : declaredConstructors) {
            if (constructor.getParameterTypes().length == args.length) {
                constructorToUse=constructor;
                break;
            }
        }
//        return instantiationStrategy.instantiate(beanName,beanDefinition,constructorToUse,args);
        return cglibSubclassingInstantiationStrategy.instantiate(beanName,beanDefinition,constructorToUse,args);
    }

    protected void applyPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) {
        try {
            PropertyValues propertyValues = beanDefinition.getPropertyValues();
            if (propertyValues == null){
                return;
            }
            PropertyValue[] propertyValueArr = propertyValues.getPropertyValueList();
            for (PropertyValue propertyValue : propertyValueArr) {
                if (propertyValue == null) {
                    continue;
                }
                String propertyName = propertyValue.getName();
                Object value = propertyValue.getValue();
                if(value instanceof BeanReference beanReference){
                    value = getBean(beanReference.getBeanName());
                }
                BeanUtil.setFieldValue(bean, propertyName, value);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error setting property values: "+beanName);
        }

    }

    private Object initializeBean(String beanName,Object bean, BeanDefinition beanDefinition) {
        Object wrappedBean = applyBeanPostProcessorsBeforeInitialization(bean, beanName);

        invokeInitMethods(beanName, wrappedBean, beanDefinition);

        wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);

        return wrappedBean;
    }

    private void invokeInitMethods(String beanName, Object wrappedBean, BeanDefinition beanDefinition) {

    }
    //处理过程存在强转，会返回新对象
    @Override
    public Object applyBeanPostProcessorsBeforeInitialization(Object bean, String beanName) {
        List<BeanPostProcessor> beanPostProcessors = getBeanPostProcessors();
        //如直接修改bean的引用，只是修改了方法的局部变量bean，外部的不会被修改
        Object result = null;
        try {
            for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
                result = beanPostProcessor.postProcessBeforeInitialization(bean, beanName);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result==null?bean:result;
    }
    @Override
    public Object applyBeanPostProcessorsAfterInitialization(Object bean, String beanName) {
        List<BeanPostProcessor> beanPostProcessors = getBeanPostProcessors();
        Object result = null;
        try {
            for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
                result = beanPostProcessor.postProcessAfterInitialization(bean,beanName);
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result==null?bean:result;
    }

}
