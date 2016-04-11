package top.xlet.sdk.codegen.define;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 *
 */
public class GeneratorFactory {

    public static final Map<String, Generator> generatorMap = Maps.newHashMap();

    static {
        generatorMap.put("JAVA", new JavaGenerator());
    }

    public static Generator get(String language) {
        if (generatorMap.containsKey(language)) {
            return generatorMap.get(language);
        } else {
            throw new RuntimeException("not support language:" + language.toLowerCase());
        }
    }
}
