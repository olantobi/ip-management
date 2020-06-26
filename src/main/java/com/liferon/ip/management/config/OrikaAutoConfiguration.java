package com.liferon.ip.management.config;

import com.liferon.ip.management.dto.AllocatedIpResponseDto;
import com.liferon.ip.management.model.AllocatedIpAddress;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrikaAutoConfiguration {
    @Bean
    public DefaultMapperFactory.MapperFactoryBuilder<?, ?> orikaMapperFactoryBuilder() {
        return new DefaultMapperFactory.Builder();
    }

    @Bean
    public MapperFactory orikaMapperFactory(DefaultMapperFactory.MapperFactoryBuilder<?, ?> orikaMapperFactoryBuilder) {
        return orikaMapperFactoryBuilder.build();
    }

    @Bean
    public MapperFacade orikaMapperFacade(MapperFactory orikaMapperFactory) {
        orikaMapperFactory.classMap(AllocatedIpAddress.class, AllocatedIpResponseDto.class)
                .fieldAToB("ipPool.id", "ipPoolId")
                .byDefault()
                .register();
        return orikaMapperFactory.getMapperFacade();
    }
}
