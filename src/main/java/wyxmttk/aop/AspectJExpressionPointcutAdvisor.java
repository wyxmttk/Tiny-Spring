package wyxmttk.aop;

import org.aopalliance.aop.Advice;

public class AspectJExpressionPointcutAdvisor implements PointcutAdvisor {

    private Advice advice;

    private AspectJExpressionPointcut pointcut;

    private String expression;

    public AspectJExpressionPointcutAdvisor(Advice advice, String expression) {
        this.advice = advice;
        this.expression = expression;
    }

    public AspectJExpressionPointcutAdvisor() {

    }



    @Override
    public Advice getAdvice() {
        return advice;
    }

    public void setAdvice(Advice advice) {
        this.advice = advice;
    }

    public void setPointcut(AspectJExpressionPointcut pointcut) {
        this.pointcut = pointcut;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public Pointcut getPointcut(){
        if(pointcut == null){
            pointcut = new AspectJExpressionPointcut(expression);
        }
        return pointcut;
    }
}
