package wyxmttk.context;

import cn.hutool.core.util.StrUtil;
import wyxmttk.beanDefinition.BeanDefinition;

import java.lang.reflect.Method;

public class DisposableBeanAdapter implements DisposableBean {
    //TODO:这适配器对象什么时候创的，需要存到哪去吗？
    private final String beanName;
    private final Object bean;
    private final BeanDefinition beanDefinition;

    public DisposableBeanAdapter(String beanName, Object bean, BeanDefinition beanDefinition) {
        this.beanName = beanName;
        this.bean = bean;
        this.beanDefinition = beanDefinition;
    }

    @Override
    public void destroy() throws Exception {
        if (bean instanceof DisposableBean disposableBean) {
            disposableBean.destroy();
        }
        String destroyMethodName = beanDefinition.getDestroyMethodName();
        if(StrUtil.isBlank(destroyMethodName)) {
            return;
        }
        //防止重复销毁，之所以初始化时不用是即便重复初始化了一般也不会有太大影响
        if(bean instanceof DisposableBean){
            return;
        }
        Method method = beanDefinition.getBeanClass().getDeclaredMethod(destroyMethodName);
        //TODO:模块化
        method.setAccessible(true);
        method.invoke(bean);
    }
}
