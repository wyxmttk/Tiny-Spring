package wyxmttk;

import wyxmttk.annotation.Component;
import wyxmttk.aop.AspectJExpressionPointcut;
import wyxmttk.aop.Pointcut;
@Component
public class MyServicePointcut extends AspectJExpressionPointcut {

    public MyServicePointcut() {
        super("execution(* wyxmttk.Service.*(..))");
    }

}
