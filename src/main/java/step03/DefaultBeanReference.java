package step03;

public class DefaultBeanReference implements BeanReference {

    private String beanName;

    public DefaultBeanReference(String beanName) {
        this.beanName = beanName;
    }
    @Override
    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
}
