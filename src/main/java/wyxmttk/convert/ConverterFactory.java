package wyxmttk.convert;

public interface ConverterFactory <S,F>{

    <T extends F> Converter<S,T> getConverter(Class<T> targetType);

}
