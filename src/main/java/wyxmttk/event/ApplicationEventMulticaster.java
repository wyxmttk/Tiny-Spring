package wyxmttk.event;

public interface ApplicationEventMulticaster {
    void addApplicationListener(ApplicationListener<?> applicationListener);

    void removeApplicationListener(ApplicationListener<?> applicationListener);

    void multicastEvent(ApplicationEvent applicationEvent);
}
