import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import top.xlet.sdk.codegen.define.PropertyInfo;
import top.xlet.sdk.codegen.define.ViewObjectClassDefine;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by jackie on 16-3-30
 */
@RunWith(JUnit4.class)
public class MustacheVoTest {

    @Test
    public void test_mustache_create_vo() throws IOException {

        ViewObjectClassDefine define = new ViewObjectClassDefine("cn.cloudtop.vo", "CityVo","城市信息",
                Lists.newArrayList(
                        new PropertyInfo("id", "long", "唯一性标识"),
                        new PropertyInfo("name", "String", "名称"),
                        new PropertyInfo("provinceId", "long", "省份id", true)
                ));

        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache mustache = mf.compile("vo.mustache");
        mustache.execute(new FileWriter(define.getClassName() + ".java"), define).flush();
    }
}
