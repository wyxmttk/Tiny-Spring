package wyxmttk.convert;

public interface Converter<S,T> {

    T convert(S source) throws Exception;

}
