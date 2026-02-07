package com.bhnatiuk.uni.bookstore.backend.config.exception;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExceptionMapperConfig {

    @Bean
    public ExceptionMapper<ErrorDescriptor> exceptionMapper() {
        return new DescriptorExceptionMapper();
    }
}
