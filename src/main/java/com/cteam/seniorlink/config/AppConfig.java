package com.cteam.seniorlink.config;

import com.cteam.seniorlink.utils.NumberUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public NumberUtils numberUtils(){
        return new NumberUtils();
    }
}
