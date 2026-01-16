package com.microservice.gateway.gateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

@Component("AuthenticationFilter")
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            String accessToken = getAccessToken(request);

            if (accessToken == null || accessToken.isEmpty()) {
                log.warn("Unauthorized access: Access token missing or invalid in cookie for path: {}", request.getPath());
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }

            try {
                Claims claims = Jwts.parser()
                        .verifyWith(getSigningKey())
                        .build()
                        .parseSignedClaims(accessToken)
                        .getPayload();

                ServerHttpRequest mutatedRequest = request.mutate()
                        .header("X-Auth-UserId", Objects.toString(claims.get("userId"), ""))
                        .header("X-Auth-UserEmail", Objects.toString(claims.get("email"), ""))
                        .header("X-Auth-UserVerified", Objects.toString(claims.get("verified"), "false"))
                        .build();

                log.info("Authenticated request for user: {} ({}) to path: {}", claims.get("email"), claims.get("userId"), request.getPath());
                return chain.filter(exchange.mutate().request(mutatedRequest).build());

            } catch (SignatureException ex) {
                log.error("Invalid JWT signature for path {}: {}", request.getPath(), ex.getMessage());
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            } catch (MalformedJwtException ex) {
                log.error("Invalid JWT token for path {}: {}", request.getPath(), ex.getMessage());
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            } catch (ExpiredJwtException ex) {
                log.error("Expired JWT token for path {}: {}", request.getPath(), ex.getMessage());
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            } catch (UnsupportedJwtException ex) {
                log.error("Unsupported JWT token for path {}: {}", request.getPath(), ex.getMessage());
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            } catch (IllegalArgumentException ex) {
                log.error("JWT claims string is empty for path {}: {}", request.getPath(), ex.getMessage());
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            } catch (Exception e) {
                log.error("Unexpected JWT validation error for path {}: {}", request.getPath(), e.getMessage());
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }
        };
    }

    private static String getAccessToken(ServerHttpRequest request) {
        String accessToken = null;
        MultiValueMap<String, HttpCookie> cookies = request.getCookies();
        List<HttpCookie> accessTokenCookies = cookies.get("accessToken");

        if (accessTokenCookies != null && !accessTokenCookies.isEmpty()) {
            accessToken = accessTokenCookies.getFirst().getValue();
        }
        return accessToken;
    }

    public static class Config {

    }
}