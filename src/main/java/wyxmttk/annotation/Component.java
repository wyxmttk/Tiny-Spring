package wyxmttk.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Component {
    //之所以不改成name是因为java注解语法中，如果一个注解只有一个属性且为value，那使用注解时注入这个属性无需指明属性名
    String value() default "";
}
