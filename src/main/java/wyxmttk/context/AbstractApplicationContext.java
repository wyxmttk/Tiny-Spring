package wyxmttk.context;

import wyxmttk.beanFactory.ConfigurableListableBeanFactory;
import wyxmttk.core.io.DefaultResourceLoader;
import wyxmttk.core.io.ResourceLoader;
import wyxmttk.processor.BeanFactoryPostProcessor;
import wyxmttk.processor.BeanPostProcessor;

import java.util.Map;

public abstract class AbstractApplicationContext extends DefaultResourceLoader implements ConfigurableApplicationContext {
    @Override
    public void refresh() {
        refreshBeanFactory();

        ConfigurableListableBeanFactory beanFactory = getBeanFactory();

        invokeBeanFactoryPostProcessors(beanFactory);

        registerBeanPostProcessors(beanFactory);
        //预先注册无需懒加载的bean对象，例如Processor
        beanFactory.preInstantiateSingletons();
    }

    @Override
    public void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    @Override
    public void close() {
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
