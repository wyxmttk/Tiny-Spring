package wyxmttk.beanFactory;

public interface ConfigurableBeanFactory extends HierarchicalBeanFactory {
    String SCOPE_SINGLETON = "singleton";
    String SCOPE_PROTOTYPE = "prototype";

    void destroySingletons();

    ClassLoader getBeanClassLoader();
}
