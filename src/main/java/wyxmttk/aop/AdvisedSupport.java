package wyxmttk.aop;


import org.aopalliance.intercept.MethodInterceptor;

//对一个完整通知功能的支持，每个advisedSupport对象封装一个需要被代理的对象，拦截器(通知＋原方法)和方法匹配器
public class AdvisedSupport {
    //是否用cglib代理
    private boolean isProxyTargetClass = true;
    //需要被代理的对象
    private Object target;
    //应为拦截器链
    private MethodInterceptor interceptor;

    private MethodMatcher methodMatcher;

    public boolean isProxyTargetClass() {
        return isProxyTargetClass;
    }

    public void setProxyTargetClass(boolean proxyTargetClass) {
        isProxyTargetClass = proxyTargetClass;
    }

    public AdvisedSupport(Object target, MethodInterceptor interceptor, MethodMatcher methodMatcher) {
        if(target == null || interceptor == null || methodMatcher == null) {
            throw new IllegalArgumentException();
        }
        this.target = target;
        this.interceptor = interceptor;
        this.methodMatcher = methodMatcher;
    }



    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public MethodInterceptor getInterceptor() {
        return interceptor;
    }

    public void setInterceptor(MethodInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    public MethodMatcher getMethodMatcher() {
        return methodMatcher;
    }

    public void setMethodMatcher(MethodMatcher methodMatcher) {
        this.methodMatcher = methodMatcher;
    }
}
