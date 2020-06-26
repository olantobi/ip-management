package com.liferon.ip.management.config;

import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrikaAutoConfiguration {
    @Bean
    public DefaultMapperFactory.MapperFactoryBuilder<?, ?> orikaMapperFactoryBuilder() {
        return new DefaultMapperFactory.Builder();
    }
}
