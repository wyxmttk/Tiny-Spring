package wyxmttk;

import wyxmttk.annotation.Component;
import wyxmttk.convert.Converter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
@Component
public class MyDateConverter implements Converter<String, Date> {

    private final DateFormat dateFormat;

    public MyDateConverter() {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    @Override
    public Date convert(String source) throws Exception {
        return dateFormat.parse(source);
    }
}
