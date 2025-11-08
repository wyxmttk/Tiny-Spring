package wyxmttk.aop;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import wyxmttk.annotation.Component;
import wyxmttk.beanDefinition.PropertyValues;
import wyxmttk.beanFactory.BeanFactory;
import wyxmttk.beanFactory.BeanFactoryAware;
import wyxmttk.beanFactory.DefaultListableBeanFactory;
import wyxmttk.processor.InstantiationAwareBeanPostProcessor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
@Component
public class DefaultAdvisorAutoProxyCreator implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {
    private DefaultListableBeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }

    public DefaultListableBeanFactory getBeanFactory() {
        return beanFactory;
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) {
        return null;
    }

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) {
        return pvs;
    }

    private boolean isInfrastructureClass(Class<?> beanClass) {
        return Advice.class.isAssignableFrom(beanClass) || Pointcut.class.isAssignableFrom(beanClass) || Advisor.class.isAssignableFrom(beanClass);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
        if(isInfrastructureClass(bean.getClass())) {
            return bean;
        }
        Collection<AspectJExpressionPointcutAdvisor> advisors =
                beanFactory.getBeansOfType(AspectJExpressionPointcutAdvisor.class).values();
        for (AspectJExpressionPointcutAdvisor advisor : advisors) {
            if(!advisor.getPointcut().getClassFilter().matches(bean.getClass())) {
                continue;
            }
            Advice advice = advisor.getAdvice();
            AdvisedSupport advisedSupport=new AdvisedSupport(new TargetSource(bean),
                    (MethodInterceptor) advice,advisor.getPointcut().getMethodMatcher());
            return new ProxyFactory(advisedSupport).getProxy();
        }
        return bean;
    }
}
