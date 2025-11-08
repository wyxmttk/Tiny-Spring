package wyxmttk;

import wyxmttk.annotation.Autowired;
import wyxmttk.annotation.Component;
import wyxmttk.annotation.Qualifier;
import wyxmttk.annotation.Value;
import wyxmttk.context.DisposableBean;
import wyxmttk.context.InitializingBean;

import java.util.Properties;
@Component("myService")
class Service implements InitializingBean, DisposableBean {

    @Value("${token}")
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private UserDao userDao;

    private String name;

    @Autowired
    @Qualifier("real")
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

    public void test() {
        System.out.println(name);
        System.out.println(location);
        System.out.println(age);
        System.out.println(mapper);
        if(mapper != null) {
            mapper.test();
        }
//        if(userDao != null) {
//            userDao.method();
//        }

    }
    public void testAnnotation() {
        System.out.println(token);
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
        age = 100;
    }
}
