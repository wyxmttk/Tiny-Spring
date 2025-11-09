package wyxmttk.util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

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

    public static Class<?>[] getParameterizedTypesOfGenericInterfaces(Class<?> clazz) {
        while(isCglibProxyClass(clazz)) {
            clazz = clazz.getSuperclass();
        }
        Type[] genericInterfaces = clazz.getGenericInterfaces();
        ArrayList<Class<?>> types = new ArrayList<>();
        for(Type genericInterface : genericInterfaces) {
            if (genericInterface instanceof ParameterizedType parameterizedType) {
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                for(Type actualTypeArgument : actualTypeArguments) {
                    Class<?> classImmediatelyIfPossible = toClassImmediatelyIfPossible(actualTypeArgument);
                    if (classImmediatelyIfPossible != null) {
                        types.add(classImmediatelyIfPossible);
                    }
                }
            }
        }
        return types.toArray(new Class<?>[0]);
    }

    public static Class<?> getClassOfFiled(String fieldName,Object object) {
        Field field=null;
        Class<?> targetClass = object.getClass();
        while (targetClass != Object.class) {
            try {
                field= targetClass.getDeclaredField(fieldName);
                break;
            } catch (NoSuchFieldException e) {
                //没有就找父类，cglib代理经常会用到这里
                targetClass = targetClass.getSuperclass();
            }
        }
        if (field == null) {
            throw new RuntimeException("Can not find field " + fieldName + " in object " + object);
        }
        field.setAccessible(true);
        return field.getType();
    }

}
