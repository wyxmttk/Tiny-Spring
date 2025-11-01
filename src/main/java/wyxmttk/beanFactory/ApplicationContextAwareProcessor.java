package wyxmttk.beanFactory;

import wyxmttk.context.ApplicationContext;
import wyxmttk.processor.BeanPostProcessor;

public class ApplicationContextAwareProcessor implements BeanPostProcessor {
    private final ApplicationContext applicationContext;

    public ApplicationContextAwareProcessor(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception {
        if (bean instanceof ApplicationContextAware applicationContextAware) {
            applicationContextAware.setApplicationContext(applicationContext);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
        return bean;
    }
}
