package wyxmttk.beanDefinition;

import java.util.ArrayList;
import java.util.List;

public class PropertyValues {
    private final List<PropertyValue> propertyValueList = new ArrayList<>();

    public PropertyValue[] getPropertyValueList() {
        return propertyValueList.toArray(new PropertyValue[0]);
    }

    public void addPropertyValue(PropertyValue propertyValue) {
        //遍历集合时若要修改集合得用迭代器
        propertyValueList.removeIf(pv -> pv.getName().equals(propertyValue.getName()));
        propertyValueList.add(propertyValue);
    }

    public PropertyValue getPropertyValue(String name) {
        for (PropertyValue propertyValue : propertyValueList) {
            if (propertyValue.getName().equals(name)) {
                return propertyValue;
            }
        }
        return null;
    }
}
