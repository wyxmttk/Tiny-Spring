package step02;

public class Test {
    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.registerBeanDefinition("mapper",new BeanDefinition(Mapper.class));
        PropertyValues propertyValues = new PropertyValues();
        propertyValues.addPropertyValue(new PropertyValue("name","ljk"));
        propertyValues.addPropertyValue(new PropertyValue("mapper",new DefaultBeanReference("mapper")));
        beanFactory.registerBeanDefinition("service",new BeanDefinition(Service.class,propertyValues));

        Service service = (Service) beanFactory.getBean("service");
        service.test();
        service.getMapper().test();

        Service newService = (Service) beanFactory.getBean("service");
        newService.test();
        newService.getMapper().test();

    }
}
class Service{
    private String name;

    private Mapper mapper;

    public Service(String name) {
        this.name = name;
    }

    public Service() {

    }

    public Mapper getMapper() {
        return mapper;
    }

    public void test(){
        System.out.println("service test: "+name);
    }
}
class Mapper{

    public void test(){
        System.out.println("mapper test");
    }

}

