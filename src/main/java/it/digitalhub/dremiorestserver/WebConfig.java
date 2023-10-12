package it.digitalhub.dremiorestserver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.ReactivePageableHandlerMethodArgumentResolver;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;

@Configuration
class WebConfig {

    @Bean
    HandlerMethodArgumentResolver reactivePageableHandlerMethodArgumentResolver() {
        return new ReactivePageableHandlerMethodArgumentResolver();
    }
}
