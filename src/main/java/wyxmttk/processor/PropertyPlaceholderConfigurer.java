package wyxmttk.processor;

import wyxmttk.beanDefinition.BeanDefinition;
import wyxmttk.beanDefinition.PropertyValue;
import wyxmttk.beanDefinition.PropertyValues;
import wyxmttk.beanFactory.ConfigurableListableBeanFactory;
import wyxmttk.core.io.DefaultResourceLoader;
import wyxmttk.core.io.Resource;

import java.util.Properties;
//主要服务于xml配置实现字段注入的功能，在解析@Value注解前执行
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
                propertyValues.addPropertyValue(new PropertyValue(pV.getName(), resolvePlaceholders(strVal,properties)));
            }
        }
        PlaceholderResolvingStringValueResolver resolver = new PlaceholderResolvingStringValueResolver(properties);
        beanFactory.addEmbeddedValueResolver(resolver);
    }

    private String resolvePlaceholders(String str,Properties properties) {
        //防御性编程，防止之后维护的时候误替换原字符串变量
        String strVal = str;
        StringBuilder builder = new StringBuilder(strVal);
        int start = strVal.indexOf(DEFAULT_PLACEHOLDER_PREFIX);
        int end = strVal.indexOf(DEFAULT_PLACEHOLDER_SUFFIX);
        if(start != -1 && end != -1 && start < end) {
            String key = strVal.substring(start + 2, end);
            String property = properties.getProperty(key);
            builder.replace(start, end+1, property);
        }
        return builder.toString();
    }
    private class PlaceholderResolvingStringValueResolver implements StringValueResolver {

        private final Properties properties;

        public PlaceholderResolvingStringValueResolver(Properties properties) {
            this.properties = properties;
        }

        @Override
        public String resolve(String strVal) {
            return resolvePlaceholders(strVal, properties);
        }
    }
}
