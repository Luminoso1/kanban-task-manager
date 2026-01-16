package com.microservice.auth.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.auth.application.dto.http.ErrorResponse;
import com.microservice.auth.application.exception.InvalidCredentialsException;
import com.microservice.auth.application.exception.UserCreationException;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;


@Configuration
public class FeignClientConfig {

    private static final Logger log = LoggerFactory.getLogger(FeignClientConfig.class);

    @Bean
    public ErrorDecoder errorDecoder(){
        return (methodKey, response) -> {
            String requestUrl = response.request().url();
            String defaultErrorMessage = String.format("Error during Feign call to %s. Status: %d", requestUrl, response.status());
            ErrorResponse userErrorResponse = null;

            if (response.body() != null) {
                try (InputStream bodyIs = response.body().asInputStream()) {
                    String responseBody = new String(bodyIs.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        userErrorResponse = mapper.readValue(responseBody, ErrorResponse.class);
                    } catch (Exception e) {
                        log.warn("Could not parse error response body from {}: {}. Reason: {}", requestUrl, responseBody, e.getMessage());
                    }
                } catch (IOException e) {
                    log.error("Failed to read error response body from {}", requestUrl, e);
                }
            }

            String actualErrorMessage = (userErrorResponse != null) ? userErrorResponse.message() : defaultErrorMessage;

            if (response.status() == HttpStatus.CONFLICT.value()) { // HTTP 409
                return new UserCreationException(actualErrorMessage);
            } else if (response.status() == HttpStatus.BAD_REQUEST.value()) { // HTTP 400
                if (actualErrorMessage.toLowerCase().contains("not found")) {
                    return new InvalidCredentialsException(actualErrorMessage);
                }
            } else if (response.status() == HttpStatus.UNAUTHORIZED.value()) { // HTTP 401
                return new InvalidCredentialsException(actualErrorMessage);
            } else if (response.status() == HttpStatus.FORBIDDEN.value()) { // HTTP 403
                return new SecurityException(actualErrorMessage);
            } else if (response.status() == HttpStatus.NOT_FOUND.value()) {
                return new IllegalArgumentException("Resource not found in user service: " + actualErrorMessage);
            } else if (response.status() >= 500) {
                return new RuntimeException("User service internal error: " + actualErrorMessage);
            }

            return new RuntimeException("Unexpected error from user service: " + actualErrorMessage);
        };
    }
}
