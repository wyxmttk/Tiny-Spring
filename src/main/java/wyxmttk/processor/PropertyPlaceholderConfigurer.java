package wyxmttk.processor;

import wyxmttk.beanDefinition.BeanDefinition;
import wyxmttk.beanDefinition.PropertyValue;
import wyxmttk.beanDefinition.PropertyValues;
import wyxmttk.beanFactory.ConfigurableListableBeanFactory;
import wyxmttk.core.io.DefaultResourceLoader;
import wyxmttk.core.io.Resource;

import java.util.Properties;

public class PropertyPlaceholderConfigurer implements BeanFactoryPostProcessor {

    public static final String DEFAULT_PLACEHOLDER_PREFIX = "${";

    public static final String DEFAULT_PLACEHOLDER_SUFFIX = "}";

    private String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws Exception {
        DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource(location);
        Properties properties = new Properties();
        properties.load(resource.getInputStream());
        String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
        for (String beanName : beanDefinitionNames) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
            PropertyValues propertyValues = beanDefinition.getPropertyValues();
            for(PropertyValue pV:propertyValues.getPropertyValueList()){
                Object value = pV.getValue();
                if(!(value instanceof String strVal)) {
                    continue;
                }
                StringBuilder builder = new StringBuilder(strVal);
                int start = strVal.indexOf(DEFAULT_PLACEHOLDER_PREFIX);
                int end = strVal.indexOf(DEFAULT_PLACEHOLDER_SUFFIX);
                if(start != -1 && end != -1 && start < end) {
                    String key = strVal.substring(start + 2, end);
                    String property = properties.getProperty(key);
                    builder.replace(start, end+1, property);
                }
                propertyValues.addPropertyValue(new PropertyValue(pV.getName(), builder.toString()));
            }
        }

    }
}
