package wyxmttk.context;

import wyxmttk.beanFactory.DefaultListableBeanFactory;
import wyxmttk.core.io.XmlBeanDefinitionReader;

public abstract class AbstractXmlApplicationContext extends AbstractRefreshableApplicationContext{
    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) {
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory,this);
        String[] configLocations = getConfigLocations();
        if (configLocations != null) {
            for(String configLocation : configLocations) {
                reader.loadBeanDefinition(configLocation);
            }
        }
    }
    protected abstract String[] getConfigLocations();
}
