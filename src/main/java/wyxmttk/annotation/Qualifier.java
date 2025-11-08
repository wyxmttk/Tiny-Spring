package wyxmttk.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
//注解在方法时，用于修饰返回的bean
//注解在某个参数时，表示该参数注入时用修饰词为xx的bean
//若注解在方法上，一般为set方法，等同于注解在参数
//注解在注解类上，表示该注解继承Qualifier("xxx")的功能，比如@Qualifier("wechat")需要硬编码wechat，那我们创个@Wechat注解类，然后用
//@Qualifier("wechat")注解它，那该注解就等同于@Qualifier("wechat")，起到了封装解耦效果
@Target({ElementType.FIELD,ElementType.PARAMETER,ElementType.METHOD,ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Inherited
@Documented
//前提一定要配合@Autowired用
//qualifier译为修饰词，在注册bean时，在类上添加该注解可设置对该bean的修饰，在注入时该bean时如果存在于其类型相同(父类或接口相同)的bean
//可用qualifier指定到底要注入哪个
public @interface Qualifier {
    String value()default "";
}
