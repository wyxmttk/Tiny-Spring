package wyxmttk.beanDefinition;

public interface BeanDefinitionRegistry {
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);

    boolean containsBeanDefinition(String beanName);

    String[] getBeanDefinitionNames();

//    BeanDefinition getBeanDefinition(String beanName);
}
