package wyxmttk.annotation;

import wyxmttk.beanDefinition.BeanDefinition;
import wyxmttk.beanDefinition.BeanDefinitionRegistry;

import java.util.Set;

public class ClassPathBeanDefinitionScanner extends ClassPathScanningCandidateComponentProvider{

    private BeanDefinitionRegistry registry;

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public void doScan(String... basePackages) {
        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidateComponents = findCandidateComponents(basePackage);
            for (BeanDefinition beanDefinition : candidateComponents) {
                setScope(beanDefinition);
                registry.registerBeanDefinition(determineBeanName(beanDefinition), beanDefinition);
            }
        }
    }
    private String determineBeanName(BeanDefinition beanDefinition) {
        Class<?> beanClass = beanDefinition.getBeanClass();
        Component annotation = beanClass.getAnnotation(Component.class);
        String value = annotation.value();
        return "".equals(value) ? beanClass.getSimpleName() : value;
    }
    private void setScope(BeanDefinition beanDefinition) {
        Class<?> beanClass = beanDefinition.getBeanClass();
        if(beanClass.isAnnotationPresent(Scope.class)) {
            Scope scope = beanClass.getAnnotation(Scope.class);
            String value = scope.value();
            if(BeanDefinition.SCOPE_SINGLETON.equals(value)||BeanDefinition.SCOPE_PROTOTYPE.equals(value)) {
                beanDefinition.setScope(value);
            }
        }
    }

}
