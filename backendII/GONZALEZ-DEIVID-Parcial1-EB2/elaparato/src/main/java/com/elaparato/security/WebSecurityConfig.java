package com.elaparato.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

  public static final String ADMIN = "admin";
  public static final String VENDEDOR = "vendedor";
  public static final String REPOSITOR = "repositor";



  private final JwtAuthConverter jwtAuthConverter;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(authorizeHttpRequests ->
      authorizeHttpRequests
      .requestMatchers("/productos/**").hasAnyRole(REPOSITOR, ADMIN)
      .requestMatchers("/ventas/**").hasAnyRole(VENDEDOR, ADMIN)
      .anyRequest().authenticated()
    );
    http.oauth2ResourceServer(oauth2ResourceServer -> 
      oauth2ResourceServer
      .jwt(jwt -> jwt
        .jwtAuthenticationConverter(jwtAuthConverter)
      ));

    http.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    return http.build();
  }

    @Bean
  public JwtDecoder jwtDecoder() {
    return NimbusJwtDecoder.withJwkSetUri("http://localhost:8080/realms/el-aparato-deivid-gonzalez/protocol/openid-connect/certs").build();
  }
}

