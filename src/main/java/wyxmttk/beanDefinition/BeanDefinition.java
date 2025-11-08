package wyxmttk.beanDefinition;

import wyxmttk.beanFactory.ConfigurableBeanFactory;

public class BeanDefinition {
    private Class<?> beanClass;

    //实例化后需要注入的属性
    //封装了属性集合及相关操作
    private PropertyValues propertyValues;

    private String initMethodName;

    private String destroyMethodName;

    public static final String SCOPE_SINGLETON = ConfigurableBeanFactory.SCOPE_SINGLETON;
    public static final String SCOPE_PROTOTYPE = ConfigurableBeanFactory.SCOPE_PROTOTYPE;

    private String scope = SCOPE_SINGLETON;

    public boolean isSingleton() {
        return SCOPE_SINGLETON.equals(scope);
    }

    public String getScope() {
        return scope;
    }

    public void setPrototype() {
        this.scope = SCOPE_PROTOTYPE;
    }

    public void setSingleton() {
        this.scope = SCOPE_SINGLETON;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getInitMethodName() {
        return initMethodName;
    }

    public void setInitMethodName(String initMethodName) {
        this.initMethodName = initMethodName;
    }

    public String getDestroyMethodName() {
        return destroyMethodName;
    }

    public void setDestroyMethodName(String destroyMethodName) {
        this.destroyMethodName = destroyMethodName;
    }

    private boolean lazyInit = true;

    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    public boolean isLazyInit() {
        return lazyInit;
    }

    public BeanDefinition(Class<?> beanClass, PropertyValues propertyValues) {
        this.beanClass = beanClass;
        this.propertyValues = propertyValues;
    }

    public BeanDefinition(Class<?> beanClass){
        this.beanClass = beanClass;
        setPropertyValues(null);
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    public void setPropertyValues(PropertyValues propertyValues) {
        this.propertyValues = propertyValues == null ? new PropertyValues() : propertyValues;
    }

    public PropertyValues getPropertyValues() {
        return propertyValues;
    }
}
