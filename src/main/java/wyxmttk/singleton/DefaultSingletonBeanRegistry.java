package wyxmttk.singleton;

import wyxmttk.context.DisposableBean;

import java.util.HashMap;
import java.util.Map;
//封装操作单例map的方法，不关心单例如何创建而来
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {
    //是为了区分从Map里获取某个值时，键存不存在。如果存在，值会为NULL_OBJECT，以表明这个工厂创建的就是null对象，并非键值对不存在导致的返回null
    public static final Object NULL_OBJECT = new Object();

    private final Map<String, Object> singletonObjects = new HashMap<>();

    private final Map<String, DisposableBean> disposableBeans = new HashMap<>();

    //如果一个类实现了某个接口（不论是直接还是通过父类），
    //只要继承链中任何父类提供了该方法的实现，就算这个接口方法被实现了。
    public void destroySingletons(){
        for (Map.Entry<String, DisposableBean> entry : disposableBeans.entrySet()) {
            try {
                entry.getValue().destroy();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public DisposableBean getDisposableBean(String beanName) {
        return disposableBeans.get(beanName);
    }
    public void registerDisposableBean(String beanName, DisposableBean disposableBean) {
        disposableBeans.put(beanName, disposableBean);
    }

    @Override
    public Object getSingleton(String beanName) {
        return singletonObjects.get(beanName);
    }

    @Override
    public void registerSingleton(String beanName, Object singletonObject) {
        singletonObjects.put(beanName, singletonObject);
    }


}
