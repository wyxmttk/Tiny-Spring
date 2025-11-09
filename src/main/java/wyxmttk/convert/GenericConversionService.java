package wyxmttk.convert;

import wyxmttk.util.ClassUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GenericConversionService implements ConversionService,ConverterRegistry {

    private final Map<GenericConverter.ConvertiblePair,GenericConverter> converters=new HashMap<>();

    @Override
    public boolean canConvert(Class<?> sourceType, Class<?> targetType) {
        return getConverter(sourceType, targetType) != null;
    }

    public GenericConverter getConverter(Class<?> sourceType, Class<?> targetType) {
        while(sourceType!=null && targetType!=null) {
            GenericConverter genericConverter = doGetConverter(sourceType, targetType);
            if(genericConverter!=null) {
                return genericConverter;
            }
            sourceType=sourceType.getSuperclass();
            if(sourceType==null) {
                return null;
            }
            genericConverter = doGetConverter(sourceType, targetType);
            if(genericConverter!=null){
                return genericConverter;
            }
            targetType=targetType.getSuperclass();
        }
        return null;
    }
    private GenericConverter doGetConverter(Class<?> sourceType, Class<?> targetType) {
        GenericConverter.ConvertiblePair convertiblePair=new GenericConverter.ConvertiblePair(sourceType,targetType);
        return converters.get(convertiblePair);
    }

    @Override
    public <T> T convert(Object source, Class<T> targetType) {
        Class<?>  sourceType=source.getClass();

        GenericConverter converter=getConverter(sourceType, targetType);
        if(converter==null) {
            throw new IllegalArgumentException(String.format("Cannot convert %s to %s", source.getClass(), targetType));
        }

        return (T) converter.convert(source,sourceType,targetType);
    }

    @Override
    public void addConverter(Converter<?, ?> converter) {
        GenericConverter.ConvertiblePair convertiblePair = getConvertiblePair(converter.getClass());
        converters.put(convertiblePair,new ConverterAdapter(converter,convertiblePair));
    }

    @Override
    public void addConverter(GenericConverter converter) {
        converter.getConvertibleTypes().forEach(type->converters.put(type,converter));
    }

    @Override
    public void addConverterFactory(ConverterFactory<?, ?> factory) {
        GenericConverter.ConvertiblePair convertiblePair = getConvertiblePair(factory.getClass());
        converters.put(convertiblePair,new ConverterFactoryAdapter(factory,convertiblePair));
    }

    private GenericConverter.ConvertiblePair getConvertiblePair(Class<?> clazz) {

        Class<?>[] classes = ClassUtils.getParameterizedTypesOfGenericInterfaces(clazz);
        if(classes.length==0){
            throw new IllegalArgumentException(String.format("Cannot convert %s to %s", clazz, clazz.getSimpleName()));
        }
        return new GenericConverter.ConvertiblePair(classes[0], classes[1]);
    }

    private static final class ConverterAdapter implements GenericConverter {

        private final Converter<Object,Object> converter;

        private final Set<GenericConverter.ConvertiblePair> convertiblePairs;

        private ConverterAdapter(Converter<?,?> converter,ConvertiblePair convertiblePair) {
            this.converter = (Converter<Object, Object>) converter;
            convertiblePairs=Set.of(convertiblePair);
        }

        @Override
        public Set<ConvertiblePair> getConvertibleTypes() {
            return convertiblePairs;
        }

        @Override
        public Object convert(Object object, Class<?> sourceType, Class<?> targetType) {
            try {
                return converter.convert(object);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    private static final class ConverterFactoryAdapter implements GenericConverter {

        private final ConverterFactory<Object,Object> converterFactory;

        private final Set<GenericConverter.ConvertiblePair> convertiblePairs;

        public ConverterFactoryAdapter(ConverterFactory<?, ?> converter, ConvertiblePair convertiblePair) {
            this.converterFactory = (ConverterFactory<Object,Object>) converter;
            this.convertiblePairs = Set.of(convertiblePair);
        }

        @Override
        public Set<ConvertiblePair> getConvertibleTypes() {
            return convertiblePairs;
        }

        @Override
        public Object convert(Object object, Class<?> sourceType, Class<?> targetType) {
            try {
                return converterFactory.getConverter(targetType).convert(object);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
