package wyxmttk;

import wyxmttk.aop.BeforeAdvice;
import wyxmttk.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

public class TestMethodBeforeAdvice implements MethodBeforeAdvice {
    @Override
    public void before(Method method, Object[] args, Object target) {
        System.out.println(method.getName()+": 前置通知成功！！！");
    }
}
