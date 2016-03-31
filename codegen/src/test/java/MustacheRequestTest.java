import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import top.xlet.sdk.codegen.define.*;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by jackie on 16-3-31
 */
@RunWith(JUnit4.class)
public class MustacheRequestTest {

    @Test
    public void test_request_create() throws IOException {

        ViewObjectClassDefine define = new ViewObjectClassDefine("cn.cloudtop.vo", "CityVo", "城市信息",
                Lists.newArrayList(
                        new PropertyInfo("id", "long", "唯一性标识"),
                        new PropertyInfo("name", "String", "名称"),
                        new PropertyInfo("provinceId", "long", "省份id", true)
                ));
        ResponseClassDefine response = new ResponseClassDefine("cn.cloudtop.response",
                "CityGetResponse", "获取指定id城市信息",
                Lists.newArrayList(
                        new PropertyInfo("cityVo", "CityVo", "城市信息", define, true)
                )
        );

        PropertyInfo idProperty = new PropertyInfo("id", "long", "唯一性标识", true);
        RequestClassDefine request = new RequestClassDefine(
                "cn.cloudtop.request", "CityGetRequest", "获取城市详细请求定义",
                Lists.newArrayList(idProperty), "GET", "/sample/{{id}}", Lists.newArrayList(
                new ParamInfo("id", idProperty)), Lists.newArrayList(
                new ParamInfo("id", idProperty)), response
        );

        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache mustache = mf.compile("request.mustache");
        mustache.execute(new FileWriter(request.getClassName() + "Url.java"), request).flush();
    }

    @Test
    public void test_create_request_with_simple_url() throws IOException {
        ViewObjectClassDefine define = new ViewObjectClassDefine("cn.cloudtop.vo", "CityVo", "城市信息",
                Lists.newArrayList(
                        new PropertyInfo("id", "long", "唯一性标识"),
                        new PropertyInfo("name", "String", "名称"),
                        new PropertyInfo("provinceId", "long", "省份id", true)
                ));
        ResponseClassDefine response = new ResponseClassDefine("cn.cloudtop.response",
                "CityGetResponse", "获取指定id城市信息",
                Lists.newArrayList(
                        new PropertyInfo("cityVo", "CityVo", "城市信息", define, true)
                )
        );

        RequestClassDefine request = new RequestClassDefine(
                "cn.cloudtop.request", "CityGetRequest", "获取城市详细请求定义",
                null, "GET", "/sample", null, null, response
        );

        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache mustache = mf.compile("request.mustache");
        mustache.execute(new FileWriter(request.getClassName() + "Simple.java"), request).flush();
    }
}
