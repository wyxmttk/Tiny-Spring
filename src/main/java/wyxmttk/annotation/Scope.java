package wyxmttk.annotation;

import java.lang.annotation.*;

//允许Method是因为配置类注册bean时会用到
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Scope {
    String value() default "singleton";
}
