package wyxmttk.context;

public class ClasspathXmlApplicationContext extends AbstractXmlApplicationContext {
    private String[] configLocations;

    public ClasspathXmlApplicationContext(String[] configLocations) {
        this.configLocations = configLocations;
        refresh();
    }

    @Override
    protected String[] getConfigLocations() {
        //局部变量仅声明不会赋默认值
        return configLocations==null?new String[0]:configLocations;
    }

    public void setConfigLocations(String[] configLocations) {
        this.configLocations = configLocations;
    }
}
