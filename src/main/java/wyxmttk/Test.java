package wyxmttk;

import cn.hutool.core.io.IoUtil;
import wyxmttk.beanFactory.*;
import wyxmttk.context.ApplicationContext;
import wyxmttk.context.ClasspathXmlApplicationContext;
import wyxmttk.context.DisposableBean;
import wyxmttk.context.InitializingBean;
import wyxmttk.core.io.*;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class Test {
    public static ResourceLoader resourceLoader = new DefaultResourceLoader();
    public static void main(String[] args) {
        System.out.println("java.version=" + System.getProperty("java.version"));
        System.out.println("java.runtime.version=" + System.getProperty("java.runtime.version"));
//        testXml();
//        testURL();
//        testFileSystem();
//        testProcessor();
//        testInitAndDestroy();
//        testAware();
        testFactoryBeanAndScope();
    }

    public static void testFactoryBeanAndScope(){
        ClasspathXmlApplicationContext classpathXmlApplicationContext = new ClasspathXmlApplicationContext(new String[]{"classpath:beans.xml"});
        classpathXmlApplicationContext.registerShutdownHook();
        Service service = (Service) classpathXmlApplicationContext.getBean("service");
        service.test();
        Service service1 = (Service) classpathXmlApplicationContext.getBean("service");
        service1.test();
        System.out.println(service);
        System.out.println(service1);
    }

    public static void testAware() {
        ClasspathXmlApplicationContext classpathXmlApplicationContext = new ClasspathXmlApplicationContext(new String[]{"classpath:beans.xml"});
        classpathXmlApplicationContext.registerShutdownHook();
        Service service = (Service) classpathXmlApplicationContext.getBean("service");
        service.test();
    }

    public static void testInitAndDestroy() {
        ClasspathXmlApplicationContext classpathXmlApplicationContext = new ClasspathXmlApplicationContext(new String[]{"classpath:beans.xml"});
        classpathXmlApplicationContext.registerShutdownHook();
        Service service = (Service) classpathXmlApplicationContext.getBean("service");
        service.test();
    }
    public static void testProcessor(){
        ClasspathXmlApplicationContext classpathXmlApplicationContext = new ClasspathXmlApplicationContext(new String[]{"classpath:beans.xml"});
        Service service = (Service) classpathXmlApplicationContext.getBean("service");
        service.test();

    }
    public static void testXml(){
        DefaultListableBeanFactory defaultListableBeanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(defaultListableBeanFactory);
        xmlBeanDefinitionReader.loadBeanDefinition("classpath:beans.xml");
        Service service = (Service)defaultListableBeanFactory.getBean("service");
        service.test();
    }
    public static void testURL(){
        UrlResource urlResource=null;
        try {
             urlResource= new UrlResource(new URL("https://github.com/wyxmttk/Tiny-Spring/blob/main/pom.xml"));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        InputStream inputStream = urlResource.getInputStream();
        String content = IoUtil.readUtf8(inputStream);
        System.out.println(content);
    }
    public static void testFileSystem(){
        FileSystemResource resource=new FileSystemResource("/Applications/IDEAWorkspace/TinySpring/src/main/resources/beans.xml");
        DefaultListableBeanFactory defaultListableBeanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(defaultListableBeanFactory);
        xmlBeanDefinitionReader.loadBeanDefinition(resource);
        Service service = (Service)defaultListableBeanFactory.getBean("service");
        service.test();
    }
}



class Service implements InitializingBean, DisposableBean {
    private UserDao userDao;

    private String name;

    private Mapper mapper;

    private String location;

    private int age;

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setMapper(Mapper mapper) {
        this.mapper = mapper;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public Service(String name) {
        this.name = name;
    }

    public Service() {

    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Mapper getMapper() {
        return mapper;
    }

    public void test(){
        System.out.println(name);
        System.out.println(location);
        System.out.println(age);
        mapper.test();
        userDao.method();
    }

    public String getLocation() {
        return location;
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("destroy service");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("InitializingBean...");
        age=100;
    }
}

interface UserDao {
    void method();
}

class Mapper implements BeanClassLoaderAware, BeanFactoryAware, ApplicationContextAware {
    public void destroy(){
        System.out.println("destroy mapper");
    }
    public void init(){
        System.out.println("init mapper");
    }

    private ClassLoader beanClassLoader;

    private ApplicationContext applicationContext;

    private BeanFactory beanFactory;

    public void test(){

        System.out.println("mapper test");
        System.out.println("beanClassLoader:"+getBeanClassLoader()+"from mapper");
        System.out.println("applicationContext:"+getApplicationContext()+"from mapper");
        System.out.println("beanFactory:"+getBeanFactory()+"from mapper");
    }

    public ClassLoader getBeanClassLoader() {
        return beanClassLoader;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext=applicationContext;
    }

    @Override
    public void setBeanClassLoader(ClassLoader beanClassLoader) {
        this.beanClassLoader=beanClassLoader;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory=beanFactory;
    }
}

