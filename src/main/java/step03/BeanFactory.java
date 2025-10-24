package step03;

//声明获取bean的方法
public interface BeanFactory {
     Object getBean(String beanName,Object[] args);
     //<T> T getBean(String beanName,Class<T> clazz);
}
