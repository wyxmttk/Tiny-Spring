package step03.core.io;

import step03.BeanDefinitionRegistry;

public interface BeanDefinitionReader {
    BeanDefinitionRegistry getRegistry();

    ResourceLoader getResourceLoader();

    void loadBeanDefinition(Resource resource);

    void loadBeanDefinition(String location);

    void loadBeanDefinitions(Resource... resource);
}
