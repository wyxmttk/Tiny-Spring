package wyxmttk.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
//注解在构造器或方法上时，会尝试对所有参数进行注入
@Target({ElementType.FIELD,ElementType.CONSTRUCTOR,ElementType.METHOD})
public @interface Autowired {
}
