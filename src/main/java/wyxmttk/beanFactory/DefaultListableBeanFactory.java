package wyxmttk.beanFactory;

import wyxmttk.beanDefinition.BeanDefinition;
import wyxmttk.beanDefinition.BeanDefinitionRegistry;

import java.util.HashMap;
import java.util.Map;

public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements BeanDefinitionRegistry {

    private final Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanName, beanDefinition);
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return beanDefinitionMap.containsKey(beanName);
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return beanDefinitionMap.keySet().toArray(new String[0]);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if (beanDefinition == null) {
            throw new RuntimeException("no bean definition found for " + beanName);
        }
        return beanDefinition;
    }

    @Override
    public void preInstantiateSingletons() {
        for (BeanDefinition beanDefinition : beanDefinitionMap.values()) {
            if (!beanDefinition.isLazyInit()){
                //TODO
            }
        }
    }
}
