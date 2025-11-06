package wyxmttk;

import wyxmttk.beanFactory.FactoryBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class UserDaoBeanFactory implements FactoryBean<UserDao> {
    @Override
    public UserDao getObject() {
        InvocationHandler handler = (proxy, method, args) -> {
            System.out.println("被代理method:"+method.getName());
            return null;
        };
        return (UserDao) Proxy.newProxyInstance(UserDao.class.getClassLoader(), new Class[]{UserDao.class}, handler);

    }

    @Override
    public Class<UserDao> getObjectType() {
        return UserDao.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
