package wyxmttk.aop;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import wyxmttk.beanDefinition.PropertyValues;
import wyxmttk.beanFactory.BeanFactory;
import wyxmttk.beanFactory.BeanFactoryAware;
import wyxmttk.beanFactory.DefaultListableBeanFactory;
import wyxmttk.processor.InstantiationAwareBeanPostProcessor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

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
        if(isInfrastructureClass(beanClass)) {
            return null;
        }
        Collection<AspectJExpressionPointcutAdvisor> advisors =
                beanFactory.getBeansOfType(AspectJExpressionPointcutAdvisor.class).values();
        for (AspectJExpressionPointcutAdvisor advisor : advisors) {
            if(!advisor.getPointcut().getClassFilter().matches(beanClass)){
                continue;
            }
            Advice advice = advisor.getAdvice();
            AdvisedSupport advisedSupport;
            try {
                Constructor<?> constructor = beanClass.getConstructor();
                constructor.setAccessible(true);
                advisedSupport=new AdvisedSupport(constructor.newInstance(),
                        (MethodInterceptor) advice,advisor.getPointcut().getMethodMatcher());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
            return new ProxyFactory(advisedSupport).getProxy();
        }
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
        return bean;
    }
}
