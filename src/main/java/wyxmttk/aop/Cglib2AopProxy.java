package wyxmttk.aop;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

public class Cglib2AopProxy implements AopProxy {

    private AdvisedSupport advisedSupport;

    public Cglib2AopProxy(AdvisedSupport advisedSupport) {
        this.advisedSupport = advisedSupport;
    }

    @Override
    public Object getProxy() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(advisedSupport.getTargetClass());
        enhancer.setCallback(new DynamicAdvisedInterceptor(advisedSupport));
        return enhancer.create();
    }
    private static class DynamicAdvisedInterceptor implements MethodInterceptor {

        private AdvisedSupport advisedSupport;

        public DynamicAdvisedInterceptor(AdvisedSupport advisedSupport) {
            this.advisedSupport = advisedSupport;
        }

        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            MethodInvocation methodInvocation = new CglibMethodInvocation(advisedSupport.getTarget(), method, objects, methodProxy);
            if(advisedSupport.getMethodMatcher().matches(method,advisedSupport.getTargetClass())) {
                org.aopalliance.intercept.MethodInterceptor interceptor = advisedSupport.getInterceptor();
                return interceptor.invoke(methodInvocation);
            }
            return methodInvocation.proceed();
        }
    }
    private static class CglibMethodInvocation extends ReflectiveMethodInvocation {

        private MethodProxy methodProxy;

        public CglibMethodInvocation(Object target, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
            super(target, method, args);
            this.methodProxy = methodProxy;
        }

        //TODO:为什么methodProxy.invoke()相比反射更快
        @Override
        public Object proceed() throws Throwable {
            return this.methodProxy.invoke(getTarget(),getArgs());
        }
    }
}
