package wyxmttk.aop;

import wyxmttk.util.ClassUtils;

public class TargetSource {

    private Object target;

    public TargetSource(Object target) {
        this.target = target;
    }

    public Object getTarget() {
        return target;
    }
    public void setTarget(Object target) {
        this.target = target;
    }

    public Class<?> getTargetClass() {
        Class<?> aClass = target.getClass();
        return ClassUtils.isCglibProxyClass(aClass) ? aClass.getSuperclass() : aClass;
    }
}
