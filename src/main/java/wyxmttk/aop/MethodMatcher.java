package wyxmttk.aop;

import java.lang.reflect.Method;

public interface MethodMatcher {

    //之所以需要传target是因为切入点表达式一般是基于类的，而用JDK动态代理生成代理对象
    boolean matches(Method method,Class<?> target);

}
