package step01;

public class Test {
    public static void main(String[] args) {
        BeanFactory bf = new BeanFactory();
        BeanDefinition bd = new BeanDefinition(new Service());
        bf.registerBeanDefinition("service",bd);
        Service service =(Service) (bf.getBean("service").getBean());
        service.test();
    }
}
class Service{
    public void test(){
        System.out.println("test");
    }
}