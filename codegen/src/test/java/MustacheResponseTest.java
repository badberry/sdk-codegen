import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import top.xlet.sdk.codegen.define.PropertyInfo;
import top.xlet.sdk.codegen.define.ResponseClassDefine;
import top.xlet.sdk.codegen.define.ViewObjectClassDefine;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by jackie on 16-3-31
 */
@RunWith(JUnit4.class)
public class MustacheResponseTest {

    @Test
    public void test_response_create() throws IOException {
        ViewObjectClassDefine define = new ViewObjectClassDefine("cn.cloudtop.vo", "CityVo","城市信息",
                Lists.newArrayList(
                        new PropertyInfo("id", "long", "唯一性标识"),
                        new PropertyInfo("name", "String", "名称"),
                        new PropertyInfo("provinceId", "long", "省份id", true)
                ));
        ResponseClassDefine response = new ResponseClassDefine("cn.cloudtop.response",
                "CityGetResponse","获取指定id城市信息",
                Lists.newArrayList(
                        new PropertyInfo("cityVo", "CityVo", "城市信息", define, true)
                )
        );

        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache mustache = mf.compile("response.mustache");
        mustache.execute(new FileWriter(response.getClassName() + ".java"), response).flush();
    }

}
