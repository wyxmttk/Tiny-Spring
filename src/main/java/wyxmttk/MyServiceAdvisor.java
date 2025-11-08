package wyxmttk;

import org.aopalliance.aop.Advice;
import wyxmttk.annotation.Autowired;
import wyxmttk.annotation.Component;
import wyxmttk.aop.AspectJExpressionPointcutAdvisor;
import wyxmttk.aop.Pointcut;
@Component
public class MyServiceAdvisor extends AspectJExpressionPointcutAdvisor {

    @Autowired
    private Advice advice;
    @Autowired
    private Pointcut pointcut;

    @Override
    public Advice getAdvice() {
        return advice;
    }

    @Override
    public Pointcut getPointcut() {
        return pointcut;
    }
}
