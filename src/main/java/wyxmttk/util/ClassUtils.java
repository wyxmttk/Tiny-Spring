package wyxmttk.util;

import java.lang.reflect.Type;

public class ClassUtils {

    public static boolean isCglibProxyClass(Class<?> clazz) {
        return clazz!=null && clazz.getName().contains("$$EnhancerByCGLIB$$");
    }

    public static Class<?> toClassImmediatelyIfPossible(Type type) {
        if (type instanceof Class) {
            return (Class<?>) type;
        }
        return null;
    }

}
