package wyxmttk.aop;

public class ProxyFactory {

    private AdvisedSupport advisedSupport;

    private AopProxy aopProxy;

    private Object proxy;

    public ProxyFactory(AdvisedSupport advisedSupport) {
        this.advisedSupport = advisedSupport;
    }

    public AdvisedSupport getAdvisedSupport() {
        return advisedSupport;
    }

    public void setAdvisedSupport(AdvisedSupport advisedSupport) {
        this.advisedSupport = advisedSupport;
    }

    public Object getProxy() {
        return getAopProxy().getProxy();
    }

    public AopProxy getAopProxy() {
        if (aopProxy == null) {
            if(advisedSupport.isProxyTargetClass()){
                aopProxy=new Cglib2AopProxy(advisedSupport);
            }else{
                aopProxy=new JdkDynamicAopProxy(advisedSupport);
            }
        }
        return aopProxy;
    }
}
