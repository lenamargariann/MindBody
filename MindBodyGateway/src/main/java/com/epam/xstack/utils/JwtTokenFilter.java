package com.epam.xstack.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtTokenFilter implements GatewayFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final Map<String, HttpMethod> allowedUrls = Map.of(
            "/main-service-path/api/v1/user/login", HttpMethod.POST,
            "/main-service-path/api/v1/trainee", HttpMethod.POST,
            "/main-service-path/api/v1/trainer", HttpMethod.POST);


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String token = jwtTokenProvider.resolveToken(request);
        if (allowedUrls.entrySet().contains(Map.entry(request.getPath().value(), request.getMethod())))
            return chain.filter(exchange);
        try {
            if (token != null && jwtTokenProvider.validateToken(token)) {
                Authentication auth = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
                exchange.mutate().request(request.mutate().header("auth", token).build());
            } else {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
            SecurityContextHolder.clearContext();
            return this.onError(exchange);
        }
        return chain.filter(exchange);
    }

    private Mono<Void> onError(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }
}
