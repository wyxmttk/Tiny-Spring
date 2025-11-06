package wyxmttk.aop;

import java.lang.reflect.Method;

//不止方法能有通知，构造器和属性也可以
public interface MethodBeforeAdvice extends BeforeAdvice {

    void before(Method method, Object[] args, Object target);

}
