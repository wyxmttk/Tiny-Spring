package step02;

public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory {

    @Override
    public Object getBean(String beanName,Object... args) {
        Object singleton = getSingleton(beanName);
        if (singleton != null) {
            System.out.println("map中存在单例"+beanName);
            return singleton;
        }
        System.out.println("单例不存在，创建");
        BeanDefinition beanDefinition = getBeanDefinition(beanName);
        return createBean(beanName, beanDefinition, args);
    }

    protected abstract BeanDefinition getBeanDefinition(String beanName);

    protected abstract Object createBean(String beanName, BeanDefinition beanDefinition,Object[] args);
}
