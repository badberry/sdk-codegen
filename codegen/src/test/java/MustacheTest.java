import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.common.collect.Maps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by jackie on 16-3-30
 */
@RunWith(JUnit4.class)
public class MustacheTest {

    List<Item> items() {
        return Arrays.asList(
                new Item("Item 1", "$19.99", Arrays.asList(new Feature("New!"), new Feature("Awesome!"))),
                new Item("Item 2", "$29.99", Arrays.asList(new Feature("Old."), new Feature("Ugly.")))
        );
    }

    static class Item {
        Item(String name, String price, List<Feature> features) {
            this.name = name;
            this.price = price;
            this.features = features;
        }

        String name, price;
        List<Feature> features;
    }

    static class Feature {
        Feature(String description) {
            this.description = description;
        }

        String description;
    }

    @Test
    public void test_mustache() throws IOException {
        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache mustache = mf.compile("template.mustache");
        mustache.execute(new PrintWriter(System.out), new MustacheTest()).flush();
    }

    @Test
    public void test_string_mustache() throws IOException {
        String url = "/sample/{id}";
        url = url.replace("{", "{{").replace("}", "}}");
        System.out.println(url);
        Map map = Maps.newHashMap();
        map.put("id", "1");

        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache mustache = mf.compile(new StringReader(url), "url.template");
        StringWriter writer = new StringWriter();
        mustache.execute(writer, map).flush();
        System.out.println(writer.toString());
    }

    @Test
    public void test_spilt_package(){
        String packageName = "cn.cloudtop.sdk.sample.request";
        System.out.println(packageName);
        String[] dirs = packageName.split("\\.");
        System.out.println(dirs.length);
        for (String dir:dirs){
            System.out.println(dir);
        }
    }
}
