package wyxmttk;

import wyxmttk.event.ApplicationListener;
import wyxmttk.event.ContextClosedEvent;

public class ContextClosedEventListener implements ApplicationListener<ContextClosedEvent> {
    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        System.out.println("关闭事件:"+event.getSource());
    }
}
