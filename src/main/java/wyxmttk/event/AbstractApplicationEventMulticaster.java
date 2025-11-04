package wyxmttk.event;

import cn.hutool.core.util.ClassUtil;
import wyxmttk.beanFactory.BeanFactory;
import wyxmttk.beanFactory.BeanFactoryAware;
import wyxmttk.util.ClassUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

//在容器运行全程发挥作用，定位是容器管理的bean，因此选择感知容器的方式拿到beanFactory而不是构造方法
public abstract class AbstractApplicationEventMulticaster implements ApplicationEventMulticaster, BeanFactoryAware {
    private final Set<ApplicationListener<ApplicationEvent>> applicationListeners = new LinkedHashSet<>();

    private BeanFactory beanFactory;

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public Set<ApplicationListener<ApplicationEvent>> getApplicationListeners() {
        return applicationListeners;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public void addApplicationListener(ApplicationListener<?> applicationListener) {
        applicationListeners.add((ApplicationListener<ApplicationEvent>) applicationListener);
    }

    @Override
    public void removeApplicationListener(ApplicationListener<?> applicationListener) {
        applicationListeners.remove(applicationListener);
    }

    protected Collection<ApplicationListener<ApplicationEvent>> getApplicationListeners(ApplicationEvent event) {
        LinkedList<ApplicationListener<ApplicationEvent>> applicationListeners = new LinkedList<>();
        for (ApplicationListener<ApplicationEvent> applicationListener : this.applicationListeners) {
            if(supportEvent(applicationListener,event)){
                applicationListeners.add(applicationListener);
            }
        }
        return applicationListeners;
    }
    protected boolean supportEvent(ApplicationListener<ApplicationEvent> applicationListener, ApplicationEvent event) {
        Class<?> clazz = applicationListener.getClass();
        clazz = ClassUtils.isCglibProxyClass(clazz) ? clazz.getSuperclass() : clazz;
        //获取自身直接实现的所有接口里支持泛型信息的接口
        Type[] genericInterfaces = clazz.getGenericInterfaces();
        for (Type genericInterface : genericInterfaces) {
            //判断是否真的有带泛型信息
            if(genericInterface instanceof ParameterizedType parameterizedType){
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                for (Type actualTypeArgument : actualTypeArguments) {
                    Class<?> clazzFromType = ClassUtils.toClassImmediatelyIfPossible(actualTypeArgument);
                    if(clazzFromType!=null && clazzFromType.isAssignableFrom(event.getClass())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


}
