package com.alibaba.csp.sentinel.dashboard.rule.nacos;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author fly
 */
@Setter
@Getter
@Accessors(chain = true)
@ConfigurationProperties(prefix = "sentinel.nacos")
public class NacosConfigProperties {

    private String serverAddr;
    private String namespace;
    private String group;
    private String username;
    private String password;

}
