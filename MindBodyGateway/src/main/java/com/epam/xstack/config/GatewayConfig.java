package com.epam.xstack.config;

import com.epam.xstack.utils.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GatewayConfig {
    private final JwtTokenFilter jwtTokenFilter;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("training-service-path", r -> r
                        .path("/training-service-path/**")
                        .filters(f -> f
                                .filter(jwtTokenFilter)
                                .stripPrefix(1)
                        )
                        .uri("lb://TRAINING-MINDBODY-SERVICE")
                )
                .route("main-service-path", r -> r
                        .path("/main-service-path/**")
                        .filters(f -> f
                                .filter(jwtTokenFilter)
                                .stripPrefix(1))
                        .uri("lb://MAIN-MINDBODY-SERVICE")
                )
                .build();
    }

}
