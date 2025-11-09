package wyxmttk.convert;

import java.util.Objects;
import java.util.Set;

public interface GenericConverter {

    Set<ConvertiblePair> getConvertibleTypes();

    Object convert(Object object,Class<?> sourceType,Class<?> targetType);


    final class ConvertiblePair {
        private final Class<?> sourceType;
        private final Class<?> targetType;

        public ConvertiblePair(Class<?> sourceType, Class<?> targetType) {
            this.sourceType = sourceType;
            this.targetType = targetType;
        }

        public Class<?> getSourceType() {
            return sourceType;
        }

        public Class<?> getTargetType() {
            return targetType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if(!(o instanceof ConvertiblePair convertiblePair)) {
                return false;
            }
            return this.sourceType.equals(convertiblePair.sourceType) && this.targetType.equals(convertiblePair.targetType);
        }

        @Override
        public int hashCode() {
            return sourceType.hashCode() * 31 + targetType.hashCode();
        }
    }
}
