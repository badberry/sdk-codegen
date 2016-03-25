package top.xlet.k.codegen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * code gen app.
 */
@SpringBootApplication
@EnableAutoConfiguration
public class App implements CommandLineRunner {

    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .bannerMode(Banner.Mode.OFF)
                .sources(App.class)
                .run(args);
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    @Override
    public void run(String... strings) throws Exception {
        LOGGER.info("start gen sdk code");
    }
}
