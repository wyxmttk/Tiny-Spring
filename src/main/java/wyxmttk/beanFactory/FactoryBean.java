package wyxmttk.beanFactory;

public interface FactoryBean<T> {
    //该工厂只生产特定类的对象，因此可用泛型
    T getObject();

    Class<T> getObjectType();

    boolean isSingleton();
}
