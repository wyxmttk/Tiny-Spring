package wyxmttk.convert;

import wyxmttk.annotation.Component;

@Component
public class DefaultConversionService extends GenericConversionService {

    public DefaultConversionService() {
        addDefaultConverter();
    }
    public void addDefaultConverter() {
        this.addConverterFactory(new StringToNumberConverterFactory());
    }
}
