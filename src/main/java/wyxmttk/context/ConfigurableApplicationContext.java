package wyxmttk.context;

import wyxmttk.event.ApplicationEventPublisher;

public interface ConfigurableApplicationContext extends ApplicationContext, ApplicationEventPublisher {
    void refresh();

    void registerShutdownHook();

    void close();
}
