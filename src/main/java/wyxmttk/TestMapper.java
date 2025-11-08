package wyxmttk;


import wyxmttk.annotation.Component;
import wyxmttk.annotation.Qualifier;

@Component
@Qualifier("fake")
public class TestMapper extends Mapper {

    public void test() {
        System.out.println("fake mapper test");
    }

}
