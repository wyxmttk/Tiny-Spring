package wyxmttk.beanDefinition;

public class PropertyValue {

    //由于字段属于什么类在beanDefinition的class对象里已经记录，所以只需指明name-value
    private String name;
    private Object value;

    public PropertyValue(String name, Object value) {
        this.name = name;
        this.value = value;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Object getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }

}
