package wyxmttk;

import wyxmttk.processor.BeanPostProcessor;

public class MyBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception {
        if (!beanName.equals("service")) {
            return bean;
        }
        Service service = (Service) bean;
        service.setName("罗景康");
        return service;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
        if (!beanName.equals("service")) {
            return bean;
        }
        Service service = (Service) bean;
        service.setLocation("字节跳动");
        return service;
    }
}
