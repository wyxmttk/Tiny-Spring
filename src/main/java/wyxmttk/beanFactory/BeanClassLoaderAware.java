package wyxmttk.beanFactory;

public interface BeanClassLoaderAware extends Aware {
    void setBeanClassLoader(ClassLoader beanClassLoader);
}
