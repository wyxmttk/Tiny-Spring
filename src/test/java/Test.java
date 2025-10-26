import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;
import wyxmttk.singleton.DefaultSingletonBeanRegistry;

public class Test {
    public static void main(String[] args) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(DefaultSingletonBeanRegistry.class);
        enhancer.setCallback(new NoOp() {
            @Override
            public int hashCode() {
                return super.hashCode();
            }
        });
        Object o = enhancer.create();
        Object o1 = enhancer.create();
        System.out.println(o == o1);
    }
}
