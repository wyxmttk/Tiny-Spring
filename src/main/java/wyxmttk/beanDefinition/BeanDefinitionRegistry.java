package wyxmttk.beanDefinition;

//对外提供注册功能，因此命名为registry
public interface BeanDefinitionRegistry {
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);

    boolean containsBeanDefinition(String beanName);

    String[] getBeanDefinitionNames();

//    BeanDefinition getBeanDefinition(String beanName);
}
