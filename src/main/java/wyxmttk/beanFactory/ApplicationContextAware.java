package wyxmttk.beanFactory;

import wyxmttk.context.ApplicationContext;

public interface ApplicationContextAware extends Aware {
    void setApplicationContext(ApplicationContext applicationContext);
}
