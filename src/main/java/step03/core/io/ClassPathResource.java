package step03.core.io;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ClassUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ClassPathResource implements Resource {
    private String path;

    private ClassLoader classLoader;

    public ClassPathResource(String path, ClassLoader classLoader) {
        if (path == null) {
            throw new NullPointerException("path is null");
        }
        this.path = path;
        this.classLoader = classLoader;
    }
    public ClassPathResource(String path) {
        if (path == null) {
            throw new NullPointerException("path is null");
        }
        this.path = path;
        //先拿线程上下文类加载器，没有则用ClassUtil类的类加载器，再没有就用系统类加载器
        this.classLoader = ClassUtil.getClassLoader();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        InputStream resourceAsStream = classLoader.getResourceAsStream(path);
        if (resourceAsStream == null) {
            throw new FileNotFoundException(path);
        }
        return resourceAsStream;
    }
}
