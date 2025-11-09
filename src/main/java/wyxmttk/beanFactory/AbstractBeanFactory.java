package wyxmttk.beanFactory;

import cn.hutool.core.util.ClassUtil;
import wyxmttk.Test;
import wyxmttk.beanDefinition.BeanDefinition;
import wyxmttk.convert.ConversionService;
import wyxmttk.processor.BeanPostProcessor;
import wyxmttk.processor.StringValueResolver;
import wyxmttk.singleton.DefaultSingletonBeanRegistry;
import wyxmttk.singleton.FactoryBeanRegistrySupport;
import wyxmttk.util.ClassUtils;

import java.util.*;

public abstract class AbstractBeanFactory extends FactoryBeanRegistrySupport implements ConfigurableListableBeanFactory {

    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    private ClassLoader beanClassLoader = ClassUtil.getClassLoader();

    private final List<StringValueResolver> stringValueResolvers = new ArrayList<>();

    private ConversionService conversionService;

    public StringValueResolver[] getStringValueResolvers() {
        return stringValueResolvers.toArray(new StringValueResolver[0]);
    }

    @Override
    public void registerConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public String resolveEmbeddedValue(String value) {
        String result = value;
        for(StringValueResolver resolver : stringValueResolvers) {
            result = resolver.resolve(result);
        }
        return result;
    }

    @Override
    public void addEmbeddedValueResolver(StringValueResolver resolver) {
        stringValueResolvers.add(resolver);
    }

    @Override
    public ClassLoader getBeanClassLoader() {
        return beanClassLoader;
    }

    public void setBeanClassLoader(ClassLoader beanClassLoader) {
        this.beanClassLoader = beanClassLoader;
    }

    protected Object doGetBean(String beanName,Object... args) {
        Object object = getSingleton(beanName);
        if (object == null) {
            object = createBean(beanName,getBeanDefinition(beanName), args);
        }
        if (object instanceof FactoryBean factoryBean) {
            Object producedSingleton = getProducedSingleton(beanName);
            if(producedSingleton == null) {
                producedSingleton = getObjectFromFactoryBean(factoryBean, beanName);
            }
            object = producedSingleton;
        }
        return object;
    }

    //根据args判断要用哪个构造器
    @Override
    public Object getBean(String beanName,Object... args) {
//        Object singleton = getSingleton(beanName);
//        if (singleton != null) {
//            System.out.println("map中存在单例"+beanName);
//            return singleton;
//        }
//        System.out.println("单例不存在，创建");
//        BeanDefinition beanDefinition = getBeanDefinition(beanName);
//        return createBean(beanName, beanDefinition, args);
        return doGetBean(beanName,args);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) {
        String[] beanDefinitionNames = getBeanDefinitionNames();
        Map<String, T> beans = new HashMap<>();
        for (String beanName : beanDefinitionNames) {
            BeanDefinition beanDefinition = getBeanDefinition(beanName);
            if(beanDefinition==null) continue;
            //相比equals比较，考虑到了继承和实现关系
            if(type.isAssignableFrom(beanDefinition.getBeanClass())) {
                //type.cast()封装了一层检查
                beans.put(beanName, type.cast(getBean(beanName)));
            }
        }
        return beans;
    }

    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        beanPostProcessors.add(beanPostProcessor);
    }



    public List<BeanPostProcessor> getBeanPostProcessors() {
        return beanPostProcessors;
    }

    public abstract BeanDefinition getBeanDefinition(String beanName);

    protected abstract Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args);

    public ConversionService getConversionService() {
        return conversionService;
    }
}
