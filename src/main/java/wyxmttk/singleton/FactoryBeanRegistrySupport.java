package wyxmttk.singleton;

import wyxmttk.beanFactory.FactoryBean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FactoryBeanRegistrySupport extends DefaultSingletonBeanRegistry{
    private final Map<String,Object> producedSingletons = new ConcurrentHashMap<>();

    protected Object getProducedSingleton(String factoryName) {
        Object singleton = producedSingletons.get(factoryName);
        return singleton != NULL_OBJECT ? singleton:null ;
    }

    protected Object getObjectFromFactoryBean(FactoryBean factoryBean, String factoryName) {
        if(factoryBean.isSingleton()){
            Object o = producedSingletons.get(factoryName);
            if(o == null){
                o = factoryBean.getObject();
                producedSingletons.put(factoryName, o!=null?o:NULL_OBJECT);
            }
            return o!=NULL_OBJECT?o:null;
        }
        return factoryBean.getObject();
    }





}
