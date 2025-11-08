package wyxmttk.beanFactory;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import wyxmttk.aop.DefaultAdvisorAutoProxyCreator;
import wyxmttk.beanDefinition.BeanDefinition;
import wyxmttk.beanDefinition.BeanReference;
import wyxmttk.beanDefinition.PropertyValue;
import wyxmttk.beanDefinition.PropertyValues;
import wyxmttk.context.DisposableBean;
import wyxmttk.context.DisposableBeanAdapter;
import wyxmttk.context.InitializingBean;
import wyxmttk.instantiate.CglibSubclassingInstantiationStrategy;
import wyxmttk.instantiate.InstantiationStrategy;
import wyxmttk.instantiate.SimpleInstantiationStrategy;
import wyxmttk.processor.AutowiredAnnotationBeanPostProcessor;
import wyxmttk.processor.BeanPostProcessor;
import wyxmttk.processor.InstantiationAwareBeanPostProcessor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

//AutowireCapable意味着能自动注入属性和处理依赖
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory {
    private InstantiationStrategy instantiationStrategy=new SimpleInstantiationStrategy();

    private InstantiationStrategy cglibSubclassingInstantiationStrategy = new CglibSubclassingInstantiationStrategy();

    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) {
        Object bean;
        try {
            //处理beanDefinition，与beanFactoryPostProcessor不同的是这边是懒处理。
            bean = resolveBeforeInstantiation(beanName,beanDefinition);

            if (bean == null) {
                //使用cglib或jdk动态代理创建bean实例
                bean = createBeanInstance(beanName, beanDefinition, args);
                if(beanDefinition.isSingleton()) {
                    Object finalBean = bean;
                    addSingletonFactory(beanName,()->getEarlyReference(beanName,finalBean));
                }

                // 实例化后判断
                boolean continueWithPropertyPopulation = applyBeanPostProcessorsAfterInstantiation(beanName, bean);
                if (!continueWithPropertyPopulation) {
                    return bean;
                }

                //解析@Autowired等自动注入相关的注解
                applyBeanPostProcessorsBeforeApplyingPropertyValues(beanName, bean, beanDefinition);
                //给bean填充属性，主要为自动注入的属性以及xml定义的属性
                applyPropertyValues(beanName,bean,beanDefinition);
                //初始化bean，分为容器感知 前置 初始化 后置操作
                //后置操作包括Aop创建代理对象
                bean = initializeBean(beanName, bean, beanDefinition);
            }
        } catch (Exception e) {
            throw new RuntimeException("createBeanInstance error", e);
        }
        registerDisposableBeanIfNecessary(beanName,bean,beanDefinition);

        Object exposedBean = bean;

        //该步作用主要是将缓存里的bean对象返回，同时清除二三级缓存并添加一级缓存，因为没有设置二级缓存到一级缓存的转换方法
        //由于动态代理本质上是对原对象的一层包装，上述的直接修改原对象的操作，都会同步给代理对象。但是后置初始化方法的重复的代理操作返回的bean对象无需
        //关注，除非本身bean定义就不是单例，那这时候缓存里也就不存在对象，那就必须用后置初始化操作返回的对象
        if(beanDefinition.isSingleton()) {
            exposedBean = getSingleton(beanName);
            registerSingleton(beanName, exposedBean);
        }
        return exposedBean;
    }
    //TODO:待完善
    protected boolean applyBeanPostProcessorsAfterInstantiation(String beanName, Object bean) {
        return true;
    }

    protected Object getEarlyReference(String beanName,Object bean) {
        Object result = bean;
        List<BeanPostProcessor> beanPostProcessors = getBeanPostProcessors();
        try {
            for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
                if(beanPostProcessor instanceof DefaultAdvisorAutoProxyCreator) {
                    result = beanPostProcessor.postProcessAfterInitialization(bean, beanName);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return bean;
    }

    protected void applyBeanPostProcessorsBeforeApplyingPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) {
        for(BeanPostProcessor beanPostProcessor:getBeanPostProcessors()) {
            if(beanPostProcessor instanceof AutowiredAnnotationBeanPostProcessor autowiredAnnotationBeanPostProcessor) {
                PropertyValues propertyValues = autowiredAnnotationBeanPostProcessor.postProcessPropertyValues(beanDefinition.getPropertyValues(), bean, beanName);
                if(propertyValues != null) {
                    for(PropertyValue pV:propertyValues.getPropertyValueList()) {
                        beanDefinition.getPropertyValues().addPropertyValue(pV);
                    }
                }
            }
        }
    }

    protected Object resolveBeforeInstantiation(String beanName, BeanDefinition beanDefinition) {
        Object bean = applyBeanPostProcessorBeforeInstantiation(beanDefinition.getBeanClass(), beanName);
        if(bean != null){
            bean=applyBeanPostProcessorsAfterInitialization(bean,beanName);
        }
        return bean;
    }

    protected Object applyBeanPostProcessorBeforeInstantiation(Class<?> clazz,String beanName) {
        for(BeanPostProcessor beanPostProcessor:getBeanPostProcessors()) {
            if(beanPostProcessor instanceof InstantiationAwareBeanPostProcessor instantiationAwareBeanPostProcessor) {
                Object result=instantiationAwareBeanPostProcessor.postProcessBeforeInstantiation(clazz,beanName);
                if(result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    protected void registerDisposableBeanIfNecessary(String beanName, Object bean, BeanDefinition beanDefinition) {
        //原型对象的生命周期交由用户处理
        if(ConfigurableBeanFactory.SCOPE_PROTOTYPE.equals(beanDefinition.getScope())) {
            return;
        }

        if(bean instanceof DisposableBean || !StrUtil.isBlank(beanDefinition.getDestroyMethodName())) {
            registerDisposableBean(beanName,new DisposableBeanAdapter(beanName,bean,beanDefinition));
        }
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
        if(bean instanceof Aware){
            if(bean instanceof BeanFactoryAware){
                ((BeanFactoryAware)bean).setBeanFactory(this);
            }
            if(bean instanceof BeanNameAware){
                ((BeanNameAware)bean).setBeanName(beanName);
            }
            if(bean instanceof BeanClassLoaderAware){
                ((BeanClassLoaderAware)bean).setBeanClassLoader(getBeanClassLoader());
            }
        }

        Object wrappedBean = applyBeanPostProcessorsBeforeInitialization(bean, beanName);

        invokeInitMethods(beanName, wrappedBean, beanDefinition);

        wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);

        return wrappedBean;
    }

    private void invokeInitMethods(String beanName, Object wrappedBean, BeanDefinition beanDefinition) {
        if(wrappedBean instanceof InitializingBean initializingBean) {
            try {
                initializingBean.afterPropertiesSet();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        String initMethodName = beanDefinition.getInitMethodName();
        //不能强转后用对象调用，因为类型不知道，而且对象里也不一定有这个方法
//        beanDefinition.getBeanClass().cast(wrappedBean);
        if(StrUtil.isBlank(initMethodName)){
            return;
        }
        try {
            Method initMethod = beanDefinition.getBeanClass().getDeclaredMethod(initMethodName);
            initMethod.setAccessible(true);
            initMethod.invoke(wrappedBean);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }


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
