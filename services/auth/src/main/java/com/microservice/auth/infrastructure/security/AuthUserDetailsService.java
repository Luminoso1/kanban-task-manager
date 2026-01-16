package com.microservice.auth.infrastructure.security;

import com.microservice.auth.application.dto.http.UserResponse;
import com.microservice.auth.application.port.out.UserClientRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthUserDetailsService implements UserDetailsService {

    private final UserClientRepository userClientRepository;

    public AuthUserDetailsService(UserClientRepository userClientRepository) {
        this.userClientRepository = userClientRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserResponse user = this.userClientRepository.getUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        return new User(
                user.email(),
                user.password(),
                Collections.emptyList()
        );
    }
}
