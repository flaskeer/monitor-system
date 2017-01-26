package com.asterisk.opensource.monitor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

import static com.asterisk.opensource.monitor.constants.Constants.DEV_KEY;
import static com.asterisk.opensource.monitor.constants.Constants.PROD_KEY;

/**
 * @author donghao
 * @Date 17/1/26
 * @Time 上午11:27
 */
@SpringBootApplication
@Slf4j
public class MonitorSystemApplication {

    @Resource
    private Environment environment;

    @PostConstruct
    public void initApplication() {
        log.info("Running with Spring profile:{}", Arrays.toString(environment.getActiveProfiles()));
        List<String> activeProfiles = Arrays.asList(environment.getActiveProfiles());
        if (activeProfiles.contains(DEV_KEY) && activeProfiles.contains(PROD_KEY)) {
            log.error("You have misconfigured your application! It should not run " +
                    "with both the 'dev' and 'prod' profiles at the same time.");
        }

    }


    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(MonitorSystemApplication.class);
        Environment env = app.run(args).getEnvironment();
        log.info("\n----------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Local: \t\thttp://127.0.0.1:{}\n\t",
                env.getProperty("spring.application.name"),
                env.getProperty("server.port"));
    }


}
