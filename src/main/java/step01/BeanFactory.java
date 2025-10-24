package step01;

import java.util.concurrent.ConcurrentHashMap;

public class BeanFactory {

    private ConcurrentHashMap<String,BeanDefinition> map = new ConcurrentHashMap<>();

    public BeanDefinition getBean(String beanName){
        return map.get(beanName);
    }

    public void registerBeanDefinition(String beanName,BeanDefinition bd){
        map.put(beanName,bd);
    }


}
