package com.alibaba.csp.sentinel.dashboard.config;

import org.influxdb.InfluxDB;
import org.springframework.boot.autoconfigure.influx.InfluxDbCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author fly
 */
@Configuration
public class InfluxDbConfig {

    @Bean
    public InfluxDbCustomizer influxDbCustomizer() {
        return influxDb -> influxDb.setDatabase("sentinel")
                .enableBatch(100, 2000, TimeUnit.MILLISECONDS)
                .setRetentionPolicy("autogen")
                .setLogLevel(InfluxDB.LogLevel.BASIC);
    }
}
