package com.catholicFile.catholicFile.Configurations;


import com.catholicFile.catholicFile.services.CustomUserDetailsService;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;



    @Configuration
    @EnableWebSecurity
    @EnableMethodSecurity
    @SecurityScheme(name = SecurityConfigurations.SECURITY, type = SecuritySchemeType.HTTP,bearerFormat = "JWT", scheme = "bearer")

    public class SecurityConfigurations {
        private final JwtFilter jwtFilter;

        public static final String SECURITY = "bearerAuth";

        public SecurityConfigurations(JwtFilter jwtFilter) {
            this.jwtFilter = jwtFilter;
        }
         @Bean
        public AuthenticationManager authenticationManager(HttpSecurity http, CustomUserDetailsService userDetailsService) throws Exception {
            AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
            authBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
            return authBuilder.build();
    }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }


            @Bean
            public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            

                    http
                            .authorizeHttpRequests(auth -> auth
                                    // Swagger UI e JSON
                                    .requestMatchers(
                                            "/v3/api-docs/**",
                                            "/swagger-ui/**",
                                            "/swagger-ui.html"
                                    ).permitAll()

                                    .requestMatchers("/usuarios/login", "/usuarios").permitAll()
                                    .anyRequest().authenticated()
                            )
                            .csrf(csrf -> csrf.disable());

                    return http.build();
                }
        }




