package com.quadballholic.backend.common.config;

import com.quadballholic.backend.common.filter.AuthEntryPointJwt;
import com.quadballholic.backend.common.filter.AuthTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;


import java.util.List;

@Configuration
@EnableWebSecurity
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@EnableMethodSecurity
@RequiredArgsConstructor
public class SharedSecurityConfig {

    private final AuthenticationConfiguration authConfiguration;
    private final AuthEntryPointJwt unauthorizedHandler;
    private final AuthTokenFilter authTokenFilter;

    @Value("${quadballholic.app.security.public-paths:}")
    private List<String> publicPaths;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable) // Microservices are stateless, CSRF not needed
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(unauthorizedHandler)
                )
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/actuator/**").permitAll();
                    auth.requestMatchers("/ws-quadball/**").permitAll();
                    auth.requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll();
                    String[] validPaths = publicPaths.stream()
                            .filter(StringUtils::hasText)
                            .map(String::trim)
                            .toArray(String[]::new);
                    if (validPaths.length > 0) {
                        auth.requestMatchers(validPaths).permitAll();
                    }
                    // C. Default Lock
                    auth.anyRequest().authenticated();
                })
                .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(form -> form
                        .usernameParameter("email")
                        .disable()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuditorAware<String> auditorProvider() {
        return new JwtAuditorAware();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authConfiguration.getAuthenticationManager();
    }
}
