package wyxmttk.core.io;

import java.net.MalformedURLException;
import java.net.URL;

public class DefaultResourceLoader implements ResourceLoader {
    @Override
    public Resource getResource(String location) {
        if(location==null){
            throw new NullPointerException("location is null");
        }
        if(location.startsWith(CLASSPATH_URL_PREFIX)){
            return new ClassPathResource(location.substring(CLASSPATH_URL_PREFIX.length()));
        }
        URL url = null;
        try {
            url = new URL(location);
            return new UrlResource(url);
        } catch (MalformedURLException e) {
            System.out.println(url + " is not a valid URL");
            return new FileSystemResource(location);
        }
    }
}
