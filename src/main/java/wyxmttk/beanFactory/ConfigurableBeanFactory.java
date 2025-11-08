package wyxmttk.beanFactory;

import wyxmttk.processor.StringValueResolver;

public interface ConfigurableBeanFactory extends HierarchicalBeanFactory {
    String SCOPE_SINGLETON = "singleton";
    String SCOPE_PROTOTYPE = "prototype";

    void destroySingletons();

    ClassLoader getBeanClassLoader();

    void addEmbeddedValueResolver(StringValueResolver resolver);

    String resolveEmbeddedValue(String value);
}
