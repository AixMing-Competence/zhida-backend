package com.aixming.yudada.config;

import com.zhipu.oapi.ClientV4;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Duzeming
 * @since 2024-08-06 23:03:20
 */
@Configuration
@ConfigurationProperties(prefix = "ai")
@Data
public class AiConfig {

    private String apiSecretKey;
    
    @Bean
    public ClientV4 clientV4() {
        return new ClientV4.Builder(apiSecretKey).build();
    }
}
