package com.asterisk.opensource.monitor.config;

import com.asterisk.opensource.monitor.helper.SnowFlakeIDHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author donghao
 * @Date 17/1/26
 * @Time 上午11:02
 */
@Configuration
public class IdConfiguration {

    @Value("${sfid.worker.id:1}")
    private Long workerId;

    @Value("${sfid.dataCenter.id:0}")
    private Long dataCenterId;

    @Bean
    public SnowFlakeIDHelper idGenerator() {
        return new SnowFlakeIDHelper(workerId,dataCenterId);
    }

}
