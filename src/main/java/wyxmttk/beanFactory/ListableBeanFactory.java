package wyxmttk.beanFactory;

import java.util.Map;

//拓展批量获取Bean的功能
public interface ListableBeanFactory extends BeanFactory {
    <T> Map<String,T> getBeansOfType(Class<T> type);

    String[] getBeanDefinitionNames();
}
