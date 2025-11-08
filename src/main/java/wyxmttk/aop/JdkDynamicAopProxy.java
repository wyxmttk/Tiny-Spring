package wyxmttk.aop;

import cn.hutool.core.util.ClassUtil;
import org.aopalliance.intercept.MethodInterceptor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {

    private AdvisedSupport advisedSupport;

    public JdkDynamicAopProxy(AdvisedSupport advisedSupport) {
        this.advisedSupport = advisedSupport;
    }

    public AdvisedSupport getAdvisedSupport() {
        return advisedSupport;
    }

    public void setAdvisedSupport(AdvisedSupport advisedSupport) {
        this.advisedSupport = advisedSupport;
    }

    @Override
    public Object getProxy() {
        return java.lang.reflect.Proxy.newProxyInstance(ClassUtil.getClassLoader()
                ,advisedSupport.getTargetClass().getInterfaces(),this);
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        method.setAccessible(true);
        MethodInterceptor interceptor = advisedSupport.getInterceptor();
        MethodMatcher methodMatcher = advisedSupport.getMethodMatcher();
        if(methodMatcher.matches(method,advisedSupport.getTargetClass())) {
            return interceptor.invoke(new ReflectiveMethodInvocation(advisedSupport.getTarget(),method,args));
        }
        return method.invoke(advisedSupport.getTarget(),args);
    }
}
