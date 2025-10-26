package wyxmttk;

import cn.hutool.core.io.IoUtil;
import wyxmttk.context.ClasspathXmlApplicationContext;
import wyxmttk.core.io.*;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class Test {
    public static ResourceLoader resourceLoader = new DefaultResourceLoader();
    public static void main(String[] args) {
//        testXml();
//        testURL();
//        testFileSystem();
        testProcessor();

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



class Service{
    private String name;

    private Mapper mapper;

    private String location;

    private int age;

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
    }

    public String getLocation() {
        return location;
    }
}
class Mapper{

    public void test(){
        System.out.println("mapper test");
    }

}

