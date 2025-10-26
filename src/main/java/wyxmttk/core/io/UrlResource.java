package wyxmttk.core.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class UrlResource implements Resource {
    private URL url;

    public  UrlResource(URL url) {
        if (url == null) {
            throw new NullPointerException("url == null");
        }
        this.url = url;
    }

    @Override
    public InputStream getInputStream(){
        URLConnection urlConnection=null;
        try {
            urlConnection = url.openConnection();
            return urlConnection.getInputStream();
        } catch (IOException e) {
            if(urlConnection instanceof HttpURLConnection){
                ((HttpURLConnection) urlConnection).disconnect();
            }
            throw new RuntimeException(e);
        }
    }
}
