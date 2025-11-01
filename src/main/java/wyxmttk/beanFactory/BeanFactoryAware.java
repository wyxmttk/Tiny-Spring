package wyxmttk.beanFactory;

public interface BeanFactoryAware extends Aware {
    void setBeanFactory(BeanFactory beanFactory);
}
