package wyxmttk.core.io;

import wyxmttk.beanDefinition.BeanDefinitionRegistry;

public interface BeanDefinitionReader {
    BeanDefinitionRegistry getRegistry();

    ResourceLoader getResourceLoader();

    void loadBeanDefinition(Resource resource);

    void loadBeanDefinition(String location);

    void loadBeanDefinitions(Resource... resource);
}
