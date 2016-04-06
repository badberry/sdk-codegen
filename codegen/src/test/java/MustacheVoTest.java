import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import top.xlet.sdk.codegen.define.GetterInfo;
import top.xlet.sdk.codegen.define.PojoInfo;
import top.xlet.sdk.codegen.define.PropertyInfo;
import top.xlet.sdk.codegen.define.ViewObjectClassDefine;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by jackie on 16-3-30
 */
@RunWith(JUnit4.class)
public class MustacheVoTest {

    @Test
    public void test_mustache_create_vo() throws IOException {

        ViewObjectClassDefine define = new ViewObjectClassDefine("cn.cloudtop.vo", "CityVo", "城市信息",
                Lists.newArrayList(
                        new PropertyInfo("id", "long", "唯一性标识"),
                        new PropertyInfo("name", "String", "名称"),
                        new PropertyInfo("provinceId", "long", "省份id", true)
                ));

        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache mustache = mf.compile("vo.mustache");
        StringWriter writer = new StringWriter();
        mustache.execute(writer, define).flush();
        System.out.println(writer.toString());
    }

    @Test
    public void test_property_toString() {
        PropertyInfo propertyInfo = new PropertyInfo("id", "long", "唯一性标识");
        System.out.println(propertyInfo);
    }

    @Test
    public void test_getter_toString() {
        PropertyInfo propertyInfo = new PropertyInfo("id", "long", "唯一性标识");
        GetterInfo getterInfo = new GetterInfo(propertyInfo);
        System.out.println(getterInfo);
    }

    @Test
    public void test_pojo_toString() {
//        PojoInfo pojoInfo = new PojoInfo("cn.cloudtop.sdk.sample", "SimpleVo", "例子", Lists.newArrayList(
//                new PropertyInfo("id", "long", "唯一性标识"),
//                new PropertyInfo("name", "String", "名称"),
//                new PropertyInfo("provinceId", "long", "省份id", true)
//        ));
//        System.out.println(pojoInfo);
    }

    @Test
    public void test_view_object_toString() {
        ViewObjectClassDefine define = new ViewObjectClassDefine("cn.cloudtop.vo", "CityVo", "城市信息",
                Lists.newArrayList(
                        new PropertyInfo("id", "long", "唯一性标识"),
                        new PropertyInfo("name", "String", "名称"),
                        new PropertyInfo("provinceId", "long", "省份id", true)
                ));
        System.out.println(define);
    }
}
