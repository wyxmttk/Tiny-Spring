package wyxmttk;

import wyxmttk.annotation.Component;
import wyxmttk.aop.BeforeAdvice;
import wyxmttk.aop.MethodBeforeAdvice;
import wyxmttk.aop.MethodBeforeAdviceInterceptor;

import java.lang.reflect.Method;

@Component("advice")
public class MyInterceptor extends MethodBeforeAdviceInterceptor{
    public MyInterceptor() {
        super.setMethodBeforeAdvice(new TestMethodBeforeAdvice());
    }
}

class TestMethodBeforeAdvice implements MethodBeforeAdvice {
    @Override
    public void before(Method method, Object[] args, Object target) {
        System.out.println(method.getName()+": 前置通知成功！！！");
    }
}
