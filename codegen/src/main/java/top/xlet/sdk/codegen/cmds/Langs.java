package top.xlet.sdk.codegen.cmds;

import com.google.common.collect.Lists;
import io.airlift.airline.Command;

import java.util.List;

/**
 * Created by jackie on 16-3-31
 */
@Command(name = "langs", description = "Shows available langs")
public class Langs implements Runnable {
    @Override
    public void run() {
        List<String> supportLanguages = Lists.newArrayList("Java", "Object-C");
        System.out.printf("Available languages: %s%n", supportLanguages);
    }
}
