package com.microservice.gateway.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("AuthBypassFilter")
public class AuthBypassFilter extends AbstractGatewayFilterFactory<AuthBypassFilter.Config> {

    private static final Logger log = LoggerFactory.getLogger(AuthBypassFilter.class);

    @Value("${gateway.internal-secret}")
    private String internalSecret;

    public AuthBypassFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            List<String> internalSecretHeaders = request.getHeaders().get("X-Internal-Secret");

            if (!internalSecretHeaders.isEmpty()) {
                String providedSecret = internalSecretHeaders.getFirst();
                if (internalSecret.equals(providedSecret)) {
                    log.info("Internal access granted for path: {}", request.getPath());

                    ServerHttpRequest mutatedRequest = request.mutate()
                            .headers(httpHeaders -> httpHeaders.remove("X-Internal-Secret"))
                            .build();

                    return chain.filter(exchange.mutate().request(mutatedRequest).build());
                }
            }

            log.warn("Unauthorized attempt to bypass authentication for path: {}", request.getPath());
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        };
    }

    public static class Config {

    }
}