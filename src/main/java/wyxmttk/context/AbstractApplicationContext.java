package wyxmttk.context;

import wyxmttk.beanDefinition.BeanDefinition;
import wyxmttk.beanDefinition.BeanDefinitionRegistry;
import wyxmttk.convert.Converter;
import wyxmttk.convert.ConverterFactory;
import wyxmttk.convert.DefaultConversionService;
import wyxmttk.convert.GenericConverter;
import wyxmttk.processor.ApplicationContextAwareProcessor;
import wyxmttk.beanFactory.ConfigurableListableBeanFactory;
import wyxmttk.core.io.DefaultResourceLoader;
import wyxmttk.event.*;
import wyxmttk.processor.BeanFactoryPostProcessor;
import wyxmttk.processor.BeanPostProcessor;

import java.util.Collection;
import java.util.Map;

public abstract class AbstractApplicationContext extends DefaultResourceLoader implements ConfigurableApplicationContext {
    private ApplicationEventMulticaster applicationEventMulticaster;

    public static final String APPLICATION_EVENT_MULTICASTER = "applicationEventMulticaster";

    @Override
    public void refresh() {
        System.out.println("application context: "+this+" printed by "+this.getClass().getName());
        refreshBeanFactory();

        ConfigurableListableBeanFactory beanFactory = getBeanFactory();

        prepareBeanFactory(beanFactory);

        invokeBeanFactoryPostProcessors(beanFactory);

        registerBeanPostProcessors(beanFactory);

        initApplicationEventMulticaster();

        registerApplicationListeners();

        finishBeanFactoryInitialization(beanFactory);

        finishRefresh();
    }

    private void finishBeanFactoryInitialization(ConfigurableListableBeanFactory beanFactory) {

        //预先注册无需懒加载的bean对象，例如Processor
        beanFactory.preInstantiateSingletons();
    }

    private void registerConversionService(ConfigurableListableBeanFactory beanFactory) {
        DefaultConversionService defaultConversionService = new DefaultConversionService();

        getBeansOfType(GenericConverter.class).values().forEach(defaultConversionService::addConverter);
        getBeansOfType(Converter.class).values().forEach(defaultConversionService::addConverter);
        getBeansOfType(ConverterFactory.class).values().forEach(defaultConversionService::addConverterFactory);

        beanFactory.registerConversionService(defaultConversionService);
    }

    private void prepareBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        //初始化ConversionService
        registerConversionService(beanFactory);

        //此处必须手动创建是因为ApplicationContext在创建后第一次获取时只能从这里拿
        beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));

    }

    private void finishRefresh() {
        publishEvent(new ContextRefreshedEvent(this));
    }

    private void registerApplicationListeners() {
        Collection<ApplicationListener> values = getBeansOfType(ApplicationListener.class).values();
        for (ApplicationListener listener : values) {
            applicationEventMulticaster.addApplicationListener(listener);
        }
    }

    private void initApplicationEventMulticaster() {
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        BeanDefinition bd = new BeanDefinition(SimpleApplicationEventMulticaster.class);
        ((BeanDefinitionRegistry) beanFactory).registerBeanDefinition(APPLICATION_EVENT_MULTICASTER, bd);
        applicationEventMulticaster=(ApplicationEventMulticaster) getBean(APPLICATION_EVENT_MULTICASTER);
    }





    @Override
    public void publishEvent(ApplicationEvent event) {
        applicationEventMulticaster.multicastEvent(event);
    }

    @Override
    public void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    @Override
    public void close() {
        publishEvent(new ContextClosedEvent(this));

        getBeanFactory().destroySingletons();
    }

    protected abstract void refreshBeanFactory();

    protected abstract ConfigurableListableBeanFactory getBeanFactory();

    private void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        Map<String, BeanFactoryPostProcessor> beansOfType = beanFactory.getBeansOfType(BeanFactoryPostProcessor.class);

        for(BeanFactoryPostProcessor processor:beansOfType.values()){
            try {
                //因为beanDefinition已经加载好，所以这边直接让processor处理
                processor.postProcessBeanFactory(beanFactory);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        Map<String, BeanPostProcessor> beansOfType = beanFactory.getBeansOfType(BeanPostProcessor.class);

        for(BeanPostProcessor processor:beansOfType.values()){
            //由于bean是懒加载，所以将processor加到beanFactory让其创建bean时使用
            beanFactory.addBeanPostProcessor(processor);
        }
    }

    //做一层封装
    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) {
        return getBeanFactory().getBeansOfType(type);
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return getBeanFactory().getBeanDefinitionNames();
    }

    @Override
    public Object getBean(String beanName, Object... args) {
        return getBeanFactory().getBean(beanName, args);
    }
}
