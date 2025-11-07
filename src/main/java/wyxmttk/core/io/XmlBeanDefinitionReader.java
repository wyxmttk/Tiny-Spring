package wyxmttk.core.io;

import cn.hutool.core.bean.BeanException;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import wyxmttk.annotation.ClassPathBeanDefinitionScanner;
import wyxmttk.beanDefinition.*;

import java.io.IOException;
import java.io.InputStream;

public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {

    private ClassPathBeanDefinitionScanner scanner;

    public XmlBeanDefinitionReader(BeanDefinitionRegistry beanDefinitionRegistry) {
        super(beanDefinitionRegistry);
        this.scanner = new ClassPathBeanDefinitionScanner(beanDefinitionRegistry);
    }

    public XmlBeanDefinitionReader(BeanDefinitionRegistry beanDefinitionRegistry, ResourceLoader resourceLoader) {
        super(beanDefinitionRegistry, resourceLoader);
        this.scanner = new ClassPathBeanDefinitionScanner(beanDefinitionRegistry);
    }

    @Override
    public void loadBeanDefinition(Resource resource) {
        try(InputStream inputStream = resource.getInputStream())
        {
            doLoadBeanDefinitions(inputStream);
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void loadBeanDefinition(String location) {
        Resource resource = getResourceLoader().getResource(location);
        try(InputStream inputStream = resource.getInputStream())
        {
            doLoadBeanDefinitions(inputStream);
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void loadBeanDefinitions(Resource... resources) {
        for (Resource resource:resources){
            loadBeanDefinition(resource);
        }
    }
    protected void doLoadBeanDefinitions(InputStream inputStream) throws ClassNotFoundException {
        Document document = XmlUtil.readXML(inputStream);
        Element root = document.getDocumentElement();
        NodeList nodes = root.getChildNodes();

        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);

            if(!(node instanceof Element bean)){
                continue;
            }
            String nodeName = bean.getNodeName();
            if(!"bean".equals(nodeName)) {
                if(!"component-scan".equals(nodeName)) {
                    continue;
                }
                String attribute = bean.getAttribute("base-packages");
                if(StrUtil.isBlank(attribute)) {
                    throw new IllegalArgumentException("base-packages attribute is required");
                }
                String[] packageArr = attribute.split(",");
                for(String packageName:packageArr) {
                    scanner.doScan(packageName);
                }
                continue;
            }
            //解析<bean>标签
            String id = bean.getAttribute("id");
            String name = bean.getAttribute("name");
            String className = bean.getAttribute("class");
            String initMethod = bean.getAttribute("init-method");
            String destroyMethod = bean.getAttribute("destroy-method");
            String scope = bean.getAttribute("scope");


            //
            Class<?> clazz = Class.forName(className);

            String beanName = getBeanName(id,name,className);


            BeanDefinition beanDefinition = new BeanDefinition(clazz);

            if(!StrUtil.isBlank(initMethod)) {
                beanDefinition.setInitMethodName(initMethod);
            }
            if(!StrUtil.isBlank(destroyMethod)) {
                beanDefinition.setDestroyMethodName(destroyMethod);
            }

            if(!StrUtil.isBlank(scope)) {
                beanDefinition.setScope(scope);
            }

            PropertyValues propertyValues = beanDefinition.getPropertyValues();
            NodeList properties = bean.getChildNodes();
            for(int j = 0; j < properties.getLength(); j++){
                Node item = properties.item(j);
                if(!(item instanceof Element property)){
                    continue;
                }
                if(!"property".equals(property.getNodeName())) {
                    continue;
                }
                String propertyName = property.getAttribute("name");
                String propertyValue = property.getAttribute("value");
                //需要自动注入的字段的名字
                String propertyRef = property.getAttribute("ref");

                PropertyValue pV = new PropertyValue(propertyName,
                        StrUtil.isBlank(propertyRef)?propertyValue:new DefaultBeanReference(propertyRef));
                propertyValues.addPropertyValue(pV);
            }
            BeanDefinitionRegistry registry = getRegistry();
            if(registry.containsBeanDefinition(beanName)) {
                throw new BeanException("Duplicate beanName[" + beanName + "] is not allowed");
            }
            registry.registerBeanDefinition(beanName, beanDefinition);
        }

    }
    protected String getBeanName(String id,String name,String className) {
        if(StrUtil.isNotBlank(id)) {
            return id;
        } else if (StrUtil.isNotBlank(name)) {
            return name;
        }else {
            return StrUtil.lowerFirst(className);
        }
    }


}
