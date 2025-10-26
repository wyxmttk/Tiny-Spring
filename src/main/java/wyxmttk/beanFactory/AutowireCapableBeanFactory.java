package wyxmttk.beanFactory;

public interface AutowireCapableBeanFactory extends BeanFactory {
    Object applyBeanPostProcessorsBeforeInitialization(Object bean, String beanName);
    Object applyBeanPostProcessorsAfterInitialization(Object bean, String beanName);
}
