package wyxmttk.event;

import wyxmttk.beanFactory.BeanFactory;

import java.util.Collection;

public class SimpleApplicationEventMulticaster extends AbstractApplicationEventMulticaster {

    @Override
    public void multicastEvent(ApplicationEvent applicationEvent) {
        Collection<ApplicationListener<ApplicationEvent>> applicationListeners = getApplicationListeners(applicationEvent);
        for (ApplicationListener<ApplicationEvent> applicationListener : applicationListeners) {
            applicationListener.onApplicationEvent(applicationEvent);
        }
    }
}
