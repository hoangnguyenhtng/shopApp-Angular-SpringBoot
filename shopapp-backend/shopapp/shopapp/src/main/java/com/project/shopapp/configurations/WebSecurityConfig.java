package com.project.shopapp.configurations;

import com.project.shopapp.filters.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static jdk.nashorn.internal.runtime.PropertyDescriptor.GET;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    @Value("${api.prefix}")
    private String apiPrefix;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, JwtTokenFilter jwtTokenFilter) throws Exception{
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
        .authorizeHttpRequests(requests -> {
            requests
                    .requestMatchers(
                            String.format("%s/users/register", apiPrefix),
                            String.format("%s/users/login", apiPrefix)
                    )
                    .permitAll()
                    .requestMatchers(POST,
                            String.format("%s/orders", apiPrefix)).hasAnyRole("USER","ADMIN")
                    .requestMatchers(PUT,
                            String.format("%s/orders", apiPrefix)).hasRole("ADMIN")
                    .requestMatchers(DELETE,
                            String.format("%s/orders", apiPrefix)).hasRole("ADMIN")
                    .requestMatchers(GET,
                            String.format("%s/orders", apiPrefix)).hasAnyRole("USER","ADMIN")
                    .anyRequest().authenticated();

        });
        return httpSecurity.build();
    }
}
