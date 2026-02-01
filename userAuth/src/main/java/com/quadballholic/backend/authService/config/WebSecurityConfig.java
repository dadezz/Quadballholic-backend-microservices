package com.quadballholic.backend.authService.config;


import com.quadballholic.backend.authService.filter.AuthTokenFilter;
import jakarta.servlet.DispatcherType;
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

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@EnableMethodSecurity

public class WebSecurityConfig {

    private final AuthenticationConfiguration authConfiguration;

    // Explicit constructor to handle the injection manually
    public WebSecurityConfig(AuthenticationConfiguration authConfiguration) {
        this.authConfiguration = authConfiguration;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                        .requestMatchers("/ws-quadball/**").permitAll()
                        .requestMatchers(
                                "/api/health",
                                "/api/test-user",
                                "/api/auth/**",
                                "/api/live-game-events/**",
                                "/api/teams/**",
                                "/api/tournaments/**",
                                "/api/players/**",
                                "api/stadium/**",
                                "/api/officials/**",
                                "/api/tournament-management"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .authenticationManager(authenticationManager())
                .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .formLogin(form -> form
                        .usernameParameter("email")
                        .disable()
                );

        return http.build();
    }

    @Bean
    AuditorAware<String> auditorProvider() {
        return new UsernameAuditorAware();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authConfiguration.getAuthenticationManager();
    }

}


