package wyxmttk.singleton;

public interface SingletonBeanRegistry {
     Object getSingleton(String beanName);

     void registerSingleton(String beanName, Object bean);
}
