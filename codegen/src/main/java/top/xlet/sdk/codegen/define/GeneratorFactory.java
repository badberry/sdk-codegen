package top.xlet.sdk.codegen.define;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 *
 */
public class GeneratorFactory {

    public static final Map<String, Generator> generatorMap = Maps.newHashMap();

    static {
        generatorMap.put("JavaGenerator", new JavaGenerator());
    }

    public static Generator get(String language) {
        String key = language + "Generator";
        if (generatorMap.containsKey(key)) {
            return generatorMap.get(key);
        } else {
            throw new RuntimeException("not support language:" + language);
        }
    }
}
