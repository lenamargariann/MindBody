package com.example.mindbodysecuritymodule.config;

import com.example.mindbodysecuritymodule.utils.JwtTokenFilter;
import com.example.mindbodysecuritymodule.utils.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Configuration
@RequiredArgsConstructor
public class GatewayConfig {
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenFilter jwtTokenFilter;

//    @Bean
//    public GatewayFilter customGlobalFilter() {
//        return (exchange, chain) -> {
//            String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
//            if (token != null && token.startsWith("Bearer ")) {
//                String jwt = token.substring(7);
//                if (jwtTokenProvider.validateToken(jwt)) {
//                    Authentication auth = jwtTokenProvider.getAuthentication(jwt);
//                    return chain.filter(exchange)
//                            .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));
//                }
//            }
//            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//            return exchange.getResponse().setComplete();
//        };
//    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("training-service-path", r -> r
                        .path("/training-service-path/**")
                        .filters(f -> f.stripPrefix(1).filter(jwtTokenFilter)
                                .filter((exchange, chain) -> {
                                    System.out.println("Training-service-path: " + exchange.getRequest().getPath());
                                    return chain.filter(exchange);
                                })
                        )
                        .uri("lb://TRAINING-MINDBODY-SERVICE")
                )
                .route("main-service-path", r -> r
                        .path("/main-service-path/**")
                        .filters(f -> f.stripPrefix(1)
                                .filter((exchange, chain) -> {
                                    System.out.println("Main-service-path: " + exchange.getRequest().getPath());
                                    return chain.filter(exchange);
                                }))
                        .uri("lb://MAIN-MINDBODY-SERVICE")
                )
                .build();
    }

}
