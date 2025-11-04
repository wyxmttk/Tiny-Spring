package wyxmttk;

import wyxmttk.event.ApplicationListener;
import wyxmttk.event.ContextRefreshedEvent;

public class ContextRefreshedEventListener implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println("刷新事件:"+event.getSource());
    }
}
