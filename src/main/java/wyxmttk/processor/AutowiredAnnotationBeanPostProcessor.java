package wyxmttk.processor;

import cn.hutool.core.bean.BeanUtil;
import wyxmttk.annotation.Autowired;
import wyxmttk.annotation.Component;
import wyxmttk.annotation.Qualifier;
import wyxmttk.annotation.Value;
import wyxmttk.beanDefinition.BeanDefinition;
import wyxmttk.beanDefinition.PropertyValue;
import wyxmttk.beanDefinition.PropertyValues;
import wyxmttk.beanFactory.BeanFactory;
import wyxmttk.beanFactory.BeanFactoryAware;
import wyxmttk.beanFactory.ConfigurableListableBeanFactory;
import wyxmttk.util.ClassUtils;

import java.lang.reflect.Field;

import java.util.Collection;

@Component
public class AutowiredAnnotationBeanPostProcessor implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {

    private ConfigurableListableBeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory=(ConfigurableListableBeanFactory) beanFactory;
    }

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) {
        Class<?> clazz = bean.getClass();
        //判断cglib
        if(ClassUtils.isCglibProxyClass(bean.getClass())) {
            clazz=clazz.getSuperclass();
        }
        //返回当前clazz自己定义的所有字段
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            resolveValue(field,bean,pvs);
            resolveAutowired(field,bean,pvs);
        }
        return pvs;
    }
    private void resolveValue(Field field, Object bean,PropertyValues pvs ) {
        field.setAccessible(true);
        Value annotation = field.getAnnotation(Value.class);
        if(annotation != null){
            String value = annotation.value();
            String s = beanFactory.resolveEmbeddedValue(value);
            pvs.addPropertyValue(new PropertyValue(field.getName(), s));
        }
    }

    private void resolveAutowired(Field field, Object bean,PropertyValues pvs ) {
        field.setAccessible(true);
        if(field.getAnnotation(Autowired.class) != null){
            Qualifier qualifier = field.getAnnotation(Qualifier.class);
            Class<?> type = field.getType();
            String name = field.getName();
            Collection<?> values = beanFactory.getBeansOfType(type).values();
            if(values.isEmpty()){
                throw new RuntimeException("Autowired bean class [" + bean.getClass().getName() + "] is empty");
            }
            if(values.size() == 1){
                pvs.addPropertyValue(new PropertyValue(field.getName(), values.iterator().next()));
                return;
            }
            if(qualifier == null){
                throw new RuntimeException("more than one bean which of class is called [" + bean.getClass().getName() + "]");
            }
            String valueOfQualifier = qualifier.value();
            for (Object oneBean : values) {
                Class<?> clazzOfBean = oneBean.getClass();
                Qualifier annotation = clazzOfBean.getAnnotation(Qualifier.class);
                if(annotation != null){
                    String value = annotation.value();
                    if(value.equals(valueOfQualifier)){
                        pvs.addPropertyValue(new PropertyValue(field.getName(), oneBean));
                        return;
                    }
                }
            }
            throw new RuntimeException("more than one bean which of class is called [" + bean.getClass().getName() + "]");
        }
    }
    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) {
        return null;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
        return bean;
    }
}
